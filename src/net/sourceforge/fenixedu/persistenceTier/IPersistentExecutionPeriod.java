package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente
 *  
 */
public interface IPersistentExecutionPeriod extends IPersistentObject {

    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia;

    /**
     * @return ArrayList
     * @throws ExcepcaoPersistencia
     */
    public List readAllExecutionPeriod() throws ExcepcaoPersistencia;

    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia;

    /**
     * Method readByNameAndExecutionYear.
     * 
     * @param string
     * @param iExecutionYear
     * @return IExecutionPeriod
     */
    public IExecutionPeriod readByNameAndExecutionYear(String executionPeriodName,
            String year) throws ExcepcaoPersistencia;

    /**
     * @param string
     * @param year
     * @return
     */
    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year)
            throws ExcepcaoPersistencia;

    /**
     * @param workingArea
     * @param executionPeriodToExportDataFrom
     */
    /*
     * public void transferData( IExecutionPeriod executionPeriodToImportDataTo,
     * IExecutionPeriod executionPeriodToExportDataFrom) throws
     * ExcepcaoPersistencia;
     */
    public List readPublic() throws ExcepcaoPersistencia;

    /**
     * @param executionYear
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public List readByExecutionYear(Integer executionYear) throws ExcepcaoPersistencia;

    /**
     * @return
     */
    public List readNotClosedExecutionPeriods() throws ExcepcaoPersistencia;

    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia;
	
	public List readNotClosedPublicExecutionPeriodsByExecutionYears(Integer executionYear) throws ExcepcaoPersistencia; 
}