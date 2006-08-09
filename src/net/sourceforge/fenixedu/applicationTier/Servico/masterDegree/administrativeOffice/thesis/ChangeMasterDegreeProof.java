package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.thesis;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ScholarshipNotFinishedServiceException;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.MasterDegreeProofVersion;
import net.sourceforge.fenixedu.domain.MasterDegreeThesis;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.State;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class ChangeMasterDegreeProof extends Service {

    public void run(IUserView userView, Integer studentCurricularPlanID, Date proofDate,
            Date thesisDeliveryDate, MasterDegreeClassification finalResult,
            Integer attachedCopiesNumber, List<Integer> teacherJuriesNumbers,
            List<Integer> externalJuriesIDs) throws FenixServiceException, ExcepcaoPersistencia {

        StudentCurricularPlan studentCurricularPlan = rootDomainObject
                .readStudentCurricularPlanByOID(studentCurricularPlanID);
        MasterDegreeThesis storedMasterDegreeThesis = studentCurricularPlan.getMasterDegreeThesis();
        if (storedMasterDegreeThesis == null) {
            throw new NonExistingServiceException(
                    "error.exception.masterDegree.nonExistentMasterDegreeThesis");
        }

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();
        IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

        if (!masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan)) {
            throw new ScholarshipNotFinishedServiceException(
                    "error.exception.masterDegree.scholarshipNotFinished");
        }

        MasterDegreeProofVersion storedMasterDegreeProofVersion = studentCurricularPlan
                .readActiveMasterDegreeProofVersion();
        if (storedMasterDegreeProofVersion != null) {
            storedMasterDegreeProofVersion.setCurrentState(new State(State.INACTIVE));
        }

        Employee employee = userView.getPerson().getEmployee();

        List<Teacher> teacherJuries = (List<Teacher>) Teacher.readByNumbers(teacherJuriesNumbers);
        List<ExternalPerson> externalJuries = ExternalPerson.readByIDs(externalJuriesIDs);

        new MasterDegreeProofVersion(storedMasterDegreeThesis, employee, new Date(),
                proofDate, thesisDeliveryDate, finalResult, attachedCopiesNumber,
                new State(State.ACTIVE), teacherJuries, externalJuries);

        if (finalResult.equals(MasterDegreeClassification.APPROVED)) {
            Person person = studentCurricularPlan.getStudent().getPerson();
            person.addPersonRoles(Role.getRoleByRoleType(RoleType.ALUMNI));
            person.removeRoleByType(RoleType.STUDENT);
        }

    }

}
