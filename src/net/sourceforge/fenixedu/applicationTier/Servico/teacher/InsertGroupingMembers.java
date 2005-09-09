/*
 * Created on 24/Ago/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
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

public class InsertGroupingMembers implements IService {

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, List studentCodes)
            throws FenixServiceException {

        IPersistentGrouping persistentGroupProperties = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;

        List students = new ArrayList();

        try {

            ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();

            persistentGroupProperties = persistentSupport.getIPersistentGrouping();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();

            IGrouping groupProperties = (IGrouping) persistentGroupProperties.readByOID(Grouping.class,
                    groupPropertiesCode);

            if (groupProperties == null) {
                throw new ExistingServiceException();
            }

            Iterator iterator = studentCodes.iterator();

            while (iterator.hasNext()) {
                IStudent student = (IStudent) persistentStudent.readByOID(Student.class,
                        (Integer) iterator.next());
                students.add(student);
            }

            Iterator iterAttends = groupProperties.getAttends().iterator();

            while (iterAttends.hasNext()) {
                IAttends existingAttend = (IAttends) iterAttends.next();
                IStudent existingAttendStudent = existingAttend.getAluno();

                Iterator iteratorStudents = students.iterator();

                while (iteratorStudents.hasNext()) {

                    IStudent student = (IStudent) iteratorStudents.next();
                    if (student.equals(existingAttendStudent)) {
                        throw new InvalidSituationServiceException();
                    }
                }
            }

            Iterator iterStudents1 = students.iterator();

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

        return Boolean.TRUE;
    }
}