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
package org.fenixedu.academic.ui.faces.bean.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GradeScaleEnum;
import org.fenixedu.academic.domain.curricularRules.CurricularRuleValidationType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicPeriod;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYears;
import org.fenixedu.academic.dto.InfoExecutionYear;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.commons.ReadNotClosedExecutionYears;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.CreateDegreeCurricularPlan;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.DeleteDegreeCurricularPlan;
import org.fenixedu.academic.service.services.scientificCouncil.curricularPlans.EditDegreeCurricularPlan;
import org.fenixedu.academic.ui.faces.bean.base.FenixBackingBean;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DegreeCurricularPlanManagementBackingBean extends FenixBackingBean {
    private static final String NO_SELECTION = "noSelection";

    private String degreeId;
    private String dcpId;
    private DegreeCurricularPlan dcp;
    private String name;
    private Boolean applyPreviousYearsEnrolmentRule;
    private String[] selectedGroupMembersToDelete;
    private String newGroupMember;
    private String durationTypeName;
    private List<SelectItem> durationTypes = null;
    private String curricularRuleValidationTypeName;
    private List<SelectItem> curricularRuleValidationTypes = null;

    public String getCurricularRuleValidationTypeName() {
        if (curricularRuleValidationTypeName == null) {
            final CurricularRuleValidationType validationType =
                    getDcp() != null ? getDcp().getCurricularRuleValidationType() : null;
            curricularRuleValidationTypeName =
                    validationType != null ? validationType.getName() : CurricularRuleValidationType.SEMESTER.getName();
        }

        return curricularRuleValidationTypeName;
    }

    public void setCurricularRuleValidationTypeName(String curricularRuleValidationTypeName) {
        this.curricularRuleValidationTypeName = curricularRuleValidationTypeName;
    }

    public CurricularRuleValidationType getCurricularRuleValidationType() {
        return CurricularRuleValidationType.valueOf(getCurricularRuleValidationTypeName());
    }

    public List<SelectItem> getCurricularRuleValidationTypes() {
        return (curricularRuleValidationTypes == null) ? (curricularRuleValidationTypes =
                readCurricularRuleValidationTypes()) : curricularRuleValidationTypes;
    }

    private List<SelectItem> readCurricularRuleValidationTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final List<CurricularRuleValidationType> entries =
                new ArrayList<CurricularRuleValidationType>(Arrays.asList(CurricularRuleValidationType.values()));
        for (CurricularRuleValidationType entry : entries) {
            result.add(new SelectItem(entry.name(), entry.getDescription(I18N.getLocale())));
        }
        return result;
    }

    public String getAction() {
        return getAndHoldStringParameter("action");
    }

    public String getNewGroupMember() {
        return newGroupMember == null ? newGroupMember = getAndHoldStringParameter("newGroupMember") : newGroupMember;
    }

    public void setNewGroupMember(String newGroupMember) {
        this.newGroupMember = newGroupMember;
    }

    public String getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldStringParameter("degreeId")) : degreeId;
    }

    public String getDcpId() {
        if (dcp == null) {
            if (getAndHoldStringParameter("dcpId") != null) {
                dcpId = getAndHoldStringParameter("dcpId");
            } else {
                dcpId = getAndHoldStringParameter("degreeCurricularPlanID");
            }
        }
        return dcpId;
    }

    public void setDcpId(String dcpId) {
        this.dcpId = dcpId;
    }

    public DegreeCurricularPlan getDcp() {
        return (dcp == null) ? (dcp = FenixFramework.getDomainObject(getDcpId())) : dcp;
    }

    public void setDcp(DegreeCurricularPlan dcp) {
        this.dcp = dcp;
    }

    public List<String> getGroupMembersLabels() {
        return getGroupMembers().stream().map(SelectItem::getLabel).collect(Collectors.toList());
    }

    public List<SelectItem> getGroupMembers() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        Group curricularPlanMembersGroup = getDcp().getCurricularPlanMembersGroup();
        if (curricularPlanMembersGroup != null) {
            curricularPlanMembersGroup.getMembers().forEach(user -> result
                    .add(new SelectItem(user.getExternalId(), user.getPerson().getName() + " (" + user.getUsername() + ")")));
        }

        return result;
    }

    @Atomic
    public void addUserToGroup() {
        if (getNewGroupMember() != null) {
            User user = User.findByUsername(getNewGroupMember());
            if (user != null) {
                Group group = getDcp().getCurricularPlanMembersGroup();
                getDcp().setCurricularPlanMembersGroup(group.grant(user));
                RoleType.grant(RoleType.BOLONHA_MANAGER, user);
            }
        }
    }

    @Atomic
    public void removeUsersFromGroup(ActionEvent event) {
        if (selectedGroupMembersToDelete != null && selectedGroupMembersToDelete.length > 0) {

            Group group = getDcp().getCurricularPlanMembersGroup();

            for (String userExternalId : selectedGroupMembersToDelete) {
                User user = FenixFramework.getDomainObject(userExternalId);
                if (user != null) {
                    group = group.revoke(user);
                    removeRoleIfNecessary(user);
                }
            }
            getDcp().setCurricularPlanMembersGroup(group);
        }
    }

    private boolean isUserMemberOfAnyCurricularPlanGroup(User user) {
        return Degree.readBolonhaDegrees().stream().flatMap(d -> d.getDegreeCurricularPlansSet().stream())
                .filter(dcp -> !dcp.equals(getDcp())).anyMatch(dcp -> dcp.getCurricularPlanMembersGroup().isMember(user));
    }

    private boolean isUserMemberOfAnyDepartmentCompetenceCourseGroup(User user) {
        return Bennu.getInstance().getDepartmentsSet().stream().anyMatch(d -> d.getCompetenceCourseMembersGroup().isMember(user));
    }

    private void removeRoleIfNecessary(User user) {
        if (!isUserMemberOfAnyCurricularPlanGroup(user) && !isUserMemberOfAnyDepartmentCompetenceCourseGroup(user)) {
            RoleType.revoke(RoleType.BOLONHA_MANAGER, user);
        }
    }

    public String getName() {
        return (name == null && getDcp() != null) ? (name = getDcp().getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getApplyPreviousYearsEnrolmentRule() {
        return (applyPreviousYearsEnrolmentRule == null && getDcp() != null) ? (applyPreviousYearsEnrolmentRule =
                getDcp().getApplyPreviousYearsEnrolmentRule()) : applyPreviousYearsEnrolmentRule;
    }

    public void setApplyPreviousYearsEnrolmentRule(final Boolean input) {
        this.applyPreviousYearsEnrolmentRule = input;
    }

    public String getCurricularStage() {
        if (getViewState().getAttribute("curricularStage") == null && getDcp() != null) {
            setCurricularStage(getDcp().getCurricularStage().getName());
        }
        return (String) getViewState().getAttribute("curricularStage");
    }

    public void setCurricularStage(String curricularStage) {
        getViewState().setAttribute("curricularStage", curricularStage);
    }

    public String getState() {
        if (getViewState().getAttribute("state") == null && getDcp() != null) {
            setState(getDcp().getState().getName());
        }
        return (String) getViewState().getAttribute("state");
    }

    public void setState(String state) {
        getViewState().setAttribute("state", state);
    }

    public String createCurricularPlan() {
        try {
            CreateDegreeCurricularPlan.run(this.getDegreeId(), this.name, getDuration());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.creatingDegreeCurricularPlan"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degreeCurricularPlan.created"));
        return "curricularPlansManagement";
    }

    public List<SelectItem> getCurricularStages() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        if (getDcp().getExecutionDegreesSet().isEmpty()) {
            result.add(new SelectItem(CurricularStage.DRAFT.name(),
                    BundleUtil.getString(Bundle.ENUMERATION, CurricularStage.DRAFT.getName())));
        }
        result.add(new SelectItem(CurricularStage.PUBLISHED.name(),
                BundleUtil.getString(Bundle.ENUMERATION, CurricularStage.PUBLISHED.getName())));
        result.add(new SelectItem(CurricularStage.APPROVED.name(),
                BundleUtil.getString(Bundle.ENUMERATION, CurricularStage.APPROVED.getName())));

        return result;
    }

    public List<SelectItem> getStates() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        result.add(new SelectItem(DegreeCurricularPlanState.ACTIVE.name(),
                BundleUtil.getString(Bundle.ENUMERATION, DegreeCurricularPlanState.ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.NOT_ACTIVE.name(),
                BundleUtil.getString(Bundle.ENUMERATION, DegreeCurricularPlanState.NOT_ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.CONCLUDED.name(),
                BundleUtil.getString(Bundle.ENUMERATION, DegreeCurricularPlanState.CONCLUDED.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.PAST.name(),
                BundleUtil.getString(Bundle.ENUMERATION, DegreeCurricularPlanState.PAST.getName())));

        return result;
    }

    public ExecutionYear getExecutionYear() {
        return FenixFramework.getDomainObject(getExecutionYearID());
    }

    public String getExecutionYearID() {
        return (String) getViewState().getAttribute("executionYearID");
    }

    public void setExecutionYearID(String executionYearID) {
        getViewState().setAttribute("executionYearID", executionYearID);
    }

    public List<SelectItem> getExecutionYearItems() throws FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final InfoExecutionYear currentInfoExecutionYear =
                InfoExecutionYear.newInfoFromDomain(ExecutionYear.findCurrent(getDcp().getDegree().getCalendar()));
        final List<InfoExecutionYear> notClosedInfoExecutionYears = ReadNotClosedExecutionYears.run();

        for (final InfoExecutionYear notClosedInfoExecutionYear : notClosedInfoExecutionYears) {
            if (notClosedInfoExecutionYear.after(currentInfoExecutionYear)) {
                result.add(new SelectItem(notClosedInfoExecutionYear.getExternalId(), notClosedInfoExecutionYear.getYear()));
            }
        }

        result.add(0, new SelectItem(currentInfoExecutionYear.getExternalId(), currentInfoExecutionYear.getYear()));

        setDefaultExecutionYearIDIfExisting();

        return result;
    }

    private void setDefaultExecutionYearIDIfExisting() {
        final DegreeCurricularPlan dcp = getDcp();
        if (dcp != null) {
            final List<ExecutionYear> executionYears =
                    new ArrayList<ExecutionYear>(dcp.getRoot().getBeginContextExecutionYears());
            Collections.sort(executionYears, ExecutionYear.COMPARATOR_BY_YEAR);
            if (!executionYears.isEmpty()) {
                setExecutionYearID(executionYears.iterator().next().getExternalId());
            }
        }
    }

    public String editCurricularPlan() {

        try {

            editCurricularPlanService();
            addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degreeCurricularPlan.edited"));

        } catch (final IllegalDataAccessException e) {
            addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));

        } catch (final DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
            return "";

        } catch (final Exception e) {
            addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.editingDegreeCurricularPlan"));
        }

        return "curricularPlansManagement";
    }

    @Atomic
    protected void editCurricularPlanService() throws FenixServiceException {
        EditDegreeCurricularPlan.run(getDcp(), getName(), CurricularStage.valueOf(getCurricularStage()),
                DegreeCurricularPlanState.valueOf(getState()), getExecutionYear(), getDuration(),
                getApplyPreviousYearsEnrolmentRule());
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(getDcpId());
        degreeCurricularPlan.setCurricularRuleValidationType(getCurricularRuleValidationType());
    }

    public String deleteCurricularPlan() {
        try {
            DeleteDegreeCurricularPlan.run(this.getDcpId());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getKey(), e.getArgs()));
            return "";
        } catch (Exception e) {
            this.addErrorMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "error.deletingDegreeCurricularPlan"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(BundleUtil.getString(Bundle.SCIENTIFIC, "degreeCurricularPlan.deleted"));
        return "curricularPlansManagement";
    }

    public String[] getSelectedGroupMembersToDelete() {
        return selectedGroupMembersToDelete;
    }

    public void setSelectedGroupMembersToDelete(String[] selectedGroupMembersToDelete) {
        this.selectedGroupMembersToDelete = selectedGroupMembersToDelete;
    }

    public String getDurationTypeName() {
        if (durationTypeName == null && getDcp() != null) {
            final AcademicPeriod duration = getDcp().getDegreeStructure().getAcademicPeriod();
            return duration != null ? (durationTypeName = duration.getRepresentationInStringFormat()) : null;
        }

        return durationTypeName;
    }

    public void setDurationTypeName(String durationTypeName) {
        this.durationTypeName = durationTypeName;
    }

    private AcademicPeriod getDuration() {
        return getDurationTypeName().equals(NO_SELECTION) ? null : AcademicPeriod
                .getAcademicPeriodFromString(getDurationTypeName());
    }

    public List<SelectItem> getDurationTypes() {
        return (durationTypes == null) ? (durationTypes = readDurationTypes()) : durationTypes;
    }

    private List<SelectItem> readDurationTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final Set<AcademicPeriod> sortedPeriods = new TreeSet<>(Collections.reverseOrder());
        sortedPeriods.addAll(AcademicPeriod.values());

        for (final AcademicPeriod entry : sortedPeriods) {

            if (!(entry instanceof AcademicYears)) {
                //only year multiples are supported
                continue;
            }

            result.add(new SelectItem(entry.getRepresentationInStringFormat(),
                    BundleUtil.getString(Bundle.ENUMERATION, entry.getName())));
        }

        result.add(0, new SelectItem(NO_SELECTION, BundleUtil.getString(Bundle.SCIENTIFIC, "choose")));

        return result;
    }

}