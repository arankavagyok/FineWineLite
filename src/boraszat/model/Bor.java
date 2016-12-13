/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boraszat.model;

import java.util.Random;

public class Bor {
    String name;
    
    
    int evjarat;
    
    
    
    private static Random rand = new Random();
    
    
    public static Bor randomBor() {
        Bor bor = new Bor();
        bor.name = "";
        for (int i = 0; i < 20; i++) {
            bor.name += (char)('a' + rand.nextInt(26));            
        }
        bor.evjarat = (rand.nextInt(100) + 1950);
        return bor;
    }

    @Override
    public String toString() {
        return "Bor{" + "name=" + name + ", evjarat=" + evjarat + '}';
    }
    
    
    
}
