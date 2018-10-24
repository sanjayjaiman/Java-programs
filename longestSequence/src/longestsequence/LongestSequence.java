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
public class LongestSequence {
    
    /**
     * @param args the command line arguments
     */

    private static void findConsec(int index, ArrayList<Integer> list, ArrayList<Integer> consec) {
        Integer in = list.get(index);
        list.remove(index);
        for (int i = 0; i < list.size(); i++) {
            Integer num = list.get(i);
            if (num == in + 1) {
                consec.add(num);
                findConsec(i, list, consec);
            }
        }
    }
    private static ArrayList<Integer> getLongest(ArrayList<Integer> list) {
        ArrayList<Integer> longest = null;
        for (int i = 0; i < list.size(); i++) {
            Integer num = list.get(i);
            ArrayList<Integer> temp = new ArrayList<>(list);
            ArrayList<Integer> consec = new ArrayList<>();
            consec.add(num);
            findConsec(i, temp, consec);
//            System.out.println("Consec list for " + num + " = " + consec.toString() + "; length = " + consec.size());
            if (longest == null || consec.size() > longest.size()) {
                longest = consec;
            }
        }
        return longest;
    }
    private static ArrayList<Integer> getLongest_R(ArrayList<Integer> longest, int index, ArrayList<Integer> list) {
        if (index == list.size()) {
            return longest;
        }
        Integer num = list.get(index);
        ArrayList<Integer> temp = new ArrayList<>(list);
        ArrayList<Integer> consec = new ArrayList<>();
        consec.add(num);
        findConsec(index, temp, consec);
//        System.out.println("Consec list for " + num + " = " + consec.toString() + "; length = " + consec.size());
        if (longest == null || consec.size() > longest.size()) {
            longest = consec;
        }
        return getLongest_R(longest, index+1, list);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        int size = 0;
        ArrayList<Integer> l = null;
        if (args.length == 1) {
            String sz = args[0];
            size = Integer.parseInt(sz);
            System.out.println("Sze of array = " + size);
            l = newList.get(size);
        }
        else {
            l = newList.get();
        }
        System.out.println(l.toString());
        boolean recurse = true;
        ArrayList<Integer> longest = null;
        if (recurse) {
            longest = getLongest_R(longest, 0, l);
            System.out.println("Longest list (recusrion) = " + longest.toString() + "; length = " + longest.size());
        }

        longest = getLongest(l);

        System.out.println("Longest list = " + longest.toString() + "; length = " + longest.size());
    }
    
}
