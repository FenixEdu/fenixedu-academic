package ServidorAplicacao.Servico.commons;

import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author jpvl
 */

public class ReadTeacherByNumber implements IServico {
	private static ReadTeacherByNumber service = new ReadTeacherByNumber();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadTeacherByNumber getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadTeacherByNumber";
	}
	
	public InfoTeacher run(Integer teacherNumber) {
                        
	  InfoTeacher infoTeacher = null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
		
		ITeacher teacher = teacherDAO.readByNumber(teacherNumber);
		if (teacher != null) {
			infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
		}

	  } catch (ExcepcaoPersistencia ex) {
	  	throw new RuntimeException(ex);
	  }
    
	  return infoTeacher;
	}

}
