/*
 * Created on May 10, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author mrsp and jdnf
 */
public class ExecutionPeriodVO extends VersionedObjectsBase implements IPersistentExecutionPeriod {

        
    public List readAllExecutionPeriod() throws ExcepcaoPersistencia {
        List executionPeriods = (List) readAll(ExecutionPeriod.class); 
        if(executionPeriods != null)
            return executionPeriods;
        
        return new ArrayList();
    }
    
    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod();   
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if(element.getState().equals(PeriodState.CURRENT))
                return element;
        }                              
        return null;
    }

   
    public IExecutionPeriod readByNameAndExecutionYear(String executionPeriodName, String year) throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod();   
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if((element.getExecutionYear().equals(year)) && (element.getName().equals(executionPeriodName)))
                return element;
        }        
        return null;
    }

    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year) throws ExcepcaoPersistencia {        
        if(year == null)
            return null;      
        List executionPeriods = readAllExecutionPeriod();   
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if((element.getExecutionYear().equals(year)) && (element.getSemester() == semester))
                return element;
        }        
        return null;
    }

    
    public List readPublic() throws ExcepcaoPersistencia {   
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();     
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if((!element.getState().equals(PeriodState.NOT_OPEN)) && 
                    (element.getSemester().compareTo(new Integer(0)) > 0))
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }

   
    public List readByExecutionYear(Integer executionYearID) throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();
        
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if(element.getExecutionYear().getIdInternal() == executionYearID)
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }

    
    public List readNotClosedExecutionPeriods() throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();
        
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if(!element.getState().equals(PeriodState.CLOSED))
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }
    
    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();
        
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if((element.getSemester().compareTo(new Integer(0)) > 0) && 
                    (!element.getState().equals(PeriodState.CLOSED)) &&
                    (!element.getState().equals(PeriodState.NOT_OPEN)))
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }

    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();
        
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if(element.getBeginDate().after(start) && element.getEndDate().before(end))
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }

    
    public List readNotClosedPublicExecutionPeriodsByExecutionYears(Integer executionYearID) throws ExcepcaoPersistencia {
        List executionPeriods = readAllExecutionPeriod(); 
        List executionPeriodsAux = new ArrayList();
        
        for (Iterator iter = executionPeriods.iterator(); iter.hasNext();) {
            ExecutionPeriod element = (ExecutionPeriod) iter.next();
            if((element.getExecutionYear().getIdInternal() == executionYearID) && 
                    (!element.getState().equals(PeriodState.CLOSED)) &&
                    (!element.getState().equals(PeriodState.NOT_OPEN)) &&
                    (element.getSemester().compareTo(new Integer(0)) > 0))
                executionPeriodsAux.add(element);
        }     
        return executionPeriodsAux;
    }
}
