package boraszat.model;

public enum Időbeosztás {
    N("Nappali"), É("Éjszakai"),H("Hétvégi");
    
    private final String displayName;

    Időbeosztás(String displayName) {
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
