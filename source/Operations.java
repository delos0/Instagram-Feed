import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class Operations {

    // Insert a new user into the hash table if not already present
    static boolean CreateUser(HashTable<User> users, String userId) {
        User user = new User(userId);
        return users.insert(user);
    }

    // Follow or unfollow a user based on the command
    static boolean Follow(HashTable<User> users, String user1_id, String user2_id, String command) {
        User user1 = new User(user1_id);
        User user2 = new User(user2_id);

        // Cannot follow/unfollow oneself
        if (Objects.equals(user1_id, user2_id)) return false;

        // Ensure both users exist in the hash table
        if (!users.contains(user1) || !users.contains(user2)) return false;

        user1 = users.get(user1);
        user2 = users.get(user2);

        // Execute follow or unfollow based on command
        if (Objects.equals(command, "follow_user")) return user1.Following(user2);
        if (Objects.equals(command, "unfollow_user")) return user1.Unfollowing(user2);
        return false;
    }

    // Create a new post and associate it with a user if both are valid
    static boolean CreatePost(HashTable<Post> posts, String postId, HashTable<User> users, String userId) {
        User user = new User(userId);
        Post post = new Post(postId, user);

        // Check if user exists and post does not already exist
        if (!users.contains(user) || posts.contains(post)) return false;

        user = users.get(user);
        posts.insert(post);  // Add post to hash table
        user.addPost(post);  // Associate post with user
        return true;
    }

    // Mark a specific post as seen by a user
    static boolean SeePost(HashTable<Post> posts, String postId, HashTable<User> users, String userId) {
        User user = new User(userId);
        Post post = new Post(postId);

        // Check if both user and post exist
        if (!users.contains(user) || !posts.contains(post)) return false;

        user = users.get(user);
        post = posts.get(post);
        user.seePost(post); // Add the post to the user's seen posts
        return true;
    }

    // Allow one user to see all posts of another user
    static boolean SeeAllPosts(HashTable<User> users, String user1_id, String user2_id) {
        User user1 = new User(user1_id);
        User user2 = new User(user2_id);

        // Ensure both users exist
        if (!users.contains(user1) || !users.contains(user2)) return false;

        user1 = users.get(user1);
        user2 = users.get(user2);

        // Add all posts of user2 to user1's seen posts
        user2.showedPostsTo(user1);
        return true;
    }

    // Like or unlike a post and return the status (1 for like, 0 for unlike, -1 for error)
    static int LikePost(HashTable<Post> posts, String postId, HashTable<User> users, String userId) {
        User user = new User(userId);
        Post post = new Post(postId);

        // Check if both user and post exist
        if (!users.contains(user) || !posts.contains(post)) return -1;

        user = users.get(user);
        post = posts.get(post);

        // Like or unlike the post
        return user.like(post);
    }

    // Generate and log the user's feed to the output stream
    static void LogFeed(HashTable<User> users, String userId, int size, PrintStream output) {
        User user = new User(userId);

        // Ensure the user exists
        if (!users.contains(user)) {
            output.println("Some error occurred in generate_feed.");
            return;
        }

        user = users.get(user);
        output.println("Feed for " + userId + ":");

        ArrayList<Post> feed = user.getFeed(); // Retrieve the user's feed
        Comparator<Post> compareByLikes = Comparator.comparingInt(Post::getLikes).thenComparing(Post::getPostId);
        BinaryHeap<Post> maxPosts = new BinaryHeap<>(feed, compareByLikes);

        // Display up to the specified number of unseen posts
        int heapSize = feed.size();
        int i = 0, count = 0;
        while (i < heapSize) {
            if (count == size) break;
            Post max = maxPosts.deleteMax();
            if (user.seenPosts.search(max) == null) { // Skip already seen posts
                count++;
                output.println("Post ID: " + max.getPostId() + ", Author: " + max.getAuthor() + ", Likes: " + max.getLikes());
            }
            i++;
        }

        // Notify if fewer posts are available
        if (count < size) output.println("No more posts available for " + userId + ".");
    }

    // Simulate a user scrolling through their feed
    static void ScrollFeed(User user, int size, int[] liked, PrintStream output) {
        output.println(user.userId + " is scrolling through feed:");
        ArrayList<Post> feed = user.getFeed();
        Comparator<Post> compareByLikes = Comparator.comparingInt(Post::getLikes).thenComparing(Post::getPostId);
        BinaryHeap<Post> maxPosts = new BinaryHeap<>(feed, compareByLikes);

        // Scroll through and process posts
        int heapSize = maxPosts.getCurrentSize();
        int i = 0, count = 0;
        while (i < heapSize) {
            if (count == size) break;
            Post max = maxPosts.deleteMax();
            if (user.seenPosts.search(max) == null) {
                if (liked[count] == 0) {
                    user.seePost(max);
                    output.println(user.userId + " saw " + max.getPostId() + " while scrolling.");
                } else {
                    user.like(max);
                    output.println(user.userId + " saw " + max.getPostId() + " while scrolling and clicked the like button.");
                }
                count++;
            }
            i++;
        }

        // Notify if no more posts are available
        if (count < size) output.println("No more posts in feed.");
    }

    // Sort and display all posts created by the user based on likes
    static void SortPosts(User user, PrintStream output) {
        if (user.userPosts.isEmpty()) {
            output.println("No posts from " + user.userId + ".");
        } else {
            output.println("Sorting " + user.userId + "'s posts:");
            ArrayList<Post> posts = user.getPosts();
            Comparator<Post> compareByLikes = Comparator.comparingInt(Post::getLikes).thenComparing(Post::getPostId);
            BinaryHeap<Post> maxPosts = new BinaryHeap<>(posts, compareByLikes);

            // Display posts in sorted order
            for (int i = 0; i < posts.size(); i++) {
                Post max = maxPosts.deleteMax();
                output.println(max.getPostId() + ", Likes: " + max.getLikes());
            }
        }
    }
}
