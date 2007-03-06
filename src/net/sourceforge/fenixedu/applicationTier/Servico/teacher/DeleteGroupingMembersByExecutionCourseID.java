/*
 * Created on 04/Sep/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembersByExecutionCourseID extends Service {

    public boolean run(Integer executionCourseCode, Integer groupingCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        Grouping grouping = rootDomainObject.readGroupingByOID(groupingCode);
        
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseCode);
       
        if (executionCourse == null) {
            throw new InvalidSituationServiceException();
        }

        List executionCourseStudentNumbers = new ArrayList();
        final List<Attends> attends = executionCourse.getAttends();
        for (final Attends attend : attends) {
            final Registration registration = attend.getRegistration();
            executionCourseStudentNumbers.add(registration.getNumber());
        }

        List attendsElements = new ArrayList();
        attendsElements.addAll(grouping.getAttends());
        Iterator iterator = attendsElements.iterator();
        while (iterator.hasNext()) {            
            Attends attend = (Attends) iterator.next();            
            if (executionCourseStudentNumbers.contains(attend.getRegistration().getNumber())) {
                boolean found = false;
                Iterator iterStudentsGroups = grouping.getStudentGroups().iterator();
                while (iterStudentsGroups.hasNext() && !found) {
                    final StudentGroup studentGroup = (StudentGroup) iterStudentsGroups.next();
                    if (studentGroup != null) {
                        studentGroup.removeAttends(attend);                                               
                        found = true;
                    }
                }
                grouping.removeAttends(attend);
            }
        }
        return true;
    }
}