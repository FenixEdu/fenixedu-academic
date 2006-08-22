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
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadSummaries;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoBibliographicReference;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.dataTransferObject.InfoEvaluationMethod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseWithExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoItem;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoExecutionCourseAndCollections;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAnnouncement;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteAssociatedCurricularCourses;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteBibliography;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluationMarks;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluations;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteFirstPage;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSection;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShifts;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteSummaries;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteTimetable;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummary;
import net.sourceforge.fenixedu.dataTransferObject.InfoSummaryWithAll;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPerson;
import net.sourceforge.fenixedu.domain.Announcement;
import net.sourceforge.fenixedu.domain.BibliographicReference;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.EvaluationMethod;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Item;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

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

	public ISiteComponent getComponent(ISiteComponent component, Site site,
			ISiteComponent commonComponent, Integer sectionIndex, Integer curricularCourseId)
			throws FenixServiceException, ExcepcaoPersistencia {

		if (component instanceof InfoSiteCommon) {
			return getInfoSiteCommon((InfoSiteCommon) component, site);
		} else if (component instanceof InfoSiteFirstPage) {
			return getInfoSiteFirstPage((InfoSiteFirstPage) component, site);

		} else if (component instanceof InfoSiteAnnouncement) {
			return getInfoSiteAnnouncement((InfoSiteAnnouncement) component, site);
		} else if (component instanceof InfoEvaluationMethod) {
			return getInfoEvaluationMethod((InfoEvaluationMethod) component, site);
		} else if (component instanceof InfoSiteBibliography) {
			return getInfoSiteBibliography((InfoSiteBibliography) component, site);
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
		} else if (component instanceof InfoSiteEvaluations) {
			return getInfoSiteEvaluations((InfoSiteEvaluations) component, site);
		} else if (component instanceof InfoSiteEvaluationMarks) {
			return getInfoSiteEvaluationMarks((InfoSiteEvaluationMarks) component, site);
		} else if (component instanceof InfoSiteSummaries) {
			return getInfoSiteSummaries((InfoSiteSummaries) component, site);
		}
		return null;
	}

	private ISiteComponent getInfoSiteSummaries(InfoSiteSummaries component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		ExecutionCourse executionCourse = site.getExecutionCourse();

		// execution courses's lesson types for display to filter summary
		List lessonTypes = findLessonTypesExecutionCourse(executionCourse);

		// execution courses's shifts for display to filter summary
		List shifts = executionCourse.getAssociatedShifts();
		List infoShifts = new ArrayList();
		if (shifts != null && shifts.size() > 0) {
			infoShifts = (List) CollectionUtils.collect(shifts, new Transformer() {

				public Object transform(Object arg0) {
					Shift turno = (Shift) arg0;
					return InfoShiftWithInfoExecutionCourseAndCollections.newInfoFromDomain(turno);
				}
			});
		}

		// execution courses's professorships for display to filter summary
		final List<InfoProfessorship> infoProfessorships = new ArrayList<InfoProfessorship>(executionCourse.getProfessorshipsCount());
		for (final Professorship professorship : executionCourse.getProfessorshipsSet()) {
		    infoProfessorships.add(InfoProfessorship.newInfoFromDomain(professorship));
		}
		
		List summaries = null;
		if (component.getShiftType() != null) {
            summaries = executionCourse.readSummariesByShiftType(component.getShiftType());
		}

		if (component.getShiftId() != null && component.getShiftId().intValue() > 0) {
			Shift shiftSelected = RootDomainObject.getInstance().readShiftByOID(component.getShiftId());
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

		if (component.getTeacherId() != null && component.getTeacherId().intValue() > 0) {
            Professorship professorshipSelected = RootDomainObject.getInstance().readProfessorshipByOID(
                    component.getTeacherId());

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

		if (component.getTeacherId() != null && component.getTeacherId().equals(new Integer(-1))) {
            List summariesByTeacher = executionCourse.readSummariesOfTeachersWithoutProfessorship();

			if (summaries != null) {
				summaries = (List) CollectionUtils.intersection(summaries, summariesByTeacher);
			} else {
				summaries = summariesByTeacher;
			}
		}

		if ((component.getShiftType() == null)
				&& (component.getShiftId() == null || component.getShiftId().intValue() == 0)
				&& (component.getTeacherId() == null || component.getTeacherId().intValue() == 0)) {

            summaries = executionCourse.getAssociatedSummaries();
		}

		List result = new ArrayList();
		Iterator iter = summaries.iterator();
		while (iter.hasNext()) {
			Summary summary = (Summary) iter.next();
			InfoSummary infoSummary = InfoSummaryWithAll.newInfoFromDomain(summary);
			result.add(infoSummary);
		}

		component.setInfoSummaries(result);
		component.setInfoSite(copyISite2InfoSite(site));
		component.setExecutionCourse(InfoExecutionCourseWithExecutionPeriod
				.newInfoFromDomain(executionCourse));

		component.setLessonTypes(lessonTypes);
		List infoShiftsOnlyType = infoShifts;
		if (component.getShiftType() != null) {
			final ShiftType shiftType = component.getShiftType();
			infoShiftsOnlyType = (List) CollectionUtils.select(infoShifts, new Predicate() {

				public boolean evaluate(Object arg0) {
					return ((InfoShift) arg0).getTipo().equals(shiftType);
				}
			});
		}
		component.setInfoShifts(infoShiftsOnlyType);
		component.setInfoProfessorships(infoProfessorships);

		return component;
	}

	private List findLessonTypesExecutionCourse(ExecutionCourse executionCourse) {
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

    private List findLesson(ExecutionCourse executionCourse, Shift shift) throws ExcepcaoPersistencia {
        return ReadSummaries.findLesson(executionCourse, shift);
	}

	private List allSummaries(List summaries, List summariesByExecutionCourse) {
		return ReadSummaries.allSummaries(summaries, summariesByExecutionCourse);
	}

	private ISiteComponent getInfoSiteEvaluations(InfoSiteEvaluations component, Site site) {
		ExecutionCourse executionCourse = site.getExecutionCourse();
		List<Evaluation> evaluations = executionCourse.getAssociatedEvaluations();
		component.setEvaluations(evaluations);
		return component;
	}

	private ISiteComponent getInfoSiteEvaluationMarks(InfoSiteEvaluationMarks component, Site site) {
		final Integer evaluationID = component.getEvaluationID();

		final ExecutionCourse executionCourse = site.getExecutionCourse();
		final List<Evaluation> evaluations = executionCourse.getAssociatedEvaluations();
		for (final Evaluation evaluation : evaluations) {
			if (evaluationID.equals(evaluation.getIdInternal())) {
				component.setEvaluation(evaluation);
				break;
			}
		}
		return component;
	}

	private ISiteComponent getInfoSiteCommon(InfoSiteCommon component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		List allSections = null;
		List infoSectionsList = null;

		List infoCurricularCourseList = null;
		List infoCurricularCourseListByDegree = null;
		// read sections

		allSections = site.getAssociatedSections();

		// build the result of this service
		Iterator iterator = allSections.iterator();
		infoSectionsList = new ArrayList(allSections.size());

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
		infoExecutionCourse = InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(site
				.getExecutionCourse());
		component.setExecutionCourse(infoExecutionCourse);
		return component;
	}

	private ISiteComponent getInfoSiteSection(InfoSiteSection component, Site site,
			InfoSiteCommon commonComponent, Integer sectionIndex) throws FenixServiceException,
			ExcepcaoPersistencia {
        
        final InfoSection infoSection = (InfoSection) commonComponent.getSections().get(
                sectionIndex.intValue());
		component.setSection(infoSection);

        final Section section = RootDomainObject.getInstance().readSectionByOID(infoSection.getIdInternal());

		final List<InfoItem> infoItemsList = new ArrayList<InfoItem>(section.getAssociatedItemsCount());
		for (final Item item : section.getAssociatedItems()) {
            final InfoItem infoItem = InfoItem.newInfoFromDomain(item);
            infoItemsList.add(infoItem);
        }

		Collections.sort(infoItemsList);
		component.setItems(infoItemsList);
		return component;
	}

	private ISiteComponent getInfoSiteShifts(InfoSiteShifts component, Site site)
			throws FenixServiceException {
		List shiftsWithAssociatedClassesAndLessons = new ArrayList();

		ExecutionCourse disciplinaExecucao = site.getExecutionCourse();
		List<Shift> shifts = disciplinaExecucao.getAssociatedShifts();

		for (final Shift shift : shifts) {
			List<Lesson> lessons = shift.getAssociatedLessons();
			List infoLessons = new ArrayList(lessons.size());
			List<SchoolClass> classesShifts = shift.getAssociatedClasses();
			List infoClasses = new ArrayList(classesShifts.size());

			for (final Lesson lesson : lessons) {
				infoLessons.add(InfoLesson.newInfoFromDomain(lesson));
			}

			for (final SchoolClass schoolClass : classesShifts) {
				infoClasses.add(InfoClass.newInfoFromDomain(schoolClass));
			}

			InfoShift infoShift = InfoShiftWithInfoExecutionCourseAndCollections
					.newInfoFromDomain(shift);

			InfoShiftWithAssociatedInfoClassesAndInfoLessons shiftWithAssociatedClassesAndLessons = new InfoShiftWithAssociatedInfoClassesAndInfoLessons(
					infoShift, infoLessons, infoClasses);

			shiftsWithAssociatedClassesAndLessons.add(shiftWithAssociatedClassesAndLessons);
		}

		component.setShifts(shiftsWithAssociatedClassesAndLessons);
		component.setInfoExecutionPeriodName(site.getExecutionCourse().getExecutionPeriod().getName());
		component.setInfoExecutionYearName(site.getExecutionCourse().getExecutionPeriod()
				.getExecutionYear().getYear());
		return component;
	}

	private ISiteComponent getInfoSiteTimetable(InfoSiteTimetable component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {
		List infoLessonList = null;

		ExecutionCourse executionCourse = site.getExecutionCourse();

		List aulas = new ArrayList();

		List shifts = executionCourse.getAssociatedShifts();
		for (int i = 0; i < shifts.size(); i++) {
			Shift shift = (Shift) shifts.get(i);
			List aulasTemp = shift.getAssociatedLessons();

			aulas.addAll(aulasTemp);
		}

		Iterator iterator = aulas.iterator();
		infoLessonList = new ArrayList();
		while (iterator.hasNext()) {
			infoLessonList.add(InfoLesson.newInfoFromDomain((Lesson) iterator.next()));
		}

		component.setLessons(infoLessonList);
		return component;
	}

	private ISiteComponent getInfoSiteAssociatedCurricularCourses(
			InfoSiteAssociatedCurricularCourses component, Site site) {
		List infoCurricularCourseList = new ArrayList();

		ExecutionCourse executionCourse = site.getExecutionCourse();

		infoCurricularCourseList = readCurricularCourses(executionCourse);

		component.setAssociatedCurricularCourses(infoCurricularCourseList);
		return component;
	}

	private ISiteComponent getInfoSiteBibliography(InfoSiteBibliography component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {
		ExecutionCourse executionCourse = site.getExecutionCourse();

		List references = executionCourse.getAssociatedBibliographicReferences();

		Iterator iterator = references.iterator();
		List infoBibRefs = new ArrayList();
		while (iterator.hasNext()) {
			BibliographicReference bibRef = (BibliographicReference) iterator.next();

			InfoBibliographicReference infoBibRef = copyFromDomain(bibRef);
			infoBibRefs.add(infoBibRef);
		}
		if (!infoBibRefs.isEmpty()) {
			component.setBibliographicReferences(infoBibRefs);
		}

		return component;
	}

	private ISiteComponent getInfoEvaluationMethod(InfoEvaluationMethod component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

        final EvaluationMethod evaluationMethod = site.getExecutionCourse().getEvaluationMethod();
		if (evaluationMethod != null) {
			component = copyFromDomain(evaluationMethod);
		}

		return component;
	}

	private ISiteComponent getInfoSiteFirstPage(InfoSiteFirstPage component, Site site)
			throws FenixServiceException, ExcepcaoPersistencia {

		ExecutionCourse executionCourse = site.getExecutionCourse();

		InfoAnnouncement infoAnnouncement = readLastAnnouncement(executionCourse);

		List infoAnnouncements = readLastFiveAnnouncements(executionCourse);

		List responsibleInfoTeachersList = readResponsibleTeachers(executionCourse);

		List lecturingInfoTeachersList = readLecturingTeachers(executionCourse);

		// set all the required information to the component

		if (!infoAnnouncements.isEmpty()) {
			infoAnnouncements.remove(0);
		}

		component.setLastAnnouncement(infoAnnouncement);
		component.setLastFiveAnnouncements(infoAnnouncements);

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

		return component;
	}

	private List readLastFiveAnnouncements(ExecutionCourse executionCourse) {
		Set<Announcement> announcements = executionCourse.getSite().getSortedAnnouncements();

		int count = 5;
		List infoAnnouncementsList = new ArrayList(count);

		for (Announcement ann : announcements) {
			infoAnnouncementsList.add(copyFromDomain(ann));
			count--;
			if (count == 0) {
				break;
			}
		}

		return infoAnnouncementsList;
	}

	private InfoSiteAnnouncement getInfoSiteAnnouncement(InfoSiteAnnouncement component, Site site) {
		Set<Announcement> announcements = site.getSortedAnnouncements();
		List infoAnnouncementsList = new ArrayList(announcements.size());

		for (Announcement ann : announcements) {
			infoAnnouncementsList.add(copyFromDomain(ann));
		}

		component.setAnnouncements(infoAnnouncementsList);
		return component;
	}

	private List readLecturingTeachers(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
		List domainLecturingTeachersList = executionCourse.getProfessorships();

		List lecturingInfoTeachersList = new ArrayList();
		if (domainLecturingTeachersList != null) {

			Iterator iter = domainLecturingTeachersList.iterator();
			while (iter.hasNext()) {
				Professorship professorship = (Professorship) iter.next();
				Teacher teacher = professorship.getTeacher();
				InfoTeacher infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
				lecturingInfoTeachersList.add(infoTeacher);
			}
		}
		return lecturingInfoTeachersList;
	}

	private List readResponsibleTeachers(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {

		List responsibleDomainTeachersList = executionCourse.responsibleFors();

		List responsibleInfoTeachersList = new ArrayList();
		if (responsibleDomainTeachersList != null) {
			Iterator iter = responsibleDomainTeachersList.iterator();
			while (iter.hasNext()) {
				Professorship professorship = (Professorship) iter.next();
				Teacher teacher = professorship.getTeacher();
				InfoTeacher infoTeacher = InfoTeacherWithPerson.newInfoFromDomain(teacher);
				responsibleInfoTeachersList.add(infoTeacher);
			}

		}
		return responsibleInfoTeachersList;
	}

	private InfoAnnouncement readLastAnnouncement(ExecutionCourse executionCourse)
			throws ExcepcaoPersistencia {
		Announcement announcement = executionCourse.getSite().getLastAnnouncement();
		InfoAnnouncement infoAnnouncement = null;
		if (announcement != null) {
			infoAnnouncement = copyFromDomain(announcement);
		}
		return infoAnnouncement;
	}

	private List readCurricularCourses(ExecutionCourse executionCourse) {
		List infoCurricularCourseScopeList;
		List infoCurricularCourseList = new ArrayList();
		if (executionCourse.getAssociatedCurricularCourses() != null)
			for (int i = 0; i < executionCourse.getAssociatedCurricularCourses().size(); i++) {
				CurricularCourse curricularCourse = executionCourse.getAssociatedCurricularCourses()
						.get(i);
				InfoCurricularCourse infoCurricularCourse = copyFromDomain(curricularCourse);
				infoCurricularCourseScopeList = new ArrayList();
				for (int j = 0; j < curricularCourse.getScopes().size(); j++) {
					CurricularCourseScope curricularCourseScope = curricularCourse.getScopes().get(j);
					InfoCurricularCourseScope infoCurricularCourseScope = copyFromDomain(curricularCourseScope);
					infoCurricularCourseScopeList.add(infoCurricularCourseScope);
				}

				infoCurricularCourse.setInfoScopes(infoCurricularCourseScopeList);
				infoCurricularCourseList.add(infoCurricularCourse);
			}

		return infoCurricularCourseList;
	}

	/**
	 * Curricular courses list organized by degree (curricular information in
	 * first page).
	 */
	private List readCurricularCoursesOrganizedByDegree(ExecutionCourse executionCourse) {
		final List curricularCourses = executionCourse.getAssociatedCurricularCourses();
		final int estimatedResultSize = curricularCourses.size();

		final List infoCurricularCourses = new ArrayList(estimatedResultSize);
		final Set degreesCodes = new HashSet(estimatedResultSize);
		for (final Iterator iterator = curricularCourses.iterator(); iterator.hasNext();) {
			final CurricularCourse curricularCourse = (CurricularCourse) iterator.next();
			final String degreeCode = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();
			if (!degreesCodes.contains(degreeCode)) {
				final InfoCurricularCourse infoCurricularCourse = copyFromDomain(curricularCourse);
				// final InfoCurricularCourse infoCurricularCourse =
				// InfoCurricularCourse.newInfoFromDomain(curricularCourse);
				infoCurricularCourses.add(infoCurricularCourse);
				infoCurricularCourse.setInfoScopes(new ArrayList());

				for (final Iterator scopeIterator = curricularCourse.getScopes().iterator(); scopeIterator
						.hasNext();) {
					final CurricularCourseScope curricularCourseScope = (CurricularCourseScope) scopeIterator
							.next();
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
		// infoCurricularCourse.getInfoDegreeCurricularPlan().getInfoDegree().getSigla();
		//			
		// if (!infoCurricularCourseList.isEmpty() &&
		// StringUtils.contains(allSiglas.toString(),currentSigla)) {
		// for (int k = 0; k < infoCurricularCourseList.size(); k++) {
		// String sigla = ((InfoDegree) ((InfoDegreeCurricularPlan)
		// ((InfoCurricularCourse)
		// ((List)
		// infoCurricularCourseList.get(k)).get(0)).getInfoDegreeCurricularPlan()).getInfoDegree()).getSigla();
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
			infoSection.setIdInternal(section.getIdInternal());
			infoSection.setName(section.getName());
			infoSection.setSectionOrder(section.getSectionOrder());
			infoSection.setSuperiorInfoSection(copyISection2InfoSection(section.getSuperiorSection()));
			infoSection.setInfoSite(copyISite2InfoSite(section.getSite()));
		}
		return infoSection;
	}

	/**
	 * @param site
	 * @return
	 */
	private InfoSite copyISite2InfoSite(Site site) {
		InfoSite infoSite = InfoSite.newInfoFromDomain(site);
		if (site != null) {
			infoSite.setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod
					.newInfoFromDomain(site.getExecutionCourse()));
		}
		return infoSite;
	}

	/**
	 * @param announcement
	 * @return
	 */
	private InfoAnnouncement copyFromDomain(Announcement announcement) {
		return InfoAnnouncement.newInfoFromDomain(announcement);
	}

	/**
	 * @param evaluationMethod
	 * @return
	 */
	private InfoEvaluationMethod copyFromDomain(EvaluationMethod evaluationMethod) {
		InfoEvaluationMethod infoEvaluationMethod = null;
		if (evaluationMethod != null) {
			infoEvaluationMethod = new InfoEvaluationMethod();
			infoEvaluationMethod.setIdInternal(evaluationMethod.getIdInternal());
			infoEvaluationMethod.setEvaluationElements(evaluationMethod.getEvaluationElements());
			infoEvaluationMethod.setEvaluationElementsEn(evaluationMethod.getEvaluationElementsEn());
		}
		return infoEvaluationMethod;
	}

	/**
	 * @param bibRef
	 * @return
	 */
	private InfoBibliographicReference copyFromDomain(BibliographicReference bibRef) {
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
