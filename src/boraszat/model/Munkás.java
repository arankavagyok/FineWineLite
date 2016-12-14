package boraszat.model;

public class Munkás {
    
    String név;
    String szülDate;
    String anyaNév;
    Munkakör mk;    
    Időbeosztás időB;
    String munkaKezd;

    public Munkás(String név, String szülDate, String anyaNév, Munkakör mk, Időbeosztás időB, String munkaKezd) {
        this.név = név;
        this.szülDate = szülDate;
        this.anyaNév = anyaNév;
        this.mk = mk;
        this.időB = időB;
        this.munkaKezd = munkaKezd;
    }

    public String getNév() {
        return név;
    }

    public String getSzülDate() {
        return szülDate;
    }

    public String getAnyaNév() {
        return anyaNév;
    }

    public Munkakör getMk() {
        return mk;
    }

    public Időbeosztás getIdőB() {
        return időB;
    }

    public String getMunkaKezd() {
        return munkaKezd;
    }

    public void setNév(String név) {
        this.név = név;
    }

    public void setSzülDate(String szülDate) {
        this.szülDate = szülDate;
    }

    public void setAnyaNév(String anyaNév) {
        this.anyaNév = anyaNév;
    }

    public void setMk(Munkakör mk) {
        this.mk = mk;
    }

    public void setIdőB(Időbeosztás időB) {
        this.időB = időB;
    }

    public void setMunkaKezd(String munkaKezd) {
        this.munkaKezd = munkaKezd;
    }

    @Override
    public String toString() {
        return "Munk\u00e1s{" + "n\u00e9v=" + név + ", sz\u00fclDate=" + szülDate + ", anyaN\u00e9v=" + anyaNév + ", mk=" + mk + ", id\u0151B=" + időB + ", munkaKezd=" + munkaKezd + '}';
    }  
}
