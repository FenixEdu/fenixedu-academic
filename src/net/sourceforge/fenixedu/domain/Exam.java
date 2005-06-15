/*
 * Created on 18/Mar/2003
 *  
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.util.Season;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class Exam extends Exam_Base {

    public Exam() {
    }

    public Exam(Calendar day, Calendar beginning, Calendar end, Season season) {
        this.setDay(day);
        this.setBeginning(beginning);
        this.setEnd(end);
        this.setSeason(season);
    }

    public String toString() {
        return "[EXAM:" + " id= '" + this.getIdInternal() + "'\n" + " day= '" + this.getDay() + "'\n"
                + " beginning= '" + this.getBeginning() + "'\n" + " end= '" + this.getEnd() + "'\n"
                + " season= '" + this.getSeason() + "'\n" + "";
    }

    public boolean equals(Object obj) {
        if (obj instanceof IExam) {
            IExam examObj = (IExam) obj;
            return this.getIdInternal().equals(examObj.getIdInternal());
        }

        return false;
    }

    /**
     * @return
     */
    public List getAssociatedRooms() {
        return (List) CollectionUtils.collect(super.getAssociatedRoomOccupation(), new Transformer() {

            public Object transform(Object arg0) {
                IRoomOccupation roomOccupation = (IRoomOccupation) arg0;
                return roomOccupation.getRoom();
            }
        });
    }

}
