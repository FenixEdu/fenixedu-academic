/*
 * Created on 14/Out/2003
 *
 */
package Dominio;

import java.util.Calendar;
import java.util.List;

/**
 * @author Ana e Ricardo
 *  
 */
public class Period extends DomainObject implements IPeriod {

    protected Calendar startDate;

    protected Calendar endDate;

    protected List roomOccupations;

    public Period() {
    }

    public Period(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Period(Calendar startDate, Calendar endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public Calendar getEndDate() {
        return endDate;
    }

    /**
     * @return
     */
    public Calendar getStartDate() {
        return startDate;
    }

    /**
     * @param calendar
     */
    public void setEndDate(Calendar calendar) {
        endDate = calendar;
    }

    /**
     * @param calendar
     */
    public void setStartDate(Calendar calendar) {
        startDate = calendar;
    }

    public boolean equals(Object obj) {
        if (obj instanceof IPeriod) {
            IPeriod periodObj = (IPeriod) obj;
            if (startDate.equals(periodObj.getStartDate())
                    && endDate.equals(periodObj.getEndDate())) {
                return true;
            }

            return false;

        }
        return false;
    }

    /**
     * @return
     */
    public List getRoomOccupations() {
        return roomOccupations;
    }

    /**
     * @param roomOccupations
     */
    public void setRoomOccupations(List roomOccupations) {
        this.roomOccupations = roomOccupations;
    }

}