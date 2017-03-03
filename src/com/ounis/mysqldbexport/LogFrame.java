/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ounis.mysqldbexport;

import com.ounis.utils.Debug;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author AndroidAppsPlatform
 */
public class LogFrame extends javax.swing.JFrame {

    /**
     * Creates new form LogFrame
     */
    public LogFrame() {
        initComponents();
    }

    public LogFrame(ArrayList<Object> alLog) {
        this();
        if (alLog != null)
        {   
            this.setTitle("Co� posz�o nie tak...");
            taLog.setLineWrap(true);
            taLog.setWrapStyleWord(true);
            
            for (Iterator<Object> it = alLog.iterator(); it.hasNext();) {
                taLog.append(it.next().toString());
            }
            
            this.repaint();
            
            //taLog.setText(Arrays.toString(alLog.toArray()));
        }
        else
        {
            Debug.printf(MainFrame.MAIN_CATEGORY, "Lista z logami pusta!\n");
        }
    }
    
    
    public static void showLogFrame(ArrayList<Object> alLog) 
    {
        Thread newT = new Thread() {
            @Override
            public void run() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        JFrame logFrame;
                        logFrame = new LogFrame(alLog);
                        logFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                        logFrame.addWindowListener(new WindowAdapter() {
                            
                            @Override
                            public void windowClosing(java.awt.event.WindowEvent windowEvent) {   
                               //JOptionPane.showMessageDialog(null,"Zamykanie..." );
                               
                            }
                            
                        });
                        
                        logFrame.setVisible(true);
                        
                        
                    }
                });                
            }
            
        };
        
        newT.start();
                      
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taLog = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taLog.setColumns(20);
        taLog.setRows(5);
        jScrollPane1.setViewportView(taLog);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(LogFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(LogFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(LogFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(LogFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new LogFrame().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taLog;
    // End of variables declaration//GEN-END:variables
}
