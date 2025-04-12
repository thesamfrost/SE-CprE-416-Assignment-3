/**
 * Guitar class that extends Instrument.
 * Demonstrates method overriding for polymorphism.
 */
public class Guitar extends Instrument {

    public Guitar(String name) {
        super(name);
    }

    /**
     * Overridden play() method to simulate a guitar playing.
     */
    @Override
    public void play() {
        System.out.println(getName() + " is strumming chords.");
    }
}
