package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.util.Cloner;
import Dominio.IAula;
import Dominio.IExecutionCourse;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 *
 */
public class SelectExecutionShiftsWithAssociatedLessonsAndClasses
	implements IServico {

	private static SelectExecutionShiftsWithAssociatedLessonsAndClasses _servico =
		new SelectExecutionShiftsWithAssociatedLessonsAndClasses();

	/**
	  * The actor of this class.
	  **/

	private SelectExecutionShiftsWithAssociatedLessonsAndClasses() {

	}

	/**
	 * Returns Service Name
	 */
	public String getNome() {
		return "SelectExecutionShiftsWithAssociatedLessonsAndClasses";
	}

	/**
	 * Returns the _servico.
	 * @return SelectShifts
	 */
	public static SelectExecutionShiftsWithAssociatedLessonsAndClasses getService() {
		return _servico;
	}

	public Object run(InfoExecutionCourse infoExecutionCourse) {

		List shiftsWithAssociatedClassesAndLessons = new ArrayList();

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IExecutionCourse disciplinaExecucao =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoExecutionCourse);
			List shifts =
				sp.getITurnoPersistente().readByExecutionCourse(
					disciplinaExecucao);

			if (shifts == null || shifts.isEmpty()) {
				
			} else {
				

				for (int i = 0; i < shifts.size(); i++) {
					ITurno shift = (ITurno) shifts.get(i);
					InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons =
						new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
							Cloner.copyShift2InfoShift(shift),
							null,
							null);
	
					List lessons = sp.getITurnoAulaPersistente().readByShift(shift);
					List infoLessons = new ArrayList();
					List classesShifts =
						sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
					List infoClasses = new ArrayList();
	
					for (int j = 0; j < lessons.size(); j++)
						infoLessons.add(
							Cloner.copyILesson2InfoLesson((IAula) lessons.get(j)));
	
					shiftWithAssociatedClassesAndLessons.setInfoLessons(
						infoLessons);
	
					for (int j = 0; j < classesShifts.size(); j++)
						infoClasses.add(
							Cloner.copyClass2InfoClass(
								((ITurmaTurno) classesShifts.get(j)).getTurma()));
	
					shiftWithAssociatedClassesAndLessons.setInfoClasses(
						infoClasses);
					
					shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
				}
			}

		} catch (ExcepcaoPersistencia e) {
		}
		return shiftsWithAssociatedClassesAndLessons;
	}

}