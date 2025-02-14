import java.util.Comparator;

public class Post implements Comparable<Post> {
    public String postId;
    private User author;
    private int likes = 0;

    public Post(String postId) {

        this.postId = postId;
    }
    public Post(String postId, User author) {
        this.postId = postId;
        this.author = author;
    }

    public boolean equals(Object object) {
        return object instanceof Post && postId.equals( ((Post)object).postId );
    }

    @Override
    public int compareTo(Post post){
        return postId.compareTo(post.postId);
//        return Comparator.comparingInt(Post::getLikes)
//                .thenComparing(Post::getPostId)
//                .compare(this, post);
    }

    public String getPostId() {
        return postId;
    }

    public int getLikes() {
        return likes;
    }
    public User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return postId;
    }

    public int hashCode() {
        return postId.hashCode();
    }

    public void liked() {
        likes++;
    }
    public void unliked() {
        likes--;
    }
}
