package boraszat.model;

public enum Tárolók {
    HORDÓ ("Hordó"), TARTÁLY ("Tartály");
    
    private final String displayName;

    Tárolók(String displayName) {
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
