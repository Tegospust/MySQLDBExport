

package com.ounis.mysqldbexport;

/**
 *
 * @author AndroidAppsPlatform
 */
public class Const {
    public static final String DEF_INI_FILE = "MySQLDBExport.ini";
    
    
    public static final String USER_PARAM = ":user:";
    public static final String PASSWORD_PARAM = ":password:";
    public static final String HOST_PARAM = ":host:";
    public static final String PROTOCOL_PARAM = ":protocol:";
    public static final String PORT_PARAM = ":port:";
    public static final String DEF_CHAR_PARAM = ":def_char:";
    public static final String DATABASE_PARAM = ":database:";
    public static final String OUTPUT_FILE_PARAM = ":output_file:";
    
    public static final String DB_KEY_USER = " --user="+USER_PARAM;
    public static final String DB_KEY_PASSWORD = " --password="+PASSWORD_PARAM;
    public static final String DB_KEY_HOST = " --host="+HOST_PARAM;
    public static final String DB_KEY_PROTOCOL = " --protocol="+PROTOCOL_PARAM;
    public static final String DB_KEY_PORT = " --port="+PORT_PARAM;
    public static final String DB_KEY_DEF_CHAR = " --default-character-set="+DEF_CHAR_PARAM;
    public static final String DB_KEY_SKIP_TRIGGERS = " --skip-triggers";
    public static final String DB_KEY_DATABASE =  DATABASE_PARAM;
    public static final String DB_KEY_OUTPUT_FILE = " > "+OUTPUT_FILE_PARAM;
    
    public static final String DB_KEY_MYSQLDUMP_DIR = "mysqldump_dir";

    public static final String RUN_APPLICATION = "mysqldump.exe";
    
    
    public static final String SKIP_MYSQLDUMP_MSG = "Warning: Using a password"+
                        " on the command line interface can be insecure.";
    
}
