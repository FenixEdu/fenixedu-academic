package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadSummaries extends Service {

    public SiteView run(Integer teacherNumber, Integer executionCourseId, String summaryType,
            Integer shiftId) throws FenixServiceException, ExcepcaoPersistencia {

        final Teacher teacher = Teacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new FenixServiceException("no.shift");
        }

        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }

        List<Summary> summaries_aux = readSummariesByType(executionCourse, summaryType);
        summaries_aux = readSummariesByShift(executionCourseId, shiftId, executionCourse, summaries_aux);
        summaries_aux = readAllSummaries(executionCourse, summaryType, shiftId, summaries_aux);

        List<InfoSummary> result = new ArrayList<InfoSummary>();
        if (summaries_aux != null && summaries_aux.size() > 0) {
            Iterator iter = summaries_aux.iterator();
            while (iter.hasNext()) {
                Summary summary = (Summary) iter.next();
                InfoSummary infoSummary = InfoSummary.newInfoFromDomain(summary);
                result.add(infoSummary);
            }
        }

        // execution courses's lesson types for display to filter summary
        List lessonTypes = findLessonTypesExecutionCourse(executionCourse);

        Site site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        // execution courses's shifts for display to filter summary
        List shifts = executionCourse.getAssociatedShifts();
        List infoShifts = new ArrayList();
        if (shifts != null && shifts.size() > 0) {
            infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {

                public Object transform(Object arg0) {
                    final Shift turno = (Shift) arg0;
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(turno);
                    return infoShift;
                }
            });
        }

        InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        bodyComponent.setInfoSummaries(result);
        bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        bodyComponent.setLessonTypes(lessonTypes);
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(new ArrayList());

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);
        SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        return siteView;
    }

    private List<ShiftType> findLessonTypesExecutionCourse(ExecutionCourse executionCourse) {
        List<ShiftType> lessonTypes = new ArrayList<ShiftType>();

        if (executionCourse.getTheoreticalHours() != null
                && executionCourse.getTheoreticalHours().intValue() > 0) {
            lessonTypes.add(ShiftType.TEORICA);
        }
        if (executionCourse.getTheoPratHours() != null
                && executionCourse.getTheoPratHours().intValue() > 0) {
            lessonTypes.add(ShiftType.TEORICO_PRATICA);
        }
        if (executionCourse.getPraticalHours() != null
                && executionCourse.getPraticalHours().intValue() > 0) {
            lessonTypes.add(ShiftType.PRATICA);
        }
        if (executionCourse.getLabHours() != null && executionCourse.getLabHours().intValue() > 0) {
            lessonTypes.add(ShiftType.LABORATORIAL);
        }

        return lessonTypes;
    }

    protected List<Summary> readSummariesByType(ExecutionCourse executionCourse, String summaryType)
            throws ExcepcaoPersistencia {
        List<Summary> summaries = null;
        if (summaryType != null && !summaryType.equals("0")) {
            ShiftType sumaryType = ShiftType.valueOf(summaryType);

            summaries = executionCourse.readSummariesByShiftType(sumaryType);
        }
        return summaries;
    }

    protected List<Summary> readSummariesByShift(Integer executionCourseId, Integer shiftId,
            ExecutionCourse executionCourse, List<Summary> summaries) throws ExcepcaoPersistencia,
            FenixServiceException {
        if (shiftId != null && shiftId.intValue() > 0) {
            Shift shiftSelected = rootDomainObject.readShiftByOID(shiftId);
            if (shiftSelected == null) {
                throw new FenixServiceException("no.shift");
            }

            List summariesByShift = shiftSelected.getAssociatedSummaries();

            List summariesByExecutionCourseByShift = findLesson(executionCourse, shiftSelected);

            if (summaries != null) {
                summaries = (List) CollectionUtils.intersection(summaries, allSummaries(
                        summariesByShift, summariesByExecutionCourseByShift));
            } else {
                summaries = allSummaries(summariesByShift, summariesByExecutionCourseByShift);
            }
        }
        return summaries;
    }

    protected List<Summary> readAllSummaries(ExecutionCourse executionCourse, String summaryType,
            Integer shiftId, List<Summary> summaries) throws ExcepcaoPersistencia {

        if ((summaryType == null || summaryType.equals("0"))
                && (shiftId == null || shiftId.intValue() == 0)) {

            summaries = executionCourse.getAssociatedSummaries();
        }
        return summaries;
    }

    private List<Summary> allSummaries(List<Summary> summaries, List<Summary> summariesByExecutionCourse) {
        List<Summary> allSummaries = new ArrayList<Summary>();

        if (summaries == null || summaries.size() <= 0) {
            if (summariesByExecutionCourse == null) {
                return new ArrayList<Summary>();
            }
            return summariesByExecutionCourse;
        }

        if (summariesByExecutionCourse == null || summariesByExecutionCourse.size() <= 0) {
            return summaries;
        }

        List intersection = (List) CollectionUtils.intersection(summaries, summariesByExecutionCourse);

        allSummaries.addAll(CollectionUtils.disjunction(summaries, intersection));
        allSummaries.addAll(CollectionUtils.disjunction(summariesByExecutionCourse, intersection));
        allSummaries.addAll(intersection);

        if (allSummaries == null) {
            return new ArrayList<Summary>();
        }
        return allSummaries;
    }

    private List findLesson(ExecutionCourse executionCourse, Shift shift) throws ExcepcaoPersistencia {

        List<Summary> summariesByExecutionCourse = executionCourse.getAssociatedSummaries();

        // copy the list
        List<Summary> summariesByShift = new ArrayList<Summary>();
        summariesByShift.addAll(summariesByExecutionCourse);

        if (summariesByExecutionCourse != null && summariesByExecutionCourse.size() > 0) {
            ListIterator iterator = summariesByExecutionCourse.listIterator();
            while (iterator.hasNext()) {
                Summary summary = (Summary) iterator.next();

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

                boolean removeSummary = true;
                if (shift.getAssociatedLessons() != null && shift.getAssociatedLessons().size() > 0) {
                    ListIterator iterLesson = shift.getAssociatedLessons().listIterator();
                    while (iterLesson.hasNext()) {
                        Lesson lesson = (Lesson) iterLesson.next();

                        beginLesson.set(Calendar.HOUR_OF_DAY, lesson.getInicio().get(
                                Calendar.HOUR_OF_DAY));
                        beginLesson.set(Calendar.MINUTE, lesson.getInicio().get(Calendar.MINUTE));
                        beginLesson.set(Calendar.SECOND, 00);

                        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim().get(Calendar.HOUR_OF_DAY));
                        endLesson.set(Calendar.MINUTE, lesson.getFim().get(Calendar.MINUTE));
                        endLesson.set(Calendar.SECOND, 00);

                        if (summary.getSummaryType().equals(shift.getTipo())
                                && dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson.getDiaSemana()
                                        .getDiaSemana().intValue()
                                && !beginLesson.after(dateAndHourSummary)
                                && endLesson.after(dateAndHourSummary)
                                && lesson.getSala().equals(summary.getRoom())) {
                            removeSummary = false;
                        }
                    }
                }

                if (removeSummary) {
                    summariesByShift.remove(summary);
                }
            }
        }
        return summariesByShift;
    }

}
