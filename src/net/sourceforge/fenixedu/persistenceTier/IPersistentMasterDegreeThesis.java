/*
 * Created on Oct 10, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier;

import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * Authors : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 * (naat@mega.ist.utl.pt)
 *  
 */
public interface IPersistentMasterDegreeThesis extends IPersistentObject {
    /**
     * 
     * @param studentCurricularPlan
     * @return @throws
     *         ExcepcaoPersistencia
     */
    public abstract IMasterDegreeThesis readByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia;
}