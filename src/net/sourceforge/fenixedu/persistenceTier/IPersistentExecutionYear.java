package net.sourceforge.fenixedu.persistenceTier;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionYear;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente
 *  
 */
public interface IPersistentExecutionYear extends IPersistentObject {

    public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia;

    public List readAllExecutionYear() throws ExcepcaoPersistencia;

    public List readNotClosedExecutionYears() throws ExcepcaoPersistencia;

    public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia;

    public List readOpenExecutionYears() throws ExcepcaoPersistencia;
    
    public List readExecutionYearsInPeriod(Date start, Date end) throws ExcepcaoPersistencia;
}