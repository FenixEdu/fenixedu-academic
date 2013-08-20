/*
 * Created on 5/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluations;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Teacher;
import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Jo�o Mota
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

    public ISiteComponent getComponent(ISiteComponent component, ExecutionCourseSite site, ISiteComponent commonComponent,
            Integer sectionIndex, Integer curricularCourseId) throws FenixServiceException {

        if (component instanceof InfoSiteCommon) {
            return getInfoSiteCommon((InfoSiteCommon) component, site);
        } else if (component instanceof InfoSiteFirstPage) {
            return getInfoSiteFirstPage((InfoSiteFirstPage) component, site);
        } else if (component instanceof InfoSiteAssociatedCurricularCourses) {
            return getInfoSiteAssociatedCurricularCourses((InfoSiteAssociatedCurricularCourses) component, site);
        } else if (component instanceof InfoSiteTimetable) {
            return getInfoSiteTimetable((InfoSiteTimetable) component, site);
        } else if (component instanceof InfoSiteShifts) {
            return getInfoSiteShifts((InfoSiteShifts) component, site);

        } else if (component instanceof InfoSiteSection) {
            return getInfoSiteSection((InfoSiteSection) component, site, (InfoSiteCommon) commonComponent, sectionIndex);
        } else if (component instanceof InfoSiteEvaluations) {
            return getInfoSiteEvaluations((InfoSiteEvaluations) component, site);
        } else if (component instanceof InfoSiteEvaluationMarks) {
            return getInfoSiteEvaluationMarks((InfoSiteEvaluationMarks) component, site);
        }
        return null;
    }

    private ISiteComponent getInfoSiteEvaluations(InfoSiteEvaluations component, ExecutionCourseSite site) {
        ExecutionCourse executionCourse = site.getExecutionCourse();
        List<Evaluation> evaluations = executionCourse.getAssociatedEvaluations();
        component.setEvaluations(evaluations);
        return component;
    }

    private ISiteComponent getInfoSiteEvaluationMarks(InfoSiteEvaluationMarks component, ExecutionCourseSite site) {
        final Integer evaluationID = component.getEvaluationID();

        final ExecutionCourse executionCourse = site.getExecutionCourse();
        final List<Evaluation> evaluations = executionCourse.getAssociatedEvaluations();
        for (final Evaluation evaluation : evaluations) {
            if (evaluationID.equals(evaluation.getExternalId())) {
                component.setEvaluation(evaluation);
                break;
            }
        }
        return component;
    }

    private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, ExecutionCourseSite site) throws FenixServiceException {

        List allSections = null;
        List<InfoSection> infoSectionsList = null;

        List<InfoCurricularCourse> infoCurricularCourseList = null;
        List<InfoCurricularCourse> infoCurricularCourseListByDegree = null;
        // read sections

        allSections = site.getAssociatedSections();

        // build the result of this service
        Iterator iterator = allSections.iterator();
        infoSectionsList = new ArrayList<InfoSection>(allSections.size());

        while (iterator.hasNext()) {
            infoSectionsList.add(copyISection2InfoSection((Section) iterator.next()));
        }
        Collections.sort(infoSectionsList);

        // read degrees

        ExecutionCourse executionCourse = site.getExecutionCourse();

        infoCurricularCourseList = readCurricularCourses(executionCourse);
        infoCurricularCourseListByDegree = readCurricularCoursesOrganizedByDegree(executionCourse);

        component.setAssociatedDegrees(infoCurricularCourseList);
        component.setAssociatedDegreesByDegree(infoCurricularCourseListByDegree);
        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);
        InfoExecutionCourse infoExecutionCourse;
        infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(site.getExecutionCourse());
        component.setExecutionCourse(infoExecutionCourse);
        return component;
    }

    private ISiteComponent getInfoSiteSection(InfoSiteSection component, ExecutionCourseSite site,
            InfoSiteCommon commonComponent, Integer sectionIndex) throws FenixServiceException {

        final InfoSection infoSection = (InfoSection) commonComponent.getSections().get(sectionIndex.intValue());
        component.setSection(infoSection);

        final Section section = (Section) AbstractDomainObject.fromExternalId(infoSection.getExternalId());

        final List<InfoItem> infoItemsList = new ArrayList<InfoItem>(section.getAssociatedItemsCount());
        for (final Item item : section.getAssociatedItems()) {
            final InfoItem infoItem = InfoItem.newInfoFromDomain(item);
            infoItemsList.add(infoItem);
        }

        Collections.sort(infoItemsList);
        component.setItems(infoItemsList);
        return component;
    }

    private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, ExecutionCourseSite site) throws FenixServiceException {
        List<InfoShiftWithAssociatedInfoClassesAndInfoLessons> shiftsWithAssociatedClassesAndLessons =
                new ArrayList<InfoShiftWithAssociatedInfoClassesAndInfoLessons>();

        ExecutionCourse disciplinaExecucao = site.getExecutionCourse();
        Set<Shift> shifts = disciplinaExecucao.getAssociatedShifts();

        for (final Shift shift : shifts) {
            List<Lesson> lessons = shift.getAssociatedLessons();
            List<InfoLesson> infoLessons = new ArrayList<InfoLesson>(lessons.size());
            List<SchoolClass> classesShifts = shift.getAssociatedClasses();
            List<InfoClass> infoClasses = new ArrayList<InfoClass>(classesShifts.size());

            for (final Lesson lesson : lessons) {
                infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
            }

            for (final SchoolClass schoolClass : classesShifts) {
                infoClasses.add(InfoClass.newInfoFromDomain(schoolClass));
            }

            InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons =
                    new InfoShiftWithAssociatedInfoClassesAndInfoLessons(infoShift, infoLessons, infoClasses);

            shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
        }

        component.setShifts(shiftsWithAssociatedClassesAndLessons);
        component.setInfoExecutionPeriodName(site.getExecutionCourse().getExecutionPeriod().getName());
        component.setInfoExecutionYearName(site.getExecutionCourse().getExecutionPeriod().getExecutionYear().getYear());
        return component;
    }

    private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, ExecutionCourseSite site)
            throws FenixServiceException {
        List<InfoLesson> infoLessonList = null;

        ExecutionCourse executionCourse = site.getExecutionCourse();

        List aulas = new ArrayList();

        Set<Shift> shifts = executionCourse.getAssociatedShifts();
        for (Shift shift : shifts) {
            List aulasTemp = shift.getAssociatedLessons();
            aulas.addAll(aulasTemp);
        }

        Iterator iterator = aulas.iterator();
        infoLessonList = new ArrayList<InfoLesson>();
        while (iterator.hasNext()) {
            infoLessonList.add(InfoLesson.newInfoFromDomain((Lesson) iterator.next()));
        }

        component.setLessons(infoLessonList);
        return component;
    }

    private ISiteComponent getInfoSiteAssociatedCurricularCourses(InfoSiteAssociatedCurricularCourses component,
            ExecutionCourseSite site) {
        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();

        ExecutionCourse executionCourse = site.getExecutionCourse();

        infoCurricularCourseList = readCurricularCourses(executionCourse);

        component.setAssociatedCurricularCourses(infoCurricularCourseList);
        return component;
    }

    private ISiteComponent getInfoSiteFirstPage(InfoSiteFirstPage component, ExecutionCourseSite site)
            throws FenixServiceException {

        ExecutionCourse executionCourse = site.getExecutionCourse();

        List<InfoTeacher> responsibleInfoTeachersList = readResponsibleTeachers(executionCourse);

        List<InfoTeacher> lecturingInfoTeachersList = readLecturingTeachers(executionCourse);

        // set all the required information to the component

        component.setAlternativeSite(site.getAlternativeSite());
        component.setInitialStatement(site.getInitialStatement());
        component.setIntroduction(site.getIntroduction());
        component.setSiteExternalId(site.getExternalId());
        if (!responsibleInfoTeachersList.isEmpty()) {
            component.setResponsibleTeachers(responsibleInfoTeachersList);
        } else {
            responsibleInfoTeachersList = new ArrayList<InfoTeacher>();
        }
        lecturingInfoTeachersList.removeAll(responsibleInfoTeachersList);
        if (!lecturingInfoTeachersList.isEmpty()) {
            component.setLecturingTeachers(lecturingInfoTeachersList);
        }

        return component;
    }

    private List<InfoTeacher> readLecturingTeachers(ExecutionCourse executionCourse) {
        List domainLecturingTeachersList = executionCourse.getProfessorships();

        List<InfoTeacher> lecturingInfoTeachersList = new ArrayList<InfoTeacher>();
        if (domainLecturingTeachersList != null) {

            Iterator iter = domainLecturingTeachersList.iterator();
            while (iter.hasNext()) {
                Professorship professorship = (Professorship) iter.next();
                Teacher teacher = professorship.getTeacher();
                InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                lecturingInfoTeachersList.add(infoTeacher);
            }
        }
        return lecturingInfoTeachersList;
    }

    private List<InfoTeacher> readResponsibleTeachers(ExecutionCourse executionCourse) {

        List responsibleDomainTeachersList = executionCourse.responsibleFors();

        List<InfoTeacher> responsibleInfoTeachersList = new ArrayList<InfoTeacher>();
        if (responsibleDomainTeachersList != null) {
            Iterator iter = responsibleDomainTeachersList.iterator();
            while (iter.hasNext()) {
                Professorship professorship = (Professorship) iter.next();
                Teacher teacher = professorship.getTeacher();
                InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                responsibleInfoTeachersList.add(infoTeacher);
            }

        }
        return responsibleInfoTeachersList;
    }

    private List<InfoCurricularCourse> readCurricularCourses(ExecutionCourse executionCourse) {
        List<InfoCurricularCourseScope> infoCurricularCourseScopeList;
        List<InfoCurricularCourse> infoCurricularCourseList = new ArrayList<InfoCurricularCourse>();
        if (executionCourse.getAssociatedCurricularCourses() != null) {
            for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
                CurricularCourse curricularCourse = executionCourse.getAssociatedCurricularCourses().get(i);
                InfoCurricularCourse infoCurricularCourse = copyFromDomain(curricularCourse);
                infoCurricularCourseScopeList = new ArrayList<InfoCurricularCourseScope>();
                for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
                    CurricularCourseScope curricularCourseScope = curricularCourse.getScopes().get(j);
                    InfoCurricularCourseScope infoCurricularCourseScope = copyFromDomain(curricularCourseScope);
                    infoCurricularCourseScopeList.add(infoCurricularCourseScope);
                }

                infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
                infoCurricularCourseList.add(infoCurricularCourse);
            }
        }

        return infoCurricularCourseList;
    }

    /**
     * Curricular courses list organized by degree (curricular information in
     * first page).
     */
    private List<InfoCurricularCourse> readCurricularCoursesOrganizedByDegree(ExecutionCourse executionCourse) {
        final List curricularCourses = executionCourse.getAssociatedCurricularCourses();
        final int estimatedResultSize = curricularCourses.size();

        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(estimatedResultSize);
        final Set<String> degreesCodes = new HashSet<String>(estimatedResultSize);
        for (final Iterator iterator = curricularCourses.iterator(); iterator.hasNext();) {
            final CurricularCourse curricularCourse = (CurricularCourse) iterator.next();
            final String degreeCode = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
            if (!degreesCodes.contains(degreeCode)) {
                final InfoCurricularCourse infoCurricularCourse = copyFromDomain(curricularCourse);
                // final InfoCurricularCourse infoCurricularCourse =
                // InfoCurricularCourse.newInfoFromDomain(curricularCourse);
                infoCurricularCourses.add(infoCurricularCourse);
                infoCurricularCourse.setInfoScopes(new ArrayList<InfoCurricularCourseScope>());

                for (Object element : curricularCourse.getScopes()) {
                    final CurricularCourseScope curricularCourseScope = (CurricularCourseScope) element;
                    final InfoCurricularCourseScope infoCurricularCourseScope = copyFromDomain(curricularCourseScope);
                    infoCurricularCourse.getInfoScopes().add(infoCurricularCourseScope);
                }

                degreesCodes.add(degreeCode);
            }
        }

        return infoCurricularCourses;
        //
        // List infoCurricularCourseScopeList;
        // List infoCurricularCourseList = new ArrayList();
        // StringBuilder allSiglas = new StringBuilder();
        //		
        // if (executionCourse.getAssociatedCurricularCourses() != null) {
        // for (int i = 0; i <
        // executionCourse.getAssociatedCurricularCourses().size(); i++) {
        // CurricularCourse curricularCourse = (CurricularCourse)
        // executionCourse
        // .getAssociatedCurricularCourses().get(i);
        // InfoCurricularCourse infoCurricularCourse =
        // copyFromDomain(curricularCourse);
        // infoCurricularCourseScopeList = new ArrayList();
        // for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
        // CurricularCourseScope curricularCourseScope =
        // (CurricularCourseScope) curricularCourse
        // .getScopes().get(j);
        // InfoCurricularCourseScope infoCurricularCourseScope =
        // copyFromDomain(curricularCourseScope);
        // infoCurricularCourseScopeList.add(infoCurricularCourseScope);
        // }
        //				
        // infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
        //	
        // String currentSigla =
        // infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree().
        // getSigla();
        //			
        // if (!infoCurricularCourseList.isEmpty() &&
        // StringUtils.contains(allSiglas.toString(),currentSigla)) {
        // for (int k = 0; k < infoCurricularCourseList.size(); k++) {
        // String sigla = ((InfoDegree) ((InfoDegreeCurricularPlan)
        // ((InfoCurricularCourse)
        // ((List)
        // infoCurricularCourseList.get(k)).get(0)).getInfoDegreeCurricularPlan()
        // ).getInfoDegree()).getSigla();
        // if (sigla.equals(currentSigla)) {
        // ((List)infoCurricularCourseList.get(k)).add(infoCurricularCourse);
        // break;
        // }
        // }
        // } else {
        // List infoCurricularCoursesByDegree = new ArrayList();
        // infoCurricularCoursesByDegree.add(infoCurricularCourse);
        // infoCurricularCourseList.add(infoCurricularCoursesByDegree);
        // allSiglas.append(currentSigla);
        // }
        // }
        // }
        //		
        // return infoCurricularCourseList;
    }

    /**
     * @param section
     * @return
     */
    private InfoSection copyISection2InfoSection(Section section) {
        InfoSection infoSection = null;
        if (section != null) {
            infoSection = new InfoSection();
            infoSection.setExternalId(section.getExternalId());
            infoSection.setName(section.getName().getContent(Language.pt));
            infoSection.setSectionOrder(section.getSectionOrder());
            infoSection.setSuperiorInfoSection(copyISection2InfoSection(section.getSuperiorSection()));
            infoSection.setInfoSite(copyISite2InfoSite((ExecutionCourseSite) section.getSite()));
        }
        return infoSection;
    }

    /**
     * @param site
     * @return
     */
    private InfoSite copyISite2InfoSite(ExecutionCourseSite site) {
        InfoSite infoSite = InfoSite.newInfoFromDomain(site);
        if (site != null) {
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(site.getExecutionCourse()));
        }
        return infoSite;
    }

    /**
     * @param curricularCourseScope
     * @return
     */
    private InfoCurricularCourseScope copyFromDomain(CurricularCourseScope curricularCourseScope) {
        InfoCurricularCourseScope infoCurricularCourseScope = null;
        if (curricularCourseScope != null) {
            infoCurricularCourseScope = new InfoCurricularCourseScope(curricularCourseScope);

        }
        return infoCurricularCourseScope;
    }

    private InfoCurricularCourse copyFromDomain(CurricularCourse curricularCourse) {
        return InfoCurricularCourse.newInfoFromDomain(curricularCourse);
    }

}