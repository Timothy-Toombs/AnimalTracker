package toombs.animaltracker;

import java.util.GregorianCalendar;

public class AnimalLogItem {
    private String logMsg;
    private GregorianCalendar infoDate;
    private long logWrapperUID;

    public AnimalLogItem(String logMsg, GregorianCalendar infoDate, long logWrapperUID) {
        this.logMsg = logMsg;
        this.infoDate = infoDate;
        this.logWrapperUID = logWrapperUID;
    }

    public String getLogMsg() {
        return logMsg;
    }

    public GregorianCalendar getInfoDate() {
        return infoDate;
    }

    public long getLogWrapperUID() {
        return logWrapperUID;
    }
}
