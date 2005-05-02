/*
 * Created on Oct 14, 2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public interface IPersistentMasterDegreeThesisDataVersion extends IPersistentObject {
    public IMasterDegreeThesisDataVersion readActiveByMasterDegreeThesis(
            IMasterDegreeThesis masterDegreeThesis) throws ExcepcaoPersistencia;

    public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    public IMasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle)
            throws ExcepcaoPersistencia;

    public List readNotActivesVersionsByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;

    public List readActiveByDegreeCurricularPlan(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia;

}