package boraszat.diagaram;
import boraszat.model.Agy;
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
    Agy agy = new Agy();

    public Diagrammok() {
        
        munkás = agy.getMunkás();
        éBor = agy.getéBor();
        
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
}
