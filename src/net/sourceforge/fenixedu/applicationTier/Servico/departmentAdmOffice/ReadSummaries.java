
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IServico;
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
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionCourse;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoAula;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 *  Manuel Pinto e João Figueiredo
 */

public class ReadSummaries implements IServico {

    private static ReadSummaries service = new ReadSummaries();

    public static ReadSummaries getService() {

        return service;
    }

    /**
     *  
     */
    public ReadSummaries() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "ReadSummaries";
    }

    public SiteView run(Integer teacherNumber, Integer executionCourseId, Integer summaryType, Integer shiftId) throws FenixServiceException {

        try {       	
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            
            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            ITeacher teacher = persistentTeacher.readByNumber(teacherNumber);
            
            if(teacher == null)
            	throw new FenixServiceException("no.shift");
            
            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseId);
            
            if (executionCourse == null) {
                throw new FenixServiceException("no.executionCourse");
            }
            
            //execution courses's lesson types for display to filter summary
            List lessonTypes = findLessonTypesExecutionCourse(executionCourse);
            
            IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
            ISite site = persistentSite.readByExecutionCourse(executionCourse);
           
            if (site == null) {
                throw new FenixServiceException("no.site");
            }	           

            //execution courses's shifts for display to filter summary
            ITurnoPersistente persistentShift = persistentSuport.getITurnoPersistente();
            List shifts = persistentShift.readByExecutionCourse(executionCourse);
            List infoShifts = new ArrayList();
            if (shifts != null && shifts.size() > 0) {
                infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {

                    public Object transform(Object arg0) {
                        IShift turno = (IShift) arg0;
                        return InfoShift.newInfoFromDomain(turno);
                    }
                });
            }

            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
            List summaries = null;
            if (summaryType != null && summaryType.intValue() > 0) {
                List summariesBySummaryType = persistentSummary
                        .readByExecutionCourseShiftsAndTypeLesson(executionCourse, new TipoAula(
                                summaryType));

                //read summary also by execution course key
                //and add to the last list
                List summariesByExecutionCourseBySummaryType = persistentSummary
                        .readByExecutionCourseAndType(executionCourse, new TipoAula(summaryType));

                summaries = allSummaries(summariesBySummaryType, summariesByExecutionCourseBySummaryType);
            }

            if (shiftId != null && shiftId.intValue() > 0) {
                IShift shiftSelected = (IShift) persistentShift.readByOID(Shift.class, shiftId);
                if (shiftSelected == null) {
                    throw new FenixServiceException("no.shift");
                }

                List summariesByShift = persistentSummary.readByShift(executionCourse, shiftSelected);

                List summariesByExecutionCourseByShift = findLesson(persistentSummary, executionCourse,
                        shiftSelected);

                if (summaries != null) {
                    summaries = (List) CollectionUtils.intersection(summaries, allSummaries(
                            summariesByShift, summariesByExecutionCourseByShift));
                } else {
                    summaries = allSummaries(summariesByShift, summariesByExecutionCourseByShift);
                }
            }

            if (teacherNumber != null && teacherNumber.intValue() > 0) {          		
                List summariesByProfessorship = persistentSummary.readByTeacher(executionCourse, teacher);

                if (summaries != null) {
                    summaries = (List) CollectionUtils.intersection(summaries, summariesByProfessorship);
                } else {
                    summaries = summariesByProfessorship;
                }
            }
            
            List responsibleTeachers = executionCourse.getResponsibleTeachers();
            boolean responsible = false;
            for (Iterator iter = responsibleTeachers.iterator(); iter.hasNext();) {
				IResponsibleFor element = (IResponsibleFor) iter.next();
				
				if (element.getTeacher().equals(teacher)){
					responsible = true;
					break;
				}
			}
            
			if(responsible){
				List summariesByTeacher = persistentSummary.readByOtherTeachers(executionCourse);               
                if (summaries != null) {                	          
                	summaries.addAll(summariesByTeacher);
                } else {
                    summaries = summariesByTeacher;
                }	                      
            }

            if ((summaryType == null || summaryType.intValue() == 0)
                    && (shiftId == null || shiftId.intValue() == 0)
                    && (teacherNumber == null || teacherNumber.intValue() == 0)) {
                summaries = persistentSummary.readByExecutionCourseShifts(executionCourse);
                List summariesByExecutionCourse = persistentSummary
                        .readByExecutionCourse(executionCourse);

                summaries = allSummaries(summaries, summariesByExecutionCourse);
            }

            List result = new ArrayList();
            if (summaries != null && summaries.size() > 0) {
                Iterator iter = summaries.iterator();
                while (iter.hasNext()) {
                    ISummary summary = (ISummary) iter.next();
                    InfoSummary infoSummary = Cloner.copyISummary2InfoSummary(summary);
                    result.add(infoSummary);
                }
            }
            
            InfoSiteSummaries bodyComponent = new InfoSiteSummaries();
            bodyComponent.setInfoSummaries(result);
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));
            bodyComponent.setLessonTypes(lessonTypes);
            bodyComponent.setInfoShifts(infoShifts);
            bodyComponent.setInfoProfessorships(new ArrayList());

            TeacherAdministrationSiteComponentBuilder componentBuilder = TeacherAdministrationSiteComponentBuilder
                    .getInstance();
            ISiteComponent commonComponent = componentBuilder.getComponent(new InfoSiteCommon(), site,
                    null, null, null);
            SiteView siteView = new ExecutionCourseSiteView(commonComponent, bodyComponent);

            return siteView;
        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
    }

    private List findLesson(IPersistentSummary persistentSummary, IExecutionCourse executionCourse,
            IShift shift) throws ExcepcaoPersistencia {

        List summariesByExecutionCourse = persistentSummary.readByExecutionCourse(executionCourse);

        //copy the list
        List summariesByShift = new ArrayList();
        summariesByShift.addAll(summariesByExecutionCourse);

        if (summariesByExecutionCourse != null && summariesByExecutionCourse.size() > 0) {
            ListIterator iterator = summariesByExecutionCourse.listIterator();
            while (iterator.hasNext()) {
                ISummary summary = (ISummary) iterator.next();

                Calendar dateAndHourSummary = Calendar.getInstance();
                dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate().get(
                        Calendar.DAY_OF_MONTH));
                dateAndHourSummary.set(Calendar.MONTH, summary.getSummaryDate().get(Calendar.MONTH));
                dateAndHourSummary.set(Calendar.YEAR, summary.getSummaryDate().get(Calendar.YEAR));
                dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summary.getSummaryHour().get(
                        Calendar.HOUR_OF_DAY));
                dateAndHourSummary.set(Calendar.MINUTE, summary.getSummaryHour().get(Calendar.MINUTE));
                dateAndHourSummary.set(Calendar.SECOND, 00);

                Calendar beginLesson = Calendar.getInstance();
                beginLesson.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate().get(
                        Calendar.DAY_OF_MONTH));
                beginLesson.set(Calendar.MONTH, summary.getSummaryDate().get(Calendar.MONTH));
                beginLesson.set(Calendar.YEAR, summary.getSummaryDate().get(Calendar.YEAR));

                Calendar endLesson = Calendar.getInstance();
                endLesson
                        .set(Calendar.DAY_OF_MONTH, summary.getSummaryDate().get(Calendar.DAY_OF_MONTH));
                endLesson.set(Calendar.MONTH, summary.getSummaryDate().get(Calendar.MONTH));
                endLesson.set(Calendar.YEAR, summary.getSummaryDate().get(Calendar.YEAR));

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

    private List findLessonTypesExecutionCourse(IExecutionCourse executionCourse) {
        List lessonTypes = new ArrayList();

        if (executionCourse.getTheoreticalHours() != null
                && executionCourse.getTheoreticalHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(1));
        }
        if (executionCourse.getTheoPratHours() != null
                && executionCourse.getTheoPratHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(3));
        }
        if (executionCourse.getPraticalHours() != null
                && executionCourse.getPraticalHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(2));
        }
        if (executionCourse.getLabHours() != null && executionCourse.getLabHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(4));
        }

        return lessonTypes;
    }
}