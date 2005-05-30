/*
 * Created on Oct 13, 2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.Predicate;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class MasterDegreeThesisDataVersionVO extends VersionedObjectsBase implements
        IPersistentMasterDegreeThesisDataVersion {

    public IMasterDegreeThesisDataVersion readActiveByStudentCurricularPlan(
            Integer studentCurricularPlanId) throws ExcepcaoPersistencia {

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanId);

        if (studentCurricularPlan.getMasterDegreeThesis() != null) {
            
            final List<IMasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                    .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

            for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    return masterDegreeThesisDataVersion;
                }
            }
        }

        return null;
    }

    public IMasterDegreeThesisDataVersion readActiveByDissertationTitle(String dissertationTitle)
            throws ExcepcaoPersistencia {

        Collection<IMasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = readAll(MasterDegreeThesisDataVersion.class);

        for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
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

        final IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) readByOID(
                StudentCurricularPlan.class, studentCurricularPlanId);

        final List<IMasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

        CollectionUtils.filter(masterDegreeThesisDataVersions, new Predicate() {

            public boolean evaluate(Object arg0) {
                IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = (IMasterDegreeThesisDataVersion) arg0;
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.INACTIVE)) {
                    return true;
                } else {
                    return false;
                }
            }

        });

        return masterDegreeThesisDataVersions;
    }

    public List readActiveByDegreeCurricularPlan(Integer degreeCurricularPlanID)
            throws ExcepcaoPersistencia {

        List result = new ArrayList();

        final IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) readByOID(
                DegreeCurricularPlan.class, degreeCurricularPlanID);

        for (IStudentCurricularPlan studentCurricularPlan : (List<IStudentCurricularPlan>) degreeCurricularPlan
                .getStudentCurricularPlans()) {

            final List<IMasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = studentCurricularPlan
                    .getMasterDegreeThesis().getMasterDegreeThesisDataVersions();

            for (IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesisDataVersions) {
                if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
                    result.add(masterDegreeThesisDataVersion);
                }
            }
        }

        return result;
    }

}