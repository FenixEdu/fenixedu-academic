/*
 * Created on 19/Mai/2003 by jpvl
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoTeacher;
import DataBeans.teacher.credits.InfoTeacherShiftPercentage;
import DataBeans.util.Cloner;
import Dominio.DisciplinaExecucao;
import Dominio.IDisciplinaExecucao;
import Dominio.IDomainObject;
import Dominio.IProfessorship;
import Dominio.ITeacher;
import Dominio.ITeacherShiftPercentage;
import Dominio.ITurno;
import Dominio.TeacherShiftPercentage;
import Dominio.Turno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.teacher.credits.ShiftPercentageExceededException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IDisciplinaExecucaoPersistente;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.IPersistentTeacherShiftPercentage;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author jpvl
 */
public class AcceptTeacherExecutionCourseShiftPercentage implements IServico {
	private static AcceptTeacherExecutionCourseShiftPercentage service =
		new AcceptTeacherExecutionCourseShiftPercentage();

	/**
	 * The singleton access method of this class.
	 */
	public static AcceptTeacherExecutionCourseShiftPercentage getService() {
		return service;
	}

	/* (non-Javadoc)
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "AcceptTeacherExecutionCourseShiftPercentage";
	}

	/**
	 *  
	 * @param infoTeacherShiftPercentageList list of shifts and percentages that teacher needs...
	 * @return 
	 * @throws FenixServiceException
	 */
	public Boolean run(
		InfoTeacher infoTeacher,
		InfoExecutionCourse infoExecutionCourse,
		List infoTeacherShiftPercentageList)
		throws FenixServiceException {

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();

			ITurnoPersistente shiftDAO = sp.getITurnoPersistente();

			IPersistentTeacherShiftPercentage teacherShiftPercentageDAO =
				sp.getIPersistentTeacherShiftPercentage();
			IPersistentTeacher teacherDAO = sp.getIPersistentTeacher();
			IDisciplinaExecucaoPersistente executionCourseDAO =
				sp.getIDisciplinaExecucaoPersistente();

			IPersistentProfessorship professorshipDAO =
				sp.getIPersistentProfessorship();

			IDisciplinaExecucao executionCourse = new DisciplinaExecucao();
			executionCourse.setIdInternal(infoExecutionCourse.getIdInternal());

			executionCourse =
				(IDisciplinaExecucao) executionCourseDAO.readByOId(
					executionCourse);

			ITeacher teacherParam = Cloner.copyInfoTeacher2Teacher(infoTeacher);

			ITeacher teacher = (ITeacher) teacherDAO.readByOId(teacherParam);

			IProfessorship professorship =
				professorshipDAO.readByTeacherAndExecutionCourse(
					teacher,
					executionCourse);

			List teacherShiftPercentageAdded = new ArrayList();
			List shiftWithErrors = new ArrayList();
			Iterator iterator = infoTeacherShiftPercentageList.iterator();
			while (iterator.hasNext()) {
				InfoTeacherShiftPercentage infoTeacherShiftPercentage =
					(InfoTeacherShiftPercentage) iterator.next();

				ITurno shift = getIShift(shiftDAO, infoTeacherShiftPercentage);

				ITeacherShiftPercentage teacherShiftPercentage =
					new TeacherShiftPercentage();

				teacherShiftPercentage.setProfessorShip(professorship);
				teacherShiftPercentage.setShift(shift);
				teacherShiftPercentage.setPercentage(
					infoTeacherShiftPercentage.getPercentage());

				boolean ok =
					validate(
						shift,
						infoTeacherShiftPercentage.getPercentage(),
						teacher);

				if (ok) {
					lockPercentages(
						teacherShiftPercentageDAO,
						teacherShiftPercentageAdded,
						shift,
						teacherShiftPercentage);
				} else {
					shiftWithErrors.add(Cloner.copyIShift2InfoShift(shift));
				}
			}
			List toRemoveList =
				(List) CollectionUtils.subtract(
					professorship.getAssociatedTeacherShiftPercentage(),
					teacherShiftPercentageAdded);

			Iterator iterator2 = toRemoveList.iterator();

			while (iterator2.hasNext()) {
				ITeacherShiftPercentage teacherShiftPercentage =
					(ITeacherShiftPercentage) iterator2.next();
				professorship.getAssociatedTeacherShiftPercentage().remove(
					teacherShiftPercentage);

				teacherShiftPercentageDAO.delete(teacherShiftPercentage);
			}
			if (!shiftWithErrors.isEmpty()) {
				// this is done this way but we have to have a more general solution when exceptions are thrown by service...
				sp.confirmarTransaccao();
				throw new ShiftPercentageExceededException(shiftWithErrors);
			}
		} catch (ExcepcaoPersistencia e) {
			e.printStackTrace(System.out);
			throw new FenixServiceException(e);
		}

		return Boolean.TRUE;
	}
	private ITurno getIShift(
		ITurnoPersistente shiftDAO,
		InfoTeacherShiftPercentage infoTeacherShiftPercentage)
		throws ExcepcaoPersistencia {
		ITurno shiftParam = new Turno();
		shiftParam.setIdInternal(
			infoTeacherShiftPercentage.getInfoShift().getIdInternal());
		ITurno shift = (ITurno) shiftDAO.readByOId((IDomainObject) shiftParam);
		return shift;
	}
	private void lockPercentages(
		IPersistentTeacherShiftPercentage teacherShiftPercentageDAO,
		List teacherShiftPercentageAdded,
		ITurno shift,
		ITeacherShiftPercentage teacherShiftPercentage)
		throws ExcepcaoPersistencia {
		List associatedTeacherProfessorShipPercentage =
			shift.getAssociatedTeacherProfessorShipPercentage();
		int indexOf =
			associatedTeacherProfessorShipPercentage.indexOf(
				teacherShiftPercentage);
		ITeacherShiftPercentage teacherShiftPercentageToWrite = null;
		if (indexOf != -1) {
			teacherShiftPercentageToWrite =
				(
					ITeacherShiftPercentage) associatedTeacherProfessorShipPercentage
						.get(
					indexOf);
		} else {
			teacherShiftPercentageToWrite = teacherShiftPercentage;
		}
		teacherShiftPercentageDAO.lockWrite(teacherShiftPercentageToWrite);
		teacherShiftPercentageToWrite.setPercentage(
			teacherShiftPercentage.getPercentage());
		teacherShiftPercentageAdded.add(teacherShiftPercentageToWrite);
	}

	/**
	 * @param shift
	 * @param teacherShiftPercentage
	 * @return
	 */
	private boolean validate(
		ITurno shift,
		Double percentage,
		ITeacher teacher) {
		List teacherProfessorShipPercentageList =
			shift.getAssociatedTeacherProfessorShipPercentage();

		Iterator iterator = teacherProfessorShipPercentageList.iterator();
		double percentageAvailable = 100;
		while (iterator.hasNext()) {
			ITeacherShiftPercentage teacherShiftPercentageBD =
				(ITeacherShiftPercentage) iterator.next();

			if (!teacherShiftPercentageBD
				.getProfessorShip()
				.getTeacher()
				.equals(teacher)) {
				percentageAvailable
					-= teacherShiftPercentageBD.getPercentage().doubleValue();
			}
		}

		if (percentageAvailable < percentage.doubleValue()) {
			// 100% exceeded
			return false;
		}
		return true;
	}
}
