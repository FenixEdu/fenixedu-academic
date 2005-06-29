/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IMasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesis;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeProofVersion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class MasterDegreeProofVersionVO extends VersionedObjectsBase implements
        IPersistentMasterDegreeProofVersion {

    public IMasterDegreeProofVersion readActiveByStudentCurricularPlan(
            IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {

            for (IMasterDegreeThesis masterDegreeThesis : (List<IMasterDegreeThesis>) studentCurricularPlan
                    .getMasterDegreeThesis()) {

                if (masterDegreeThesis.getMasterDegreeProofVersions() != null) {

                    for (IMasterDegreeProofVersion masterDegreeProofVersion : (List<IMasterDegreeProofVersion>) masterDegreeThesis
                            .getMasterDegreeProofVersions()) {
                        if (masterDegreeProofVersion.getCurrentState().equals(State.ACTIVE)) {
                            return masterDegreeProofVersion;
                        }
                    }
                }
            }
        }
        return null;
    }

    public List readNotActiveByStudentCurricularPlan(Integer studentCurricularPlanID)
            throws ExcepcaoPersistencia {

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanID);

        List<IMasterDegreeProofVersion> result = new ArrayList();

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {

            for (IMasterDegreeThesis masterDegreeThesis : (List<IMasterDegreeThesis>) studentCurricularPlan
                    .getMasterDegreeThesis()) {

                if (masterDegreeThesis.getMasterDegreeProofVersions() != null) {

                    for (IMasterDegreeProofVersion masterDegreeProofVersion : (List<IMasterDegreeProofVersion>) masterDegreeThesis
                            .getMasterDegreeProofVersions()) {
                        if (!masterDegreeProofVersion.getCurrentState().equals(State.ACTIVE)) {
                            result.add(masterDegreeProofVersion);
                        }
                    }
                }
            }
        }
        return result;
    }
}