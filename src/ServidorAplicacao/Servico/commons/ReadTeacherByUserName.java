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

public class ReadTeacherByUserName implements IServico {
	private static ReadTeacherByUserName service = new ReadTeacherByUserName();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadTeacherByUserName getService() {
	  return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadTeacherByUserName";
	}
	
	public InfoTeacher run(String userName) {
                        
	  InfoTeacher infoTeacher = null;
	  try {
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
		IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
		
		ITeacher teacher = teacherDAO.readTeacherByUsername(userName);
		if (teacher != null) {
			infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
		}

	  } catch (ExcepcaoPersistencia ex) {
	  	throw new RuntimeException(ex);
	  }
    
	  return infoTeacher;
	}

}
