/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.slide.common.SlideException;

import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoClassWithInfoExecutionDegree;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoDegreeCurricularPlanWithDegree;
import DataBeans.InfoEvaluation;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseWithExecutionPeriod;
import DataBeans.InfoItem;
import DataBeans.InfoLesson;
import DataBeans.InfoProfessorshipWithAll;
import DataBeans.InfoRoomOccupation;
import DataBeans.InfoSection;
import DataBeans.InfoShift;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoShiftWithInfoExecutionCourseAndCollections;
import DataBeans.InfoSite;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteTimetable;
import DataBeans.InfoSummary;
import DataBeans.InfoSummaryWithAll;
import DataBeans.InfoTeacher;
import DataBeans.InfoTeacherWithPerson;
import DataBeans.util.CMSUtils;
import DataBeans.util.Cloner;
import Dominio.IAnnouncement;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IEvaluation;
import Dominio.IEvaluationMethod;
import Dominio.IExecutionCourse;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import Dominio.Professorship;
import Dominio.Turno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoAula;
import fileSuport.FileSuport;
import fileSuport.IFileSuport;

/**
 * @author João Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentBuilder {

    private static ExecutionCourseSiteComponentBuilder instance = null;

    public ExecutionCourseSiteComponentBuilder() {
    }

    public static ExecutionCourseSiteComponentBuilder getInstance() {
        if (instance == null) {
            instance = new ExecutionCourseSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, ISite site,
            ISiteComponent commonComponent, Integer sectionIndex,
            Integer curricularCourseId) throws FenixServiceException {

        if (component instanceof InfoSiteCommon) {
            return getInfoSiteCommon((InfoSiteCommon) component, site);
        } else if (component instanceof InfoSiteFirstPage) {
            return getInfoSiteFirstPage((InfoSiteFirstPage) component, site);

        } else if (component instanceof InfoSiteAnnouncement) {
            return getInfoSiteAnnouncement((InfoSiteAnnouncement) component,
                    site);
        } else if (component instanceof InfoEvaluationMethod) {
            return getInfoEvaluationMethod((InfoEvaluationMethod) component,
                    site);
        } else if (component instanceof InfoSiteBibliography) {
            return getInfoSiteBibliography((InfoSiteBibliography) component,
                    site);
        } else if (component instanceof InfoSiteAssociatedCurricularCourses) {
            return getInfoSiteAssociatedCurricularCourses(
                    (InfoSiteAssociatedCurricularCourses) component, site);
        } else if (component instanceof InfoSiteTimetable) {
            return getInfoSiteTimetable((InfoSiteTimetable) component, site);
        } else if (component instanceof InfoSiteShifts) {
            return getInfoSiteShifts((InfoSiteShifts) component, site);

        } else if (component instanceof InfoSiteSection) {
            return getInfoSiteSection((InfoSiteSection) component, site,
                    (InfoSiteCommon) commonComponent, sectionIndex);
        } else if (component instanceof InfoSiteEvaluation) {
            return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
        } else if (component instanceof InfoSiteSummaries) {
            return getInfoSiteSummaries((InfoSiteSummaries) component, site);
        }
        return null;
    }

    /**
     * @param summaries
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteSummaries(InfoSiteSummaries component,
            ISite site) throws FenixServiceException {
        try {
            IExecutionCourse executionCourse = site.getExecutionCourse();

            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();

            //execution courses's lesson types for display to filter summary
            List lessonTypes = findLessonTypesExecutionCourse(executionCourse);

            //execution courses's shifts for display to filter summary
            ITurnoPersistente persistentShift = persistentSuport
                    .getITurnoPersistente();
            List shifts = persistentShift
                    .readByExecutionCourse(executionCourse);
            List infoShifts = new ArrayList();
            if (shifts != null && shifts.size() > 0) {
                infoShifts = (List) CollectionUtils.collect(shifts,
                        new Transformer() {

                            public Object transform(Object arg0) {
                                ITurno turno = (ITurno) arg0;
                                return InfoShiftWithInfoExecutionCourseAndCollections
                                        .newInfoFromDomain(turno);
                            }
                        });
            }

            //execution courses's professorships for display to filter summary
            IPersistentProfessorship persistentProfessorship = persistentSuport
                    .getIPersistentProfessorship();
            List professorships = persistentProfessorship
                    .readByExecutionCourse(executionCourse);
            List infoProfessorships = new ArrayList();
            if (professorships != null && professorships.size() > 0) {
                infoProfessorships = (List) CollectionUtils.collect(
                        professorships, new Transformer() {

                            public Object transform(Object arg0) {
                                IProfessorship professorship = (IProfessorship) arg0;
                                return InfoProfessorshipWithAll
                                        .newInfoFromDomain(professorship);
                            }

                        });
            }

            IPersistentSummary persistentSummary = persistentSuport
                    .getIPersistentSummary();
            List summaries = null;
            if (component.getLessonType() != null
                    && component.getLessonType().intValue() > 0) {
                List summariesBySummaryType = persistentSummary
                        .readByExecutionCourseShiftsAndTypeLesson(
                                executionCourse, new TipoAula(component
                                        .getLessonType()));

                //read summary also by execution course key
                //and add to the last list
                List summariesByExecutionCourseBySummaryType = persistentSummary
                        .readByExecutionCourseAndType(executionCourse,
                                new TipoAula(component.getLessonType()));

                summaries = allSummaries(summariesBySummaryType,
                        summariesByExecutionCourseBySummaryType);
            }

            if (component.getShiftId() != null
                    && component.getShiftId().intValue() > 0) {
                ITurno shiftSelected = (ITurno) persistentShift.readByOID(
                        Turno.class, component.getShiftId());
                if (shiftSelected == null) {
                    throw new FenixServiceException("no.shift");
                }

                List summariesByShift = persistentSummary.readByShift(
                        executionCourse, shiftSelected);

                List summariesByExecutionCourseByShift = findLesson(
                        persistentSummary, executionCourse, shiftSelected);

                if (summaries != null) {
                    summaries = (List) CollectionUtils.intersection(summaries,
                            allSummaries(summariesByShift,
                                    summariesByExecutionCourseByShift));
                } else {
                    summaries = allSummaries(summariesByShift,
                            summariesByExecutionCourseByShift);
                }
            }

            if (component.getTeacherId() != null
                    && component.getTeacherId().intValue() > 0) {
                IProfessorship professorshipSelected = (IProfessorship) persistentProfessorship
                        .readByOID(Professorship.class, component
                                .getTeacherId());

                if (professorshipSelected == null
                        || professorshipSelected.getTeacher() == null) {
                    throw new FenixServiceException("no.shift");
                }

                List summariesByProfessorship = persistentSummary
                        .readByTeacher(executionCourse, professorshipSelected
                                .getTeacher());

                if (summaries != null) {
                    summaries = (List) CollectionUtils.intersection(summaries,
                            summariesByProfessorship);
                } else {
                    summaries = summariesByProfessorship;
                }
            }

            if (component.getTeacherId() != null
                    && component.getTeacherId().equals(new Integer(-1))) {
                List summariesByTeacher = persistentSummary
                        .readByOtherTeachers(executionCourse);

                if (summaries != null) {
                    summaries = (List) CollectionUtils.intersection(summaries,
                            summariesByTeacher);
                } else {
                    summaries = summariesByTeacher;
                }
            }

            if ((component.getLessonType() == null || component.getLessonType()
                    .intValue() == 0)
                    && (component.getShiftId() == null || component
                            .getShiftId().intValue() == 0)
                    && (component.getTeacherId() == null || component
                            .getTeacherId().intValue() == 0)) {
                summaries = persistentSummary
                        .readByExecutionCourseShifts(executionCourse);
                List summariesByExecutionCourse = persistentSummary
                        .readByExecutionCourse(executionCourse);

                summaries = allSummaries(summaries, summariesByExecutionCourse);
            }

            List result = new ArrayList();
            Iterator iter = summaries.iterator();
            while (iter.hasNext()) {
                ISummary summary = (ISummary) iter.next();
                InfoSummary infoSummary = InfoSummaryWithAll
                        .newInfoFromDomain(summary);
                result.add(infoSummary);
            }

            component.setInfoSummaries(result);
            component.setInfoSite(copyISite2InfoSite(site));
            component.setExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                    .newInfoFromDomain(executionCourse));

            component.setLessonTypes(lessonTypes);
            List infoShiftsOnlyType = infoShifts;
            if (component.getLessonType() != null
                    && component.getLessonType().intValue() != 0) {
                final TipoAula lessonTypeSelect = new TipoAula(component
                        .getLessonType().intValue());
                infoShiftsOnlyType = (List) CollectionUtils.select(infoShifts,
                        new Predicate() {

                            public boolean evaluate(Object arg0) {
                                return ((InfoShift) arg0).getTipo().equals(
                                        lessonTypeSelect);
                            }
                        });
            }
            component.setInfoShifts(infoShiftsOnlyType);
            component.setInfoProfessorships(infoProfessorships);

            return component;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

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
        if (executionCourse.getLabHours() != null
                && executionCourse.getLabHours().intValue() > 0) {
            lessonTypes.add(new TipoAula(4));
        }

        return lessonTypes;
    }

    private List findLesson(IPersistentSummary persistentSummary,
            IExecutionCourse executionCourse, ITurno shift)
            throws ExcepcaoPersistencia {

        List summariesByExecutionCourse = persistentSummary
                .readByExecutionCourse(executionCourse);

        //copy the list
        List summariesByShift = new ArrayList();
        summariesByShift.addAll(summariesByExecutionCourse);

        if (summariesByExecutionCourse != null
                && summariesByExecutionCourse.size() > 0) {
            ListIterator iterator = summariesByExecutionCourse.listIterator();
            while (iterator.hasNext()) {
                ISummary summary = (ISummary) iterator.next();

                Calendar dateAndHourSummary = Calendar.getInstance();
                dateAndHourSummary.set(Calendar.DAY_OF_MONTH, summary
                        .getSummaryDate().get(Calendar.DAY_OF_MONTH));
                dateAndHourSummary.set(Calendar.MONTH, summary.getSummaryDate()
                        .get(Calendar.MONTH));
                dateAndHourSummary.set(Calendar.YEAR, summary.getSummaryDate()
                        .get(Calendar.YEAR));
                dateAndHourSummary.set(Calendar.HOUR_OF_DAY, summary
                        .getSummaryHour().get(Calendar.HOUR_OF_DAY));
                dateAndHourSummary.set(Calendar.MINUTE, summary
                        .getSummaryHour().get(Calendar.MINUTE));
                dateAndHourSummary.set(Calendar.SECOND, 00);

                Calendar beginLesson = Calendar.getInstance();
                beginLesson.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate()
                        .get(Calendar.DAY_OF_MONTH));
                beginLesson.set(Calendar.MONTH, summary.getSummaryDate().get(
                        Calendar.MONTH));
                beginLesson.set(Calendar.YEAR, summary.getSummaryDate().get(
                        Calendar.YEAR));

                Calendar endLesson = Calendar.getInstance();
                endLesson.set(Calendar.DAY_OF_MONTH, summary.getSummaryDate()
                        .get(Calendar.DAY_OF_MONTH));
                endLesson.set(Calendar.MONTH, summary.getSummaryDate().get(
                        Calendar.MONTH));
                endLesson.set(Calendar.YEAR, summary.getSummaryDate().get(
                        Calendar.YEAR));

                boolean removeSummary = true;
                if (shift.getAssociatedLessons() != null
                        && shift.getAssociatedLessons().size() > 0) {
                    ListIterator iterLesson = shift.getAssociatedLessons()
                            .listIterator();
                    while (iterLesson.hasNext()) {
                        IAula lesson = (IAula) iterLesson.next();

                        beginLesson.set(Calendar.HOUR_OF_DAY, lesson
                                .getInicio().get(Calendar.HOUR_OF_DAY));
                        beginLesson.set(Calendar.MINUTE, lesson.getInicio()
                                .get(Calendar.MINUTE));
                        beginLesson.set(Calendar.SECOND, 00);

                        endLesson.set(Calendar.HOUR_OF_DAY, lesson.getFim()
                                .get(Calendar.HOUR_OF_DAY));
                        endLesson.set(Calendar.MINUTE, lesson.getFim().get(
                                Calendar.MINUTE));
                        endLesson.set(Calendar.SECOND, 00);

                        if (summary.getSummaryType().equals(shift.getTipo())
                                && dateAndHourSummary.get(Calendar.DAY_OF_WEEK) == lesson
                                        .getDiaSemana().getDiaSemana()
                                        .intValue()
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

        if (summariesByExecutionCourse == null
                || summariesByExecutionCourse.size() <= 0) {
            return summaries;
        }

        List intersection = (List) CollectionUtils.intersection(summaries,
                summariesByExecutionCourse);

        allSummaries.addAll(CollectionUtils
                .disjunction(summaries, intersection));
        allSummaries.addAll(CollectionUtils.disjunction(
                summariesByExecutionCourse, intersection));
        allSummaries.addAll(intersection);

        if (allSummaries == null) {
            return new ArrayList();
        }
        return allSummaries;
    }

    /**
     * @param evaluation
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component,
            ISite site) {
        IExecutionCourse executionCourse = site.getExecutionCourse();
        List evaluations = executionCourse.getAssociatedEvaluations();
        List infoEvaluations = new ArrayList();
        Iterator iter = evaluations.iterator();
        while (iter.hasNext()) {
            IEvaluation evaluation = (IEvaluation) iter.next();
            infoEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
        }

        component.setInfoEvaluations(infoEvaluations);
        return component;
    }

    /**
     * @param common
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteCommon(InfoSiteCommon component,
            ISite site) throws FenixServiceException {

        ISuportePersistente sp;
        List allSections = null;
        List infoSectionsList = null;

        List infoCurricularCourseList = null;
        try {
            // read sections

            sp = SuportePersistenteOJB.getInstance();
            allSections = sp.getIPersistentSection().readBySite(site);

            // build the result of this service
            Iterator iterator = allSections.iterator();
            infoSectionsList = new ArrayList(allSections.size());

            while (iterator.hasNext()) {
                infoSectionsList
                        .add(copyISection2InfoSection((ISection) iterator
                                .next()));
            }
            Collections.sort(infoSectionsList);

            // read degrees

            IExecutionCourse executionCourse = site.getExecutionCourse();

            infoCurricularCourseList = readCurricularCourses(executionCourse);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        component.setAssociatedDegrees(infoCurricularCourseList);
        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);
        InfoExecutionCourse executionCourse;
        executionCourse = InfoExecutionCourseWithExecutionPeriod
                .newInfoFromDomain(site.getExecutionCourse());
        component.setExecutionCourse(executionCourse);
        return component;
    }

    /**
     * @param section
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteSection(InfoSiteSection component,
            ISite site, InfoSiteCommon commonComponent, Integer sectionIndex)
            throws FenixServiceException {
        List sections = commonComponent.getSections();
        InfoSection infoSection = (InfoSection) sections.get(sectionIndex
                .intValue());
        component.setSection(infoSection);
        List itemsList = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();
            ISection section = Cloner.copyInfoSection2ISection(infoSection);
            itemsList = persistentItem.readAllItemsBySection(section);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        List infoItemsList = new ArrayList(itemsList.size());
        Iterator iter = itemsList.iterator();
        IFileSuport fileSuport = FileSuport.getInstance();

        while (iter.hasNext()) {
            IItem item = (IItem) iter.next();
            InfoItem infoItem = InfoItem.newInfoFromDomain(item);
            try {
                infoItem.setLinks(CMSUtils.getItemLinks(fileSuport, item
                        .getSlideName()));
            } catch (SlideException e1) {
                //the item does not have a folder associated
            }
            infoItemsList.add(infoItem);
        }

        Collections.sort(infoItemsList);
        component.setItems(infoItemsList);
        return component;
    }

    /**
     * @param shifts
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteShifts(InfoSiteShifts component,
            ISite site) throws FenixServiceException {
        List shiftsWithAssociatedClassesAndLessons = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse disciplinaExecucao = site.getExecutionCourse();
            List shifts = sp.getITurnoPersistente().readByExecutionCourse(
                    disciplinaExecucao);

            if (shifts == null || shifts.isEmpty()) {

            } else {

                for (int i = 0; i < shifts.size(); i++) {
                    ITurno shift = (ITurno) shifts.get(i);
                    InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons = new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
                            InfoShiftWithInfoExecutionCourseAndCollections
                                    .newInfoFromDomain(shift), null, null);

        
                    //List lessons = sp.getITurnoAulaPersistente().readByShift(shift);
					List lessons = shift.getAssociatedLessons();
                    List infoLessons = new ArrayList();
                    List classesShifts = sp.getITurmaTurnoPersistente()
                            .readClassesWithShift(shift);
                    List infoClasses = new ArrayList();

                    for (int j = 0; j < lessons.size(); j++)
          {
                    	InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
                    	InfoLesson infoLesson = Cloner.copyILesson2InfoLesson((IAula) lessons.get(j)); 
                        infoLesson.setInfoShift(infoShift);
                        infoLessons.add(infoLesson);
                    }
                    shiftWithAssociatedClassesAndLessons.setInfoLessons(infoLessons);

                    for (int j = 0; j < classesShifts.size(); j++)
                        infoClasses.add(InfoClassWithInfoExecutionDegree
                                .newInfoFromDomain(((ITurmaTurno) classesShifts
                                        .get(j)).getTurma()));

                    shiftWithAssociatedClassesAndLessons
                            .setInfoClasses(infoClasses);

                    shiftsWithAssociatedClassesAndLessons
                            .add(shiftWithAssociatedClassesAndLessons);
                }
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        component.setShifts(shiftsWithAssociatedClassesAndLessons);
        component.setInfoExecutionPeriodName(site.getExecutionCourse()
                .getExecutionPeriod().getName());
        component.setInfoExecutionYearName(site.getExecutionCourse()
                .getExecutionPeriod().getExecutionYear().getYear());
        return component;
    }

    /**
     * @param timetable
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component,
            ISite site) throws FenixServiceException {
        ArrayList infoLessonList = null;
        
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = site.getExecutionCourse();

//                 List aulas = sp.getIAulaPersistente().readByExecutionCourse(executionCourse);
			List aulas = new ArrayList();

			List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);
			for (int i=0; i < shifts.size(); i++)
            {                
             	ITurno shift = (ITurno) shifts.get(i);
             	List aulasTemp = sp.getIAulaPersistente().readLessonsByShift(shift);
             	
             	aulas.addAll(aulasTemp);  
            }
			
            Iterator iterator = aulas.iterator();
            infoLessonList = new ArrayList();
            while (iterator.hasNext()) {
                IAula elem = (IAula) iterator.next();
//                InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoExecutionCourse
//                        .newInfoFromDomain(elem);
                InfoLesson infoLesson = copyILesson2InfoLesson(elem);
                
                infoLessonList.add(infoLesson);
                                
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        component.setLessons(infoLessonList);
        return component;
    }

    private InfoLesson copyILesson2InfoLesson(IAula lesson) {
        InfoLesson infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLesson();
            infoLesson.setIdInternal(lesson.getIdInternal());
            infoLesson.setDiaSemana(lesson.getDiaSemana());
            infoLesson.setFim(lesson.getFim());
            infoLesson.setInicio(lesson.getInicio());
            infoLesson.setTipo(lesson.getTipo());
            //infoLesson.setInfoSala(copyISala2InfoRoom(lesson.getSala()));  
            
            InfoRoomOccupation infoRoomOccupation = Cloner.copyIRoomOccupation2InfoRoomOccupation(lesson.getRoomOccupation());
            infoLesson.setInfoRoomOccupation(infoRoomOccupation);
            
			ITurno shift = lesson.getShift();
			InfoShift infoShift = Cloner.copyShift2InfoShift(shift);
			infoLesson.setInfoShift(infoShift);
        }
        return infoLesson;
    }
    /**
     * @param courses
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteAssociatedCurricularCourses(
            InfoSiteAssociatedCurricularCourses component, ISite site) {
        List infoCurricularCourseList = new ArrayList();

        IExecutionCourse executionCourse = site.getExecutionCourse();

        infoCurricularCourseList = readCurricularCourses(executionCourse);

        component.setAssociatedCurricularCourses(infoCurricularCourseList);
        return component;
    }

    /**
     * @param bibliography
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteBibliography(
            InfoSiteBibliography component, ISite site)
            throws FenixServiceException {
        List references = null;
        List infoBibRefs = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentBibliographicReference persistentBibliographicReference = persistentBibliographicReference = sp
                    .getIPersistentBibliographicReference();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            references = persistentBibliographicReference
                    .readBibliographicReference(executionCourse);

            Iterator iterator = references.iterator();
            infoBibRefs = new ArrayList();
            while (iterator.hasNext()) {
                IBibliographicReference bibRef = (IBibliographicReference) iterator
                        .next();

                InfoBibliographicReference infoBibRef = copyFromDomain(bibRef);
                infoBibRefs.add(infoBibRef);

            }
            if (!infoBibRefs.isEmpty()) {
                component.setBibliographicReferences(infoBibRefs);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return component;
    }

    /**
     * @param evaluation
     * @param site
     * @return
     */
    private ISiteComponent getInfoEvaluationMethod(
            InfoEvaluationMethod component, ISite site)
            throws FenixServiceException {
        try {
            IExecutionCourse executionCourse = site.getExecutionCourse();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentEvaluationMethod persistentEvaluationMethod = sp
                    .getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                    .readByIdExecutionCourse(executionCourse);
            if (evaluationMethod != null) {
                component = copyFromDomain(evaluationMethod);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return component;
    }

    /**
     * @param page
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteFirstPage(InfoSiteFirstPage component,
            ISite site) throws FenixServiceException {
        try {
            ISuportePersistente persistentSupport = SuportePersistenteOJB
                    .getInstance();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            InfoAnnouncement infoAnnouncement = readLastAnnouncement(
                    persistentSupport, executionCourse);

            List responsibleInfoTeachersList = readResponsibleTeachers(
                    persistentSupport, executionCourse);

            List lecturingInfoTeachersList = readLecturingTeachers(
                    persistentSupport, executionCourse);

            //set all the required information to the component
            component.setLastAnnouncement(infoAnnouncement);
            component.setAlternativeSite(site.getAlternativeSite());
            component.setInitialStatement(site.getInitialStatement());
            component.setIntroduction(site.getIntroduction());
            component.setSiteIdInternal(site.getIdInternal());
            if (!responsibleInfoTeachersList.isEmpty()) {
                component.setResponsibleTeachers(responsibleInfoTeachersList);
            } else {
                responsibleInfoTeachersList = new ArrayList();
            }
            lecturingInfoTeachersList.removeAll(responsibleInfoTeachersList);
            if (!lecturingInfoTeachersList.isEmpty()) {
                component.setLecturingTeachers(lecturingInfoTeachersList);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return component;
    }

    private InfoSiteAnnouncement getInfoSiteAnnouncement(
            InfoSiteAnnouncement component, ISite site)
            throws FenixServiceException {
        try {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            List announcementsList = sp.getIPersistentAnnouncement()
                    .readAnnouncementsBySite(site);
            List infoAnnouncementsList = new ArrayList();

            if (announcementsList != null
                    && announcementsList.isEmpty() == false) {
                Iterator iterAnnouncements = announcementsList.iterator();
                while (iterAnnouncements.hasNext()) {
                    IAnnouncement announcement = (IAnnouncement) iterAnnouncements
                            .next();
                    infoAnnouncementsList.add(copyFromDomain(announcement));
                }
            }

            Collections.sort(infoAnnouncementsList, new ComparatorChain(
                    new BeanComparator("lastModifiedDate"), true));
            component.setAnnouncements(infoAnnouncementsList);
            return component;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

    private List readLecturingTeachers(ISuportePersistente persistentSupport,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        List domainLecturingTeachersList = null;
        IPersistentProfessorship persistentProfessorship = persistentSupport
                .getIPersistentProfessorship();
        domainLecturingTeachersList = persistentProfessorship
                .readByExecutionCourse(executionCourse);

        List lecturingInfoTeachersList = new ArrayList();
        if (domainLecturingTeachersList != null) {

            Iterator iter = domainLecturingTeachersList.iterator();
            while (iter.hasNext()) {
                IProfessorship professorship = (IProfessorship) iter.next();
                ITeacher teacher = professorship.getTeacher();
                InfoTeacher infoTeacher = InfoTeacherWithPerson
                        .newInfoFromDomain(teacher);
                lecturingInfoTeachersList.add(infoTeacher);
            }
        }
        return lecturingInfoTeachersList;
    }

    private List readResponsibleTeachers(ISuportePersistente persistentSupport,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        List responsibleDomainTeachersList = null;

        IPersistentResponsibleFor persistentResponsibleFor = persistentSupport
                .getIPersistentResponsibleFor();
        responsibleDomainTeachersList = persistentResponsibleFor
                .readByExecutionCourse(executionCourse);

        List responsibleInfoTeachersList = new ArrayList();
        if (responsibleDomainTeachersList != null) {
            Iterator iter = responsibleDomainTeachersList.iterator();
            while (iter.hasNext()) {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                ITeacher teacher = responsibleFor.getTeacher();
                InfoTeacher infoTeacher = InfoTeacherWithPerson
                        .newInfoFromDomain(teacher);
                responsibleInfoTeachersList.add(infoTeacher);
            }

        }
        return responsibleInfoTeachersList;
    }

    private InfoAnnouncement readLastAnnouncement(
            ISuportePersistente persistentSupport,
            IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        ISite site = persistentSupport.getIPersistentSite()
                .readByExecutionCourse(executionCourse);
        IAnnouncement announcement = persistentSupport
                .getIPersistentAnnouncement().readLastAnnouncementForSite(site);
        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null) {
            infoAnnouncement = copyFromDomain(announcement);
        }
        return infoAnnouncement;
    }

    private List readCurricularCourses(IExecutionCourse executionCourse) {
        List infoCurricularCourseScopeList;
        List infoCurricularCourseList = new ArrayList();
        if (executionCourse.getAssociatedCurricularCourses() != null)
            for (int i = 0; i < executionCourse
                    .getAssociatedCurricularCourses().size(); i++) {
                ICurricularCourse curricularCourse = (ICurricularCourse) executionCourse
                        .getAssociatedCurricularCourses().get(i);
                InfoCurricularCourse infoCurricularCourse = copyFromDomain(curricularCourse);
                infoCurricularCourseScopeList = new ArrayList();
                for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
                    ICurricularCourseScope curricularCourseScope = (ICurricularCourseScope) curricularCourse
                            .getScopes().get(j);
                    InfoCurricularCourseScope infoCurricularCourseScope = copyFromDomain(curricularCourseScope);
                    infoCurricularCourseScopeList
                            .add(infoCurricularCourseScope);
                }

                infoCurricularCourse
                        .setInfoScopes(infoCurricularCourseScopeList);
                infoCurricularCourseList.add(infoCurricularCourse);

            }
        return infoCurricularCourseList;
    }

    /**
     * @param section
     * @return
     */
    private InfoSection copyISection2InfoSection(ISection section) {
        InfoSection infoSection = null;
        if (section != null) {
            infoSection = new InfoSection();
            infoSection.setIdInternal(section.getIdInternal());
            infoSection.setName(section.getName());
            infoSection.setSectionOrder(section.getSectionOrder());
            infoSection.setSuperiorInfoSection(copyISection2InfoSection(section
                    .getSuperiorSection()));
            infoSection.setInfoSite(copyISite2InfoSite(section.getSite()));
        }
        return infoSection;
    }

    /**
     * @param site
     * @return
     */
    private InfoSite copyISite2InfoSite(ISite site) {
        InfoSite infoSite = null;
        if (site != null) {
            infoSite = new InfoSite();
            infoSite.setIdInternal(site.getIdInternal());
            infoSite.setAlternativeSite(site.getAlternativeSite());
            infoSite.setInitialStatement(site.getInitialStatement());
            infoSite.setIntroduction(site.getIntroduction());
            infoSite.setMail(site.getMail());
            infoSite.setStyle(site.getStyle());
            infoSite
                    .setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
                            .newInfoFromDomain(site.getExecutionCourse()));
        }
        return infoSite;
    }

    /**
     * @param announcement
     * @return
     */
    private InfoAnnouncement copyFromDomain(IAnnouncement announcement) {
        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null) {
            infoAnnouncement = new InfoAnnouncement();
            infoAnnouncement.setIdInternal(announcement.getIdInternal());
            infoAnnouncement.setCreationDate(announcement.getCreationDate());
            infoAnnouncement.setInformation(announcement.getInformation());
            infoAnnouncement.setLastModifiedDate(announcement
                    .getLastModifiedDate());
            infoAnnouncement.setTitle(announcement.getTitle());
        }
        return infoAnnouncement;
    }

    /**
     * @param evaluationMethod
     * @return
     */
    private InfoEvaluationMethod copyFromDomain(
            IEvaluationMethod evaluationMethod) {
        InfoEvaluationMethod infoEvaluationMethod = null;
        if (evaluationMethod != null) {
            infoEvaluationMethod = new InfoEvaluationMethod();
            infoEvaluationMethod
                    .setIdInternal(evaluationMethod.getIdInternal());
            infoEvaluationMethod.setEvaluationElements(evaluationMethod
                    .getEvaluationElements());
            infoEvaluationMethod.setEvaluationElementsEn(evaluationMethod
                    .getEvaluationElementsEn());
        }
        return infoEvaluationMethod;
    }

    /**
     * @param bibRef
     * @return
     */
    private InfoBibliographicReference copyFromDomain(
            IBibliographicReference bibRef) {
        InfoBibliographicReference infoBibliographicReference = null;
        if (bibRef != null) {
            infoBibliographicReference = new InfoBibliographicReference();
            infoBibliographicReference.setAuthors(bibRef.getAuthors());
            infoBibliographicReference.setIdInternal(bibRef.getIdInternal());
            infoBibliographicReference.setOptional(bibRef.getOptional());
            infoBibliographicReference.setReference(bibRef.getReference());
            infoBibliographicReference.setTitle(bibRef.getTitle());
            infoBibliographicReference.setYear(bibRef.getYear());

        }
        return infoBibliographicReference;
    }

    /**
     * @param curricularCourseScope
     * @return
     */
    private InfoCurricularCourseScope copyFromDomain(
            ICurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScope infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScope();
            infoCurricularCourseScope.setIdInternal(curricularCourseScope
                    .getIdInternal());
            infoCurricularCourseScope.setBeginDate(curricularCourseScope
                    .getBeginDate());
            infoCurricularCourseScope.setEndDate(curricularCourseScope
                    .getEndDate());

        }
        return infoCurricularCourseScope;
    }

    /**
     * @param curricularCourse
     * @return
     */
    private InfoCurricularCourse copyFromDomain(
            ICurricularCourse curricularCourse) {
        InfoCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourse();
            infoCurricularCourse
                    .setIdInternal(curricularCourse.getIdInternal());
            infoCurricularCourse.setBasic(curricularCourse.getBasic());
            infoCurricularCourse.setCode(curricularCourse.getCode());
            infoCurricularCourse.setCredits(curricularCourse.getCredits());
            infoCurricularCourse.setEctsCredits(curricularCourse
                    .getEctsCredits());
            infoCurricularCourse.setEnrollmentWeigth(curricularCourse
                    .getEnrollmentWeigth());
            infoCurricularCourse.setLabHours(curricularCourse.getLabHours());
            infoCurricularCourse.setMandatory(curricularCourse.getMandatory());
            infoCurricularCourse.setMandatoryEnrollment(curricularCourse
                    .getMandatoryEnrollment());
            infoCurricularCourse
                    .setMaximumValueForAcumulatedEnrollments(curricularCourse
                            .getMaximumValueForAcumulatedEnrollments());
            infoCurricularCourse
                    .setMinimumValueForAcumulatedEnrollments(curricularCourse
                            .getMinimumValueForAcumulatedEnrollments());
            infoCurricularCourse.setName(curricularCourse.getName());
            infoCurricularCourse.setPraticalHours(curricularCourse
                    .getPraticalHours());
            infoCurricularCourse.setTheoPratHours(curricularCourse
                    .getTheoPratHours());
            infoCurricularCourse.setTheoreticalHours(curricularCourse
                    .getTheoreticalHours());
            infoCurricularCourse.setType(curricularCourse.getType());
            infoCurricularCourse.setWeigth(curricularCourse.getWeigth());
            infoCurricularCourse
                    .setInfoDegreeCurricularPlan(InfoDegreeCurricularPlanWithDegree
                            .newInfoFromDomain(curricularCourse
                                    .getDegreeCurricularPlan()));
        }
        return infoCurricularCourse;
    }

}