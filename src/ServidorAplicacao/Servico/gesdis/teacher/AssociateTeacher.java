package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ISite;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

 
public class AssociateTeacher implements IServico{
    
    
    private static AssociateTeacher service = new AssociateTeacher();
    
    /**
	 * The singleton access method of this class.
	 **/
    public static AssociateTeacher getService() {
		return service;
    }
    
    /**
	 * The Actor of this class.
	 **/
    private AssociateTeacher() { }
    
    /**
	 * returns the service name
	 **/
    public final String getNome() {
		return "gesdis.teacher.AssociateTeacher";
    }
    
    /**
	 * Executes the service.
	 *
	 **/
	
    public void run(InfoExecutionCourse infoExecutionCourse,Integer teacherNumber) throws FenixServiceException{
		try{
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			IPersistentSite persistentSite = sp.getIPersistentSite();
			ITeacher teacher = null;
            IDisciplinaExecucao executionCourse = null;
			ISite site=null;
			
			teacher = persistentTeacher.readTeacherByNumber(teacherNumber);		
			
			Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
			site = persistentSite.readByExecutionCourse(executionCourse);
             if(teacher == null )  
                            throw new InvalidArgumentsServiceException();
			List professorShipsExecutionCoursesList=teacher.getProfessorShipsExecutionCourses();
			if(professorShipsExecutionCoursesList.contains(executionCourse))
							throw new ExistingServiceException();
			professorShipsExecutionCoursesList.add(executionCourse);
			teacher.setProfessorShipsExecutionCourses(professorShipsExecutionCoursesList);
			
			
			persistentTeacher.lockWrite(teacher);
		}
		catch(ExcepcaoPersistencia ex){
			throw new ExistingServiceException(ex);
		}
    }
}

