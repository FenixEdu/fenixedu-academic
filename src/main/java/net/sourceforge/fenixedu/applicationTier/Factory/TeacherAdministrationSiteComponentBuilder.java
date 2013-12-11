package net.sourceforge.fenixedu.applicationTier.Factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadShiftsAndGroups;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourseAndInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupations;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithAttends;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithInfoSiteAndInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteExamExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteInstructions;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteItems;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteNewProjectProposals;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteObjectives;
import net.sourceforge.fenixedu.dataTransferObject.InfoSitePrograms;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteProjects;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRegularSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteRootSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSentedProjectProposalsWaiting;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroupAndStudents;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentInformation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentsAndShiftByStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTeachers;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroupWithAttendsAndGroupingAndShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;

import org.apache.commons.beanutils.BeanUtils;

import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quit�rio
 * 
 */
public class TeacherAdministrationSiteComponentBuilder {

    private static TeacherAdministrationSiteComponentBuilder instance = null;

    public TeacherAdministrationSiteComponentBuilder() {
    }

    public static TeacherAdministrationSiteComponentBuilder getInstance() {
        if (instance == null) {
            instance = new TeacherAdministrationSiteComponentBuilder();
        }
        return instance;
    }

    public ISiteComponent getComponent(ISiteComponent component, ExecutionCourseSite site, ISiteComponent commonComponent,
            String obj1, String obj2) throws FenixServiceException {

        if (component instanceof InfoSiteCommon) {
            return getInfoSiteCommon((InfoSiteCommon) component, site);
        } else if (component instanceof InfoSiteInstructions) {
            return getInfoSiteInstructions((InfoSiteInstructions) component, site);
        } else if (component instanceof InfoSite) {
            return getInfoSiteCustomizationOptions((InfoSite) component, site);
        } else if (component instanceof InfoSiteObjectives) {
            return getInfoSiteObjectives((InfoSiteObjectives) component, site);
        } else if (component instanceof InfoSitePrograms) {
            return getInfoSitePrograms((InfoSitePrograms) component, site);
        } else if (component instanceof InfoCurriculum) {
            return getInfoCurriculum((InfoCurriculum) component, site, obj1);
        } else if (component instanceof InfoSiteTeachers) {
            return getInfoSiteTeachers((InfoSiteTeachers) component, site, obj2);
        } else if (component instanceof InfoSiteEvaluation) {
            return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
        } else if (component instanceof InfoSiteExam) {
            return getInfoSiteExam((InfoSiteExam) component, site);
        } else if (component instanceof InfoSiteEvaluationExecutionCourses) {
            return getInfoSiteEvaluationExecutionCourses((InfoSiteEvaluationExecutionCourses) component, site, obj1);
        } else if (component instanceof InfoSiteRootSections) {
            return getInfoSiteRootSections((InfoSiteRootSections) component, site);
        } else if (component instanceof InfoEvaluation) {
            return getInfoEvaluation((InfoEvaluation) component, site, obj1);
        } else if (component instanceof InfoSiteSection) {
            return getInfoSiteSection((InfoSiteSection) component, site, obj1);
        } else if (component instanceof InfoSiteRegularSections) {
            return getInfoSiteRegularSections((InfoSiteRegularSections) component, site, obj1);
        } else if (component instanceof InfoSiteSections) {
            return getInfoSiteSections((InfoSiteSections) component, site, obj1);
        } else if (component instanceof InfoSiteItems) {
            return getInfoSiteItems((InfoSiteItems) component, site, obj1);
        } else if (component instanceof InfoSiteProjects) {
            return getInfoSiteProjects((InfoSiteProjects) component, site);
        } else if (component instanceof InfoSiteNewProjectProposals) {
            return getInfoSiteNewProjectProposals((InfoSiteNewProjectProposals) component, site);
        } else if (component instanceof InfoSiteSentedProjectProposalsWaiting) {
            return getInfoSiteSentedProjectProposalsWaiting((InfoSiteSentedProjectProposalsWaiting) component, site);
        } else if (component instanceof InfoSiteShiftsAndGroups) {
            return getInfoSiteShiftsAndGroups((InfoSiteShiftsAndGroups) component, obj1);
        } else if (component instanceof InfoSiteStudentGroup) {
            return getInfoSiteStudentGroup((InfoSiteStudentGroup) component, obj1);
        } else if (component instanceof InfoSiteGrouping) {
            return getInfoSiteGroupProperties((InfoSiteGrouping) component, obj1);
        } else if (component instanceof InfoSiteShifts) {
            return getInfoSiteShifts((InfoSiteShifts) component, obj1, obj2);
        } else if (component instanceof InfoSiteStudentGroupAndStudents) {
            return getInfoSiteStudentGroupAndStudents((InfoSiteStudentGroupAndStudents) component, obj1, obj2);
        }
        return null;
    }

    /**
     * @param common
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, ExecutionCourseSite site) throws FenixServiceException {

        final List<Section> allSections = site.getAssociatedSections();
        final List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());
        for (final Section section : allSections) {
            infoSectionsList.add(InfoSectionWithAll.newInfoFromDomain(section));
        }
        Collections.sort(infoSectionsList);

        component.setTitle(site.getExecutionCourse().getNome());
        component.setMail(site.getMail());
        component.setSections(infoSectionsList);

        final ExecutionCourse executionCourse = site.getExecutionCourse();
        final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse.newInfoFromDomain(executionCourse);
        component.setExecutionCourse(infoExecutionCourse);

        final Collection<CurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(curricularCourses.size());
        for (final CurricularCourse curricularCourse : curricularCourses) {
            infoCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(curricularCourse));
        }
        component.setAssociatedDegrees(infoCurricularCourses);

        return component;
    }

    /**
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteInstructions(InfoSiteInstructions component, ExecutionCourseSite site) {

        return component;
    }

    /**
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteCustomizationOptions(InfoSite component, ExecutionCourseSite site) {
        component.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(site.getExecutionCourse()));
        return component;
    }

    /**
     * @param objectives
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteObjectives(InfoSiteObjectives component, ExecutionCourseSite site)
            throws FenixServiceException {

        ExecutionCourse executionCourse = site.getExecutionCourse();
        Collection curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Iterator iter = curricularCourses.iterator();
        List<InfoCurriculum> infoCurriculums = new ArrayList<InfoCurriculum>();

        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            Curriculum curriculum = curricularCourse.findLatestCurriculum();

            if (curriculum != null) {

                infoCurriculums.add(InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum));
            }
        }
        component.setInfoCurriculums(infoCurriculums);
        component.setInfoCurricularCourses(readInfoCurricularCourses(site));

        return component;
    }

    /**
     * @param program
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSitePrograms(InfoSitePrograms component, ExecutionCourseSite site) throws FenixServiceException {
        ExecutionCourse executionCourse = site.getExecutionCourse();
        Collection curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Iterator iter = curricularCourses.iterator();
        List<InfoCurriculum> infoCurriculums = new ArrayList<InfoCurriculum>();

        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            Curriculum curriculum = curricularCourse.findLatestCurriculum();

            if (curriculum != null) {
                InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum);
                infoCurriculums.add(infoCurriculum);
            }
        }
        component.setInfoCurriculums(infoCurriculums);
        component.setInfoCurricularCourses(readInfoCurricularCourses(site));

        return component;
    }

    /**
     * @param methods
     * @param site
     * @param integer
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoCurriculum(InfoCurriculum component, ExecutionCourseSite site, String curricularCourseCode)
            throws FenixServiceException {
        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);

        Curriculum curriculum = curricularCourse.findLatestCurriculum();
        InfoCurriculum infoCurriculum = null;

        if (curriculum != null) {
            infoCurriculum = InfoCurriculumWithInfoCurricularCourseAndInfoDegree.newInfoFromDomain(curriculum);
        }

        return infoCurriculum;
    }

    /**
     * @param teachers
     * @param site
     * @param username
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteTeachers(InfoSiteTeachers component, ExecutionCourseSite site, String username)
            throws FenixServiceException {

        ExecutionCourse executionCourse = site.getExecutionCourse();
        Collection teachers = executionCourse.getProfessorships();
        List<InfoTeacher> infoTeachers = new ArrayList<InfoTeacher>();
        if (teachers != null) {

            Iterator iter = teachers.iterator();
            while (iter.hasNext()) {
                Professorship professorship = (Professorship) iter.next();
                Teacher teacher = professorship.getTeacher();

                InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                infoTeachers.add(infoTeacher);
            }

            // see if teacher is responsible for that execution
            // course
            List responsibleTeachers = executionCourse.responsibleFors();

            List<InfoTeacher> infoResponsibleTeachers = new ArrayList<InfoTeacher>();
            boolean isResponsible = false;
            if (responsibleTeachers != null) {
                Iterator iter2 = responsibleTeachers.iterator();
                while (iter2.hasNext()) {
                    Professorship responsibleFor = (Professorship) iter2.next();
                    Teacher teacher = responsibleFor.getTeacher();

                    InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
                    infoResponsibleTeachers.add(infoTeacher);
                }

                Person person = Person.readPersonByUsername(username);
                Professorship responsibleFor = person.getProfessorshipByExecutionCourse(executionCourse);
                if (person != null) {
                    if (responsibleTeachers != null && !responsibleTeachers.isEmpty()
                            && responsibleTeachers.contains(responsibleFor)) {
                        isResponsible = true;
                    }
                }
            }

            component.setInfoTeachers(infoTeachers);
            component.setIsResponsible(new Boolean(isResponsible));
        }

        return component;
    }

    /**
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component, ExecutionCourseSite site) {
        ExecutionCourse executionCourse = site.getExecutionCourse();

        Collection evaluations = executionCourse.getAssociatedEvaluations();
        Iterator iter = evaluations.iterator();

        // boolean hasFinalEvaluation = false;
        List<InfoEvaluation> infoEvaluations = new ArrayList<InfoEvaluation>();
        List<InfoEvaluation> infoFinalEvaluations = new ArrayList<InfoEvaluation>();
        List<InfoEvaluation> infoOnlineTests = new ArrayList<InfoEvaluation>();

        while (iter.hasNext()) {
            Evaluation evaluation = (Evaluation) iter.next();
            if (evaluation instanceof Exam) {
                infoEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
            } else if (evaluation instanceof FinalEvaluation) {
                infoFinalEvaluations.add(InfoEvaluation.newInfoFromDomain(evaluation));
            } else if (evaluation instanceof OnlineTest) {
                infoOnlineTests.add(InfoEvaluation.newInfoFromDomain(evaluation));
            }
        }

        Collections.sort(infoEvaluations, InfoEvaluation.COMPARATOR_BY_START);
        // merge lists
        infoEvaluations.addAll(infoOnlineTests);
        infoEvaluations.addAll(infoFinalEvaluations);

        component.setInfoEvaluations(infoEvaluations);
        return component;
    }

    /**
     * @param component
     * @param site
     * @return
     */
    private ISiteComponent getInfoSiteExam(InfoSiteExam component, ExecutionCourseSite site) {
        ExecutionCourse executionCourse = site.getExecutionCourse();
        Collection<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
        List<Exam> exams = new ArrayList<Exam>();
        for (Evaluation evaluation : associatedEvaluations) {
            if (evaluation instanceof Exam) {
                exams.add((Exam) evaluation);
            }
        }
        List<InfoExam> infoExams = new ArrayList<InfoExam>();
        Iterator iter = exams.iterator();
        while (iter.hasNext()) {
            Exam exam = (Exam) iter.next();

            InfoExam infoExam = InfoExam.newInfoFromDomain(exam);
            infoExams.add(infoExam);
        }
        component.setInfoExams(infoExams);
        return component;
    }

    private ISiteComponent getInfoSiteEvaluationExecutionCourses(InfoSiteEvaluationExecutionCourses component,
            ExecutionCourseSite site, String evaluationID) {

        final Evaluation evaluation = FenixFramework.getDomainObject(evaluationID);

        if (evaluation instanceof Exam) {
            final Exam exam = (Exam) evaluation;
            exam.checkIfCanDistributeStudentsByRooms();
            final InfoExam infoExam = InfoExamWithRoomOccupations.newInfoFromDomain(exam);
            infoExam.setEnrolledStudents(exam.getWrittenEvaluationEnrolments().size());

            final List<InfoExecutionCourse> infoExecutionCourses =
                    new ArrayList<InfoExecutionCourse>(exam.getAssociatedExecutionCourses().size());
            for (final ExecutionCourse executionCourse : exam.getAssociatedExecutionCourses()) {
                infoExecutionCourses.add(InfoExecutionCourse.newInfoFromDomain(executionCourse));
            }

            final InfoSiteExamExecutionCourses infoSiteExamExecutionCourses = (InfoSiteExamExecutionCourses) component;
            infoSiteExamExecutionCourses.setInfoExam(infoExam);
            infoSiteExamExecutionCourses.setInfoExecutionCourses(infoExecutionCourses);

        } else if (evaluation instanceof WrittenTest) {
            // TODO: Return InfoWrittenTest
        } else {
            // TODO: Error
        }
        return component;
    }

    private ISiteComponent getInfoEvaluation(InfoEvaluation component, ExecutionCourseSite site, String evaluationCode)
            throws FenixServiceException {
        Evaluation evaluation = FenixFramework.getDomainObject(evaluationCode);

        InfoEvaluation infoEvaluation = InfoEvaluation.newInfoFromDomain(evaluation);

        if (infoEvaluation instanceof InfoExam) {
            InfoExam infoExam = (InfoExam) infoEvaluation;
            InfoExam examComponent = new InfoExam();

            // examComponent.setAssociatedRooms(infoExam.getAssociatedRooms());
            examComponent.setEvaluationType(infoExam.getEvaluationType());
            examComponent.setInfoExecutionCourse(infoExam.getInfoExecutionCourse());
            examComponent.setSeason(infoExam.getSeason());
            try {
                BeanUtils.copyProperties(examComponent, infoExam);
            } catch (IllegalAccessException e1) {
                throw new FenixServiceException(e1);
            } catch (InvocationTargetException e1) {
                throw new FenixServiceException(e1);
            }
            component = examComponent;
        } else if (evaluation instanceof FinalEvaluation) {
        }

        return component;
    }

    /**
     * @param sections
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteRootSections(InfoSiteRootSections component, ExecutionCourseSite site)
            throws FenixServiceException {

        List allSections = site.getAssociatedSections();

        // build the result of this service
        Iterator iterator = allSections.iterator();
        List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());

        while (iterator.hasNext()) {
            Section section = (Section) iterator.next();
            if (section.getSuperiorSection() == null) {
                infoSectionsList.add(InfoSection.newInfoFromDomain(section));
            }
        }
        Collections.sort(infoSectionsList);

        component.setRootSections(infoSectionsList);

        return component;
    }

    private ISiteComponent getInfoSiteSection(InfoSiteSection component, ExecutionCourseSite site, String sectionCode)
            throws FenixServiceException {

        final Section section = (Section) FenixFramework.getDomainObject(sectionCode);

        final List<InfoItem> infoItemsList = new ArrayList<InfoItem>(section.getAssociatedItemsCount());
        for (final Item item : section.getAssociatedItems()) {
            final InfoItem infoItem = InfoItem.newInfoFromDomain(item);
            infoItemsList.add(infoItem);
        }

        component.setSection(InfoSectionWithAll.newInfoFromDomain(section));
        Collections.sort(infoItemsList);
        component.setItems(infoItemsList);

        return component;
    }

    private ISiteComponent getInfoSiteRegularSections(InfoSiteRegularSections component, ExecutionCourseSite site,
            String sectionCode) throws FenixServiceException {
        Section iSuperiorSection = (Section) FenixFramework.getDomainObject(sectionCode);
        List allSections = site.getAssociatedSections();

        // build the result of this service
        Iterator iterator = allSections.iterator();
        List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());
        while (iterator.hasNext()) {
            Section section = (Section) iterator.next();

            if (section.getSuperiorSection() != null && section.getSuperiorSection().equals(iSuperiorSection)) {

                infoSectionsList.add(InfoSection.newInfoFromDomain(section));
            }
        }
        Collections.sort(infoSectionsList);

        component.setRegularSections(infoSectionsList);
        return component;
    }

    private ISiteComponent getInfoSiteSections(InfoSiteSections component, ExecutionCourseSite site, String sectionCode)
            throws FenixServiceException {
        Section iSection = (Section) FenixFramework.getDomainObject(sectionCode);

        InfoSection infoSection = InfoSection.newInfoFromDomain(iSection);
        List allSections = site.getAssociatedSections();

        // build the result of this service
        Iterator iterator = allSections.iterator();
        List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());

        if (iSection.getSuperiorSection() == null) {
            while (iterator.hasNext()) {
                Section section = (Section) iterator.next();
                if ((section.getSuperiorSection() == null) && !section.getName().equals(iSection.getName())) {
                    infoSectionsList.add(InfoSection.newInfoFromDomain(section));
                }
            }
        } else {
            while (iterator.hasNext()) {
                Section section = (Section) iterator.next();
                if ((section.getSuperiorSection() != null && section.getSuperiorSection().getExternalId()
                        .equals(iSection.getSuperiorSection().getExternalId()))
                        && !section.getName().equals(iSection.getName())) {
                    infoSectionsList.add(InfoSection.newInfoFromDomain(section));
                }
            }
        }

        Collections.sort(infoSectionsList);
        component.setSection(infoSection);
        component.setSections(infoSectionsList);
        return component;
    }

    private ISiteComponent getInfoSiteItems(InfoSiteItems component, ExecutionCourseSite site, String itemCode)
            throws FenixServiceException {

        final Item iItem = (Item) FenixFramework.getDomainObject(itemCode);
        final Section iSection = iItem.getSection();

        final InfoItem infoItem = InfoItem.newInfoFromDomain(iItem);
        infoItem.setInfoSection(InfoSectionWithInfoSiteAndInfoExecutionCourse.newInfoFromDomain(iSection));

        final List<Item> allItems = iSection.getAssociatedItems();
        List<InfoItem> infoItemsList = new ArrayList<InfoItem>(iSection.getAssociatedItemsCount());
        for (final Item item : allItems) {
            if (item != iItem) {
                infoItemsList.add(InfoItem.newInfoFromDomain(item));
            }
        }

        Collections.sort(infoItemsList);
        component.setItem(infoItem);
        component.setItems(infoItemsList);

        return component;
    }

    private List readInfoCurricularCourses(ExecutionCourseSite site) {

        ExecutionCourse executionCourse = site.getExecutionCourse();
        Collection curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Iterator iter = curricularCourses.iterator();
        List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
        while (iter.hasNext()) {
            CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse.newInfoFromDomain(curricularCourse);
            infoCurricularCourses.add(infoCurricularCourse);
        }
        return infoCurricularCourses;
    }

    /**
     * @param component
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */

    private ISiteComponent getInfoSiteProjects(InfoSiteProjects component, ExecutionCourseSite site) throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseProjects(site.getExecutionCourse().getExternalId());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseProjects(String executionCourseCode) throws ExcepcaoInexistente, FenixServiceException {

        List<InfoGrouping> projects = null;
        Grouping groupProperties;

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);

        List<Grouping> executionCourseProjects = new ArrayList<Grouping>();
        Collection groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
        while (iterGroupPropertiesExecutionCourse.hasNext()) {
            ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourse.next();
            if ((groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 1)
                    || (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 2)) {
                executionCourseProjects.add(groupPropertiesExecutionCourse.getGrouping());
            }
        }

        projects = new ArrayList<InfoGrouping>();
        Iterator iterator = executionCourseProjects.iterator();

        while (iterator.hasNext()) {
            // projects
            groupProperties = (Grouping) iterator.next();

            InfoGrouping infoGrouping = InfoGroupingWithExportGrouping.newInfoFromDomain(groupProperties);

            projects.add(infoGrouping);
        }

        return projects;
    }

    /**
     * @param component
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */

    private ISiteComponent getInfoSiteNewProjectProposals(InfoSiteNewProjectProposals component, ExecutionCourseSite site)
            throws FenixServiceException {

        List infoGroupPropertiesList = readExecutionCourseNewProjectProposals(site.getExecutionCourse().getExternalId());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseNewProjectProposals(String executionCourseCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List<InfoGrouping> projects = null;
        Grouping groupProperties;

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);

        List<Grouping> executionCourseProjects = new ArrayList<Grouping>();
        Collection groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
        Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
        while (iterGroupPropertiesExecutionCourse.hasNext()) {
            ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourse.next();
            if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                executionCourseProjects.add(groupPropertiesExecutionCourse.getGrouping());
            }
        }

        projects = new ArrayList<InfoGrouping>();
        Iterator iterator = executionCourseProjects.iterator();

        while (iterator.hasNext()) {

            groupProperties = (Grouping) iterator.next();

            InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(groupProperties);

            projects.add(infoGroupProperties);
        }

        return projects;
    }

    /**
     * @param component
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */

    private ISiteComponent getInfoSiteSentedProjectProposalsWaiting(InfoSiteSentedProjectProposalsWaiting component,
            ExecutionCourseSite site) throws FenixServiceException {

        List infoGroupPropertiesList =
                readExecutionCourseSentedProjectProposalsWaiting(site.getExecutionCourse().getExternalId());
        component.setInfoGroupPropertiesList(infoGroupPropertiesList);
        return component;
    }

    private List readExecutionCourseSentedProjectProposalsWaiting(String executionCourseCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List<InfoGrouping> projects = null;

        ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseCode);

        List<Grouping> executionCourseSentedProjects = new ArrayList<Grouping>();
        List groupPropertiesList = executionCourse.getGroupings();
        Iterator iterGroupPropertiesList = groupPropertiesList.iterator();
        while (iterGroupPropertiesList.hasNext()) {
            boolean found = false;
            Grouping groupProperties = (Grouping) iterGroupPropertiesList.next();
            Collection groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
            Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList.iterator();
            while (iterGroupPropertiesExecutionCourseList.hasNext() && !found) {
                ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourseList.next();
                if (groupPropertiesExecutionCourse.getProposalState().getState().intValue() == 3) {
                    executionCourseSentedProjects.add(groupPropertiesExecutionCourse.getGrouping());
                    found = true;
                }
            }
        }

        projects = new ArrayList<InfoGrouping>();
        Iterator iterator = executionCourseSentedProjects.iterator();

        while (iterator.hasNext()) {

            Grouping groupProperties = (Grouping) iterator.next();

            InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping.newInfoFromDomain(groupProperties);

            projects.add(infoGroupProperties);
        }

        return projects;
    }

    /**
     * @param component
     * @param site
     * @param groupPropertiesCode
     * @return
     * @throws ExcepcaoPersistencia
     */

    private ISiteComponent getInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups component, String groupPropertiesCode)
            throws FenixServiceException {

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);

        List infoSiteShiftsAndGroups = ReadShiftsAndGroups.run(groupProperties).getInfoSiteGroupsByShiftList();
        component.setInfoSiteGroupsByShiftList(infoSiteShiftsAndGroups);

        Integer numberOfStudentsOutsideGrouping = readNumberOfStudentsOutsideGrouping(groupProperties);
        component.setNumberOfStudentsOutsideAttendsSet(numberOfStudentsOutsideGrouping);

        Integer numberOfStudentsInsideAttendsSet = readNumberOfStudentsInsideGrouping(groupProperties);
        component.setNumberOfStudentsInsideAttendsSet(numberOfStudentsInsideAttendsSet);

        InfoGrouping infoGrouping = InfoGrouping.newInfoFromDomain(groupProperties);
        component.setInfoGrouping(infoGrouping);

        return component;
    }

    private Integer readNumberOfStudentsOutsideGrouping(Grouping groupProperties) {

        if (groupProperties == null) {
            return null;
        }

        return groupProperties.getNumberOfStudentsNotInGrouping();

    }

    private Integer readNumberOfStudentsInsideGrouping(Grouping groupProperties) {

        if (groupProperties == null) {
            return null;
        }

        return groupProperties.getNumberOfStudentsInGrouping();

    }

    private ISiteComponent getInfoSiteStudentGroup(InfoSiteStudentGroup component, String studentGroupID)
            throws FenixServiceException {

        final StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupID);
        if (studentGroup == null) {
            return null;
        }

        final List<InfoSiteStudentInformation> infoSiteStudentInformations = new ArrayList<InfoSiteStudentInformation>();
        for (final Attends attend : studentGroup.getAttends()) {
            infoSiteStudentInformations.add(new InfoSiteStudentInformation(attend.getRegistration().getPerson().getName(), attend
                    .getRegistration().getPerson().getEmail(), attend.getRegistration().getPerson().getUsername(), attend
                    .getRegistration().getNumber()));
        }
        Collections.sort(infoSiteStudentInformations, InfoSiteStudentInformation.COMPARATOR_BY_NUMBER);
        component.setInfoSiteStudentInformationList(infoSiteStudentInformations);
        component.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift.newInfoFromDomain(studentGroup));

        if (studentGroup.getGrouping().getMaximumCapacity() != null) {
            int freeGroups = studentGroup.getGrouping().getMaximumCapacity() - studentGroup.getAttends().size();
            component.setNrOfElements(Integer.valueOf(freeGroups));
        } else {
            component.setNrOfElements("Sem limite");
        }
        return component;
    }

    private ISiteComponent getInfoSiteStudentGroupAndStudents(InfoSiteStudentGroupAndStudents component,
            String groupPropertiesCode, String shiftCode) throws FenixServiceException {
        List infoSiteStudentsAndShiftByStudentGroupList = readStudentGroupAndStudents(groupPropertiesCode, shiftCode);
        component.setInfoSiteStudentsAndShiftByStudentGroupList(infoSiteStudentsAndShiftByStudentGroupList);

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = readShiftAndGroups(groupPropertiesCode, shiftCode);
        component.setInfoSiteShiftsAndGroups(infoSiteShiftsAndGroups);
        return component;
    }

    private InfoSiteShiftsAndGroups readShiftAndGroups(String groupPropertiesCode, String shiftCode) throws ExcepcaoInexistente,
            FenixServiceException {

        InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();

        Grouping grouping = FenixFramework.getDomainObject(groupPropertiesCode);
        if (grouping == null) {
            return null;
        }

        Shift shift = FenixFramework.getDomainObject(shiftCode);

        List<InfoSiteGroupsByShift> infoSiteGroupsByShiftList = new ArrayList<InfoSiteGroupsByShift>();
        InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteShift.setInfoShift(InfoShift.newInfoFromDomain(shift));

        List allStudentGroups = grouping.readAllStudentGroupsBy(shift);

        if (grouping.getDifferentiatedCapacity()) {
            Integer vagas = shift.getShiftGroupingProperties().getCapacity();
            infoSiteShift.setNrOfGroups(vagas);
        } else {
            if (grouping.getGroupMaximumNumber() != null) {
                int vagas = grouping.getGroupMaximumNumber().intValue() - allStudentGroups.size();
                infoSiteShift.setNrOfGroups(Integer.valueOf(vagas));
            } else {
                infoSiteShift.setNrOfGroups("Sem limite");
            }
        }
        InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);

        List<InfoSiteStudentGroup> infoSiteStudentGroupsList = null;
        if (allStudentGroups.size() != 0) {
            infoSiteStudentGroupsList = new ArrayList<InfoSiteStudentGroup>();
            Iterator iterGroups = allStudentGroups.iterator();
            while (iterGroups.hasNext()) {
                InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
                InfoStudentGroup infoStudentGroup = new InfoStudentGroup();
                infoStudentGroup = InfoStudentGroup.newInfoFromDomain((StudentGroup) iterGroups.next());
                infoSiteStudentGroup.setInfoStudentGroup(infoStudentGroup);
                infoSiteStudentGroupsList.add(infoSiteStudentGroup);
            }
            Collections.sort(infoSiteStudentGroupsList, InfoSiteStudentGroup.COMPARATOR_BY_NUMBER);
        }
        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

        infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

        return infoSiteShiftsAndGroups;
    }

    private List readStudentGroupAndStudents(String groupPropertiesCode, String shiftCode) throws ExcepcaoInexistente,
            FenixServiceException {

        List<InfoSiteStudentsAndShiftByStudentGroup> infoSiteStudentsAndShiftByStudentGroupList =
                new ArrayList<InfoSiteStudentsAndShiftByStudentGroup>();

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            return null;
        }

        Shift shift = FenixFramework.getDomainObject(shiftCode);

        List<StudentGroup> aux = new ArrayList<StudentGroup>();
        List studentGroupsWithShift = groupProperties.getStudentGroupsWithShift();
        Iterator iterStudentGroupsWithShift = studentGroupsWithShift.iterator();
        while (iterStudentGroupsWithShift.hasNext()) {
            StudentGroup studentGroup = (StudentGroup) iterStudentGroupsWithShift.next();
            if (studentGroup.getShift().equals(shift)) {
                aux.add(studentGroup);
            }
        }
        List<StudentGroup> allStudentGroups = new ArrayList<StudentGroup>();
        allStudentGroups.addAll(groupProperties.getStudentGroupsSet());

        Iterator iterAux = aux.iterator();
        while (iterAux.hasNext()) {
            StudentGroup studentGroup = (StudentGroup) iterAux.next();
            allStudentGroups.remove(studentGroup);
        }

        Iterator iterAllStudentGroups = allStudentGroups.iterator();
        InfoSiteStudentsAndShiftByStudentGroup infoSiteStudentsAndShiftByStudentGroup = null;
        while (iterAllStudentGroups.hasNext()) {
            infoSiteStudentsAndShiftByStudentGroup = new InfoSiteStudentsAndShiftByStudentGroup();

            StudentGroup studentGroup = (StudentGroup) iterAllStudentGroups.next();
            Shift turno = studentGroup.getShift();
            infoSiteStudentsAndShiftByStudentGroup.setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroup));
            infoSiteStudentsAndShiftByStudentGroup.setInfoShift(InfoShift.newInfoFromDomain(turno));

            Collection attendsList = studentGroup.getAttends();

            List<InfoSiteStudentInformation> studentGroupAttendInformationList = new ArrayList<InfoSiteStudentInformation>();
            Iterator iterAttendsList = attendsList.iterator();
            InfoSiteStudentInformation infoSiteStudentInformation = null;
            Attends attend = null;

            while (iterAttendsList.hasNext()) {
                infoSiteStudentInformation = new InfoSiteStudentInformation();

                attend = (Attends) iterAttendsList.next();

                infoSiteStudentInformation.setNumber(attend.getRegistration().getNumber());

                studentGroupAttendInformationList.add(infoSiteStudentInformation);

            }

            Collections.sort(studentGroupAttendInformationList, InfoSiteStudentInformation.COMPARATOR_BY_NUMBER);

            infoSiteStudentsAndShiftByStudentGroup.setInfoSiteStudentInformationList(studentGroupAttendInformationList);
            infoSiteStudentsAndShiftByStudentGroupList.add(infoSiteStudentsAndShiftByStudentGroup);

            Collections.sort(infoSiteStudentsAndShiftByStudentGroupList,
                    InfoSiteStudentsAndShiftByStudentGroup.COMPARATOR_BY_NUMBER);

        }

        return infoSiteStudentsAndShiftByStudentGroupList;
    }

    /**
     * @param component
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */

    private ISiteComponent getInfoSiteGroupProperties(InfoSiteGrouping component, String groupPropertiesCode)
            throws FenixServiceException {

        InfoGrouping infoGrouping = readGroupProperties(groupPropertiesCode);
        component.setInfoGrouping(infoGrouping);
        return component;
    }

    private InfoGrouping readGroupProperties(String groupPropertiesCode) throws ExcepcaoInexistente, FenixServiceException {
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        return InfoGroupingWithAttends.newInfoFromDomain(groupProperties);
    }

    /**
     * @param shifts
     * @param site
     * @return
     * @throws ExcepcaoPersistencia
     */
    private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, String groupPropertiesCode, String studentGroupCode)
            throws FenixServiceException {
        List<InfoShift> infoShifts = new ArrayList<InfoShift>();
        ExecutionCourse executionCourse = null;

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            return null;
        }
        if (studentGroupCode != null) {
            StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
            if (studentGroup == null) {
                component.setShifts(null);
                return component;
            }

            component.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (strategy.checkHasShift(groupProperties)) {

            List executionCourses = new ArrayList();
            executionCourses = groupProperties.getExecutionCourses();

            Iterator iterExecutionCourses = executionCourses.iterator();
            List<Shift> shifts = new ArrayList<Shift>();
            while (iterExecutionCourses.hasNext()) {
                ExecutionCourse executionCourse2 = (ExecutionCourse) iterExecutionCourses.next();
                Set<Shift> someShifts = executionCourse2.getAssociatedShifts();

                shifts.addAll(someShifts);
            }

            if (shifts == null || shifts.isEmpty()) {

            } else {
                for (int i = 0; i < shifts.size(); i++) {
                    Shift shift = shifts.get(i);
                    if (strategy.checkShiftType(groupProperties, shift)) {
                        executionCourse = shift.getDisciplinaExecucao();
                        InfoShift infoShift = new InfoShift(shift);
                        infoShifts.add(infoShift);
                    }
                }
            }
        }

        component.setShifts(infoShifts);

        return component;
    }
}