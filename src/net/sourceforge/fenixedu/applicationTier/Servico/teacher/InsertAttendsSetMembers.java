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
import net.sourceforge.fenixedu.domain.AttendInAttendsSet;
import net.sourceforge.fenixedu.domain.GroupProperties;
import net.sourceforge.fenixedu.domain.IAttendInAttendsSet;
import net.sourceforge.fenixedu.domain.IAttends;
import net.sourceforge.fenixedu.domain.IAttendsSet;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IGroupProperties;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IFrequentaPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentAttendInAttendsSet;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGroupProperties;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author joaosa & rmalo
 *
 */

public class InsertAttendsSetMembers implements IService {

    /**
     * The constructor of this class.
     */
    public InsertAttendsSetMembers() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode,
            List studentCodes) throws FenixServiceException {

        IPersistentGroupProperties persistentGroupProperties = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        List students = new ArrayList();
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperties = persistentSupport
                    .getIPersistentGroupProperties();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();

            
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            
            if (groupProperties == null) {
                throw new ExistingServiceException();
            }

            
            Iterator iterator = studentCodes.iterator();

            
            while (iterator.hasNext()) {
                IStudent student = (IStudent) persistentStudent.readByOID(
                        Student.class, (Integer) iterator.next());
                students.add(student);
            }
            
            
            
            
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            
            List attends = new ArrayList();
            attends.addAll(attendsSet.getAttends());
            Iterator iterAttends = attends.iterator();

            
            
            while (iterAttends.hasNext()) {
                IAttends existingAttend = (IAttends) iterAttends
                        .next();
                IStudent existingAttendStudent = existingAttend.getAluno();
                
                List studentsList = new ArrayList();
                studentsList.addAll(students);
                Iterator iteratorStudents = studentsList.iterator();
                
                while (iteratorStudents.hasNext()) {
                	
            
                	
                    IStudent student = (IStudent)iteratorStudents.next();
                    if(student.equals(existingAttendStudent)){
                    	throw new InvalidSituationServiceException();
                    }
                }
            }

            
            
            List studentsList1 = new ArrayList();
            studentsList1.addAll(students);
            Iterator iterStudents1 = studentsList1.iterator();
            
            while (iterStudents1.hasNext()) {
            	IAttends attend=null;
                IStudent student = (IStudent)iterStudents1.next();
            
                List listaExecutionCourses = new ArrayList();
                listaExecutionCourses.addAll(groupProperties.getExecutionCourses());
                Iterator iterExecutionCourse = listaExecutionCourses.iterator();
                while (iterExecutionCourse.hasNext() && attend==null) {
            
                	IExecutionCourse executionCourse = (IExecutionCourse)iterExecutionCourse.next();
                	attend = persistentAttend.readByAlunoAndDisciplinaExecucao(student,executionCourse);
                }
                
				IAttendInAttendsSet attendInAttendsSet = new AttendInAttendsSet(attend,attendsSet);
                persistentAttendInAttendsSet.simpleLockWrite(attendInAttendsSet);
                attendsSet.addAttendInAttendsSet(attendInAttendsSet);
                attend.addAttendInAttendsSet(attendInAttendsSet);
            }
            

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }

        return new Boolean(true);
    }
}