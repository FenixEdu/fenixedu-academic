package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author asnr and scpo
 * 
 */

public class UnEnrollStudentInGroup implements IService {

    public Boolean run(String userName, Integer studentGroupCode) throws FenixServiceException,
            ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentStudentGroup persistentStudentGroup = persistentSuport.getIPersistentStudentGroup();
        IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSuport
                .getIPersistentStudentGroupAttend();
        IPersistentStudent persistentStudent = persistentSuport.getIPersistentStudent();

        IStudentGroup studentGroup = (IStudentGroup) persistentStudentGroup.readByOID(
                StudentGroup.class, studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidSituationServiceException();
        }

        IStudent student = persistentStudent.readByUsername(userName);

        IGroupProperties groupProperties = studentGroup.getAttendsSet().getGroupProperties();

        IAttends attend = studentGroup.getAttendsSet().getStudentAttend(student);

        if (attend == null) {
            throw new NotAuthorizedException();
        }

        IStudentGroupAttend studentGroupAttendToDelete = persistentStudentGroupAttend
                .readByStudentGroupAndAttend(studentGroup.getIdInternal(), attend.getIdInternal());

        if (studentGroupAttendToDelete == null) {
            throw new InvalidArgumentsServiceException();
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
                .getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
                .getGroupEnrolmentStrategyInstance(groupProperties);

        boolean resultEmpty = strategy.checkIfStudentGroupIsEmpty(studentGroupAttendToDelete,
                studentGroup);

        studentGroupAttendToDelete.getStudentGroup().getStudentGroupAttends().remove(
                studentGroupAttendToDelete);
        studentGroupAttendToDelete.setStudentGroup(null);
        studentGroupAttendToDelete.getAttend().getStudentGroupAttends().remove(
                studentGroupAttendToDelete);
        studentGroupAttendToDelete.setAttend(null);
        persistentStudentGroupAttend.deleteByOID(StudentGroupAttend.class, studentGroupAttendToDelete
                .getIdInternal());

        if (resultEmpty) {
            groupProperties.getAttendsSet().removeStudentGroup(studentGroup);
            studentGroup.setAttendsSet(null);
            if (studentGroup.getShift().getAssociatedStudentGroups() != null) {
                studentGroup.getShift().getAssociatedStudentGroups().remove(studentGroup);
            }
            studentGroup.setShift(null);
            persistentStudentGroup.deleteByOID(StudentGroup.class, studentGroup.getIdInternal());
            return new Boolean(false);
        }

        return new Boolean(true);
    }

}