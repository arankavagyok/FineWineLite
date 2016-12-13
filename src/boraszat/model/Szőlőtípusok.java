package boraszat.model;

public enum Szőlőtípusok {
    CS ("Cabernet Sauvignon"), KFK ("Kékfrankos"), CSF ("Cserszegi Fűszeres"), BKV ("Bikavér"),;
    
     private final String displayName;

    Szőlőtípusok(String displayName) {
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
