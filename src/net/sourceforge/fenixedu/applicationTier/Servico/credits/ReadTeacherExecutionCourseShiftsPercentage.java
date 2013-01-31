/*
 * Created on 14/Mai/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.credits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftPercentage;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.credits.InfoShiftProfessorshipAndTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.TeacherExecutionCourseProfessorshipShiftsDTO;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftProfessorship;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.ist.fenixWebFramework.services.Service;

/**
 * @author T�nia & Alexandra
 */
public class ReadTeacherExecutionCourseShiftsPercentage extends FenixService {

	@Service
	public static TeacherExecutionCourseProfessorshipShiftsDTO run(InfoTeacher infoTeacher,
			InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {

		TeacherExecutionCourseProfessorshipShiftsDTO result = new TeacherExecutionCourseProfessorshipShiftsDTO();

		List<InfoShiftPercentage> infoShiftPercentageList = new ArrayList<InfoShiftPercentage>();

		ExecutionCourse executionCourse = readExecutionCourse(infoExecutionCourse);
		Teacher teacher = readTeacher(infoTeacher);

		result.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
		result.setInfoTeacher(InfoTeacher.newInfoFromDomain(teacher));

		Set<Shift> executionCourseShiftsList = executionCourse.getAssociatedShifts();

		Iterator<Shift> iterator = executionCourseShiftsList.iterator();
		while (iterator.hasNext()) {

			Shift shift = iterator.next();
			InfoShiftPercentage infoShiftPercentage = new InfoShiftPercentage();
			final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
			infoShiftPercentage.setShift(infoShift);
			double availablePercentage = 100;
			InfoShiftProfessorship infoShiftProfessorship = null;

			Iterator<ShiftProfessorship> iter = shift.getAssociatedShiftProfessorship().iterator();
			while (iter.hasNext()) {
				ShiftProfessorship shiftProfessorship = iter.next();
				/**
				 * if shift's type is LABORATORIAL the shift professorship
				 * percentage can exceed 100%
				 */
				if ((shift.getCourseLoadsCount() != 1 || !shift.containsType(ShiftType.LABORATORIAL))
						&& shiftProfessorship.getProfessorship().getTeacher() != teacher) {
					availablePercentage -= shiftProfessorship.getPercentage().doubleValue();
				}
				infoShiftProfessorship = InfoShiftProfessorshipAndTeacher.newInfoFromDomain(shiftProfessorship);
				infoShiftPercentage.addInfoShiftProfessorship(infoShiftProfessorship);
			}

			List<InfoLesson> infoLessons =
					(List<InfoLesson>) CollectionUtils.collect(shift.getAssociatedLessons(), new Transformer() {
						@Override
						public Object transform(Object input) {
							Lesson lesson = (Lesson) input;
							return InfoLesson.newInfoFromDomain(lesson);
						}
					});

			infoShiftPercentage.setInfoLessons(infoLessons);
			infoShiftPercentage.setAvailablePercentage(Double.valueOf(availablePercentage));
			infoShiftPercentageList.add(infoShiftPercentage);
		}

		result.setInfoShiftPercentageList(infoShiftPercentageList);
		return result;
	}

	private static Teacher readTeacher(InfoTeacher infoTeacher) {
		return rootDomainObject.readTeacherByOID(infoTeacher.getIdInternal());
	}

	private static ExecutionCourse readExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		return rootDomainObject.readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
	}
}