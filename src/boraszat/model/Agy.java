package boraszat.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Agy {
    
    Gson gson = new Gson();
    
    public int számlaSorSzám;
    
    ArrayList<KészBor> kBor = new ArrayList<>();
    ArrayList<ÉrőBor> éBor = new ArrayList<>();
    ArrayList<Munkás> munkás = new ArrayList<>();
    
    Munkás mMód = new Munkás(null, null, null, null, null, null);
    ÉrőBor éToKBor = new ÉrőBor(null, null, null, 0, null);
    KészBor eladKBor = new KészBor(null, new ÉrőBor(null, null, null, 0, null), 0, null, null, null, 0);
//    KészBor kToÉBor = new KészBor(null, null, 0, null, null, null);

    public ÉrőBor getÉToKBor() {
        return éToKBor;
    }
    public Munkás getMMód(){
        return mMód;
    }
    public KészBor getEladBor(){
        return eladKBor;
    }
    
//    public KészBor getKToÉBor(){
//        return kToÉBor;
//    }
    
    public void resetEladKBor(){
        this.eladKBor.setNév(null);
        this.eladKBor.setDb(0);
        this.eladKBor.setÁr(0);        
    }
    public void resetÉToKBor(){
        this.éToKBor.setSzőlőTípus(null);
        this.éToKBor.setTárolás(null);
        this.éToKBor.setÉrésKezd(null);
        this.éToKBor.setLiter(0);
        this.éToKBor.setÉvjárat(null);
    }
    public void resetMMód(){
        this.mMód.setNév(null);
        this.mMód.setSzülDate(null);
        this.mMód.setAnyaNév(null);
        this.mMód.setMunkaKezd(null);
        this.mMód.setIdőB(null);
        this.mMód.setMunkaKezd(null);
    }

    public Agy() {
        feltölt();
        backUpData();
    }
    public boolean isNév(String name) {
        
    return  name.matches("[a-zA-Z]+" + " " +".*[^\\x20-\\x7E].*") ||
            name.matches("[a-zA-Z]+" + " " +"[a-zA-Z]+") ||
            name.matches(".*[^\\x20-\\x7E].*" + " " +"[a-zA-Z]+") ||
            name.matches(".*[^\\x20-\\x7E].*" + " " +".*[^\\x20-\\x7E].*");
        
        
//        return name.matches("[a-zA-Z]+") || name.matches(".*[^\\x20-\\x7E].*");
    }
    public boolean isRandomNév(String bor){
        return bor.matches("[a-zA-Z]+") || bor.matches(".*[^\\x20-\\x7E].*");
    }
    public boolean isDátumValid(String date){
        
        boolean ok=true;
        
        String[]évHóNap = date.split("-");
        String év = évHóNap[0];
        int intÉv = Integer.parseInt(év);
        String hó = évHóNap[1];
        int intHó = Integer.parseInt(hó);
        String nap = évHóNap[2];
        int intNap = Integer.parseInt(nap);
        
        int aktÉv = Calendar.getInstance().get(Calendar.YEAR);
 
        if (nap.equals("31") && ( 
            hó.equals("4") || hó.equals("6") || hó.equals("9") ||
            hó.equals("11") || hó.equals("04") || hó.equals("06") ||
            hó.equals("09")
            )){
            ok = false; // csak 1,3,5,7,8,10,12
	} else if (hó.equals("2") || hó.equals("02")) {
                  //szökőév
            if(intÉv % 4==0){
		if(nap.equals("30") || nap.equals("31")){
                    ok = false;
		}else{
                    ok = true;
		}
            }else{
		if(nap.equals("29")||nap.equals("30")||nap.equals("31")){
                    ok = false;
		}else{
                    ok = true;
		 }
            }
        }      
        if (intÉv>aktÉv){
            ok = false;
        }
        if (intHó>12){
            ok = false;
        }
        if (intNap>31){
            ok = false;
        }
        return ok;
    }
    
    public boolean validateDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(date);
            System.out.println("TRUE");
            return true;
        }
        catch(ParseException ex) {
            System.out.println("FALSE");
            return false;
        }
    }
    
    public String tételString (){
        return éToKBor.toString();
    } 
    
    public void mAdatMód(String név, String év, String anya, Munkakör mk, Időbeosztás ib, String kezd){
        
        mMód.setNév(név);
        mMód.setSzülDate(év);
        mMód.setAnyaNév(anya);
        mMód.setMk(mk);
        mMód.setIdőB(ib);
        mMód.setMunkaKezd(kezd);
        
    }
    public void tételÁtvitel(Szőlőtípusok szőlő, String évj, Tárolók tár, double menny, String kezdet){
        
        éToKBor.setLiter(menny);
        éToKBor.setSzőlőTípus(szőlő);
        éToKBor.setTárolás(tár);
        éToKBor.setÉrésKezd(kezdet);
        éToKBor.setÉvjárat(évj);
    }
    public void áruElad(String név, int db, int ár){
        
        eladKBor.setÁr(ár);
        eladKBor.setDb(db);
        eladKBor.setNév(név);
    }
    
    public TableView getKBorTableData(){
        
        TableView<KészBor> tvK = new TableView<>();
        
        TableColumn<KészBor, String> névCol = 
            new TableColumn<>("Név");
        TableColumn<KészBor, Szőlőtípusok> szőlőCol = 
            new TableColumn<>("Szőlőtípus");
        TableColumn<KészBor, String> évCol = 
            new TableColumn<>("Évjárat");
        TableColumn<KészBor, String> cukorCol = 
            new TableColumn<>("Cukor g/l");
        TableColumn<KészBor, Double> alkCol =
            new TableColumn<>("Alkohol %"); 
        TableColumn<KészBor, Double> litCol =
            new TableColumn<>("Liter l");
        TableColumn<KészBor, Integer> palackCol =
            new TableColumn<>("Palack szám");
        TableColumn<KészBor, Double> vesztCol =
            new TableColumn<>("Veszteség literben"); 
        TableColumn<KészBor, String> érésCol =
            new TableColumn<>("Éréskezdete");
        TableColumn<KészBor, String> palackozásCol =
            new TableColumn<>("Palackozva");
        TableColumn<KészBor, Integer> árCol =
            new TableColumn<>("Ár/palack");
        
        névCol.setCellValueFactory(
            new PropertyValueFactory<>("név"));
        szőlőCol.setCellValueFactory(
            new PropertyValueFactory<>("szőlőTípus"));
        évCol.setCellValueFactory(
            new PropertyValueFactory<>("évjárat"));
        cukorCol.setCellValueFactory(
            new PropertyValueFactory<>("cukor"));
        alkCol.setCellValueFactory(
            new PropertyValueFactory<>("alkohol"));
        litCol.setCellValueFactory(
            new PropertyValueFactory<>("liter"));
        palackCol.setCellValueFactory(
            new PropertyValueFactory<>("db"));
        vesztCol.setCellValueFactory(
            new PropertyValueFactory<>("veszteség"));
        érésCol.setCellValueFactory(
            new PropertyValueFactory<>("érésKezd"));
        palackozásCol.setCellValueFactory(
            new PropertyValueFactory<>("palackozásDate"));
        árCol.setCellValueFactory(
            new PropertyValueFactory<>("ár"));
        
        tvK.setItems(getOKBorList());
        tvK.getColumns().addAll(névCol, szőlőCol, évCol, cukorCol, alkCol, litCol, palackCol, vesztCol, érésCol, palackozásCol, árCol);
        
        return tvK;
    }
    public TableView getÉBorTableData(){
        
        TableView<ÉrőBor> tvÉ = new TableView<>();
        
        TableColumn<ÉrőBor, Szőlőtípusok> szőlőCol = 
            new TableColumn<>("Szőlőtípus");
        TableColumn<ÉrőBor, String> évCol = 
            new TableColumn<>("Évjárat");
        TableColumn<ÉrőBor, Tárolók> tárCol = 
            new TableColumn<>("Tárolótípus");
        TableColumn<ÉrőBor, Double> menyCol = 
            new TableColumn<>("Liter");
        TableColumn<ÉrőBor, String> kezdDát =
            new TableColumn<>("Érés kezdete"); 
        
        szőlőCol.setCellValueFactory(
            new PropertyValueFactory<>("szőlőTípus"));
        évCol.setCellValueFactory(
            new PropertyValueFactory<>("évjárat"));
        tárCol.setCellValueFactory(
            new PropertyValueFactory<>("tárolás"));
        menyCol.setCellValueFactory(
            new PropertyValueFactory<>("liter"));
        kezdDát.setCellValueFactory(
            new PropertyValueFactory<>("érésKezd"));
        
        tvÉ.setItems(getOÉBorList());
        tvÉ.getColumns().addAll(szőlőCol, évCol, tárCol, menyCol, kezdDát);
        
        return tvÉ;
    }
    public TableView getMunkásTableData(){
        
        TableView<Munkás> tvM = new TableView<>();
        
        TableColumn<Munkás, String> névCol = 
            new TableColumn<>("Név");
        TableColumn<Munkás, String> szülCol = 
            new TableColumn<>("Születési dátum");
        TableColumn<Munkás, String> anyaCol = 
            new TableColumn<>("Anyja neve");
        TableColumn<Munkás, Munkakör> mKörCol = 
            new TableColumn<>("Munkakör");
        TableColumn<Munkás, Időbeosztás> időBCol = 
            new TableColumn<>("Időbeosztás");
        TableColumn<Munkás, String> kezdDát =
            new TableColumn<>("Munka kezdete"); 
        
        névCol.setCellValueFactory(
            new PropertyValueFactory<>("név"));
        szülCol.setCellValueFactory(
            new PropertyValueFactory<>("szülDate"));
        anyaCol.setCellValueFactory(
            new PropertyValueFactory<>("anyaNév"));
        mKörCol.setCellValueFactory(
            new PropertyValueFactory<>("mk"));
        időBCol.setCellValueFactory(
            new PropertyValueFactory<>("időB"));
        kezdDát.setCellValueFactory(
            new PropertyValueFactory<>("munkaKezd"));
        
        tvM.setItems(getOMList());
        tvM.getColumns().addAll(névCol, szülCol, anyaCol, mKörCol, időBCol,kezdDát);
        
        return tvM;
    }
    
    public void removeÉBor(ÉrőBor éB){
        éBor.remove(éB);
    }
    public void removeKBor(KészBor kB){
        kBor.remove(kB);
    }
    public void removeM(Munkás m){
        munkás.remove(m);
    }
    
    public ObservableList<ÉrőBor>getOÉBorList(){
        ObservableList<ÉrőBor> oÉBor = FXCollections.observableArrayList(éBor);
        return oÉBor;
    }
    public ObservableList<KészBor>getOKBorList(){
        ObservableList<KészBor> oKBor = FXCollections.observableArrayList(kBor);
        return oKBor;
    }
    public ObservableList<Munkás>getOMList(){
        ObservableList<Munkás> oMunkás = FXCollections.observableArrayList(munkás);
        return oMunkás;
    }

    public ArrayList<KészBor> getKBor() {
        return kBor;
    }
    public ArrayList<ÉrőBor> getéBor() {
        return éBor;
    }
    public ArrayList<Munkás> getMunkás(){
        return munkás;
    }
    
    public void addMunkás(String név, String szül, String anya,Munkakör mk, Időbeosztás ib,String kezd){
        
        Munkás m = new Munkás(név, szül, anya, mk, ib, kezd);
        munkás.add(m);
    }
    public void addÉBor(Szőlőtípusok szőlő, String évj, Tárolók tár, double menny, String kezdet) {
        
       ÉrőBor éB = new ÉrőBor(szőlő,évj,tár,menny,kezdet);
       éBor.add(éB);
        
    }
    public void addKBor(String név,ÉrőBor éb, double alk, String cuk, String érés, String palackD, int ár) {
        
       KészBor kb = new KészBor(név, éb, alk, cuk, érés, palackD, ár);
       kBor.add(kb);
    }
    
    public String getDateYMDHM(){
             
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("_yyyy-MM-dd_HH_mm");
        LocalDateTime localDate = LocalDateTime.now();
        String dateYMD=(dtf.format(localDate));
        
        return dateYMD;
    }
    public int getSzámlaSorSzám() {
        return számlaSorSzám;
    }
    
    public void sorSzámLép(){
        számlaSorSzám+=1;
    }
    
    private void backUpData(){
        String jsonKBor = (gson.toJson(kBor));
        
        try (PrintWriter writer = new PrintWriter(new File("./backup/keszborok"+getDateYMDHM()+".json"))) {
                    writer.write(jsonKBor);                
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
        String jsonÉBor = (gson.toJson(éBor));
      
        try (PrintWriter writer = new PrintWriter(new File("./backup/eroborok"+getDateYMDHM()+".json"))) {
                writer.write(jsonÉBor);                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        String jsonMunkás = (gson.toJson(munkás));
      
        try (PrintWriter writer = new PrintWriter(new File("./backup/munkas"+getDateYMDHM()+".json"))) {
                writer.write(jsonMunkás);                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
    public void saveData(){
        
        String jsonKBor = (gson.toJson(kBor));
        
        try (PrintWriter writer = new PrintWriter("data/keszborok.json")) {
                    writer.write(jsonKBor);                
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
        String jsonSorSzám = (gson.toJson(számlaSorSzám));
        
        try (PrintWriter writer = new PrintWriter("data/szamlasorsz.json")) {
                    writer.write(jsonSorSzám);                
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
        String jsonÉBor = (gson.toJson(éBor));
      
        try (PrintWriter writer = new PrintWriter("data/eroborok.json")) {
                writer.write(jsonÉBor);                
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        String jsonMunkás = (gson.toJson(munkás));
        
        try (PrintWriter writer = new PrintWriter("data/munkas.json")) {
                writer.write(jsonMunkás);                
                
            } catch (Exception e) {
                e.printStackTrace();
            }   
    }
    private void feltölt(){
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("data/szamlasorsz.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                számlaSorSzám = gson.fromJson(mind, int.class);
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("data/keszborok.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                kBor = gson.fromJson(mind,new TypeToken<ArrayList<KészBor>>(){}.getType());
 
            } catch (Exception e) {
                e.printStackTrace();
            }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("data/eroborok.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                éBor = gson.fromJson(mind,new TypeToken<ArrayList<ÉrőBor>>(){}.getType());
   
            } catch (Exception e) {
                e.printStackTrace();
            }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("data/munkas.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                munkás = gson.fromJson(mind,new TypeToken<ArrayList<Munkás>>(){}.getType());

            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}


