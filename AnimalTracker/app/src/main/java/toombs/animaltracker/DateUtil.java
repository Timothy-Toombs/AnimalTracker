package toombs.animaltracker;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static GregorianCalendar setCurrentInfoDate() {
        Date d = new Date();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        return calendar;
    }

    public static String logMsgInfoDateToString(GregorianCalendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd, yyyy");
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
    }

    public static String infoDateToString(GregorianCalendar calendar) {
        SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yy");
        fmt.setCalendar(calendar);
        return fmt.format(calendar.getTime());
    }
}
