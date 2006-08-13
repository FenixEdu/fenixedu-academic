/*
 * ReadDegreesClassesLessons.java
 * 
 * Created on 2003/07/17
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoViewClassSchedule;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * TODO Remove cloner deste servi�o...
 */
public class ReadDegreesClassesLessons extends Service {

	public List run(List infoExecutionDegrees, InfoExecutionPeriod infoExecutionPeriod) throws ExcepcaoPersistencia {

		List infoViewClassScheduleList = new ArrayList();

		List classes = new ArrayList();
		for (int i = 0; i < infoExecutionDegrees.size(); i++) {
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) infoExecutionDegrees.get(i);
			ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(infoExecutionDegree.getIdInternal());
			List degreeClasses = executionDegree.getSchoolClasses();
			for (Iterator iterator = degreeClasses.iterator(); iterator.hasNext();) {
				SchoolClass klass = (SchoolClass) iterator.next();
				if (klass.getExecutionPeriod().getIdInternal().equals(
						infoExecutionPeriod.getIdInternal())) {
					classes.add(klass);
				}
			}
		}

		for (int i = 0; i < classes.size(); i++) {
			InfoViewClassSchedule infoViewClassSchedule = new InfoViewClassSchedule();
			SchoolClass turma = (SchoolClass) classes.get(i);

			// read class lessons
			List shiftList = turma.getAssociatedShifts();
			Iterator iterator = shiftList.iterator();
			List infoLessonList = new ArrayList();
			while (iterator.hasNext()) {
				Shift shift = (Shift) iterator.next();
				final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

				final ExecutionCourse executionCourse = shift.getDisciplinaExecucao();
				final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
						.newInfoFromDomain(executionCourse);
				infoShift.setInfoDisciplinaExecucao(infoExecutionCourse);

				final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
				final InfoExecutionPeriod infoExecutionPeriod2 = InfoExecutionPeriod
						.newInfoFromDomain(executionPeriod);
				infoExecutionCourse.setInfoExecutionPeriod(infoExecutionPeriod2);

				final Collection lessons = shift.getAssociatedLessons();
				final List infoLessons = new ArrayList(lessons.size());
				infoShift.setInfoLessons(infoLessons);
				for (final Iterator iterator2 = lessons.iterator(); iterator2.hasNext();) {
					final Lesson lesson = (Lesson) iterator2.next();
					final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
					infoLessons.add(infoLesson);
					infoLessonList.add(infoLesson);
				}

				final Collection schoolClasses = shift.getAssociatedClasses();
				final List infoSchoolClasses = new ArrayList(schoolClasses.size());
				infoShift.setInfoClasses(infoSchoolClasses);
				for (final Iterator iterator2 = schoolClasses.iterator(); iterator2.hasNext();) {
					final SchoolClass schoolClass = (SchoolClass) iterator2.next();
					final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
					infoSchoolClasses.add(infoClass);
				}

			}

			InfoClass infoClass = InfoClass.newInfoFromDomain(turma);
			final ExecutionDegree executionDegree = turma.getExecutionDegree();
			final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
					.newInfoFromDomain(executionDegree);
			infoClass.setInfoExecutionDegree(infoExecutionDegree);

			final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
			final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
					.newInfoFromDomain(degreeCurricularPlan);
			infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

			infoClass.setInfoExecutionPeriod(infoExecutionPeriod);

			infoViewClassSchedule.setInfoClass(infoClass);
			infoViewClassSchedule.setClassLessons(infoLessonList);
			infoViewClassScheduleList.add(infoViewClassSchedule);
		}

		return infoViewClassScheduleList;
	}
}