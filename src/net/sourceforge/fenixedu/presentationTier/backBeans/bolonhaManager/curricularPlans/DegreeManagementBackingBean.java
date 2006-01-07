/*
 * Created on Dec 7, 2005
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularPlans;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.MarkType;

public class DegreeManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bundleBolonha = getResourceBundle("ServidorApresentacao/BolonhaManagerResources");
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

    public void setGradeType(String markType) {
        this.gradeType = markType;
    }

    public String getPersonDepartmentName() {
        Employee employee = getUserView().getPerson().getEmployee();
        return (employee != null && employee.getCurrentDepartmentWorkingPlace() != null) ? employee
                .getCurrentDepartmentWorkingPlace().getRealName() : "";
    }
    
    public List<SelectItem> getBolonhaDegreeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, bundleBolonha.getString("choose")));
        result.add(new SelectItem(BolonhaDegreeType.DEGREE, bundleEnumeration.getString(BolonhaDegreeType.DEGREE.getName()) + " (3 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE, bundleEnumeration.getString(BolonhaDegreeType.INTEGRATED_MASTER_DEGREE.getName())  + " (5 anos)"));
        result.add(new SelectItem(BolonhaDegreeType.MASTER_DEGREE, bundleEnumeration.getString(BolonhaDegreeType.MASTER_DEGREE.getName())  + " (2 anos)"));
        
        return result;
    }

    public List<SelectItem> getGradeTypes() {
        List<SelectItem> result = new ArrayList<SelectItem>();
        
        result.add(new SelectItem(this.NO_SELECTION, bundleBolonha.getString("choose")));
        result.add(new SelectItem("" + MarkType.TYPE5, "1-5"));
        result.add(new SelectItem("" + MarkType.TYPE20, "1-20"));
        
        return result;
    }
    
    public String createDegree() {
        return "curricularPlansManagement";
    }
    
    public String editDegree() {
        return "curricularPlansManagement";
    }

}
