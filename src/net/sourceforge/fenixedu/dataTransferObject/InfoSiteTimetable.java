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

	private IExecutionPeriod executionPeriod;

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

    /**
     * @return
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @param infoExecutionPeriod
     */
    public void setExecutionPeriod(IExecutionPeriod iExecutionPeriod) {
    	executionPeriod = iExecutionPeriod;
    }    
 
}