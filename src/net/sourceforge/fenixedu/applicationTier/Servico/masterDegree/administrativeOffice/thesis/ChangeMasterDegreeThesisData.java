package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.GuiderAlreadyChosenServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IExternalPerson;
import net.sourceforge.fenixedu.domain.IMasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
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
        
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) sp
                .getIStudentCurricularPlanPersistente().readByOID(StudentCurricularPlan.class,
                        studentCurricularPlanID, true);

        IMasterDegreeThesisDataVersion storedMasterDegreeThesisDataVersion = sp
                .getIPersistentMasterDegreeThesisDataVersion().readActiveByStudentCurricularPlan(
                        studentCurricularPlanID);
        if (storedMasterDegreeThesisDataVersion == null) {
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistentMasterDegreeThesis");
        }

        storedMasterDegreeThesisDataVersion.setCurrentState(new State(State.INACTIVE));
        sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(
                storedMasterDegreeThesisDataVersion);

        IMasterDegreeThesisDataVersion masterDegreeThesisDataVersionWithChosenDissertationTitle = sp
                .getIPersistentMasterDegreeThesisDataVersion().readActiveByDissertationTitle(
                        dissertationTitle);

        if (masterDegreeThesisDataVersionWithChosenDissertationTitle != null) {
            if (!masterDegreeThesisDataVersionWithChosenDissertationTitle.getMasterDegreeThesis()
                    .getStudentCurricularPlan().getIdInternal().equals(studentCurricularPlanID)) {
                throw new ExistingServiceException(
                        "error.exception.masterDegree.dissertationTitleAlreadyChosen");
            }
        }

        IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
        IEmployee employee = sp.getIPersistentEmployee().readByPerson(person.getIdInternal().intValue());

        IMasterDegreeThesisDataVersion masterDegreeThesisDataVersion = new MasterDegreeThesisDataVersion(
                storedMasterDegreeThesisDataVersion.getMasterDegreeThesis(), employee,
                dissertationTitle, new Timestamp(new Date().getTime()), new State(State.ACTIVE));

        Collection<ITeacher> guiders = sp.getIPersistentTeacher().readByNumbers(guidersNumbers);
        Collection<ITeacher> assistentGuiders = sp.getIPersistentTeacher().readByNumbers(
                assistentGuidersNumbers);
        Collection<IExternalPerson> externalGuiders = sp.getIPersistentExternalPerson().readByIDs(
                externalGuidersIDs);
        Collection<IExternalPerson> externalAssistentGuiders = sp.getIPersistentExternalPerson()
                .readByIDs(externalAssistentGuidersIDs);

        masterDegreeThesisDataVersion.getGuiders().addAll((List) guiders);
        masterDegreeThesisDataVersion.getAssistentGuiders().addAll((List) assistentGuiders);
        masterDegreeThesisDataVersion.getExternalGuiders().addAll((List) externalGuiders);
        masterDegreeThesisDataVersion.getExternalAssistentGuiders().addAll((List) externalAssistentGuiders);

        sp.getIPersistentMasterDegreeThesisDataVersion().simpleLockWrite(masterDegreeThesisDataVersion);

        studentCurricularPlan.getMasterDegreeThesis().getMasterDegreeThesisDataVersions().add(
                masterDegreeThesisDataVersion);
        sp.getIPersistentMasterDegreeThesis().simpleLockWrite(
                studentCurricularPlan.getMasterDegreeThesis());

    }
}
