/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteShifts extends DataTranferObject implements ISiteComponent {

    private String infoExecutionPeriodName;

    private String infoExecutionYearName;

    private List shifts;
    
    private InfoShift oldShift;

    /**
     * @return
     */
    public List getShifts() {
        return shifts;
    }

    /**
     * @param list
     */
    public void setShifts(List list) {
        shifts = list;
    }
    
    /**
     * @return InfoShift
     */
    public InfoShift getOldShift() {
        return oldShift;
    }

    /**
     * @param InfoShift
     */
    public void setOldShift(InfoShift oldShift) {
    	this.oldShift = oldShift;
    }


    /**
     * @return
     */
    public String getInfoExecutionPeriodName() {
        return infoExecutionPeriodName;
    }

    /**
     * @return
     */
    public String getInfoExecutionYearName() {
        return infoExecutionYearName;
    }

    /**
     * @param string
     */
    public void setInfoExecutionPeriodName(String string) {
        infoExecutionPeriodName = string;
    }

    /**
     * @param string
     */
    public void setInfoExecutionYearName(String string) {
        infoExecutionYearName = string;
    }

}