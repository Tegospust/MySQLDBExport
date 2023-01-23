

package com.ounis.mysqldbexport;

import com.ounis.ftools.FTools;
import com.ounis.utils.Debug;
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
import java.net.URISyntaxException;
import java.net.URL;

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
        // zród³o pobierania zasobów
        // http://stackoverflow.com/questions/1096398/add-image-to-jar-java
        // pobieramy zasoby dla przycisku
        // tylko w ten sposób obrazek bêdzie dostêpny zarówno w runtime IDE Beansów
        // jak i w odpalanym .JARze
        URL imgurl = this.getClass().getResource("/Resources/accept.png");
        Debug.printf(MainFrame.MAIN_CATEGORY, "%s\n", imgurl.toString());
        btnOK.setIcon(new ImageIcon(imgurl));
        
        
//        try {
//            System.out.printf("pe³na œcie¿ka: %s\n>", FTools.getPath(filename));
//        }
//        catch (Exception e){
//            System.err.println(e.getLocalizedMessage());
//        }
        
        
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

      dialog.setResizable(false);
      dialog.setTitle("Coœ posz³o nie tak...");
      dialog.setVisible(true);
        
        
    }

}
