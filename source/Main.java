import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Scanner;

public class Main extends Operations {
    public static void main(String[] args) {

        // Initialize hash tables for users and posts
        HashTable<User> users = new HashTable<>(1000000);
        HashTable<Post> posts = new HashTable<>(700000);

        // Read from console the input and output file
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        PrintStream output;
        try {
            output = new PrintStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        Scanner reader;
        try {
            reader = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find input file");
            return;
        }

        // Process each line & command one by one
        while (reader.hasNextLine()) {
            String line = reader.nextLine();
            String[] lineParts = line.split(" ");
            String command = lineParts[0];
            if (Objects.equals(command, "create_user")) {
                String userId = lineParts[1];
                boolean created = CreateUser(users, userId);
                if (created) output.println("Created user with Id " + userId + ".");
                else output.println("Some error occurred in create_user.");
            }
            if (Objects.equals(command, "follow_user")) {
                String user1_id = lineParts[1];
                String user2_id = lineParts[2];
                boolean followed = true;
                followed = Follow(users, user1_id, user2_id, command);
                if (followed) output.println(user1_id + " followed " + user2_id + ".");
                else output.println("Some error occurred in follow_user.");
            }
            if (Objects.equals(command, "unfollow_user")) {
                String user1 = lineParts[1];
                String user2 = lineParts[2];
                boolean unfollowed = Follow(users, user1, user2, command);
                if (unfollowed) output.println(user1 + " unfollowed " + user2 + ".");
                else output.println("Some error occurred in unfollow_user.");
            }
            if (Objects.equals(command, "create_post")) {
                String user = lineParts[1];
                String postId = lineParts[2];
                boolean created = CreatePost(posts, postId, users, user);
                if (created) output.println(user + " created a post with Id " + postId + '.');
                else output.println("Some error occurred in create_post.");
            }
            if (Objects.equals(command, "see_post")) {
                String user = lineParts[1];
                String postId = lineParts[2];
                boolean seen = SeePost(posts, postId, users, user);
                if (seen) output.println(user + " saw " + postId + '.');
                else output.println("Some error occurred in see_post.");
            }
            if (Objects.equals(command, "see_all_posts_from_user")) {
                String user1 = lineParts[1];
                String user2 = lineParts[2];
                boolean seenAll = SeeAllPosts(users, user1, user2);
                if (seenAll) output.println(user1 + " saw all posts of " + user2 + '.');
                else output.println("Some error occurred in see_all_posts_from_user.");
            }
            if (Objects.equals(command, "toggle_like")) {
                String user = lineParts[1];
                String postId = lineParts[2];
                int liked = LikePost(posts, postId, users, user);
                if (liked == 1) output.println(user + " liked " + postId + '.');
                else if (liked == 0) output.println(user + " unliked " + postId + '.');
                else output.println("Some error occurred in toggle_like.");
            }
            if (Objects.equals(command, "generate_feed")) {
                String user = lineParts[1];
                int size = Integer.parseInt(lineParts[2]);
                LogFeed(users, user, size, output);
            }
            if (Objects.equals(command, "scroll_through_feed")) {
                String userId = lineParts[1];
                int size = Integer.parseInt(lineParts[2]);
                int[] liked = new int[size];
                User user = new User(userId);
                if (!users.contains(user)) output.println("Some error occurred in scroll_through_feed.");
                else {
                    for (int i = 0; i < size; i++) {
                        liked[i] = Integer.parseInt(lineParts[3 + i]);
                    }
                    user = users.get(user);
                    ScrollFeed(user, size, liked, output);
                }
            }
            if(Objects.equals(command, "sort_posts")) {
                String userId = lineParts[1];
                User user = new User(userId);
                if(!users.contains(user)) output.println("Some error occurred in sort_posts.");
                else {
                    user = users.get(user);
                    SortPosts(user, output);
                }
            }


        }
    }

}