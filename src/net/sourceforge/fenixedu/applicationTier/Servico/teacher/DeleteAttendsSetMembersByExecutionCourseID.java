/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.AttendsSet;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.domain.IStudentGroupAttend;
import net.sourceforge.fenixedu.domain.StudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroupAttend;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteAttendsSetMembersByExecutionCourseID implements IService {

    public boolean run(Integer executionCourseCode, Integer attendsSetCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentAttendsSet persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = persistentSupport
                .getIPersistentAttendInAttendsSet();
        IPersistentStudentGroupAttend persistentStudentGroupAttend = persistentSupport
                .getIPersistentStudentGroupAttend();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                .getIPersistentExecutionCourse();
        IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet.readByOID(AttendsSet.class,
                attendsSetCode);

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseCode);

        if (attendsSet == null) {
            throw new ExistingServiceException();
        }

        if (executionCourse == null) {
            throw new InvalidSituationServiceException();
        }

        List executionCourseStudentNumbers = new ArrayList();
        List studentsInExecutionCourse = executionCourse.getAttendingStudents();
        Iterator iterStudentsInExecutionCourse = studentsInExecutionCourse.iterator();
        while (iterStudentsInExecutionCourse.hasNext()) {
            IStudent student = (IStudent) iterStudentsInExecutionCourse.next();
            executionCourseStudentNumbers.add(student.getNumber());
        }

        List attendsSetElements = new ArrayList();
        attendsSetElements.addAll(attendsSet.getAttendInAttendsSet());
        Iterator iterator = attendsSetElements.iterator();
        while (iterator.hasNext()) {
            IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet) iterator.next();
            IAttends frequenta = attendInAttendsSet.getAttend();
            if (executionCourseStudentNumbers.contains(frequenta.getAluno().getNumber())) {
                boolean found = false;
                Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
                while (iterStudentsGroups.hasNext() && !found) {
                    final IStudentGroup studentGroup = (IStudentGroup) iterStudentsGroups.next();
                    IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
                            .readByStudentGroupAndAttend(studentGroup.getIdInternal(), frequenta
                                    .getIdInternal());
                    if (oldStudentGroupAttend != null) {
                        oldStudentGroupAttend.getStudentGroup().getStudentGroupAttends().remove(oldStudentGroupAttend);
                        oldStudentGroupAttend.setStudentGroup(null);
                        oldStudentGroupAttend.getAttend().getStudentGroupAttends().remove(oldStudentGroupAttend);
                        oldStudentGroupAttend.setAttend(null);
                        persistentStudentGroupAttend.deleteByOID(StudentGroupAttend.class,
                                oldStudentGroupAttend.getIdInternal());
                        found = true;
                    }
                }
                frequenta.removeAttendInAttendsSet(attendInAttendsSet);
                attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
                persistentAttendInAttendsSet.deleteByOID(AttendInAttendsSet.class, attendInAttendsSet
                        .getIdInternal());
            }
        }
        return true;
    }
}