/*
 * Created on 5/Mai/2003
 *
 * 
 */
package DataBeans;

import java.util.List;

import Dominio.IExecutionPeriod;

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