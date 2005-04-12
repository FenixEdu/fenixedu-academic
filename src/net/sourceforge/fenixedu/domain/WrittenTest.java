/*
 * Created on 10/Out/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

/**
 * @author Ana e Ricardo
 *  
 */
public class WrittenTest extends WrittenTest_Base {

    public WrittenTest() {
    }

    public WrittenTest(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public WrittenTest(Calendar day, Calendar beginning, Calendar end) {
        this.setDay(day);
        this.setBeginning(beginning);
        this.setEnd(end);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IWrittenTest) {
            IWrittenTest writtenTestObj = (IWrittenTest) obj;
            return this.getIdInternal().equals(writtenTestObj.getIdInternal());
        }

        return false;
    }

    public String toString() {
        return "[WRITTEN_TEST:" + " id= '" + this.getIdInternal() + "'\n" + " day= '" + this.getDay()
                + "'\n" + " beginning= '" + this.getBeginning() + "'\n" + " end= '" + this.getEnd()
                + "'\n" + "";
    }

}