package com.hhyg.TyClosing.log;

import static com.hhyg.TyClosing.config.Constants.IS_LOG_OPEN;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.hhyg.TyClosing.global.FileOperator;
import com.hhyg.TyClosing.global.JsonPostParamBuilder;

import android.os.Environment;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;

public class Logger {
    public static final String SDPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath();
    final String path = SDPATH + "/hnyg/";
    public final String logFilePath = path + "log";

    public long maxFileSize = 1024 * 100;
    private static Logger mLogger = new Logger();
    private static final String ACTION_DEBUG = "debug"; //调试信息
    private static final String ACTION_TRACK = "track"; //UI界面跟踪信息
    private static final String ACTION_CRASH = "crash"; //崩溃信息
    private static final String ACTION_ERROR = "error"; //错误信息
    private static final String ACTION_ACTION = "action"; //操作记录
    private static final String ACTION_NET = "net";  //网络日志
    private static final String ACTION_EXCEPTION = "exception";  //异常日志

    public static final String LEVEL_INFO = "info";
    public static final String LEVEL_ERROR = "err";
    public static final String LEVEL_WARNING = "warning";
    private static NetworkThread mThread;
    private ArrayList<HashMap> mList = new ArrayList<HashMap>();

    /*
    日记文件名中日期和日志索引中的空格，
    在日志上传功能之前，空格数为2个，日志功能完成后，修改为3个
    上传模块已日志的空格个数判断，判断日志是否是新代码生成的，如果是传送至服务器
     */
    public static final String BLANK_SPACE = "   ";


    public static Logger GetInstance() {
        return mLogger;
    }

    private Logger(){
        if(IS_LOG_OPEN == false)
            return;
        if (mThread == null) {
            mThread = new NetworkThread();
            mThread.start();
        }
    }

    public void Debug(String str) {
        logInfo(ACTION_DEBUG,LEVEL_INFO,str);
    }

    public void Warning(String str) {
        logInfo(ACTION_DEBUG,LEVEL_WARNING,str);
    }

    //崩溃消息不能有延迟，直接写入文件
    public void Crash(String str) {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        StackTraceElement s = stack[2];
        String outPutString = formatLog(ACTION_CRASH,LEVEL_ERROR,s,str);
        HashMap map = new HashMap();
        map.put(ACTION_CRASH,outPutString);
        handleLog(map);
    }

    public void Track(String str) {
        logInfo(ACTION_TRACK,LEVEL_INFO,str);
    }

    public void Action(String str) {
        logInfo(ACTION_ACTION,LEVEL_INFO,str);
    }

    public void Net(String strLevel,String str) {
        //1.如果是日志上传模块发送的网络消息，则不记录入文件，否则上传的日志发送被记录入文件，下一次会被发送，再次记录入文件，形成一个大的死循环。
        //2.如果是未登录前记录是日志，访问saleid会导致程序异常，zai
        if(str.contains("uploadlog") == false && str.contains("login") == false) {
            if(LEVEL_ERROR.equals(strLevel)){//如果是网络错误，记录入错误文件夹
                logInfo(ACTION_ERROR, strLevel, str);
            }
            logInfo(ACTION_NET, strLevel, str);
        }
    }

    public void Exception(String str) {
        logInfo(ACTION_EXCEPTION,LEVEL_ERROR,str);
    }

    public void Error(String str) {
        logInfo(ACTION_ERROR,LEVEL_ERROR,str);
    }

    private void logInfo(String type, String level,String str){
        if(IS_LOG_OPEN == false)
            return;
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        StackTraceElement s = stack[2];
        String outPutString = formatLog(type,level,s,str);
        HashMap map = new HashMap();
        map.put(type,outPutString);
        synchronized (mList){
            mList.add(map);
        }
    }


    class NetworkThread extends Thread {
        @Override public void run() {
            while (true){
                int nLength = mList.size();
                if(nLength == 0){
                    try {
                        Thread.sleep(3000);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    continue;
                }
                for (int i = 0; i < nLength; i++){
                    HashMap map = mList.get(i);
                    handleLog(map);
                }
                synchronized (mList){
                    for (int i = nLength - 1; i >= 0; i--){
                        mList.remove(i);
                    }
                }
            }
        }
    }

    private String getCurTime() {
        SimpleDateFormat fomatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = fomatter.format(curDate);
        return str;
    }

    private String getCurDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String time = format.format(calendar.getTime());
        return time;
    }

    private void handleLog(HashMap map) {
        String strKey = "";
        String str = "";
        Iterator it = map.entrySet().iterator();
        if(it.hasNext()){
            Map.Entry entry = (Map.Entry)it.next();
            strKey = entry.getKey().toString();
            str = entry.getValue().toString();
        }

        StringBuilder builder = new StringBuilder(256);
        builder.append(logFilePath).append("/").append(this.getCurDate());
        String strName = builder.toString();

        FileOperator.createFileDirectory(strName);
        File fileFolder = new File(strName);
        String[] fileNameArray = fileFolder.list();
        int FileCount = 1;
        int nUpFileCount = 0;
        if(fileNameArray != null){
            FileCount = fileNameArray.length;
            for (int i = 0;i < FileCount ; i++){
                String strName1 = fileNameArray[i];
                if(strName1.contains("up"))
                    nUpFileCount ++;
            }
            if(FileCount != 0)
                FileCount = FileCount - nUpFileCount;
            if(FileCount == 0)
                FileCount = 1;
            if (nUpFileCount == 0)
                nUpFileCount = 1;
        }

        long fileSize = this.maxFileSize;
        String strFilePath = getFilePath(this.getCurDate(),FileCount);
        String strNewFilePath = getFilePath(this.getCurDate(),FileCount + 1);
        //错误，异常，以及崩溃信息写入上传文件
        if(strKey.equals(ACTION_ERROR) || strKey.equals(ACTION_EXCEPTION) || strKey.equals(ACTION_CRASH)) {
            strFilePath = getUploadFilePath(this.getCurDate(), nUpFileCount);
            strNewFilePath = getUploadFilePath(this.getCurDate(),nUpFileCount + 1);
            fileSize = 1024 * 20;
        }
        File lastFile = new File(strFilePath);
        try {
            if (!lastFile.exists()) {
                lastFile.createNewFile();
            }
            else{
                if (lastFile.length() > fileSize) {
                    lastFile = new File(strNewFilePath);
                    lastFile.createNewFile();
                }
            }
            FileWriter fw = new FileWriter(lastFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(str);
            bw.flush();
            bw.close();
        } catch (Exception e) {

        }
    }

    //StackTraceElement s
    private String formatLog(String type,String level,StackTraceElement s,String content){
        String date = getCurDate() + "-" +this.getCurTime();
        com.alibaba.fastjson.JSONObject j = new com.alibaba.fastjson.JSONObject();
        try {
            j.put("date", date);
            j.put("type", type);
            j.put("level", level);

            String str = s.getClassName();
            String[] strs = str.split("\\.");
            if(strs.length != 0) {
                String strName = strs[strs.length - 1];
                StringBuilder builder = new StringBuilder(strName).append("::").append(s.getLineNumber());
                j.put("pos", builder.toString());
            }
            j.put("content", content);
            j.put("imei", MyApplication.GetInstance().getAndroidId());
            j.put("saleId", ClosingRefInfoMgr.getInstance().getSalerInfo().getSalerId());
            j.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
            j.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
        }
        catch (Exception e){

        }
        return j.toJSONString() + "\n\n";
    }

    public String getFilePath(String strDate, int nIndex){
        StringBuilder builder = new StringBuilder(logFilePath).append("/").append(strDate).append("/").append(strDate).
                append(BLANK_SPACE).append(nIndex).append(".log");
        return builder.toString();
    }

    public String getUploadFilePath(String strDate, int nIndex){
        StringBuilder builder = new StringBuilder(logFilePath).append("/").append(strDate).append("/").append(strDate).
                append(BLANK_SPACE).append(nIndex).append("_up.log");
        return builder.toString();
    }


    public String readFileContent(String filePath) {
        String strReturn  = "";
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            StringBuilder builder = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
            }
            strReturn = builder.toString();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {

                }
            }
        }
        return strReturn;
    }

}
