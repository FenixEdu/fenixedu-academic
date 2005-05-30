/*
 * Created on May 20, 2005
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author mrsp and jdnf
 */
public class ExecutionYearVO  extends VersionedObjectsBase implements IPersistentExecutionYear {

    public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia {
        Collection<IExecutionYear> executionYears = readAll(ExecutionYear.class);
        for(IExecutionYear executionYear : executionYears){
            if(executionYear.getYear().equals(year))
                return executionYear;
        }        
        
        return null;
    }
    
    public List readNotClosedExecutionYears() throws ExcepcaoPersistencia {
        Collection<IExecutionYear> executionYears = readAll(ExecutionYear.class);
        List executionYearsAux = new ArrayList();
        
        for(IExecutionYear executionYear : executionYears){
            if(!executionYear.getState().equals(PeriodState.CLOSED))
                executionYearsAux.add(executionYear);
        }        
        return executionYearsAux;
    }
    
    public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia {
        Collection<IExecutionYear> executionYears = readAll(ExecutionYear.class);
        
        for(IExecutionYear executionYear : executionYears){
            if(executionYear.getState().equals(PeriodState.CURRENT))
                return executionYear;
        }        
        return null;
    }

    public List readOpenExecutionYears() throws ExcepcaoPersistencia {
        Collection<IExecutionYear> executionYears = readAll(ExecutionYear.class);
        List executionYearsAux = new ArrayList();
        
        for(IExecutionYear executionYear : executionYears){
            if(executionYear.getState().equals(PeriodState.OPEN))
                executionYearsAux.add(executionYear);
        }        
        return executionYearsAux;
    }

    public List readExecutionYearsInPeriod(Date start, Date end) throws ExcepcaoPersistencia {
        Collection<IExecutionYear> executionYears = readAll(ExecutionYear.class);
        List executionYearsAux = new ArrayList();
        
        for(IExecutionYear executionYear : executionYears){
            if(executionYear.getBeginDate().before(end) && executionYear.getEndDate().after(start))
                executionYearsAux.add(executionYear);
        }        
        return executionYearsAux;
    }
}
