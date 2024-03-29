package com;

import java.io.File;
import java.util.LinkedList;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 11.06.2014
 */
public class FileAssist {
    /**
     *
     * @param programmname defines the programm name/the subdirectory to return
     * @return returns a OS independent folder dependent on the currently logged in user.
     */
    public static String getUserDir(String programmname) {
        String userdir;
        if (System.getProperty("os.name").toLowerCase().indexOf("win") > -1)
            userdir = System.getenv("APPDATA") + File.separator
                    + programmname;
        else
            userdir = System.getProperty("user.home") + File.separator
                    + programmname;
        return userdir;
    }

    /**
     *
     * @return returns all files saved on the server
     */
    public static File[] getAllFiles(){
        if(!new File(FileAssist.getUserDir("DMS")).exists())return null;
        File dir=new File(FileAssist.getUserDir("DMS"));
        File[] files=dir.listFiles();
        return files;
    }

    /**
     *
     * @return returns all file names on the server
     */
    public static String[] getFileNames(){
        if(!new File(FileAssist.getUserDir("DMS")).exists())return null;
        File dir=new File(FileAssist.getUserDir("DMS"));
        String[] files=dir.list();
        return files;
    }
}
