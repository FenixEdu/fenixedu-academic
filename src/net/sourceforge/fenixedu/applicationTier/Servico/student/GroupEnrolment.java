/*
 * Created on 28/Ago/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NoChangeMadeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */
public class GroupEnrolment implements IService {

    public boolean run(Integer groupingID, Integer shiftID, Integer groupNumber, List studentIDs,
            String username) throws FenixServiceException, ExcepcaoPersistencia {

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final IGrouping grouping = (IGrouping) persistentSupport.getIPersistentGrouping().readByOID(
                Grouping.class, groupingID);
        if (grouping == null) {
            throw new NonExistingServiceException();
        }

        final IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        final IStudent userStudent = persistentStudent.readByUsername(username);

        List<IStudent> studentsList = new ArrayList<IStudent>();
        for (final Integer studentID : (List<Integer>) studentIDs) {
            IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentID);
            studentsList.add(student);
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);
        if (!strategy.checkStudentInGrouping(grouping, userStudent.getPerson().getUsername())) {
            throw new NoChangeMadeServiceException();
        }

        IShift shift = null;
        if (shiftID != null) {
            shift = (IShift) persistentSupport.getITurnoPersistente().readByOID(Shift.class, shiftID);
        }
        Integer result = strategy.enrolmentPolicyNewGroup(grouping, studentIDs.size() + 1, shift);

        if (result.equals(Integer.valueOf(-1))) {
            throw new InvalidArgumentsServiceException();
        }
        if (result.equals(Integer.valueOf(-2))) {
            throw new NonValidChangeServiceException();
        }
        if (result.equals(Integer.valueOf(-3))) {
            throw new NotAuthorizedException();
        }

        IStudentGroup newStudentGroup = grouping.readStudentGroupBy(groupNumber);
        if (newStudentGroup != null) {
            throw new FenixServiceException();
        }

        if (!strategy.checkStudentsInGrouping(studentIDs, grouping)) {
            throw new InvalidStudentNumberServiceException();
        }

        final IAttends userAttend = grouping.getStudentAttend(userStudent);

        checkStudentCodesAndUserAttendInStudentGroup(studentsList, grouping, userAttend);
        newStudentGroup = DomainFactory.makeStudentGroup(groupNumber, grouping, shift);

        grouping.addStudentGroups(newStudentGroup);

        for (final IStudent student : studentsList) {
            IAttends attend = grouping.getStudentAttend(student);
            attend.addStudentGroups(newStudentGroup);

        }

        return true;
    }

    private void checkStudentCodesAndUserAttendInStudentGroup(final List<IStudent> students,
            final IGrouping grouping, final IAttends userAttend) throws FenixServiceException,
            ExcepcaoPersistencia {

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(grouping);

        if (strategy.checkAlreadyEnroled(grouping, userAttend.getAluno().getPerson().getUsername())) {
            throw new InvalidSituationServiceException();
        }

        for (final IStudent student : students) {
            if (strategy.checkAlreadyEnroled(grouping, student.getPerson().getUsername())) {
                throw new InvalidSituationServiceException();
            }
        }
    }
}