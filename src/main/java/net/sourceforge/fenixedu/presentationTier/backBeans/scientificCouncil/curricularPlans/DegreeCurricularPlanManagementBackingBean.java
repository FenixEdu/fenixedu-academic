package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadCurrentExecutionYear;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadNotClosedExecutionYears;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.CreateDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.DeleteDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans.EditDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DegreeCurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");
    private final String NO_SELECTION = "noSelection";

    private String degreeId;
    private String dcpId;
    private DegreeCurricularPlan dcp;
    private String name;
    private String gradeScale;

    public String getAction() {
        return getAndHoldStringParameter("action");
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
        return (dcp == null) ? (dcp = AbstractDomainObject.fromExternalId(getDcpId())) : dcp;
    }

    public void setDcp(DegreeCurricularPlan dcp) {
        this.dcp = dcp;
    }

    public String getName() {
        return (name == null && getDcp() != null) ? (name = getDcp().getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGradeScale() {
        return (gradeScale == null && getDcp() != null) ? (gradeScale = getDcp().getGradeScale().getName()) : gradeScale;
    }

    public void setGradeScale(String gradeScale) {
        this.gradeScale = gradeScale;
    }

    public List<SelectItem> getGradeScales() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(GradeScale.TYPE20.name(), enumerationBundle.getString(GradeScale.TYPE20.name())));
        result.add(new SelectItem(GradeScale.TYPE5.name(), enumerationBundle.getString(GradeScale.TYPE5.name())));

        return result;
    }

    public String createCurricularPlan() {
        // valueOf
        // (this.
        // gradeScale)
        // };
        try {
            CreateDegreeCurricularPlan.run(this.getDegreeId(), this.name, null);
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessages(domainExceptionBundle, e.getKey(), e.getArgs());
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.creatingDegreeCurricularPlan"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degreeCurricularPlan.created"));
        return "curricularPlansManagement";
    }

    public List<SelectItem> getCurricularStages() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        if (!getDcp().hasAnyExecutionDegrees()) {
            result.add(new SelectItem(CurricularStage.DRAFT.name(), enumerationBundle.getString(CurricularStage.DRAFT.getName())));
        }
        result.add(new SelectItem(CurricularStage.PUBLISHED.name(), enumerationBundle.getString(CurricularStage.PUBLISHED
                .getName())));
        result.add(new SelectItem(CurricularStage.APPROVED.name(),
                enumerationBundle.getString(CurricularStage.APPROVED.getName())));

        return result;
    }

    public List<SelectItem> getStates() {
        List<SelectItem> result = new ArrayList<SelectItem>();

        result.add(new SelectItem(DegreeCurricularPlanState.ACTIVE.name(), enumerationBundle
                .getString(DegreeCurricularPlanState.ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.NOT_ACTIVE.name(), enumerationBundle
                .getString(DegreeCurricularPlanState.NOT_ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.CONCLUDED.name(), enumerationBundle
                .getString(DegreeCurricularPlanState.CONCLUDED.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.PAST.name(), enumerationBundle
                .getString(DegreeCurricularPlanState.PAST.getName())));

        return result;
    }

    public String getExecutionYearID() {
        return (String) getViewState().getAttribute("executionYearID");
    }

    public void setExecutionYearID(String executionYearID) {
        getViewState().setAttribute("executionYearID", executionYearID);
    }

    public List<SelectItem> getExecutionYearItems() throws FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final InfoExecutionYear currentInfoExecutionYear = ReadCurrentExecutionYear.run();
        final List<InfoExecutionYear> notClosedInfoExecutionYears = ReadNotClosedExecutionYears.run();

        for (final InfoExecutionYear notClosedInfoExecutionYear : notClosedInfoExecutionYears) {
            Person loggedPerson = AccessControl.getPerson();
            if (loggedPerson.hasRole(RoleType.MANAGER) || notClosedInfoExecutionYear.after(currentInfoExecutionYear)) {
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
                setExecutionYearID(executionYears.get(0).getExternalId());
            }
        }
    }

    public String editCurricularPlan() {
        try {
            EditDegreeCurricularPlan.run(getDcpId(), getName(), CurricularStage.valueOf(getCurricularStage()),
                    DegreeCurricularPlanState.valueOf(getState()), null, getExecutionYearID());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessages(domainExceptionBundle, e.getKey(), e.getArgs());
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.editingDegreeCurricularPlan"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degreeCurricularPlan.edited"));
        return "curricularPlansManagement";
    }

    public String deleteCurricularPlan() {
        try {
            DeleteDegreeCurricularPlan.run(this.getDcpId());
        } catch (IllegalDataAccessException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessages(domainExceptionBundle, e.getKey(), e.getArgs());
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.deletingDegreeCurricularPlan"));
            return "curricularPlansManagement";
        }

        this.addInfoMessage(scouncilBundle.getString("degreeCurricularPlan.deleted"));
        return "curricularPlansManagement";
    }

}