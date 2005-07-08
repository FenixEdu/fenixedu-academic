package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupStudent;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.persistenceTier.IPessoaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.StudentCurricularPlanIDDomainType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class FinalDegreeWorkOrientatorForCandidacy extends AccessControlFilter {

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        final IUserView userView = (IUserView) request.getRequester();
        if (userView == null) {
            throw new NotAuthorizedFilterException("authentication.not.provided");
        }

        //final String studentCurricularPlanId = (String) request.getServiceParameters().parametersArray()[1];
        final StudentCurricularPlanIDDomainType studentCurricularPlanIDDomainType = (StudentCurricularPlanIDDomainType) request.getServiceParameters().parametersArray()[1];
        final Integer studentCurricularPlanId = studentCurricularPlanIDDomainType.getId();
        if (studentCurricularPlanId == null) {
            throw new NotAuthorizedFilterException("no.student.specified");
        } else if (studentCurricularPlanId.intValue() < 0) {
            return;
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPessoaPersistente persistentPerson = persistentSupport.getIPessoaPersistente();

        final IPerson person = persistentPerson.lerPessoaPorUsername(userView.getUtilizador());
        for (final ITeacher teacher : person.getAssociatedTeachers()) {

            final List<IProposal> orientatingProposals = teacher.getAssociatedProposalsByOrientator();
            final List<IProposal> coorientatingProposals = teacher.getAssociatedProposalsByCoorientator();

            final List<IProposal> proposals = new ArrayList(orientatingProposals.size() + coorientatingProposals.size());
            proposals.addAll(orientatingProposals);
            proposals.addAll(coorientatingProposals);

            for (final IProposal proposal : proposals) {
                for (final IGroupProposal groupProposal : proposal.getGroupProposals()) {
                    final IGroup group = groupProposal.getFinalDegreeDegreeWorkGroup();
                    for (final IGroupStudent groupStudent : group.getGroupStudents()) {
                        final IStudent student = groupStudent.getStudent();
                        for (final IStudentCurricularPlan studentCurricularPlan : student.getStudentCurricularPlans()) {
                            if (studentCurricularPlan.getIdInternal().equals(studentCurricularPlanId)) {
                                return;
                            }
                        }
                    }
                }
            }

        }

        throw new NotAuthorizedFilterException();
    }

}
