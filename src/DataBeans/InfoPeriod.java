/*
 * Created on Nov 3, 2003
 *
 */
package DataBeans;

import java.util.Calendar;

import Dominio.IPeriod;

/**
 * @author Ana e Ricardo
 *
 */
public class InfoPeriod extends InfoObject {

	protected Calendar startDate;
	protected Calendar endDate;
	
    protected InfoPeriod nextPeriod;

    public InfoPeriod getNextPeriod() {
        return nextPeriod;
    }

    public void setNextPeriod(InfoPeriod nextPeriod) {
        this.nextPeriod = nextPeriod;
    }

	/**
	 * @return Returns the endDate.
	 */
	public Calendar getEndDate() {
		return endDate;
	}

	/**
     * @param endDate
     *            The endDate to set.
	 */
	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return Returns the startDate.
	 */
	public Calendar getStartDate() {
		return startDate;
	}

	/**
     * @param startDate
     *            The startDate to set.
	 */
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

    
    public void copyFromDomain(IPeriod period) {
        if (period != null) {
            setStartDate(period.getStartDate());
            setEndDate(period.getEndDate());
            setIdInternal(period.getIdInternal());
            if (period.getNextPeriod() != null){
                InfoPeriod infoPeriod = new InfoPeriod();
                infoPeriod.copyFromDomain(period.getNextPeriod());
                setNextPeriod(infoPeriod); 
}
        }
    }
    public static InfoPeriod newInfoFromDomain(
            IPeriod period) {
        InfoPeriod infoPeriod = null;
        if (period != null) {
            infoPeriod = new InfoPeriod();
            infoPeriod.copyFromDomain(period);
        }
        return infoPeriod;
    }
    
    public Calendar endDateOfComposite() {
        Calendar end = this.endDate;
        InfoPeriod period = this.nextPeriod;
        while (period != null) {
            end = period.getEndDate();
            period = period.getNextPeriod();
        }
        return end;
    }

}