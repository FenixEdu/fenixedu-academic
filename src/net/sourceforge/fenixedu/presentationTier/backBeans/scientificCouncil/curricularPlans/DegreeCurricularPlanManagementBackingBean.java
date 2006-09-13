package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class DegreeCurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("resources/ScientificCouncilResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("resources/EnumerationResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("resources/DomainExceptionResources");
    private final String NO_SELECTION = "noSelection";
    
    private Integer degreeId;
    private Integer dcpId;
    private DegreeCurricularPlan dcp;
    private String name;
    private String gradeScale;
    
    public String getAction() {
        return getAndHoldStringParameter("action");
    }
    
    public Integer getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldIntegerParameter("degreeId")) : degreeId;
    }

    public Integer getDcpId() {
        if (dcp == null) {
            if (getAndHoldIntegerParameter("dcpId") != null) {
                dcpId = getAndHoldIntegerParameter("dcpId");
            } else {
                dcpId = getAndHoldIntegerParameter("degreeCurricularPlanID");
            }
        }
        return dcpId;
    }

    public void setDcpId(Integer dcpId) {
        this.dcpId = dcpId;
    }

    public DegreeCurricularPlan getDcp() {
        return (dcp == null) ? (dcp = rootDomainObject.readDegreeCurricularPlanByOID(getDcpId())) : dcp;
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
        Object[] args = { this.getDegreeId(), this.name, null }; //GradeScale.valueOf(this.gradeScale) };
        return changeDegreeCurricularPlan("CreateDegreeCurricularPlan", args, "degreeCurricularPlan.created", "error.creatingDegreeCurricularPlan");
    }
    
    public List<SelectItem> getCurricularStages() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        if (!getDcp().hasAnyExecutionDegrees()) {
            result.add(new SelectItem(CurricularStage.DRAFT.name(), enumerationBundle.getString(CurricularStage.DRAFT.getName())));    
        }
        result.add(new SelectItem(CurricularStage.PUBLISHED.name(), enumerationBundle.getString(CurricularStage.PUBLISHED.getName())));
        result.add(new SelectItem(CurricularStage.APPROVED.name(), enumerationBundle.getString(CurricularStage.APPROVED.getName())));
        
        return result;
    }
    
    public List<SelectItem> getStates() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(DegreeCurricularPlanState.ACTIVE.name(), enumerationBundle.getString(DegreeCurricularPlanState.ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.NOT_ACTIVE.name(), enumerationBundle.getString(DegreeCurricularPlanState.NOT_ACTIVE.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.CONCLUDED.name(), enumerationBundle.getString(DegreeCurricularPlanState.CONCLUDED.getName())));
        result.add(new SelectItem(DegreeCurricularPlanState.PAST.name(), enumerationBundle.getString(DegreeCurricularPlanState.PAST.getName())));
        
        return result;
    }
    
    public Integer getExecutionYearID() {
        return (Integer) getViewState().getAttribute("executionYearID");
    }
    
    public void setExecutionYearID(Integer executionYearID) {
        getViewState().setAttribute("executionYearID", executionYearID);
    }
    
    public List<SelectItem> getExecutionYearItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();

        final InfoExecutionYear currentInfoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(getUserView(), "ReadCurrentExecutionYear", new Object[] {});
        final List<InfoExecutionYear> notClosedInfoExecutionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(getUserView(), "ReadNotClosedExecutionYears", new Object[] {});
        for (final InfoExecutionYear notClosedInfoExecutionYear : notClosedInfoExecutionYears) {
            if (notClosedInfoExecutionYear.after(currentInfoExecutionYear)) {
                result.add(new SelectItem(notClosedInfoExecutionYear.getIdInternal(), notClosedInfoExecutionYear.getYear()));
            }
        }
        result.add(0, new SelectItem(currentInfoExecutionYear.getIdInternal(), currentInfoExecutionYear.getYear()));
        return result;
    }
    
    public String editCurricularPlan() {
        Object[] args = { getDcpId(), getName(), CurricularStage.valueOf(getCurricularStage()), DegreeCurricularPlanState.valueOf(getState()), null, getExecutionYearID() };
        return changeDegreeCurricularPlan("EditDegreeCurricularPlan", args, "degreeCurricularPlan.edited", "error.editingDegreeCurricularPlan");
    }
    
    private String changeDegreeCurricularPlan(String serviceName, Object[] args, String successfulMsg, String errorMsg) {
        try {
            ServiceUtils.executeService(this.getUserView(), serviceName, args);
        } catch (FenixFilterException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            addErrorMessages(domainExceptionBundle, e.getKey(), e.getArgs());
            return "";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString(errorMsg));
            return "curricularPlansManagement";
        }
        
        this.addInfoMessage(scouncilBundle.getString(successfulMsg));
        return "curricularPlansManagement";
    }

    public String deleteCurricularPlan() {
        Object[] args = { this.getDcpId() };
        return changeDegreeCurricularPlan("DeleteDegreeCurricularPlan", args, "degreeCurricularPlan.deleted", "error.deletingDegreeCurricularPlan");
    }
    
}
