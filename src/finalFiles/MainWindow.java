/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalFiles;

import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import javax.swing.JFileChooser;

/**
 *
 * @author Rita
 */
public class MainWindow extends javax.swing.JFrame {

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
    }

    //decimal format to display results
    DecimalFormat df = new DecimalFormat("####.00");

    //initializing the text that will be saved to txt file
    StringBuilder printStuff = new StringBuilder();
    final String lineBreak = System.getProperty("line.separator");

    /**
     * Method to check if there is exactly one text field that is empty. It
     * refers to the solid panel. The liquid panel has its own similar method.
     * Input values are not checked yet. Boolean true is returned, if 3 fields
     * contain text, and one does not.
     *
     * @return
     */
    public boolean inputCheckSolid() {
        int endSum = 0;
        String weight = txtWeight.getText();
        String mw = txtMW.getText();
        String vol = txtVolume.getText();
        String conc = txtConc.getText();

        if (weight.equals("")) {
            endSum++;
        }
        if (mw.equals("")) {
            endSum++;
        }
        if (vol.equals("")) {
            endSum++;
        }
        if (conc.equals("")) {
            endSum++;
        }

        return (endSum == 1);

    }

    /**
     * Checking if there is only 1 empty field on the liquid pane. Input values
     * are not checked yet.
     *
     * @return
     */
    public boolean inputCheckLiquid() {
        int endSum = 0;
        String stockV = txtStockV.getText();
        String stockCc = txtStockcc.getText();
        String finalV = txtFinalV.getText();
        String finalCc = txtFinalcc.getText();

        if (stockV.equals("")) {
            endSum++;
        }
        if (stockCc.equals("")) {
            endSum++;
        }
        if (finalV.equals("")) {
            endSum++;
        }
        if (finalCc.equals("")) {
            endSum++;
        }
        return (endSum == 1);
    }

    /**
     * Method to calculate wieght, when MW, Volume and CC inputs are given.
     */
    public void calcWeight() {
        try {
            //getting the texts from the text fields
            String mwText = txtMW.getText();
            String volumeText = txtVolume.getText();
            String concText = txtConc.getText();
            String mwIn;
            String volumeIn;
            String concIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                mwIn = mwText.replace(",", ".");
                volumeIn = volumeText.replace(",", ".");
                concIn = concText.replace(",", ".");
            } else {
                mwIn = mwText;
                volumeIn = volumeText;
                concIn = concText;
            }

            //getting the numbers for the calculation
            double mw = Double.parseDouble(mwIn);
            double volume = Double.parseDouble(volumeIn);
            double conc = Double.parseDouble(concIn);

            //getting the real volume
            String volUnit = (String) cbVolume.getSelectedItem();
            double volFactor;
            switch (volUnit) {
                case "mL":
                    volFactor = 0.001;
                    break;
                case "microL":
                    volFactor = 0.000001;
                    break;
                default:
                    volFactor = 1.0;
            }
            volume *= volFactor;

            //getting the real concentration
            String concUnit = (String) cbConc.getSelectedItem();
            double concFactor;
            switch (concUnit) {
                case "mM":
                    concFactor = 0.001;
                    break;
                case "microM":
                    concFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    concFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    concFactor = Math.pow(10, -12);
                    break;
                default:
                    concFactor = 1.0;
            }
            conc *= concFactor;

            //doing the calculation
            double weight = conc * volume * mw;

            //finding the adequate unit for the weight
            if (weight + 0.005 > 1000.0) {
                weight /= 1000.0;
                cbWeight.setSelectedItem("kg");
            } else if (weight + 0.005 > 1.0) {
                cbWeight.setSelectedItem("g");
            } else if (weight + 5.0 * Math.pow(10, -6) > 0.001) {
                weight *= 1000.0;
                cbWeight.setSelectedItem("mg");
            } else if (weight + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                weight *= 1000000.0;
                cbWeight.setSelectedItem("microg");
            }

            //checking the range and displaying the value in the textfield
            if (weight >= Math.pow(10, 6) || weight + 5.0 * Math.pow(10, -9) <= Math.pow(10, -6)) {
                lblMessageBar.setText("The result (Weight) is out of the display limits!");
            } else {
                lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtWeight.setText(df.format(weight));
                } else {
                    String display = ((df.format(weight)).replace(",", "."));
                    txtWeight.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }
    }

    /**
     * Method to calculate MW, when weight, volume and cc are given.
     */
    public void calcMW() {
        try {
            //getting the texts from the text fields
            String weightText = txtWeight.getText();
            String volumeText = txtVolume.getText();
            String concText = txtConc.getText();
            String weightIn;
            String volumeIn;
            String concIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                weightIn = weightText.replace(",", ".");
                volumeIn = volumeText.replace(",", ".");
                concIn = concText.replace(",", ".");
            } else {
                weightIn = weightText;
                volumeIn = volumeText;
                concIn = concText;
            }
            //getting the numbers for the calculation
            double weight = Double.parseDouble(weightIn);
            double volume = Double.parseDouble(volumeIn);
            double conc = Double.parseDouble(concIn);

            //getting the real weight
            String weightUnit = (String) cbWeight.getSelectedItem();
            double weightFactor;
            switch (weightUnit) {
                case "kg":
                    weightFactor = 1000.0;
                    break;
                case "mg":
                    weightFactor = 0.001;
                    break;
                case "microg":
                    weightFactor = 0.000001;
                    break;
                default:
                    weightFactor = 1.0;
            }
            weight *= weightFactor;

            //getting the real volume
            String volUnit = (String) cbVolume.getSelectedItem();
            double volFactor;
            switch (volUnit) {
                case "mL":
                    volFactor = 0.001;
                    break;
                case "microL":
                    volFactor = 0.000001;
                    break;
                default:
                    volFactor = 1.0;
            }
            volume *= volFactor;

            //getting the real concentration
            String concUnit = (String) cbConc.getSelectedItem();
            double concFactor;
            switch (concUnit) {
                case "mM":
                    concFactor = 0.001;
                    break;
                case "microM":
                    concFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    concFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    concFactor = Math.pow(10, -12);
                    break;
                default:
                    concFactor = 1.0;
            }
            conc *= concFactor;

            //calculation
            double mw = weight / (volume * conc);

            //checking the range 
            if (mw > 10000.0 || mw + 5.0 * Math.pow(10, -6) <= 0.001) {
                lblMessageBar.setText("The result (MW) is out of the display limits!");
            } //displaying the mw in the text field
            else {
                lblMessageBar.setText("Here you go!");
                String display;
                if (chbEuropeFriendly.isSelected()) {
                    display = df.format(mw);
                    txtMW.setText(display);
                } else {
                    display = ((df.format(mw)).replace(",", "."));
                    txtMW.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }

    }

    /**
     * Method to calculate volume if weight, mw and conc are given.
     */
    public void calcVolume() {
        try {
            String weightText = txtWeight.getText();
            String mwText = txtMW.getText();
            String concText = txtConc.getText();
            String weightIn;
            String mwIn;
            String concIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                weightIn = weightText.replace(",", ".");
                mwIn = mwText.replace(",", ".");
                concIn = concText.replace(",", ".");
            } else {
                weightIn = weightText;
                mwIn = mwText;
                concIn = concText;
            }
            //getting the numbers for the calculation
            double weight = Double.parseDouble(weightIn);
            double mw = Double.parseDouble(mwIn);
            double conc = Double.parseDouble(concIn);

            //getting the real weight
            String weightUnit = (String) cbWeight.getSelectedItem();
            double weightFactor;
            switch (weightUnit) {
                case "kg":
                    weightFactor = 1000.0;
                    break;
                case "mg":
                    weightFactor = 0.001;
                    break;
                case "microg":
                    weightFactor = 0.000001;
                    break;
                default:
                    weightFactor = 1.0;
            }
            weight *= weightFactor;

            //getting the real concentration
            String concUnit = (String) cbConc.getSelectedItem();
            double concFactor;
            switch (concUnit) {
                case "mM":
                    concFactor = 0.001;
                    break;
                case "microM":
                    concFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    concFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    concFactor = Math.pow(10, -12);
                    break;
                default:
                    concFactor = 1.0;
            }
            conc *= concFactor;

            //calculation
            double volume = weight / (mw * conc);

            //finding the adequate unit for the volume            
            if (volume + 0.005 > 1.0) {
                cbVolume.setSelectedItem("L");
            } else if (volume + 5.0 * Math.pow(10, -6) > 0.001) {
                cbVolume.setSelectedItem("mL");
                volume *= 1000.0;
            } else if (volume + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbVolume.setSelectedItem("microL");
                volume *= 1000000.0;
            }

            //checking the range and displaying the value in the textfield
            if (volume >= 1000.0 || volume + 5.0 * Math.pow(10, -9) <= Math.pow(10, -6)) {
                lblMessageBar.setText("The result (Volume) is out of the display limits!");
            } else {
                lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtVolume.setText(df.format(volume));
                } else {
                    String display = ((df.format(volume)).replace(",", "."));
                    txtVolume.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }

    }

    /**
     * Method to calculate the concentration if weight, mw and volume are given.
     */
    public void calcConc() {
        try {
            String weightText = txtWeight.getText();
            String mwText = txtMW.getText();
            String volumeText = txtVolume.getText();
            String weightIn;
            String mwIn;
            String volumeIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                weightIn = weightText.replace(",", ".");
                mwIn = mwText.replace(",", ".");
                volumeIn = volumeText.replace(",", ".");
            } else {
                weightIn = weightText;
                mwIn = mwText;
                volumeIn = volumeText;
            }
            //getting the numbers for the calculation
            double weight = Double.parseDouble(weightIn);
            double mw = Double.parseDouble(mwIn);
            double volume = Double.parseDouble(volumeIn);

            //getting the real weight
            String weightUnit = (String) cbWeight.getSelectedItem();
            double weightFactor;
            switch (weightUnit) {
                case "kg":
                    weightFactor = 1000.0;
                    break;
                case "mg":
                    weightFactor = 0.001;
                    break;
                case "microg":
                    weightFactor = 0.000001;
                    break;
                default:
                    weightFactor = 1.0;
            }
            weight *= weightFactor;

            //getting the real volume
            String volUnit = (String) cbVolume.getSelectedItem();
            double volFactor;
            switch (volUnit) {
                case "mL":
                    volFactor = 0.001;
                    break;
                case "microL":
                    volFactor = 0.000001;
                    break;
                default:
                    volFactor = 1.0;
            }
            volume *= volFactor;

            //calculating conc
            double conc = weight / (mw * volume);

            //finding the adequate unit for the cc
            if (conc + 0.005 > 1.0) {
                cbConc.setSelectedItem("M");
            } else if (conc + 5.0 * Math.pow(10, -6) > 0.001) {
                cbConc.setSelectedItem("mM");
                conc *= 1000.0;
            } else if (conc + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbConc.setSelectedItem("microM");
                conc *= Math.pow(10, 6);
            } else if (conc + 5.0 * Math.pow(10, -12) > Math.pow(10, -9)) {
                cbConc.setSelectedItem("nanoM");
                conc *= Math.pow(10, 9);
            } else if (conc + 5.0 * Math.pow(10, -15) > Math.pow(10, -12)) {
                cbConc.setSelectedItem("picoM");
                conc *= Math.pow(10, 12);
            }

            //checking the range and displaying the value in the cc text field
            if (conc >= 1000.0 || conc + 5.0 * Math.pow(10, -15) <= Math.pow(10, -12)) {
                lblMessageBar.setText("The result (Concentration) is out of the display limits");
            } else {
                lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtConc.setText(df.format(conc));
                } else {
                    String display = ((df.format(conc)).replace(",", "."));
                    txtConc.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }
    }

    /**
     * Method to calculate stockV, if stockCc, finalV and finalCc are given
     */
    public void calcStockV() {
        try {
            String stockCctxt = txtStockcc.getText();
            String finalVtxt = txtFinalV.getText();
            String finalCctxt = txtFinalcc.getText();
            String stockCcIn;
            String finalVIn;
            String finalCcIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                stockCcIn = stockCctxt.replace(",", ".");
                finalVIn = finalVtxt.replace(",", ".");
                finalCcIn = finalCctxt.replace(",", ".");
            } else {
                stockCcIn = stockCctxt;
                finalVIn = finalVtxt;
                finalCcIn = finalCctxt;
            }

            //getting the numbers for the calculation
            double stockCc = Double.parseDouble(stockCcIn);
            double finalV = Double.parseDouble(finalVIn);
            double finalCc = Double.parseDouble(finalCcIn);

            //getting the real stockCc
            String stockCcUnit = (String) cbStockcc.getSelectedItem();
            double stockCcFactor;
            switch (stockCcUnit) {
                case "mM":
                    stockCcFactor = 0.001;
                    break;
                case "microM":
                    stockCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    stockCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    stockCcFactor = Math.pow(10, -12);
                    break;
                default:
                    stockCcFactor = 1.0;
            }
            stockCc *= stockCcFactor;

            //getting the real finalV
            String finalVUnit = (String) cbFinalV.getSelectedItem();
            double finalVfactor;
            switch (finalVUnit) {
                case "mL":
                    finalVfactor = 0.001;
                    break;
                case "microL":
                    finalVfactor = 0.000001;
                    break;
                default:
                    finalVfactor = 1.0;
            }
            finalV *= finalVfactor;

            //getting the real finalCc
            String finalCcUnit = (String) cbFinalcc.getSelectedItem();
            double finalCcFactor;
            switch (finalCcUnit) {
                case "mM":
                    finalCcFactor = 0.001;
                    break;
                case "microM":
                    finalCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    finalCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    finalCcFactor = Math.pow(10, -12);
                    break;
                default:
                    finalCcFactor = 1.0;
            }
            finalCc *= finalCcFactor;

            //making the calculation
            double stockV = (finalCc * finalV) / stockCc;

            //finding the adequate unit for the stockV
            if (stockV + 0.005 > 1.0) {
                cbStockV.setSelectedItem("L");
            } else if (stockV + 5.0 * Math.pow(10, -6) > 0.001) {
                cbStockV.setSelectedItem("mL");
                stockV *= 1000.0;
            } else if (stockV + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbStockV.setSelectedItem("microL");
                stockV *= 1000000.0;
            }

            //checking the range and displaying the stockV
            if (stockV >= 1000.0 || stockV + 5.0 * Math.pow(10, -9) <= Math.pow(10, -6)) {
                lblMessageBar.setText("The result (Stock volume) is out of the display limits");
            } else {
                //lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtStockV.setText(df.format(stockV));
                } else {
                    String display = ((df.format(stockV)).replace(",", "."));
                    txtStockV.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }
    }

    /**
     * Method to calculate the stockCc, if stockV, finalV and finalCc are given
     */
    public void calcStockCc() {
        try {
            String stockVtxt = txtStockV.getText();
            String finalVtxt = txtFinalV.getText();
            String finalCctxt = txtFinalcc.getText();
            String stockVIn;
            String finalVIn;
            String finalCcIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                stockVIn = stockVtxt.replace(",", ".");
                finalVIn = finalVtxt.replace(",", ".");
                finalCcIn = finalCctxt.replace(",", ".");
            } else {
                stockVIn = stockVtxt;
                finalVIn = finalVtxt;
                finalCcIn = finalCctxt;
            }

            //getting the numbers for the calculation
            double stockV = Double.parseDouble(stockVIn);
            double finalV = Double.parseDouble(finalVIn);
            double finalCc = Double.parseDouble(finalCcIn);

            //getting the real stockV
            String stockVUnit = (String) cbStockV.getSelectedItem();
            double stockVfactor;
            switch (stockVUnit) {
                case "mL":
                    stockVfactor = 0.001;
                    break;
                case "microL":
                    stockVfactor = 0.000001;
                    break;
                default:
                    stockVfactor = 1.0;
            }
            stockV *= stockVfactor;

            //getting the real finalV
            String finalVUnit = (String) cbFinalV.getSelectedItem();
            double finalVfactor;
            switch (finalVUnit) {
                case "mL":
                    finalVfactor = 0.001;
                    break;
                case "microL":
                    finalVfactor = 0.000001;
                    break;
                default:
                    finalVfactor = 1.0;
            }
            finalV *= finalVfactor;

            //getting the real finalCc
            String finalCcUnit = (String) cbFinalcc.getSelectedItem();
            double finalCcFactor;
            switch (finalCcUnit) {
                case "mM":
                    finalCcFactor = 0.001;
                    break;
                case "microM":
                    finalCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    finalCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    finalCcFactor = Math.pow(10, -12);
                    break;
                default:
                    finalCcFactor = 1.0;
            }
            finalCc *= finalCcFactor;

            //making the calculation
            double stockCc = (finalCc * finalV) / stockV;

            //finding the adeqaue unit for the stockCc
            if (stockCc + 0.005 > 1.0) {
                cbStockcc.setSelectedItem("L");
            } else if (stockCc + 5.0 * Math.pow(10, -6) > 0.001) {
                cbStockcc.setSelectedItem("mM");
                stockCc *= 1000.0;
            } else if (stockCc + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbStockcc.setSelectedItem("microM");
                stockCc *= Math.pow(10, 6);
            } else if (stockCc + 5.0 * Math.pow(10, -12) > Math.pow(10, -9)) {
                cbStockcc.setSelectedItem("nanoM");
                stockCc *= Math.pow(10, 9);
            } else if (stockCc + 5.0 * Math.pow(10, -15) > Math.pow(10, -12)) {
                cbStockcc.setSelectedItem("picoM");
                stockCc *= Math.pow(10, 12);
            }

            //checking the range and displaying the stockV
            if (stockCc >= 1000.0 || stockCc + 5.0 * Math.pow(10, -15) <= Math.pow(10, -12)) {
                lblMessageBar.setText("The result (Stock cc) is out of the display limits");
            } else {
                lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtStockcc.setText(df.format(stockCc));
                } else {
                    String display = ((df.format(stockCc)).replace(",", "."));
                    txtStockcc.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }
    }

    /**
     * Method to calculate finalV, if stockV, stockCc and finalCc are given.
     */
    public void calcFinalV() {
        try {
            String stockCctxt = txtStockcc.getText();
            String stockVtxt = txtStockV.getText();
            String finalCctxt = txtFinalcc.getText();
            String stockCcIn;
            String stockVIn;
            String finalCcIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                stockCcIn = stockCctxt.replace(",", ".");
                stockVIn = stockVtxt.replace(",", ".");
                finalCcIn = finalCctxt.replace(",", ".");
            } else {
                stockCcIn = stockCctxt;
                stockVIn = stockVtxt;
                finalCcIn = finalCctxt;
            }

            //getting the numbers for the calculation
            double stockCc = Double.parseDouble(stockCcIn);
            double stockV = Double.parseDouble(stockVIn);
            double finalCc = Double.parseDouble(finalCcIn);

            //getting the real stockCc
            String stockCcUnit = (String) cbStockcc.getSelectedItem();
            double stockCcFactor;
            switch (stockCcUnit) {
                case "mM":
                    stockCcFactor = 0.001;
                    break;
                case "microM":
                    stockCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    stockCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    stockCcFactor = Math.pow(10, -12);
                    break;
                default:
                    stockCcFactor = 1.0;
            }
            stockCc *= stockCcFactor;

            //getting the real stockV
            String stockVUnit = (String) cbStockV.getSelectedItem();
            double stockVfactor;
            switch (stockVUnit) {
                case "mL":
                    stockVfactor = 0.001;
                    break;
                case "microL":
                    stockVfactor = 0.000001;
                    break;
                default:
                    stockVfactor = 1.0;
            }
            stockV *= stockVfactor;

            //getting the real finalCc
            String finalCcUnit = (String) cbFinalcc.getSelectedItem();
            double finalCcFactor;
            switch (finalCcUnit) {
                case "mM":
                    finalCcFactor = 0.001;
                    break;
                case "microM":
                    finalCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    finalCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    finalCcFactor = Math.pow(10, -12);
                    break;
                default:
                    finalCcFactor = 1.0;
            }
            finalCc *= finalCcFactor;

            //making the calculation
            double finalV = (stockCc * stockV) / finalCc;

            //finding the adequate unit for the finalV
            if (finalV + 0.005 > 1.0) {
                cbFinalV.setSelectedItem("L");
            } else if (finalV + 5.0 * Math.pow(10, -6) > 0.001) {
                cbFinalV.setSelectedItem("mL");
                finalV *= 1000.0;
            } else if (finalV + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbFinalV.setSelectedItem("microL");
                finalV *= 1000000.0;
            }

            //checking the range and displaying the finalV
            if (finalV >= 1000.0 || finalV + 5.0 * Math.pow(10, -9) <= Math.pow(10, -6)) {
                lblMessageBar.setText("The result (Final volume) is out of the display limits");
            } else {
                //lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtFinalV.setText(df.format(finalV));
                } else {
                    String display = ((df.format(finalV)).replace(",", "."));
                    txtFinalV.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }
    }

    /**
     * Method to calculate finalCc, if stockV, stockCc and finalV are given.
     */
    public void calcFinalCc() {
        try {
            String stockVtxt = txtStockV.getText();
            String finalVtxt = txtFinalV.getText();
            String stockCctxt = txtStockcc.getText();
            String stockVIn;
            String finalVIn;
            String stockCcIn;

            //implementing the Europe-friendly option
            if (chbEuropeFriendly.isSelected()) {
                stockVIn = stockVtxt.replace(",", ".");
                finalVIn = finalVtxt.replace(",", ".");
                stockCcIn = stockCctxt.replace(",", ".");
            } else {
                stockVIn = stockVtxt;
                finalVIn = finalVtxt;
                stockCcIn = stockCctxt;
            }

            //getting the numbers for the calculation
            double stockV = Double.parseDouble(stockVIn);
            double finalV = Double.parseDouble(finalVIn);
            double stockCc = Double.parseDouble(stockCcIn);

            //getting the real stockV
            String stockVUnit = (String) cbStockV.getSelectedItem();
            double stockVfactor;
            switch (stockVUnit) {
                case "mL":
                    stockVfactor = 0.001;
                    break;
                case "microL":
                    stockVfactor = 0.000001;
                    break;
                default:
                    stockVfactor = 1.0;
            }
            stockV *= stockVfactor;

            //getting the real finalV
            String finalVUnit = (String) cbFinalV.getSelectedItem();
            double finalVfactor;
            switch (finalVUnit) {
                case "mL":
                    finalVfactor = 0.001;
                    break;
                case "microL":
                    finalVfactor = 0.000001;
                    break;
                default:
                    finalVfactor = 1.0;
            }
            finalV *= finalVfactor;

            //getting the real stockCc
            String stockCcUnit = (String) cbStockcc.getSelectedItem();
            double stockCcFactor;
            switch (stockCcUnit) {
                case "mM":
                    stockCcFactor = 0.001;
                    break;
                case "microM":
                    stockCcFactor = Math.pow(10, -6);
                    break;
                case "nanoM":
                    stockCcFactor = Math.pow(10, -9);
                    break;
                case "picoM":
                    stockCcFactor = Math.pow(10, -12);
                    break;
                default:
                    stockCcFactor = 1.0;
            }
            stockCc *= stockCcFactor;

            //making the calculation
            double finalCc = (stockCc * stockV) / finalV;

            //finding the adeqaue unit for the finalCc
            if (finalCc + 0.005 > 1.0) {
                cbFinalcc.setSelectedItem("L");
            } else if (finalCc + 5.0 * Math.pow(10, -6) > 0.001) {
                cbFinalcc.setSelectedItem("mM");
                finalCc *= 1000.0;
            } else if (finalCc + 5.0 * Math.pow(10, -9) > Math.pow(10, -6)) {
                cbFinalcc.setSelectedItem("microM");
                finalCc *= Math.pow(10, 6);
            } else if (finalCc + 5.0 * Math.pow(10, -12) > Math.pow(10, -9)) {
                cbFinalcc.setSelectedItem("nanoM");
                finalCc *= Math.pow(10, 9);
            } else if (finalCc + 5.0 * Math.pow(10, -15) > Math.pow(10, -12)) {
                cbFinalcc.setSelectedItem("picoM");
                finalCc *= Math.pow(10, 12);
            }

            //checking the range and displaying the finalCc
            if (finalCc >= 1000.0 || finalCc + 5.0 * Math.pow(10, -15) <= Math.pow(10, -12)) {
                lblMessageBar.setText("The result (Final cc) is out of the display limits");
            } else {
                lblMessageBar.setText("Here you go!");
                if (chbEuropeFriendly.isSelected()) {
                    txtFinalcc.setText(df.format(finalCc));
                } else {
                    String display = ((df.format(finalCc)).replace(",", "."));
                    txtFinalcc.setText(display);
                }
            }
        } catch (NumberFormatException ex) {
            lblMessageBar.setText("<html>Invalid data in at least one of the fields!<br>"
                    + "Maybe you are using commas as decimal separators?");
        }

    }

    private void writeTextFile(Path p, String content) {

        try (BufferedWriter bw = Files.newBufferedWriter(p)) {
            bw.write(content);
        } catch (IOException ex) {
            lblMessageBar.setText("Something went wrong when writing the file!");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblWelcome = new javax.swing.JLabel();
        btnInfo = new javax.swing.JButton();
        lblMessageBar = new javax.swing.JLabel();
        tpCalculationArea = new javax.swing.JTabbedPane();
        pnSolid = new javax.swing.JPanel();
        lblWeight = new javax.swing.JLabel();
        txtWeight = new javax.swing.JTextField();
        cbWeight = new javax.swing.JComboBox<>();
        txtMW = new javax.swing.JTextField();
        txtVolume = new javax.swing.JTextField();
        cbVolume = new javax.swing.JComboBox<>();
        txtConc = new javax.swing.JTextField();
        cbConc = new javax.swing.JComboBox<>();
        lblMW = new javax.swing.JLabel();
        lblVolume = new javax.swing.JLabel();
        lblConc = new javax.swing.JLabel();
        txtNameSubstance = new javax.swing.JTextField();
        txtNameSolvent = new javax.swing.JTextField();
        btnCalcSolid = new javax.swing.JButton();
        btnClearSolid = new javax.swing.JButton();
        btnAddSolid = new javax.swing.JButton();
        btnAddClearSolid = new javax.swing.JButton();
        lblSubstance = new javax.swing.JLabel();
        lblSolvent = new javax.swing.JLabel();
        pnlLiquid = new javax.swing.JPanel();
        lblStockV = new javax.swing.JLabel();
        txtStockV = new javax.swing.JTextField();
        cbStockV = new javax.swing.JComboBox<>();
        txtStockcc = new javax.swing.JTextField();
        cbStockcc = new javax.swing.JComboBox<>();
        txtFinalV = new javax.swing.JTextField();
        cbFinalV = new javax.swing.JComboBox<>();
        txtFinalcc = new javax.swing.JTextField();
        cbFinalcc = new javax.swing.JComboBox<>();
        lblStockcc = new javax.swing.JLabel();
        lblFinalcc = new javax.swing.JLabel();
        lblFinalV = new javax.swing.JLabel();
        txtNameStock = new javax.swing.JTextField();
        txtNameDiluent = new javax.swing.JTextField();
        btnCalcLiquid = new javax.swing.JButton();
        btnAddClearLiquid = new javax.swing.JButton();
        btnClearLiquid = new javax.swing.JButton();
        btnAddLiquid = new javax.swing.JButton();
        lblStock = new javax.swing.JLabel();
        lblDiluent = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        txtNameSolution = new javax.swing.JTextField();
        chbEuropeFriendly = new javax.swing.JCheckBox();
        chbSavingEnable = new javax.swing.JCheckBox();
        btnInsertName = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CCMaster - the Solution");
        setBounds(new java.awt.Rectangle(100, 100, 0, 0));
        setName("CCMaster"); // NOI18N

        lblWelcome.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        lblWelcome.setText("Hi! For detailed info on how to use the program, click on the Info button!");

        btnInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btnInfo.setText("Info");
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
            }
        });

        lblMessageBar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblMessageBar.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblMessageBar.setText("<html>Messages <br>\nAnything happens in this box, stays in this box. ");
        lblMessageBar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        lblMessageBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 255)));
        lblMessageBar.setOpaque(true);

        tpCalculationArea.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        lblWeight.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblWeight.setText("Weight");

        cbWeight.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "kg", "g", "mg", "microg" }));
        cbWeight.setSelectedIndex(1);

        cbVolume.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "mL", "microL", " " }));

        cbConc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "mM", "microM", "nanoM", "picoM" }));

        lblMW.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblMW.setText("MW");

        lblVolume.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblVolume.setText("Volume");

        lblConc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblConc.setText("Concentration");

        txtNameSubstance.setText("Name (max 15 char)");
        txtNameSubstance.setEnabled(false);
        txtNameSubstance.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                substanceReactClick(evt);
            }
        });

        txtNameSolvent.setText("Name (max 15 char)");
        txtNameSolvent.setEnabled(false);
        txtNameSolvent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                solventReactClick(evt);
            }
        });

        btnCalcSolid.setText("CALCULATE");
        btnCalcSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcSolidActionPerformed(evt);
            }
        });
        btnCalcSolid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                reactEnterSolid(evt);
            }
        });

        btnClearSolid.setText("CLEAR");
        btnClearSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSolidActionPerformed(evt);
            }
        });

        btnAddSolid.setText("ADD to Solution");
        btnAddSolid.setEnabled(false);
        btnAddSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSolidActionPerformed(evt);
            }
        });

        btnAddClearSolid.setText("ADD and CLEAR");
        btnAddClearSolid.setEnabled(false);
        btnAddClearSolid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClearSolidActionPerformed(evt);
            }
        });

        lblSubstance.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSubstance.setText("Substance");
        lblSubstance.setEnabled(false);

        lblSolvent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblSolvent.setText("Solvent");
        lblSolvent.setEnabled(false);

        javax.swing.GroupLayout pnSolidLayout = new javax.swing.GroupLayout(pnSolid);
        pnSolid.setLayout(pnSolidLayout);
        pnSolidLayout.setHorizontalGroup(
            pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSolidLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnSolidLayout.createSequentialGroup()
                        .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnSolidLayout.createSequentialGroup()
                                .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblWeight))
                        .addGap(44, 44, 44)
                        .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMW, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMW))
                        .addGap(52, 52, 52)
                        .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnSolidLayout.createSequentialGroup()
                                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnSolidLayout.createSequentialGroup()
                                        .addComponent(txtVolume, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblVolume))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnSolidLayout.createSequentialGroup()
                                        .addComponent(txtConc, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbConc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lblConc))
                                .addGap(24, 24, 24))
                            .addGroup(pnSolidLayout.createSequentialGroup()
                                .addComponent(txtNameSolvent, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSolvent)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(pnSolidLayout.createSequentialGroup()
                        .addComponent(txtNameSubstance, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSubstance)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(pnSolidLayout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCalcSolid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAddSolid, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddClearSolid)
                    .addComponent(btnClearSolid, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnSolidLayout.setVerticalGroup(
            pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnSolidLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWeight)
                    .addComponent(lblMW)
                    .addComponent(lblVolume)
                    .addComponent(lblConc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbVolume, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtConc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbConc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMW, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNameSubstance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNameSolvent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSubstance)
                    .addComponent(lblSolvent))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCalcSolid)
                    .addComponent(btnClearSolid))
                .addGap(18, 18, 18)
                .addGroup(pnSolidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddSolid)
                    .addComponent(btnAddClearSolid))
                .addGap(36, 36, 36))
        );

        tpCalculationArea.addTab("Solid", pnSolid);

        lblStockV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblStockV.setText("Stock volume");

        cbStockV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "mL", "microL" }));

        cbStockcc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "mM", "microM", "nanoM", "picoM" }));

        cbFinalV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "L", "mL", "microL" }));

        cbFinalcc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "M", "mM", "microM", "nanoM", "picoM" }));

        lblStockcc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblStockcc.setText("Stock cc");

        lblFinalcc.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblFinalcc.setText("Final cc");

        lblFinalV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblFinalV.setText("Final volume");

        txtNameStock.setText("Name (max 15 char)");
        txtNameStock.setEnabled(false);
        txtNameStock.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                stockReactClick(evt);
            }
        });

        txtNameDiluent.setText("Name (max 15 char)");
        txtNameDiluent.setEnabled(false);
        txtNameDiluent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                diluentReactClick(evt);
            }
        });

        btnCalcLiquid.setText("CALCULATE");
        btnCalcLiquid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcLiquidActionPerformed(evt);
            }
        });
        btnCalcLiquid.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                reactEnterLiquid(evt);
            }
        });

        btnAddClearLiquid.setText("ADD and CLEAR");
        btnAddClearLiquid.setEnabled(false);
        btnAddClearLiquid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddClearLiquidActionPerformed(evt);
            }
        });

        btnClearLiquid.setText("CLEAR");
        btnClearLiquid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearLiquidActionPerformed(evt);
            }
        });

        btnAddLiquid.setText("ADD to Solution");
        btnAddLiquid.setEnabled(false);
        btnAddLiquid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddLiquidActionPerformed(evt);
            }
        });

        lblStock.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblStock.setText("Stock");
        lblStock.setEnabled(false);

        lblDiluent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDiluent.setText("Diluent");
        lblDiluent.setEnabled(false);

        javax.swing.GroupLayout pnlLiquidLayout = new javax.swing.GroupLayout(pnlLiquid);
        pnlLiquid.setLayout(pnlLiquidLayout);
        pnlLiquidLayout.setHorizontalGroup(
            pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLiquidLayout.createSequentialGroup()
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLiquidLayout.createSequentialGroup()
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLiquidLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblStockV)
                                    .addComponent(lblFinalV)))
                            .addGroup(pnlLiquidLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(txtStockV, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbStockV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(52, 52, 52)
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStockcc)
                            .addGroup(pnlLiquidLayout.createSequentialGroup()
                                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtStockcc, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtFinalcc, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbStockcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbFinalcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lblFinalcc)))
                    .addGroup(pnlLiquidLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txtFinalV, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbFinalV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNameStock)
                    .addComponent(txtNameDiluent, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStock)
                    .addComponent(lblDiluent))
                .addGap(92, 92, 92))
            .addGroup(pnlLiquidLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCalcLiquid, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                    .addComponent(btnAddLiquid, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(36, 36, 36)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAddClearLiquid)
                    .addComponent(btnClearLiquid, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(332, Short.MAX_VALUE))
        );
        pnlLiquidLayout.setVerticalGroup(
            pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLiquidLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblStockV)
                    .addComponent(lblStockcc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLiquidLayout.createSequentialGroup()
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtStockV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbStockV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbStockcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFinalV)
                            .addComponent(lblFinalcc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbFinalV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFinalcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbFinalcc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFinalV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlLiquidLayout.createSequentialGroup()
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNameStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStock))
                        .addGap(48, 48, 48)
                        .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNameDiluent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDiluent))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCalcLiquid)
                    .addComponent(btnClearLiquid))
                .addGap(18, 18, 18)
                .addGroup(pnlLiquidLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddLiquid)
                    .addComponent(btnAddClearLiquid))
                .addContainerGap())
        );

        tpCalculationArea.addTab("Liquid", pnlLiquid);

        btnSave.setText("Save solution");
        btnSave.setEnabled(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        txtNameSolution.setText("Solution name");
        txtNameSolution.setEnabled(false);
        txtNameSolution.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clickSolName(evt);
            }
        });

        chbEuropeFriendly.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        chbEuropeFriendly.setText("Use comma as decimal separator");

        chbSavingEnable.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        chbSavingEnable.setText("Enable saving");
        chbSavingEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chbSavingEnableActionPerformed(evt);
            }
        });

        btnInsertName.setText("Insert solution name");
        btnInsertName.setEnabled(false);
        btnInsertName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblWelcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(47, 47, 47)
                        .addComponent(btnInfo)
                        .addGap(59, 59, 59))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tpCalculationArea, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(chbEuropeFriendly)
                                .addGap(66, 66, 66)
                                .addComponent(chbSavingEnable))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtNameSolution, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnInsertName)
                                .addGap(115, 115, 115)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblMessageBar, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWelcome)
                    .addComponent(btnInfo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chbEuropeFriendly)
                    .addComponent(chbSavingEnable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(txtNameSolution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInsertName))
                .addGap(18, 18, 18)
                .addComponent(tpCalculationArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblMessageBar, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoActionPerformed
        InfoWindow frameInfos = new InfoWindow();
        frameInfos.setVisible(true);
    }//GEN-LAST:event_btnInfoActionPerformed

    private void btnClearSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSolidActionPerformed
        txtWeight.setText("");
        txtMW.setText("");
        txtVolume.setText("");
        txtConc.setText("");
        cbWeight.setSelectedItem("g");
        cbVolume.setSelectedItem("L");
        cbConc.setSelectedItem("M");
    }//GEN-LAST:event_btnClearSolidActionPerformed

    private void btnCalcSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcSolidActionPerformed
        if (inputCheckSolid() == false) {
            lblMessageBar.setText("Something is wrong with the input. Please type values in 3 fields and leave 1 empty!");
        } else {
            if (txtWeight.getText().equals("")) {
                calcWeight();
                txtWeight.requestFocus();
                txtWeight.selectAll();
            } else if (txtMW.getText().equals("")) {
                calcMW();
                txtMW.requestFocus();
                txtMW.selectAll();
            } else if (txtVolume.getText().equals("")) {
                calcVolume();
                txtVolume.requestFocus();
                txtVolume.selectAll();
            } else {
                calcConc();
                txtConc.requestFocus();
                txtConc.selectAll();
            }
        }

    }//GEN-LAST:event_btnCalcSolidActionPerformed


    private void chbSavingEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chbSavingEnableActionPerformed
        if (chbSavingEnable.isSelected()) {
            txtNameSubstance.setEnabled(true);
            txtNameSolvent.setEnabled(true);
            btnSave.setEnabled(true);
            txtNameSolution.setEnabled(true);
            btnAddSolid.setEnabled(true);
            btnAddClearSolid.setEnabled(true);
            btnInsertName.setEnabled(true);
            btnAddClearLiquid.setEnabled(true);
            btnAddLiquid.setEnabled(true);
            txtNameStock.setEnabled(true);
            txtNameDiluent.setEnabled(true);
            lblSubstance.setEnabled(true);
            lblSolvent.setEnabled(true);
            lblStock.setEnabled(true);
            lblDiluent.setEnabled(true);
        } else {
            txtNameSubstance.setEnabled(false);
            txtNameSolvent.setEnabled(false);
            btnSave.setEnabled(false);
            txtNameSolution.setEnabled(false);
            btnAddSolid.setEnabled(false);
            btnAddClearSolid.setEnabled(false);
            btnInsertName.setEnabled(false);
            btnAddClearLiquid.setEnabled(false);
            btnAddLiquid.setEnabled(false);
            txtNameStock.setEnabled(false);
            txtNameDiluent.setEnabled(false);
            lblSubstance.setEnabled(false);
            lblSolvent.setEnabled(false);
            lblStock.setEnabled(false);
            lblDiluent.setEnabled(false);
        }
    }//GEN-LAST:event_chbSavingEnableActionPerformed

    private void btnClearLiquidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearLiquidActionPerformed
        txtStockV.setText("");
        txtStockcc.setText("");
        txtFinalV.setText("");
        txtFinalcc.setText("");
        cbStockV.setSelectedItem("L");
        cbStockcc.setSelectedItem("M");
        cbFinalV.setSelectedItem("L");
        cbFinalcc.setSelectedItem("M");
    }//GEN-LAST:event_btnClearLiquidActionPerformed

    private void btnCalcLiquidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcLiquidActionPerformed
        if (inputCheckLiquid() == false) {
            lblMessageBar.setText("Something is wrong with the input. Please type values in 3 fields and leave 1 empty!");
        } else {
            lblMessageBar.setText("Here you go!");
            if (txtStockV.getText().equals("")) {
                calcStockV();
                txtStockV.requestFocus();
                txtStockV.selectAll();
            } else if (txtStockcc.getText().equals("")) {
                calcStockCc();
                txtStockcc.requestFocus();
                txtStockcc.selectAll();
            } else if (txtFinalV.getText().equals("")) {
                calcFinalV();
                txtFinalV.requestFocus();
                txtFinalV.selectAll();
            } else {
                calcFinalCc();
                txtFinalcc.requestFocus();
                txtFinalcc.selectAll();
            }
        }
    }//GEN-LAST:event_btnCalcLiquidActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        JFileChooser fc = new JFileChooser();
        int answer = fc.showSaveDialog(this);
        if (answer == JFileChooser.APPROVE_OPTION) {
            writeTextFile(fc.getSelectedFile().toPath(), printStuff.toString());
        }

        //line used during development only
        //System.out.println(printStuff.toString());
    }//GEN-LAST:event_btnSaveActionPerformed


    private void btnInsertNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertNameActionPerformed
        if (printStuff.length() == 0) {
            printStuff.append(txtNameSolution.getText());
            printStuff.append(lineBreak);
            lblMessageBar.setText("The solution name has been inserted");
        } else {
            printStuff.append(lineBreak);
            printStuff.append(txtNameSolution.getText());
            printStuff.append(lineBreak);
            lblMessageBar.setText("The solution name has been inserted");
        }
    }//GEN-LAST:event_btnInsertNameActionPerformed

    private void btnAddSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSolidActionPerformed
        if (txtNameSubstance.getText().length() > 15 || txtNameSolvent.getText().length() > 15) {
            lblMessageBar.setText("<html>The name of the substance or the name of the solvent is longer than 15 characters!<br>"
                    + "The line has not been added. Please change and try it again.");
        } else {
            try {
                //getting the texts from the text fields
                String weightText = txtWeight.getText();
                String mwText = txtMW.getText();
                String volumeText = txtVolume.getText();
                String concText = txtConc.getText();
                String weightIn;
                String mwIn;
                String volumeIn;
                String concIn;

                //implementing the Europe-friendly option
                if (chbEuropeFriendly.isSelected()) {
                    weightIn = weightText.replace(",", ".");
                    mwIn = mwText.replace(",", ".");
                    volumeIn = volumeText.replace(",", ".");
                    concIn = concText.replace(",", ".");
                } else {
                    weightIn = weightText;
                    mwIn = mwText;
                    volumeIn = volumeText;
                    concIn = concText;
                }

                //getting the numbers for the calculation
                double weight = Double.parseDouble(weightIn);
                double mw = Double.parseDouble(mwIn);
                double volume = Double.parseDouble(volumeIn);
                double conc = Double.parseDouble(concIn);

                String weightF = String.format("|%7.2f  ", weight);
                printStuff.append(weightF);
                String weightUnitF = String.format("%-7s", cbWeight.getSelectedItem());
                printStuff.append(weightUnitF);
                String substanceF = String.format("%-15s|", txtNameSubstance.getText());
                printStuff.append(substanceF);
                String mwF = String.format("MW =%9.2f |", mw);
                printStuff.append(mwF);
                String volumeF = String.format("%7.2f  ", volume);
                printStuff.append(volumeF);
                String volUnitF = String.format("%-7s", cbVolume.getSelectedItem());
                printStuff.append(volUnitF);
                String solventF = String.format("%-15s|", txtNameSolvent.getText());
                printStuff.append(solventF);
                String concF = String.format("%7.2f  ", conc);
                printStuff.append(concF);
                String cUnitF = String.format("%-7s|", cbConc.getSelectedItem());
                printStuff.append(cUnitF);
                printStuff.append(lineBreak);

                lblMessageBar.setText("The line was added to the solution");

            } catch (NumberFormatException ex) {
                lblMessageBar.setText("Something went wrong when adding the line to the solution");
            }
        }


    }//GEN-LAST:event_btnAddSolidActionPerformed

    private void btnAddClearSolidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClearSolidActionPerformed

        if (txtNameSubstance.getText().length() > 15 || txtNameSolvent.getText().length() > 15) {
            lblMessageBar.setText("<html>The name of the substance or the name of the solvent is longer than 15 characters!<br>"
                    + "The line has not been added. Please change and try it again.");
        } else {
            try {
                //getting the texts from the text fields
                String weightText = txtWeight.getText();
                String mwText = txtMW.getText();
                String volumeText = txtVolume.getText();
                String concText = txtConc.getText();
                String weightIn;
                String mwIn;
                String volumeIn;
                String concIn;

                //implementing the Europe-friendly option
                if (chbEuropeFriendly.isSelected()) {
                    weightIn = weightText.replace(",", ".");
                    mwIn = mwText.replace(",", ".");
                    volumeIn = volumeText.replace(",", ".");
                    concIn = concText.replace(",", ".");
                } else {
                    weightIn = weightText;
                    mwIn = mwText;
                    volumeIn = volumeText;
                    concIn = concText;
                }

                //getting the numbers for the calculation
                double weight = Double.parseDouble(weightIn);
                double mw = Double.parseDouble(mwIn);
                double volume = Double.parseDouble(volumeIn);
                double conc = Double.parseDouble(concIn);

                String weightF = String.format("|%7.2f  ", weight);
                printStuff.append(weightF);
                String weightUnitF = String.format("%-7s", cbWeight.getSelectedItem());
                printStuff.append(weightUnitF);
                String substanceF = String.format("%-15s|", txtNameSubstance.getText());
                printStuff.append(substanceF);
                String mwF = String.format("MW =%9.2f |", mw);
                printStuff.append(mwF);
                String volumeF = String.format("%7.2f  ", volume);
                printStuff.append(volumeF);
                String volUnitF = String.format("%-7s", cbVolume.getSelectedItem());
                printStuff.append(volUnitF);
                String solventF = String.format("%-15s|", txtNameSolvent.getText());
                printStuff.append(solventF);
                String concF = String.format("%7.2f  ", conc);
                printStuff.append(concF);
                String cUnitF = String.format("%-7s|", cbConc.getSelectedItem());
                printStuff.append(cUnitF);
                printStuff.append(lineBreak);

                lblMessageBar.setText("The line was added to the solution");

                txtWeight.setText("");
                txtMW.setText("");
                txtVolume.setText("");
                txtConc.setText("");
                cbWeight.setSelectedItem("g");
                cbVolume.setSelectedItem("L");
                cbConc.setSelectedItem("M");

            } catch (NumberFormatException ex) {
                lblMessageBar.setText("Something went wrong when adding the line to the solution");
            }
        }

    }//GEN-LAST:event_btnAddClearSolidActionPerformed


    private void reactEnterSolid(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reactEnterSolid
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputCheckSolid() == false) {
                lblMessageBar.setText("Something is wrong with the input. Please type values in 3 fields and leave 1 empty!");
            } else {
                if (txtWeight.getText().equals("")) {
                    calcWeight();
                    txtWeight.requestFocus();
                    txtWeight.selectAll();
                } else if (txtMW.getText().equals("")) {
                    calcMW();
                    txtMW.requestFocus();
                    txtMW.selectAll();
                } else if (txtVolume.getText().equals("")) {
                    calcVolume();
                    txtVolume.requestFocus();
                    txtVolume.selectAll();
                } else {
                    calcConc();
                    txtConc.requestFocus();
                    txtConc.selectAll();
                }
            }
        }
    }//GEN-LAST:event_reactEnterSolid

    private void reactEnterLiquid(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_reactEnterLiquid
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (inputCheckLiquid() == false) {
                lblMessageBar.setText("Something is wrong with the input. Please type values in 3 fields and leave 1 empty!");
            } else {
                lblMessageBar.setText("Here you go!");
                if (txtStockV.getText().equals("")) {
                    calcStockV();
                    txtStockV.requestFocus();
                    txtStockV.selectAll();
                } else if (txtStockcc.getText().equals("")) {
                    calcStockCc();
                    txtStockcc.requestFocus();
                    txtStockcc.selectAll();
                } else if (txtFinalV.getText().equals("")) {
                    calcFinalV();
                    txtFinalV.requestFocus();
                    txtFinalV.selectAll();
                } else {
                    calcFinalCc();
                    txtFinalcc.requestFocus();
                    txtFinalcc.selectAll();
                }
            }
        }
    }//GEN-LAST:event_reactEnterLiquid

    private void clickSolName(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clickSolName
        txtNameSolution.selectAll();
    }//GEN-LAST:event_clickSolName

    private void btnAddLiquidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddLiquidActionPerformed
        if (txtNameStock.getText().length() > 15 || txtNameDiluent.getText().length() > 15) {
            lblMessageBar.setText("<html>The name of the stock or the name of the diluent is longer than 15 characters!<br>"
                    + "The line has not been added. Please change and try it again.");
        } else {
            try {
                //getting the texts from the text fields
                String stockVText = txtStockV.getText();
                String stockCcText = txtStockcc.getText();
                String finalVText = txtFinalV.getText();
                String finalCcText = txtFinalcc.getText();
                String stockVIn;
                String stockCcIn;
                String finalVIn;
                String finalCcIn;

                //implementing the Europe-friendly option
                if (chbEuropeFriendly.isSelected()) {
                    stockVIn = stockVText.replace(",", ".");
                    stockCcIn = stockCcText.replace(",", ".");
                    finalVIn = finalVText.replace(",", ".");
                    finalCcIn = finalCcText.replace(",", ".");
                } else {
                    stockVIn = stockVText;
                    stockCcIn = stockCcText;
                    finalVIn = finalVText;
                    finalCcIn = finalCcText;
                }

                //getting the numbers for the calculation
                double stockV = Double.parseDouble(stockVIn);
                double stockCc = Double.parseDouble(stockCcIn);
                double finalV = Double.parseDouble(finalVIn);
                double finalCc = Double.parseDouble(finalCcIn);

                String stockVF = String.format("|%7.2f  ", stockV);
                printStuff.append(stockVF);
                String stockVUnitF = String.format("%-7s", cbStockV.getSelectedItem());
                printStuff.append(stockVUnitF);
                String stockF = String.format("%-15s|", txtNameStock.getText());
                printStuff.append(stockF);
                String stockCcF = String.format("%7.2f", stockCc);
                printStuff.append(stockCcF);
                String stockCcUnitF = String.format(" %-6s|", cbStockcc.getSelectedItem());
                printStuff.append(stockCcUnitF);
                String finalVF = String.format("%7.2f  ", finalV);
                printStuff.append(finalVF);
                String finalVUnitF = String.format("%-7s", cbFinalV.getSelectedItem());
                printStuff.append(finalVUnitF);
                String diluentF = String.format("%-15s|", txtNameDiluent.getText());
                printStuff.append(diluentF);
                String finalCcF = String.format("%7.2f  ", finalCc);
                printStuff.append(finalCcF);
                String finalCcUnitF = String.format("%-7s|", cbFinalcc.getSelectedItem());
                printStuff.append(finalCcUnitF);
                printStuff.append(lineBreak);

                lblMessageBar.setText("The line was added to the solution");

            } catch (NumberFormatException ex) {
                lblMessageBar.setText("Something went wrong when adding the line to the solution");
            }
        }
    }//GEN-LAST:event_btnAddLiquidActionPerformed

    private void btnAddClearLiquidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddClearLiquidActionPerformed
        if (txtNameStock.getText().length() > 15 || txtNameDiluent.getText().length() > 15) {
            lblMessageBar.setText("<html>The name of the stock or the name of the diluent is longer than 15 characters!<br>"
                    + "The line has not been added. Please change and try it again.");
        } else {
            try {
                //getting the texts from the text fields
                String stockVText = txtStockV.getText();
                String stockCcText = txtStockcc.getText();
                String finalVText = txtFinalV.getText();
                String finalCcText = txtFinalcc.getText();
                String stockVIn;
                String stockCcIn;
                String finalVIn;
                String finalCcIn;

                //implementing the Europe-friendly option
                if (chbEuropeFriendly.isSelected()) {
                    stockVIn = stockVText.replace(",", ".");
                    stockCcIn = stockCcText.replace(",", ".");
                    finalVIn = finalVText.replace(",", ".");
                    finalCcIn = finalCcText.replace(",", ".");
                } else {
                    stockVIn = stockVText;
                    stockCcIn = stockCcText;
                    finalVIn = finalVText;
                    finalCcIn = finalCcText;
                }

                //getting the numbers for the calculation
                double stockV = Double.parseDouble(stockVIn);
                double stockCc = Double.parseDouble(stockCcIn);
                double finalV = Double.parseDouble(finalVIn);
                double finalCc = Double.parseDouble(finalCcIn);

                String stockVF = String.format("|%7.2f  ", stockV);
                printStuff.append(stockVF);
                String stockVUnitF = String.format("%-7s", cbStockV.getSelectedItem());
                printStuff.append(stockVUnitF);
                String stockF = String.format("%-15s|", txtNameStock.getText());
                printStuff.append(stockF);
                String stockCcF = String.format("%7.2f", stockCc);
                printStuff.append(stockCcF);
                String stockCcUnitF = String.format(" %-6s|", cbStockcc.getSelectedItem());
                printStuff.append(stockCcUnitF);
                String finalVF = String.format("%7.2f  ", finalV);
                printStuff.append(finalVF);
                String finalVUnitF = String.format("%-7s", cbFinalV.getSelectedItem());
                printStuff.append(finalVUnitF);
                String diluentF = String.format("%-15s|", txtNameDiluent.getText());
                printStuff.append(diluentF);
                String finalCcF = String.format("%7.2f  ", finalCc);
                printStuff.append(finalCcF);
                String finalCcUnitF = String.format("%-7s|", cbFinalcc.getSelectedItem());
                printStuff.append(finalCcUnitF);
                printStuff.append(lineBreak);

                lblMessageBar.setText("The line was added to the solution");

                txtStockV.setText("");
                txtStockcc.setText("");
                txtFinalV.setText("");
                txtFinalcc.setText("");
                cbStockV.setSelectedItem("L");
                cbStockcc.setSelectedItem("M");
                cbFinalV.setSelectedItem("L");
                cbFinalcc.setSelectedItem("M");
            } catch (NumberFormatException ex) {
                lblMessageBar.setText("Something went wrong when adding the line to the solution");
            }
        }
    }//GEN-LAST:event_btnAddClearLiquidActionPerformed

    private void solventReactClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_solventReactClick
        txtNameSolvent.selectAll();
    }//GEN-LAST:event_solventReactClick

    private void substanceReactClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_substanceReactClick
        txtNameSubstance.selectAll();
    }//GEN-LAST:event_substanceReactClick

    private void stockReactClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stockReactClick
        txtNameStock.selectAll();
    }//GEN-LAST:event_stockReactClick

    private void diluentReactClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_diluentReactClick
        txtNameDiluent.selectAll();
    }//GEN-LAST:event_diluentReactClick

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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddClearLiquid;
    private javax.swing.JButton btnAddClearSolid;
    private javax.swing.JButton btnAddLiquid;
    private javax.swing.JButton btnAddSolid;
    private javax.swing.JButton btnCalcLiquid;
    private javax.swing.JButton btnCalcSolid;
    private javax.swing.JButton btnClearLiquid;
    private javax.swing.JButton btnClearSolid;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnInsertName;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbConc;
    private javax.swing.JComboBox<String> cbFinalV;
    private javax.swing.JComboBox<String> cbFinalcc;
    private javax.swing.JComboBox<String> cbStockV;
    private javax.swing.JComboBox<String> cbStockcc;
    private javax.swing.JComboBox<String> cbVolume;
    private javax.swing.JComboBox<String> cbWeight;
    private javax.swing.JCheckBox chbEuropeFriendly;
    private javax.swing.JCheckBox chbSavingEnable;
    private javax.swing.JLabel lblConc;
    private javax.swing.JLabel lblDiluent;
    private javax.swing.JLabel lblFinalV;
    private javax.swing.JLabel lblFinalcc;
    private javax.swing.JLabel lblMW;
    private javax.swing.JLabel lblMessageBar;
    private javax.swing.JLabel lblSolvent;
    private javax.swing.JLabel lblStock;
    private javax.swing.JLabel lblStockV;
    private javax.swing.JLabel lblStockcc;
    private javax.swing.JLabel lblSubstance;
    private javax.swing.JLabel lblVolume;
    private javax.swing.JLabel lblWeight;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel pnSolid;
    private javax.swing.JPanel pnlLiquid;
    private javax.swing.JTabbedPane tpCalculationArea;
    private javax.swing.JTextField txtConc;
    private javax.swing.JTextField txtFinalV;
    private javax.swing.JTextField txtFinalcc;
    private javax.swing.JTextField txtMW;
    private javax.swing.JTextField txtNameDiluent;
    private javax.swing.JTextField txtNameSolution;
    private javax.swing.JTextField txtNameSolvent;
    private javax.swing.JTextField txtNameStock;
    private javax.swing.JTextField txtNameSubstance;
    private javax.swing.JTextField txtStockV;
    private javax.swing.JTextField txtStockcc;
    private javax.swing.JTextField txtVolume;
    private javax.swing.JTextField txtWeight;
    // End of variables declaration//GEN-END:variables
}
