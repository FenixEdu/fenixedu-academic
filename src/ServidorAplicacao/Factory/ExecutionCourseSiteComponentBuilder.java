/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package ServidorAplicacao.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.slide.common.SlideException;

import DataBeans.ISiteComponent;
import DataBeans.InfoAnnouncement;
import DataBeans.InfoBibliographicReference;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurriculum;
import DataBeans.InfoEvaluationMethod;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoItem;
import DataBeans.InfoLesson;
import DataBeans.InfoLink;
import DataBeans.InfoSection;
import DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import DataBeans.InfoSiteAnnouncement;
import DataBeans.InfoSiteAssociatedCurricularCourses;
import DataBeans.InfoSiteBibliography;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteCurricularCourse;
import DataBeans.InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses;
import DataBeans.InfoSiteEvaluation;
import DataBeans.InfoSiteEvaluationMethods;
import DataBeans.InfoSiteExam;
import DataBeans.InfoSiteFirstPage;
import DataBeans.InfoSiteObjectives;
import DataBeans.InfoSiteProgram;
import DataBeans.InfoSiteSection;
import DataBeans.InfoSiteShifts;
import DataBeans.InfoSiteSummaries;
import DataBeans.InfoSiteTimetable;
import DataBeans.InfoSummary;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.CurricularCourse;
import Dominio.IAnnouncement;
import Dominio.IAula;
import Dominio.IBibliographicReference;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.ICurriculum;
import Dominio.IExecutionCourse;
import Dominio.IEvaluation;
import Dominio.IEvaluationMethod;
import Dominio.IExam;
import Dominio.IItem;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ISection;
import Dominio.ISite;
import Dominio.ISummary;
import Dominio.ITeacher;
import Dominio.ITurmaTurno;
import Dominio.ITurno;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentBibliographicReference;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentCurriculum;
import ServidorPersistente.IPersistentEvaluationMethod;
import ServidorPersistente.IPersistentItem;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentResponsibleFor;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import fileSuport.FileSuport;
import fileSuport.FileSuportObject;
import fileSuport.IFileSuport;

/**
 * @author João Mota
 * 
 *  
 */
public class ExecutionCourseSiteComponentBuilder
{

    private static ExecutionCourseSiteComponentBuilder instance = null;

    public ExecutionCourseSiteComponentBuilder()
    {
    }

    public static ExecutionCourseSiteComponentBuilder getInstance()
    {
        if (instance == null)
        {
            instance = new ExecutionCourseSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(
        ISiteComponent component,
        ISite site,
        ISiteComponent commonComponent,
        Integer sectionIndex,
        Integer curricularCourseId)
        throws FenixServiceException
    {

        // updateSite(executionYearName, executionPeriodName,
        // executionCourseName);

        if (component instanceof InfoSiteCommon)
        {
            return getInfoSiteCommon((InfoSiteCommon) component, site);
        }
        else if (component instanceof InfoSiteFirstPage)
        {
            return getInfoSiteFirstPage((InfoSiteFirstPage) component, site);

        }
        else if (component instanceof InfoSiteAnnouncement)
        {
            return getInfoSiteAnnouncement((InfoSiteAnnouncement) component, site);
        }
        else if (component instanceof InfoSiteObjectives)
        {
            return getInfoSiteObjectives((InfoSiteObjectives) component, site);
        }
        else if (component instanceof InfoSiteProgram)
        {
            return getInfoSiteProgram((InfoSiteProgram) component, site);
        }
        else if (component instanceof InfoEvaluationMethod)
        {
            return getInfoEvaluationMethod((InfoEvaluationMethod) component, site);
        }
        else if (component instanceof InfoSiteEvaluationMethods)
        {
            return getInfoEvaluation((InfoSiteEvaluationMethods) component, site);
        }
        else if (component instanceof InfoSiteBibliography)
        {
            return getInfoSiteBibliography((InfoSiteBibliography) component, site);
        }
        else if (component instanceof InfoSiteAssociatedCurricularCourses)
        {
            return getInfoSiteAssociatedCurricularCourses(
                (InfoSiteAssociatedCurricularCourses) component,
                site);
        }
        else if (component instanceof InfoSiteTimetable)
        {
            return getInfoSiteTimetable((InfoSiteTimetable) component, site);
        }
        else if (component instanceof InfoSiteShifts)
        {
            return getInfoSiteShifts((InfoSiteShifts) component, site);

        }
        else if (component instanceof InfoSiteSection)
        {
            return getInfoSiteSection(
                (InfoSiteSection) component,
                site,
                (InfoSiteCommon) commonComponent,
                sectionIndex);
        }
        else if (component instanceof InfoSiteExam)
        {
            return getInfoSiteExam((InfoSiteExam) component, site);
        }
        else if (component instanceof InfoSiteEvaluation)
        {
            return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
        }
        else if (component instanceof InfoSiteSummaries)
        {
            return getInfoSiteSummaries((InfoSiteSummaries) component, site);
        }
        else if (component instanceof InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses)
        {
            return getInfoSiteCurricularCoursesAndAssociatedShiftsAndClasses(
                (InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses) component,
                site);
        }
        else if (component instanceof InfoSiteCurricularCourse)
        {
            return getInfoSiteCurricularCourse(
                (InfoSiteCurricularCourse) component,
                site,
                curricularCourseId);
        }
        return null;
    }

    /**
     * @param course
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteCurricularCourse(
        InfoSiteCurricularCourse component,
        ISite site,
        Integer curricularCourseId)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICurricularCourse curricularCourse = new CurricularCourse(curricularCourseId);
            IPersistentCurricularCourse persistentCurricularCourse = sp.getIPersistentCurricularCourse();
            curricularCourse =
                (ICurricularCourse) persistentCurricularCourse.readByOId(curricularCourse, false);
            if (curricularCourse == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentCurriculum persistentCurriculum = sp.getIPersistentCurriculum();
            ICurriculum curriculum =
                persistentCurriculum.readCurriculumByCurricularCourse(curricularCourse);
            InfoCurriculum infoCurriculum = null;
            if (curriculum != null)
            {
                infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
            }
            component.setInfoCurriculum(infoCurriculum);
            InfoCurricularCourse infoCurricularCourse =
                Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
            List infoCurricularCourseScopes = new ArrayList();
            List curricularCourseScopes = curricularCourse.getScopes();
            Iterator iter = curricularCourseScopes.iterator();
            while (iter.hasNext())
            {
                ICurricularCourseScope scope = (ICurricularCourseScope) iter.next();
                InfoCurricularCourseScope infoScope =
                    Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(scope);
                infoCurricularCourseScopes.add(infoScope);

            }
            infoCurricularCourse.setInfoScopes(infoCurricularCourseScopes);
            component.setInfoCurricularCourse(infoCurricularCourse);

            return component;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

    /**
     * @param summaries
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteSummaries(InfoSiteSummaries component, ISite site)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();
            List summaries = null;
            if (component.getSummaryType() == null)
            {
                summaries = persistentSummary.readByExecutionCourse(executionCourse);
            }
            else
            {
                summaries =
                    persistentSummary.readByExecutionCourseAndType(
                        executionCourse,
                        component.getSummaryType());
            }

            List result = new ArrayList();
            Iterator iter = summaries.iterator();
            while (iter.hasNext())
            {
                ISummary summary = (ISummary) iter.next();
                InfoSummary infoSummary = Cloner.copyISummary2InfoSummary(summary);
                result.add(infoSummary);
            }

            component.setInfoSummaries(result);
            component.setInfoSite(Cloner.copyISite2InfoSite(site));
            component.setExecutionCourse(
                (InfoExecutionCourse) Cloner.get(executionCourse));

            return component;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

    }

    /**
     * @param evaluation
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component, ISite site)
    {
        IExecutionCourse executionCourse = site.getExecutionCourse();
        List evaluations = executionCourse.getAssociatedEvaluations();
        List infoEvaluations = new ArrayList();
        Iterator iter = evaluations.iterator();
        while (iter.hasNext())
        {
            IEvaluation evaluation = (IEvaluation) iter.next();
            infoEvaluations.add(Cloner.copyIEvaluation2InfoEvaluation(evaluation));
        }

        component.setInfoEvaluations(infoEvaluations);
        return component;
    }

    /**
     * @param exam
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteExam(InfoSiteExam component, ISite site)
    {

        IExecutionCourse executionCourse = site.getExecutionCourse();
        List exams = executionCourse.getAssociatedExams();
        List infoExams = new ArrayList();
        Iterator iter = exams.iterator();
        while (iter.hasNext())
        {
            IExam exam = (IExam) iter.next();
            infoExams.add(Cloner.copyIExam2InfoExam(exam));
        }
        component.setInfoExams(infoExams);
        return component;
    }

    /**
     * @param common
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, ISite site)
        throws FenixServiceException
    {

        ISuportePersistente sp;
        List allSections = null;
        List infoSectionsList = null;

        List infoCurricularCourseList = null;
        try
        {
            // read sections

            sp = SuportePersistenteOJB.getInstance();
            allSections = sp.getIPersistentSection().readBySite(site);

            // build the result of this service
            Iterator iterator = allSections.iterator();
            infoSectionsList = new ArrayList(allSections.size());

            while (iterator.hasNext())
                infoSectionsList.add(Cloner.copyISection2InfoSection((ISection) iterator.next()));

            Collections.sort(infoSectionsList);

            // read degrees

            IExecutionCourse executionCourse = site.getExecutionCourse();

            infoCurricularCourseList = readCurricularCourses(executionCourse);

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
        component.setAssociatedDegrees(infoCurricularCourseList);
        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);
        InfoExecutionCourse executionCourse;
        executionCourse = (InfoExecutionCourse) Cloner.get(site.getExecutionCourse());
        component.setExecutionCourse(executionCourse);
        return component;
    }

    /**
     * @param section
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteSection(
        InfoSiteSection component,
        ISite site,
        InfoSiteCommon commonComponent,
        Integer sectionIndex)
        throws FenixServiceException
    {

        List sections = commonComponent.getSections();
        InfoSection infoSection = (InfoSection) sections.get(sectionIndex.intValue());
        component.setSection(infoSection);
        List itemsList = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentItem persistentItem = sp.getIPersistentItem();

            ISection section = Cloner.copyInfoSection2ISection(infoSection);

            itemsList = persistentItem.readAllItemsBySection(section);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        List infoItemsList = new ArrayList(itemsList.size());
        Iterator iter = itemsList.iterator();

        IFileSuport fileSuport = FileSuport.getInstance();
        while (iter.hasNext())
        {
            IItem item = (IItem) iter.next();
            InfoItem infoItem = Cloner.copyIItem2InfoItem(item);
            try
            {
                List files = fileSuport.getDirectoryFiles(item.getSlideName());
                if (files != null && !files.isEmpty())
                {
                    List links = new ArrayList();
                    Iterator iterFiles = files.iterator();
                    while (iterFiles.hasNext())
                    {
                        FileSuportObject file = (FileSuportObject) iterFiles.next();
                        InfoLink infoLink = new InfoLink();
                        infoLink.setLink(file.getFileName());
                        infoLink.setLinkName(file.getLinkName());
                        links.add(infoLink);
                    }
                    infoItem.setLinks(links);
                }
            }
            catch (SlideException e1)
            {
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
    private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, ISite site)
        throws FenixServiceException
    {
        List shiftsWithAssociatedClassesAndLessons = new ArrayList();

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse disciplinaExecucao = site.getExecutionCourse();
            List shifts = sp.getITurnoPersistente().readByExecutionCourse(disciplinaExecucao);

            if (shifts == null || shifts.isEmpty())
            {

            }
            else
            {

                for (int i = 0; i < shifts.size(); i++)
                {
                    ITurno shift = (ITurno) shifts.get(i);
                    InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons =
                        new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
                            Cloner.copyShift2InfoShift(shift),
                            null,
                            null);

                    List lessons = sp.getITurnoAulaPersistente().readByShift(shift);
                    List infoLessons = new ArrayList();
                    List classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
                    List infoClasses = new ArrayList();

                    for (int j = 0; j < lessons.size(); j++)
                        infoLessons.add(Cloner.copyILesson2InfoLesson((IAula) lessons.get(j)));

                    shiftWithAssociatedClassesAndLessons.setInfoLessons(infoLessons);

                    for (int j = 0; j < classesShifts.size(); j++)
                        infoClasses.add(
                            Cloner.copyClass2InfoClass(((ITurmaTurno) classesShifts.get(j)).getTurma()));

                    shiftWithAssociatedClassesAndLessons.setInfoClasses(infoClasses);

                    shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
                }
            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        component.setShifts(shiftsWithAssociatedClassesAndLessons);
        component.setInfoExecutionPeriodName(site.getExecutionCourse().getExecutionPeriod().getName());
        component.setInfoExecutionYearName(
            site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return component;
    }

    /**
     * @param timetable
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, ISite site)
        throws FenixServiceException
    {
        ArrayList infoLessonList = null;

        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            List aulas = sp.getIAulaPersistente().readByExecutionCourse(executionCourse);

            Iterator iterator = aulas.iterator();
            infoLessonList = new ArrayList();
            while (iterator.hasNext())
            {
                IAula elem = (IAula) iterator.next();
                InfoLesson infoLesson = Cloner.copyILesson2InfoLesson(elem);
                infoLessonList.add(infoLesson);
            }
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw new FenixServiceException(ex);
        }
        component.setLessons(infoLessonList);
        return component;
    }

    /**
     * @param courses
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteAssociatedCurricularCourses(
        InfoSiteAssociatedCurricularCourses component,
        ISite site)
    {
        List infoCurricularCourseList = new ArrayList();

        IExecutionCourse executionCourse = site.getExecutionCourse();

        infoCurricularCourseList = readCurricularCourses(executionCourse);

        component.setAssociatedCurricularCourses(infoCurricularCourseList);
        return component;
    }

    private List readCurricularCourses(IExecutionCourse executionCourse)
    {
        List infoCurricularCourseScopeList;
        List infoCurricularCourseList = new ArrayList();
        if (executionCourse.getAssociatedCurricularCourses() != null)
            for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++)
            {
                ICurricularCourse curricularCourse =
                    (ICurricularCourse) executionCourse.getAssociatedCurricularCourses().get(i);
                InfoCurricularCourse infoCurricularCourse =
                    Cloner.copyCurricularCourse2InfoCurricularCourse(curricularCourse);
                infoCurricularCourseScopeList = new ArrayList();
                for (int j = 0; j < curricularCourse.getScopes().size(); j++)
                {
                    ICurricularCourseScope curricularCourseScope =
                        (ICurricularCourseScope) curricularCourse.getScopes().get(j);
                    InfoCurricularCourseScope infoCurricularCourseScope =
                        Cloner.copyICurricularCourseScope2InfoCurricularCourseScope(
                            curricularCourseScope);
                    infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                }

                infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
                infoCurricularCourseList.add(infoCurricularCourse);

            }
        return infoCurricularCourseList;
    }

    /**
     * @param bibliography
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteBibliography(InfoSiteBibliography component, ISite site)
        throws FenixServiceException
    {
        List references = null;
        List infoBibRefs = null;
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentBibliographicReference persistentBibliographicReference =
                persistentBibliographicReference = sp.getIPersistentBibliographicReference();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            references = persistentBibliographicReference.readBibliographicReference(executionCourse);

            Iterator iterator = references.iterator();
            infoBibRefs = new ArrayList();
            while (iterator.hasNext())
            {
                IBibliographicReference bibRef = (IBibliographicReference) iterator.next();

                InfoBibliographicReference infoBibRef =
                    Cloner.copyIBibliographicReference2InfoBibliographicReference(bibRef);
                infoBibRefs.add(infoBibRef);

            }
            if (!infoBibRefs.isEmpty())
            {
                component.setBibliographicReferences(infoBibRefs);
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        return component;
    }

    /**
     * @param evaluation
     * @param site
     * @return
     */
    private ISiteComponent getInfoEvaluationMethod(InfoEvaluationMethod component, ISite site)
        throws FenixServiceException
    {
        try
        {
            IExecutionCourse executionCourse = site.getExecutionCourse();

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentEvaluationMethod persistentEvaluationMethod = sp.getIPersistentEvaluationMethod();
            IEvaluationMethod evaluationMethod = persistentEvaluationMethod
                    .readByIdExecutionCourse(executionCourse);
            if (evaluationMethod != null)
            {
                component = Cloner.copyIEvaluationMethod2InfoEvaluationMethod(evaluationMethod);
            }
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return component;
    }

    /**
     * @param evaluation
     * @param site
     * @return
     */
    private ISiteComponent getInfoEvaluation(InfoSiteEvaluationMethods component, ISite site)
        throws FenixServiceException
    {
        try
        {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = site.getExecutionCourse();
            List curricularCourses = executionCourse.getAssociatedCurricularCourses();

            Iterator iter = curricularCourses.iterator();
            List infoEvaluationMethods = new ArrayList();

            while (iter.hasNext())
            {
                ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
                ICurriculum curriculum =
                    sp.getIPersistentCurriculum().readCurriculumByCurricularCourse(curricularCourse);

                if (curriculum != null)
                {
                    infoEvaluationMethods.add(Cloner.copyICurriculum2InfoCurriculum(curriculum));
                }
            }

            component.setInfoEvaluations(infoEvaluationMethods);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return component;
    }

    /**
     * @param program
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteProgram(InfoSiteProgram component, ISite site)
        throws FenixServiceException
    {
        try
        {

            List curriculums = readCurriculum(site);
            Iterator iter = curriculums.iterator();
            List infoCurriculums = new ArrayList();
            while (iter.hasNext())
            {
                ICurriculum curriculum = (ICurriculum) iter.next();
                InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
                infoCurriculums.add(infoCurriculum);
            }
            component.setInfoCurriculums(infoCurriculums);
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return component;
    }

    /**
     * @param objectives
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteObjectives(InfoSiteObjectives component, ISite site)
        throws FenixServiceException
    {
        try
        {

            List curriculums = readCurriculum(site);
            Iterator iter = curriculums.iterator();
            List infoCurriculums = new ArrayList();
            while (iter.hasNext())
            {
                ICurriculum curriculum = (ICurriculum) iter.next();
                InfoCurriculum infoCurriculum = Cloner.copyICurriculum2InfoCurriculum(curriculum);
                infoCurriculums.add(infoCurriculum);
            }
            component.setInfoCurriculums(infoCurriculums);

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return component;
    }

    private List readCurriculum(ISite site) throws ExcepcaoPersistencia
    {

        IExecutionCourse executionCourse = site.getExecutionCourse();

        ISuportePersistente sp;

        sp = SuportePersistenteOJB.getInstance();
        List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Iterator iter = curricularCourses.iterator();
        List curriculums = new ArrayList();
        while (iter.hasNext())
        {
            ICurricularCourse curricularCourse = (ICurricularCourse) iter.next();
            ICurriculum curriculum = null;
            curriculum =
                sp.getIPersistentCurriculum().readCurriculumByCurricularCourse(curricularCourse);
            curriculums.add(curriculum);
        }

        return curriculums;
    }

    /**
     * @param page
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteFirstPage(InfoSiteFirstPage component, ISite site)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = site.getExecutionCourse();

            InfoAnnouncement infoAnnouncement = readLastAnnouncement(persistentSupport, executionCourse);

            List responsibleInfoTeachersList =
                readResponsibleTeachers(persistentSupport, executionCourse);

            List lecturingInfoTeachersList = readLecturingTeachers(persistentSupport, executionCourse);

            //set all the required information to the component
            component.setLastAnnouncement(infoAnnouncement);
            component.setAlternativeSite(site.getAlternativeSite());
            component.setInitialStatement(site.getInitialStatement());
            component.setIntroduction(site.getIntroduction());
            component.setSiteIdInternal(site.getIdInternal());
            if (!responsibleInfoTeachersList.isEmpty())
            {
                component.setResponsibleTeachers(responsibleInfoTeachersList);
            }
            else
            {
                responsibleInfoTeachersList = new ArrayList();
            }
            lecturingInfoTeachersList.removeAll(responsibleInfoTeachersList);
            if (!lecturingInfoTeachersList.isEmpty())
            {
                component.setLecturingTeachers(lecturingInfoTeachersList);
            }

        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return component;
    }

    private InfoSiteAnnouncement getInfoSiteAnnouncement(InfoSiteAnnouncement component, ISite site)
        throws FenixServiceException
    {
        try
        {

            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            List announcementsList = sp.getIPersistentAnnouncement().readAnnouncementsBySite(site);
            List infoAnnouncementsList = new ArrayList();

            if (announcementsList != null && announcementsList.isEmpty() == false)
            {
                Iterator iterAnnouncements = announcementsList.iterator();
                while (iterAnnouncements.hasNext())
                {
                    IAnnouncement announcement = (IAnnouncement) iterAnnouncements.next();
                    infoAnnouncementsList.add(Cloner.copyIAnnouncement2InfoAnnouncement(announcement));
                }
            }

            Collections.sort(infoAnnouncementsList, new ComparatorChain(new BeanComparator("lastModifiedDate"), true));
            component.setAnnouncements(infoAnnouncementsList);
            return component;
        }
        catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

    private List readLecturingTeachers(
        ISuportePersistente persistentSupport,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        List domainLecturingTeachersList = null;
        IPersistentProfessorship persistentProfessorship =
            persistentSupport.getIPersistentProfessorship();
        domainLecturingTeachersList = persistentProfessorship.readByExecutionCourse(executionCourse);

        List lecturingInfoTeachersList = new ArrayList();
        if (domainLecturingTeachersList != null)
        {

            Iterator iter = domainLecturingTeachersList.iterator();
            while (iter.hasNext())
            {
                IProfessorship professorship = (IProfessorship) iter.next();
                ITeacher teacher = professorship.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                lecturingInfoTeachersList.add(infoTeacher);
            }
        }
        return lecturingInfoTeachersList;
    }

    private List readResponsibleTeachers(
        ISuportePersistente persistentSupport,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        List responsibleDomainTeachersList = null;

        IPersistentResponsibleFor persistentResponsibleFor =
            persistentSupport.getIPersistentResponsibleFor();
        responsibleDomainTeachersList = persistentResponsibleFor.readByExecutionCourse(executionCourse);

        List responsibleInfoTeachersList = new ArrayList();
        if (responsibleDomainTeachersList != null)
        {
            Iterator iter = responsibleDomainTeachersList.iterator();
            while (iter.hasNext())
            {
                IResponsibleFor responsibleFor = (IResponsibleFor) iter.next();
                ITeacher teacher = responsibleFor.getTeacher();
                InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                responsibleInfoTeachersList.add(infoTeacher);
            }

        }
        return responsibleInfoTeachersList;
    }

    private InfoAnnouncement readLastAnnouncement(
        ISuportePersistente persistentSupport,
        IExecutionCourse executionCourse)
        throws ExcepcaoPersistencia
    {
        ISite site = persistentSupport.getIPersistentSite().readByExecutionCourse(executionCourse);
        IAnnouncement announcement =
            persistentSupport.getIPersistentAnnouncement().readLastAnnouncementForSite(site);
        InfoAnnouncement infoAnnouncement = null;
        if (announcement != null)
            infoAnnouncement = Cloner.copyIAnnouncement2InfoAnnouncement(announcement);
        return infoAnnouncement;
    }

    /**
     * Angela && Tânia
     *  
     */

    /**
     * @param common
     * @param site
     * @return
     */
    private InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses getInfoSiteCurricularCoursesAndAssociatedShiftsAndClasses(
        InfoSiteCurricularCoursesAndAssociatedShiftsAndClasses component,
        ISite site)
        throws FenixServiceException
    {

        ISuportePersistente sp;
        List infoShiftsWithAssociatedClassesList = new ArrayList();
        List infoCurricularCourseList = new ArrayList();
        List shifts = new ArrayList();

        InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons = null;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            // read degrees

            IExecutionCourse executionCourse = site.getExecutionCourse();
            infoCurricularCourseList = readCurricularCourses(executionCourse);

            // read shifts and classes
            shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);

            if (shifts == null || shifts.isEmpty())
            {

            }
            else
            {

                for (int i = 0; i < shifts.size(); i++)
                {
                    ITurno shift = (ITurno) shifts.get(i);
                    shiftWithAssociatedClassesAndLessons =
                        new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
                            Cloner.copyShift2InfoShift(shift),
                            null,
                            null);

                    List classesShifts = sp.getITurmaTurnoPersistente().readClassesWithShift(shift);
                    List infoClasses = new ArrayList();

                    for (int j = 0; j < classesShifts.size(); j++)
                        infoClasses.add(
                            Cloner.copyClass2InfoClass(((ITurmaTurno) classesShifts.get(j)).getTurma()));

                    shiftWithAssociatedClassesAndLessons.setInfoClasses(infoClasses);
                    infoShiftsWithAssociatedClassesList.add(shiftWithAssociatedClassesAndLessons);
                }
            }

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        component.setAssociatedCurricularCourses(infoCurricularCourseList);
        InfoExecutionCourse executionCourse;
        executionCourse = (InfoExecutionCourse) Cloner.get(site.getExecutionCourse());
        component.setInfoExecutionCourse(executionCourse);
        component.setInfoShiftsWithAssociatedClassesList(infoShiftsWithAssociatedClassesList);

        return component;

    }

}
