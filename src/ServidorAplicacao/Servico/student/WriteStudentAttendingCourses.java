package ServidorAplicacao.Servico.student;


import DataBeans.InfoExecutionCourse;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.Frequenta;
import Dominio.IDisciplinaExecucao;
import Dominio.IFrequenta;
import Dominio.IStudent;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IFrequentaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import java.util.Iterator;
import java.util.List;


/**
 * Describe class <code>WriteStudentAttendingCourses</code> here.
 *
 * @author <a href="mailto:tdi-dev@tagus.ist.utl.pt">tdi-dev:Edgar Gonçalves</a>
 * @version 1.0
 */
public class WriteStudentAttendingCourses implements IServico {

  private static WriteStudentAttendingCourses _servico = new WriteStudentAttendingCourses();

  /**
   * The actor of this class.
   **/

  private WriteStudentAttendingCourses() {

  }

  /**
   * Returns Service Name
   */
  public String getNome() {
    return "WriteStudentAttendingCourses";
  }

  /**
   * Returns the _servico.
   * @return WriteStudentAttendingCourses
   */
  public static WriteStudentAttendingCourses getService() {
    return _servico;
  }

  public Boolean run(InfoStudent infoStudent,
		    List infoExecutionCourses) 
      throws FenixServiceException{



    boolean result = false;
    try {
      List executionCourseList = null;
      

      ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		
      //Reads the student from the database
      IStudent student = sp.getIPersistentStudent().readByNumero(infoStudent.getNumber(), 
								 infoStudent.getDegreeType());

      IFrequentaPersistente attendsDAO = sp.getIFrequentaPersistente();
      
      //Read every course the student attends to, to delete them...
      List attendingCourses = attendsDAO.readByStudentId(student.getNumber());
      Iterator i = attendingCourses.iterator();
      while ( i.hasNext()) {
	attendsDAO.delete((IFrequenta) i.next());
      } // end of while ()
      


      IDisciplinaExecucaoPersistente executionCourseDAO = sp.getIDisciplinaExecucaoPersistente();
      
      i = infoExecutionCourses.iterator();
      while ( i.hasNext()) {
	InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) i.next();
	IDisciplinaExecucao executionCourse = Cloner.copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
	
	IDisciplinaExecucao executionCourseFromDB = executionCourseDAO
	  .readByExecutionCourseInitialsAndExecutionPeriod(executionCourse.getSigla(), 
							   executionCourse.getExecutionPeriod());

	IFrequenta attendsEntry = new Frequenta(student, executionCourseFromDB);
	attendsDAO.lockWrite(attendsEntry);
      } // end of while ()

      result = true;

    } catch (ExcepcaoPersistencia e) {
      
      e.printStackTrace();
      throw new FenixServiceException();
    }

    return new Boolean(result);

  }

}
