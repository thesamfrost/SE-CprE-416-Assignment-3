/**
 * Abstract base class representing a musical instrument.
 * Implements core functionality such as volume adjustment.
 */
public abstract class Instrument {
    
    // Marking fields as private to enforce encapsulation.
    private String name;
    private int volume;

    public Instrument(String name) {
        this.name = name;
        this.volume = 5; // default volume
    }
    
    /**
     * Returns the instrument's name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the current volume.
     */
    public int getVolume() {
        return volume;
    }
    
    /**
     * Abstract method to be overridden by subclasses for playing the instrument.
     */
    public abstract void play();
    
    /**
     * Adjusts the volume of the instrument using a primitive int value.
     * Demonstrates data coupling.
     * 
     * @param newVolume the new volume level
     */
    public void adjustVolume(int newVolume) {
        updateVolume(newVolume);
    }
    
    /**
     * Helper method to update the volume.
     * 
     * @param newVolume the new volume level
     */
    private void updateVolume(int newVolume) {
        this.volume = newVolume;
        System.out.println(getName() + " volume set to " + volume);
    }
}
