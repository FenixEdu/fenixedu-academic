/*
 * Created on 08/Mars/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGrouping;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGrouping;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 * 
 */

public class InsertStudentsInGrouping implements IService {

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, String[] selected)
            throws FenixServiceException {

        IPersistentGrouping persistentGrouping = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        List students = new ArrayList();

        try {
            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            persistentGrouping = persistentSupport.getIPersistentGrouping();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();

            IGrouping groupProperties = (IGrouping) persistentGrouping.readByOID(Grouping.class,
                    groupPropertiesCode);

            if (groupProperties == null) {
                throw new ExistingServiceException();
            }

            if (selected == null)
                return Boolean.TRUE;

            List studentCodes = Arrays.asList(selected);

            Iterator iterator = studentCodes.iterator();

            while (iterator.hasNext()) {
                String number = (String) iterator.next();
                if (number.equals("Todos os Alunos")) {
                } else {
                    IStudent student = (IStudent) persistentStudent.readByOID(Student.class,
                            new Integer(number));
                    students.add(student);
                }
            }

            Iterator iterAttends = groupProperties.getAttends().iterator();

            while (iterAttends.hasNext()) {
                IAttends existingAttend = (IAttends) iterAttends.next();
                IStudent existingAttendStudent = existingAttend.getAluno();

                List studentsList = new ArrayList();
                studentsList.addAll(students);
                Iterator iteratorStudents = studentsList.iterator();

                while (iteratorStudents.hasNext()) {

                    IStudent student = (IStudent) iteratorStudents.next();
                    if (student.equals(existingAttendStudent)) {
                        throw new InvalidSituationServiceException();
                    }
                }
            }

            List studentsList1 = new ArrayList();
            studentsList1.addAll(students);
            Iterator iterStudents1 = studentsList1.iterator();

            while (iterStudents1.hasNext()) {
                IAttends attend = null;
                IStudent student = (IStudent) iterStudents1.next();

                List listaExecutionCourses = new ArrayList();
                listaExecutionCourses.addAll(groupProperties.getExecutionCourses());
                Iterator iterExecutionCourse = listaExecutionCourses.iterator();
                while (iterExecutionCourse.hasNext() && attend == null) {

                    IExecutionCourse executionCourse = (IExecutionCourse) iterExecutionCourse.next();
                    attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student.getIdInternal(),
                            executionCourse.getIdInternal());
                }
                groupProperties.addAttends(attend);
            }

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}