/**
 * Song class represents a musical track.
 * (Duplicate definition removed to avoid redundancy.)
 */
public class Song {
    // Fields are kept package-private for simplicity in this toy example.
    public String title;
    public int duration; // in seconds

    public Song(String title, int duration) {
        this.title = title;
        this.duration = duration;
    }
}
