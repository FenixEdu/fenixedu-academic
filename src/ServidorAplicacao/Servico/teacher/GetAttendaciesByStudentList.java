/*
 * Created on 16/Set/2003, 12:18:28 By Goncalo Luiz gedl [AT] rnl [DOT] ist
 * [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoAttendWithEnrollment;
import DataBeans.InfoAttendsSummary;
import DataBeans.InfoFrequenta;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.IFrequenta;
import Dominio.IStudent;
import Dominio.Student;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentEnrolment;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt Created at
 *         16/Set/2003, 12:18:28
 */
public class GetAttendaciesByStudentList implements IService
{

    public GetAttendaciesByStudentList()
    {
    }

    public InfoAttendsSummary run(Integer executionCourseID, List infoStudents)
            throws BDException
    {
        List attendacies = new LinkedList();
        InfoAttendsSummary attendsSummary = new InfoAttendsSummary();
        List keys = new ArrayList();
        Map enrollmentDsitribution = new HashMap();
        try
        {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB
                    .getInstance();
            IFrequentaPersistente persistentAttendacy = persistenceSupport
                    .getIFrequentaPersistente();
            IPersistentStudent persistentStudent = persistenceSupport
                    .getIPersistentStudent();
            IPersistentEnrolment persistentEnrolment = persistenceSupport
                    .getIPersistentEnrolment();
            List students = new LinkedList();
            for (Iterator infoStudentsIterator = infoStudents.iterator(); infoStudentsIterator
                    .hasNext(); )
            {
                InfoStudent infoStudent = (InfoStudent) infoStudentsIterator
                        .next();
                IStudent student = new Student(infoStudent.getIdInternal());
                student = (IStudent) persistentStudent
                        .readByOId(student, false);
                if (student != null)
                {
                    students.add(student);
                }

            }

            for (Iterator studentsIterator = students.iterator(); studentsIterator
                    .hasNext(); )
            {
                IStudent student = (IStudent) studentsIterator.next();
                IFrequenta attendacy = persistentAttendacy
                        .readByAlunoIdAndDisciplinaExecucaoId(student
                                .getIdInternal(), executionCourseID);

                Integer enrollments = new Integer(0);
                if (attendacy.getEnrolment() != null)
                {
                    List enrollmentList = persistentEnrolment
                            .readEnrollmentsByStudentAndCurricularCourseNameAndCode(
                                    attendacy.getEnrolment()
                                            .getStudentCurricularPlan()
                                            .getStudent(), attendacy
                                            .getEnrolment()
                                            .getCurricularCourse());
                    enrollments = new Integer(enrollmentList.size());
                    if (keys.contains(enrollments))
                    {
                        enrollmentDsitribution.put(enrollments, new Integer(
                                ((Integer) enrollmentDsitribution
                                        .get(enrollments)).intValue() + 1));
                    }
                    else
                    {
                        keys.add(enrollments);
                        enrollmentDsitribution.put(enrollments, new Integer(1));
                    }

                }

                InfoFrequenta infoFrequenta = Cloner
                        .copyIFrequenta2InfoFrequenta(attendacy);
                InfoAttendWithEnrollment infoAttendWithEnrollment = new InfoAttendWithEnrollment();
                infoAttendWithEnrollment.setAluno(infoFrequenta.getAluno());
                infoAttendWithEnrollment.setDisciplinaExecucao(infoFrequenta
                        .getDisciplinaExecucao());
                infoAttendWithEnrollment.setIdInternal(infoFrequenta
                        .getIdInternal());
                infoAttendWithEnrollment.setEnrollments(enrollments);
                infoAttendWithEnrollment.setInfoEnrolment(infoFrequenta
                        .getInfoEnrolment());
                attendacies.add(infoAttendWithEnrollment);

            }
            attendsSummary.setAttends(attendacies);
            attendsSummary.setEnrollmentDistribution(enrollmentDsitribution);
            Collections.sort(keys);
            attendsSummary.setNumberOfEnrollments(keys);
            
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new BDException(
                    "Got an error while trying to get info about a student's work group",
                    ex);
        }
        return attendsSummary;
    }
}
