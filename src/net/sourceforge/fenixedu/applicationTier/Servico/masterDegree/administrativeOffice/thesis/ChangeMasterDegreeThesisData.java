package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ChangeMasterDegreeThesisData extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanID, String dissertationTitle,
            List<Integer> guidersNumbers, List<Integer> assistentGuidersNumbers,
            List<Integer> externalGuidersIDs, List<Integer> externalAssistentGuidersIDs)
            throws FenixServiceException, ExcepcaoPersistencia {

        // check duplicate guiders and assistent guiders
        if (CollectionUtils.intersection(guidersNumbers, assistentGuidersNumbers).size() > 0) {
            throw new GuiderAlreadyChosenServiceException(
                    "error.exception.masterDegree.guiderAlreadyChosen");
        }

        // check duplicate external guiders and external assistent guiders
        if (CollectionUtils.intersection(externalGuidersIDs, externalAssistentGuidersIDs).size() > 0) {
            throw new GuiderAlreadyChosenServiceException(
                    "error.exception.masterDegree.externalGuiderAlreadyChosen");
        }

        StudentCurricularPlan studentCurricularPlan = rootDomainObject
                .readStudentCurricularPlanByOID(studentCurricularPlanID);
        MasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = studentCurricularPlan
                .readActiveMasterDegreeThesisDataVersion();
        if (storedMasterDegreeThesisDataVersion == null) {
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistentMasterDegreeThesis");
        }

        storedMasterDegreeThesisDataVersion.setCurrentState(new State(State.INACTIVE));

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersionWithChosenDissertationTitle = MasterDegreeThesisDataVersion
                .readActiveByDissertationTitle(dissertationTitle);

        if (masterDegreeThesisDataVersionWithChosenDissertationTitle != null) {
            if (!masterDegreeThesisDataVersionWithChosenDissertationTitle.getMasterDegreeThesis()
                    .getStudentCurricularPlan().getIdInternal().equals(studentCurricularPlanID)) {
                throw new ExistingServiceException(
                        "error.exception.masterDegree.dissertationTitleAlreadyChosen");
            }
        }

        Employee employee = userView.getPerson().getEmployee();

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = new MasterDegreeThesisDataVersion(
                storedMasterDegreeThesisDataVersion.getMasterDegreeThesis(), employee,
                dissertationTitle, new Date(), new State(State.ACTIVE));

        Collection<Teacher> guiders = Teacher.readByNumbers(guidersNumbers);
        Collection<Teacher> assistentGuiders = Teacher.readByNumbers(assistentGuidersNumbers);
        Collection<ExternalContract> externalGuiders = ExternalContract.readByIDs(externalGuidersIDs);
        Collection<ExternalContract> externalAssistentGuiders = ExternalContract
                .readByIDs(externalAssistentGuidersIDs);

        masterDegreeThesisDataVersion.getGuiders().addAll(guiders);
        masterDegreeThesisDataVersion.getAssistentGuiders().addAll(assistentGuiders);
        masterDegreeThesisDataVersion.getExternalGuiders().addAll(externalGuiders);
        masterDegreeThesisDataVersion.getExternalAssistentGuiders().addAll(externalAssistentGuiders);

        studentCurricularPlan.getMasterDegreeThesis().getMasterDegreeThesisDataVersions().add(
                masterDegreeThesisDataVersion);
    }
}
