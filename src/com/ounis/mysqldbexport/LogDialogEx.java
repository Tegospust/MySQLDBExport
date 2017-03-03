

package com.ounis.mysqldbexport;

import com.ounis.ftools.FTools;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.io.File;

/**
 *
 * @author AndroidAppsPlatform
 */
public class LogDialogEx extends JPanel{

    
    JDialog dialog;
    JButton btnOK;
    
    public LogDialogEx(ArrayList<Object> aList) {
        
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        JTextArea taLog = new JTextArea();
        taLog.setEditable(false);
        panel.add(taLog);
        add(panel, BorderLayout.CENTER);
        
        if (aList != null) {
            for (Object o: aList) 
                taLog.append(o.toString());
        }
        
        btnOK = new JButton();
        btnOK.setText("OK");
        
        String filename = "img\\accept.png";
        if (FTools.fileExistsEx(filename))
            btnOK.setIcon(new ImageIcon(filename));
        
        
        try {
            System.out.printf("pe³na œcie¿ka: %s\n>", FTools.getPath(filename));
        }
        catch (Exception e){
            System.err.println(e.getLocalizedMessage());
        }
        
        
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                dialog.setVisible(false);
            }
        });
        
        
        JPanel pnlButton = new JPanel();
        pnlButton.add(btnOK);
        add(pnlButton, BorderLayout.SOUTH);
    } // constructor

    
    
    public void show(Component parent, ArrayList<Object> aList) {
        
        
      Frame owner = null;
      
      if (parent instanceof Frame)
          owner = (Frame) parent;
      else owner = (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);
      
      if (dialog == null || dialog.getOwner() != owner)
      {
         dialog = new JDialog(owner, true);
         dialog.add(this);
         dialog.getRootPane().setDefaultButton(btnOK);
         dialog.pack();
      }
      
      dialog.setLocationRelativeTo(parent);
       //FramesUtils.centerWindow(dialog, -1, -1);
      // set title and show dialog

      dialog.setTitle("Coœ posz³o nie tak...");
      dialog.setVisible(true);
        
        
    }

}
