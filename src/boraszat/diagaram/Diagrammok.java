package boraszat.diagaram;
import boraszat.model.Agy;
import boraszat.model.KészBor;
import boraszat.model.Munkás;
import boraszat.model.ÉrőBor;
import java.util.ArrayList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Diagrammok{
    
    ArrayList<Munkás> munkás = new ArrayList<>();
    ArrayList<ÉrőBor> éBor = new ArrayList<>();
    ArrayList<KészBor> kBor = new ArrayList<>();
    Agy agy = new Agy();

    public Diagrammok() {
        
        munkás = agy.getMunkás();
        éBor = agy.getéBor();
        kBor = agy.getKBor();
        
    }

    public BarChart createChartDolg(){
        
        CategoryAxis x  = new CategoryAxis();
        x.setLabel("Munkakörök");

        NumberAxis y = new NumberAxis();
        y.setLabel("Fő");

        BarChart oszlopDia = new BarChart(x, y);

        XYChart.Series nappali = new XYChart.Series();
        nappali.setName("Nappali");
        XYChart.Series éjszakai = new XYChart.Series();
        éjszakai.setName("Éjszakai");
        XYChart.Series hétvégi = new XYChart.Series();
        hétvégi.setName("Hétvégi");

        int napBŐr = 0, napPMunkás = 0, napTakarító = 0, napBorász = 0, napTitkár = 0;
 
        for (Munkás m : munkás) {
            /* NAPPALI*/   
            if(m.getMk().displayName().equals("Takarító") && m.getIdőB().displayName().equals("Nappali")){
                napTakarító++;
            } else if (m.getMk().displayName().equals("Borász") && m.getIdőB().displayName().equals("Nappali")){
                napBorász++;
            } else if(m.getMk().displayName().equals("Pincemunkás") && m.getIdőB().displayName().equals("Nappali")){
                napPMunkás++;
            } else if(m.getMk().displayName().equals("Titkár") && m.getIdőB().displayName().equals("Nappali")){
                napTitkár++;
            } else if(m.getMk().displayName().equals("Biztonsági őr") && m.getIdőB().displayName().equals("Nappali")){
                napBŐr++;
            }    
        }
        
        nappali.getData().add(new XYChart.Data("Pincemunkás", napPMunkás));
        nappali.getData().add(new XYChart.Data("Takarító", napTakarító));
        nappali.getData().add(new XYChart.Data("Borász", napBorász));
        nappali.getData().add(new XYChart.Data("Titkár", napTitkár));
        nappali.getData().add(new XYChart.Data("Biztonsági őr", napBŐr));

        oszlopDia.getData().add(nappali);           

        int éjPMunkás = 0, éjTakarító = 0, éjBorász = 0, éjTitkár = 0, éjBŐr = 0;
        
        for (Munkás m : munkás) {
            /*ÉJSZAKAI*/
            if(m.getMk().displayName().equals("Pincemunkás") && m.getIdőB().displayName().equals("Éjszakai")){
                éjPMunkás++;
            } else if(m.getMk().displayName().equals("Takarító") && m.getIdőB().displayName().equals("Éjszakai")){
                éjTakarító++;
            } else if(m.getMk().displayName().equals("Borász") && m.getIdőB().displayName().equals("Éjszakai")){
                éjBorász++;
            } else if(m.getMk().displayName().equals("Titkár") && m.getIdőB().displayName().equals("Éjszakai")){
                éjTitkár++;
            } else if(m.getMk().displayName().equals("Biztonsági őr") && m.getIdőB().displayName().equals("Éjszakai")){
                éjBŐr++;
            }
        }
            
        éjszakai.getData().add(new XYChart.Data("Pincemunkás", éjPMunkás));
        éjszakai.getData().add(new XYChart.Data("Takarító", éjTakarító));
        éjszakai.getData().add(new XYChart.Data("Borász", éjBorász));
        éjszakai.getData().add(new XYChart.Data("Titkár", éjTitkár));
        éjszakai.getData().add(new XYChart.Data("Biztonsági őr", éjBŐr));

        oszlopDia.getData().add(éjszakai);      
        
        int hétvPMunkás = 0, hétvTakarító = 0, hétvBorász = 0, hétvTitkár = 0, hétvBŐr = 0;
        
        for (Munkás m : munkás) {
            /* HÉTVÉGI */
            if(m.getMk().displayName().equals("Pincemunkás") && m.getIdőB().displayName().equals("Hétvégi")){
                hétvPMunkás++;
            } else if(m.getMk().displayName().equals("Takarító") && m.getIdőB().displayName().equals("Hétvégi")){
                hétvTakarító++;
            } else if(m.getMk().displayName().equals("Borász") && m.getIdőB().displayName().equals("Hétvégi")){
                hétvBorász++;
            } else if(m.getMk().displayName().equals("Titkár") && m.getIdőB().displayName().equals("Hétvégi")){
                hétvTitkár++;
            } else if(m.getMk().displayName().equals("Biztonsági őr") && m.getIdőB().displayName().equals("Hétvégi")){
                hétvBŐr++;
            }
        }
        hétvégi.getData().add(new XYChart.Data("Pincemunkás", hétvPMunkás));
        hétvégi.getData().add(new XYChart.Data("Takarító", hétvTakarító));
        hétvégi.getData().add(new XYChart.Data("Borász", hétvBorász));
        hétvégi.getData().add(new XYChart.Data("Titkár", hétvTitkár));
        hétvégi.getData().add(new XYChart.Data("Biztonsági őr", hétvBŐr));

        oszlopDia.getData().add(hétvégi);
        
        return oszlopDia;
    }
    public BarChart createChartÉBor(){
        
        CategoryAxis x  = new CategoryAxis();
        x.setLabel("Szőlőtípus");

        NumberAxis y = new NumberAxis();
        y.setLabel("Menyniség (liter)");

        BarChart oszlopDia = new BarChart(x, y);

        XYChart.Series adat2015 = new XYChart.Series();
        adat2015.setName("2015");
        XYChart.Series adat2016 = new XYChart.Series();
        adat2016.setName("2016");
        
        double kfk2015 = 0, csf2015 = 0, cbf2015 = 0, cs2015 = 0, syr2015 = 0;
        double mrl2015 = 0, irs2015 = 0, kdk2015 = 0, chr2015 = 0, orz2015 = 0;
        
        for (ÉrőBor éB : éBor) {
            if(éB.getSzőlőTípus().displayName().equals("Kékfrankos") && éB.getÉvjárat().equals("2015")){
                kfk2015=éB.getLiter() +kfk2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && éB.getÉvjárat().equals("2015")){
                csf2015=éB.getLiter() +csf2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Cabernet Franc") && éB.getÉvjárat().equals("2015")){
                cbf2015=éB.getLiter() +cbf2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && éB.getÉvjárat().equals("2015")){
                cs2015=éB.getLiter() +cs2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Syrah") && éB.getÉvjárat().equals("2015")){
                syr2015=éB.getLiter() +syr2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Merlot") && éB.getÉvjárat().equals("2015")){
                mrl2015=éB.getLiter() +mrl2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Irsai Olivér") && éB.getÉvjárat().equals("2015")){
                irs2015=éB.getLiter() +irs2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Kadarka") && éB.getÉvjárat().equals("2015")){
                kdk2015=éB.getLiter() +kdk2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Chardonnay") && éB.getÉvjárat().equals("2015")){
                chr2015=éB.getLiter() +chr2015;
            } else if(éB.getSzőlőTípus().displayName().equals("Olaszrizling") && éB.getÉvjárat().equals("2015")){
                orz2015=éB.getLiter() +orz2015;
            }
        }
        adat2015.getData().add(new XYChart.Data("Kékfrankos", kfk2015));
        adat2015.getData().add(new XYChart.Data("Cserszegi Fűszeres", csf2015));
        adat2015.getData().add(new XYChart.Data("Cabernet Franc", cbf2015));
        adat2015.getData().add(new XYChart.Data("Cabernet Sauvignon", cs2015));
        adat2015.getData().add(new XYChart.Data("Syrah", syr2015));
        adat2015.getData().add(new XYChart.Data("Merlot", mrl2015));
        adat2015.getData().add(new XYChart.Data("Irsai Olivér", irs2015));
        adat2015.getData().add(new XYChart.Data("Kadarka", kdk2015));
        adat2015.getData().add(new XYChart.Data("Chardonnay", chr2015));
        adat2015.getData().add(new XYChart.Data("Olaszrizling", orz2015));

        oszlopDia.getData().add(adat2015);
        
        double kfk2016 = 0, csf2016 = 0, cbf2016 = 0, cs2016 = 0, syr2016 = 0;
        double mrl2016 = 0, irs2016 = 0, kdk2016 = 0, chr2016 = 0, orz2016 = 0;
        
        for (ÉrőBor éB : éBor) {
            if(éB.getSzőlőTípus().displayName().equals("Kékfrankos") && éB.getÉvjárat().equals("2016")){
                kfk2016=éB.getLiter() +kfk2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && éB.getÉvjárat().equals("2016")){
                csf2016=éB.getLiter() +csf2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Cabernet Franc") && éB.getÉvjárat().equals("2016")){
                cbf2016=éB.getLiter() +cbf2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && éB.getÉvjárat().equals("2016")){
                cs2016=éB.getLiter() +cs2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Syrah") && éB.getÉvjárat().equals("2016")){
                syr2016=éB.getLiter() +syr2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Merlot") && éB.getÉvjárat().equals("2016")){
                mrl2016=éB.getLiter() +mrl2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Irsai Olivér") && éB.getÉvjárat().equals("2016")){
                irs2016=éB.getLiter() +irs2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Kadarka") && éB.getÉvjárat().equals("2016")){
                kdk2016=éB.getLiter() +kdk2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Chardonnay") && éB.getÉvjárat().equals("2016")){
                chr2016=éB.getLiter() +chr2016;
            } else if(éB.getSzőlőTípus().displayName().equals("Olaszrizling") && éB.getÉvjárat().equals("2016")){
                orz2016=éB.getLiter() +orz2016;
            }
        }
        adat2016.getData().add(new XYChart.Data("Kékfrankos", kfk2016));
        adat2016.getData().add(new XYChart.Data("Cserszegi Fűszeres", csf2016));
        adat2016.getData().add(new XYChart.Data("Cabernet Franc", cbf2016));
        adat2016.getData().add(new XYChart.Data("Cabernet Sauvignon", cs2016));
        adat2016.getData().add(new XYChart.Data("Syrah", syr2016));
        adat2016.getData().add(new XYChart.Data("Merlot", mrl2016));
        adat2016.getData().add(new XYChart.Data("Irsai Olivér", irs2016));
        adat2016.getData().add(new XYChart.Data("Kadarka", kdk2016));
        adat2016.getData().add(new XYChart.Data("Chardonnay", chr2016));
        adat2016.getData().add(new XYChart.Data("Olaszrizling", orz2016));

        oszlopDia.getData().add(adat2016);
        
        return oszlopDia;
    }
    public BarChart createChartKBor(){
        
                CategoryAxis x  = new CategoryAxis();
        x.setLabel("Szőlőtípus");

        NumberAxis y = new NumberAxis();
        y.setLabel("Mennyiség (liter)");

        BarChart oszlopDia = new BarChart(x, y);

        XYChart.Series száraz = new XYChart.Series();
        száraz.setName("Száraz");
        XYChart.Series félszáraz = new XYChart.Series();
        félszáraz.setName("Félszáraz");
        XYChart.Series félédes = new XYChart.Series();
        félédes.setName("Félédes");
        XYChart.Series édes = new XYChart.Series();
        édes.setName("Édes");
        
        double kfkSzáraz = 0, csfSzáraz = 0, cbfSzáraz = 0, csSzáraz = 0, syrSzáraz = 0;
        double mrlSzáraz = 0, irsSzáraz = 0, kdkSzáraz = 0, chrSzáraz = 0, orzSzáraz = 0;
        
        for (KészBor kB : kBor) {
            if(kB.getSzőlőTípus().displayName().equals("Kékfrankos") && kB.getCukor().equals("<4 g/l Száraz")){
                kfkSzáraz=kB.getLiter() +kfkSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && kB.getCukor().equals("<4 g/l Száraz")){
                csfSzáraz=kB.getLiter() +csfSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Franc") && kB.getCukor().equals("<4 g/l Száraz")){
                cbfSzáraz=kB.getLiter() +cbfSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && kB.getCukor().equals("<4 g/l Száraz")){
                csSzáraz=kB.getLiter() +csSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Syrah") && kB.getCukor().equals("<4 g/l Száraz")){
                syrSzáraz=kB.getLiter() +syrSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Merlot") && kB.getCukor().equals("<4 g/l Száraz")){
                mrlSzáraz=kB.getLiter() +mrlSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Irsai Olivér") && kB.getCukor().equals("<4 g/l Száraz")){
                irsSzáraz=kB.getLiter() +irsSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Kadarka") && kB.getCukor().equals("<4 g/l Száraz")){
                kdkSzáraz=kB.getLiter() +kdkSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Chardonnay") && kB.getCukor().equals("<4 g/l Száraz")){
                chrSzáraz=kB.getLiter() +chrSzáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Olaszrizling") && kB.getCukor().equals("<4 g/l Száraz")){
                orzSzáraz=kB.getLiter() +orzSzáraz;
            }
        }
        száraz.getData().add(new XYChart.Data("Kékfrankos", kfkSzáraz));
        száraz.getData().add(new XYChart.Data("Cserszegi Fűszeres", csfSzáraz));
        száraz.getData().add(new XYChart.Data("Cabernet Franc", cbfSzáraz));
        száraz.getData().add(new XYChart.Data("Cabernet Sauvignon", csSzáraz));
        száraz.getData().add(new XYChart.Data("Syrah", syrSzáraz));
        száraz.getData().add(new XYChart.Data("Merlot", mrlSzáraz));
        száraz.getData().add(new XYChart.Data("Irsai Olivér", irsSzáraz));
        száraz.getData().add(new XYChart.Data("Kadarka", kdkSzáraz));
        száraz.getData().add(new XYChart.Data("Chardonnay", chrSzáraz));
        száraz.getData().add(new XYChart.Data("Olaszrizling", orzSzáraz));

        oszlopDia.getData().add(száraz);
        
        
        double kfkFélszáraz = 0, csfFélszáraz = 0, cbfFélszáraz = 0, csFélszáraz = 0, syrFélszáraz = 0;
        double mrlFélszáraz = 0, irsFélszáraz = 0, kdkFélszáraz = 0, chrFélszáraz = 0, orzFélszáraz = 0;

        for (KészBor kB : kBor) {
            if(kB.getSzőlőTípus().displayName().equals("Kékfrankos") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                kfkFélszáraz=kB.getLiter() +kfkFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                csfFélszáraz=kB.getLiter() +csfFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Franc") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                cbfFélszáraz=kB.getLiter() +cbfFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                csFélszáraz=kB.getLiter() +csFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Syrah") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                syrFélszáraz=kB.getLiter() +syrFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Merlot") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                mrlFélszáraz=kB.getLiter() +mrlFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Irsai Olivér") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                irsFélszáraz=kB.getLiter() +irsFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Kadarka") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                kdkFélszáraz=kB.getLiter() +kdkFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Chardonnay") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                chrFélszáraz=kB.getLiter() +chrFélszáraz;
            } else if(kB.getSzőlőTípus().displayName().equals("Olaszrizling") && kB.getCukor().equals("4-12 g/l Félszáraz")){
                orzFélszáraz=kB.getLiter() +orzFélszáraz;
            }
        }
        félszáraz.getData().add(new XYChart.Data("Kékfrankos", kfkFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Cserszegi Fűszeres", csfFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Cabernet Franc", cbfFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Cabernet Sauvignon", csFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Syrah", syrFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Merlot", mrlFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Irsai Olivér", irsFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Kadarka", kdkFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Chardonnay", chrFélszáraz));
        félszáraz.getData().add(new XYChart.Data("Olaszrizling", orzFélszáraz));

        oszlopDia.getData().add(félszáraz);
        
        double kfkFélédes = 0, csfFélédes = 0, cbfFélédes = 0, csFélédes = 0, syrFélédes = 0;
        double mrlFélédes = 0, irsFélédes = 0, kdkFélédes = 0, chrFélédes = 0, orzFélédes = 0;
        
        for (KészBor kB : kBor) {
            if(kB.getSzőlőTípus().displayName().equals("Kékfrankos") && kB.getCukor().equals("12-45 g/l Félédes")){
                kfkFélédes=kB.getLiter() +kfkFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && kB.getCukor().equals("12-45 g/l Félédes")){
                csfFélédes=kB.getLiter() +csfFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Franc") && kB.getCukor().equals("12-45 g/l Félédes")){
                cbfFélédes=kB.getLiter() +cbfFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && kB.getCukor().equals("12-45 g/l Félédes")){
                csFélédes=kB.getLiter() +csFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Syrah") && kB.getCukor().equals("12-45 g/l Félédes")){
                syrFélédes=kB.getLiter() +syrFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Merlot") && kB.getCukor().equals("12-45 g/l Félédes")){
                mrlFélédes=kB.getLiter() +mrlFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Irsai Olivér") && kB.getCukor().equals("12-45 g/l Félédes")){
                irsFélédes=kB.getLiter() +irsFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Kadarka") && kB.getCukor().equals("12-45 g/l Félédes")){
                kdkFélédes=kB.getLiter() +kdkFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Chardonnay") && kB.getCukor().equals("12-45 g/l Félédes")){
                chrFélédes=kB.getLiter() +chrFélédes;
            } else if(kB.getSzőlőTípus().displayName().equals("Olaszrizling") && kB.getCukor().equals("12-45 g/l Félédes")){
                orzFélédes=kB.getLiter() +orzFélédes;
            }
        }
        félédes.getData().add(new XYChart.Data("Kékfrankos", kfkFélédes));
        félédes.getData().add(new XYChart.Data("Cserszegi Fűszeres", csfFélédes));
        félédes.getData().add(new XYChart.Data("Cabernet Franc", cbfFélédes));
        félédes.getData().add(new XYChart.Data("Cabernet Sauvignon", csFélédes));
        félédes.getData().add(new XYChart.Data("Syrah", syrFélédes));
        félédes.getData().add(new XYChart.Data("Merlot", mrlFélédes));
        félédes.getData().add(new XYChart.Data("Irsai Olivér", irsFélédes));
        félédes.getData().add(new XYChart.Data("Kadarka", kdkFélédes));
        félédes.getData().add(new XYChart.Data("Chardonnay", chrFélédes));
        félédes.getData().add(new XYChart.Data("Olaszrizling", orzFélédes));

        oszlopDia.getData().add(félédes);
        
        double kfkÉdes = 0, csfÉdes = 0, cbfÉdes = 0, csÉdes = 0, syrÉdes = 0;
        double mrlÉdes = 0, irsÉdes = 0, kdkÉdes = 0, chrÉdes = 0, orzÉdes = 0;
        
        for (KészBor kB : kBor) {
            if(kB.getSzőlőTípus().displayName().equals("Kékfrankos") && kB.getCukor().equals(">45 g/l Édes")){
                kfkÉdes=kB.getLiter() +kfkÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cserszegi Fűszeres") && kB.getCukor().equals(">45 g/l Édes")){
                csfÉdes=kB.getLiter() +csfÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Franc") && kB.getCukor().equals(">45 g/l Édes")){
                cbfÉdes=kB.getLiter() +cbfÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Cabernet Sauvignon") && kB.getCukor().equals(">45 g/l Édes")){
                csÉdes=kB.getLiter() +csÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Syrah") && kB.getCukor().equals(">45 g/l Édes")){
                syrÉdes=kB.getLiter() +syrÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Merlot") && kB.getCukor().equals(">45 g/l Édes")){
                mrlÉdes=kB.getLiter() +mrlÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Irsai Olivér") && kB.getCukor().equals(">45 g/l Édes")){
                irsÉdes=kB.getLiter() +irsÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Kadarka") && kB.getCukor().equals(">45 g/l Édes")){
                kdkÉdes=kB.getLiter() +kdkÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Chardonnay") && kB.getCukor().equals(">45 g/l Édes")){
                chrÉdes=kB.getLiter() +chrÉdes;
            } else if(kB.getSzőlőTípus().displayName().equals("Olaszrizling") && kB.getCukor().equals(">45 g/l Édes")){
                orzÉdes=kB.getLiter() +orzÉdes;
            }
        }
        édes.getData().add(new XYChart.Data("Kékfrankos", kfkÉdes));
        édes.getData().add(new XYChart.Data("Cserszegi Fűszeres", csfÉdes));
        édes.getData().add(new XYChart.Data("Cabernet Franc", cbfÉdes));
        édes.getData().add(new XYChart.Data("Cabernet Sauvignon", csÉdes));
        édes.getData().add(new XYChart.Data("Syrah", syrÉdes));
        édes.getData().add(new XYChart.Data("Merlot", mrlÉdes));
        édes.getData().add(new XYChart.Data("Irsai Olivér", irsÉdes));
        édes.getData().add(new XYChart.Data("Kadarka", kdkÉdes));
        édes.getData().add(new XYChart.Data("Chardonnay", chrÉdes));
        édes.getData().add(new XYChart.Data("Olaszrizling", orzÉdes));

        oszlopDia.getData().add(édes);
        
        return oszlopDia;
    }
}
