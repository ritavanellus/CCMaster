package finalFiles;

import java.text.DecimalFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Rita
 */
public class TesterClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         DecimalFormat df= new DecimalFormat("####.00");
         System.out.println(df.format(999.9951));
                 }
    
}
