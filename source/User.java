import java.util.ArrayList;

public class User implements Comparable<User> {

    public String userId; // Unique identifier for the user
    public AVL<Post> userPosts = new AVL<>(); // Tree containing posts created by the user
    protected AVL<User> followed = new AVL<>(); // Tree containing users followed by this user
    protected AVL<Post> seenPosts = new AVL<>(); // Tree containing posts seen by this user
    protected AVL<Post> likedPosts = new AVL<>(); // Tree containing posts liked by this user

    public User() {}

    public User(String userId) {
        this.userId = userId;
    }

    // Compare users by their userId
    @Override
    public int compareTo(User user) {
        return userId.compareTo(user.userId);
    }

    public boolean equals(Object object) {
        return object instanceof User && userId.equals(((User) object).userId);
    }

    private String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return userId;
    }

    // Hash code based on userId
    public int hashCode() {
        return userId.hashCode();
    }

    // Follow another user if not already followed
    public boolean Following(User user2) {
        if (followed.search(user2) == null) { // Check if user2 is not already followed
            followed.insert(user2);
            return true;
        }
        return false;
    }

    // Unfollow a user if currently followed
    public boolean Unfollowing(User user2) {
        if (followed.search(user2) == null) return false; // If not followed, do nothing
        followed.delete(user2);
        return true;
    }

    // Add a new post created by this user
    public void addPost(Post post) {
        userPosts.insert(post);
    }

    // Mark a post as seen by this user
    public void seePost(Post post) {
        seenPosts.insert(post);
    }

    // Insert all posts created by this user to another user's seen posts
    public void showedPostsTo(User user2) {
        userPosts.insertToTree(userPosts.getRoot(), user2.seenPosts);
    }

    // Like or unlike a post
    public int like(Post post) {
        seenPosts.insert(post); // Mark the post as seen
        if (likedPosts.search(post) == null) { // If not already liked
            likedPosts.insert(post);
            post.liked(); // Increment like count on the post
            return 1; // Indicate that the post was liked
        } else { // If already liked
            likedPosts.delete(post);
            post.unliked(); // Decrement like count on the post
            return 0; // Indicate that the like was removed
        }
    }

    // Get all posts created by this user as an ArrayList
    public ArrayList<Post> getPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        userPosts.insertToArray(userPosts.getRoot(), posts);
        return posts;
    }

    // Generate the feed by gathering posts from all followed users
    public ArrayList<Post> getFeed() {
        ArrayList<Post> feed = new ArrayList<>();
        ArrayList<User> allFollowed = new ArrayList<>();
        followed.insertToArray(followed.getRoot(), allFollowed); // Get all followed users
        for (User user : allFollowed) {
            user.userPosts.insertToArray(user.userPosts.getRoot(), feed); // Add their posts to the feed
        }
        return feed;
    }
}
