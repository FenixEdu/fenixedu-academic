/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class MasterDegreeThesisDataVersionVO extends VersionedObjectsBase implements
        IPersistentMasterDegreeThesisDataVersion {

    public MasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
            Integer studentCurricularPlanId) throws ExcepcaoPersistencia {

        final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanId);

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {
            
            final List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                    .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeThesisDataVersion;
                }
            }
        }

        return null;
    }

    public MasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle)
            throws ExcepcaoPersistencia {

        Collection<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = readAll(MasterDegreeThesisDataVersion.class);

        for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
            if ((masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE))
                    && (masterDegreeThesisDataVersion.getDissertationTitle()
                            .equalsIgnoreCase(dissertationTitle))) {
                return masterDegreeThesisDataVersion;
            }
        }

        return null;
    }

    public List readNotActivesVersionsByStudentCurricularPlan(Integer studentCurricularPlanId)
            throws ExcepcaoPersistencia {

        final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanId);

        final List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

        CollectionUtils.filter(masterDegreeThesisDataVersions, new Predicate() {

            public boolean evaluate(Object arg0) {
                MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (MasterDegreeThesisDataVersion) arg0;
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.INACTIVE)) {
                    return true;
                } 
                return false;
            }

        });

        return masterDegreeThesisDataVersions;
    }

    public List readActiveByDegreeCurricularPlan(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {

        List result = new ArrayList();

        final DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        for (StudentCurricularPlan studentCurricularPlan : degreeCurricularPlan.getStudentCurricularPlans()) {

            final List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                    .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

            for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    result.add(masterDegreeThesisDataVersion);
                }
            }
        }

        return result;
    }

}