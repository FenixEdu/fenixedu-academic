/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Dec 8, 2005
 */
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.competenceCourses;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CompetenceCourse;
import org.fenixedu.academic.domain.CompetenceCourseType;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReference;
import org.fenixedu.academic.domain.degreeStructure.BibliographicReferences.BibliographicReferenceType;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseInformation;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLevel;
import org.fenixedu.academic.domain.degreeStructure.CompetenceCourseLoad;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.degreeStructure.RegimeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.CompetenceCourseGroupUnit;
import org.fenixedu.academic.domain.organizationalStructure.DepartmentUnit;
import org.fenixedu.academic.domain.organizationalStructure.ScientificAreaUnit;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.dto.bolonhaManager.CourseLoad;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.bolonhaManager.CreateCompetenceCourse;
import org.fenixedu.academic.service.services.bolonhaManager.DeleteCompetenceCourse;
import org.fenixedu.academic.service.services.bolonhaManager.EditCompetenceCourse;
import org.fenixedu.academic.service.services.bolonhaManager.EditCompetenceCourseLoad;
import org.fenixedu.academic.service.services.exceptions.ExistingCompetenceCourseInformationException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import javax.faces.component.UISelectItems;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.fenixedu.academic.predicate.AccessControl.check;

public class CompetenceCourseManagementBackingBean extends FenixBackingBean {
    private final Integer NO_SELECTION = 0;

    private String selectedDepartmentUnitID = null;
    private String competenceCourseID = null;
    private String executionYearID = null;
    private String executionSemesterID = null;
    private Unit competenceCourseGroupUnit = null;
    private CompetenceCourse competenceCourse = null;

    // Competence-Course-Information
    private String code;
    private String name;
    private String nameEn;
    private String acronym;
    private Boolean basic;
    private boolean setNumberOfPeriods = true;
    // Competence-Course-Additional-Data
    private String objectives;
    private String program;
    private String evaluationMethod;
    private String prerequisites;
    private String laboratorialComponent;
    private String programmingAndComputingComponent;
    private String crossCompetenceComponent;
    private String ethicalPrinciples;
    private String objectivesEn;
    private String programEn;
    private String evaluationMethodEn;
    private String prerequisitesEn;
    private String laboratorialComponentEn;
    private String programmingAndComputingComponentEn;
    private String crossCompetenceComponentEn;
    private String ethicalPrinciplesEn;

    private String stage;
    // BibliographicReferences
    private Integer bibliographicReferenceID;
    private String year;
    private String title;
    private String author;
    private String reference;
    private String type;
    private String url;

    // group management
    private String newGroupMember;
    private String[] selectedGroupMembersToDelete;
    private Boolean groupEditMode;

    private UISelectItems departmentUnitItems;
    private UISelectItems scientificAreaUnitItems;
    private UISelectItems competenceCourseGroupUnitItems;
    private UISelectItems competenceCourseExecutionSemesters;
    private UISelectItems executionSemesterItems;
    private UISelectItems futureExecutionSemesterItems;

    private List<SelectItem> selectedYears = null;

    public String[] getSelectedGroupMembersToDelete() {
        return selectedGroupMembersToDelete;
    }

    public Boolean getGroupEditMode() {
        if (groupEditMode == null) {
            String requestParameter = getRequestParameter("groupEditMode");
            setGroupEditMode("true".equals(requestParameter));
        }
        return groupEditMode;
    }

    public void setGroupEditMode(Boolean groupEditMode) {
        this.groupEditMode = groupEditMode;
    }

    public void toggleGroupEditMode() {
        setGroupEditMode(!getGroupEditMode());
    }

    public void setSelectedGroupMembersToDelete(String[] selectedGroupMembersToDelete) {
        this.selectedGroupMembersToDelete = selectedGroupMembersToDelete;
    }

    public String getNewGroupMember() {
        return newGroupMember == null ? newGroupMember = getAndHoldStringParameter("newGroupMember") : newGroupMember;
    }

    public void setNewGroupMember(String newGroupMember) {
        this.newGroupMember = newGroupMember;
    }

    public String getAction() {
        return getAndHoldStringParameter("action");
    }

    public String getCompetenceCoursesToList() {
        return getAndHoldStringParameter("competenceCoursesToList");
    }

    public Boolean getCanView() {
        DepartmentUnit selectedDepartmentUnit = getSelectedDepartmentUnit();
        if (selectedDepartmentUnit == null) {
            return (this.getPersonDepartment() != null
                    && this.getPersonDepartment().getCompetenceCourseMembersGroup() != null) ? this.getPersonDepartment()
                            .getCompetenceCourseMembersGroup().isMember(this.getUserView()) : false;
        } else {
            return selectedDepartmentUnit.getDepartment().getCompetenceCourseMembersGroup() != null ? selectedDepartmentUnit
                    .getDepartment().getCompetenceCourseMembersGroup().isMember(getUserView()) : false;
        }
    }

    public Department getPersonDepartment() {
        final User user = Authenticate.getUser();
        return Bennu.getInstance().getDepartmentsSet().stream()
                .filter(dep -> dep.getCompetenceCourseMembersGroup().isMember(user)).findAny().orElse(null);
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
            return (DepartmentUnit) FenixFramework.getDomainObject(this.getSelectedDepartmentUnitID());
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
        return getGroupMembers().stream().map(SelectItem::getLabel).collect(Collectors.toList());
    }

    public List<SelectItem> getGroupMembers() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        if (getSelectedDepartmentUnit() == null || getSelectedDepartmentUnit().getDepartment() == null
                || getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup() == null) {
            return result;
        }

        Group competenceCoursesManagementGroup = getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup();

        if (competenceCoursesManagementGroup != null) {
            competenceCoursesManagementGroup.getMembers().forEach(user -> result
                    .add(new SelectItem(user.getExternalId(), user.getProfile().getFullName() + " (" + user.getUsername() + ")")));
        }

        return result;
    }

    private boolean isUserMemberOfAnyCurricularPlanGroup(User user) {
        return Degree.readBolonhaDegrees().stream().flatMap(d -> d.getDegreeCurricularPlansSet().stream())
                .map(dcp -> dcp.getCurricularPlanMembersGroup()).reduce(Group.nobody(), (g1, g2) -> g1.or(g2)).isMember(user);
    }

    private boolean isUserMemberOfAnyDepartmentCompetenceCourseGroup(User user) {
        return Bennu.getInstance().getDepartmentsSet().stream()
                .filter(d -> !d.equals(getSelectedDepartmentUnit().getDepartment()))
                .anyMatch(d -> d.getCompetenceCourseMembersGroup().isMember(user));
    }

    private void removeRoleIfNecessary(User user) {
        if (!isUserMemberOfAnyCurricularPlanGroup(user) && !isUserMemberOfAnyDepartmentCompetenceCourseGroup(user)) {
            DynamicGroup dynaGroup = DynamicGroup.get("bolonhaManager");
            dynaGroup.mutator().changeGroup(dynaGroup.underlyingGroup().revoke(user));
        }
    }

    @Atomic
    public void addUserToGroup() {
        if (getNewGroupMember() != null) {
            User user = User.findByUsername(getNewGroupMember());
            if (user != null) {
                Group group = getSelectedDepartmentUnit().getDepartment().getCompetenceCourseMembersGroup();
                getSelectedDepartmentUnit().getDepartment().setCompetenceCourseMembersGroup(group.grant(user));
                DynamicGroup dynaGroup = DynamicGroup.get("bolonhaManager");
                dynaGroup.mutator().changeGroup(dynaGroup.underlyingGroup().grant(user));
            }
        }
    }

    @Atomic
    public void removeUsersFromGroup(ActionEvent event) {
        if (selectedGroupMembersToDelete != null && selectedGroupMembersToDelete.length > 0) {

            final Department department = getSelectedDepartmentUnit().getDepartment();
            Group group = department.getCompetenceCourseMembersGroup();

            for (String userExternalId : selectedGroupMembersToDelete) {
                User user = FenixFramework.getDomainObject(userExternalId);
                if (user != null) {
                    group = group.revoke(user);
                    removeRoleIfNecessary(user);
                }
            }

            department.setCompetenceCourseMembersGroup(group);
        }
    }

    public String getCompetenceCourseGroupUnitID() {
        return getAndHoldStringParameter("competenceCourseGroupUnitID");
    }

    public Unit getCompetenceCourseGroupUnit() {
        if (competenceCourseGroupUnit == null && getCompetenceCourseGroupUnitID() != null) {
            competenceCourseGroupUnit = (Unit) FenixFramework.getDomainObject(getCompetenceCourseGroupUnitID());
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

    public String getCode() {
        if (code == null && getCompetenceCourse() != null) {
            code = getCompetenceCourse().getCode();
        }

        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
                RegimeType regime = getCompetenceCourse().getRegime(getAssociatedExecutionPeriod());
                setRegime(regime != null ? regime.getName() : "");
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
                    setCompetenceCourseLevel(
                            getCompetenceCourse().getCompetenceCourseLevel(getAssociatedExecutionPeriod()).getName());
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
        result.add(new SelectItem(Integer.valueOf(2), BundleUtil.getString(Bundle.BOLONHA, "yes")));
        result.add(new SelectItem(Integer.valueOf(1), BundleUtil.getString(Bundle.BOLONHA, "no")));
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
            if (getAndHoldStringParameter("selectedDepartmentUnitID") != null) {
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
        return (competenceCourseID == null) ? (competenceCourseID =
                getAndHoldStringParameter("competenceCourseID")) : competenceCourseID;
    }

    public void setCompetenceCourseID(String competenceCourseID) {
        this.competenceCourseID = competenceCourseID;
    }

    public CompetenceCourse getCompetenceCourse() {
        if (competenceCourse == null && getCompetenceCourseID() != null) {
            competenceCourse = FenixFramework.getDomainObject(getCompetenceCourseID());
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

    public String getPrerequisites() {
        if (prerequisites == null && getCompetenceCourse() != null) {
            prerequisites = getCompetenceCourse().getPrerequisites(getAssociatedExecutionPeriod());
        }
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getLaboratorialComponent() {
        if (laboratorialComponent == null && getCompetenceCourse() != null) {
            laboratorialComponent = getCompetenceCourse().getLaboratorialComponent(getAssociatedExecutionPeriod());
        }
        return laboratorialComponent;
    }

    public void setLaboratorialComponent(String laboratorialComponent) {
        this.laboratorialComponent = laboratorialComponent;
    }

    public String getProgrammingAndComputingComponent() {
        if (programmingAndComputingComponent == null && getCompetenceCourse() != null) {
            programmingAndComputingComponent = getCompetenceCourse().getProgrammingAndComputingComponent(getAssociatedExecutionPeriod());
        }
        return programmingAndComputingComponent;
    }

    public void setProgrammingAndComputingComponent(String programmingAndComputingComponent) {
        this.programmingAndComputingComponent = programmingAndComputingComponent;
    }

    public String getCrossCompetenceComponent() {
        if (crossCompetenceComponent == null && getCompetenceCourse() != null) {
            crossCompetenceComponent = getCompetenceCourse().getCrossCompetenceComponent(getAssociatedExecutionPeriod());
        }
        return crossCompetenceComponent;
    }

    public void setCrossCompetenceComponent(String crossCompetenceComponent) {
        this.crossCompetenceComponent = crossCompetenceComponent;
    }

    public String getEthicalPrinciples() {
        if (ethicalPrinciples == null && getCompetenceCourse() != null) {
            ethicalPrinciples = getCompetenceCourse().getEthicalPrinciples(getAssociatedExecutionPeriod());
        }
        return ethicalPrinciples;
    }

    public void setEthicalPrinciples(String ethicalPrinciples) {
        this.ethicalPrinciples = ethicalPrinciples;
    }

    public String getPrerequisitesEn() {
        if (prerequisitesEn == null && getCompetenceCourse() != null) {
            prerequisitesEn = getCompetenceCourse().getPrerequisitesEn(getAssociatedExecutionPeriod());
        }
        return prerequisitesEn;
    }

    public void setPrerequisitesEn(String prerequisitesEn) {
        this.prerequisitesEn = prerequisitesEn;
    }

    public String getLaboratorialComponentEn() {
        if (laboratorialComponentEn == null && getCompetenceCourse() != null) {
            laboratorialComponentEn = getCompetenceCourse().getLaboratorialComponentEn(getAssociatedExecutionPeriod());
        }
        return laboratorialComponentEn;
    }

    public void setLaboratorialComponentEn(String laboratorialComponentEn) {
        this.laboratorialComponentEn = laboratorialComponentEn;
    }

    public String getProgrammingAndComputingComponentEn() {
        if (programmingAndComputingComponentEn == null && getCompetenceCourse() != null) {
            programmingAndComputingComponentEn = getCompetenceCourse().getProgrammingAndComputingComponentEn(getAssociatedExecutionPeriod());
        }
        return programmingAndComputingComponentEn;
    }

    public void setProgrammingAndComputingComponentEn(String programmingAndComputingComponentEn) {
        this.programmingAndComputingComponentEn = programmingAndComputingComponentEn;
    }

    public String getCrossCompetenceComponentEn() {
        if (crossCompetenceComponentEn == null && getCompetenceCourse() != null) {
            crossCompetenceComponentEn = getCompetenceCourse().getCrossCompetenceComponentEn(getAssociatedExecutionPeriod());
        }
        return crossCompetenceComponentEn;
    }

    public void setCrossCompetenceComponentEn(String crossCompetenceComponentEn) {
        this.crossCompetenceComponentEn = crossCompetenceComponentEn;
    }

    public String getEthicalPrinciplesEn() {
        if (ethicalPrinciplesEn == null && getCompetenceCourse() != null) {
            ethicalPrinciplesEn = getCompetenceCourse().getEthicalPrinciplesEn(getAssociatedExecutionPeriod());
        }
        return ethicalPrinciplesEn;
    }

    public void setEthicalPrinciplesEn(String ethicalPrinciplesEn) {
        this.ethicalPrinciplesEn = ethicalPrinciplesEn;
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
        return (getCompetenceCourse()
                .getBibliographicReferences(getAssociatedExecutionPeriod()) == null) ? null : getCompetenceCourse()
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
                addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.mustSetCompetenceCourseLevel"));
            }

            if (!isCompetenceCourseTypeValid()) {
                valid = false;
                addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.mustSetCompetenceCourseType"));
            }

            if (valid) {

                final CompetenceCourse competenceCourse = CreateCompetenceCourse.run(getName(), getNameEn(), null, getBasic(),
                        RegimeType.SEMESTRIAL, getEnumCompetenceCourseLevel(), getEnumCompetenceCourseType(),
                        getCompetenceCourseGroupUnitID(), getExecutionSemester(), getCode());
                setCompetenceCourse(competenceCourse);
                return "setCompetenceCourseLoad";
            }
        } catch (IllegalDataAccessException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.creatingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    public String createCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "setCompetenceCourseAdditionalInformation";
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    public String createCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "competenceCoursesManagement";
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourse() {
        try {
            if (isCompetenceCourseLevelValid()) {
                EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getName(), getNameEn(), getBasic(),
                        getEnumCompetenceCourseLevel(), getEnumCompetenceCourseType(), CurricularStage.valueOf(getStage()),
                        getCode());
                return "editCompetenceCourseMainPage";

            } else {
                addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.mustSetCompetenceCourseLevel"));
            }
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (ExistingCompetenceCourseInformationException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getKey(), e.getArgs()));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourseLoad() {
        try {
            setCompetenceCourseLoad();
            return "editCompetenceCourseMainPage";
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    public String editCompetenceCourseAdditionalInformation() {
        try {
            setCompetenceCourseAdditionalInformation();
            return "editCompetenceCourseMainPage";
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        }
        return "";
    }

    private void setCompetenceCourseLoad() throws FenixServiceException {

        EditCompetenceCourseLoad.run(getCompetenceCourseID(), RegimeType.valueOf(getRegime()), getNumberOfPeriods(),
                getCourseLoads());
    }

    private void setCompetenceCourseAdditionalInformation() throws FenixServiceException {
        EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getObjectives(), getProgram(),
                getEvaluationMethod(), getPrerequisites(), getLaboratorialComponent(), getProgrammingAndComputingComponent(),
                getCrossCompetenceComponent(), getEthicalPrinciples(), getObjectivesEn(), getProgramEn(), getEvaluationMethodEn(),
                getPrerequisitesEn(), getLaboratorialComponentEn(), getProgrammingAndComputingComponentEn(),
                getCrossCompetenceComponentEn(), getEthicalPrinciplesEn());
    }

    public String deleteCompetenceCourse() {
        try {

            DeleteCompetenceCourse.run(getCompetenceCourseID());
            addInfoMessage(BundleUtil.getString(Bundle.BOLONHA, "competenceCourseDeleted"));
            return "competenceCoursesManagement";
        } catch (IllegalDataAccessException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.deletingCompetenceCourse"));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

    public String createBibliographicReference() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getYear(), getTitle(), getAuthor(),
                    getReference(), BibliographicReferenceType.valueOf(getType()), getUrl());
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.creatingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        setBibliographicReferenceID(-1);
        return "";
    }

    public String editBibliographicReference() {
        try {
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), getBibliographicReferenceID(), getYear(),
                    getTitle(), getAuthor(), getReference(), BibliographicReferenceType.valueOf(getType()), getUrl());
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
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
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.deletingBibliographicReference"));
        } catch (FenixServiceException e) {
            addErrorMessage(e.getMessage());
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
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
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.switchBibliographicReferencePositions"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
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
            CurricularStage changed = (getCompetenceCourse().getCurricularStage()
                    .equals(CurricularStage.PUBLISHED) ? CurricularStage.APPROVED : CurricularStage.PUBLISHED);
            EditCompetenceCourse.runEditCompetenceCourse(getCompetenceCourseID(), changed);
            return "";
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
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

    public void onChangeExecutionSemester(ValueChangeEvent event) {
        setExecutionSemesterID((String) event.getNewValue());
        getDepartmentUnitItems().setValue(readDepartmentUnitLabels());
        getScientificAreaUnitItems().setValue(readScientificAreaUnitLabels(null));
        getCompetenceCourseGroupUnitItems().setValue(readCompetenceCourseGroupUnitLabels(null));
    }

    public void onChangeDepartmentUnit(ValueChangeEvent event) {
        setTransferToDepartmentUnitID((String) event.getNewValue());
        getScientificAreaUnitItems().setValue(readScientificAreaUnitLabels((String) event.getNewValue()));
        getCompetenceCourseGroupUnitItems().setValue(readCompetenceCourseGroupUnitLabels(null));
    }

    private List<SelectItem> readDepartmentUnitLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final Department departmentObject : Bennu.getInstance().getDepartmentsSet()) {
            DepartmentUnit departmentUnit = departmentObject.getDepartmentUnit();
            if (departmentUnit.isActive(getExecutionSemester().getBeginDateYearMonthDay())) {
                result.add(new SelectItem(departmentUnit.getExternalId(), departmentUnit.getName()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.BOLONHA, "choose")));
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
        result.add(0, new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.BOLONHA, "choose")));
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
        return (DepartmentUnit) FenixFramework.getDomainObject(transferToDepartmentUnitID);
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
        result.add(0, new SelectItem(this.NO_SELECTION, BundleUtil.getString(Bundle.BOLONHA, "choose")));
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
        return (ScientificAreaUnit) FenixFramework.getDomainObject(transferToScientificAreaUnitID);
    }

    public String transferCompetenceCourse() {
        check(this, RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        try {
            if (getCompetenceCourse() == null || readCompetenceCourseGroupUnitToTransferTo() == null
                    || getExecutionSemester() == null) {
                addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.transferingCompetenceCourse"));
                return "competenceCoursesManagement";
            }

            FenixFramework.atomic(() -> {
                getCompetenceCourse().transfer((CompetenceCourseGroupUnit) readCompetenceCourseGroupUnitToTransferTo(),
                        getExecutionSemester(), BundleUtil.getString(Bundle.SCIENTIFIC, "transfer.done.by.scientific.council"),
                        AccessControl.getPerson());
            });

        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "competenceCoursesManagement";
    }

    private Unit readCompetenceCourseGroupUnitToTransferTo() {
        if (getTransferToCompetenceCourseGroupUnitID() != null) {
            return (Unit) FenixFramework.getDomainObject(getTransferToCompetenceCourseGroupUnitID());
        }
        return null;
    }

    public String getTransferToCompetenceCourseGroupUnitID() {
        if (getViewState().getAttribute("transferToCompetenceCourseGroupUnitID") != null) {
            return (String) getViewState().getAttribute("transferToCompetenceCourseGroupUnitID");
        }
        return null;
    }

    public void setTransferToCompetenceCourseGroupUnitID(String transferToCompetenceCourseGroupUnitID) {
        this.getViewState().setAttribute("transferToCompetenceCourseGroupUnitID", transferToCompetenceCourseGroupUnitID);
    }

    private ExecutionSemester getExecutionSemester() {
        return FenixFramework.getDomainObject(getExecutionSemesterID());
    }

    public String getExecutionSemesterID() {
        if (executionSemesterID == null) {
            executionSemesterID = (String) getViewState().getAttribute("executionSemesterID");
        }
        if (executionSemesterID == null) {
            final ExecutionYear executionYear = getExecutionYear();
            if (executionYear != null) {
                executionSemesterID = executionYear.getFirstExecutionPeriod().getExternalId();
            }
        }
        ExecutionSemester currentSemester = ExecutionSemester.readActualExecutionSemester();
        if ((executionSemesterID == null) && (getCompetenceCourse() != null)) {
            if (getCompetenceCourse().getCompetenceCourseInformationsSet().size() == 1) {
                executionSemesterID = getCompetenceCourse().getCompetenceCourseInformationsSet().iterator().next()
                        .getExecutionPeriod().getExternalId();
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
        return FenixFramework.getDomainObject(getExecutionYearID());
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
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.editingCompetenceCourse"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage(), e.getArgs()));
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

    public boolean isBasic() {
        final CompetenceCourseInformation information = getCompetenceCourse().findCompetenceCourseInformationForExecutionPeriod(getAssociatedExecutionPeriod());
        return information != null && information.getBasic() && information.getBasic().booleanValue();
    }

}