package boraszat.model;

public enum Munkakör {
    PM("Pincemunkás"), TKÓ("Takarító"), BR("Borász"), TKR("Titkár"), BŐ("Biztonsági őr");
    
    private final String displayName;

    Munkakör(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() { 
        return displayName;
    }

    // Optionally and/or additionally, toString.
    @Override public String toString() { 
        return displayName; 
    }
}
