package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ScientificCouncilCurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("ServidorApresentacao/ScientificCouncilResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("ServidorApresentacao/EnumerationResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("ServidorApresentacao/DomainExceptionResources");
    private final String NO_SELECTION = "noSelection";
    
    private Integer degreeId;
    private Integer dcpId;
    private IDegreeCurricularPlan dcp;
    private String name;
    private Double ectsCredits;
    private String curricularStage;
    private String gradeScale;
    
    public Integer getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldIntegerParameter("degreeId")) : degreeId;
    }

    public Integer getDcpId() {
        return (dcpId == null) ? (dcpId = getAndHoldIntegerParameter("dcpId")) : dcpId;
    }

    public void setDcpId(Integer dcpId) {
        this.dcpId = dcpId;
    }

    public IDegreeCurricularPlan getDcp() throws FenixFilterException, FenixServiceException {
        return (dcp == null) ? (dcp = (IDegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDcpId())) : dcp;
    }

    public void setDcp(IDegreeCurricularPlan dcp) {
        this.dcp = dcp;
    }

    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getDcp() != null) ? (name = getDcp().getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public Double getEctsCredits() throws FenixFilterException, FenixServiceException {
        return (ectsCredits == null && getDcp() != null) ? (ectsCredits = getDcp().getEctsCredits()) : ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
        this.ectsCredits = ectsCredits;
    }
    
    public String getCurricularStage() throws FenixFilterException, FenixServiceException {
        return (curricularStage == null && getDcp() != null) ? (curricularStage = getDcp().getCurricularStage().getName()) : curricularStage;
    }

    public void setCurricularStage(String curricularStage) {
        this.curricularStage = curricularStage;
    }
    
    public String getGradeScale() throws FenixFilterException, FenixServiceException {
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
        Object[] args = { this.getDegreeId(), this.name, this.ectsCredits, CurricularStage.DRAFT, null }; //GradeScale.valueOf(this.gradeScale) };
        return changeDegreeCurricularPlan("CreateDegreeCurricularPlan", args, "degreeCurricularPlan.created", "error.creatingDegreeCurricularPlan");
    }
    
    public List<SelectItem> getCurricularStages() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(CurricularStage.DRAFT.name(), enumerationBundle.getString(CurricularStage.DRAFT.getName())));
        result.add(new SelectItem(CurricularStage.PUBLISHED.name(), enumerationBundle.getString(CurricularStage.PUBLISHED.getName())));
        result.add(new SelectItem(CurricularStage.APPROVED.name(), enumerationBundle.getString(CurricularStage.APPROVED.getName())));
        
        return result;
    }
    
    public String editCurricularPlan() {
        Object[] args = { this.getDcpId(), this.name, this.ectsCredits, CurricularStage.valueOf(this.curricularStage), null }; //GradeScale.valueOf(this.gradeScale) };
        return changeDegreeCurricularPlan("EditDegreeCurricularPlan", args, "degreeCurricularPlan.edited", "error.editingDegreeCurricularPlan");
    }
    
    private String changeDegreeCurricularPlan(String serviceName, Object[] args, String successfulMsg, String errorMsg) {
        try {
            ServiceUtils.executeService(this.getUserView(), serviceName, args);
        } catch (FenixFilterException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (FenixServiceException e) {
            this.setErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "";
        } catch (DomainException e) {
            this.setErrorMessage(domainExceptionBundle.getString(e.getMessage()));
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
