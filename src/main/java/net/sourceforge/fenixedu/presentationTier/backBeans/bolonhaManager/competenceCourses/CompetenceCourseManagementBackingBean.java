/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.competenceCourses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.DeleteCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditCompetenceCourse;
import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.EditCompetenceCourseLoad;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingCompetenceCourseInformationException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CourseLoad;
import net.sourceforge.fenixedu.domain.CompetenceCourse;
import net.sourceforge.fenixedu.domain.CompetenceCourseType;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.contents.Container;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import net.sourceforge.fenixedu.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLevel;
import net.sourceforge.fenixedu.domain.degreeStructure.CompetenceCourseLoad;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.functionalities.AbstractFunctionalityContext;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.BundleUtil;
import net.sourceforge.fenixedu.util.StringUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final Integer NO_SELECTION = 0;

    private String selectedDepartmentUnitID = null;
    private String competenceCourseID = null;
    private String executionYearID = null;
    private String executionSemesterID = null;
    private Unit competenceCourseGroupUnit = null;
    private CompetenceCourse competenceCourse = null;

    // Competence-Course-Information
    private String name;
    private String nameEn;
    private String acronym;
    private Boolean basic;
    private boolean setNumberOfPeriods = true;
    // Competence-Course-Additional-Data
    private String objectives;
    private String program;
    private String evaluationMethod;
    private String objectivesEn;
    private String programEn;
    private String evaluationMethodEn;
    private String stage;
    // BibliographicReferences
    private Integer bibliographicReferenceID;
    private String year;
    private String title;
    private String author;
    private String reference;
    private String type;
    private String url;

    private UISelectItems departmentUnitItems;
    private UISelectItems scientificAreaUnitItems;
    private UISelectItems competenceCourseGroupUnitItems;
    private UISelectItems competenceCourseExecutionSemesters;
    private UISelectItems executionSemesterItems;
    private UISelectItems futureExecutionSemesterItems;

    private List<SelectItem> selectedYears = null;

    public String getAction() {
        return getAndHoldStringParameter("action");
    }

    public String getCompetenceCoursesToList() {
        return getAndHoldStringParameter("competenceCoursesToList");
    }

    public Boolean getCanView() {
        DepartmentUnit selectedDepartmentUnit = getSelectedDepartmentUnit();
        if (selectedDepartmentUnit == null) {
            return (this.getPersonDepartment() != null && this.getPersonDepartment().getCompetenceCourseMembersGroup() != null) ? this
                    .getPersonDepartment().getCompetenceCourseMembersGroup().isMember(this.getUserView().getPerson()) : false;
        } else {
            return selectedDepartmentUnit.getDepartment().getCompetenceCourseMembersGroup() != null ? selectedDepartmentUnit
                    .getDepartment().getCompetenceCourseMembersGroup().isMember(getUserView().getPerson()) : false;
        }
    }

    public Department getPersonDepartment() {
        final IUserView userView = getUserView();
        final Person person = userView == null ? null : userView.getPerson();
        final Employee employee = person == null ? null : person.getEmployee();
        return employee == null ? null : employee.getCurrentDepartmentWorkingPlace();
    }

    public Department getDepartmentToDisplay() {
        if (getSelectedDepartmentUnit() != null) {
            return getSelectedDepartmentUnit().getDepartment();
        } else {
            return getPersonDepartment();
        }
    }

    public DepartmentUnit getSelectedDepartmentUnit() {
        if (this.getSelectedDepartmentUnitID() != null) {
            return (DepartmentUnit) AbstractDomainObject.fromExternalId(this.getSelectedDepartmentUnitID());
        } else {
            return null;
        }
    }

    public List<ScientificAreaUnit> getScientificAreaUnits() {
        DepartmentUnit departmentUnit = null;
        if (getSelectedDepartmentUnit() != null) {
            departmentUnit = getSelectedDepartmentUnit();
        } else if (getPersonDepartment() != null) {
            departmentUnit = getPersonDepartment().getDepartmentUnit();
        }
        return (departmentUnit != null) ? departmentUnit.getScientificAreaUnits() : null;
    }

    public List<CompetenceCourse> getDepartmentCompetenceCourses(CurricularStage curricularStage) {
        DepartmentUnit selectedDepartmentUnit = getSelectedDepartmentUnit();
        if (selectedDepartmentUnit != null) {
            return selectedDepartmentUnit.getCompetenceCourses(curricularStage);
        }
        return new ArrayList<CompetenceCourse>();
    }

    public List<CompetenceCourse> getDepartmentCompetenceCourses() {
        return getDepartmentCompetenceCourses(CurricularStage.valueOf(getCompetenceCoursesToList()));
    }

    public List<CompetenceCourse> getDepartmentDraftCompetenceCourses() {
        return getDepartmentCompetenceCourses(CurricularStage.DRAFT);
    }

    public List<CompetenceCourse> getDepartmentPublishedCompetenceCourses() {
        return getDepartmentCompetenceCourses(CurricularStage.PUBLISHED);
    }

    public List<CompetenceCourse> getDepartmentApprovedCompetenceCourses() {
        return getDepartmentCompetenceCourses(CurricularStage.APPROVED);
    }

    public List<String> getGroupMembersLabels() {
        List<String> result = null;

        if (getSelectedDepartmentUnit() == null || getSelectedDepartmentUnit().getDepartment() == null
                || getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup() == null) {
            return result;
        }

        Group competenceCoursesManagementGroup = getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup();
        if (competenceCoursesManagementGroup != null) {
            result = new ArrayList<String>();

            for (Person person : competenceCoursesManagementGroup.getElements()) {
                result.add(person.getName() + " (" + person.getUsername() + ")");
            }
        }

        return result;
    }

    public String getCompetenceCourseGroupUnitID() {
        return getAndHoldStringParameter("competenceCourseGroupUnitID");
    }

    public Unit getCompetenceCourseGroupUnit() {
        if (competenceCourseGroupUnit == null && getCompetenceCourseGroupUnitID() != null) {
            competenceCourseGroupUnit = (Unit) AbstractDomainObject.fromExternalId(getCompetenceCourseGroupUnitID());
        }
        return competenceCourseGroupUnit;
    }

    public String getName() {
        if (name == null && getCompetenceCourse() != null) {
            name = getCompetenceCourse().getName(getAssociatedExecutionPeriod());
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        if (nameEn == null && getCompetenceCourse() != null) {
            nameEn = getCompetenceCourse().getNameEn(getAssociatedExecutionPeriod());
        }
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAcronym() {
        if (acronym == null && getCompetenceCourse() != null) {
            acronym = getCompetenceCourse().getAcronym(getAssociatedExecutionPeriod());
        }
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public Boolean getBasic() {
        if (basic == null && getCompetenceCourse() != null) {
            basic = getCompetenceCourse().isBasic(getAssociatedExecutionPeriod());
        }
        return basic;
    }

    public void setBasic(Boolean basic) {
        this.basic = basic;
    }

    public String getRegime() {
        if (getViewState().getAttribute("regime") == null) {
            if (getCompetenceCourse() != null) {
                ExecutionYear executionYear = getExecutionYear();
                setRegime(getCompetenceCourse().getRegime(getAssociatedExecutionPeriod()).getName());
            } else {
                setRegime("SEMESTRIAL");
            }
        }
        return (String) getViewState().getAttribute("regime");
    }

    public void setRegime(String regime) {
        getViewState().setAttribute("regime", regime);
    }

    public String getCompetenceCourseLevel() {
        if (StringUtils.isEmpty((String) getViewState().getAttribute("competenceCourseLevel"))) {
            if (getCompetenceCourse() != null) {
                if (getCompetenceCourse().getCompetenceCourseLevel(getAssociatedExecutionPeriod()) != null) {
                    setCompetenceCourseLevel(getCompetenceCourse().getCompetenceCourseLevel(getAssociatedExecutionPeriod())
                            .getName());
                }
            }
        }
        return (String) getViewState().getAttribute("competenceCourseLevel");
    }

    public void setCompetenceCourseLevel(String competenceCourseLevel) {
        getViewState().setAttribute("competenceCourseLevel", competenceCourseLevel);
    }

    public String getCompetenceCourseType() {
        if (getViewState().getAttribute("competenceCourseType") == null && getCompetenceCourse() != null) {
            if (getCompetenceCourse().getType() != null) {
                setCompetenceCourseType(getCompetenceCourse().getType().name());
            }
        }

        return (String) getViewState().getAttribute("competenceCourseType");
    }

    public void setCompetenceCourseType(String competenceCourseType) {
        getViewState().setAttribute("competenceCourseType", competenceCourseType);
    }

    public Integer getNumberOfPeriods() {
        if (getViewState().getAttribute("numberOfPeriods") == null) {
            if (getCompetenceCourse() != null && getCompetenceCourse().getCompetenceCourseLoads().size() > 0) {
                setNumberOfPeriods(getCompetenceCourse().getCompetenceCourseLoads(getAssociatedExecutionPeriod()).size());
            } else {
                setNumberOfPeriods(Integer.valueOf(1));
            }
        }
        return (Integer) getViewState().getAttribute("numberOfPeriods");
    }

    public void setNumberOfPeriods(Integer numberOfPeriods) {
        if (setNumberOfPeriods) {
            getViewState().setAttribute("numberOfPeriods", numberOfPeriods);
        }
    }

    public List<SelectItem> getPeriods() {
        final List<SelectItem> result = new ArrayList<SelectItem>(2);
        result.add(new SelectItem(Integer.valueOf(2), bolonhaResources.getString("yes")));
        result.add(new SelectItem(Integer.valueOf(1), bolonhaResources.getString("no")));
        return result;
    }

    public List<CourseLoad> getCourseLoads() {
        if (getViewState().getAttribute("courseLoads") == null) {
            if (getAction().equals("create")) {
                getViewState().setAttribute("courseLoads", createNewCourseLoads());
            } else if (getAction().equals("edit") && getCompetenceCourse() != null) {
                getViewState().setAttribute("courseLoads", getExistingCourseLoads());
            }
        }
        return (List<CourseLoad>) getViewState().getAttribute("courseLoads");
    }

    private List<CourseLoad> createNewCourseLoads() {
        int numberOfPeriods = getNumberOfPeriods().intValue();
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(numberOfPeriods);
        for (int i = 0; i < numberOfPeriods; i++) {
            courseLoads.add(new CourseLoad(i + 1));
        }
        return courseLoads;
    }

    private List<CourseLoad> getExistingCourseLoads() {
        final List<CourseLoad> courseLoads = new ArrayList<CourseLoad>(getCompetenceCourse().getCompetenceCourseLoadsCount());
        for (final CompetenceCourseLoad competenceCourseLoad : getCompetenceCourse().getSortedCompetenceCourseLoads()) {
            courseLoads.add(new CourseLoad("edit", competenceCourseLoad));
        }
        if (courseLoads.isEmpty()) {
            courseLoads.add(new CourseLoad(1));
        }
        return courseLoads;
    }

    public void setCourseLoads(List<CourseLoad> courseLoads) {
        getViewState().setAttribute("courseLoads", courseLoads);
    }

    public void resetCourseLoad(ValueChangeEvent event) {
        calculateCourseLoad((String) event.getNewValue(), 1);
    }

    public void resetCorrespondentCourseLoad(ValueChangeEvent event) {
        calculateCourseLoad(getRegime(), ((Integer) event.getNewValue()).intValue());
    }

    private void calculateCourseLoad(String regime, int newNumberOfPeriods) {
        final List<CourseLoad> courseLoads = getCourseLoads();
        if (regime.equals("ANUAL")) {
            if (newNumberOfPeriods > getNumberOfPeriods().intValue()) {
                addCourseLoad(courseLoads);
            } else {
                removeCourseLoad(courseLoads);
            }
        } else if (regime.equals("SEMESTRIAL")) {
            removeCourseLoad(courseLoads);
            setNumberOfPeriods(Integer.valueOf(1));
            // prevent application to reset the value
            setNumberOfPeriods = false;
        }
        setCourseLoads(courseLoads);
    }

    private void addCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create")) {
            courseLoads.add(new CourseLoad(courseLoads.size() + 1));
        } else if (getAction().equals("edit")) {
            final CourseLoad courseLoad = searchDeletedCourseLoad(courseLoads);
            if (courseLoad != null) {
                courseLoad.setAction("edit");
            } else {
                courseLoads.add(new CourseLoad(courseLoads.size() + 1));
            }
        }
    }

    private CourseLoad searchDeletedCourseLoad(final List<CourseLoad> courseLoads) {
        for (final CourseLoad courseLoad : courseLoads) {
            if (courseLoad.getAction().equals("delete")) {
                return courseLoad;
            }
        }
        return null;
    }

    private void removeCourseLoad(final List<CourseLoad> courseLoads) {
        if (getAction().equals("create") && courseLoads.size() > 1) {
            courseLoads.remove(courseLoads.size() - 1);
        } else if (getAction().equals("edit") && courseLoads.size() > 1) {
            courseLoads.get(courseLoads.size() - 1).setAction("delete");
        }
    }

    public String getSelectedDepartmentUnitID() {
        if (selectedDepartmentUnitID == null) {
            Container site = AbstractFunctionalityContext.getCurrentContext(getRequest()).getSelectedContainer();
            if (site != null && site instanceof DepartmentSite) {
                selectedDepartmentUnitID = ((DepartmentSite) site).getDepartment().getDepartmentUnit().getExternalId();
            } else if (getAndHoldStringParameter("selectedDepartmentUnitID") != null) {
                selectedDepartmentUnitID = getAndHoldStringParameter("selectedDepartmentUnitID");
            } else if (getPersonDepartment() != null) {
                selectedDepartmentUnitID = getPersonDepartment().getDepartmentUnit().getExternalId();
            }
        }
        return selectedDepartmentUnitID;
    }

    public void setSelectedDepartmentUnitID(String selectedDepartmentUnitID) {
        this.selectedDepartmentUnitID = selectedDepartmentUnitID;
    }

    public String getCompetenceCourseID() {
        return (competenceCourseID == null) ? (competenceCourseID = getAndHoldStringParameter("competenceCourseID")) : competenceCourseID;
    }

    public void setCompetenceCourseID(String competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }

    public CompetenceCourse getCompetenceCourse() {
        if (competenceCourse == null && getCompetenceCourseID() != null) {
            competenceCourse = AbstractDomainObject.fromExternalId(getCompetenceCourseID());
        }
        return competenceCourse;
    }

    public void setCompetenceCourse(CompetenceCourse competenceCourse) {
        this.competenceCourse = competenceCourse;
    }

    public ExecutionSemester getAssociatedExecutionPeriod() {
        return getExecutionSemester();
    }

    public String getObjectives() {
        if (objectives == null && getCompetenceCourse() != null) {
            objectives = getCompetenceCourse().getObjectives(getAssociatedExecutionPeriod());
        }
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getProgram() {
        if (program == null && getCompetenceCourse() != null) {
            program = getCompetenceCourse().getProgram(getAssociatedExecutionPeriod());
        }
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getEvaluationMethod() {
        if (evaluationMethod == null && getCompetenceCourse() != null) {
            evaluationMethod = getCompetenceCourse().getEvaluationMethod(getAssociatedExecutionPeriod());
        }
        return evaluationMethod;
    }

    public void setEvaluationMethod(String evaluationMethod) {
        this.evaluationMethod = evaluationMethod;
    }

    public String getObjectivesEn() {
        if (objectivesEn == null && getCompetenceCourse() != null) {
            objectivesEn = getCompetenceCourse().getObjectivesEn(getAssociatedExecutionPeriod());
        }
        return objectivesEn;
    }

    public void setObjectivesEn(String objectivesEn) {
        this.objectivesEn = objectivesEn;
    }

    public String getProgramEn() {
        if (programEn == null && getCompetenceCourse() != null) {
            programEn = getCompetenceCourse().getProgramEn(getAssociatedExecutionPeriod());
        }
        return programEn;
    }

    public void setProgramEn(String programEn) {
        this.programEn = programEn;
    }

    public String getEvaluationMethodEn() {
        if (evaluationMethodEn == null && getCompetenceCourse() != null) {
            evaluationMethodEn = getCompetenceCourse().getEvaluationMethodEn(getAssociatedExecutionPeriod());
        }
        return evaluationMethodEn;
    }

    public void setEvaluationMethodEn(String evaluationMethodEn) {
        this.evaluationMethodEn = evaluationMethodEn;
    }

    public String getStage() {
        if (stage == null && getCompetenceCourse() != null) {
            stage = getCompetenceCourse().getCurricularStage().name();
        }
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getBibliographicReferenceID() {
        return (bibliographicReferenceID == null) ? (bibliographicReferenceID =
                getAndHoldIntegerParameter("bibliographicReferenceID")) : bibliographicReferenceID;
    }

    public void setBibliographicReferenceID(Integer bibliographicReferenceID) {
        this.bibliographicReferenceID = bibliographicReferenceID;
    }

    public String getYear() {
        if (this.year == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.year = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getYear();
        }
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        if (this.title == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.title = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getTitle();
        }
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        if (this.author == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.author = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getAuthors();
        }
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReference() {
        if (this.reference == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.reference = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getReference();
        }
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getType() {
        if (this.type == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.type = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getType().getName();
        }
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        if (this.url == null && getCompetenceCourse() != null && getBibliographicReferenceID() != null) {
            this.url = getCompetenceCourse().getBibliographicReference(getBibliographicReferenceID()).getUrl();
        }
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<CompetenceCourseLoad> getSortedCompetenceCourseLoads() {
        return getCompetenceCourse().getSortedCompetenceCourseLoads(getAssociatedExecutionPeriod());
    }

    public List<BibliographicReference> getMainBibliographicReferences() {
        final List<BibliographicReference> result = new ArrayList<BibliographicReference>();
        if (this.getBibliographicReferences() == null) {
            return result;
        }
        for (final BibliographicReference bibliographicReference : getBibliographicReferences()) {
            if (bibliographicReference.getType().equals(BibliographicReferenceType.MAIN)) {
                result.add(bibliographicReference);
            }
        }
        return result;
    }

    public List<BibliographicReference> getSecondaryBibliographicReferences() {
        final List<BibliographicReference> result = new ArrayList<BibliographicReference>();
        if (this.getBibliographicReferences() == null) {
            return result;
        }
        for (final BibliographicReference bibliographicReference : getBibliographicReferences()) {
            if (bibliographicReference.getType().equals(BibliographicReferenceType.SECONDARY)) {
                result.add(bibliographicReference);
            }
        }
        return result;
    }

    private List<BibliographicReference> getBibliographicReferences() {
        return (getCompetenceCourse().getBibliographicReferences(getAssociatedExecutionPeriod()) == null) ? null : getCompetenceCourse()
                .getBibliographicReferences(getAssociatedExecutionPeriod()).getBibliographicReferencesList();
    }

    public int getBibliographicReferencesCount() {
        return (getBibliographicReferences() != null) ? getBibliographicReferences().size() : 0;
    }

    private CompetenceCourseLevel getEnumCompetenceCourseLevel() {
        return (getCompetenceCourseLevel() == null || getCompetenceCourseLevel().length() == 0) ? null : CompetenceCourseLevel
                .valueOf(getCompetenceCourseLevel());
    }

    private CompetenceCourseType getEnumCompetenceCourseType() {
        String value = getCompetenceCourseType();

        if (value == null || value.length() == 0) {
            return null;
        } else {
            try {
                return CompetenceCourseType.valueOf(value);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    private boolean isCompetenceCourseLevelValid() {
        return getEnumCompetenceCourseLevel() != null;
    }

    private boolean isCompetenceCourseTypeValid() {
        return getEnumCompetenceCourseType() != null;
    }

    public String createCompetenceCourse() {
        try {
            boolean valid = true;

            if (!isCompetenceCourseLevelValid()) {
                valid = false;
                addErrorMessage(bolonhaResources.getString("error.mustSetCompetenceCourseLevel"));
            }

            if (!isCompetenceCourseTypeValid()) {
                valid = false;
                addErrorMessage(bolonhaResources.getString("error.mustSetCompetenceCourseType"));
            }

            if (valid) {

                final CompetenceCourse competenceCourse =
                        CreateCompetenceCourse.run(getName(), getNameEn(), null, getBasic(), RegimeType.SEMESTRIAL,
                                getEnumCompetenceCourseLevel(), getEnumCompetenceCourseType(), getCompetenceCourseGroupUnitID(),
                                getExecutionSemester());
                setCompetenceCourse(competenceCourse);
                return "setCompetenceCourseLoad";
            }
        } catch (IllegalDataAccessException e) {
            addErrorMessage(bolonhaResources.getString("error.creatingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(getFormatedMessage(bolonhaResources, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }

    public String createCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "setCompetenceCourseAdditionalInformation";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }

    public String createCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "competenceCoursesManagement";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourse() {
        try {
            if (isCompetenceCourseLevelValid()) {
                EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getName(), getNameEn(), getBasic(),
                        getEnumCompetenceCourseLevel(), getEnumCompetenceCourseType(), CurricularStage.valueOf(getStage()));
                return "editCompetenceCourseMainPage";

            } else {
                addErrorMessage(bolonhaResources.getString("error.mustSetCompetenceCourseLevel"));
            }
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(getFormatedMessage(bolonhaResources, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "editCompetenceCourseMainPage";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "editCompetenceCourseMainPage";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        }
        return "";
    }

    private void setCompetenceCourseLoad() throws FenixServiceException {

        EditCompetenceCourseLoad.run(getCompetenceCourseID(), RegimeType.valueOf(getRegime()), getNumberOfPeriods(),
                getCourseLoads());
    }

    private void setCompetenceCourseAdditionalInformation() throws FenixServiceException {
        EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getObjectives(), getProgram(),
                getEvaluationMethod(), getObjectivesEn(), getProgramEn(), getEvaluationMethodEn());
    }

    public String deleteCompetenceCourse() {
        try {

            DeleteCompetenceCourse.run(getCompetenceCourseID());
            addInfoMessage(bolonhaResources.getString("competenceCourseDeleted"));
            return "competenceCoursesManagement";
        } catch (IllegalDataAccessException e) {
            addErrorMessage(bolonhaResources.getString("error.deletingCompetenceCourse"));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

    public String createBibliographicReference() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getYear(), getTitle(), getAuthor(),
                    getReference(), BibliographicReferenceType.valueOf(getType()), getUrl());
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.creatingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }

    public String editBibliographicReference() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getBibliographicReferenceID(), getYear(),
                    getTitle(), getAuthor(), getReference(), BibliographicReferenceType.valueOf(getType()), getUrl());
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }

    public Integer getBibliographicReferenceIDToDelete() {
        return getAndHoldIntegerParameter("bibliographicReferenceIDToDelete");
    }

    public String deleteBibliographicReference() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getBibliographicReferenceIDToDelete());
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.deletingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }

    public Integer getOldPosition() {
        return getAndHoldIntegerParameter("oldPosition");
    }

    public Integer getNewPosition() {
        return getAndHoldIntegerParameter("newPosition");
    }

    public String switchBibliographicReferencePosition() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getOldPosition(), getNewPosition());
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.switchBibliographicReferencePositions"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }

    public String cancelBibliographicReference() {
        setBibliographicReferenceID(-1);
        return "";
    }

    public String changeCompetenceCourseState() {
        try {
            CurricularStage changed =
                    (getCompetenceCourse().getCurricularStage().equals(CurricularStage.PUBLISHED) ? CurricularStage.APPROVED : CurricularStage.PUBLISHED);
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), changed);
            return "";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

    public String getDepartmentRealName() {
        return getCompetenceCourse().getDepartmentUnit(getExecutionSemester()).getDepartment().getRealName();
    }

    public String getScientificAreaUnitName() {
        return getCompetenceCourse().getScientificAreaUnit(getExecutionSemester()).getName();
    }

    public String getCompetenceCourseGroupUnitName() {
        return getCompetenceCourse().getCompetenceCourseGroupUnit(getExecutionSemester()).getName();
    }

    public UISelectItems getDepartmentUnitItems() {
        if (departmentUnitItems == null) {
            departmentUnitItems = new UISelectItems();
            departmentUnitItems.setValue(readDepartmentUnitLabels());
        }
        return departmentUnitItems;
    }

    public void setDepartmentUnitItems(UISelectItems departmentUnitItems) {
        this.departmentUnitItems = departmentUnitItems;
    }

    public void onChangeDepartmentUnit(ValueChangeEvent event) {
        setTransferToDepartmentUnitID((String) event.getNewValue());
        getScientificAreaUnitItems().setValue(readScientificAreaUnitLabels((String) event.getNewValue()));
        getCompetenceCourseGroupUnitItems().setValue(readCompetenceCourseGroupUnitLabels(null));
    }

    private List<SelectItem> readDepartmentUnitLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Object departmentObject : RootDomainObject.readAllDomainObjects(Department.class)) {
            DepartmentUnit departmentUnit = ((Department) departmentObject).getDepartmentUnit();
            if (departmentUnit.isActive(getExecutionSemester().getBeginDateYearMonthDay())) {
                result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaResources.getString("choose")));
        return result;
    }

    public UISelectItems getScientificAreaUnitItems() {
        if (scientificAreaUnitItems == null) {
            scientificAreaUnitItems = new UISelectItems();
            scientificAreaUnitItems.setValue(readScientificAreaUnitLabels(getTransferToDepartmentUnitID()));
        }
        return scientificAreaUnitItems;
    }

    public void setScientificAreaUnitItems(UISelectItems scientificAreaUnitItems) {
        this.scientificAreaUnitItems = scientificAreaUnitItems;
    }

    public void onChangeScientificAreaUnit(ValueChangeEvent event) {
        setTransferToScientificAreaUnitID((String) event.getNewValue());
        getCompetenceCourseGroupUnitItems().setValue(readCompetenceCourseGroupUnitLabels((String) event.getNewValue()));
    }

    private List<SelectItem> readScientificAreaUnitLabels(String transferToDepartmentUnitID) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (transferToDepartmentUnitID != null) {
            for (final ScientificAreaUnit unit : readDepartmentUnitToTransferTo(transferToDepartmentUnitID)
                    .getScientificAreaUnits()) {
                result.add(new SelectItem(unit.getExternalId(), unit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaResources.getString("choose")));
        return result;
    }

    public String getTransferToDepartmentUnitID() {
        if (getViewState().getAttribute("transferToDepartmentUnitID") != null) {
            return (String) getViewState().getAttribute("transferToDepartmentUnitID");
        }
        return null;
    }

    public void setTransferToDepartmentUnitID(String transferToDepartmentUnitID) {
        this.getViewState().setAttribute("transferToDepartmentUnitID", transferToDepartmentUnitID);
    }

    private DepartmentUnit readDepartmentUnitToTransferTo(String transferToDepartmentUnitID) {
        return (DepartmentUnit) AbstractDomainObject.fromExternalId(transferToDepartmentUnitID);
    }

    public UISelectItems getCompetenceCourseGroupUnitItems() {
        if (competenceCourseGroupUnitItems == null) {
            competenceCourseGroupUnitItems = new UISelectItems();
            competenceCourseGroupUnitItems.setValue(readCompetenceCourseGroupUnitLabels(getTransferToScientificAreaUnitID()));
        }
        return competenceCourseGroupUnitItems;
    }

    public void setCompetenceCourseGroupUnitItems(UISelectItems competenceCourseGroupUnitItems) {
        this.competenceCourseGroupUnitItems = competenceCourseGroupUnitItems;
    }

    private List<SelectItem> readCompetenceCourseGroupUnitLabels(String transferToScientificAreaUnitID) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (transferToScientificAreaUnitID != null) {
            for (final Unit unit : readScientificAreaUnitToTransferTo(transferToScientificAreaUnitID)
                    .getCompetenceCourseGroupUnits()) {
                result.add(new SelectItem(unit.getExternalId(), unit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, bolonhaResources.getString("choose")));
        return result;
    }

    public String getTransferToScientificAreaUnitID() {
        if (getViewState().getAttribute("transferToScientificAreaUnitID") != null) {
            return (String) getViewState().getAttribute("transferToScientificAreaUnitID");
        }
        return null;
    }

    public void setTransferToScientificAreaUnitID(String transferToScientificAreaUnitID) {
        this.getViewState().setAttribute("transferToScientificAreaUnitID", transferToScientificAreaUnitID);
    }

    private ScientificAreaUnit readScientificAreaUnitToTransferTo(String transferToScientificAreaUnitID) {
        return (ScientificAreaUnit) AbstractDomainObject.fromExternalId(transferToScientificAreaUnitID);
    }

    @Checked("RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE")
    @Service
    public String transferCompetenceCourse() {
        try {
            if (getCompetenceCourse() == null || readCompetenceCourseGroupUnitToTransferTo() == null
                    || getExecutionSemester() == null) {
                addErrorMessage(scouncilBundle.getString("error.transferingCompetenceCourse"));
                return "competenceCoursesManagement";
            }
            getCompetenceCourse().transfer((CompetenceCourseGroupUnit) readCompetenceCourseGroupUnitToTransferTo(),
                    getExecutionSemester(),
                    BundleUtil.getMessageFromModuleOrApplication("ScientificCouncil", "transfer.done.by.scientific.council"),
                    AccessControl.getPerson());

        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "competenceCoursesManagement";
    }

    private Unit readCompetenceCourseGroupUnitToTransferTo() {
        if (getTransferToCompetenceCourseGroupUnitID() != null) {
            return (Unit) AbstractDomainObject.fromExternalId(getTransferToCompetenceCourseGroupUnitID());
        }
        return null;
    }

    public String getTransferToCompetenceCourseGroupUnitID() {
        if (getViewState().getAttribute("transferToCompetenceCourseGroupUnitID") != null) {
            return (String) getViewState().getAttribute("transferToCompetenceCourseGroupUnitID");
        }
        return null;
    }

    public void setTransferToCompetenceCourseGroupUnitID(Integer transferToCompetenceCourseGroupUnitID) {
        this.getViewState().setAttribute("transferToCompetenceCourseGroupUnitID", transferToCompetenceCourseGroupUnitID);
    }

    private ExecutionSemester getExecutionSemester() {
        return AbstractDomainObject.fromExternalId(getExecutionSemesterID());
    }

    public String getExecutionSemesterID() {
        if (executionSemesterID == null) {
            executionSemesterID = (String) getViewState().getAttribute("executionSemesterID");
        }
        ExecutionSemester currentSemester = ExecutionSemester.readActualExecutionSemester();
        if ((executionSemesterID == null) && (getCompetenceCourse() != null)) {
            if (getCompetenceCourse().getCompetenceCourseInformationsCount() == 1) {
                executionSemesterID =
                        getCompetenceCourse().getCompetenceCourseInformationsSet().iterator().next().getExecutionPeriod()
                                .getExternalId();
            }
        }
        if (executionSemesterID == null) {
            executionSemesterID = currentSemester.getExternalId();
        }
        return executionSemesterID;
    }

    public void setExecutionSemesterID(String executionSemesterID) {
        this.executionSemesterID = executionSemesterID;
        reset();
    }

    public ExecutionYear getExecutionYear() {
        return AbstractDomainObject.fromExternalId(getExecutionYearID());
    }

    public String getExecutionYearID() {
        if (executionYearID == null) {
            executionYearID = getAndHoldStringParameter("executionYearID");
        }
        if (executionYearID == null) {
            executionYearID = ExecutionYear.readCurrentExecutionYear().getExternalId();
        }
        return executionYearID;
    }

    public void setExecutionYearID(String executionYearID) {
        this.executionYearID = executionYearID;
        reset();
    }

    public UISelectItems getExecutionSemesterItems() {
        if (executionSemesterItems == null) {
            executionSemesterItems = new UISelectItems();
            executionSemesterItems.setValue(readExecutionSemesterLabels());
        }
        return executionSemesterItems;
    }

    public void setExecutionSemesterItems(UISelectItems executionSemesterItems) {

    }

    private List<SelectItem> readExecutionSemesterLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (ExecutionSemester semester : getOrderedCompetenceCourseExecutionSemesters()) {
            result.add(new SelectItem(semester.getExternalId(), semester.getQualifiedName()));
        }
        return result;
    }

    public UISelectItems getFutureExecutionSemesterItems() {
        if (futureExecutionSemesterItems == null) {
            futureExecutionSemesterItems = new UISelectItems();
            futureExecutionSemesterItems.setValue(readFutureExecutionSemesterLabels());
        }
        return futureExecutionSemesterItems;
    }

    public void setFutureExecutionSemesterItems(UISelectItems futureExecutionSemesterItems) {
        this.futureExecutionSemesterItems = futureExecutionSemesterItems;
    }

    private List<SelectItem> readFutureExecutionSemesterLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        ExecutionSemester semester = ExecutionSemester.readActualExecutionSemester();
        while (semester != null) {
            result.add(new SelectItem(semester.getExternalId(), semester.getQualifiedName()));
            semester = semester.getNextExecutionPeriod();
        }
        return result;
    }

    public UISelectItems getCompetenceCourseExecutionSemesters() {
        if (competenceCourseExecutionSemesters == null) {
            competenceCourseExecutionSemesters = new UISelectItems();
            competenceCourseExecutionSemesters.setValue(readCompetenceCourseExecutionSemesterLabels());
        }
        return competenceCourseExecutionSemesters;
    }

    public void setCompetenceCourseExecutionSemesters(UISelectItems competenceCourseExecutionSemesters) {
        this.competenceCourseExecutionSemesters = competenceCourseExecutionSemesters;
    }

    private List<SelectItem> readCompetenceCourseExecutionSemesterLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (ExecutionSemester semester : getOrderedCompetenceCourseExecutionSemesters()) {
            result.add(new SelectItem(semester.getExternalId(), semester.getQualifiedName()));
        }
        return result;
    }

    private TreeSet<ExecutionSemester> getOrderedCompetenceCourseExecutionSemesters() {
        final TreeSet<ExecutionSemester> result =
                new TreeSet<ExecutionSemester>(ExecutionSemester.COMPARATOR_BY_SEMESTER_AND_YEAR);
        ExecutionSemester semester = getCompetenceCourse().getStartExecutionSemester();
        result.add(semester);
        while (semester.hasNextExecutionPeriod()) {
            semester = semester.getNextExecutionPeriod();
            result.add(semester);
        }
        return result;
    }

    public String editAcronym() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getAcronym());
            return "editCompetenceCourseMainPage";
        } catch (NotAuthorizedException e) {
            addErrorMessage(bolonhaResources.getString("error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessages(domainResources, e.getMessage(), e.getArgs());
        }
        return "";
    }

    public List<SelectItem> getExecutionYears() {
        if (selectedYears == null) {

            ExecutionYear year = null;
            if (getCompetenceCourse() != null) {
                final ExecutionSemester semester = getCompetenceCourse().getStartExecutionSemester();
                year = semester != null ? semester.getExecutionYear() : null;
            }

            selectedYears = new ArrayList<SelectItem>();
            for (ExecutionYear executionYear : ExecutionYear.readNotClosedExecutionYears()) {
                if (year == null || executionYear.isAfterOrEquals(year)) {
                    selectedYears.add(new SelectItem(executionYear.getExternalId(), executionYear.getYear()));
                }
            }
            Collections.sort(selectedYears, new ReverseComparator(new BeanComparator("label")));
        }

        return selectedYears;
    }

    private void reset() {
        name = null;
        nameEn = null;
        acronym = null;
        basic = null;
        objectives = null;
        program = null;
        evaluationMethod = null;
        objectivesEn = null;
        programEn = null;
        evaluationMethodEn = null;
        stage = null;
        bibliographicReferenceID = null;
    }

}