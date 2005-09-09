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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class DeleteGroupingMembersByExecutionCourseID implements IService {

    public boolean run(Integer executionCourseCode, Integer groupingCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentGrouping persistentGrouping = persistentSupport.getIPersistentGrouping();
        IPersistentExecutionCourse persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
        IPersistentStudentGroup persistentStudentGroup = persistentSupport.getIPersistentStudentGroup();
        IGrouping grouping = (IGrouping) persistentGrouping.readByOID(Grouping.class, groupingCode);
        
        if (grouping == null) {
            throw new ExistingServiceException();
        }

        IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseCode);
       
        if (executionCourse == null) {
            throw new InvalidSituationServiceException();
        }

        List executionCourseStudentNumbers = new ArrayList();
        final List<IAttends> attends = executionCourse.getAttends();
        for (final IAttends attend : attends) {
            final IStudent student = attend.getAluno();
            executionCourseStudentNumbers.add(student.getNumber());
        }

        List attendsElements = new ArrayList();
        attendsElements.addAll(grouping.getAttends());
        Iterator iterator = attendsElements.iterator();
        while (iterator.hasNext()) {            
            IAttends attend = (IAttends) iterator.next();            
            if (executionCourseStudentNumbers.contains(attend.getAluno().getNumber())) {
                boolean found = false;
                Iterator iterStudentsGroups = grouping.getStudentGroups().iterator();
                while (iterStudentsGroups.hasNext() && !found) {
                    final IStudentGroup studentGroup = (IStudentGroup) iterStudentsGroups.next();
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