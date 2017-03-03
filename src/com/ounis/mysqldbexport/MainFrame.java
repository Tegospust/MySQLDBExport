/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ounis.mysqldbexport;


import com.ounis.ftools.FTools;
import com.ounis.utils.ArrayListLoader;
import com.ounis.utils.ArrayListSaver;
import com.ounis.utils.Debug;
import com.ounis.utils.FramesUtils;
import com.ounis.utils.MsgDialog;
import com.ounis.utils.MsgDlg;


import com.sun.webkit.event.WCKeyEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 *
 * @author AndroidAppsPlatform
 */
public class MainFrame extends javax.swing.JFrame {

    
    public static final String MAIN_CATEGORY = "MySQLDBExport";
    
    private static final char PASS_CHAR = '\u002a';
    private static final char NON_PASS_CHAR = '\u0000';
    
    static volatile boolean done = false;
    
    public static final int DEF_WIDTH = 389;
    public static final int DEF_HEIGHT = 376;
    
    private String mySqlDumpDir = "";
    
    // obs³uga focusa na kontrolkach
    class LocalFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource().equals(edDataBase))
            {
                  edOutputFile.setText(genOutputFileName(edDataBase.getText()));
            }
        }
        
    }
    
    
     class ErrorMsg extends Thread {
        private String msg;
        private Component owner;
        
        public ErrorMsg(String msg, Component owner) {
            super();
            this.msg = msg;
            this.owner = owner;
        }
        
        @Override 
        public void run ()
        {
            JOptionPane.showMessageDialog(owner, msg, "B³¹d",JOptionPane.ERROR_MESSAGE);
        }
        
        public void show() {
            this.start();
        }
    }
    
    // obs³uga przyciskania klawiszy na kontrolkach
    class LocalKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getSource().equals(edDataBase))
            {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER)
//                {
                    edOutputFile.setText(genOutputFileName(edDataBase.getText()));
//                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
                 if (e.getSource().equals(edDataBase))
            {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER)
//                {
                    edOutputFile.setText(genOutputFileName(edDataBase.getText()));
//                }
            }   }
        
    }
    
    // obs³uga dzia³ania myszy na kontrolkach
    class LocalMouseHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2)
            {
                if (e.getSource().equals(edDataBase))
                {
                    if (!edDataBase.getText().equals(""))
                    {
                        edOutputFile.setText(genOutputFileName(edDataBase.getText()));
                    }
                                      
                }
                if (e.getSource().equals(edPass) || e.getSource().equals(jLabel2))
                {
                    char c = edPass.getEchoChar();
                    if (c == PASS_CHAR)
                        edPass.setEchoChar(NON_PASS_CHAR);
                    else
                        edPass.setEchoChar(PASS_CHAR);
                                  
                }
            }
        }

    }
    


    class BtnCopyClick implements ActionListener  {

        
        private String getPassword(char[] pass) {
            String result = "";

            for (char c: pass)
                result += c;
            
//            for (int y = 0;y < pass.length;y++)
//                result += pass[y];
            
            return result;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String msg = "";
            String pass = getPassword(edPass.getPassword());
            
                if (edUser.getText().equals(""))
                   msg = "Podaj u¿ytkownika";
                else
                  if (pass.equals(""))
                  {
                    msg = "Podaj has³o";
                  }
                  else
                   if (edHost.getText().equals(""))
                        msg = "Podaj hosta";
                   else
                    if (edProtocol.getText().equals(""))
                            msg = "Podaj protokó³";
                    else
                      if (edPort.getText().equals(""))
                            msg = "Podaj port";
                      else
                       if (edDefChar.getText().equals(""))
                            msg = "Podaj kodowanie znaków";
                       else
                        if (edDataBase.getText().equals(""))
                                msg = "Podaj bazê danych";
                        else
                         if (edOutputFile.getText().equals(""))   
                            msg = "Podaj plik wyj.";
                        
                         if (!msg.equals("")){
                             
//                             PasswordChooser pc = new PasswordChooser();
//                             pc.showDialog(null, "DUPA");
                             //MsgDialog.show(MainFrame.this, "B³¹d", msg);
                             ErrorMsg errMsgThd = new ErrorMsg(msg, MainFrame.this);
                             //errMsgThd.show();
                             errMsgThd.start();
//                            MsgDlg.show(MainFrame.this, msg, "B³¹d", JOptionPane.ERROR_MESSAGE);
                         }
                         else
                         {
                             
//                            JOptionPane.showMessageDialog(MainFrame.this, "Sukces!\nWszystkie pola wype³nione.");
                             
                             String line = prepareCmdLine(edUser.getText(),
                                           pass, edHost.getText(),
                                           edProtocol.getText(),
                                           edPort.getText(),
                                           edDefChar.getText(), 
                                           !cbTriggers.isSelected(), 
                                           edDataBase.getText(), 
                                           edOutputFile.getText());
                             System.out.printf("%s\n", line);
                             saveData();
                             createBatchFile(Const.DEF_INI_FILE, line);
                             
//                             runExternalFile();
                             
                             try 
                             {
                                runExternalFileWithResults(line);
                             }
                             catch (IOException ex)
                             {
                                 JOptionPane.showMessageDialog(MainFrame.this,
                                         "runExternalFileWithResults B£¥D: "+
                                                       ex.getLocalizedMessage());
                             }
                             
                         }
                               

        }
    
    }


    
    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        
        Debug.println(MAIN_CATEGORY, "Napis próbny");
//        setSize(DEF_WIDTH
//            ,DEF_HEIGHT);
        
        FramesUtils.centerWindow(this, DEF_WIDTH, DEF_HEIGHT);
                      
        
                      
        initComponents();
        loadData();
        
        setTitle("MySQLDBExport");
        
        edOutputFile.setText(genOutputFileName(edDataBase.getText()));
        
        //edDataBase.addMouseListener(new MouseHandler());
        edDataBase.addKeyListener(new LocalKeyListener());
        edDataBase.addFocusListener(new LocalFocusListener());
        
        edPass.addMouseListener(new LocalMouseHandler());
        jLabel2.addMouseListener(new LocalMouseHandler());
        //edPass.setVisible(false);
        
        
        btnCopy.addActionListener(new BtnCopyClick());
        
//        btnCopy.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                
//                if (edUser.getText().equals(""))
//                    JOptionPane.showMessageDialog(MainFrame.this, "Podaj u¿ytkownika");
//                else
//                  if (edPass.getPassword().equals(""))
//                    JOptionPane.showMessageDialog(MainFrame.this, "Podaj has³o");
//                  else
//                   if (edHost.getText().equals(""))
//                        JOptionPane.showMessageDialog(MainFrame.this, "Podaj hosta");
//                   else
//                    if (edProtocol.getText().equals(""))
//                            JOptionPane.showMessageDialog(MainFrame.this, "Podaj protokó³");
//                    else
//                      if (edPort.getText().equals(""))
//                            JOptionPane.showMessageDialog(MainFrame.this, "Podaj port");
//                      else
//                       if (edDefChar.getText().equals(""))
//                            JOptionPane.showMessageDialog(MainFrame.this, "Podaj kodowanie znaków");
//                       else
//                        if (edDataBase.getText().equals(""))
//                            JOptionPane.showMessageDialog(MainFrame.this, "Podaj bazê danych");
//                        else
//                        {
//
//                         if (edOutputFile.getText().equals(""))   
//                            JOptionPane.showMessageDialog(MainFrame.this, "Podaj plik wyj.");
//                        
//                         else
//                         {
////                            JOptionPane.showMessageDialog(MainFrame.this, "Sukces!\nWszystkie pola wype³nione.");
//                             
//                             String line = prepareCmdLine(edUser.getText(),
//                                           edPass.getText(), edHost.getText(),
//                                           edProtocol.getText(),
//                                           edPort.getText(),
//                                           edDefChar.getText(), 
//                                           !cbTriggers.isSelected(), 
//                                           edDataBase.getText(), 
//                                           edOutputFile.getText());
//                             System.out.printf("%s\n", line);
//                             saveData();
//                             createBatchFile(Const.DEF_INI_FILE, line);
//                             
////                             runExternalFile();
//                             
//                             try 
//                             {
//                                runExternalFileWithResults(line);
//                             }
//                             catch (IOException ex)
//                             {
//                                 JOptionPane.showMessageDialog(MainFrame.this,
//                                         "runExternalFileWithResults B£¥D: "+
//                                                       ex.getLocalizedMessage());
//                             }
//                             
//                         }
//                        }     
//            }
//        });
        
    }

    private void createBatchFile(String FileName, String cmdLine)
    {
        ArrayList<Object> alCommand = new ArrayList<>();
        //alCommand.add("@echo off");
        alCommand.add(cmdLine);
        String[] aS = FileName.split(".ini");
        ArrayListSaver als = new ArrayListSaver();
        try
        {
            als.saveArrList(alCommand, aS[0]+".bat", false);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, String.format("B³¹d zapisu pliku wsadowego: %s", e.getLocalizedMessage()));
        }
    }
    
    private String genOutputFileName(String basePrefix)
    {
        Calendar cal = new GregorianCalendar();
        basePrefix += String.format("_%d_%02d_%02d", 
                      cal.get(Calendar.YEAR),
                      cal.get(Calendar.MONTH) + 1,
                      cal.get(Calendar.DAY_OF_MONTH));        
        
        return basePrefix + ".sql";
    }
    
    private String prepareCmdLine(String user, String pass, String host,
                  String protocol, String port, String defchar,
                  boolean skip_trigg, String databasename, String outputfile)
    {
        String result;
        String tmp;
        
        
        
        result = "\""+(!mySqlDumpDir.equals("")?mySqlDumpDir:"") + Const.RUN_APPLICATION+"\"";
        
        tmp = Const.DB_KEY_USER;
        result += tmp.replace(Const.USER_PARAM, user);
        
        tmp = Const.DB_KEY_PASSWORD;
        result += tmp.replace(Const.PASSWORD_PARAM, pass);
        
        tmp = Const.DB_KEY_HOST;
        result += tmp.replace(Const.HOST_PARAM, host);
        
        tmp = Const.DB_KEY_PROTOCOL;
        result += tmp.replace(Const.PROTOCOL_PARAM, protocol);
        
        tmp = Const.DB_KEY_PORT;
        result += tmp.replace(Const.PORT_PARAM, port);
        
        tmp = Const.DB_KEY_DEF_CHAR;
        result += tmp.replace(Const.DEF_CHAR_PARAM, defchar);
        
        if (skip_trigg) result += Const.DB_KEY_SKIP_TRIGGERS;
        
        tmp = Const.DB_KEY_DATABASE;
        result += tmp.replace(Const.DATABASE_PARAM , " \""+databasename+"\"");
        
        tmp = Const.DB_KEY_OUTPUT_FILE;
        result += tmp.replace(Const.OUTPUT_FILE_PARAM, outputfile);
        
        return result;
    }
    
    
    private void saveData()
    {
        ArrayList<Object> dataList = new ArrayList<>();
        dataList.add(Const.DB_KEY_USER.replace(Const.USER_PARAM, edUser.getText()));
 //       dataList.add(Const.DB_KEY_PASSWORD.replace(Const.PASSWORD_PARAM, edPass.getText()));
        dataList.add(Const.DB_KEY_HOST.replace(Const.HOST_PARAM, edHost.getText()));
        dataList.add(Const.DB_KEY_PROTOCOL.replace(Const.PROTOCOL_PARAM, edProtocol.getText()));
        dataList.add(Const.DB_KEY_PORT.replace(Const.PORT_PARAM, edPort.getText()));
        dataList.add(Const.DB_KEY_DEF_CHAR.replace(Const.DEF_CHAR_PARAM, edDefChar.getText()));
        dataList.add(Const.DB_KEY_SKIP_TRIGGERS + (!cbTriggers.isSelected()?"=YES":"=NO" ));
        dataList.add("--database="+(Const.DB_KEY_DATABASE.replace(Const.DATABASE_PARAM, edDataBase.getText())).trim());
        //dataList.add(Const.DB_KEY_OUTPUT_FILE.replace(Const.OUTPUT_FILE_PARAM, edOutputFile.getText()));
        if (!mySqlDumpDir.equals(""))
            dataList.add(Const.DB_KEY_MYSQLDUMP_DIR+"="+mySqlDumpDir);
        ArrayListSaver als = new ArrayListSaver();
        try
        {
            als.saveArrList(dataList, Const.DEF_INI_FILE, false);
        }
        catch (IOException e)
        {
            JOptionPane.showMessageDialog(this, 
               String.format("B³¹d podczas zapisu ustawieñ: %s", e.getLocalizedMessage()));
            
            
        }
                      
    }
    
    private String getValue(String aKeyValue)
    {
        String[] arrS = aKeyValue.split("=");
        if (arrS.length == 2)
            return arrS[1];
        else
            return "";
    }
    
    private void loadData()
    {
        boolean defsett = false;
        ArrayListLoader all = new ArrayListLoader();
        if (FTools.fileExists(Const.DEF_INI_FILE))
        {
            ArrayList<Object> dataList = all.loadArrList(Const.DEF_INI_FILE);
            if (dataList != null)
            {
               for (Object o: dataList) {
                   String tmp = o.toString();
                   if (tmp.toLowerCase().indexOf("user")>-1)
                   {
                       edUser.setText(getValue(tmp));
                   }
//                   if (tmp.indexOf("password")>0)
//                       edPass.setText(getValue(tmp));
                   if (tmp.toLowerCase().indexOf("host")>-1)
                       edHost.setText(getValue(tmp));
                   if (tmp.toLowerCase().indexOf("protocol")>-1)
                       edProtocol.setText(getValue(tmp));
                   if (tmp.toLowerCase().indexOf("port")>-1)
                       edPort.setText(getValue(tmp));
                   if (tmp.toLowerCase().indexOf("default-character-set")>-1)
                       edDefChar.setText(getValue(tmp));
                   if (tmp.toLowerCase().indexOf("skip-triggers")>-1)
                   {
                      cbTriggers.setSelected((getValue(tmp).equals("YES"))?false:true);
                   }
                   if (tmp.toLowerCase().indexOf("database")>-1)
                   {
                       edDataBase.setText(getValue(tmp));
                       edOutputFile.setText(genOutputFileName(edDataBase.getText()));
                   }
                   if (tmp.toLowerCase().indexOf(Const.DB_KEY_MYSQLDUMP_DIR.toLowerCase())>-1)
                   {
                       mySqlDumpDir = getValue(tmp);
                       if (!mySqlDumpDir.equals(""))
                       {    
                        if (mySqlDumpDir.charAt(mySqlDumpDir.length()-1) != '\\')
                               mySqlDumpDir += "\\";
                        Debug.printf(MAIN_CATEGORY, "Katalog: %s\n", mySqlDumpDir);
                       } 
                   }
               }
            }
            else
                defsett = true;
        }
        else
          defsett = true;
        if (defsett)
        {
            Debug.println(MAIN_CATEGORY,"Brak pliku ustawieñ!\n"
                          + "Zastosowano ustawienia domyœlne.");
        }
    }
    
    
    
    private void runExternalFileWithResults(String cmdLine) throws IOException {
        
        final Process p; // Obiekt process reprezentuje jeden rodzimy  proces.
        BufferedReader isE; // Obiekt, w którym bêd¹ zapisywane wyniki wykonywanego procesu.
        BufferedReader isOK;
        

        String line;
        String msg;
        
        
        String fileexec = Const.DEF_INI_FILE;
        fileexec = fileexec.replace(".ini", ".bat");
        fileexec = fileexec;
        //fileexec = "mysqldump.exe --user=ounis --password=dupa";
        p = Runtime.getRuntime().exec(fileexec);
        done = false;
        Debug.println(MAIN_CATEGORY,"...po wywo³aniu exec.");
// Opcjonalne: uruchamiamy w¹tek oczekuj¹cy na zakoñczenie
// procesu. Nie bêdziemy czekaæ w metodzie main() - tutaj
// ustawiamy jedynie flagê "done" i u¿ywamy jej do kontroli
// dzia³ania g³ównej pêtli odczytuj¹cej, umieszczonej poni¿ej.

        //Thread waiter = 
                      (new Thread() {
            @Override
            public void run() {
                try {
                    p.waitFor();
                } catch (InterruptedException ex) {
// OK, po prostu koñczymy.
                    return;
                }
                Debug.println(MAIN_CATEGORY,"Proces zakoñczony!");
                done = true;
//                JOptionPane.showMessageDialog(MainFrame.this, 
//                      "Wykonywanie kopii zapasowej zakoñczone.");
            }
        }).start();
        
//        waiter.start();
// Metoda getInputStream zwraca strumieñ wejœciowy (InputStream)
// skojarzony ze standardowym wyjœciem uruchomionego programu
// zewnêtrznego (i na odwrót). U¿yjemy go do utworzenia obiektu
// BufferedReader, dziêki czemu bêdziemy mogli odczytywaæ wiersze
// tekstu przy u¿yciu metody readLine().
        isE = new BufferedReader(new InputStreamReader(p.getErrorStream() ));
        ArrayList<Object> errorLines = new ArrayList<>();
        isOK = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
        while (!done && ((line = isE.readLine()) != null)) {
              
            if (!line.contains(Const.SKIP_MYSQLDUMP_MSG))
                errorLines.add(String.format("Err: %s\n",line));
        }
        
        while (!done && ((line = isOK.readLine()) != null)) {
            Debug.println(MAIN_CATEGORY," Out: "+line);
        }
        if (errorLines.size() > 0)
        {
           // LogFrame.showLogFrame(errorLines);
               
//            LogDialog lDialog = new LogDialog(errorLines,this, "Coœ posz³o nie tak...", true);
//            FramesUtils.centerWindow(lDialog, DEF_WIDTH + 200, DEF_HEIGHT);
//            //lDialog.setSize(200, 100);
////            lDialog.setTitle("Coœ posz³o nie tak...");
//            lDialog.setModal(true);
//            lDialog.setVisible(true);
            
            //LogDialog.show(errorLines, this);
            (new LogDialogEx(errorLines)).show(this, errorLines);
            
            
            Debug.printf(MAIN_CATEGORY,"Wyst¹pi³ jakiœ b³¹d!!!\n---------------------\n");
            for (Iterator<Object> it = errorLines.iterator(); it.hasNext();) {
                String s = (String)it.next();
                Debug.printf(MAIN_CATEGORY,"%s\n", s);
            }
        }
        else
        {
            (new Thread() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Kopia zosta³a wykonana pomyœlnie...",
                              "Informacja",JOptionPane.INFORMATION_MESSAGE);
                              }
            
                }).start();  
            
        }

        Debug.println(MAIN_CATEGORY, "...po zakoñczeniu odczytu.");
        isE.close();
        isOK.close();
                      
        
        return;
    }



    
    
    private void runExternalFile()
    {
     
       
       //              ... --- ...  .-  -.- - ---  - ---  .--- . ... -  
        // SOS a kto to jest
        
       //String fileexec = (Const.DEF_INI_FILE.split(".ini"))[0] + ".bat";
       String fileexec = Const.DEF_INI_FILE;
    
       
       
       fileexec = fileexec.replace(".ini", ".bat");
//       if (FTools.fileExists(fileexec)) {
           try 
           {
               String line;
               Process p = Runtime.getRuntime().exec(fileexec);
               p.waitFor();
               BufferedReader is = new BufferedReader(new InputStreamReader(p.getInputStream()));
               while (((line = is.readLine()) != null))
                    System.out.println(line);
               
           }
           catch (IOException e)
           {
               JOptionPane.showMessageDialog(this, "Nie uruchomiono pliku: " + e.getLocalizedMessage());
           }
           catch (InterruptedException e)
           {
               JOptionPane.showMessageDialog(this, "B³¹d procesu: " + e.getLocalizedMessage());
               
           }
                         
//       }
           
                     
    }

    public void setBtnCopyEnabled(boolean value) {
        btnCopy.setEnabled(value);
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cbTriggers = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        edUser = new javax.swing.JTextField();
        edHost = new javax.swing.JTextField();
        edProtocol = new javax.swing.JTextField();
        edPort = new javax.swing.JTextField();
        edDefChar = new javax.swing.JTextField();
        edDataBase = new javax.swing.JTextField();
        edOutputFile = new javax.swing.JTextField();
        edPass = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        btnCopy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("U¿ytkownik:");

        jLabel2.setText("Has³o:");

        jLabel3.setText("Host:");

        jLabel4.setText("Protokó³:");

        jLabel5.setText("Port:");

        jLabel6.setText("DefChar:");

        cbTriggers.setText("Wyzwalacze");

        jLabel7.setText("Baza:");

        jLabel8.setText("Plik wyj.:");

        edHost.setText("localhost");

        edProtocol.setText("tcp");

        edPort.setText("3306");

        edDefChar.setText("utf8");

        edDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edDataBaseActionPerformed(evt);
            }
        });

        edOutputFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edOutputFileActionPerformed(evt);
            }
        });

        edPass.setEchoChar('*');

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(edDefChar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbTriggers)
                                .addGap(132, 132, 132))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(edHost, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(edPort, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                                    .addComponent(edUser)
                                    .addComponent(edPass))
                                .addContainerGap(31, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(edDataBase)
                            .addComponent(edOutputFile, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(edUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(edPass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(edHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edProtocol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(edPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cbTriggers)
                    .addComponent(edDefChar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edDataBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edOutputFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnCopy.setText("Kopia");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCopy)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCopy)
                .addGap(26, 26, 26))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void edDataBaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edDataBaseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edDataBaseActionPerformed

    private void edOutputFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edOutputFileActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_edOutputFileActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCopy;
    private javax.swing.JCheckBox cbTriggers;
    private javax.swing.JTextField edDataBase;
    private javax.swing.JTextField edDefChar;
    private javax.swing.JTextField edHost;
    private javax.swing.JTextField edOutputFile;
    private javax.swing.JPasswordField edPass;
    private javax.swing.JTextField edPort;
    private javax.swing.JTextField edProtocol;
    private javax.swing.JTextField edUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
