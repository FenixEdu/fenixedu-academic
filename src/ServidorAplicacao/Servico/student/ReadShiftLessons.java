/*
 * ReadShiftEnrolment.java
 *
 * Created on December 20th, 2002, 03:39
 */

package ServidorAplicacao.Servico.student;

/**
 * Service ReadShiftSignup
 *
 * @author tfc130
 **/
import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.ITurno;
import ServidorAplicacao.IServico;

public class ReadShiftLessons implements IServico {

	private static ReadShiftLessons _servico = new ReadShiftLessons();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadShiftLessons getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private ReadShiftLessons() {
	}

	/**
	 * Returns service name */
	public final String getNome() {
		return "ReadShiftLessons";
	}

	public Object run(InfoShift infoShift) {
		List infoLessons = new ArrayList();

//		try {
//			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			
			ITurno shift = Cloner.copyInfoShift2Shift(infoShift); 
			
			List lessons =
				shift.getAssociatedLessons();
//				sp.getITurnoAulaPersistente().readByShift(
//					shift);

			for (int i = 0; i < lessons.size(); i++) {
				IAula lesson = (IAula) lessons.get(i);

				InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(lesson);				
				infoLesson.setInfoShift(infoShift);

				infoLessons.add(infoLesson);
			}
//		} catch (ExcepcaoPersistencia ex) {
//			ex.printStackTrace();
//		}

		return infoLessons;
	}

}