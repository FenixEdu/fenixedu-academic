/*
 * InfoStudent.java
 * 
 * Created on 13 de Dezembro de 2002, 16:04
 */

package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;

/**
 * 
 * @author naat
 * 
 */

public class InfoTeacherPersonalExpectation extends InfoObject {

    protected String educationMainFocus;

    protected Integer graduations;

    protected String graduationsDescription;

    protected Integer cientificPosGraduations;

    protected String cientificPosGraduationsDescription;

    protected Integer professionalPosGraduations;

    protected String professionalPosGraduationsDescription;

    protected Integer seminaries;

    protected String seminariesDescription;

    protected String orientationsMainFocus;

    protected String researchAndDevMainFocus;

    protected Integer researchAndDevProjects;

    protected Integer jornalArticlePublications;

    protected Integer bookPublications;

    protected Integer conferencePublications;

    protected Integer technicalReportPublications;

    protected Integer patentPublications;

    protected Integer otherPublications;

    protected String otherPublicationsDescription;

    protected Integer phdOrientations;

    protected Integer masterDegreeOrientations;

    protected Integer finalDegreeWorkOrientations;

    protected String universityServiceMainFocus;

    protected String departmentOrgans;

    protected String istOrgans;

    protected String utlOrgans;

    protected String professionalActivityMainFocus;

    protected String cientificComunityService;

    protected String societyService;

    protected String consulting;

    protected String companySocialOrgans;

    protected String companyPositions;

    protected String tutorComment;

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

    public String getIstOrgans() {
	return istOrgans;
    }

    public void setIstOrgans(String istOrgans) {
	this.istOrgans = istOrgans;
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

    public String getUniversityServiceMainFocus() {
	return universityServiceMainFocus;
    }

    public void setUniversityServiceMainFocus(String universityServiceMainFocus) {
	this.universityServiceMainFocus = universityServiceMainFocus;
    }

    public String getUtlOrgans() {
	return utlOrgans;
    }

    public void setUtlOrgans(String utlOrgans) {
	this.utlOrgans = utlOrgans;
    }

    public static InfoTeacherPersonalExpectation newInfoFromDomain(TeacherPersonalExpectation teacherPersonalExpectation) {
	InfoTeacherPersonalExpectation infoTeacherPersonalExpectation = null;
	if (teacherPersonalExpectation != null) {
	    infoTeacherPersonalExpectation = new InfoTeacherPersonalExpectation();
	    infoTeacherPersonalExpectation.copyFromDomain(teacherPersonalExpectation);
	}

	return infoTeacherPersonalExpectation;
    }

    public void copyFromDomain(TeacherPersonalExpectation teacherPersonalExpectation) {
	if (teacherPersonalExpectation != null) {
	    super.copyFromDomain(teacherPersonalExpectation);
	    setBookPublications(teacherPersonalExpectation.getBookPublications());
	    setCientificComunityService(teacherPersonalExpectation.getCientificComunityService());
	    setCientificPosGraduations(teacherPersonalExpectation.getCientificPosGraduations());
	    setCientificPosGraduationsDescription(teacherPersonalExpectation.getCientificPosGraduationsDescription());
	    setCompanyPositions(teacherPersonalExpectation.getCompanyPositions());
	    setCompanySocialOrgans(teacherPersonalExpectation.getCompanySocialOrgans());
	    setConferencePublications(teacherPersonalExpectation.getConferencePublications());
	    setConsulting(teacherPersonalExpectation.getConsulting());
	    setDepartmentOrgans(teacherPersonalExpectation.getDepartmentOrgans());
	    setEducationMainFocus(teacherPersonalExpectation.getEducationMainFocus());
	    setFinalDegreeWorkOrientations(teacherPersonalExpectation.getFinalDegreeWorkOrientations());
	    setGraduations(teacherPersonalExpectation.getGraduations());
	    setGraduationsDescription(teacherPersonalExpectation.getGraduationsDescription());
	    setIstOrgans(teacherPersonalExpectation.getIstOrgans());
	    setJornalArticlePublications(teacherPersonalExpectation.getJornalArticlePublications());
	    setMasterDegreeOrientations(teacherPersonalExpectation.getMasterDegreeOrientations());
	    setOtherPublications(teacherPersonalExpectation.getOtherPublications());
	    setOtherPublicationsDescription(teacherPersonalExpectation.getOtherPublicationsDescription());
	    setOrientationsMainFocus(teacherPersonalExpectation.getOrientationsMainFocus());
	    setPatentPublications(teacherPersonalExpectation.getPatentPublications());
	    setPhdOrientations(teacherPersonalExpectation.getPhdOrientations());
	    setProfessionalActivityMainFocus(teacherPersonalExpectation.getProfessionalActivityMainFocus());
	    setProfessionalPosGraduations(teacherPersonalExpectation.getProfessionalPosGraduations());
	    setProfessionalPosGraduationsDescription(teacherPersonalExpectation.getProfessionalPosGraduationsDescription());
	    setResearchAndDevProjects(teacherPersonalExpectation.getResearchAndDevProjects());
	    setResearchAndDevMainFocus(teacherPersonalExpectation.getResearchAndDevMainFocus());
	    setSeminaries(teacherPersonalExpectation.getSeminaries());
	    setSeminariesDescription(teacherPersonalExpectation.getSeminariesDescription());
	    setSocietyService(teacherPersonalExpectation.getSocietyService());
	    setTechnicalReportPublications(teacherPersonalExpectation.getTechnicalReportPublications());
	    setTutorComment(teacherPersonalExpectation.getTutorComment());
	    setUniversityServiceMainFocus(teacherPersonalExpectation.getUniversityServiceMainFocus());
	    setUtlOrgans(teacherPersonalExpectation.getUtlOrgans());

	}

    }

}