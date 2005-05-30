package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente
 *  
 */
public interface IPersistentExecutionPeriod extends IPersistentObject {

    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia;

    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia;
   
    public IExecutionPeriod readByNameAndExecutionYear(String executionPeriodName,
            String year) throws ExcepcaoPersistencia;

    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year)
            throws ExcepcaoPersistencia;

    public List readPublic() throws ExcepcaoPersistencia;

    public List readByExecutionYear(Integer executionYear) throws ExcepcaoPersistencia;
 
    public List readNotClosedExecutionPeriods() throws ExcepcaoPersistencia;

    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia;
	
	public List readNotClosedPublicExecutionPeriodsByExecutionYears(Integer executionYear) throws ExcepcaoPersistencia; 
}