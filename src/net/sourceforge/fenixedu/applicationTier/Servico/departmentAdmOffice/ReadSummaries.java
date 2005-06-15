package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * Manuel Pinto e João Figueiredo
 */
public class ReadSummaries implements IService {
    
    public SiteView run(Integer teacherNumber, Integer executionCourseId, String summaryType,
            Integer shiftId) throws FenixServiceException, ExcepcaoPersistencia {
        
        final ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        final IPersistentExecutionCourse persistentExecutionCourse = persistentSuport.getIPersistentExecutionCourse();
        final IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
        final ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
        
        final ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
        if (teacher == null) {
            throw new FenixServiceException("no.shift");
        }
        
        final IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                ExecutionCourse.class, executionCourseId);
        if (executionCourse == null) {
            throw new FenixServiceException("no.executionCourse");
        }
       
        List<ISummary> summaries_aux = readSummariesByType(executionCourseId, summaryType, persistentSummary);        
       
        summaries_aux = readSummariesByShift(executionCourseId, shiftId, executionCourse, persistentShift, persistentSummary, summaries_aux);
               
        summaries_aux = readAllSummaries(executionCourseId, summaryType, shiftId, persistentSummary, summaries_aux);
                
        List result = new ArrayList();
        if (summaries_aux != null && summaries_aux.size() > 0) {
            Iterator iter = summaries_aux.iterator();
            while (iter.hasNext()) {
                ISummary summary = (ISummary) iter.next();
                InfoSummary infoSummary = InfoSummary.newInfoFromDomain(summary);
                result.add(infoSummary);
            }
        }
        
        // execution courses's lesson types for display to filter summary
        List lessonTypes = findLessonTypesExecutionCourse(executionCourse);
        
        IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
        ISite site = persistentSite.readByExecutionCourse(executionCourse.getIdInternal());
        
        if (site == null) {
            throw new FenixServiceException("no.site");
        }
        
        // execution courses's shifts for display to filter summary
        List shifts = persistentShift.readByExecutionCourse(executionCourse.getIdInternal());
        List infoShifts = new ArrayList();
        if (shifts != null && shifts.size() > 0) {
            infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {
                
                public Object transform(Object arg0) {
                    final IShift turno = (IShift) arg0;
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(turno);
                    infoShift.setInfoLessons(new ArrayList(turno.getAssociatedLessons().size()));
                    for (final Iterator iterator = turno.getAssociatedLessons().iterator(); iterator
                    .hasNext();) {
                        final ILesson lesson = (ILesson) iterator.next();
                        final InfoLesson infoLesson = InfoLesson.newInfoFromDomain(lesson);
                        final InfoRoom infoRoom = InfoRoom.newInfoFromDomain(lesson.getSala());
                        infoLesson.setInfoSala(infoRoom);
                        infoShift.getInfoLessons().add(infoLesson);
                    }
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
    
    private List findLessonTypesExecutionCourse(IExecutionCourse executionCourse) {
        List lessonTypes = new ArrayList();
        
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
    
    protected List readSummariesByType(Integer executionCourseId, String summaryType,
            IPersistentSummary persistentSummary) throws ExcepcaoPersistencia {
        List summaries = null;
        if (summaryType != null && !summaryType.equals("0")) {
            ShiftType sumaryType = ShiftType.valueOf(summaryType);

            List summariesBySummaryType = persistentSummary.readByExecutionCourseShiftsAndTypeLesson(
                    executionCourseId, sumaryType);

            List summariesByExecutionCourseBySummaryType = persistentSummary
                    .readByExecutionCourseAndType(executionCourseId, sumaryType);

            summaries = allSummaries(summariesBySummaryType, summariesByExecutionCourseBySummaryType);
        }
        return summaries;
    }
    
    protected List readSummariesByShift(Integer executionCourseId, Integer shiftId,
            IExecutionCourse executionCourse, ITurnoPersistente persistentShift,
            IPersistentSummary persistentSummary, List summaries) throws ExcepcaoPersistencia,
            FenixServiceException {
        if (shiftId != null && shiftId.intValue() > 0) {
            IShift shiftSelected = (IShift) persistentShift.readByOID(Shift.class, shiftId);
            if (shiftSelected == null) {
                throw new FenixServiceException("no.shift");
            }

            List summariesByShift = persistentSummary.readByShift(executionCourseId, shiftSelected
                    .getIdInternal());

            List summariesByExecutionCourseByShift = findLesson(persistentSummary, executionCourse,
                    shiftSelected);

            if (summaries != null) {
                summaries = (List) CollectionUtils.intersection(summaries, allSummaries(
                        summariesByShift, summariesByExecutionCourseByShift));
            } else {
                summaries = allSummaries(summariesByShift, summariesByExecutionCourseByShift);
            }
        }
        return summaries;
    }
    
    protected List readTeacherSummaries(Integer executionCourseId, Integer professorShiftId,
            IPersistentProfessorship persistentProfessorship, IPersistentSummary persistentSummary,
            List summaries) throws ExcepcaoPersistencia, FenixServiceException {

        if (professorShiftId != null && professorShiftId.intValue() > 0) {
            IProfessorship professorshipSelected = (IProfessorship) persistentProfessorship.readByOID(
                    Professorship.class, professorShiftId);

            if (professorshipSelected == null || professorshipSelected.getTeacher() == null) {
                throw new FenixServiceException("no.shift");
            }

            List summariesByProfessorship = persistentSummary.readByTeacher(executionCourseId,
                    professorshipSelected.getTeacher().getTeacherNumber());

            if (summaries != null) {
                summaries = (List) CollectionUtils.intersection(summaries, summariesByProfessorship);
            } else {
                summaries = summariesByProfessorship;
            }
        }
        return summaries;
    }
    
    protected List readAllSummaries(Integer executionCourseId, String summaryType, Integer shiftId, IPersistentSummary persistentSummary, List summaries) throws ExcepcaoPersistencia {
        if ((summaryType == null || summaryType.equals("0")) && (shiftId == null || shiftId.intValue() == 0)) {
            
            summaries = persistentSummary.readByExecutionCourseShifts(executionCourseId);

            List summariesByExecutionCourse = persistentSummary.readByExecutionCourse(executionCourseId);           
            
            summaries = allSummaries(summaries, summariesByExecutionCourse);
        }
        return summaries;
    }  
    
    private List allSummaries(List summaries, List summariesByExecutionCourse) {
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
    
    private List findLesson(IPersistentSummary persistentSummary, IExecutionCourse executionCourse,
            IShift shift) throws ExcepcaoPersistencia {

        List summariesByExecutionCourse = persistentSummary.readByExecutionCourse(executionCourse
                .getIdInternal());

        // copy the list
        List summariesByShift = new ArrayList();
        summariesByShift.addAll(summariesByExecutionCourse);

        if (summariesByExecutionCourse != null && summariesByExecutionCourse.size() > 0) {
            ListIterator iterator = summariesByExecutionCourse.listIterator();
            while (iterator.hasNext()) {
                ISummary summary = (ISummary) iterator.next();

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
                        ILesson lesson = (ILesson) iterLesson.next();

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
                                && endLesson.after(dateAndHourSummary)) {
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