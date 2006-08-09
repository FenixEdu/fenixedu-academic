package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class CreateMasterDegreeThesis extends Service {

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
        MasterDegreeThesis storedMasterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
        if (storedMasterDegreeThesis != null) {
            throw new ExistingServiceException("error.exception.masterDegree.existingMasterDegreeThesis");
        }

        MasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = MasterDegreeThesisDataVersion
                .readActiveByDissertationTitle(dissertationTitle);
        if ((storedMasterDegreeThesisDataVersion != null)
                && (!storedMasterDegreeThesisDataVersion.getMasterDegreeThesis()
                        .getStudentCurricularPlan().getIdInternal().equals(studentCurricularPlanID))) {
            throw new ExistingServiceException(
                    "error.exception.masterDegree.dissertationTitleAlreadyChosen");
        }

        Employee employee = userView.getPerson().getEmployee();

        MasterDegreeThesis masterDegreeThesis = new MasterDegreeThesis();
        masterDegreeThesis.setStudentCurricularPlan(studentCurricularPlan);

        // write data version
        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = new MasterDegreeThesisDataVersion(
                masterDegreeThesis, employee, dissertationTitle, new Date(), new State(State.ACTIVE));

        Collection<Teacher> guiders = Teacher.readByNumbers(guidersNumbers);
        Collection<Teacher> assistentGuiders = Teacher.readByNumbers(assistentGuidersNumbers);
        Collection<ExternalPerson> externalGuiders = ExternalPerson.readByIDs(externalGuidersIDs);
        Collection<ExternalPerson> externalAssistentGuiders = ExternalPerson
                .readByIDs(externalAssistentGuidersIDs);

        masterDegreeThesisDataVersion.getGuiders().addAll(guiders);
        masterDegreeThesisDataVersion.getAssistentGuiders().addAll(assistentGuiders);
        masterDegreeThesisDataVersion.getExternalGuiders().addAll(externalGuiders);
        masterDegreeThesisDataVersion.getExternalAssistentGuiders().addAll(externalAssistentGuiders);

        masterDegreeThesis.getMasterDegreeThesisDataVersions().add(masterDegreeThesisDataVersion);

    }

}
