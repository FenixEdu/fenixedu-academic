/*
 * Created on 10/Set/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IStudentTestLog;

/**
 * @author Susana Fernandes
 */
public class InfoStudentTestLog extends InfoObject {
    private InfoStudent infoStudent;

    private InfoDistributedTest infoDistributedTest;

    private Date date;

    private String event;

    private List eventList;

    public InfoStudentTestLog() {
    }

    public Date getDate() {
        return date;
    }

    public String getEvent() {
        return event;
    }

    public InfoDistributedTest getInfoDistributedTest() {
        return infoDistributedTest;
    }

    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setEvent(String string) {
        event = string;
    }

    public void setInfoDistributedTest(InfoDistributedTest test) {
        infoDistributedTest = test;
    }

    public void setInfoStudent(InfoStudent student) {
        infoStudent = student;
    }

    public List getEventList() {
        return eventList;
    }

    public void setEventList(List list) {
        eventList = list;
    }

    public String getDateFormatted() {
        String result = new String();

        Date date = getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10)
            result += "0";
        result += calendar.get(Calendar.MINUTE);
        result += ":";
        if (calendar.get(Calendar.SECOND) < 10)
            result += "0";
        result += calendar.get(Calendar.SECOND);
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoStudentTestLog) {
            InfoStudentTestLog infoStudentTestLog = (InfoStudentTestLog) obj;
            result = getIdInternal().equals(infoStudentTestLog.getIdInternal());
            result = result || (getInfoStudent().equals(infoStudentTestLog.getInfoStudent()))
                    && (getInfoDistributedTest().equals(infoStudentTestLog.getInfoDistributedTest()))
                    && (getDate().equals(infoStudentTestLog.getDate()))
                    && (getEvent().equals(infoStudentTestLog.getEvent()));
        }
        return result;
    }

    public void copyFromDomain(IStudentTestLog studentTestLog) {
        super.copyFromDomain(studentTestLog);
        if (studentTestLog != null) {
            setDate(studentTestLog.getDate());
            setEvent(studentTestLog.getEvent());
            //setEventList(studentTestLog.getEventList());
        }
    }

    public static InfoStudentTestLog newInfoFromDomain(IStudentTestLog studentTestLog) {
        InfoStudentTestLog infoStudentTestLog = null;
        if (studentTestLog != null) {
            infoStudentTestLog = new InfoStudentTestLog();
            infoStudentTestLog.copyFromDomain(studentTestLog);
        }
        return infoStudentTestLog;
    }

}