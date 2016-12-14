/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boraszat.model;

public class KészBor{
    
    String név;
    Szőlőtípusok szőlőTípus;
    String évjárat;
    String cukor;
    double alkohol;
    double liter;
    int db;
    double veszteség;
    String érésKezd;
    String palackozásDate;
        
    public KészBor(String név, ÉrőBor éB, double alkohol, String cukor, String érés, String palackD) {
        this.név=név;
        szőlőTípus=éB.getSzőlőTípus();
        évjárat=éB.getÉvjárat();
        this.liter=éB.getLiter();
        this.alkohol=alkohol;
        this.cukor=cukor;
        this.érésKezd=érés;
        this.palackozásDate=palackD;
        setDB();
    }
    private void setDB(){
        db=(int) (liter/0.75);
        veszteség=liter%0.75;
    }

    public String getNév() {
        return név;
    }

    public void setNév(String név) {
        this.név = név;
    }

    public void setSzőlőTípus(Szőlőtípusok szőlőTípus) {
        this.szőlőTípus = szőlőTípus;
    }

    public void setÉvjárat(String évjárat) {
        this.évjárat = évjárat;
    }

    public void setCukor(String cukor) {
        this.cukor = cukor;
    }

    public void setAlkohol(double alkohol) {
        this.alkohol = alkohol;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }

    public void setDb(int db) {
        this.db = db;
    }

    public void setVeszteség(double veszteség) {
        this.veszteség = veszteség;
    }

    public Szőlőtípusok getSzőlőTípus() {
        return szőlőTípus;
    }

    public String getÉvjárat() {
        return évjárat;
    }

    public String getCukor() {
        return cukor;
    }

    public double getAlkohol() {
        return alkohol;
    }

    public double getLiter() {
        return liter;
    }

    public int getDb() {
        return db;
    }

    public String getÉrésKezd() {
        return érésKezd;
    }

    public void setÉrésKezd(String érésKezd) {
        this.érésKezd = érésKezd;
    }

    public void setPalackozásDate(String palackozásDate) {
        this.palackozásDate = palackozásDate;
    }

    public String getPalackozásDate() {
        return palackozásDate;
    }

    public double getVeszteség() {
        return veszteség;
    }

    @Override
    public String toString() {
        return "K\u00e9szBor{" + "n\u00e9v=" + név + ", sz\u0151l\u0151T\u00edpus=" + szőlőTípus + ", \u00e9vj\u00e1rat=" + évjárat + ", cukor=" + cukor + ", alkohol=" + alkohol + ", liter=" + liter + ", db=" + db + ", vesztes\u00e9g=" + veszteség + ", \u00e9r\u00e9sKezd=" + érésKezd + ", palackoz\u00e1sDate=" + palackozásDate + '}';
    }


    
}
