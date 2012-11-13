package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.CourseLoad;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExternalTeacherAuthorization;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.TeacherAuthorization;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalRegime;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.research.activity.JournalIssue;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.domain.research.result.publication.Article;
import net.sourceforge.fenixedu.domain.research.result.publication.Book;
import net.sourceforge.fenixedu.domain.research.result.publication.BookPart;
import net.sourceforge.fenixedu.domain.research.result.publication.Inproceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.Manual;
import net.sourceforge.fenixedu.domain.research.result.publication.OtherPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.Proceedings;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;
import net.sourceforge.fenixedu.domain.research.result.publication.TechnicalReport;
import net.sourceforge.fenixedu.domain.research.result.publication.Unstructured;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeacherService;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class TeacherCurricularInformation implements Serializable {
    protected Teacher teacher;
    protected Degree degree;
    protected SortedSet<ExecutionSemester> executionSemesters = new TreeSet<ExecutionSemester>(
	    ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
    protected SortedSet<QualificationBean> qualificationBeans = new TreeSet<QualificationBean>(new ReverseComparator(
	    new Comparator<QualificationBean>() {
		@Override
		public int compare(final QualificationBean o1, final QualificationBean o2) {
		    int compareYear = o1.type.compareTo(o2.type);
		    if (compareYear == 0) {
			if (o1.year == null && o2.year == null) {
			    return 0;
			} else if (o1.year == null) {
			    return -1;
			} else if (o2.year == null) {
			    return 1;
			}
			return o1.year.compareTo(o2.year);
		    }
		    return compareYear;
		}
	    }));

    protected Set<LecturedCurricularUnit> lecturedUCsOnCycle;
    protected Set<LecturedCurricularUnit> lecturedUCsOnOtherCycles;

    public TeacherCurricularInformation(Teacher teacher, Degree degree, List<ExecutionSemester> executionSemester) {
	super();
	this.teacher = teacher;
	this.degree = degree;
	this.executionSemesters.addAll(executionSemester);
	for (Qualification qualification : teacher.getPerson().getAssociatedQualifications()) {
	    if (qualification.getType() != null
		    && (qualification.getType().equals(QualificationType.DEGREE)
			    || qualification.getType().equals(QualificationType.MASTER_DEGREE)
			    || qualification.getType().equals(QualificationType.DOCTORATE_DEGREE) || qualification.getType()
			    .name().startsWith("DOCTORATE_DEGREE"))) {
		this.qualificationBeans.add(new QualificationBean(qualification));
	    }
	}
	setlecturedUCs();
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public SortedSet<QualificationBean> getQualifications() {
	return qualificationBeans;
    }

    public void setQualifications(SortedSet<QualificationBean> qualificationBeans) {
	this.qualificationBeans = qualificationBeans;
    }

    public QualificationBean getCurrentQualification() {
	return qualificationBeans.size() != 0 ? qualificationBeans.first() : null;
    }

    public SortedSet<QualificationBean> getOtherQualifications() {
	SortedSet<QualificationBean> otherQualifications = new TreeSet<QualificationBean>(qualificationBeans);
	if (qualificationBeans.size() != 0) {
	    otherQualifications.remove(qualificationBeans.first());
	}
	return otherQualifications;
    }

    public String getUnitName() {
	Department lastWorkingDepartment = getTeacher().getLastWorkingDepartment(
		executionSemesters.first().getBeginDateYearMonthDay(), executionSemesters.last().getEndDateYearMonthDay());
	return lastWorkingDepartment == null ? null : lastWorkingDepartment.getName();
    }

    public String getProfessionalCategoryName() {
	ProfessionalCategory lastCategory = getTeacher().getLastCategory(
		executionSemesters.first().getBeginDateYearMonthDay().toLocalDate(),
		executionSemesters.last().getEndDateYearMonthDay().toLocalDate());
	if (lastCategory != null) {
	    if (lastCategory.getName().getContent().equalsIgnoreCase("Assistente")) {
		return "Assistente";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Assistente Convidado")) {
		return "Assistente convidado";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Assistente")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Equiparado Assistente")) {
		return "Equiparado a Assistente";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Assistente Convidado")) {
		return "Equiparado a Assistente";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip. Prof. Auxiliar")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Equiparado Professor Auxiliar")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Professor Auxiliar")) {
		return "Professor Auxiliar";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Assistente Estagiario")) {
		return "Assistente Estagiário";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Monitor S/Lic")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Monitor-E.C.D.U. c/Licenciatura")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Monitor-E.C.D.U.s/Licenciatura")) {
		return "Monitor";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Equip.Prof.Auxiliar Convidado")
		    || lastCategory.getName().getContent().equalsIgnoreCase("Prof Auxiliar Convidado")) {
		return "Professor Auxiliar convidado";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Associado")) {
		return "Professor Associado";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Associado Convidado")) {
		return "Professor Associado convidado";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Catedrático")) {
		return "Professor Catedrático";
	    } else if (lastCategory.getName().getContent().equalsIgnoreCase("Professor Catedrático Convidado")) {
		return "Professor Catedrático convidado";
	    }
	}
	System.out.println("Invalid category: " + lastCategory.getName().getContent());
	return lastCategory == null ? null : lastCategory.getName().getContent();
    }

    public Double getProfessionalRegimeTime() {
	double maxRegimeTime = 100.0;
	PersonProfessionalData personProfessionalData = getTeacher().getPerson().getPersonProfessionalData();
	ProfessionalRegime lastProfessionalRegime = null;
	if (personProfessionalData != null) {
	    GiafProfessionalData giafProfessionalData = personProfessionalData.getGiafProfessionalData();
	    if (giafProfessionalData != null) {
		lastProfessionalRegime = personProfessionalData.getLastProfessionalRegime(giafProfessionalData,
			executionSemesters.first().getBeginDateYearMonthDay().toLocalDate(), executionSemesters.last()
				.getEndDateYearMonthDay().toLocalDate());
	    }
	}
	if (lastProfessionalRegime != null) {
	    String regime = StringNormalizer.normalize(lastProfessionalRegime.getName().getContent());
	    if (regime.matches(".*?" + StringNormalizer.normalize("Tempo Integral") + ".*?")) {
		return maxRegimeTime;
	    } else if (regime.matches(".*?" + StringNormalizer.normalize("Exclusividade") + ".*?")) {
		return maxRegimeTime;
	    } else if (regime.matches(".*?" + StringNormalizer.normalize("Tempo Parcial") + ".*?")) {
		Integer weighting = lastProfessionalRegime.getWeighting();
		if (weighting != null) {
		    return Math.min(maxRegimeTime, weighting);
		}
	    }
	}
	Double authorizationRegimeTime = getAuthorizationRegimeTime(executionSemesters.last());
	if (authorizationRegimeTime == 0.0) {
	    authorizationRegimeTime = getAuthorizationRegimeTime(executionSemesters.first());
	}

	return authorizationRegimeTime;
    }

    protected Double getAuthorizationRegimeTime(ExecutionSemester executionSemester) {
	TeacherAuthorization teacherAuthorization = getTeacher().getTeacherAuthorization(executionSemester);
	if (teacherAuthorization != null) {
	    Double lessonHours = ((ExternalTeacherAuthorization) teacherAuthorization).getLessonHours();
	    return new Double(Math.round((lessonHours * 100) / 12));
	}
	return 0.0;
    }

    public List<String> getTop5ResultParticipation() {
	List<String> resultParticipations = new ArrayList<String>();
	for (ResultParticipation participation : getTeacher().getPerson().getResultParticipationsSet()) {
	    ResearchResult result = participation.getResult();
	    if (participation.getRole().equals(ResultParticipationRole.Author) && result instanceof ResearchResultPublication) {
		if (!(result instanceof Manual || result instanceof TechnicalReport || result instanceof OtherPublication || result instanceof Unstructured)) {
		    resultParticipations.add(getResearchDescription(participation));
		}
	    }
	}
	return resultParticipations.subList(0, Math.min(5, resultParticipations.size()));
    }

    public List<String> getTop5ProfessionalCareer() {
	List<String> result = new ArrayList<String>();

	List<ProfessionalCareer> sorted = new ArrayList<ProfessionalCareer>();
	for (Career career : teacher.getPerson().getAssociatedCareersSet()) {
	    if (career instanceof ProfessionalCareer) {
		sorted.add((ProfessionalCareer) career);
	    }
	}
	Collections.sort(sorted, ProfessionalCareer.CAREER_DATE_COMPARATOR);
	for (ProfessionalCareer professionalCareer : sorted.subList(0, Math.min(5, sorted.size()))) {
	    result.add(professionalCareer.getBeginYear()
		    + (professionalCareer.getEndYear() != null ? " - " + professionalCareer.getEndYear() : "") + " "
		    + professionalCareer.getFunction() + " (" + professionalCareer.getEntity() + ")");
	}
	return result;
    }

    public String getResearchDescription(ResultParticipation participation) {
	List<String> resultDescription = new ArrayList<String>();

	ResearchResultPublication publication = (ResearchResultPublication) participation.getResult();
	resultDescription.add(filter(publication.getTitle()));
	List<String> parts = new ArrayList<String>();
	for (ResultParticipation participant : publication.getOrderedAuthorsResultParticipations()) {
	    parts.add(participant.getPerson().getNickname());
	}
	resultDescription.add(filter(StringUtils.join(parts, ", ")));
	if (publication instanceof Article) {
	    JournalIssue issue = ((Article) publication).getArticleAssociation().getJournalIssue();
	    String journal = filter(issue.getScientificJournal().getName());

	    String volume = filter(issue.getVolume()
		    + (StringUtils.isNotBlank(issue.getNumber()) ? " (" + issue.getNumber() + ")" : ""));
	    insert(resultDescription, join(journal, volume, publication.getYear()));
	    insert(resultDescription, filter(issue.getPublisher()));
	} else if (publication instanceof Book) {
	    Book book = (Book) publication;
	    insert(resultDescription, filter(book.getPublisher()));
	    insert(resultDescription, book.getYear());
	    insert(resultDescription, filter(book.getEdition()));
	} else if (publication instanceof BookPart) {
	    BookPart bookPart = (BookPart) publication;
	    insert(resultDescription, filter(bookPart.getBookTitle()));
	    insert(resultDescription, filter(bookPart.getPublisher()));
	    insert(resultDescription, publication.getYear());
	    insert(resultDescription, filter(bookPart.getEdition()));
	} else if (publication instanceof Inproceedings) {
	    insert(resultDescription, publication.getYear());
	    insert(resultDescription, filter(publication.getPublisher()));
	    insert(resultDescription, filter(((Inproceedings) publication).getEventEdition().getFullName()));
	} else if (publication instanceof Proceedings) {
	    insert(resultDescription, publication.getYear());
	    insert(resultDescription, filter(((Proceedings) publication).getEventEdition().getFullName()));
	}

	return StringUtils.join(resultDescription, ", ");
    }

    private String filter(String... text) {
	Set<String> parts = new HashSet<String>();
	for (String string : text) {
	    if (StringUtils.isNotBlank(string)) {
		parts.add(string.replace('\n', ' ').replace('\r', ' ').trim());
	    }
	}
	return StringUtils.join(parts, ", ");
    }

    private String join(String journal, String volume, Integer year) {
	StringBuilder builder = new StringBuilder();
	if (StringUtils.isNotBlank(journal)) {
	    builder.append(journal);
	}
	if (StringUtils.isNotBlank(volume)) {
	    builder.append(" - " + volume);
	}
	if (year != null) {
	    builder.append(" " + year);
	}
	return builder.toString();
    }

    private void insert(List<String> publication, Integer part) {
	if (part != null) {
	    publication.add(Integer.toString(part));
	}
    }

    private void insert(List<String> publication, String part) {
	if (StringUtils.isNotBlank(part)) {
	    publication.add(part);
	}
    }

    public void setlecturedUCs() {
	lecturedUCsOnCycle = new HashSet<LecturedCurricularUnit>();
	lecturedUCsOnOtherCycles = new HashSet<LecturedCurricularUnit>();
	for (ExecutionSemester executionSemester : executionSemesters) {
	    for (Professorship professorship : teacher.getProfessorships(executionSemester)) {
		if (professorship.getExecutionCourse().getDegreesSortedByDegreeName().contains(degree)) {
		    lecturedUCsOnCycle.addAll(getLecturedCurricularUnitForProfessorship(professorship, executionSemester));
		} else {
		    lecturedUCsOnOtherCycles.addAll(getLecturedCurricularUnitForProfessorship(professorship, executionSemester));
		}
	    }
	    for (ThesisEvaluationParticipant thesisEvaluationParticipant : teacher.getPerson().getThesisEvaluationParticipants(
		    executionSemester)) {
		ExecutionCourse executionCourse = thesisEvaluationParticipant.getThesis().getEnrolment()
			.getExecutionCourseFor(executionSemester);
		if (executionCourse != null) {
		    if (executionCourse.getDegreesSortedByDegreeName().contains(degree)) {
			lecturedUCsOnCycle.add(new LecturedCurricularUnit(executionCourse.getDegreePresentationString(),
				"Dissertação", null, null));
		    } else {
			lecturedUCsOnOtherCycles.add(new LecturedCurricularUnit(executionCourse.getDegreePresentationString(),
				"Dissertação " + degree.getName(), null, null));
		    }
		}
	    }
	    if (degree.getPhdProgram() != null) {
		for (PhdIndividualProgramProcess phdIndividualProgramProcess : teacher.getPerson()
			.getPhdIndividualProgramProcesses()) {
		    if (phdIndividualProgramProcess.isActive(executionSemester.getAcademicInterval().toInterval())
			    && phdIndividualProgramProcess.isGuiderOrAssistentGuider(teacher.getPerson())
			    && teacher.isActiveOrHasAuthorizationForSemester(executionSemester)) {
			if (phdIndividualProgramProcess.getPhdProgram().equals(degree.getPhdProgram())) {
			    lecturedUCsOnCycle.add(new LecturedCurricularUnit(phdIndividualProgramProcess.getPhdProgram()
				    .getName().getContent(), "Dissertação", null, null));
			} else {
			    lecturedUCsOnOtherCycles.add(new LecturedCurricularUnit(phdIndividualProgramProcess.getPhdProgram()
				    .getName().getContent(), "Dissertação " + degree.getName(), null, null));
			}
		    }
		}
	    }
	}
	return;
    }

    public Set<LecturedCurricularUnit> getLecturedUCsOnCycle() {
	return lecturedUCsOnCycle;
    }

    public Set<LecturedCurricularUnit> getLecturedUCsOnOtherCycles() {
	return lecturedUCsOnOtherCycles;
    }

    public String getDegreeSiglas(ExecutionCourse executionCourse) {
	Set<String> degreeSiglas = new HashSet<String>();
	for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
	    degreeSiglas.add(curricularCourse.getDegreeCurricularPlan().getDegree().getSigla());
	}
	return StringUtils.join(degreeSiglas, ", ");
    }

    protected List<LecturedCurricularUnit> getLecturedCurricularUnitForProfessorship(Professorship professorship,
	    ExecutionSemester executionSemester) {
	List<LecturedCurricularUnit> result = new ArrayList<LecturedCurricularUnit>();
	Map<String, Double> hoursByTypeMap = new HashMap<String, Double>();
	TeacherService teacherService = teacher.getTeacherServiceByExecutionPeriod(executionSemester);
	if (teacherService != null) {
	    List<DegreeTeachingService> degreeTeachingServices = teacherService
		    .getDegreeTeachingServiceByProfessorship(professorship);
	    for (DegreeTeachingService degreeTeachingService : degreeTeachingServices) {
		for (CourseLoad courseLoad : degreeTeachingService.getShift().getCourseLoads()) {
		    Double duration = hoursByTypeMap.get(courseLoad.getType().getSiglaTipoAula());
		    Double weeklyHours = courseLoad.getTotalQuantity().doubleValue()
			    * (degreeTeachingService.getPercentage().doubleValue() / 100);
		    hoursByTypeMap.put(courseLoad.getType().getSiglaTipoAula(), duration == null ? weeklyHours : duration
			    + weeklyHours);
		}
	    }
	}
	String name = professorship.getExecutionCourse().getName();
	if (hoursByTypeMap.isEmpty()) {
	    result.add(new LecturedCurricularUnit(professorship.getExecutionCourse().getDegreePresentationString(), name, null,
		    null));
	} else {
	    for (String shiftType : hoursByTypeMap.keySet()) {
		result.add(new LecturedCurricularUnit(professorship.getExecutionCourse().getDegreePresentationString(), name,
			shiftType, hoursByTypeMap.get(shiftType)));
	    }
	}
	return result;
    }

    public class QualificationBean {
	protected QualificationType type;
	protected String degree;
	protected String scientificArea;
	protected Integer year;
	protected String institution;
	protected String classification;

	public QualificationBean(Qualification qualification) {
	    type = qualification.getType();
	    degree = qualification.getType() != null ? qualification.getType().getLocalizedName() : "Sem Grau";
	    if (qualification.getType().name().startsWith("DOCTORATE_DEGREE")) {
		degree = QualificationType.DOCTORATE_DEGREE.getLocalizedName();
	    }
	    scientificArea = qualification.getDegree();
	    year = qualification.getYear() != null ? Integer.parseInt(qualification.getYear()) : null;
	    institution = qualification.getSchool();
	    classification = qualification.getMark();
	}

	public QualificationType getType() {
	    return type;
	}

	public String getDegree() {
	    return degree;
	}

	public String getScientificArea() {
	    return scientificArea;
	}

	public Integer getYear() {
	    return year;
	}

	public String getInstitution() {
	    return institution;
	}

	public String getClassification() {
	    return classification;
	}

    }

    public class LecturedCurricularUnit {
	protected String degree;
	protected String name;
	protected String shiftType;
	protected Double hours;

	public LecturedCurricularUnit(String degree, String name, String shiftType, Double hoursValue) {
	    this.degree = degree;
	    this.name = name;
	    this.shiftType = getShiftType(shiftType);
	    this.hours = hoursValue == null ? 0.0 : new Double(Math.round(hoursValue * 100.0) / 100.0);
	}

	private String getShiftType(String shiftType) {
	    if (shiftType == null) {
		return null;
	    } else if (shiftType.equals("T") || shiftType.equals("TP") || shiftType.equals("TC") || shiftType.equals("S")
		    || shiftType.equals("E") || shiftType.equals("OT")) {
		return shiftType;
	    } else if (shiftType.equals("L")) {
		return "PL";
	    } else if (shiftType.equals("PB")) {
		return "TP";
	    }
	    System.out.println("Invalid shift type: " + shiftType);
	    return "O";
	}

	public String getName() {
	    return name;
	}

	public String getShiftType() {
	    return shiftType;
	}

	public Double getHours() {
	    return hours;
	}

	public String getDegree() {
	    return degree;
	}

	@Override
	public boolean equals(Object obj) {
	    if (obj instanceof LecturedCurricularUnit) {
		LecturedCurricularUnit o = (LecturedCurricularUnit) obj;
		return equal(getDegree(), o.getDegree()) && equal(getName(), o.getName())
			&& equal(getShiftType(), o.getShiftType()) && equal(getHours(), o.getHours());
	    }
	    return false;
	}

	protected boolean equal(Object obj1, Object obj2) {
	    if (obj1 == null && obj2 == null) {
		return true;
	    }
	    if (obj1 == null || obj2 == null) {
		return false;
	    }
	    return obj1.equals(obj2);
	}

	@Override
	public int hashCode() {
	    return getDegree().hashCode() + getName().hashCode() + (getShiftType() != null ? getShiftType().hashCode() : 0)
		    + (getHours() != null ? getHours().hashCode() : 0);
	}
    }

}
