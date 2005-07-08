package net.sourceforge.fenixedu.persistenceTier.transactions;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

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

    public List readAllNonReimbursedByExecutionYearAndStudent(Integer executionYearID,
            Integer studentID) throws ExcepcaoPersistencia;

    public List readAllByExecutionYearAndStudent(Integer executionYearID, Integer studentID)
            throws ExcepcaoPersistencia;

}