package boraszat;


import boraszat.diagaram.Diagrammok;
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
import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
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
    Tab újTételFelv = new Tab("Új tétel felvétele");
    Tab újMunkásFelv = new Tab("Új munkás felvétele");
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
        cbxCukor.getSelectionModel().selectFirst();
        
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
            
            boolean alkoholJo = true;
            boolean árJo = true;
            boolean bor = false;
            Integer ár = -1;
            Double mennyiség = -1.0;
            
            /* ÁR SZÁME*/
            if (!(tfÁr.getText().trim().isEmpty())){
                try {
                    ár=Integer.parseInt(tfÁr.getText());
                    System.out.println("An integer");
                    árJo=false;                
                }       
                catch (NumberFormatException e) {    
                } 
            }
            /* ALKOHOL SZÁME */
            try {
                    System.out.println("parse baj");
                    mennyiség=Double.parseDouble(tfAlkTart.getText());
                    alkoholJo=false;
                }       
                catch (NumberFormatException e) {
                    
                }
            
            if(tfBorNév.getText().trim().isEmpty()){
                bor = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("A bor nem kapott nevet!");                
                alert.showAndWait();
                
            } else if(alkoholJo){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Alkoholtartalom nem lett megadva, vagy nem helyes érték!");
                alert.showAndWait();
                
            } else if(!alkoholJo && (mennyiség<6.1 || mennyiség>14.9) ){
                alkoholJo = true;
                System.out.println("rossz alkohol");
                System.out.println(mennyiség);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Az alkoholtartalomn 6 és 15 közötti értéket vár!");
                alert.showAndWait();
                
            } else if(árJo || ár<0) {
                System.out.println("ár nem jó");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Ár mező üres vagy nem szám!");
                alert.showAndWait();
                
            } else if(!árJo && ár<1){
                árJo = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Ár nem lehet 0 vagy kisebb 0-nál!");
                alert.showAndWait();  
                
            }
               
            if(!alkoholJo && 
                mennyiség<15 && 
                mennyiség>6 &&
                !bor    &&
                !árJo
                ){

                agy.addKBor(tfBorNév.getText(),agy.getÉToKBor(), Double.parseDouble(tfAlkTart.getText()),
                            cbxCukor.getValue().toString(), lblÉrésKezdVal.getText(), getDateYMD(), Integer.parseInt(tfÁr.getText()));
                
                System.out.println(agy.getKBor());          
                
                cbxCukor.setValue(null);
                tfBorNév.clear();
                tfAlkTart.clear();                
                agy.resetÉToKBor();
                
                tp.getTabs().removeAll(készTételRögz);
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
            boolean nev=false, date=false, anya=false;
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                sdf.parse(tfSzül.getText());
                tfDate=false;    
                } catch (ParseException ex) {}
  
            if(!(agy.isNév(tfNév.getText())) || tfNév.getText().trim().isEmpty()){  
                nev=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Név számokat nem tartalmazhat vagy a mező üres!");
                alert.showAndWait();
                
            } else if(tfDate || tfSzül.getText().trim().isEmpty()){ 
                date=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Születési dátum nem felelmeg az előírásnak (év-hónap-nap) vagy a mező üres!");    
                alert.showAndWait();
                
            } else if(!tfDate && !agy.isDátumValid(tfSzül.getText())){
                date=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Év-hónap-nap, nem naptár szerinti dátum!");            
                alert.showAndWait();
                
            } else if(!(agy.isNév(tfAnya.getText())) || tfAnya.getText().trim().isEmpty()){
                anya=true;
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
            if(!anya && !nev && !date){
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
        
        Label lblNév = new Label("Neve (Vezetéknév Keresztnév): ");
        Label lblSzül = new Label("Születési dátum (év-hónap-nap): ");
        Label lblAnya = new Label("Anyja neve (Vezetéknév Keresztnév): ");
        Label lblMunkak = new Label("Munkakör: ");
        Label lblIdőB = new Label("Időbeosztás: ");
        
        TextField tfNév = new TextField();
        TextField tfSzül = new TextField();
        TextField tfAnya = new TextField();
        
        ComboBox<Munkakör> cbxMunkak = new ComboBox<>();
        cbxMunkak.getItems().setAll(Munkakör.values());
        cbxMunkak.getSelectionModel().selectFirst();
        
        ComboBox<Időbeosztás> cbxIdőB = new ComboBox<>();
        cbxIdőB.getItems().setAll(Időbeosztás.values());
        cbxIdőB.getSelectionModel().selectFirst();
        
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
        boolean nev=false, date=false, anya=false;
            
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            sdf.parse(tfSzül.getText());
            tfDate=false;    
            } catch (ParseException ex) {
                
            }
            if(!(agy.isNév(tfNév.getText())) /*|| tfNév.getText().trim().isEmpty()*/){  
                nev=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Név számokat nem tartalmazhat vagy a mező üres!");
                alert.showAndWait();
                
            } else if(tfDate/* || tfSzül.getText().trim().isEmpty()*/){ 
                date=true;
                System.out.println(sdf.toString());
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Születési dátum nem felelmeg az előírásnak (év-hónap-nap) vagy a mező üres!");            
                alert.showAndWait();
                
            } else if(!tfDate && !agy.isDátumValid(tfSzül.getText())){
                date=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Év-hónap-nap, nem naptár szerinti dátum!");            
                alert.showAndWait();
                
            } else if(!(agy.isNév(tfAnya.getText())) /*|| tfAnya.getText().trim().isEmpty()*/){
                anya=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Anyja neve számokat nem tartalmazhat vagy a mező üres!");
                alert.showAndWait();
                
            }
            
            if(!anya && !nev && !date){
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
        cbxTár.getSelectionModel().selectFirst();
        
        ComboBox<Szőlőtípusok> cbxSzőlő = new ComboBox<>();
        cbxSzőlő.getItems().setAll(Szőlőtípusok.values());
        cbxSzőlő.getSelectionModel().selectFirst();
        
        ComboBox<String> cbxÉvjárat = new ComboBox<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy");
        LocalDate localDate = LocalDate.now();
        Integer year = Integer.parseInt(dtf.format(localDate));
        Integer lastYear = year-1;
        cbxÉvjárat.getItems().setAll(year.toString(), lastYear.toString());
        cbxÉvjárat.getSelectionModel().selectFirst();
        
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
       
            if(txtIsntInt || mennyiség<1){
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
            if(!txtIsntInt 
                && mennyiség>0
                ){
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
        
        Label lblVevőNév = new Label("Vevő neve(személy/cég): ");
        Label lblVevőCím = new Label("Vevő címe(város): ");
        Label lblVevőAdóSz = new Label("Vevő adószáma: ");
        Label lblVevőBankSz = new Label("Vevő bankszámlaszáma: ");
        
        TextField tfVNév = new TextField();
        TextField tfVCím = new TextField();
        TextField tfVAdóSz = new TextField();
        TextField tfVBankSz = new TextField();
        
        Button btnMentés = new Button("Kész (számla készítése)");
        
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
            boolean nev = false, cim = false;
            Integer mennyiség=-1;
            Integer mennyiség2=-1;
            
            /* SZÁME AZ ADÓSZ*/
            try {
                    Integer.parseInt(tfVAdóSz.getText());
                    System.out.println("adoszam parseolva");
                    AdóSzIsntInt=false;               
                }       
                catch (NumberFormatException e) {     
                }
            /* SZÁME A BANKSZ */
            try {
                    Integer.parseInt(tfVBankSz.getText());
                    System.out.println("banksz parseolva");
                    BankSzIsntInt=false;
                }       
                catch (NumberFormatException e) {
                }
       
            if(tfVNév.getText().trim().isEmpty()){
                nev = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Vevő nevét kötelező kitölteni!");
                alert.showAndWait();
                
            } else if(tfVCím.getText().trim().isEmpty() || !(agy.isRandomNév(tfVCím.getText()))){
                cim = true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Vevő címét kötelező kitölteni!");
                alert.showAndWait();
                
            }  else if(AdóSzIsntInt){
                System.out.println("adoszam nemjo");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Adószám mező üres vagy nem szám!");
                alert.showAndWait();
                
            }else  if(!(tfVAdóSz.getLength()==10) && !AdóSzIsntInt){
                AdóSzIsntInt=true;
                System.out.println("adoszam hossz hibe");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Ádószámnak 10 karakter hosszúnak kell lennie!");
                alert.showAndWait();
                    
            } else if(BankSzIsntInt){     
                System.out.println("bankszam nemjo");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Bankszámlaszám mező üres vagy nem szám!");
                alert.showAndWait(); 
                
            } else if(!(tfVBankSz.getLength()==8 && !BankSzIsntInt)){
                System.out.println("bankszam hossz hibe");
                BankSzIsntInt=true;
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Hibás mező!");
                alert.setHeaderText("Bankszámlaszámnak 8 karakter hosszúnak!");
                alert.showAndWait();
                
            }
            if (!BankSzIsntInt && 
                !AdóSzIsntInt &&
                !cim &&
                !nev
                ){
                
                System.out.println("lefutott");
                
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
                
                Desktop de = Desktop.getDesktop();
                
                try {
                        de.open(new File("szamlak/szamla_sorsz_"+agy.getSzámlaSorSzám()+".pdf"));
                } catch (IOException e) {
                }
                agy.sorSzámLép();
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
            újTételFelv.setContent(tételFelv());
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
            újMunkásFelv.setContent(munkásFelv());
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
    
    private BorderPane kimutatásPane(){
        
        BorderPane br = new BorderPane();
        
        Diagrammok dia = new Diagrammok();
        
        ComboBox cbxKimutatás = new ComboBox();
        cbxKimutatás.getItems().addAll(
                                "Dolgozók időbeosztás szerint",
                                "Érés alatti borok, faj és év szerint",
                                "Készborok mennyiség és cukra"
        );
        cbxKimutatás.getSelectionModel().selectFirst();
        
        Button btnDiaKészít = new Button("Megjelenítés");
        
        btnDiaKészít.setOnAction(action -> {
            
            br.setCenter(null);
            
            if(cbxKimutatás.getValue().equals("Dolgozók időbeosztás szerint")){
                br.setCenter(dia.createChartDolg());
            }
            else if(cbxKimutatás.getValue().equals("Érés alatti borok, faj és év szerint")){
                br.setCenter(dia.createChartÉBor());
            }
            else if(cbxKimutatás.getValue().equals("Készborok mennyiség és cukra")){
                br.setCenter(dia.createChartKBor());
            }
        });
        
        VBox vb = new VBox(cbxKimutatás, btnDiaKészít);
        vb.setAlignment(Pos.CENTER);
        
        br.setTop(vb);
        
        return br;
    } 
}
