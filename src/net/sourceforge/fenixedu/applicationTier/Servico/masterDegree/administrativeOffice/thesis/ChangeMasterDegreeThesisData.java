package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class ChangeMasterDegreeThesisData implements IService {

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

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID, true);

        MasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = sp
                .getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(
                        studentCurricularPlanID);
        if (storedMasterDegreeThesisDataVersion == null) {
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistentMasterDegreeThesis");
        }

        storedMasterDegreeThesisDataVersion.setCurrentState(new State(State.INACTIVE));

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersionWithChosenDissertationTitle = sp
                .getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(
                        dissertationTitle);

        if (masterDegreeThesisDataVersionWithChosenDissertationTitle != null) {
            if (!masterDegreeThesisDataVersionWithChosenDissertationTitle.getMasterDegreeThesis()
                    .getStudentCurricularPlan().getIdInternal().equals(studentCurricularPlanID)) {
                throw new ExistingServiceException(
                        "error.exception.masterDegree.dissertationTitleAlreadyChosen");
            }
        }

        Person person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        Employee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());

        MasterDegreeThesisDataVersion masterDegreeThesisDataVersion = DomainFactory
                .makeMasterDegreeThesisDataVersion(storedMasterDegreeThesisDataVersion
                        .getMasterDegreeThesis(), employee, dissertationTitle, new Date(), new State(
                        State.ACTIVE));

        Collection<Teacher> guiders = sp.getIPersistentTeacher().readByNumbers(guidersNumbers);
        Collection<Teacher> assistentGuiders = sp.getIPersistentTeacher().readByNumbers(
                assistentGuidersNumbers);
        Collection<ExternalPerson> externalGuiders = sp.getIPersistentExternalPerson().readByIDs(
                externalGuidersIDs);
        Collection<ExternalPerson> externalAssistentGuiders = sp.getIPersistentExternalPerson()
                .readByIDs(externalAssistentGuidersIDs);

        masterDegreeThesisDataVersion.getGuiders().addAll(guiders);
        masterDegreeThesisDataVersion.getAssistentGuiders().addAll(assistentGuiders);
        masterDegreeThesisDataVersion.getExternalGuiders().addAll(externalGuiders);
        masterDegreeThesisDataVersion.getExternalAssistentGuiders().addAll(externalAssistentGuiders);

        studentCurricularPlan.getMasterDegreeThesis().getMasterDegreeThesisDataVersions().add(
                masterDegreeThesisDataVersion);
    }
}
