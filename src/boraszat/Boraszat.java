package boraszat;


import boraszat.model.Tárolók;
import boraszat.model.Agy;
import boraszat.model.Időbeosztás;
import boraszat.model.KészBor;
import boraszat.model.Munkakör;
import boraszat.model.Munkás;
import boraszat.model.PDFSzamla;
import boraszat.model.Szőlőtípusok;
import boraszat.model.ÉrőBor;
import com.google.gson.Gson;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Boraszat extends Application {
    
    private Stage stageRef;
    TabPane tp = new TabPane();
    Tab újTételFelv = new Tab("Új tétel felvétele", tételFelv());
    Tab újMunkásFelv = new Tab("Új munkás felvétele", munkásFelv());
    Tab mMódosítás = new Tab("Adatmódosítás");
    Tab készTételRögz = new Tab("Kész tétel rögzítése");
    Tab áruElad = new Tab("Tétel eladása");
    Tab tbÉrőBorok = new Tab("Érés alatti borok");
    Tab tbKészBorok = new Tab("Kész borok");
    Tab tbMunkások = new Tab("Munkások");
    Tab tbKimutatás = new Tab("Kimutatások");
    TableView<ÉrőBor> éBorTv = new TableView<>();
    TableView<KészBor> kBorTv = new TableView<>();
    TableView<Munkás> mTv = new TableView<>();
    
    Group loginRoot = new Group();
    BorderPane root = new BorderPane();
    
    Agy agy = new Agy();
    
    Gson gson = new Gson();
            
    Scene loginScene = new Scene(loginRoot,400,400);
    Scene mainScene = new Scene(root,1000,600);
    
    @Override
    public void start(Stage primaryStage) {
        initLoginScene();
        
        stageRef = primaryStage;
        stageRef.setScene(loginScene);
        stageRef.setTitle("FineWineLite");
        stageRef.setResizable(false);
        stageRef.show();
    }
    @Override
    public void stop(){
        agy.saveData();
        System.out.println("Stage is closing");     
    }
    
    public String getDateYMD(){
             
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateYMD=(dtf.format(localDate));
        
        return dateYMD;
    }
    
    public static void main(String[] args) {
        launch(args);
    }

    private void initLoginScene() {
        
        Circle C = new Circle(100);
        
        Label lbl_vlaami = new Label();
        TextField tf_login = new TextField();
        Button bt_login = new Button("Login!");
        
        lbl_vlaami.textProperty().bind(tf_login.textProperty());
                
                
        bt_login.setOnAction(action -> {
//            lbl_vlaami.setText(tf_login.getText());
            Controller.getInstance().login_code = tf_login.getText();
            initMainScene();
            stageRef.setScene(mainScene);
        });
        
        VBox vbox = new VBox(lbl_vlaami,tf_login,bt_login);
        StackPane sp_login = new StackPane(vbox);
        
        
        
        C.centerXProperty().bind(loginScene.widthProperty().divide(2.0d));
        C.centerYProperty().bind(loginScene.heightProperty().divide(2.0d));
        
        loginRoot.getChildren().addAll(sp_login,C);
    }

    private void initMainScene() {
        Label lbl_vlaami = new Label();

        tbKészBorok.setClosable(false);
        tbKészBorok.setContent(készBorPane());
        tbÉrőBorok.setClosable(false);
        tbÉrőBorok.setContent(érőBorPane());
        tbMunkások.setClosable(false);
        tbMunkások.setContent(munkásokPane()); 
        tbKimutatás.setClosable(false);
        tbKimutatás.setContent(kimutatásPane());
      
        tp.getTabs().add(tbKészBorok);
        tp.getTabs().add(tbÉrőBorok);
        tp.getTabs().add(tbMunkások);
        tp.getTabs().add(tbKimutatás);
       
        lbl_vlaami.setText(Controller.getInstance().login_code);
        VBox vbox = new VBox(lbl_vlaami);
        root.setTop(vbox);
        root.setCenter(tp);
    }
    
    private GridPane tételRögz(){
        
        GridPane grid = new GridPane();
        
        Label lblNév = new Label("Bor neve: ");
        Label lblTíp = new Label("Szőlő típusa: ");
        Label lblÉv = new Label("Évjárata: ");
        Label lblTár = new Label("Eddigi tárolás típusa: ");
        Label lblAlk = new Label("Alkoholtartalom %-ba (6.0< % <15.0): ");
        Label lblCukor = new Label ("Cukortartalom g/l-be: ");
        Label lblÉrésKezd = new Label("Bor érlelésének kezdete: ");
        Label lblÁr = new Label("Bor palackonkénti ára: ");
        
        TextField tfBorNév = new TextField();
        TextField tfAlkTart = new TextField();
        TextField tfÁr = new TextField();
        
        ComboBox cbxCukor = new ComboBox();
        cbxCukor.getItems().addAll(
                "<4 g/l Száraz",
                "4-12 g/l Félszáraz",
                "12-45 g/l Félédes",
                ">45 g/l Édes"
        );
        
        Label lblTípVal = new Label();
        lblTípVal.setText(agy.getÉToKBor().getSzőlőTípus().displayName());
        
        Label lblÉvVal = new Label();     
        lblÉvVal.setText(agy.getÉToKBor().getÉvjárat());
        
        Label lblTárVal = new Label();
        lblTárVal.setText(agy.getÉToKBor().getTárolás().displayName());
        
        Label lblÉrésKezdVal = new Label();
        lblÉrésKezdVal.setText(agy.getÉToKBor().getÉrésKezd());   
        
        Button btnKészTételMentés = new Button("Bor mentése");
        
        grid.add(lblNév, 0, 0);
        grid.add(tfBorNév, 1, 0);
        grid.add(lblTíp, 0, 1);
        grid.add(lblTípVal, 1, 1);
        grid.add(lblÉv, 0, 2);
        grid.add(lblÉvVal, 1, 2);
        grid.add(lblTár, 0, 3);
        grid.add(lblTárVal, 1, 3);
        grid.add(lblCukor, 0, 4);
        grid.add(cbxCukor, 1, 4);
        grid.add(lblAlk, 0, 5);
        grid.add(tfAlkTart, 1, 5);
        grid.add(lblÉrésKezd, 0, 6);
        grid.add(lblÉrésKezdVal, 1, 6);
        grid.add(lblÁr, 0, 7);
        grid.add(tfÁr, 1,7);
        grid.add(btnKészTételMentés, 1, 8);

        btnKészTételMentés.setOnAction(action ->{
            
            boolean txtIsntDouble = true;
            boolean txtIsntInt = true;
            Integer ár = -1;
            Double mennyiség = -1.0;
            
            if(tfBorNév.getText().trim().isEmpty()){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("A bor nem kapott nevet!");                
                alert.showAndWait();
            } else if(cbxCukor.getValue()==null){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Cukortartalom nem lett meghatározva!");
                alert.showAndWait();   
            } else if(txtIsntDouble || mennyiség<6 || mennyiség>15){
                try {
                    mennyiség=Double.parseDouble(tfAlkTart.getText());
                    txtIsntDouble=false;
                }       
                catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hibás mező!");
                    alert.setHeaderText("Alkoholtartalom nem lett megadva, vagy nem helyes érték!");
                
                    alert.showAndWait(); 
                }
                    if(!txtIsntDouble && mennyiség<6.1 || mennyiség>14.9 ){
                        System.out.println("belépett");
                        System.out.println(mennyiség);
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Hibás mező!");
                        alert.setHeaderText("Az alkoholtartalomn 6 és 15 közötti értéket vár!");
                
                        alert.showAndWait();
                }
            } else if(txtIsntInt || ár<0) {
                try {
                    ár=Integer.parseInt(tfÁr.getText());
                    System.out.println("An integer");
                    txtIsntInt=false;                
                }       
                catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hibás mező!");
                    alert.setHeaderText("Ár mező üres vagy nem szám!");
                
                    alert.showAndWait(); 
                }                
                    if(!txtIsntInt && ár<1){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Hibás mező!");
                        alert.setHeaderText("Ár nem lehet kisebb mint 0!");
                
                        alert.showAndWait();   
                }
            }
                    
                if(!txtIsntDouble && mennyiség<15 && mennyiség>6){
                    System.out.println("belép az atomba");
               
                agy.addKBor(tfBorNév.getText(),agy.getÉToKBor(), Double.parseDouble(tfAlkTart.getText()),
                            cbxCukor.getValue().toString(), lblÉrésKezdVal.getText(), getDateYMD(), Integer.parseInt(tfÁr.getText()));
                
                    System.out.println(agy.getKBor());          
                
                cbxCukor.setValue(null);
                tfBorNév.clear();
                tfAlkTart.clear();                
                agy.resetÉToKBor();
                
                tp.getTabs().removeAll(készTételRögz);
                System.out.println("miafasz");
                tp.getSelectionModel().select(tbKészBorok);
                
                agy.removeÉBor(éBorTv.getSelectionModel().getSelectedItem());
                éBorTv.getItems().removeAll(éBorTv.getSelectionModel().getSelectedItems());             
                éBorTv.getSelectionModel().clearSelection();
                
                kBorTv.getItems().clear();
                tbKészBorok.setContent(készBorPane());
                }                
        });


        return grid;
    }
    
    private GridPane mAdatMód(){
        
        GridPane grid = new GridPane();
        
        Label lblNév = new Label("Neve: ");
        Label lblSzül = new Label("Születési dátum: ");
        Label lblAnya = new Label("Anyja neve: ");
        Label lblMunkak = new Label("Munkakör: ");
        Label lblIdőB = new Label("Időbeosztás: ");
        
        TextField tfNév = new TextField(agy.getMMód().getNév());    
        TextField tfSzül = new TextField(agy.getMMód().getSzülDate());
        TextField tfAnya = new TextField(agy.getMMód().getAnyaNév());
        String kezdDate = agy.getMMód().getMunkaKezd();
        
        ComboBox<Munkakör> cbxMunkak = new ComboBox<>();
        cbxMunkak.getItems().setAll(Munkakör.values());
        cbxMunkak.setValue(agy.getMMód().getMk());
        
        ComboBox<Időbeosztás> cbxIdőB = new ComboBox<>();
        cbxIdőB.getItems().setAll(Időbeosztás.values());
        cbxIdőB.setValue(agy.getMMód().getIdőB());
        
        Button btnMentés = new Button("Mentés");
        Button btnMégse = new Button("Mégse");
        
        grid.add(lblNév, 0, 0);
        grid.add(tfNév, 1, 0);
        grid.add(lblSzül, 0, 1);
        grid.add(tfSzül, 1, 1);
        grid.add(lblAnya, 0, 2);
        grid.add(tfAnya, 1, 2);
        grid.add(lblMunkak, 0, 3);
        grid.add(cbxMunkak, 1, 3);
        grid.add(lblIdőB, 0, 4);
        grid.add(cbxIdőB, 1, 4);
        grid.add(btnMentés, 0, 6);
        grid.add(btnMégse, 1, 6);
        
        btnMégse.setOnAction(action -> {
            
            agy.resetMMód();
            
            tfNév.clear();
            tfSzül.clear();
            tfAnya.clear();
            cbxMunkak.setValue(null);
            cbxIdőB.setValue(null);
            
            tp.getTabs().removeAll(mMódosítás);
            System.out.println("miafasz");
            tp.getSelectionModel().select(tbMunkások);
            
            mTv.getItems().clear();
            tbMunkások.setContent(munkásokPane());
            
        });
        
        btnMentés.setOnAction(action ->{
            
            boolean tfDate=true;    
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                sdf.parse(tfSzül.getText());
                tfDate=false;    
                } catch (ParseException ex) {}
  
            if(!(agy.isString(tfNév.getText())) || tfNév.getText().trim().isEmpty()){  
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Név számokat nem tartalmazhat vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(tfDate || tfSzül.getText().trim().isEmpty()){ 

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Születési dátum nem felelmeg az előírásnak (év-hónap-nap) vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(!(agy.isString(tfAnya.getText())) || tfAnya.getText().trim().isEmpty()){
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Anyja neve számokat nem tartalmazhat vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(cbxMunkak.getValue()==null){

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Munkakör nem lett megnevezve!");
                
                alert.showAndWait();
                
            } else if(cbxIdőB.getValue()==null){
     
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Időbeosztás nem lett kiválasztva!");
                
                alert.showAndWait();
            }
            if(!(cbxIdőB.getValue()==null)){
                System.out.println("lefutott");
                
                agy.removeM(mTv.getSelectionModel().getSelectedItem());
                agy.resetMMód();;
                
                agy.addMunkás(tfNév.getText(), tfSzül.getText(), tfAnya.getText(), cbxMunkak.getValue(), cbxIdőB.getValue(), kezdDate);
                
                tfNév.clear();
                tfSzül.clear();
                tfAnya.clear();
                cbxMunkak.setValue(null);
                cbxIdőB.setValue(null);
 
                tp.getTabs().removeAll(mMódosítás);
                System.out.println("miafasz");
                tp.getSelectionModel().select(tbMunkások);
                
                mTv.getItems().clear();
                tbMunkások.setContent(munkásokPane());
            }     
        }); 
        return grid;   
    }
    
    private GridPane munkásFelv(){
        
        GridPane grid = new GridPane();
        
        Label lblNév = new Label("Neve: ");
        Label lblSzül = new Label("Születési dátum: ");
        Label lblAnya = new Label("Anyja neve: ");
        Label lblMunkak = new Label("Munkakör: ");
        Label lblIdőB = new Label("Időbeosztás: ");
        
        TextField tfNév = new TextField();
        TextField tfSzül = new TextField();
        TextField tfAnya = new TextField();
        
        ComboBox<Munkakör> cbxMunkak = new ComboBox<>();
        cbxMunkak.getItems().setAll(Munkakör.values());
        
        ComboBox<Időbeosztás> cbxIdőB = new ComboBox<>();
        cbxIdőB.getItems().setAll(Időbeosztás.values());
        
        Button btnMentés = new Button("Felvétel");
        
        
        grid.add(lblNév, 0, 0);
        grid.add(tfNév, 1, 0);
        grid.add(lblSzül, 0, 1);
        grid.add(tfSzül, 1, 1);
        grid.add(lblAnya, 0, 2);
        grid.add(tfAnya, 1, 2);
        grid.add(lblMunkak, 0, 3);
        grid.add(cbxMunkak, 1, 3);
        grid.add(lblIdőB, 0, 4);
        grid.add(cbxIdőB, 1, 4);
        grid.add(btnMentés, 1, 5);
        
        btnMentés.setOnAction(action ->{
            
        boolean tfDate=true;    
            
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(tfSzül.getText());
            tfDate=false;    
            } catch (ParseException ex) {
                
            }
  
            if(!(agy.isString(tfNév.getText())) || tfNév.getText().trim().isEmpty()){  
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Név számokat nem tartalmazhat vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(tfDate || tfSzül.getText().trim().isEmpty()){ 

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Születési dátum nem felelmeg az előírásnak (év-hónap-nap) vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(!(agy.isString(tfAnya.getText())) || tfAnya.getText().trim().isEmpty()){
                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Anyja neve számokat nem tartalmazhat vagy a mező üres!");
                
                alert.showAndWait();
                
            } else if(cbxMunkak.getValue()==null){

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Munkakör nem lett megnevezve!");
                
                alert.showAndWait();
                
            } else if(cbxIdőB.getValue()==null){
     
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Időbeosztás nem lett kiválasztva!");
                
                alert.showAndWait();
            }
            if(!(cbxIdőB.getValue()==null)){
                System.out.println("lefutott");
                agy.addMunkás(tfNév.getText(), tfSzül.getText(), tfAnya.getText(), cbxMunkak.getValue(), cbxIdőB.getValue(), getDateYMD());
                
                tfNév.clear();
                tfSzül.clear();
                tfAnya.clear();
                cbxMunkak.setValue(null);
                cbxIdőB.setValue(null);
 
                tp.getTabs().removeAll(újMunkásFelv);
                System.out.println("miafasz");
                tp.getSelectionModel().select(tbMunkások);
                
                mTv.getItems().clear();
                tbMunkások.setContent(munkásokPane());
            }
            
        });
        
        return grid;
    }
    
    private GridPane tételFelv(){
        
        GridPane grid = new GridPane();
        
        Label lblTíp = new Label("Szőlő típusa: ");
        Label lblÉv = new Label("Évjárata: ");
        Label lblTár = new Label("Tárolás típusa: ");
        Label lblMenny = new Label("Tárolandó mennyiség liter-ben: ");      
        
        ComboBox<Tárolók> cbxTár = new ComboBox<>();
        cbxTár.getItems().setAll(Tárolók.values());
        
        ComboBox<Szőlőtípusok> cbxSzőlő = new ComboBox<>();
        cbxSzőlő.getItems().setAll(Szőlőtípusok.values());
        
        ComboBox<String> cbxÉvjárat = new ComboBox<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate = LocalDate.now();
        Integer year = Integer.parseInt(dtf.format(localDate));
        Integer lastYear = year-1;
        cbxÉvjárat.getItems().setAll(year.toString(), lastYear.toString());
        
        TextField tfMenny = new TextField();
        
        Button btnMentés = new Button("Felvétel");
        
        grid.add(lblTíp, 0, 0);
        grid.add(cbxSzőlő, 1, 0);
        grid.add(lblÉv, 0, 1);
        grid.add(cbxÉvjárat, 1, 1);
        grid.add(lblTár, 0, 2);
        grid.add(cbxTár, 1, 2);
        grid.add(lblMenny, 0, 3);
        grid.add(tfMenny, 1, 3);
        grid.add(btnMentés, 1, 4);
        
        btnMentés.setOnAction(action ->{
            
            boolean txtIsntInt=true;
            Integer mennyiség=-1;           
       
            if(cbxSzőlő.getValue()==null){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Szőlőtípus nem kapott típust!");
                
                alert.showAndWait();
                
            } else if(cbxÉvjárat.getValue()==null){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Évjárat nem kapott értéket!");
                
                alert.showAndWait();
                
            } else if(cbxTár.getValue()==null){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Tároló nem kapott típust!");
                
                alert.showAndWait();
                
            } else if(txtIsntInt || mennyiség<1){
                System.out.println("ideselépbe");
                try {
                    mennyiség=Integer.parseInt(tfMenny.getText());
                    System.out.println("An integer");
                    txtIsntInt=false;                
                }       
                catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hibás mező!");
                    alert.setHeaderText("Mennyiség mező üres vagy nem szám!");
                
                    alert.showAndWait(); 
                }                
                    if(!txtIsntInt && mennyiség<1){
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Hibás mező!");
                        alert.setHeaderText("Mennyiség nem lehet kisebb mint 0!");
                
                        alert.showAndWait();   
                }
            }
            
            if(!txtIsntInt && mennyiség>0){
                agy.addÉBor(cbxSzőlő.getValue(), cbxÉvjárat.getValue(), cbxTár.getValue(), Integer.parseInt(tfMenny.getText()), getDateYMD());
                
                cbxSzőlő.setValue(null);
                cbxTár.setValue(null);
                cbxÉvjárat.setValue(null);
                tfMenny.clear();
                tp.getTabs().removeAll(újTételFelv);
                System.out.println("miafasz");
                tp.getSelectionModel().select(tbÉrőBorok);
                
                éBorTv.getItems().clear();
                tbÉrőBorok.setContent(érőBorPane());
            }
            });
        return grid;
    }
    
    private GridPane eladás(){
        
        System.out.println("szalmasorsz:"+agy.getSzámlaSorSzám());
        
        GridPane grid = new GridPane();
        Label lblEmpty = new Label("     ");
        
        Label lblNév = new Label("Bor neve: ");
        Label lblDb = new Label("Darabszám: ");
        Label lblÁr = new Label("Ár / db: ");
        
        Label lblNévVal = new Label();
        lblNévVal.setText(agy.getEladBor().getNév());       
        Label lblDbVal = new Label();
        lblDbVal.setText(Integer.toString(agy.getEladBor().getDb()));
        Label lblÁrVal = new Label();
        lblÁrVal.setText(Integer.toString(agy.getEladBor().getÁr()));
        
        /* VEVŐ ADATOK */
        
        Label lblVevőNév = new Label("Vevő neve: ");
        Label lblVevőCím = new Label("Vevő címe: ");
        Label lblVevőAdóSz = new Label("Vevő adszószáma: ");
        Label lblVevőBankSz = new Label("Vevő bankszámlaszáma: ");
        
        TextField tfVNév = new TextField();
        TextField tfVCím = new TextField();
        TextField tfVAdóSz = new TextField();
        TextField tfVBankSz = new TextField();
        
        Button btnMentés = new Button("Felvétel");
        
        grid.add(lblNév, 0, 0);
        grid.add(lblDb, 0, 1);
        grid.add(lblÁr, 0, 2);
        grid.add(lblNévVal, 1, 0);
        grid.add(lblDbVal, 1, 1);
        grid.add(lblÁrVal, 1, 2);
        
        grid.add(lblEmpty, 2, 0);
//        grid.add(lblEmpty, 2, 1);
//        grid.add(lblEmpty, 2, 2);
        
        grid.add(lblVevőNév, 3, 0);
        grid.add(lblVevőCím, 3, 1);
        grid.add(lblVevőAdóSz, 3, 2);
        grid.add(lblVevőBankSz, 3, 3);
        grid.add(tfVNév, 4, 0);
        grid.add(tfVCím, 4, 1);
        grid.add(tfVAdóSz, 4, 2);
        grid.add(tfVBankSz, 4, 3);

        grid.add(btnMentés, 3, 4);
        
        btnMentés.setOnAction(action ->{
            
            boolean AdóSzIsntInt=true;
            boolean BankSzIsntInt=true;
            boolean pipa = false;
            Integer mennyiség=-1;
            Integer mennyiség2=-1;
       
            if(tfVNév.getText().trim().isEmpty()){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Vevő nevét kötelező kitölteni!");
                
                alert.showAndWait();
                
            } else if(tfVCím.getText().trim().isEmpty()){                
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Vevő címét kötelező kitölteni!");
                
                alert.showAndWait();
                
            }  else if(AdóSzIsntInt || mennyiség<0){
                System.out.println("adoszam nemjo");
                try {
                    Integer.parseInt(tfVAdóSz.getText());
                    System.out.println("An integer");
                    AdóSzIsntInt=false;                
                }       
                catch (NumberFormatException e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Hibás mező!");
                    alert.setHeaderText("Adószám mező üres vagy nem szám!");
                
                    alert.showAndWait(); 
                }   
                if(BankSzIsntInt && !AdóSzIsntInt){     
                System.out.println("bankszam nemjo");
                    try {
                        Integer.parseInt(tfVBankSz.getText());
                        System.out.println("An integer");
                        BankSzIsntInt=false;
                    }       
                    catch (NumberFormatException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Hibás mező!");
                        alert.setHeaderText("Bankszámlaszám mező üres vagy nem szám!");
                
                        alert.showAndWait(); 
                    }
                }
            }  
            
            if (!BankSzIsntInt && !AdóSzIsntInt){        

                try { 
                    Document doc = new Document();
                    PdfWriter.getInstance(doc, new FileOutputStream(new File("szamlak/szamla_sorsz_"+agy.getSzámlaSorSzám()+".pdf")));                 
                  
                    doc.open();
                    
                    PDFSzamla számla = new PDFSzamla(doc, agy.getEladBor(), tfVNév.getText(),
                            tfVCím.getText(), tfVAdóSz.getText() , tfVBankSz.getText(), agy.getSzámlaSorSzám());
                    
                    doc.close();
                    
                } catch (DocumentException | FileNotFoundException e) {                  
                }
                
                agy.resetEladKBor();
                agy.sorSzámLép();
                
                tfVNév.clear();
                tfVCím.clear();
                tfVAdóSz.clear();
                tfVBankSz.clear();
                tp.getTabs().removeAll(áruElad);
                tp.getSelectionModel().select(tbKészBorok);
                
                agy.removeKBor(kBorTv.getSelectionModel().getSelectedItem());
                éBorTv.getItems().removeAll(kBorTv.getSelectionModel().getSelectedItems());             
                éBorTv.getSelectionModel().clearSelection();
                
                kBorTv.getItems().clear();
                tbKészBorok.setContent(készBorPane()); 
            }
                
        });
         
        return grid;
    }
    
    private StackPane készBorPane(){
        StackPane sp = new StackPane();

        kBorTv = agy.getKBorTableData();
        
        Button btnEladás = new Button("Kiválasztott tétel eladása");
        btnEladás.setOnAction(action ->{
            System.out.println(kBorTv.getSelectionModel().getSelectedItem());
            
            agy.áruElad(kBorTv.getSelectionModel().getSelectedItem().getNév(),
                        kBorTv.getSelectionModel().getSelectedItem().getDb(),
                        kBorTv.getSelectionModel().getSelectedItem().getÁr()     
            );
            áruElad.setContent(eladás());
            tp.getTabs().add(áruElad);
            tp.getSelectionModel().select(áruElad); 
        });
        
        HBox hb = new HBox(btnEladás);
        VBox vb = new VBox(kBorTv, hb);
        sp.getChildren().add(vb);
        
        btnEladás.disableProperty().bind(Bindings.isEmpty(kBorTv.getSelectionModel().getSelectedItems()));
        
        kBorTv.setRowFactory((TableView<KészBor> tv) -> {
            final TableRow<KészBor> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index >= 0 && index < kBorTv.getItems().size() && kBorTv.getSelectionModel().isSelected(index)  ) {
                    kBorTv.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;  
        });
        
        /* DUPLA SZELEKCIÓHOZ */
//        kBorTv.getSelectionModel().setSelectionMode(
//            SelectionMode.MULTIPLE
//        );
        
        return sp;
    }
    
    private StackPane érőBorPane(){
        
        StackPane sp = new StackPane();

        éBorTv = agy.getÉBorTableData();
     
        Button tétel = new Button("Új tétel");    
        tétel.setOnAction(action -> {        
            tp.getTabs().add(újTételFelv);
            tp.getSelectionModel().select(újTételFelv);
        });
        
        Button btnCreateKBor = new Button("Elkészült tétel");
        btnCreateKBor.setOnAction(action -> { 
            System.out.println(éBorTv.getSelectionModel().getSelectedItem());
            agy.tételÁtvitel(éBorTv.getSelectionModel().getSelectedItem().getSzőlőTípus(),
                            éBorTv.getSelectionModel().getSelectedItem().getÉvjárat(),
                            éBorTv.getSelectionModel().getSelectedItem().getTárolás(),
                            éBorTv.getSelectionModel().getSelectedItem().getLiter(),
                            éBorTv.getSelectionModel().getSelectedItem().getÉrésKezd()
                            );
            készTételRögz.setContent(tételRögz());
            tp.getTabs().add(készTételRögz);
            tp.getSelectionModel().select(készTételRögz);           
        });
        
        Button btnDelTétel = new Button("Tétel törlése");
        btnDelTétel.setOnAction(action -> {         
                agy.removeÉBor(éBorTv.getSelectionModel().getSelectedItem());
                éBorTv.getItems().removeAll(éBorTv.getSelectionModel().getSelectedItems());
                éBorTv.getSelectionModel().clearSelection();
            });
        
        btnDelTétel.disableProperty().bind(Bindings.isEmpty(éBorTv.getSelectionModel().getSelectedItems()));
        btnCreateKBor.disableProperty().bind(Bindings.isEmpty(éBorTv.getSelectionModel().getSelectedItems()));
      
        HBox hb = new HBox(tétel,btnCreateKBor, btnDelTétel);
        VBox vb = new VBox(éBorTv, hb);
        sp.getChildren().add(vb);
        
        éBorTv.setRowFactory((TableView<ÉrőBor> tv) -> {
            final TableRow<ÉrőBor> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index >= 0 && index < éBorTv.getItems().size() && éBorTv.getSelectionModel().isSelected(index)  ) {
                    éBorTv.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;  
        });
        
        return sp;
    }
    
    private StackPane munkásokPane(){
        
        StackPane sp = new StackPane();
        
        mTv = agy.getMunkásTableData();
  
        Button btnMÚj = new Button("Új munkás");
        btnMÚj.setOnAction(action -> {        
            tp.getTabs().add(újMunkásFelv);
            tp.getSelectionModel().select(újMunkásFelv);
        });
        
        Button btnMTörlés = new Button("Munkás törlése");
        btnMTörlés.setOnAction(action -> {         
                agy.removeM(mTv.getSelectionModel().getSelectedItem());
                mTv.getItems().removeAll(mTv.getSelectionModel().getSelectedItems());
                mTv.getSelectionModel().clearSelection();
            });
        
        Button btnMMódosít = new Button("Adatmódosítás");
        btnMMódosít.setOnAction(action -> {
            agy.mAdatMód(mTv.getSelectionModel().getSelectedItem().getNév(),
                         mTv.getSelectionModel().getSelectedItem().getSzülDate(),
                         mTv.getSelectionModel().getSelectedItem().getAnyaNév(),
                         mTv.getSelectionModel().getSelectedItem().getMk(),
                         mTv.getSelectionModel().getSelectedItem().getIdőB(),
                         mTv.getSelectionModel().getSelectedItem().getMunkaKezd()
                            );
            mMódosítás.setContent(mAdatMód());
                      
            tp.getTabs().add(mMódosítás);
            tp.getSelectionModel().select(mMódosítás);
        });
        
        btnMMódosít.disableProperty().bind(Bindings.isEmpty(mTv.getSelectionModel().getSelectedItems()));
        btnMTörlés.disableProperty().bind(Bindings.isEmpty(mTv.getSelectionModel().getSelectedItems()));
        
        HBox hb = new HBox(btnMÚj, btnMTörlés, btnMMódosít);
        VBox vb = new VBox(mTv, hb);        
        sp.getChildren().add(vb);
        
        mTv.setRowFactory((TableView<Munkás> tv) -> {
            final TableRow<Munkás> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                final int index = row.getIndex();
                if (index >= 0 && index < mTv.getItems().size() && mTv.getSelectionModel().isSelected(index)  ) {
                    mTv.getSelectionModel().clearSelection();
                    event.consume();
                }
            });
            return row;  
        });  
        
        return sp;        
    }
    
    private StackPane kimutatásPane(){
        
        StackPane sp = new StackPane();
        
        ComboBox cbxKimutatás = new ComboBox();
        
        return sp;
    } 
}
