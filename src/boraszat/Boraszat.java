/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boraszat;

import boraszat.model.Bor;
import boraszat.model.Tárolók;
import boraszat.model.Agy;
import boraszat.model.KészBor;
import boraszat.model.Szőlőtípusok;
import boraszat.model.ÉrőBor;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Boraszat extends Application {
    
    private Stage stageRef;
    TabPane tp = new TabPane();
    Tab újTételFelv = new Tab("Új tétel felvétele", tételFelv());
    Tab készTételRögz = new Tab("Kész tétel rögzítése");
    Tab tbÉrőBorok = new Tab("Érés alatti borok");
    Tab tbKészBorok = new Tab("Kész borok");
    TableView<ÉrőBor> éBorTv = new TableView<>();
    TableView<KészBor> kBorTv = new TableView<>();
    
    Group loginRoot = new Group();
    BorderPane root = new BorderPane();
    
    Agy agy = new Agy();
    
    Gson gson = new Gson();
            
    Scene loginScene = new Scene(loginRoot,800,600);
    Scene mainScene = new Scene(root,800,600);
    
    @Override
    public void start(Stage primaryStage) {
        initLoginScene();
        
        stageRef = primaryStage;
        stageRef.setScene(loginScene);
        stageRef.setTitle("Borászat");
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
      
        tp.getTabs().add(tbKészBorok);
        tp.getTabs().add(tbÉrőBorok);
        
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
        
        TextField tfBorNév = new TextField();
        TextField tfAlkTart = new TextField();
        
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
        grid.add(btnKészTételMentés, 1, 7);

        btnKészTételMentés.setOnAction(action ->{
            
            boolean txtIsntDouble = true;
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
            }
                    
                if(!txtIsntDouble && mennyiség<15 && mennyiség>6){
                    System.out.println("belép az atomba");
               
//                KészBor kB = new KészBor(tfBorNév.getText(), agy.getÉToKBor(), Double.parseDouble(tfAlkTart.getText()), cbxCukor.getValue().toString());
//                    System.out.println(kB);
                agy.addKBor(tfBorNév.getText(),agy.getÉToKBor(), Double.parseDouble(tfAlkTart.getText()), cbxCukor.getValue().toString());
                    System.out.println(agy.getKBor());
                
                cbxCukor.setValue(null);
                tfBorNév.clear();
                tfAlkTart.clear();                
                agy.resetÉToKBor();
                
                tp.getTabs().removeAll(készTételRögz);
                System.out.println("miafasz");
                tp.getSelectionModel().select(tbKészBorok);
                
                kBorTv.getItems().clear();
                tbKészBorok.setContent(készBorPane());

//                String json = (gson.toJson(agy.getKBor()));
//                    System.out.println("atom2");
//            
//            
//                try (PrintWriter writer = new PrintWriter("keszborok.json")) {
//                    writer.write(json);                
//                
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                
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

//            ÉrőBor éB = new ÉrőBor(cbxSzőlő.getValue(), cbxÉvjárat.getValue(), cbxTár.getValue(), mennyiség, getDateYMD());
            
            
            
//            String json = (gson.toJson(agy.getéBor()));
//            System.out.println("atom");
//            
//            
//            try (PrintWriter writer = new PrintWriter("eroborok.json")) {
//                writer.write(json);                
//                
//            } catch (Exception e) {
//                e.printStackTrace();
//            }   
            });
        return grid;
    }
    
    private StackPane készBorPane(){
        StackPane sp = new StackPane();
        
        System.out.println(agy.getOKBorList());
       
        kBorTv = agy.getKBorTableData();
        
        Button btnBármi = new Button("aaa");

        HBox hb = new HBox(btnBármi);
        VBox vb = new VBox(kBorTv, hb);
        sp.getChildren().add(vb);
        
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
            éBorTv.getSelectionModel().clearSelection();            
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
        
        return sp;
    }
//    
//    private StackPane createView() {
//        StackPane sp = new StackPane();
//        Text t = new Text("text");
//        Button b = new Button("b");
//        Button tétel = new Button("Új tétel");
//        
//        tétel.setOnAction(action -> {
//            
//            tp.getTabs().add(újTételFelv);
//            
//        });
//        
//        TextField tf = new TextField();
//        
//        b.setOnAction(action -> {
//            // general + kiir fajlba
//            Bor [] borArr = new Bor[30];
//            for (int i = 0; i < 30; i++) {
//                borArr[i] = Bor.randomBor();
//            }
//            
//            String json = (gson.toJson(borArr));
//            
//            try (PrintWriter writer = new PrintWriter("borok.json")) {
//                writer.write(json);                
//                
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            
//        });
//        
//        Button b2 = new Button("kiir");
//        b2.setOnAction(action -> {
//            // beolvas fajlbol + kiir konzolra
//            try (BufferedReader reader = new BufferedReader(new FileReader(new File("borok.json")))) {
//                String s = "";
//                String mind = "";
//                while ((s = reader.readLine()) != null) {                    
//                    mind += s;
//                }
//                
//                Bor [] borok = gson.fromJson(mind, Bor[].class);
//                
//                for (Bor bs : borok) {
//                    System.out.println(bs);
//                }
//                
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        HBox hbox = new HBox(t,b,b2,tf,tétel);
//        sp.getChildren().add(hbox);
//        return sp;
//    }
}
