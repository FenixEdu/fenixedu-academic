/*
 * Created on 5/Mai/2003
 *
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * @author João Mota
 * 
 *  
 */
public class InfoSiteTimetable extends DataTranferObject implements ISiteComponent {

	private List lessons;

	private InfoExecutionPeriod infoExecutionPeriod;

    /**
     * @return
     */
    public List getLessons() {
        return lessons;
    }

    /**
     * @param list
     */
    public void setLessons(List list) {
        lessons = list;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }
    

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }
    

}