package com.hhyg.TyClosing.global;

import java.io.File;
import java.util.ArrayList;

public class FileOperator {
	
	public static boolean createFileDirectory(String path) {
		boolean res = true;
		File file = new File(path);
		if (!file.exists()) {
			res = file.mkdirs();
		}
		return res;
	}
	
    public static boolean deleteFolder(String sPath) {
       boolean flag = false;
       File file = new File(sPath);

        if (!file.exists()) {
            return flag;
        } else {

            if (file.isFile()) {
                return deleteFile(sPath);
            } else {
                return deleteDirectory(sPath);
            }
        }
    }
    

    public static boolean deleteFile(String sPath) {
       boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
	

    private static boolean deleteDirectory(String sPath) {

        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
       boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            }
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;

        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static ArrayList getAllFile(String filePath){
        ArrayList<String> filelist = new ArrayList<String>();
        getFiles(filePath,filelist);
        return filelist;
    }

    private static void getFiles(String filePath,ArrayList<String> filelist){
        File root = new File(filePath);
        File[] files = root.listFiles();
        if(files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), filelist);
                } else {
                    filelist.add(file.getAbsolutePath());
                }
            }
        }
    }
}
