/*
 * Created on 08/Mars/2005
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.AttendInAttendsSet;
import Dominio.GroupProperties;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import Dominio.IGroupProperties;
import Dominio.IStudent;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class InsertStudentsInAttendsSet implements IService {

    /**
     * The constructor of this class.
     */
    public InsertStudentsInAttendsSet() {
    }

    /**
     * Executes the service.
     */

    public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode,
            String[] selected) throws FenixServiceException {

        IPersistentGroupProperties persistentGroupProperties = null;
        IFrequentaPersistente persistentAttend = null;
        IPersistentStudent persistentStudent = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        List students = new ArrayList();
        
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentGroupProperties = persistentSupport
                    .getIPersistentGroupProperties();
            persistentStudent = persistentSupport.getIPersistentStudent();
            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();

            
            
            IGroupProperties groupProperties = (IGroupProperties) persistentGroupProperties
                    .readByOID(GroupProperties.class, groupPropertiesCode);

            
            if (groupProperties == null) {
                throw new ExistingServiceException();
            }
            
            if (selected==null) return new Boolean(true);
            
            List studentCodes = Arrays.asList(selected);
            
            Iterator iterator = studentCodes.iterator();
            
            while (iterator.hasNext()) {
                String number = (String)iterator.next();
                if (number.equals("Todos os Alunos")) {
                }
                else{
                IStudent student = (IStudent) persistentStudent.readByOID(
                        Student.class, new Integer (number));
                students.add(student);
                }
            }
            
            
            IAttendsSet attendsSet = groupProperties.getAttendsSet();
            
            List attends = new ArrayList();
            attends.addAll(attendsSet.getAttends());
            Iterator iterAttends = attends.iterator();

            
            
            while (iterAttends.hasNext()) {
                IFrequenta existingAttend = (IFrequenta) iterAttends
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
            	IFrequenta attend=null;
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