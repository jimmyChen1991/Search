package com.hhyg.TyClosing.util;

import com.hhyg.TyClosing.log.Logger;

/**
 * Created by mjf on 2016/10/9.
 */
public class Validate{
    public static Boolean isStringEquals(String str1,String str2){
        if(StringUtil.isNull(str1) || StringUtil.isNull(str2)){
            Logger.GetInstance().Error("stringEqualsException:  " + "str1 :" + str1 + "str2 :" + str2 + " \r\n " +  Thread.currentThread() .getStackTrace());
            return false;
        }
        boolean b = str1.equals(str2);
        return b;
    }

    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (mobiles.equals(""))
            return false;
        else
            return mobiles.matches(telRegex);
    }


    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {     //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {   //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    public static Boolean checkIdCode(String idCode) {
        if(StringUtil.isEmpty(idCode))
            return false;
        boolean card = IdentityUtil.validateCard(idCode);
        return card;
    }

    public static boolean isObEqual(Object ob1,Object ob2){
        if(ob1!= null && ob1.equals(ob2))
            return true;
        return false;
    }
}