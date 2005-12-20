package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.curriculum.GradeType;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ScientificCouncilDegreeManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("ServidorApresentacao/ScientificCouncilResources");
    private final ResourceBundle enumerationBundle = getResourceBundle("ServidorApresentacao/EnumerationResources");
    private final ResourceBundle domainExceptionbundle = getResourceBundle("ServidorApresentacao/EnumerationResources");
    private final String NO_SELECTION = "noSelection";  
    
    private Integer degreeId;
    private IDegree degree;
    private String name;
    private String nameEn;
    private String acronym;
    private String bolonhaDegreeType;
    private String gradeType;

    public Integer getDegreeId() {
        return (degreeId == null) ? (degreeId = getAndHoldIntegerParameter("degreeId")) : degreeId;
    }

    public void setDegreeId(Integer degreeId) {
        this.degreeId = degreeId;
    }
    
    public IDegree getDegree() throws FenixFilterException, FenixServiceException {
        return (degree == null) ? (degree = (IDegree) readDomainObject(Degree.class, getDegreeId())) : degree;
    }
    
    public String getName() throws FenixFilterException, FenixServiceException {
        return (name == null && getDegree() != null) ? (name = getDegree().getNome()) : name;
    }

    public void setName(String name) {
        this.name = name;
    }    

    public String getNameEn() throws FenixFilterException, FenixServiceException {
        return (nameEn == null && getDegree() != null) ? (nameEn = getDegree().getNameEn()) : nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAcronym() throws FenixFilterException, FenixServiceException {
        return (acronym == null && getDegree() != null) ? (acronym = getDegree().getAcronym()) : acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public String getBolonhaDegreeType() throws FenixFilterException, FenixServiceException {
        return (bolonhaDegreeType == null && getDegree() != null) ? (bolonhaDegreeType = getDegree().getBolonhaDegreeType().getName()) : bolonhaDegreeType;
    }

    public void setBolonhaDegreeType(String bolonhaDegreeType) {
        this.bolonhaDegreeType = bolonhaDegreeType;
    }

    public String getGradeType() throws FenixFilterException, FenixServiceException {
        return (gradeType == null && getDegree() != null) ? (gradeType = getDegree().getNome()) : gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public List<SelectItem> getBolonhaDegreeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(BolonhaDegreeType.DEGREE.name(), enumerationBundle.getString(BolonhaDegreeType.DEGREE.getName()) + " (3 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE.name(), enumerationBundle.getString(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE.getName())  + " (5 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.MASTER_DEGREE.name(), enumerationBundle.getString(BolonhaDegreeType.MASTER_DEGREE.getName())  + " (2 anos)"));
        
        return result;
    }

    public List<SelectItem> getGradeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(GradeType.GRADETWENTY.name(), enumerationBundle.getString(GradeType.GRADETWENTY.name())));
        result.add(new SelectItem(GradeType.GRADEFIVE.name(), enumerationBundle.getString(GradeType.GRADEFIVE.name())));
        
        return result;
    }
    
    public String createDegree() {
        Object[] args = { this.name, this.nameEn, this.acronym, BolonhaDegreeType.valueOf(this.bolonhaDegreeType), GradeType.GRADETWENTY }; //GradeType.valueOf(this.gradeType) };
        return changeDegree("CreateDegree", args, "degree.created", "error.creatingDegree");
    }
    
    public String editDegree() {
        Object[] args = { this.getDegreeId(), this.name, this.nameEn, this.acronym, BolonhaDegreeType.valueOf(this.bolonhaDegreeType), GradeType.GRADETWENTY }; //GradeType.valueOf(this.gradeType) };
        return changeDegree("EditDegree", args, "degree.edited", "error.editingDegree");
    }
    
    private String changeDegree(String serviceName, Object[] args, String successfulMsg, String errorMsg) {
        if (this.bolonhaDegreeType.equals(this.NO_SELECTION)) {// || this.gradeType.equals(this.NO_SELECTION)) {
            this.setErrorMessage(scouncilBundle.getString("choose.request"));
            return "";
        }
        
        try {
            ServiceUtils.executeService(this.getUserView(), serviceName, args);
        } catch (FenixFilterException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (Exception e) {
            this.addErrorMessage(scouncilBundle.getString(errorMsg));
            return "curricularPlansManagement";
        } 
        
        this.addInfoMessage(scouncilBundle.getString(successfulMsg));
        return "curricularPlansManagement";
    }
    
    public String deleteDegree() {
        Object[] args = { this.getDegreeId() };
        try {
            ServiceUtils.executeService(this.getUserView(), "DeleteDegree", args);
        } catch (FenixFilterException e) {
            this.addErrorMessage(scouncilBundle.getString("error.notAuthorized"));
            return "curricularPlansManagement";
        } catch (DomainException e) {
            this.addErrorMessage(scouncilBundle.getString(e.getMessage()));
            return "curricularPlansManagement";
        } catch (Exception e) {
            this.addErrorMessage(domainExceptionbundle.getString("error.deletingDegree"));
            return "curricularPlansManagement";
        } 
        
        this.addInfoMessage(scouncilBundle.getString("degree.deleted"));
        return "curricularPlansManagement";
    }

}
