/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentMasterDegreeProofVersion extends IPersistentObject {

    public IMasterDegreeProofVersion readActiveByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    public List readNotActiveByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia;
}