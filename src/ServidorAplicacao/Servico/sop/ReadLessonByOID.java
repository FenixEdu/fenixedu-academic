/*
 * Created on 2003/07/30
 *
 *
 */
package ServidorAplicacao.Servico.sop;

import DataBeans.InfoLesson;
import DataBeans.util.Cloner;
import Dominio.Aula;
import Dominio.IAula;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 * 
 */
public class ReadLessonByOID implements IServico {

	private static ReadLessonByOID service = new ReadLessonByOID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadLessonByOID getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadLessonByOID";
	}

	public InfoLesson run(Integer oid) throws FenixServiceException {

		InfoLesson result = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IAulaPersistente lessonDAO = sp.getIAulaPersistente();
			IAula lesson = (IAula) lessonDAO.readByOID(Aula.class, oid);
			if (lesson != null) {
				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);
//				ITurno shift = lesson.getShift();
//				InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
//				infoLesson.setInfoShift(infoShift);
				
				result = infoLesson;
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
