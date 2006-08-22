package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

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
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

public class ReadSummaries extends Service {

    public SiteView run(Integer executionCourseId, String summaryType, Integer shiftId,
            Integer professorShiftId) throws FenixServiceException, ExcepcaoPersistencia {

        ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }

        final Site site = executionCourse.getSite();
        if (site == null) {
            throw new FenixServiceException("no.site");
        }

        // execution courses's lesson types for display to filter summary
        List lessonTypes = findLessonTypesExecutionCourse(executionCourse);

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

        // execution courses's professorships for display to filter summary
        List<Professorship> professorships = executionCourse.getProfessorships();
        List infoProfessorships = new ArrayList();
        if (professorships != null && professorships.size() > 0) {
            infoProfessorships = (List) CollectionUtils.collect(professorships, new Transformer() {

                public Object transform(Object arg0) {
                    Professorship professorship = (Professorship) arg0;
                    return InfoProfessorship.newInfoFromDomain(professorship);
                }
            });
        }

        List summaries = readSummariesByType(executionCourse, summaryType);
        summaries = readSummariesByShift(executionCourseId, shiftId, executionCourse, summaries);
        summaries = readTeacherSummaries(executionCourseId, professorShiftId, summaries);
        summaries = readSummariesOfOtherTeachers(executionCourse, professorShiftId, summaries);
        summaries = readSummariesOfOtherTeachersIfResponsible(executionCourse, professorShiftId, summaries, professorships, summaryType);
        summaries = readAllSummaries(executionCourse, summaryType, shiftId, professorShiftId, summaries);

        List<InfoSummary> result = new ArrayList<InfoSummary>();
        if (summaries != null && summaries.size() > 0) {
            Iterator iter = summaries.iterator();
            while (iter.hasNext()) {
                Summary summary = (Summary) iter.next();
                InfoSummary infoSummary = InfoSummary.newInfoFromDomain(summary);
                result.add(infoSummary);
            }
        }

        InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
        bodyComponent.setInfoSummaries(result);
        bodyComponent.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));
        bodyComponent.setLessonTypes(lessonTypes);
        bodyComponent.setInfoShifts(infoShifts);
        bodyComponent.setInfoProfessorships(infoProfessorships);

        TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                .getInstance();
        ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site, null,
                null, null);
        SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

        return siteView;
    }

    protected List readAllSummaries(ExecutionCourse executionCourse, String summaryType,
            Integer shiftId, Integer professorShiftId, List summaries) throws ExcepcaoPersistencia {
        
        if ((summaryType == null || summaryType.equals("0"))
                && (shiftId == null || shiftId.intValue() == 0)
                && (professorShiftId == null || professorShiftId.intValue() == 0)) {
            
            summaries = executionCourse.getAssociatedSummaries();
        }
        return summaries;
    }

    protected List readSummariesByShift(Integer executionCourseId, Integer shiftId,
            ExecutionCourse executionCourse, List summaries) throws ExcepcaoPersistencia,
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

    protected List readSummariesByType(ExecutionCourse executionCourse, String summaryType)
            throws ExcepcaoPersistencia {
        List summaries = null;
        if (summaryType != null && !summaryType.equals("0")) {
            ShiftType sumaryType = ShiftType.valueOf(summaryType);

            summaries = executionCourse.readSummariesByShiftType(sumaryType);
        }
        return summaries;
    }

    protected List readTeacherSummaries(Integer executionCourseId, Integer professorShiftId,
            List summaries) throws ExcepcaoPersistencia, FenixServiceException {

        if (professorShiftId != null && professorShiftId.intValue() > 0) {
            Professorship professorshipSelected = rootDomainObject.readProfessorshipByOID(professorShiftId);
            if (professorshipSelected == null || professorshipSelected.getTeacher() == null) {
                throw new FenixServiceException("no.shift");
            }

            List summariesByProfessorship = professorshipSelected.getAssociatedSummaries();

            if (summaries != null) {
                summaries = (List) CollectionUtils.intersection(summaries, summariesByProfessorship);
            } else {
                summaries = summariesByProfessorship;
            }
        }
        return summaries;
    }

    protected List readSummariesOfOtherTeachersIfResponsible(ExecutionCourse executionCourse,
            Integer professorShiftId, List summaries,
            List<Professorship> professorships, String summaryType) throws ExcepcaoPersistencia {

        Professorship professorship = getProfessorship(professorShiftId, professorships);

        if ((professorship != null) && (professorShiftId != null) && (professorShiftId.intValue() > 0)) {

            Professorship responsibleFor = professorship.getTeacher().responsibleFor(executionCourse);

            if (responsibleFor != null && responsibleFor.getResponsibleFor()) {
                List<Summary> summariesByTeacher = executionCourse.readSummariesOfTeachersWithoutProfessorship();

                if (summaryType != null && !summaryType.equals("0")) {
                    ShiftType sumaryType = ShiftType.valueOf(summaryType);

                    List<Summary> summariesAux = new ArrayList<Summary>();
                    for (Summary summary : summariesByTeacher) {
                        if (summary.getSummaryType().equals(sumaryType))
                            summariesAux.add(summary);
                    }

                    if (summaries != null)
                        summaries = (List) CollectionUtils.union(summaries, summariesAux);
                    else
                        summaries = summariesAux;
                } else {
                    if (summaries != null)
                        summaries = (List) CollectionUtils.union(summaries, summariesByTeacher);
                    else
                        summaries = summariesByTeacher;
                }
            }
        }
        return summaries;
    }

    protected List readSummariesOfOtherTeachers(ExecutionCourse executionCourse,
            Integer professorShiftId, List summaries) throws ExcepcaoPersistencia {
        if (professorShiftId != null && professorShiftId.equals(new Integer(-1))) {
            List summariesByTeacher = executionCourse.readSummariesOfTeachersWithoutProfessorship();

            if (summaries != null) {
                summaries = (List) CollectionUtils.intersection(summaries, summariesByTeacher);
            } else {
                summaries = summariesByTeacher;
            }
        }
        return summaries;
    }

    protected Professorship getProfessorship(Integer professorShiftId, List<Professorship> professorships) {
        Professorship professorship = null;
        for (Professorship professorship2 : professorships) {
            if (professorship2.getIdInternal().equals(professorShiftId)) {
                professorship = professorship2;
                break;
            }
        }
        return professorship;
    }

    public static List findLesson(ExecutionCourse executionCourse, Shift shift)
            throws ExcepcaoPersistencia {

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

                        if (summary.getSummaryType() == shift.getTipo()
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

    public static List allSummaries(List summaries, List summariesByExecutionCourse) {
        List allSummaries = new ArrayList();

        if (summaries == null || summaries.size() <= 0) {
            if (summariesByExecutionCourse == null) {
                return new ArrayList();
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
            return new ArrayList();
        }
        return allSummaries;
    }

    private List findLessonTypesExecutionCourse(ExecutionCourse executionCourse) {
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
}