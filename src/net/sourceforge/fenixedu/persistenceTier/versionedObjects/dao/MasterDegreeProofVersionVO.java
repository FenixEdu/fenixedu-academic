/*
 * Created on Oct 13, 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
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

    public MasterDegreeProofVersion readActiveByStudentCurricularPlan(
            StudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {

            for (MasterDegreeThesis masterDegreeThesis : (List<MasterDegreeThesis>) studentCurricularPlan
                    .getMasterDegreeThesis()) {

                if (masterDegreeThesis.getMasterDegreeProofVersions() != null) {

                    for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis
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

        final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanID);

        List<MasterDegreeProofVersion> result = new ArrayList();

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {

            for (MasterDegreeThesis masterDegreeThesis : (List<MasterDegreeThesis>) studentCurricularPlan
                    .getMasterDegreeThesis()) {

                if (masterDegreeThesis.getMasterDegreeProofVersions() != null) {

                    for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis
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