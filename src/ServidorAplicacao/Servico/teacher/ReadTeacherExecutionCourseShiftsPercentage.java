/*
 * Created on 14/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoShiftPercentage;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IAula;
import Dominio.IDisciplinaExecucao;
import Dominio.ITeacherShiftPercentage;
import Dominio.ITurno;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage implements IServico {
	private static ReadTeacherExecutionCourseShiftsPercentage service = new ReadTeacherExecutionCourseShiftsPercentage();

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

	public List run(InfoTeacher infoTeacher, InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

		List infoShiftPercentageList = new ArrayList();

		try {
			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(infoExecutionCourse.getIdInternal());

			ITurno shiftExample = new Turno();
			shiftExample.setDisciplinaExecucao(executionCourse);

			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

			List executionCourseShiftsList = null;
			// cannot use mapped objects...with readByCriteria
			executionCourseShiftsList = shiftDAO.readByCriteria(shiftExample);

			Iterator iterator = executionCourseShiftsList.iterator();
			while (iterator.hasNext()) {
				ITurno shift = (ITurno) iterator.next();

				InfoShiftPercentage infoShiftPercentage = new InfoShiftPercentage();
				infoShiftPercentage.setShift(Cloner.copyIShift2InfoShift(shift));
				double availablePercentage = 100;
				InfoTeacherShiftPercentage infoTeacherShiftPercentage = null;

				Iterator iter = shift.getAssociatedTeacherProfessorShipPercentage().iterator();
				while (iter.hasNext()) {
					ITeacherShiftPercentage teacherShiftPercentage = (ITeacherShiftPercentage) iter.next();

					availablePercentage -= teacherShiftPercentage.getPercentage().doubleValue();

					infoTeacherShiftPercentage = Cloner.copyITeacherShiftPercentage2InfoTeacherShiftPercentage(teacherShiftPercentage);
					infoShiftPercentage.addInfoTeacherShiftPercentage(infoTeacherShiftPercentage);
				}

				List infoLessons = (List) CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {
					public Object transform(Object input) {
						IAula lesson = (IAula) input;
						return Cloner.copyILesson2InfoLesson(lesson);
					}
				});
				infoShiftPercentage.setInfoLessons(infoLessons);

				infoShiftPercentage.setAvailablePercentage(new Double(availablePercentage));

				infoShiftPercentageList.add(infoShiftPercentage);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace();
			throw new FenixServiceException(e);
		}
		return infoShiftPercentageList;
	}
}
