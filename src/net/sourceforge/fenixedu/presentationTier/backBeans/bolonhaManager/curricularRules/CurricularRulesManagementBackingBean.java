/*
 * Created on Feb 3, 2006
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class CurricularRulesManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    //private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final String NO_SELECTION = "no_selection";
    
    private DegreeModule degreeModule = null;
    private DegreeCurricularPlan degreeCurricularPlan = null;
    
    private UISelectItems curricularRuleTypeItems;
    private UISelectItems degreeModuleItems;
    private UISelectItems courseGroupItems;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getDegreeModuleID() {
        return getAndHoldIntegerParameter("degreeModuleID");
    }

    public DegreeModule getDegreeModule() throws FenixFilterException, FenixServiceException {
        return (degreeModule == null) ? (degreeModule = (DegreeModule) readDomainObject(DegreeModule.class, getDegreeModuleID())) : degreeModule;
    }
    
    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException, FenixServiceException {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = (DegreeCurricularPlan) readDomainObject(DegreeCurricularPlan.class, getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public String getSelectedCurricularRuleType() {
        return (String) getViewState().getAttribute("selectedCurricularRuleType");
    }

    public void setSelectedCurricularRuleType(String ruleType) {
        getViewState().setAttribute("selectedCurricularRuleType", ruleType);
    }
    
    private List<SelectItem> getRuleTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(NO_SELECTION, enumerationResources.getString("dropDown.Default")));
        for (final CurricularRuleType curricularRuleType : CurricularRuleType.values()) {
            result.add(new SelectItem(curricularRuleType.getName(), enumerationResources.getString(curricularRuleType.getName())));
        }
        return result;
    }

    public UISelectItems getCurricularRuleTypeItems() {
        if (curricularRuleTypeItems == null) {
            curricularRuleTypeItems = new UISelectItems();
            curricularRuleTypeItems.setValue(getRuleTypes());
        }
        return curricularRuleTypeItems;
    }
    
    public void setCurricularRuleTypeItems(UISelectItems ruleTypeItems) {
        this.curricularRuleTypeItems = ruleTypeItems;
    }
    
    public void onChangeCurricularRuleTypeDropDown(ValueChangeEvent event) throws FenixFilterException, FenixServiceException {
        getDegreeModuleItems().setValue(readDegreeModules((String) event.getNewValue()));
        getCourseGroupItems().setValue(readCourseGroups((String) event.getNewValue()));
    }

    public Integer getSelectedPrecendenceDegreeModuleID() {
        return (Integer) getViewState().getAttribute("selectedPrecendenceDegreeModuleID");        
    }

    public void setSelectedPrecendenceDegreeModuleID(Integer selectedPrecendenceDegreeModuleID) {
        getViewState().setAttribute("selectedPrecendenceDegreeModuleID", selectedPrecendenceDegreeModuleID);
    }
   
    public Integer getSelectedContextCourseGroupID() {
        return (Integer) getViewState().getAttribute("selectedContextCourseGroupID");        
    }

    public void setSelectedContextCourseGroupID(Integer selectedContextCourseGroupID) {
        getViewState().setAttribute("selectedContextCourseGroupID", selectedContextCourseGroupID);
    }
   
    public String getMinimumValue() {
        return (getViewState().getAttribute("minimumValue") != null) ? (String) getViewState().getAttribute("minimumValue") : "0";
    }

    public void setMinimumValue(String minimumValue) {
        getViewState().setAttribute("minimumValue", minimumValue);
    }
    
    public String getMaximumValue() {
        return (getViewState().getAttribute("maximumValue") != null) ? (String) getViewState().getAttribute("maximumValue") : "0";
    }

    public void setMaximumValue(String maximumValue) {
        getViewState().setAttribute("maximumValue", maximumValue);
    }
    
    public String getSelectedSemester() {
        return (String) getViewState().getAttribute("selectedSemester");
    }

    public void setSelectedSemester(String selectedSemester) {
        getViewState().setAttribute("selectedSemester", selectedSemester);
    }
    
    public UISelectItems getDegreeModuleItems() throws FenixFilterException, FenixServiceException {
        if (degreeModuleItems == null) {
            degreeModuleItems = new UISelectItems();
            degreeModuleItems.setValue(readDegreeModules(getSelectedCurricularRuleType()));
        }
        return degreeModuleItems;
    }

    public void setDegreeModuleItems(UISelectItems degreeModuleItems) {
        this.degreeModuleItems = degreeModuleItems;
    }
    
    public UISelectItems getCourseGroupItems() throws FenixFilterException, FenixServiceException {
        if (courseGroupItems == null) {
            courseGroupItems = new UISelectItems();
            courseGroupItems.setValue(readCourseGroups(getSelectedCurricularRuleType()));
        }
        return courseGroupItems;
    }
    
    public void setCourseGroupItems(UISelectItems courseGroupItems) {
        this.courseGroupItems = courseGroupItems;
    }
    
    private List<SelectItem> readDegreeModules(String selectedCurricularRuleType) throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), bolonhaResources.getString("choose")));
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            getCurricularCourses(result);
            getCourseGroups(result);
        }
        Collections.sort(result, new BeanComparator("label"));
        return result;
    }
 
    private Object readCourseGroups(String selectedCurricularRuleType) throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), bolonhaResources.getString("any.context")));
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            getCourseGroups(result);
        }
        Collections.sort(result, new BeanComparator("label"));
        return result;
    }
    
    private void getCourseGroups(final List<SelectItem> result) throws FenixFilterException, FenixServiceException {
        for (final CourseGroup courseGroup : getDegreeCurricularPlan().getDcpCourseGroups()) {
            result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName() + " (" + bolonhaResources.getString("courseGroup") + ")"));
        }
    }

    private void getCurricularCourses(final List<SelectItem> result) throws FenixFilterException, FenixServiceException {
        for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getDcpCurricularCourses()) {
            result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName() + " (" + bolonhaResources.getString("curricularCourse") + ")"));
        }
    }
    
    public String createCurricularRule() {
        try {
            checkSelectedAttributes();
            final CurricularRuleParametersDTO parametersDTO = new CurricularRuleParametersDTO();
            parametersDTO.setPrecedenceDegreeModuleID(getSelectedPrecendenceDegreeModuleID());
            parametersDTO.setContextCourseGroupID((getSelectedContextCourseGroupID() != null && getSelectedContextCourseGroupID().equals(0)) ? null : getSelectedContextCourseGroupID() );
            parametersDTO.setCurricularPeriodInfoDTO(new CurricularPeriodInfoDTO(Integer.valueOf(getSelectedSemester()), CurricularPeriodType.SEMESTER));            
            parametersDTO.setMinimumInt(Integer.valueOf(getMinimumValue()));
            parametersDTO.setMaximumInt(Integer.valueOf(getMaximumValue()));
            parametersDTO.setMinimumDouble(Double.valueOf(getMinimumValue()));
            parametersDTO.setMaximumDouble(Double.valueOf(getMaximumValue()));
            final Object[] args = {getDegreeModuleID(), CurricularRuleType.valueOf(getSelectedCurricularRuleType()), parametersDTO };
            ServiceUtils.executeService(getUserView(), "CreateCurricularRule", args);
            return "setCurricularRules";
        } catch (FenixActionException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (NumberFormatException e) {
            addErrorMessage(bolonhaResources.getString("invalid.minimum.maximum.values"));
        }
        return "";
    }
    
    private void checkSelectedAttributes() throws FenixActionException {
        if (getSelectedCurricularRuleType() == null || getSelectedCurricularRuleType().equals(NO_SELECTION)) {
            throw new FenixActionException("must.select.curricular.rule.type");
        }        
    }
}
