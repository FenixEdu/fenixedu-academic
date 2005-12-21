package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ScientificCouncilCurricularPlanManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("ServidorApresentacao/ScientificCouncilResources");
    private final ResourceBundle domainExceptionBundle = getResourceBundle("ServidorApresentacao/EnumerationResources");
    
    private Integer degreeId;
    private Integer dcpId;
    private IDegreeCurricularPlan dcp;
    private String name;
    private String acronym;
    
    public Integer getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldIntegerParameter("degreeId")) : degreeId;
    }

    public Integer getDcpId() {
        return (dcpId == null) ? (dcpId = getAndHoldIntegerParameter("dcpId")) : dcpId;
    }

    public void setDcpId(Integer dcpId) {
        this.dcpId = dcpId;
    }
    
    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getDcp() != null) ? (name = getDcp().getName()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }    

//    public String getAcronym() throws FenixFilterException, FenixServiceException {
//        return (acronym == null && getDcp() != null) ? (acronym = getDcp().getAcronym()) : acronym;
//    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    
    public IDegreeCurricularPlan getDcp() throws FenixFilterException, FenixServiceException {
        return (dcp == null) ? (dcp = (IDegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDcpId())) : dcp;
    }

    public void setDcp(IDegreeCurricularPlan dcp) {
        this.dcp = dcp;
    }

    public String createCurricularPlan() {
        Object[] args = { this.name, this.acronym };
        return changeDegreeCurricularPlan("CreateDegreeCurricularPlan", args, "degreeCurricularPlan.created", "error.creatingDegreeCurricularPlan");
    }
    
    public String editCurricularPlan() {
        Object[] args = { this.getDcpId(), this.name, this.acronym };
        return changeDegreeCurricularPlan("EditDegreeCurricularPlan", args, "degreeCurricularPlan.edited", "error.editingDegreeCurricularPlan");
    }
    
    private String changeDegreeCurricularPlan(String serviceName, Object[] args, String successfulMsg, String errorMsg) {
        try {
            ServiceUtils.executeService(this.getUserView(), serviceName, args);
        } catch (FenixFilterException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(domainExceptionBundle.getString(e.getMessage()));
            return "curricularPlansManagement";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString("error.deletingDegree"));
            return "curricularPlansManagement";
        }
        
        this.addInfoMessage(scouncilBundle.getString(successfulMsg));
        return "curricularPlansManagement";
    }

}
