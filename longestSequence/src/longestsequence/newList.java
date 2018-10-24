/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longestsequence;

import java.util.*;

/**
 *
 * @author sanjay
 */
public class newList {    
    private static int SIZE = 30;
    private static ArrayList<Integer> list = new ArrayList<>();
    
    private static boolean found(int s) {
        for (Iterator<Integer> i = list.iterator(); i.hasNext();) {
            Integer l = i.next();
//            System.out.println("Compare " + l + " and " + s);
            if (l.equals(s)) {
                return true;
            }
        }
        return false;
    }
    private static int next() {
        Random random = new Random();        
        int rand = random.nextInt(SIZE*2);
        while (found(rand)) {
            rand = random.nextInt(SIZE*2);
        }
        return rand;
    }
    /**
     *
     * @return
     */
    public static ArrayList<Integer> get(int size) {    
        for (int i = 0; i < size; i++) {
            Integer I = next();
//            System.out.println("Adding " + I);
            list.add(I);
        }
        return list;
    }
    
    public static ArrayList<Integer> get() {
        return get(SIZE);
    }
}
