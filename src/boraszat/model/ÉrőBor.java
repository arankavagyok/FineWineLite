/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boraszat.model;

public class ÉrőBor {
    
    Szőlőtípusok szőlőTípus; 
    String évjárat;
    Tárolók tárolás;
    double liter;
    String érésKezd;

    public ÉrőBor(Szőlőtípusok szőlőTípus, String évjárat, Tárolók tárolás, double mennyiség, String érésKezd) {
        this.szőlőTípus = szőlőTípus;
        this.évjárat = évjárat;
        this.tárolás = tárolás;
        this.liter = mennyiség;
        this.érésKezd = érésKezd;
    }
    
    public Szőlőtípusok getSzőlőTípus() {
        return szőlőTípus;
    }

    public String getÉvjárat() {
        return évjárat;
    }

    public Tárolók getTárolás() {
        return tárolás;
    }

    public void setSzőlőTípus(Szőlőtípusok szőlőTípus) {
        this.szőlőTípus = szőlőTípus;
    }

    public void setÉvjárat(String évjárat) {
        this.évjárat = évjárat;
    }

    public void setTárolás(Tárolók tárolás) {
        this.tárolás = tárolás;
    }

    public void setLiter(double liter) {
        this.liter = liter;
    }

    public void setÉrésKezd(String érésKezd) {
        this.érésKezd = érésKezd;
    }

    public double getLiter() {
        return liter;
    }

    public String getÉrésKezd() {
        return érésKezd;
    }

    @Override
    public String toString() {
        return "\u00c9r\u0151Bor{" + "sz\u0151l\u0151T\u00edpus=" + szőlőTípus + ", \u00e9vj\u00e1rat=" + évjárat + ", t\u00e1rol\u00e1s=" + tárolás + ", liter=" + liter + '}';
    }
    
}
