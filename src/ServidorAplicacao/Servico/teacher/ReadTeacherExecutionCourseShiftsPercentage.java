/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftPercentage;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import DataBeans.util.Cloner;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacherShiftPercentage;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage implements IServico {
	private static ReadTeacherExecutionCourseShiftsPercentage service =
		new ReadTeacherExecutionCourseShiftsPercentage();

	/**
	 * The singleton access method of this class.
	 */
	public static ReadTeacherExecutionCourseShiftsPercentage getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadTeacherExecutionCourseShiftsPercentage";
	}

	public List run(
		InfoTeacher infoTeacher,
		InfoExecutionCourse infoExecutionCourse)
		throws FenixServiceException {

		List infoShiftPercentageList = new ArrayList();

		try {
			IDisciplinaExecucao executionCourse =
				Cloner.copyInfoExecutionCourse2ExecutionCourse(
					infoExecutionCourse);
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			executionCourse =
				(IDisciplinaExecucao) executionCourseDAO.readByOId(
					executionCourse);

			ITurno shiftExample = new Turno();
			shiftExample.setDisciplinaExecucao(executionCourse);

			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

			List executionCourseShiftsList =
				shiftDAO.readByCriteria(shiftExample);

			Iterator iterator = executionCourseShiftsList.iterator();
			while (iterator.hasNext()) {
				ITurno shift = (ITurno) iterator.next();

				InfoShiftPercentage infoShiftPercentage =
					new InfoShiftPercentage();
				infoShiftPercentage.setShift(
					Cloner.copyIShift2InfoShift(shift));

				double availablePercentage = 100;
				InfoTeacherShiftPercentage infoTeacherShiftPercentage = null;
				Iterator iter =
					shift
						.getAssociatedTeacherProfessorShipPercentage()
						.iterator();
				while (iter.hasNext()) {
					ITeacherShiftPercentage teacherShiftPercentage =
						(ITeacherShiftPercentage) iter.next();

					availablePercentage
						-= teacherShiftPercentage.getPercentage().doubleValue();

					infoTeacherShiftPercentage =
						Cloner
							.copyITeacherShiftPercentage2InfoTeacherShiftPercentage(
							teacherShiftPercentage);
					infoShiftPercentage.addInfoTeacherShiftPercentage(
						infoTeacherShiftPercentage);
				}

				infoShiftPercentage.setAvailablePercentage(
					new Double(availablePercentage));

				infoShiftPercentageList.add(infoShiftPercentage);
			}

		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return infoShiftPercentageList;
	}
}
