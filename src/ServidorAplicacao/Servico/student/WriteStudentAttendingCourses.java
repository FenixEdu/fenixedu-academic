package ServidorAplicacao.Servico.student;


import DataBeans.InfoExecutionCourse;
import DataBeans.InfoStudent;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
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

  /**
   * Describe <code>run</code> method here.
   *
   * @param infoStudent an <code>InfoStudent</code>,
   * @param infoExecutionCourses a <code>List</code> with the wanted executionCourse.idInternal's of the ATTEND table.
   * @return a <code>Boolean</code> to indicate if all went fine.
   * @exception FenixServiceException if an error occurs.
   */
  public Boolean run(InfoStudent infoStudent,
		     List infoExecutionCourses) 
    throws FenixServiceException{


    if ( infoExecutionCourses== null ||  infoExecutionCourses.size() == 0 ||infoStudent == null) {
      return new Boolean(false);
    } // end of if ()


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
	Integer executionCourseId = new Integer((String) i.next()); 
	IDisciplinaExecucao executionCourse = new DisciplinaExecucao(executionCourseId);
	executionCourse = (IDisciplinaExecucao) executionCourseDAO.readByOId(executionCourse, false);

	if ( executionCourse == null) {
	  System.out.println("Execution course with ID=" + executionCourseId + " does not exist in the database!");
	  throw new FenixServiceException();
	} // end of if ()
	else {
	  

	
	
	  IDisciplinaExecucao executionCourseFromDB = executionCourseDAO
	    .readByExecutionCourseInitialsAndExecutionPeriod(executionCourse.getSigla(), 
							     executionCourse.getExecutionPeriod());

	  IFrequenta attendsEntry = new Frequenta();
	  //FIXME: (tdi-dev:edgar.goncalves) - lockWrite ain't working...
	  attendsDAO.lockWrite(attendsEntry);
	  attendsEntry.setAluno(student);
	  attendsEntry.setDisciplinaExecucao(executionCourseFromDB);
	  
	} // end of if () else
	

      } // end of while ()

      result = true;

    } catch (ExcepcaoPersistencia e) {
      
      e.printStackTrace();
      throw new FenixServiceException();
    }

    return new Boolean(result);

  }

}
