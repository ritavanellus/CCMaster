/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalFiles;

/**
 *
 * @author Rita
 */
public class InfoWindow extends javax.swing.JFrame {

    /**
     * Creates new form InfoWindow
     */
    public InfoWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setTitle("Info - CCMaster");
        setBounds(new java.awt.Rectangle(200, 200, 0, 0));
        setName("frameInfos"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("<html><b>Welcome! </b><br>\nMake calculations for solution making or dilution of stock solutions, and save it to a txt file with CCMaster! <br>\n<br>\n<p align=\"justify\"><b>1. Calculations</b><br>\nSelect the panel you need, \"solid\" to calculate solution parameters, or \"liquid\" for the dilution of stocks. Type in the parameters and select the units, leaving only one field empty. \nYou can use the tab and the cursor keys. To perform the calculation, press the calculate button. \nTo clear the form, press Clear.\nYou may use commas as decimal separators, if you activate the checkbox on top. </p>\n<br>\n<p align=\"justify\"><b>2. Saving your solution into a txt file</b><br>\nActivate the Enable saving option box. \nYou can give a name to the solution, and insert it to the text by clicking on the Insert... button. \nIf you are calculating and saving multiple solutions, just insert the name of the new solution before adding the first line of it. \nDo the calculation as usual. Fill the Name of substance/Name of solvent fields (solid), or the Name of stock/Name of diluent fields (liquid). \nOnce everything is ready to save, hit the \"Add to Solution\" or the \"Add and Clear\" button. \nThe latter adds the line to the solution and clears the form thereafter.\nYou can add both solid and liquid lines into a solution, mix them as you need.\nWhen you have finished, hit the \"Save solution\" button and save the file as a txt. \nYou can open it with any text editor to edit or print. </p> \n<br>\n<b>About: </b><br>\n<br>\nCCMaster 1.0<br>\nAuthor: Rita Papp<br>\nv_vanellus@yahoo.com<br>\n15.11.2017. <br>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InfoWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InfoWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InfoWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InfoWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InfoWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
