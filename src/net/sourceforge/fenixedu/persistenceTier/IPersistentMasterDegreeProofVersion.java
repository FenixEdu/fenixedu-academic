/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentMasterDegreeProofVersion extends IPersistentObject {

    public MasterDegreeProofVersion readActiveByStudentCurricularPlan(
            StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    public List readNotActiveByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia;
}