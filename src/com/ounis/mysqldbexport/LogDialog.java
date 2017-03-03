

package com.ounis.mysqldbexport;

import com.ounis.utils.FramesUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JTextArea;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author AndroidAppsPlatform
 */
public class LogDialog extends JDialog {
    
    
    private JTextArea taLog;
    private javax.swing.JScrollPane jScrollPane1;
    private JPanel panel;
    private JButton btnOK;
    
    
    class btnOKClick implements ActionListener {
        private JDialog dialog;
        
        public btnOKClick(JDialog dialog) {
            this.dialog = dialog;
        }
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
        
    }
    
    
    public LogDialog (ArrayList<Object> alLog, Frame owner, String title, boolean modal)
    {
        super(owner, title, modal);
        
        
        jScrollPane1 = new javax.swing.JScrollPane();
        
        taLog = new JTextArea();
        taLog.setRows(5);
        taLog.setColumns(20);
        taLog.setLineWrap(true);
        taLog.setWrapStyleWord(true);
        taLog.setEditable(false);
        //taLog.setEnabled(false);

        if (alLog != null) {
            for (Object o: alLog)
            {
                taLog.append(o.toString());
            }
        }
        
        jScrollPane1.setViewportView(taLog);
        
        //JButton bookBtn = new JButton("Book", new ImageIcon("img/accept.png"));
        
        //panel = new JPanel();
        
        ImageIcon img = new ImageIcon("img/accept.png");
        btnOK = new JButton("OK", img);
        //new JButton("Start", new ImageIcon("start.gif"));
        
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1 sposób
                    LogDialog.this.setVisible(false); // reszt¹ powinien zaj¹æ siê
                                                      // gc z JVM
                // 2 sposób
                //LogDialog.this.dispose();
                
                // 3 sposób
                // zamykanie dialogu 
                LogDialog.this.setVisible(false);
                LogDialog.this.dispatchEvent(new WindowEvent(
                    LogDialog.this, WindowEvent.WINDOW_CLOSING));
            }
        });
        
        //this.add(btnOK, BorderLayout.SOUTH);
        //this.add(panel, BorderLayout.SOUTH);
        
        this.add(jScrollPane1);
        
    }
    
    public static void show(ArrayList<Object> aList, JFrame frame) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JDialog logDialog = new LogDialog(aList, frame, 
                              "Coœ posz³o nie tak...", true);
                FramesUtils.centerWindow(logDialog, MainFrame.DEF_WIDTH + 200, MainFrame.DEF_HEIGHT);
                
                logDialog.setVisible(true);
            }
        });
    }
}
