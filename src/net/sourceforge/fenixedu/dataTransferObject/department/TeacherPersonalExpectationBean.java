package net.sourceforge.fenixedu.dataTransferObject.department;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;

public class TeacherPersonalExpectationBean implements Serializable {

    private String educationMainFocus;

    private Integer graduations;

    private String graduationsDescription;

    private Integer cientificPosGraduations;

    private String cientificPosGraduationsDescription;

    private Integer professionalPosGraduations;

    private String professionalPosGraduationsDescription;

    private Integer seminaries;

    private String seminariesDescription;

    private String orientationsMainFocus;

    private String researchAndDevMainFocus;

    private Integer researchAndDevProjects;

    private Integer jornalArticlePublications;

    private Integer bookPublications;

    private Integer conferencePublications;

    private Integer technicalReportPublications;

    private Integer patentPublications;

    private Integer otherPublications;

    private String otherPublicationsDescription;

    private Integer phdOrientations;

    private Integer masterDegreeOrientations;

    private Integer finalDegreeWorkOrientations;

    private String universityServiceMainFocus;

    private String departmentOrgans;

    private String institutionOrgans;

    private String universityOrgans;

    private String professionalActivityMainFocus;

    private String cientificComunityService;

    private String societyService;

    private String consulting;

    private String companySocialOrgans;

    private String companyPositions;

    private String tutorComment;

    private String autoEvaluation;

    private DomainReference<ExecutionYear> executionYearReference;
    
    private DomainReference<Teacher> teacherReference;
    

    public TeacherPersonalExpectationBean(ExecutionYear executionYear, Teacher teacher) {
	setExecutionYear(executionYear);
	setTeacher(teacher);
    }

    public ExecutionYear getExecutionYear() {
	return (this.executionYearReference != null) ? this.executionYearReference.getObject() : null;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
	this.executionYearReference = (executionYear != null) ? new DomainReference<ExecutionYear>(
		executionYear) : null;
    }
    
    public Teacher getTeacher() {
	return (this.teacherReference != null) ? this.teacherReference.getObject() : null;
    }

    public void setTeacher(Teacher teacher) {
	this.teacherReference = (teacher != null) ? new DomainReference<Teacher>(teacher) : null;
    }

    public String getAutoEvaluation() {
        return autoEvaluation;
    }

    public void setAutoEvaluation(String autoEvaluation) {
        this.autoEvaluation = autoEvaluation;
    }

    public Integer getBookPublications() {
        return bookPublications;
    }

    public void setBookPublications(Integer bookPublications) {
        this.bookPublications = bookPublications;
    }

    public String getCientificComunityService() {
        return cientificComunityService;
    }

    public void setCientificComunityService(String cientificComunityService) {
        this.cientificComunityService = cientificComunityService;
    }

    public Integer getCientificPosGraduations() {
        return cientificPosGraduations;
    }

    public void setCientificPosGraduations(Integer cientificPosGraduations) {
        this.cientificPosGraduations = cientificPosGraduations;
    }

    public String getCientificPosGraduationsDescription() {
        return cientificPosGraduationsDescription;
    }

    public void setCientificPosGraduationsDescription(String cientificPosGraduationsDescription) {
        this.cientificPosGraduationsDescription = cientificPosGraduationsDescription;
    }

    public String getCompanyPositions() {
        return companyPositions;
    }

    public void setCompanyPositions(String companyPositions) {
        this.companyPositions = companyPositions;
    }

    public String getCompanySocialOrgans() {
        return companySocialOrgans;
    }

    public void setCompanySocialOrgans(String companySocialOrgans) {
        this.companySocialOrgans = companySocialOrgans;
    }

    public Integer getConferencePublications() {
        return conferencePublications;
    }

    public void setConferencePublications(Integer conferencePublications) {
        this.conferencePublications = conferencePublications;
    }

    public String getConsulting() {
        return consulting;
    }

    public void setConsulting(String consulting) {
        this.consulting = consulting;
    }

    public String getDepartmentOrgans() {
        return departmentOrgans;
    }

    public void setDepartmentOrgans(String departmentOrgans) {
        this.departmentOrgans = departmentOrgans;
    }

    public String getEducationMainFocus() {
        return educationMainFocus;
    }

    public void setEducationMainFocus(String educationMainFocus) {
        this.educationMainFocus = educationMainFocus;
    }

    public Integer getFinalDegreeWorkOrientations() {
        return finalDegreeWorkOrientations;
    }

    public void setFinalDegreeWorkOrientations(Integer finalDegreeWorkOrientations) {
        this.finalDegreeWorkOrientations = finalDegreeWorkOrientations;
    }

    public Integer getGraduations() {
        return graduations;
    }

    public void setGraduations(Integer graduations) {
        this.graduations = graduations;
    }

    public String getGraduationsDescription() {
        return graduationsDescription;
    }

    public void setGraduationsDescription(String graduationsDescription) {
        this.graduationsDescription = graduationsDescription;
    }

    public String getInstitutionOrgans() {
        return institutionOrgans;
    }

    public void setInstitutionOrgans(String institutionOrgans) {
        this.institutionOrgans = institutionOrgans;
    }

    public Integer getJornalArticlePublications() {
        return jornalArticlePublications;
    }

    public void setJornalArticlePublications(Integer jornalArticlePublications) {
        this.jornalArticlePublications = jornalArticlePublications;
    }

    public Integer getMasterDegreeOrientations() {
        return masterDegreeOrientations;
    }

    public void setMasterDegreeOrientations(Integer masterDegreeOrientations) {
        this.masterDegreeOrientations = masterDegreeOrientations;
    }

    public String getOrientationsMainFocus() {
        return orientationsMainFocus;
    }

    public void setOrientationsMainFocus(String orientationsMainFocus) {
        this.orientationsMainFocus = orientationsMainFocus;
    }

    public Integer getOtherPublications() {
        return otherPublications;
    }

    public void setOtherPublications(Integer otherPublications) {
        this.otherPublications = otherPublications;
    }

    public String getOtherPublicationsDescription() {
        return otherPublicationsDescription;
    }

    public void setOtherPublicationsDescription(String otherPublicationsDescription) {
        this.otherPublicationsDescription = otherPublicationsDescription;
    }

    public Integer getPatentPublications() {
        return patentPublications;
    }

    public void setPatentPublications(Integer patentPublications) {
        this.patentPublications = patentPublications;
    }

    public Integer getPhdOrientations() {
        return phdOrientations;
    }

    public void setPhdOrientations(Integer phdOrientations) {
        this.phdOrientations = phdOrientations;
    }

    public String getProfessionalActivityMainFocus() {
        return professionalActivityMainFocus;
    }

    public void setProfessionalActivityMainFocus(String professionalActivityMainFocus) {
        this.professionalActivityMainFocus = professionalActivityMainFocus;
    }

    public Integer getProfessionalPosGraduations() {
        return professionalPosGraduations;
    }

    public void setProfessionalPosGraduations(Integer professionalPosGraduations) {
        this.professionalPosGraduations = professionalPosGraduations;
    }

    public String getProfessionalPosGraduationsDescription() {
        return professionalPosGraduationsDescription;
    }

    public void setProfessionalPosGraduationsDescription(String professionalPosGraduationsDescription) {
        this.professionalPosGraduationsDescription = professionalPosGraduationsDescription;
    }

    public String getResearchAndDevMainFocus() {
        return researchAndDevMainFocus;
    }

    public void setResearchAndDevMainFocus(String researchAndDevMainFocus) {
        this.researchAndDevMainFocus = researchAndDevMainFocus;
    }

    public Integer getResearchAndDevProjects() {
        return researchAndDevProjects;
    }

    public void setResearchAndDevProjects(Integer researchAndDevProjects) {
        this.researchAndDevProjects = researchAndDevProjects;
    }

    public Integer getSeminaries() {
        return seminaries;
    }

    public void setSeminaries(Integer seminaries) {
        this.seminaries = seminaries;
    }

    public String getSeminariesDescription() {
        return seminariesDescription;
    }

    public void setSeminariesDescription(String seminariesDescription) {
        this.seminariesDescription = seminariesDescription;
    }

    public String getSocietyService() {
        return societyService;
    }

    public void setSocietyService(String societyService) {
        this.societyService = societyService;
    }

    public Integer getTechnicalReportPublications() {
        return technicalReportPublications;
    }

    public void setTechnicalReportPublications(Integer technicalReportPublications) {
        this.technicalReportPublications = technicalReportPublications;
    }

    public String getTutorComment() {
        return tutorComment;
    }

    public void setTutorComment(String tutorComment) {
        this.tutorComment = tutorComment;
    }

    public String getUniversityOrgans() {
        return universityOrgans;
    }

    public void setUniversityOrgans(String universityOrgans) {
        this.universityOrgans = universityOrgans;
    }

    public String getUniversityServiceMainFocus() {
        return universityServiceMainFocus;
    }

    public void setUniversityServiceMainFocus(String universityServiceMainFocus) {
        this.universityServiceMainFocus = universityServiceMainFocus;
    }
}
