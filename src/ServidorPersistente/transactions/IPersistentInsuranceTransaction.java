package ServidorPersistente.transactions;

import java.util.List;

import Dominio.IExecutionYear;
import Dominio.IStudent;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPersistentInsuranceTransaction extends IPersistentObject {

    /*
     * public IInsuranceTransaction readByExecutionYearAndStudent(
     * IExecutionYear executionYear, IStudent student) throws
     * ExcepcaoPersistencia;
     */

    public List readAllNonReimbursedByExecutionYearAndStudent(IExecutionYear executionYear,
            IStudent student) throws ExcepcaoPersistencia;

    public List readAllByExecutionYearAndStudent(IExecutionYear executionYear, IStudent student)
            throws ExcepcaoPersistencia;

}