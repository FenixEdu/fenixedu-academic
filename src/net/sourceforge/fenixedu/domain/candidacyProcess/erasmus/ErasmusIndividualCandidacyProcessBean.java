package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessDocumentUploadBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFileType;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.period.ErasmusCandidacyPeriod;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ErasmusIndividualCandidacyProcessBean extends IndividualCandidacyProcessBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Set<CurricularCourse> selectedCurricularCourses;

    private ErasmusStudentDataBean erasmusStudentDataBean;

    private boolean toAccessFenix;

    private Boolean validatedByGri;

    private Boolean validatedByErasmusCoordinator;

    private String alertSubject;

    private String alertBody;

    private Boolean createAlert;

    private Boolean sendEmail;

    private StorkAttributesList personalFieldsFromStork;

    private NationalIdCardAvoidanceQuestion nationalIdCardAvoidanceQuestion;

    private String idCardAvoidanceOtherReason;

    public ErasmusIndividualCandidacyProcessBean() {
	setCandidacyDate(new LocalDate());
	initializeDocumentUploadBeans();
	setSelectedCurricularCourses(new HashSet<CurricularCourse>());
	setSendEmail(false);

	this.toAccessFenix = false;
    }

    public ErasmusIndividualCandidacyProcessBean(CandidacyProcess candidacyProcess) {
	this();
	setCandidacyProcess(candidacyProcess);
	setErasmusStudentDataBean(new ErasmusStudentDataBean(getCandidacyProcess()));
    }

    public ErasmusIndividualCandidacyProcessBean(final ErasmusIndividualCandidacyProcess process) {
	setIndividualCandidacyProcess(process);
	setCandidacyProcess(process.getCandidacyProcess());
	setSelectedCurricularCourses(new HashSet<CurricularCourse>(process.getCandidacy().getCurricularCoursesSet()));
	setErasmusStudentDataBean(new ErasmusStudentDataBean(process.getCandidacy().getErasmusStudentData()));
	setCandidacyDate(process.getCandidacyDate());
	setObservations(process.getCandidacy().getObservations());

	setValidatedByErasmusCoordinator(process.getValidatedByErasmusCoordinator());
	setValidatedByGri(process.getValidatedByGri());

	setNationalIdCardAvoidanceQuestion(process.getCandidacy().getNationalIdCardAvoidanceQuestion());
	setIdCardAvoidanceOtherReason(process.getCandidacy().getIdCardAvoidanceOtherReason());
    }

    @Override
    protected void initializeDocumentUploadBeans() {
	setPhotoDocument(new CandidacyProcessDocumentUploadBean(IndividualCandidacyDocumentFileType.PHOTO));
    }

    public Set<CurricularCourse> getSelectedCurricularCourses() {
	return selectedCurricularCourses;
    }

    public void setSelectedCurricularCourses(Set<CurricularCourse> selectedCurricularCourses) {
	this.selectedCurricularCourses = selectedCurricularCourses;
    }

    public ErasmusStudentDataBean getErasmusStudentDataBean() {
	return erasmusStudentDataBean;
    }

    public void setErasmusStudentDataBean(ErasmusStudentDataBean erasmusStudentDataBean) {
	this.erasmusStudentDataBean = erasmusStudentDataBean;
    }

    public void addCurricularCourse(final CurricularCourse curricularCourse) {
	for (CurricularCourse course : this.getSelectedCurricularCourses()) {
	    if (curricularCourse.isEquivalent(course))
		return;
	}

	this.getSelectedCurricularCourses().add(curricularCourse);
    }

    public void removeCurricularCourse(final CurricularCourse curricularCourse) {
	this.getSelectedCurricularCourses().remove(curricularCourse);
    }

    public List<CurricularCourse> getSortedSelectedCurricularCourses() {
	List<CurricularCourse> curricularCourses = new ArrayList<CurricularCourse>(this.getSelectedCurricularCourses());
	Collections.sort(curricularCourses, CurricularCourse.COMPARATOR_BY_NAME);
	return curricularCourses;
    }

    public boolean isToAccessFenix() {
	return this.toAccessFenix;
    }

    public void willAccessFenix() {
	this.toAccessFenix = true;
    }

    public Boolean getValidatedByGri() {
	return validatedByGri;
    }

    public void setValidatedByGri(Boolean validatedByGri) {
	this.validatedByGri = validatedByGri;
    }

    public Boolean getValidatedByErasmusCoordinator() {
	return validatedByErasmusCoordinator;
    }

    public void setValidatedByErasmusCoordinator(Boolean validatedByErasmusCoordinator) {
	this.validatedByErasmusCoordinator = validatedByErasmusCoordinator;
    }

    public String getAlertSubject() {
	return alertSubject;
    }

    public void setAlertSubject(String alertSubject) {
	this.alertSubject = alertSubject;
    }

    public String getAlertBody() {
	return alertBody;
    }

    public void setAlertBody(String alertBody) {
	this.alertBody = alertBody;
    }

    public Boolean getCreateAlert() {
	return createAlert;
    }

    public void setCreateAlert(Boolean createAlert) {
	this.createAlert = createAlert;
    }

    public Boolean getSendEmail() {
	return sendEmail;
    }

    public void setSendEmail(Boolean sendEmail) {
	this.sendEmail = sendEmail;
    }

    public StorkAttributesList getPersonalFieldsFromStork() {
	return this.personalFieldsFromStork;
    }

    public void setPersonalFieldsFromStork(final StorkAttributesList value) {
	this.personalFieldsFromStork = value;
    }

    public NationalIdCardAvoidanceQuestion getNationalIdCardAvoidanceQuestion() {
	return nationalIdCardAvoidanceQuestion;
    }

    public void setNationalIdCardAvoidanceQuestion(NationalIdCardAvoidanceQuestion nationalIdCardAvoidanceQuestion) {
	this.nationalIdCardAvoidanceQuestion = nationalIdCardAvoidanceQuestion;
    }

    public String getIdCardAvoidanceOtherReason() {
	return idCardAvoidanceOtherReason;
    }

    public void setIdCardAvoidanceOtherReason(String idCardAvoidanceOtherReason) {
	this.idCardAvoidanceOtherReason = idCardAvoidanceOtherReason;
    }

    public ErasmusVacancy calculateErasmusVacancy() {
	ErasmusCandidacyPeriod period = (ErasmusCandidacyPeriod) getCandidacyProcess().getCandidacyPeriod();
	UniversityUnit selectedUniversity = getErasmusStudentDataBean().getSelectedUniversity();

	Degree selectedDegree = getMostDominantDegreeFromCourses();

	ErasmusVacancy vacancy = period.getAssociatedVacancyToDegreeAndUniversity(selectedDegree, selectedUniversity);

	if (vacancy == null) {
	    throw new DomainException("error.erasmus.candidacy.process.no.courses.from.one.degree.selected");
	}

	return vacancy;
    }

    private Degree getMostDominantDegreeFromCourses() {
	Map<Degree, List<CurricularCourse>> coursesMappedByDegree = new HashMap<Degree, List<CurricularCourse>>();

	for (CurricularCourse curricularCourse : getSelectedCurricularCourses()) {
	    if (!coursesMappedByDegree.containsKey(curricularCourse.getDegree())) {
		coursesMappedByDegree.put(curricularCourse.getDegree(), new ArrayList<CurricularCourse>());
	    }

	    coursesMappedByDegree.get(curricularCourse.getDegree()).add(curricularCourse);
	}

	List<Degree> candidateDegrees = new ArrayList<Degree>();
	int max = 0;

	for (Degree degree : coursesMappedByDegree.keySet()) {
	    if (coursesMappedByDegree.get(degree).size() > max) {
		candidateDegrees = new ArrayList<Degree>();
		candidateDegrees.add(degree);
		max = coursesMappedByDegree.get(degree).size();
	    } else if (coursesMappedByDegree.get(degree).size() == max) {
		candidateDegrees.add(degree);
	    }
	}

	if (candidateDegrees.size() == 0) {
	    return null;
	}

	if (candidateDegrees.size() > 1) {
	    throw new DomainException("error.erasmus.candidacy.process.find.dominant.degree.not.one");
	}

	return candidateDegrees.get(0);
    }

    public List<Degree> getPossibleDegreesFromSelectedUniversity() {
	if (this.getErasmusStudentDataBean().getSelectedUniversity() == null) {
	    return new ArrayList<Degree>();
	}

	ErasmusCandidacyPeriod period = (ErasmusCandidacyPeriod) this.getCandidacyProcess().getCandidacyPeriod();

	return period.getPossibleDegreesAssociatedToUniversity(this.getErasmusStudentDataBean().getSelectedUniversity());
    }

    public String getSelectedCourseNameForView() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AcademicAdminOffice", Language.getLocale());
	try {
	    ErasmusVacancy vacancy = calculateErasmusVacancy();
	    return vacancy.getDegree().getNameI18N().getContent(Language.getLanguage());
	} catch (DomainException e) {
	    return bundle.getString(e.getMessage());
	}
    }

    @Override
    public boolean isErasmus() {
	return true;
    }
}
