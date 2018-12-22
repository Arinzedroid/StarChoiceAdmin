package com.tech.arinzedroid.starchoiceadmin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static String parseDateTime(Date dateTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss",Locale.ROOT);
        String mDateTime = "";
        try{
            mDateTime = simpleDateFormat.format(dateTime);
            return mDateTime;
        }catch (Exception ex){
            ex.printStackTrace();
            return mDateTime;
        }
    }

    public static boolean isSameDay(Date one, Date two){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ROOT);
        boolean val = fmt.format(one).equals(fmt.format(two));
        return val;
    }

    public static boolean isDateBefore(Date one, Date two) throws Exception{
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd", Locale.ROOT);
        boolean val = fmt.parse(fmt.format(one)).before(fmt.parse(fmt.format(two)));
        return val;
    }
}
