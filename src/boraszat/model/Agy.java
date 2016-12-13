package boraszat.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Agy {
    
    ArrayList<KészBor> kBor = new ArrayList<>();
    Gson gson = new Gson();
    ArrayList<ÉrőBor> éBor = new ArrayList<>();
    ÉrőBor éToKBor = new ÉrőBor(null, null, null, 0, null);

    public ÉrőBor getÉToKBor() {
        return éToKBor;
    }
    
    public void resetÉToKBor(){
        this.éToKBor.setSzőlőTípus(null);
        this.éToKBor.setTárolás(null);
        this.éToKBor.setÉrésKezd(null);
        this.éToKBor.setLiter(0);
        this.éToKBor.setÉvjárat(null);
    }

    public Agy() {
        feltölt();
    }
    
    public String tételString (){
        return éToKBor.toString();
    }    
    public void tételÁtvitel(Szőlőtípusok szőlő, String évj, Tárolók tár, double menny, String kezdet){
        
        éToKBor.setLiter(menny);
        éToKBor.setSzőlőTípus(szőlő);
        éToKBor.setTárolás(tár);
        éToKBor.setÉrésKezd(kezdet);
        éToKBor.setÉvjárat(évj);
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
            new TableColumn<>("Cukor");
        TableColumn<KészBor, Double> alkCol =
            new TableColumn<>("Alkohol"); 
        TableColumn<KészBor, Double> litCol =
            new TableColumn<>("Liter");
        TableColumn<KészBor, Integer> palackCol =
            new TableColumn<>("Palack szám");
        TableColumn<KészBor, Double> vesztCol =
            new TableColumn<>("Veszteség literben"); 
        
        névCol.setCellValueFactory(
            new PropertyValueFactory<>("név"));
        szőlőCol.setCellValueFactory(
            new PropertyValueFactory<>("szőlőTípus"));
        évCol.setCellValueFactory(
            new PropertyValueFactory<>("évjárat"));
        cukorCol.setCellValueFactory(
            new PropertyValueFactory<>("liter"));
        alkCol.setCellValueFactory(
            new PropertyValueFactory<>("alkohol"));
        litCol.setCellValueFactory(
            new PropertyValueFactory<>("liter"));
        palackCol.setCellValueFactory(
            new PropertyValueFactory<>("db"));
        vesztCol.setCellValueFactory(
            new PropertyValueFactory<>("veszteség"));
        
        tvK.setItems(getOKBorList());
        tvK.getColumns().addAll(névCol, szőlőCol, évCol, cukorCol, alkCol, litCol, palackCol, vesztCol);
        
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
            new TableColumn<>("Mennyiség");
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
    public void removeÉBor(ÉrőBor éB){
        éBor.remove(éB);
    }
    
    public void addÉBor(Szőlőtípusok szőlő, String évj, Tárolók tár, double menny, String kezdet) {
        
       ÉrőBor éB = new ÉrőBor(szőlő,évj,tár,menny,kezdet);
       éBor.add(éB);
        
    }
    public ObservableList<ÉrőBor>getOÉBorList(){
        ObservableList<ÉrőBor> oÉBor = FXCollections.observableArrayList(éBor);
        return oÉBor;
    }
    public ObservableList<KészBor>getOKBorList(){
        ObservableList<KészBor> oKBor = FXCollections.observableArrayList(kBor);
        return oKBor;
    }

    public ArrayList<KészBor> getKBor() {
        return kBor;
    }

    public ArrayList<ÉrőBor> getéBor() {
        return éBor;
    }
    
    
    public void addKBor(String név,ÉrőBor éb, double alk, String cuk) {
        
       KészBor kb = new KészBor(név, éb, alk, cuk);
       kBor.add(kb);
        
    }
    public void saveData(){
        
        String jsonKBor = (gson.toJson(kBor));
        
        try (PrintWriter writer = new PrintWriter("keszborok.json")) {
                    writer.write(jsonKBor);                
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
        String jsonÉBor = (gson.toJson(éBor));
      
        try (PrintWriter writer = new PrintWriter("eroborok.json")) {
                writer.write(jsonÉBor);                
                
            } catch (Exception e) {
                e.printStackTrace();
            }   
    }
    private void feltölt(){
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("keszborok.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                kBor = gson.fromJson(mind,new TypeToken<ArrayList<KészBor>>(){}.getType());
                
//                for (KészBor készBor : kBor) {
//                    System.out.println(készBor);
//            }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("eroborok.json")))) {
            
                String s = "";
                String mind = "";
                while ((s = reader.readLine()) != null) {                    
                    mind += s;
                }
                
                éBor = gson.fromJson(mind,new TypeToken<ArrayList<ÉrőBor>>(){}.getType());
                
//                for (KészBor készBor : kBor) {
//                    System.out.println(készBor);
//            }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}

