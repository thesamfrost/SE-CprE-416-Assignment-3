/**
 * Piano class that extends Instrument and implements Recordable.
 * Demonstrates both method overriding and method overloading.
 */
public class Piano extends Instrument implements Recordable {

    public Piano(String name) {
        super(name);
    }

    /**
     * Overridden play() method.
     */
    @Override
    public void play() {
        System.out.println(getName() + " is playing a melody.");
    }

    /**
     * Overloaded play() method to play a specific song.
     *
     * @param songName the name of the song to be played
     */
    public void play(String songName) {
        System.out.println(getName() + " is playing the song: " + songName);
    }

    /**
     * Implementation of the record() method from the Recordable interface.
     */
    @Override
    public void record() {
        System.out.println(getName() + " recording started.");
    }
}
