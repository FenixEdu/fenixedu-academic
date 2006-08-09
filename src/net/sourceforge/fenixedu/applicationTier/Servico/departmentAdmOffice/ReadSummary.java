/*
 * Created on 21/Jul/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummaryWithAll;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Manuel Pinto e Jo�o Figueiredo
 */
public class ReadSummary extends Service {

    public SiteView run(Integer teacherNumber, Integer executionCourseId, Integer summaryId)
            throws FenixServiceException, ExcepcaoPersistencia {
        SiteView siteView;

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executioncourse");
        }

        Site site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        List shifts = executionCourse.getAssociatedShifts();
        List infoShifts = new ArrayList();
        if (shifts != null && shifts.size() > 0) {
            infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {

                public Object transform(Object arg0) {
                    final Shift turno = (Shift) arg0;
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(turno);
                    infoShift.setInfoLessons(new ArrayList(turno.getAssociatedLessons().size()));
                    for (final Lesson lesson : turno.getAssociatedLessonsSet()) {
                    	infoShift.getInfoLessons().add(InfoLesson.newInfoFromDomain(lesson));
                    }
                    return infoShift;
                }
            });
        }

        List infoProfessorships = new ArrayList();
        Teacher teacher = Teacher.readByNumber(teacherNumber);

        if (teacher != null) {
            Professorship professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            if (professorship != null) {
                infoProfessorships.add(InfoProfessorshipWithAll.newInfoFromDomain(professorship));
            }
        }

        Collection<OldRoom> rooms = OldRoom.getOldRooms();
        List infoRooms = new ArrayList();
        if (rooms != null && rooms.size() > 0) {
            infoRooms = (List) CollectionUtils.collect(rooms, new Transformer() {

                public Object transform(Object arg0) {
                    OldRoom room = (OldRoom) arg0;
                    return InfoRoom.newInfoFromDomain(room);
                }
            });
        }
        Collections.sort(infoRooms, new BeanComparator("nome"));

        Summary summary = rootDomainObject.readSummaryByOID(summaryId);
        if (summary == null) {
            throw new FenixServiceException("no.summary");
        }
        if (summary.getShift() == null) {
            findBestShiftForSummary(summary, shifts);
            if (summary.getShift() == null) {
                throw new FenixServiceException();
            }
        }

        InfoSiteSummary bodyComponent = new InfoSiteSummary();
        bodyComponent.setInfoSummary(InfoSummaryWithAll.newInfoFromDomain(summary));
        bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(infoProfessorships);
        bodyComponent.setInfoRooms(infoRooms);

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);

        siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        return siteView;
    }

    private void findBestShiftForSummary(Summary summary, List shifts) {
        // choose the shift with:
        // 1. the same summary type / lesson type
        // 2. the lesson with date and hour of the summary

        if (summary.getSummaryDate() != null && summary.getSummaryHour() != null && shifts != null
                && shifts.size() >= 0) {
            Calendar dateAndHourSummary = Calendar.getInstance();

            Calendar summaryDate = Calendar.getInstance();
            summaryDate.setTime(summary.getSummaryDate());

            Calendar summaryHour = Calendar.getInstance();
            summaryHour.setTime(summary.getSummaryHour());

            dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
            dateAndHourSummary.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            dateAndHourSummary.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));
            dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summaryHour.get(Calendar.HOUR_OF_DAY));
            dateAndHourSummary.set(Calendar.MINUTE, summaryHour.get(Calendar.MINUTE));
            dateAndHourSummary.set(Calendar.SECOND, 00);

            Calendar beginLesson = Calendar.getInstance();
            beginLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
            beginLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            beginLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            Calendar endLesson = Calendar.getInstance();
            endLesson.set(Calendar.DAY_OF_MONTH, summaryDate.get(Calendar.DAY_OF_MONTH));
            endLesson.set(Calendar.MONTH, summaryDate.get(Calendar.MONTH));
            endLesson.set(Calendar.YEAR, summaryDate.get(Calendar.YEAR));

            ListIterator iteratorShift = shifts.listIterator();
            while (iteratorShift.hasNext()) {
                Shift shift = (Shift) iteratorShift.next();
                if (summary.getSummaryType() != null && shift.getTipo() != null
                        && !summary.getSummaryType().equals(shift.getTipo())) {
                    continue;
                }

                if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0) {
                    ListIterator iteratorLesson = shift.getAssociatedLessons().listIterator();
                    while (iteratorLesson.hasNext()) {
                        Lesson lesson = (Lesson) iteratorLesson.next();

                        beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(
                                Calendar.HOUR_OF_DAY));
                        beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(Calendar.MINUTE));
                        beginLesson.set(Calendar.SECOND, 00);

                        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
                        endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
                        endLesson.set(Calendar.SECOND, 00);

                        if (dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana()
                                .getDiaSemana().intValue()
                                && !beginLesson.after(dateAndHourSummary)
                                && endLesson.after(dateAndHourSummary)) {
                            summary.setShift(shift);
                            summary.setIsExtraLesson(Boolean.FALSE);
                            summary.setRoom(lesson.getSala());
                            return;
                        }
                    }
                }
            }

            // If the execution to arrive until here,
            // then was impossible attribute a shift to the summary
            summary.setIsExtraLesson(Boolean.TRUE);
            summary.setShift((Shift) shifts.get(0));
        }
    }
}