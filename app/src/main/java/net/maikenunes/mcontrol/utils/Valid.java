package net.maikenunes.mcontrol.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Maike Nunes on 17/05/2015.
 */
public class Valid {
    final static String DATE_FORMAT = "dd/MM/yyyy";
    final static String TIME_FORMAT = "HH:mm";

    /**
     * enviar date no formato dd/MM/yyyy para validação
     * @param date
     * @return boolean
     */
    public static boolean isDateValid(String date)
    {
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        } catch (Exception e){
            return false;
        }
    }

    /**
     * enviar time no formato HH:mm para validação
     * @param time
     * @return boolean
     */
    public static boolean isTimeValid(String time)
    {
        try {
            DateFormat df = new SimpleDateFormat(TIME_FORMAT);
            df.setLenient(false);
            df.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        } catch (Exception e){
            return false;
        }
    }

}
