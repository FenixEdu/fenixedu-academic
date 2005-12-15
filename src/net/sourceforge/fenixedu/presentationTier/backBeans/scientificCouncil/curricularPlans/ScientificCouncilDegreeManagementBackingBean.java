/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.scientificCouncil.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.MarkType;

public class ScientificCouncilDegreeManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle scouncilBundle = getResourceBundle("ServidorApresentacao/ScientificCouncilResources");
    private final ResourceBundle bundleEnumeration = getResourceBundle("ServidorApresentacao/EnumerationResources");
    private final String NO_SELECTION = "noSelection";  
    
    private String namePt;
    private String nameEn;
    private String code;
    private String bolonhaDegreeType;
    private String gradeType;

    public String getNamePt() {
        return namePt;
    }

    public void setNamePt(String namePt) {
        this.namePt = namePt;
    }    

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBolonhaDegreeType() {
        return bolonhaDegreeType;
    }

    public void setBolonhaDegreeType(String bolonhaDegreeType) {
        this.bolonhaDegreeType = bolonhaDegreeType;
    }

    public String getGradeType() {
        return gradeType;
    }

    public void setGradeType(String gradeType) {
        this.gradeType = gradeType;
    }

    public String getPersonDepartmentName() {
        IEmployee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getDepartmentWorkingPlace() != null) ? employee
                .getDepartmentWorkingPlace().getRealName() : "";
    }
    
    public List<SelectItem> getBolonhaDegreeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(BolonhaDegreeType.DEGREE, bundleEnumeration.getString(BolonhaDegreeType.DEGREE.getName()) + " (3 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE, bundleEnumeration.getString(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE.getName())  + " (5 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.MASTER_DEGREE, bundleEnumeration.getString(BolonhaDegreeType.MASTER_DEGREE.getName())  + " (2 anos)"));
        
        return result;
    }

    public List<SelectItem> getGradeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, scouncilBundle.getString("choose")));
        result.add(new SelectItem(MarkType.TYPE20, "1-20 valores"));
        result.add(new SelectItem(MarkType.TYPE5, "1-5 valores"));
        
        return result;
    }
    
    public String createDegree() {
        if (this.bolonhaDegreeType instanceof String || this.gradeType instanceof String) {
            this.setErrorMessage("choose.request");
            return "";
        }
        
        Object[] args = { this.namePt, this.nameEn, this.code, this.bolonhaDegreeType, this.gradeType };
        try {
            ServiceUtils.executeService(this.getUserView(), "CreateDegree", args);
        } catch (FenixFilterException e) {
            this.setErrorMessage("error.creatingDegree");
        } catch (FenixServiceException e) {
            this.setErrorMessage("error.creatingDegree");
        }
        
        return "curricularPlansManagement";
    }
    
    public String editDegree() {
        return "curricularPlansManagement";
    }

}
