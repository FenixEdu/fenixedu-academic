/*
 * Created on 20/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.ITeacher;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jmota
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ReadTeacherByUsername implements IServico {
	private static ReadTeacherByUsername service = new ReadTeacherByUsername();
	/**
	 * 
	 */
	public ReadTeacherByUsername() {
	}
	/**
		 * The singleton access method of this class.
		 **/

	public static ReadTeacherByUsername getService() {

		return service;

	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {

		return "ReadTeacherByUsername";
	}
	/**
		 * Executes the service. Returns the current collection of
		 * sitios names.
		 *
		 * @throws ExcepcaoInexistente is there is none sitio.
		 **/

	public InfoTeacher run(String username) throws FenixServiceException {
		ITeacher teacher = null;
		InfoTeacher infoTeacher=null;
		try {

			ISuportePersistente sp;

			sp = SuportePersistenteOJB.getInstance();
			IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
			teacher=persistentTeacher.readTeacherByUsername(username);
			if (teacher!=null) {
				infoTeacher=Cloner.copyITeacher2InfoTeacher(teacher);
		} 
	
		} catch (ExcepcaoPersistencia e) {
			throw new FenixServiceException(e);
		}
		
		return infoTeacher;
	}
}
