/*
 * Created on 04/Sep/2004
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Dominio.AttendsSet;
import Dominio.ExecutionCourse;
import Dominio.IAttendInAttendsSet;
import Dominio.IAttendsSet;
import Dominio.IExecutionCourse;
import Dominio.IAttends;
import Dominio.IStudent;
import Dominio.IStudentGroup;
import Dominio.IStudentGroupAttend;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidSituationServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.IPersistentAttendInAttendsSet;
import ServidorPersistente.IPersistentAttendsSet;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentStudentGroupAttend;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author joaosa & rmalo
 *
 */

public class DeleteAttendsSetMembersByExecutionCourseID implements IServico {

    private static  DeleteAttendsSetMembersByExecutionCourseID service = new DeleteAttendsSetMembersByExecutionCourseID();

    /**
     * The singleton access method of this class.
     */
    public static DeleteAttendsSetMembersByExecutionCourseID getService() {
        return service;
    }

    /**
     * The constructor of this class.
     */
    private DeleteAttendsSetMembersByExecutionCourseID() {
    }

    /**
     * The name of the service
     */
    public final String getNome() {
        return "DeleteAttendsSetMembersByExecutionCourseID";
    }

    /**
     * Executes the service.
     */

    public boolean run(Integer executionCourseCode, Integer attendsSetCode) throws FenixServiceException {

        
        IFrequentaPersistente persistentAttend = null;
        IPersistentAttendsSet persistentAttendsSet = null;
        IPersistentAttendInAttendsSet persistentAttendInAttendsSet = null;
        IPersistentStudentGroupAttend persistentStudentGroupAttend = null;
        IPersistentExecutionCourse persistentExecutionCourse = null;
        

        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            persistentAttend = persistentSupport.getIFrequentaPersistente();
            persistentAttendsSet = persistentSupport.getIPersistentAttendsSet();
            persistentAttendInAttendsSet = persistentSupport.getIPersistentAttendInAttendsSet();
            persistentStudentGroupAttend = persistentSupport.getIPersistentStudentGroupAttend();
            persistentExecutionCourse = persistentSupport.getIPersistentExecutionCourse();
            IAttendsSet attendsSet = (IAttendsSet) persistentAttendsSet
										.readByOID(AttendsSet.class, attendsSetCode);
            
            IExecutionCourse executionCourse = (IExecutionCourse)persistentExecutionCourse
                                        .readByOID(ExecutionCourse.class, executionCourseCode);

            if (attendsSet == null) {
                throw new ExistingServiceException();
            }

            if (executionCourse == null) {
                throw new InvalidSituationServiceException();
            }
           
            
            List executionCourseStudentNumbers = new ArrayList();
            List studentsInExecutionCourse = executionCourse.getAttendingStudents(); 
            Iterator iterStudentsInExecutionCourse = studentsInExecutionCourse.iterator();
            while(iterStudentsInExecutionCourse.hasNext()){
            	IStudent student = (IStudent)iterStudentsInExecutionCourse.next();
            	executionCourseStudentNumbers.add(student.getNumber());
            }
            
            List attendsSetElements = new ArrayList();
            attendsSetElements.addAll(attendsSet.getAttendInAttendsSet());
            Iterator iterator = attendsSetElements.iterator();
            while (iterator.hasNext()) {
            	IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterator.next();
            	IAttends frequenta = (IAttends)attendInAttendsSet.getAttend();
            	if(executionCourseStudentNumbers.contains(frequenta.getAluno().getNumber())){
            		boolean found = false;
                    Iterator iterStudentsGroups = attendsSet.getStudentGroups().iterator();
                    while (iterStudentsGroups.hasNext() && !found) {
                            
                    	IStudentGroupAttend oldStudentGroupAttend = persistentStudentGroupAttend
    														.readBy((IStudentGroup)iterStudentsGroups.next(), frequenta);
                    	if (oldStudentGroupAttend != null) {
                    		persistentStudentGroupAttend.delete(oldStudentGroupAttend);
                    		found = true;
                    	} 
                    }            
                    frequenta.removeAttendInAttendsSet(attendInAttendsSet);
            		attendsSet.removeAttendInAttendsSet(attendInAttendsSet);
            		persistentAttendInAttendsSet.delete(attendInAttendsSet);
            	}
            }            
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}