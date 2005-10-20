package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherPersonalExpectation;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author naat
 * 
 */
public class TeacherExpectationManagement extends FenixBackingBean {

    private String selectedExecutionYearName;

    private Boolean needsToCreateExpectation;

    private Integer teacherPersonalExpectationID;

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

    private String istOrgans;

    private String utlOrgans;

    private String professionalActivityMainFocus;

    private String cientificComunityService;

    private String societyService;

    private String consulting;

    private String companySocialOrgans;

    private String companyPositions;

    private String tutorComment;

    private HtmlInputHidden selectedExecutionYearIdHidden;

    private List<IExecutionCourse> lecturedDegreeExecutionCourses;

    private Map<Integer, String> lecturedDegreeExecutionCourseDegreeNames;

    private List<IExecutionCourse> lecturedMasterDegreeExecutionCourses;

    private Map<Integer, String> lecturedMasterDegreeExecutionCourseDegreeNames;

    private List<IProposal> finalDegreeWorks;

    private InfoTeacher infoTeacher;

    public HtmlInputHidden getSelectedExecutionYearIdHidden() {
        if (this.selectedExecutionYearIdHidden == null) {
            this.selectedExecutionYearIdHidden = new HtmlInputHidden();
            // this.selectedExecutionYearIdHidden.setValue(this.selectedExecutionYearID);
            this.selectedExecutionYearIdHidden.setValue(this.getSelectedExecutionYearID());
        }
        return selectedExecutionYearIdHidden;
    }

    public void setSelectedExecutionYearIdHidden(HtmlInputHidden selectedExecutionYearIdHidden) {
        if (selectedExecutionYearIdHidden != null) {
            // this.selectedExecutionYearID =
            // Integer.valueOf(selectedExecutionYearIdHidden.getValue()
            // .toString());
            this.setSelectedExecutionYearID(Integer.valueOf(selectedExecutionYearIdHidden.getValue()
                    .toString()));
        }

        this.selectedExecutionYearIdHidden = selectedExecutionYearIdHidden;
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

    public Boolean getNeedsToCreateExpectation() {
        return needsToCreateExpectation;
    }

    public void setNeedsToCreateExpectation(Boolean needsToCreateExpectation) {
        this.needsToCreateExpectation = needsToCreateExpectation;
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

    public Integer getSelectedExecutionYearID() {

        return (Integer) this.getViewState().getAttribute("selectedExecutionYearID");

    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {

        this.getViewState().setAttribute("selectedExecutionYearID", selectedExecutionYearID);

    }

    public String getSelectedExecutionYearName() throws FenixFilterException, FenixServiceException {

        return selectedExecutionYearName;
    }

    public void setSelectedExecutionYearName(String selectedExecutionYearName) {
        this.selectedExecutionYearName = selectedExecutionYearName;
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

    public Integer getTeacherPersonalExpectationID() {
        return teacherPersonalExpectationID;
    }

    public void setTeacherPersonalExpectationID(Integer teacherPersonalExpectationID) {
        this.teacherPersonalExpectationID = teacherPersonalExpectationID;
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

    public List<IExecutionCourse> getLecturedDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedDegreeExecutionCourses == null) {
            loadLecturedDegreeExecutionCourses();

        }

        return this.lecturedDegreeExecutionCourses;
    }

    public Map<Integer, String> getLecturedDegreeExecutionCourseDegreeNames() {
        return lecturedDegreeExecutionCourseDegreeNames;
    }

    public List<IExecutionCourse> getLecturedMasterDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {

        if (this.lecturedMasterDegreeExecutionCourses == null) {
            loadLecturedMasterDegreeExecutionCourses();
        }

        return this.lecturedMasterDegreeExecutionCourses;
    }

    public Map<Integer, String> getLecturedMasterDegreeExecutionCourseDegreeNames() {
        return lecturedMasterDegreeExecutionCourseDegreeNames;
    }

    private List<IExecutionCourse> readLecturedExecutionCourses(DegreeType degreeType)
            throws FenixFilterException, FenixServiceException {
        InfoTeacher infoTeacher = getInfoTeacher();

        List<IExecutionCourse> lecturedExecutionCourses = (List<IExecutionCourse>) ServiceUtils
                .executeService(getUserView(),
                        "ReadLecturedExecutionCoursesByTeacherIDAndExecutionYearIDAndDegreeType",
                        new Object[] { infoTeacher.getIdInternal(), this.getSelectedExecutionYearID(),
                                degreeType });

        return lecturedExecutionCourses;
    }

    private Map<Integer, String> computeExecutionCoursesDegreeAcronyms(
            List<IExecutionCourse> executionCourses) {
        Map<Integer, String> result = new HashMap<Integer, String>();

        for (IExecutionCourse executionCourse : executionCourses) {
            String degreeAcronyns = computeDegreeAcronyms(executionCourse);
            result.put(executionCourse.getIdInternal(), degreeAcronyns);
        }

        return result;
    }

    private String computeDegreeAcronyms(IExecutionCourse executionCourse) {
        StringBuilder degreeAcronyms = new StringBuilder();

        List<ICurricularCourse> curricularCourses = executionCourse.getAssociatedCurricularCourses();
        Set<String> processedAcronyns = new HashSet<String>();

        for (ICurricularCourse curricularCourse : curricularCourses) {
            String degreeAcronym = curricularCourse.getDegreeCurricularPlan().getDegree().getSigla();

            if (!processedAcronyns.contains(degreeAcronym)) {
                degreeAcronyms.append(degreeAcronym).append(",");
                processedAcronyns.add(degreeAcronym);

            }
        }

        if (degreeAcronyms.toString().endsWith(",")) {
            degreeAcronyms.deleteCharAt(degreeAcronyms.length() - 1);
        }

        return degreeAcronyms.toString();

    }

    public List getExecutionYears() throws FenixFilterException, FenixServiceException {

        /*
         * List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>)
         * ServiceUtils.executeService( getUserView(),
         * "ReadNotClosedExecutionYears", null);
         * 
         * List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
         * for (InfoExecutionYear executionYear : executionYears) {
         * result.add(new SelectItem(executionYear.getIdInternal(),
         * executionYear.getYear())); }
         * 
         * if (this.getSelectedExecutionYearID() == null) {
         * setSelectedExecutionYearID(executionYears.get(executionYears.size() -
         * 1).getIdInternal()); }
         */

        // TODO: THIS SHOULD BE REMOVED WHEN WE WAVE PERSONAL EXPECTATION
        // DEFINITION PERIODS
        // ONLY EXECUTION YEARS WITH EXPECTATION DEFINITION PERIODS SHOULD BE
        // VISIBLE
        String executionYearName = PropertiesManager
                .getProperty("teacherPersonalExpecationDefaultExecutionYear");

        InfoExecutionYear executionYear = (InfoExecutionYear) ServiceUtils.executeService(getUserView(),
                "ReadExecutionYear", new Object[] { executionYearName });

        List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));

        setSelectedExecutionYearID(executionYear.getIdInternal());
        loadPersonalExpectationData();

        return result;
    }

    public List<IProposal> getFinalDegreeWorks() throws FenixFilterException, FenixServiceException {

        if (this.finalDegreeWorks == null) {
            loadFinalDegreeWorks();
        }

        return this.finalDegreeWorks;
    }

    private void loadFinalDegreeWorks() throws FenixFilterException, FenixServiceException {
        InfoTeacher infoTeacher = getInfoTeacher();

        this.finalDegreeWorks = (List<IProposal>) ServiceUtils.executeService(getUserView(),
                "ReadFinalDegreeWorksByTeacherIDAndExecutionYearID", new Object[] {
                        infoTeacher.getIdInternal(), this.getSelectedExecutionYearID() });
    }

    public InfoTeacher getInfoTeacher() throws FenixFilterException, FenixServiceException {

        if (this.infoTeacher == null) {
            this.infoTeacher = (InfoTeacher) ServiceUtils.executeService(getUserView(),
                    "ReadTeacherByUsername", new Object[] { this.getUserView().getUtilizador() });
        }
        return this.infoTeacher;
    }

    public String savePersonalExpectation() throws FenixFilterException, FenixServiceException {

        InfoTeacher infoTeacher = getInfoTeacher();

        InfoTeacherPersonalExpectation infoTeacherPersonalExpectation = buildInfoTeacherPersonalExpectation();

        Object[] args = { infoTeacherPersonalExpectation, infoTeacher.getIdInternal(),
                this.getSelectedExecutionYearID() };

        ServiceUtils.executeService(getUserView(), "InsertTeacherPersonalExpectation", args);

        this.needsToCreateExpectation = false;

        loadPersonalExpectationData();

        return "success";
    }

    public String editPersonalExpectation() throws FenixFilterException, FenixServiceException {

        InfoTeacherPersonalExpectation infoTeacherPersonalExpectation = buildInfoTeacherPersonalExpectation();
        Object[] args = { infoTeacherPersonalExpectation };

        ServiceUtils.executeService(getUserView(), "EditTeacherPersonalExpectation", args);

        this.needsToCreateExpectation = false;

        loadPersonalExpectationData();

        return "success";
    }

    public String viewPersonalExpecation() throws FenixFilterException, FenixServiceException {

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(
                getUserView(), "ReadExecutionYearByID",
                new Object[] { this.getSelectedExecutionYearID() });

        this.selectedExecutionYearName = infoExecutionYear.getYear();

        loadPersonalExpectationData();
        return "view";
    }

    public void preparePersonalExpectationForEdit(ActionEvent event) throws FenixFilterException,
            FenixServiceException {
        loadLecturedDegreeExecutionCourses();
        loadLecturedMasterDegreeExecutionCourses();
        loadPersonalExpectationData();
        loadExecutionYearName();

    }

    private void loadLecturedMasterDegreeExecutionCourses() throws FenixFilterException,
            FenixServiceException {
        this.lecturedMasterDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.MASTER_DEGREE);
        this.lecturedMasterDegreeExecutionCourseDegreeNames = computeExecutionCoursesDegreeAcronyms(this.lecturedMasterDegreeExecutionCourses);
    }

    private void loadLecturedDegreeExecutionCourses() throws FenixFilterException, FenixServiceException {
        this.lecturedDegreeExecutionCourses = readLecturedExecutionCourses(DegreeType.DEGREE);
        this.lecturedDegreeExecutionCourseDegreeNames = computeExecutionCoursesDegreeAcronyms(this.lecturedDegreeExecutionCourses);
    }

    private void loadPersonalExpectationData() throws FenixFilterException, FenixServiceException {
        clearFields();

        InfoTeacher infoTeacher = getInfoTeacher();

        Object[] args = { infoTeacher.getIdInternal(), this.getSelectedExecutionYearID() };

        InfoTeacherPersonalExpectation infoTeacherPersonalExpectation = (InfoTeacherPersonalExpectation) ServiceUtils
                .executeService(getUserView(),
                        "ReadTeacherPersonalExpectationByTeacherIDAndExecutionYearID", args);

        if (infoTeacherPersonalExpectation != null) {
            this.needsToCreateExpectation = false;
            bindPersonalExpectationDataWithFields(infoTeacherPersonalExpectation);
        } else {
            this.needsToCreateExpectation = true;
        }
    }

    private void loadExecutionYearName() throws FenixFilterException, FenixServiceException {
        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(
                getUserView(), "ReadExecutionYearByID",
                new Object[] { this.getSelectedExecutionYearID() });

        this.selectedExecutionYearName = infoExecutionYear.getYear();
    }

    private void bindPersonalExpectationDataWithFields(
            InfoTeacherPersonalExpectation infoTeacherPersonalExpectation) {
        this.teacherPersonalExpectationID = infoTeacherPersonalExpectation.getIdInternal();
        this.bookPublications = infoTeacherPersonalExpectation.getBookPublications();
        this.cientificComunityService = infoTeacherPersonalExpectation.getCientificComunityService();
        this.cientificPosGraduations = infoTeacherPersonalExpectation.getCientificPosGraduations();
        this.cientificPosGraduationsDescription = infoTeacherPersonalExpectation
                .getCientificPosGraduationsDescription();
        this.companyPositions = infoTeacherPersonalExpectation.getCompanyPositions();
        this.companySocialOrgans = infoTeacherPersonalExpectation.getCompanySocialOrgans();
        this.conferencePublications = infoTeacherPersonalExpectation.getConferencePublications();
        this.consulting = infoTeacherPersonalExpectation.getConsulting();
        this.departmentOrgans = infoTeacherPersonalExpectation.getDepartmentOrgans();
        this.educationMainFocus = infoTeacherPersonalExpectation.getEducationMainFocus();
        this.finalDegreeWorkOrientations = infoTeacherPersonalExpectation
                .getFinalDegreeWorkOrientations();
        this.graduations = infoTeacherPersonalExpectation.getGraduations();
        this.graduationsDescription = infoTeacherPersonalExpectation.getGraduationsDescription();
        this.orientationsMainFocus = infoTeacherPersonalExpectation.getOrientationsMainFocus();
        this.researchAndDevMainFocus = infoTeacherPersonalExpectation.getResearchAndDevMainFocus();
        this.istOrgans = infoTeacherPersonalExpectation.getIstOrgans();
        this.jornalArticlePublications = infoTeacherPersonalExpectation.getJornalArticlePublications();
        this.masterDegreeOrientations = infoTeacherPersonalExpectation.getMasterDegreeOrientations();
        this.otherPublications = infoTeacherPersonalExpectation.getOtherPublications();
        this.otherPublicationsDescription = infoTeacherPersonalExpectation
                .getOtherPublicationsDescription();
        this.patentPublications = infoTeacherPersonalExpectation.getPatentPublications();
        this.phdOrientations = infoTeacherPersonalExpectation.getPhdOrientations();
        this.professionalActivityMainFocus = infoTeacherPersonalExpectation
                .getProfessionalActivityMainFocus();
        this.professionalPosGraduations = infoTeacherPersonalExpectation.getProfessionalPosGraduations();
        this.professionalPosGraduationsDescription = infoTeacherPersonalExpectation
                .getProfessionalPosGraduationsDescription();
        this.researchAndDevProjects = infoTeacherPersonalExpectation.getResearchAndDevProjects();
        this.seminaries = infoTeacherPersonalExpectation.getSeminaries();
        this.seminariesDescription = infoTeacherPersonalExpectation.getSeminariesDescription();
        this.societyService = infoTeacherPersonalExpectation.getSocietyService();
        this.technicalReportPublications = infoTeacherPersonalExpectation
                .getTechnicalReportPublications();
        this.universityServiceMainFocus = infoTeacherPersonalExpectation.getUniversityServiceMainFocus();
        this.utlOrgans = infoTeacherPersonalExpectation.getUtlOrgans();

    }

    private void clearFields() {
        this.teacherPersonalExpectationID = null;
        this.bookPublications = null;
        this.cientificComunityService = null;
        this.cientificPosGraduations = null;
        this.cientificPosGraduationsDescription = null;
        this.companyPositions = null;
        this.companySocialOrgans = null;
        this.conferencePublications = null;
        this.consulting = null;
        this.departmentOrgans = null;
        this.educationMainFocus = null;
        this.finalDegreeWorkOrientations = null;
        this.graduations = null;
        this.graduationsDescription = null;
        this.orientationsMainFocus = null;
        this.researchAndDevMainFocus = null;
        this.istOrgans = null;
        this.jornalArticlePublications = null;
        this.masterDegreeOrientations = null;
        this.otherPublications = null;
        this.otherPublicationsDescription = null;
        this.patentPublications = null;
        this.phdOrientations = null;
        this.professionalActivityMainFocus = null;
        this.professionalPosGraduations = null;
        this.professionalPosGraduationsDescription = null;
        this.researchAndDevProjects = null;
        this.seminaries = null;
        this.seminariesDescription = null;
        this.societyService = null;
        this.technicalReportPublications = null;
        this.universityServiceMainFocus = null;
        this.utlOrgans = null;

    }

    private InfoTeacherPersonalExpectation buildInfoTeacherPersonalExpectation() {
        InfoTeacherPersonalExpectation infoTeacherPersonalExpectation = new InfoTeacherPersonalExpectation();
        infoTeacherPersonalExpectation.setIdInternal(getTeacherPersonalExpectationID());
        infoTeacherPersonalExpectation.setBookPublications(getBookPublications());
        infoTeacherPersonalExpectation.setCientificComunityService(getCientificComunityService());
        infoTeacherPersonalExpectation.setCientificPosGraduations(getCientificPosGraduations());
        infoTeacherPersonalExpectation
                .setCientificPosGraduationsDescription(getCientificPosGraduationsDescription());
        infoTeacherPersonalExpectation.setCompanyPositions(getCompanyPositions());
        infoTeacherPersonalExpectation.setCompanySocialOrgans(getCompanySocialOrgans());
        infoTeacherPersonalExpectation.setConferencePublications(getConferencePublications());
        infoTeacherPersonalExpectation.setConsulting(getConsulting());
        infoTeacherPersonalExpectation.setDepartmentOrgans(getDepartmentOrgans());
        infoTeacherPersonalExpectation.setEducationMainFocus(getEducationMainFocus());
        infoTeacherPersonalExpectation.setFinalDegreeWorkOrientations(getFinalDegreeWorkOrientations());
        infoTeacherPersonalExpectation.setGraduations(getGraduations());
        infoTeacherPersonalExpectation.setGraduationsDescription(getGraduationsDescription());
        infoTeacherPersonalExpectation.setOrientationsMainFocus(getOrientationsMainFocus());
        infoTeacherPersonalExpectation.setResearchAndDevMainFocus(getResearchAndDevMainFocus());
        infoTeacherPersonalExpectation.setIstOrgans(getIstOrgans());
        infoTeacherPersonalExpectation.setJornalArticlePublications(getJornalArticlePublications());
        infoTeacherPersonalExpectation.setMasterDegreeOrientations(getMasterDegreeOrientations());
        infoTeacherPersonalExpectation.setOtherPublications(getOtherPublications());
        infoTeacherPersonalExpectation
                .setOtherPublicationsDescription(getOtherPublicationsDescription());
        infoTeacherPersonalExpectation.setPatentPublications(getPatentPublications());
        infoTeacherPersonalExpectation.setPhdOrientations(getPhdOrientations());
        infoTeacherPersonalExpectation
                .setProfessionalActivityMainFocus(getProfessionalActivityMainFocus());
        infoTeacherPersonalExpectation.setProfessionalPosGraduations(getProfessionalPosGraduations());
        infoTeacherPersonalExpectation
                .setProfessionalPosGraduationsDescription(getProfessionalPosGraduationsDescription());
        infoTeacherPersonalExpectation.setResearchAndDevProjects(getResearchAndDevProjects());
        infoTeacherPersonalExpectation.setSeminaries(getSeminaries());
        infoTeacherPersonalExpectation.setSeminariesDescription(getSeminariesDescription());
        infoTeacherPersonalExpectation.setSocietyService(getSocietyService());
        infoTeacherPersonalExpectation.setTechnicalReportPublications(getTechnicalReportPublications());
        infoTeacherPersonalExpectation.setUniversityServiceMainFocus(getUniversityServiceMainFocus());
        infoTeacherPersonalExpectation.setUtlOrgans(getUtlOrgans());

        return infoTeacherPersonalExpectation;
    }

    public String prepareCreatePersonalExpectation() throws FenixFilterException, FenixServiceException {

        // In case we are switching execution years
        clearFields();

        loadLecturedDegreeExecutionCourses();
        this.graduations = this.lecturedDegreeExecutionCourses.size();

        loadLecturedMasterDegreeExecutionCourses();
        this.cientificPosGraduations = this.lecturedMasterDegreeExecutionCourses.size();

        loadFinalDegreeWorks();
        this.finalDegreeWorkOrientations = this.finalDegreeWorks.size();

        loadExecutionYearName();

        return "create";

    }

}
