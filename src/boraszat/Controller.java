/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boraszat;

public class Controller {
    
    private static Controller instance;
    
    public String login_code;
    
//    public Tab készTételRögz(){
//        Tab tab = new Tab();
//        
//        return tab;
//    }
    
    
    private Controller() {};

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            System.out.println("Initing controller...");
        }
        return instance;
    }
    
    
    
}
