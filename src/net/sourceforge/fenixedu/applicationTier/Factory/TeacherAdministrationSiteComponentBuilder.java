package net.sourceforge.fenixedu.applicationTier.Factory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
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
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourseAndInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamWithRoomOccupations;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithAttends;
import net.sourceforge.fenixedu.dataTransferObject.InfoGroupingWithExportGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoSectionWithInfoSiteAndInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationExecutionCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMethods;
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
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.FinalEvaluation;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.comparators.ComparatorChain;

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

	public ISiteComponent getComponent(ISiteComponent component, Site site,
			ISiteComponent commonComponent, Object obj1, Object obj2) throws FenixServiceException,
			ExcepcaoPersistencia {

		if (component instanceof InfoSiteCommon) {
			return getInfoSiteCommon((InfoSiteCommon) component, site);
		} else if (component instanceof InfoSiteInstructions) {
			return getInfoSiteInstructions((InfoSiteInstructions) component, site);
		} else if (component instanceof InfoSite) {
			return getInfoSiteCustomizationOptions((InfoSite) component, site);
		} else if (component instanceof InfoSiteAnnouncement) {
			return getInfoSiteAnnouncement((InfoSiteAnnouncement) component, site);
		} else if (component instanceof InfoAnnouncement) {
			return getInfoAnnouncement((InfoAnnouncement) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteObjectives) {
			return getInfoSiteObjectives((InfoSiteObjectives) component, site);
		} else if (component instanceof InfoSitePrograms) {
			return getInfoSitePrograms((InfoSitePrograms) component, site);
		} else if (component instanceof InfoCurriculum) {
			return getInfoCurriculum((InfoCurriculum) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteEvaluationMethods) {
			return getInfoEvaluationMethods((InfoSiteEvaluationMethods) component, site);
		} else if (component instanceof InfoEvaluationMethod) {
			return getInfoEvaluationMethod((InfoEvaluationMethod) component, site);
		} else if (component instanceof InfoSiteBibliography) {
			return getInfoSiteBibliography((InfoSiteBibliography) component, site);
		} else if (component instanceof InfoBibliographicReference) {
			return getInfoBibliographicReference((InfoBibliographicReference) component, site,
					(Integer) obj1);
		} else if (component instanceof InfoSiteTeachers) {
			return getInfoSiteTeachers((InfoSiteTeachers) component, site, (String) obj2);
		} else if (component instanceof InfoSiteEvaluation) {
			return getInfoSiteEvaluation((InfoSiteEvaluation) component, site);
		} else if (component instanceof InfoSiteExam) {
			return getInfoSiteExam((InfoSiteExam) component, site);
		} else if (component instanceof InfoSiteEvaluationExecutionCourses) {
			return getInfoSiteEvaluationExecutionCourses((InfoSiteEvaluationExecutionCourses) component,
					site, (Integer) obj1);
		} else if (component instanceof InfoSiteRootSections) {
			return getInfoSiteRootSections((InfoSiteRootSections) component, site);
		} else if (component instanceof InfoEvaluation) {
			return getInfoEvaluation((InfoEvaluation) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteSection) {
			return getInfoSiteSection((InfoSiteSection) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteRegularSections) {
			return getInfoSiteRegularSections((InfoSiteRegularSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteSections) {
			return getInfoSiteSections((InfoSiteSections) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteItems) {
			return getInfoSiteItems((InfoSiteItems) component, site, (Integer) obj1);
		} else if (component instanceof InfoSiteProjects) {
			return getInfoSiteProjects((InfoSiteProjects) component, site);
		} else if (component instanceof InfoSiteNewProjectProposals) {
			return getInfoSiteNewProjectProposals((InfoSiteNewProjectProposals) component, site);
		} else if (component instanceof InfoSiteSentedProjectProposalsWaiting) {
			return getInfoSiteSentedProjectProposalsWaiting(
					(InfoSiteSentedProjectProposalsWaiting) component, site);
		} else if (component instanceof InfoSiteShiftsAndGroups) {
			return getInfoSiteShiftsAndGroups((InfoSiteShiftsAndGroups) component, (Integer) obj1);
		} else if (component instanceof InfoSiteStudentGroup) {
			return getInfoSiteStudentGroup((InfoSiteStudentGroup) component, (Integer) obj1);
		} else if (component instanceof InfoSiteGrouping) {
			return getInfoSiteGroupProperties((InfoSiteGrouping) component, (Integer) obj1);
		} else if (component instanceof InfoSiteShifts) {
			return getInfoSiteShifts((InfoSiteShifts) component, (Integer) obj1, (Integer) obj2);
		} else if (component instanceof InfoSiteStudentGroupAndStudents) {
			return getInfoSiteStudentGroupAndStudents((InfoSiteStudentGroupAndStudents) component,
					(Integer) obj1, (Integer) obj2);
		}
		return null;
	}

	/**
	 * @param common
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

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
		final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod
				.newInfoFromDomain(executionCourse);
		component.setExecutionCourse(infoExecutionCourse);

		final List<CurricularCourse> curricularCourses = executionCourse
				.getAssociatedCurricularCourses();
		final List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>(
				curricularCourses.size());
		for (final CurricularCourse curricularCourse : curricularCourses) {
			infoCurricularCourses.add(InfoCurricularCourseWithInfoDegree
					.newInfoFromDomain(curricularCourse));
		}
		component.setAssociatedDegrees(infoCurricularCourses);

		return component;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteInstructions(InfoSiteInstructions component, Site site) {

		return component;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 */
	private ISiteComponent getInfoSiteCustomizationOptions(InfoSite component, Site site) {
		component.setInfoExecutionCourse(InfoExecutionCourse
				.newInfoFromDomain(site.getExecutionCourse()));
		return component;
	}

	private InfoSiteAnnouncement getInfoSiteAnnouncement(InfoSiteAnnouncement component, Site site) {
		Set<Announcement> announcements = site.getSortedAnnouncements();
		List<InfoAnnouncement> infoAnnouncementsList = new ArrayList<InfoAnnouncement>(announcements.size());

		for (Announcement ann : announcements) {
			infoAnnouncementsList.add(InfoAnnouncement.newInfoFromDomain(ann));
		}

		component.setAnnouncements(infoAnnouncementsList);
		return component;
	}

	/**
	 * @param announcement
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoAnnouncement(InfoAnnouncement component, Site site,
			Integer announcementCode) throws FenixServiceException, ExcepcaoPersistencia {
		Announcement iAnnouncement = RootDomainObject.getInstance().readAnnouncementByOID(announcementCode);
		InfoAnnouncement infoAnnouncement = InfoAnnouncement.newInfoFromDomain(iAnnouncement);

		component.setCreationDate(infoAnnouncement.getCreationDate());
		component.setIdInternal(infoAnnouncement.getIdInternal());
		component.setInformation(infoAnnouncement.getInformation());
		component.setInfoSite(infoAnnouncement.getInfoSite());
		component.setLastModifiedDate(infoAnnouncement.getLastModifiedDate());
		component.setTitle(infoAnnouncement.getTitle());
		return component;
	}

	/**
	 * @param objectives
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteObjectives(InfoSiteObjectives component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		ExecutionCourse executionCourse = site.getExecutionCourse();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List<InfoCurriculum> infoCurriculums = new ArrayList<InfoCurriculum>();

		while (iter.hasNext()) {
			CurricularCourse curricularCourse = (CurricularCourse) iter.next();
			Curriculum curriculum = curricularCourse.findLatestCurriculum();

			if (curriculum != null) {

				infoCurriculums
						.add(InfoCurriculumWithInfoCurricularCourse.newInfoFromDomain(curriculum));
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
	private ISiteComponent getInfoSitePrograms(InfoSitePrograms component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = site.getExecutionCourse();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List<InfoCurriculum> infoCurriculums = new ArrayList<InfoCurriculum>();

		while (iter.hasNext()) {
			CurricularCourse curricularCourse = (CurricularCourse) iter.next();
			Curriculum curriculum = curricularCourse.findLatestCurriculum();

			if (curriculum != null) {
				InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourse
						.newInfoFromDomain(curriculum);
				infoCurriculums.add(infoCurriculum);
			}
		}
		component.setInfoCurriculums(infoCurriculums);
		component.setInfoCurricularCourses(readInfoCurricularCourses(site));

		return component;
	}

	/**
	 * @param evaluation
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoEvaluationMethods(InfoSiteEvaluationMethods component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = site.getExecutionCourse();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List<InfoCurriculum> infoEvaluationMethods = new ArrayList<InfoCurriculum>();

		while (iter.hasNext()) {
			CurricularCourse curricularCourse = (CurricularCourse) iter.next();
            Curriculum curriculum = curricularCourse.findLatestCurriculum();

			if (curriculum != null) {
				infoEvaluationMethods.add(InfoCurriculum.newInfoFromDomain(curriculum));
			}
		}
		component.setInfoEvaluations(infoEvaluationMethods);
		component.setInfoCurricularCourses(readInfoCurricularCourses(site));

		return component;
	}

	private ISiteComponent getInfoEvaluationMethod(InfoEvaluationMethod component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		final EvaluationMethod evaluationMethod = site.getExecutionCourse().getEvaluationMethod();
		if (evaluationMethod != null) {
			component = InfoEvaluationMethod.newInfoFromDomain(evaluationMethod);
		}
		return component;
	}

	/**
	 * @param methods
	 * @param site
	 * @param integer
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoCurriculum(InfoCurriculum component, Site site,
			Integer curricularCourseCode) throws FenixServiceException, ExcepcaoPersistencia {
		CurricularCourse curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseCode);

        Curriculum curriculum = curricularCourse.findLatestCurriculum();
		InfoCurriculum infoCurriculum = null;

		if (curriculum != null) {
			infoCurriculum = InfoCurriculumWithInfoCurricularCourseAndInfoDegree
					.newInfoFromDomain(curriculum);
		}

		return infoCurriculum;
	}

	/**
	 * @param bibliography
	 * @param site
	 * @return
	 * 
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteBibliography(InfoSiteBibliography component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = site.getExecutionCourse();

		List references = executionCourse.getAssociatedBibliographicReferences();

		if (references != null) {
			Iterator iterator = references.iterator();
			List<InfoBibliographicReference> infoBibRefs = new ArrayList<InfoBibliographicReference>();
			while (iterator.hasNext()) {
				BibliographicReference bibRef = (BibliographicReference) iterator.next();

				InfoBibliographicReference infoBibRef = InfoBibliographicReference
						.newInfoFromDomain(bibRef);
				infoBibRefs.add(infoBibRef);

			}
			component.setBibliographicReferences(infoBibRefs);
		}

		return component;
	}

	/**
	 * @param reference
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoBibliographicReference(InfoBibliographicReference component,
			Site site, Integer bibliographicReferenceCode) throws FenixServiceException,
			ExcepcaoPersistencia {
		BibliographicReference iBibliographicReference = RootDomainObject.getInstance().readBibliographicReferenceByOID(bibliographicReferenceCode);

        InfoBibliographicReference infoBibliographicReference = InfoBibliographicReference
				.newInfoFromDomain(iBibliographicReference);

		component.setTitle(infoBibliographicReference.getTitle());
		component.setAuthors(infoBibliographicReference.getAuthors());
		component.setReference(infoBibliographicReference.getReference());
		component.setYear(infoBibliographicReference.getYear());
		component.setOptional(infoBibliographicReference.getOptional());
		component.setIdInternal(infoBibliographicReference.getIdInternal());
		return component;
	}

	/**
	 * @param teachers
	 * @param site
	 * @param username
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteTeachers(InfoSiteTeachers component, Site site, String username)
			throws FenixServiceException, ExcepcaoPersistencia {

        ExecutionCourse executionCourse = site.getExecutionCourse();
		List teachers = executionCourse.getProfessorships();
		List<InfoTeacher> infoTeachers = new ArrayList<InfoTeacher>();
		if (teachers != null) {

			Iterator iter = teachers.iterator();
			while (iter.hasNext()) {
				Professorship professorship = (Professorship) iter.next();
				Teacher teacher = professorship.getTeacher();

				InfoTeacher infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
				infoTeachers.add(infoTeacher);
			}

			// see if teacher is responsible for that execution course
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

				Teacher teacher = Teacher.readTeacherByUsername(username);
				Professorship responsibleFor = teacher.getProfessorshipByExecutionCourse(executionCourse);
				if (teacher != null) {
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
	private ISiteComponent getInfoSiteEvaluation(InfoSiteEvaluation component, Site site) {
		ExecutionCourse executionCourse = site.getExecutionCourse();

		List evaluations = executionCourse.getAssociatedEvaluations();
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

		ComparatorChain comparatorChain = new ComparatorChain();
		comparatorChain.addComparator(new BeanComparator("day.time"));
		comparatorChain.addComparator(new BeanComparator("beginning.time"));

		Collections.sort(infoEvaluations, comparatorChain);
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
	private ISiteComponent getInfoSiteExam(InfoSiteExam component, Site site) {
		System.out.println("\n\n\n\n\nEntrei na funao getInfoSite Exam!!!\n\n\n\n\n");
		ExecutionCourse executionCourse = site.getExecutionCourse();
		List<Evaluation> associatedEvaluations = executionCourse.getAssociatedEvaluations();
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

	private ISiteComponent getInfoSiteEvaluationExecutionCourses(
			InfoSiteEvaluationExecutionCourses component, Site site, Integer evaluationID)
			throws ExcepcaoPersistencia {

		final Evaluation evaluation = RootDomainObject.getInstance().readEvaluationByOID(evaluationID);

		if (evaluation instanceof Exam) {
			final Exam exam = (Exam) evaluation;
			exam.checkIfCanDistributeStudentsByRooms();
			final InfoExam infoExam = InfoExamWithRoomOccupations.newInfoFromDomain(exam);
			infoExam.setEnrolledStudents(exam.getWrittenEvaluationEnrolmentsCount());

			final List<InfoExecutionCourse> infoExecutionCourses = new ArrayList<InfoExecutionCourse>(
					exam.getAssociatedExecutionCoursesCount());
			for (final ExecutionCourse executionCourse : exam.getAssociatedExecutionCourses()) {
				final InfoExecutionCourse infoExecutionCourse = InfoExecutionCourse
						.newInfoFromDomain(executionCourse);
				infoExecutionCourse.setNumberOfAttendingStudents(executionCourse.getAttendsCount());
				infoExecutionCourses.add(infoExecutionCourse);
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

	private ISiteComponent getInfoEvaluation(InfoEvaluation component, Site site, Integer evaluationCode)
			throws FenixServiceException, ExcepcaoPersistencia {
		Evaluation evaluation = (Evaluation) RootDomainObject.getInstance().readEvaluationByOID(evaluationCode);

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
	private ISiteComponent getInfoSiteRootSections(InfoSiteRootSections component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

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

	private ISiteComponent getInfoSiteSection(InfoSiteSection component, Site site, Integer sectionCode)
			throws FenixServiceException, ExcepcaoPersistencia {

        final Section section = RootDomainObject.getInstance().readSectionByOID(sectionCode);

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

	private ISiteComponent getInfoSiteRegularSections(InfoSiteRegularSections component, Site site,
			Integer sectionCode) throws FenixServiceException, ExcepcaoPersistencia {
		Section iSuperiorSection = RootDomainObject.getInstance().readSectionByOID(sectionCode);
		List allSections = site.getAssociatedSections();

		// build the result of this service
		Iterator iterator = allSections.iterator();
		List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());
		while (iterator.hasNext()) {
			Section section = (Section) iterator.next();

			if (section.getSuperiorSection() != null
					&& section.getSuperiorSection().equals(iSuperiorSection)) {

				infoSectionsList.add(InfoSection.newInfoFromDomain(section));
			}
		}
		Collections.sort(infoSectionsList);

		component.setRegularSections(infoSectionsList);
		return component;
	}

	private ISiteComponent getInfoSiteSections(InfoSiteSections component, Site site,
			Integer sectionCode) throws FenixServiceException, ExcepcaoPersistencia {
		Section iSection = RootDomainObject.getInstance().readSectionByOID(sectionCode);

		InfoSection infoSection = InfoSection.newInfoFromDomain(iSection);
		List allSections = site.getAssociatedSections(); 

		// build the result of this service
		Iterator iterator = allSections.iterator();
		List<InfoSection> infoSectionsList = new ArrayList<InfoSection>(allSections.size());

		if (iSection.getSuperiorSection() == null) {
			while (iterator.hasNext()) {
				Section section = (Section) iterator.next();
				if ((section.getSuperiorSection() == null)
						&& !section.getName().equals(iSection.getName())) {
					infoSectionsList.add(InfoSection.newInfoFromDomain(section));
				}
			}
		} else {
			while (iterator.hasNext()) {
				Section section = (Section) iterator.next();
				if ((section.getSuperiorSection() != null && section.getSuperiorSection()
						.getIdInternal().equals(iSection.getSuperiorSection().getIdInternal()))
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

	private ISiteComponent getInfoSiteItems(InfoSiteItems component, Site site, Integer itemCode)
			throws FenixServiceException, ExcepcaoPersistencia {

		final Item iItem = RootDomainObject.getInstance().readItemByOID(itemCode);
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

	private List readInfoCurricularCourses(Site site) {

		ExecutionCourse executionCourse = site.getExecutionCourse();
		List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		Iterator iter = curricularCourses.iterator();
		List<InfoCurricularCourse> infoCurricularCourses = new ArrayList<InfoCurricularCourse>();
		while (iter.hasNext()) {
			CurricularCourse curricularCourse = (CurricularCourse) iter.next();
			InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
					.newInfoFromDomain(curricularCourse);
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

	private ISiteComponent getInfoSiteProjects(InfoSiteProjects component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		List infoGroupPropertiesList = readExecutionCourseProjects(site.getExecutionCourse()
				.getIdInternal());
		component.setInfoGroupPropertiesList(infoGroupPropertiesList);
		return component;
	}

	private List readExecutionCourseProjects(Integer executionCourseCode) throws ExcepcaoInexistente,
			FenixServiceException, ExcepcaoPersistencia {

		List<InfoGrouping> projects = null;
		Grouping groupProperties;

		ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);

		List<Grouping> executionCourseProjects = new ArrayList<Grouping>();
		List groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
		Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
		while (iterGroupPropertiesExecutionCourse.hasNext()) {
			ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourse
					.next();
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

			InfoGrouping infoGrouping = InfoGroupingWithExportGrouping
					.newInfoFromDomain(groupProperties);

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

	private ISiteComponent getInfoSiteNewProjectProposals(InfoSiteNewProjectProposals component,
			Site site) throws FenixServiceException, ExcepcaoPersistencia {

		List infoGroupPropertiesList = readExecutionCourseNewProjectProposals(site.getExecutionCourse()
				.getIdInternal());
		component.setInfoGroupPropertiesList(infoGroupPropertiesList);
		return component;
	}

	private List readExecutionCourseNewProjectProposals(Integer executionCourseCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		List<InfoGrouping> projects = null;
		Grouping groupProperties;


		ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);

		List<Grouping> executionCourseProjects = new ArrayList<Grouping>();
		List groupPropertiesExecutionCourseList = executionCourse.getExportGroupings();
		Iterator iterGroupPropertiesExecutionCourse = groupPropertiesExecutionCourseList.iterator();
		while (iterGroupPropertiesExecutionCourse.hasNext()) {
			ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourse
					.next();
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

	private ISiteComponent getInfoSiteSentedProjectProposalsWaiting(
			InfoSiteSentedProjectProposalsWaiting component, Site site) throws FenixServiceException,
			ExcepcaoPersistencia {

		List infoGroupPropertiesList = readExecutionCourseSentedProjectProposalsWaiting(site
				.getExecutionCourse().getIdInternal());
		component.setInfoGroupPropertiesList(infoGroupPropertiesList);
		return component;
	}

	private List readExecutionCourseSentedProjectProposalsWaiting(Integer executionCourseCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		List<InfoGrouping> projects = null;

		ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseCode);

		List<Grouping> executionCourseSentedProjects = new ArrayList<Grouping>();
		List groupPropertiesList = executionCourse.getGroupings();
		Iterator iterGroupPropertiesList = groupPropertiesList.iterator();
		while (iterGroupPropertiesList.hasNext()) {
			boolean found = false;
			Grouping groupProperties = (Grouping) iterGroupPropertiesList.next();
			List groupPropertiesExecutionCourseList = groupProperties.getExportGroupings();
			Iterator iterGroupPropertiesExecutionCourseList = groupPropertiesExecutionCourseList
					.iterator();
			while (iterGroupPropertiesExecutionCourseList.hasNext() && !found) {
				ExportGrouping groupPropertiesExecutionCourse = (ExportGrouping) iterGroupPropertiesExecutionCourseList
						.next();
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

			InfoGrouping infoGroupProperties = InfoGroupingWithExportGrouping
					.newInfoFromDomain(groupProperties);

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

	private ISiteComponent getInfoSiteShiftsAndGroups(InfoSiteShiftsAndGroups component,
			Integer groupPropertiesCode) throws FenixServiceException, ExcepcaoPersistencia {

		Grouping groupProperties = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesCode);

		List infoSiteShiftsAndGroups = ReadShiftsAndGroups.run(groupProperties)
				.getInfoSiteGroupsByShiftList();
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

		if (groupProperties == null)
			return null;

		return groupProperties.getNumberOfStudentsNotInGrouping();

	}

	private Integer readNumberOfStudentsInsideGrouping(Grouping groupProperties) {

		if (groupProperties == null)
			return null;

		return groupProperties.getNumberOfStudentsInGrouping();

	}

	private ISiteComponent getInfoSiteStudentGroup(InfoSiteStudentGroup component, Integer studentGroupID)
			throws FenixServiceException, ExcepcaoPersistencia {

		final StudentGroup studentGroup = RootDomainObject.getInstance().readStudentGroupByOID(studentGroupID);
		if (studentGroup == null) {
			return null;
		}

		final List<InfoSiteStudentInformation> infoSiteStudentInformations = new ArrayList<InfoSiteStudentInformation>();
		for (final Attends attend : studentGroup.getAttends()) {
			infoSiteStudentInformations.add(new InfoSiteStudentInformation(attend.getAluno().getPerson()
					.getNome(), attend.getAluno().getPerson().getEmail(), attend.getAluno().getPerson()
					.getUsername(), attend.getAluno().getNumber()));
		}
		Collections.sort(infoSiteStudentInformations, new BeanComparator("number"));
		component.setInfoSiteStudentInformationList(infoSiteStudentInformations);
		component.setInfoStudentGroup(InfoStudentGroupWithAttendsAndGroupingAndShift
				.newInfoFromDomain(studentGroup));

		if (studentGroup.getGrouping().getMaximumCapacity() != null) {
			int freeGroups = studentGroup.getGrouping().getMaximumCapacity()
					- studentGroup.getAttendsCount();
			component.setNrOfElements(Integer.valueOf(freeGroups));
		} else
			component.setNrOfElements("Sem limite");
		return component;
	}

	private ISiteComponent getInfoSiteStudentGroupAndStudents(InfoSiteStudentGroupAndStudents component,
			Integer groupPropertiesCode, Integer shiftCode) throws FenixServiceException,
			ExcepcaoPersistencia {
		List infoSiteStudentsAndShiftByStudentGroupList = readStudentGroupAndStudents(
				groupPropertiesCode, shiftCode);
		component
				.setInfoSiteStudentsAndShiftByStudentGroupList(infoSiteStudentsAndShiftByStudentGroupList);

		InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = readShiftAndGroups(groupPropertiesCode,
				shiftCode);
		component.setInfoSiteShiftsAndGroups(infoSiteShiftsAndGroups);
		return component;
	}

	private InfoSiteShiftsAndGroups readShiftAndGroups(Integer groupPropertiesCode, Integer shiftCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();

		Grouping grouping = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesCode);
		if (grouping == null)
			return null;

		Shift shift = RootDomainObject.getInstance().readShiftByOID(shiftCode);

		List<InfoSiteGroupsByShift> infoSiteGroupsByShiftList = new ArrayList<InfoSiteGroupsByShift>();
		InfoSiteShift infoSiteShift = new InfoSiteShift();
		infoSiteShift.setInfoShift(InfoShiftWithInfoLessons.newInfoFromDomain(shift));

		List allStudentGroups = grouping.readAllStudentGroupsBy(shift);

		if (grouping.getGroupMaximumNumber() != null) {
			int vagas = grouping.getGroupMaximumNumber().intValue() - allStudentGroups.size();
			infoSiteShift.setNrOfGroups(new Integer(vagas));
		} else
			infoSiteShift.setNrOfGroups("Sem limite");
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
			Collections.sort(infoSiteStudentGroupsList, new BeanComparator(
					"infoStudentGroup.groupNumber"));
		}
		infoSiteGroupsByShift.setInfoSiteStudentGroupsList(infoSiteStudentGroupsList);

		infoSiteGroupsByShiftList.add(infoSiteGroupsByShift);
		infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);

		return infoSiteShiftsAndGroups;
	}

	private List readStudentGroupAndStudents(Integer groupPropertiesCode, Integer shiftCode)
			throws ExcepcaoInexistente, FenixServiceException, ExcepcaoPersistencia {

		List<InfoSiteStudentsAndShiftByStudentGroup> infoSiteStudentsAndShiftByStudentGroupList = new ArrayList<InfoSiteStudentsAndShiftByStudentGroup>();

		Grouping groupProperties = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesCode);
		if (groupProperties == null)
			return null;

		Shift shift = RootDomainObject.getInstance().readShiftByOID(shiftCode);

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
		allStudentGroups.addAll(groupProperties.getStudentGroups());

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
			infoSiteStudentsAndShiftByStudentGroup.setInfoStudentGroup(InfoStudentGroup
					.newInfoFromDomain(studentGroup));
			infoSiteStudentsAndShiftByStudentGroup.setInfoShift(InfoShift.newInfoFromDomain(turno));

			List attendsList = studentGroup.getAttends();

			List<InfoSiteStudentInformation> studentGroupAttendInformationList = new ArrayList<InfoSiteStudentInformation>();
			Iterator iterAttendsList = attendsList.iterator();
			InfoSiteStudentInformation infoSiteStudentInformation = null;
			Attends attend = null;

			while (iterAttendsList.hasNext()) {
				infoSiteStudentInformation = new InfoSiteStudentInformation();

				attend = (Attends) iterAttendsList.next();

				infoSiteStudentInformation.setNumber(attend.getAluno().getNumber());

				studentGroupAttendInformationList.add(infoSiteStudentInformation);

			}

			Collections.sort(studentGroupAttendInformationList, new BeanComparator("number"));

			infoSiteStudentsAndShiftByStudentGroup
					.setInfoSiteStudentInformationList(studentGroupAttendInformationList);
			infoSiteStudentsAndShiftByStudentGroupList.add(infoSiteStudentsAndShiftByStudentGroup);

			Collections.sort(infoSiteStudentsAndShiftByStudentGroupList, new BeanComparator(
					"infoStudentGroup.groupNumber"));

		}

		return infoSiteStudentsAndShiftByStudentGroupList;
	}

	/**
	 * @param component
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */

	private ISiteComponent getInfoSiteGroupProperties(InfoSiteGrouping component,
			Integer groupPropertiesCode) throws FenixServiceException, ExcepcaoPersistencia {

		InfoGrouping infoGrouping = readGroupProperties(groupPropertiesCode);
		component.setInfoGrouping(infoGrouping);
		return component;
	}

	private InfoGrouping readGroupProperties(Integer groupPropertiesCode) throws ExcepcaoInexistente,
			FenixServiceException, ExcepcaoPersistencia {
		Grouping groupProperties = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesCode);
		return InfoGroupingWithAttends.newInfoFromDomain(groupProperties);
	}

	/**
	 * @param shifts
	 * @param site
	 * @return
	 * @throws ExcepcaoPersistencia
	 */
	private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, Integer groupPropertiesCode,
			Integer studentGroupCode) throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoShift> infoShifts = new ArrayList<InfoShift>();
		ExecutionCourse executionCourse = null;

        Grouping groupProperties = RootDomainObject.getInstance().readGroupingByOID(groupPropertiesCode);
		if (groupProperties == null) {
			return null;
		}
		if (studentGroupCode != null) {
            StudentGroup studentGroup = RootDomainObject.getInstance().readStudentGroupByOID(studentGroupCode);
			if (studentGroup == null) {
				component.setShifts(null);
				return component;
			}

			component.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
		}

		IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory
				.getInstance();
		IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory
				.getGroupEnrolmentStrategyInstance(groupProperties);

		if (strategy.checkHasShift(groupProperties)) {

			List executionCourses = new ArrayList();
			executionCourses = groupProperties.getExecutionCourses();

			Iterator iterExecutionCourses = executionCourses.iterator();
			List<Shift> shifts = new ArrayList<Shift>();
			while (iterExecutionCourses.hasNext()) {
				ExecutionCourse executionCourse2 = (ExecutionCourse) iterExecutionCourses.next();
				List<Shift> someShifts = executionCourse2.getAssociatedShifts();

				shifts.addAll(someShifts);
			}

			if (shifts == null || shifts.isEmpty()) {

			} else {
				for (int i = 0; i < shifts.size(); i++) {
					Shift shift = (Shift) shifts.get(i);
					if (strategy.checkShiftType(groupProperties, shift)) {
						executionCourse = shift.getDisciplinaExecucao();
						InfoShift infoShift = new InfoShift(shift.getNome(), shift.getTipo(), shift
								.getLotacao(), InfoExecutionCourse.newInfoFromDomain(executionCourse));

						List lessons = shift.getAssociatedLessons();
						List<InfoLesson> infoLessons = new ArrayList<InfoLesson>();
						List<SchoolClass> classesShifts = shift.getAssociatedClasses();
						List<InfoClass> infoClasses = new ArrayList<InfoClass>();

						for (int j = 0; j < lessons.size(); j++)
							infoLessons.add(InfoLesson.newInfoFromDomain((Lesson) lessons.get(j)));

						infoShift.setInfoLessons(infoLessons);

						for (int j = 0; j < classesShifts.size(); j++)
							infoClasses.add(InfoClass.newInfoFromDomain(classesShifts.get(j)));
						infoShift.setInfoClasses(infoClasses);
						infoShift.setIdInternal(shift.getIdInternal());

						infoShifts.add(infoShift);
					}
				}
			}
		}

		component.setShifts(infoShifts);

		return component;
	}
}
