/*
 * LerAulasDeSalaEmSemestre.java
 *
 * Created on 29 de Outubro de 2002, 15:44
 */

package ServidorAplicacao.Servico.sop;

/**
 * Servi�o LerAulasDeSalaEmSemestre.
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoLesson;
import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionPeriod;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IAulaPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class LerAulasDeSalaEmSemestre implements IServico {

	private static LerAulasDeSalaEmSemestre _servico =
		new LerAulasDeSalaEmSemestre();
	/**
	 * The singleton access method of this class.
	 **/
	public static LerAulasDeSalaEmSemestre getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private LerAulasDeSalaEmSemestre() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "LerAulasDeSalaEmSemestre";
	}

	public List run(
		InfoExecutionPeriod infoExecutionPeriod,
		InfoRoom infoRoom) {
		List infoAulas = null;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			IAulaPersistente lessonDAO = sp.getIAulaPersistente();

			IExecutionPeriod executionPeriod =
				Cloner.copyInfoExecutionPeriod2IExecutionPeriod(
					infoExecutionPeriod);
			ISala room = Cloner.copyInfoRoom2Room(infoRoom);

			List lessonList =
				lessonDAO.readByRoomAndExecutionPeriod(room, executionPeriod);

			Iterator iterator = lessonList.iterator();
			infoAulas = new ArrayList();
			while (iterator.hasNext()) {
				IAula elem = (IAula) iterator.next();
				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
				infoAulas.add(infoLesson);
			}
		} catch (ExcepcaoPersistencia ex) {
			ex.printStackTrace();
		}
		return infoAulas;
	}

}