/*
 * Created on Feb 3, 2006
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.faces.component.UISelectItems;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.PrecedenceRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularRulesManagementBackingBean extends FenixBackingBean {
    private final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    private final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    private final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    private final String NO_SELECTION = "no_selection";
    
    private Integer degreeModuleID = null;
    private DegreeModule degreeModule = null;

    private DegreeCurricularPlan degreeCurricularPlan = null;

    private CurricularRule curricularRule = null;

    private UISelectItems curricularRuleTypeItems;

    private UISelectItems degreeModuleItems;

    private UISelectItems courseGroupItems;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }

    public Integer getDegreeModuleID() throws FenixFilterException, FenixServiceException {
        if (degreeModuleID == null) {
            degreeModuleID = getAndHoldIntegerParameter("degreeModuleID"); 
            if (degreeModuleID == null && getCurricularRule() != null) {
                degreeModuleID = getCurricularRule().getDegreeModuleToApplyRule().getIdInternal();
            }
        }
        return degreeModuleID;
    }

    public DegreeModule getDegreeModule() throws FenixFilterException, FenixServiceException {
        return (degreeModule == null) ? (degreeModule = (DegreeModule) readDomainObject(
                DegreeModule.class, getDegreeModuleID())) : degreeModule;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() throws FenixFilterException,
            FenixServiceException {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = (DegreeCurricularPlan) readDomainObject(
                DegreeCurricularPlan.class, getDegreeCurricularPlanID()))
                : degreeCurricularPlan;
    }

    public String getSelectedCurricularRuleType() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedCurricularRuleType") == null && getCurricularRule() != null) {
            setSelectedCurricularRuleType(getCurricularRule().getCurricularRuleType().getName());            
        }
        return (String) getViewState().getAttribute("selectedCurricularRuleType");
    }

    public void setSelectedCurricularRuleType(String ruleType) {
        getViewState().setAttribute("selectedCurricularRuleType", ruleType);
    }

    // TODO: check this method
    private List<SelectItem> getRuleTypes() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(NO_SELECTION, enumerationResources.getString("dropDown.Default")));
        for (final CurricularRuleType curricularRuleType : CurricularRuleType.values()) {
            if (getDegreeModule() instanceof CourseGroup) {
                if (curricularRuleType != CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE
                        && curricularRuleType != CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                            .getString(curricularRuleType.getName())));
                }
            } else if (getDegreeModule() instanceof CurricularCourse) {
                if (curricularRuleType != CurricularRuleType.CREDITS_LIMIT
                        && curricularRuleType != CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                            .getString(curricularRuleType.getName())));
                }
            }
        }
        return result;
    }

    public List<String> getRulesLabels() throws FenixFilterException, FenixServiceException {
        List<String> resultLabels = new ArrayList<String>();
        for (CurricularRule curricularRule : getDegreeModule().getCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }

        return resultLabels;
    }

    public String getRuleLabel() throws FenixFilterException, FenixServiceException {
        return CurricularRuleLabelFormatter.getLabel(getCurricularRule());
    }

    public UISelectItems getCurricularRuleTypeItems() throws FenixFilterException, FenixServiceException {
        if (curricularRuleTypeItems == null) {
            curricularRuleTypeItems = new UISelectItems();
            curricularRuleTypeItems.setValue(getRuleTypes());
        }
        return curricularRuleTypeItems;
    }

    public void setCurricularRuleTypeItems(UISelectItems ruleTypeItems) {
        this.curricularRuleTypeItems = ruleTypeItems;
    }

    public void onChangeCurricularRuleTypeDropDown(ValueChangeEvent event) throws FenixFilterException,
            FenixServiceException {
        getDegreeModuleItems().setValue(readDegreeModules((String) event.getNewValue()));
        getCourseGroupItems().setValue(readCourseGroups((String) event.getNewValue()));
    }

    public Integer getSelectedPrecendenceDegreeModuleID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedPrecendenceDegreeModuleID") == null && getCurricularRule() != null) {
            if (getCurricularRule() instanceof PrecedenceRule) {
                setSelectedPrecendenceDegreeModuleID(((PrecedenceRule) getCurricularRule()).getPrecedenceDegreeModule().getIdInternal());                
            } 
        }
        return (Integer) getViewState().getAttribute("selectedPrecendenceDegreeModuleID");        
    }

    public void setSelectedPrecendenceDegreeModuleID(Integer selectedPrecendenceDegreeModuleID) {
        getViewState().setAttribute("selectedPrecendenceDegreeModuleID", selectedPrecendenceDegreeModuleID);
    }
   
    public Integer getSelectedContextCourseGroupID() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedContextCourseGroupID") == null && getCurricularRule() != null) {
            if (getCurricularRule().getContextCourseGroup() != null) {
                setSelectedContextCourseGroupID(getCurricularRule().getContextCourseGroup().getIdInternal());
            }
        }
        return (Integer) getViewState().getAttribute("selectedContextCourseGroupID");
    }

    public void setSelectedContextCourseGroupID(Integer selectedContextCourseGroupID) {
        getViewState().setAttribute("selectedContextCourseGroupID", selectedContextCourseGroupID);
    }
    
    public Integer getMinimumLimit() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("minimumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT)) {
                setMinimumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMinimum());               
            } else {
                setMinimumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("minimumLimit");
    }

    public void setMinimumLimit(Integer minimumLimit) {
        getViewState().setAttribute("minimumLimit", minimumLimit);
    }
    
    public Integer getMaximumLimit() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("maximumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT)) {
                setMaximumLimit(((DegreeModulesSelectionLimit) getCurricularRule()).getMaximum());               
            } else {
                setMaximumLimit(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("maximumLimit");
    }

    public void setMaximumLimit(Integer maximumLimit) {
        getViewState().setAttribute("maximumLimit", maximumLimit);
    }
   
    public Double getMinimumCredits() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("minimumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.CREDITS_LIMIT)) {
                setMinimumCredits(((CreditsLimit) getCurricularRule()).getMinimum());               
            } else {
                setMinimumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("minimumCredits");
    }

    public void setMinimumCredits(Double minimumCredits) {
        getViewState().setAttribute("minimumCredits", minimumCredits);
    }
    
    public Double getMaximumCredits() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("maximumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.CREDITS_LIMIT)) {
                setMaximumCredits(((CreditsLimit) getCurricularRule()).getMaximum());               
            } else {
                setMaximumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("maximumCredits");
    }

    public void setMaximumCredits(Double maximumCredits) {
        getViewState().setAttribute("maximumCredits", maximumCredits);
    }
    
    public String getSelectedSemester() throws FenixFilterException, FenixServiceException {
        if (getViewState().getAttribute("selectedSemester") == null) {
            if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE)) {
                setSelectedSemester(((RestrictionDoneDegreeModule) getCurricularRule()).getCurricularPeriodOrder().toString());
            } else if (getCurricularRule() != null && getCurricularRule().getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE)) {
                    setSelectedSemester(((RestrictionEnroledDegreeModule) getCurricularRule()).getCurricularPeriodOrder().toString());
            } else {
                setSelectedSemester("0");
            }
        }
        return (String) getViewState().getAttribute("selectedSemester");
    }

    public void setSelectedSemester(String selectedSemester) {
        getViewState().setAttribute("selectedSemester", selectedSemester);
    }
    
    public Integer getCurricularRuleID() {
        return getAndHoldIntegerParameter("curricularRuleID");
    }
    
    public CurricularRule getCurricularRule() throws FenixFilterException, FenixServiceException {
        return (curricularRule == null) ? (curricularRule = (CurricularRule) readDomainObject(CurricularRule.class, getCurricularRuleID())) : curricularRule;
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

    // TODO: check this method
    private Object readCourseGroups(String selectedCurricularRuleType) throws FenixFilterException,
            FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final SortedSet<CourseGroup> courseGroups = new TreeSet<CourseGroup>(new BeanComparator("name"));
        result.add(new SelectItem(Integer.valueOf(0), bolonhaResources.getString("all")));
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            for (final Context context : getDegreeModule().getDegreeModuleContexts()) {
                if (!context.getCourseGroup().isRoot()) {
                    courseGroups.add(context.getCourseGroup());
                }
            }
            for (final CourseGroup courseGroup : courseGroups) {
                result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName()));
            }
        }
        return result;
    }

    // TODO: check this method
    private List<SelectItem> readDegreeModules(String selectedCurricularRuleType)
            throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem(Integer.valueOf(0), bolonhaResources.getString("choose")));
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION)) {
            if (selectedCurricularRuleType.equals(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE.name())
                    || selectedCurricularRuleType.equals(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE.name())) {
                getCurricularCourses(result);
            } else if (selectedCurricularRuleType.equals(CurricularRuleType.CREDITS_LIMIT.name())
                    || selectedCurricularRuleType.equals(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT.name())) {
                getCourseGroups(result);
            }
        }
        return result;
    }

    // TODO: check this method
    private void getCourseGroups(final List<SelectItem> result) throws FenixFilterException,
            FenixServiceException {
        final List<CourseGroup> courseGroups = (List<CourseGroup>) getDegreeCurricularPlan()
                .getDcpDegreeModules(CourseGroup.class);
        Collections.sort(result, new BeanComparator("name"));
        for (final CourseGroup courseGroup : courseGroups) {
            if (courseGroup != getDegreeModule()) {
                result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName()));
            }
        }
    }

    // TODO: check this method
    private void getCurricularCourses(final List<SelectItem> result) throws FenixFilterException,
            FenixServiceException {
        final List<CurricularCourse> curricularCourses = (List<CurricularCourse>) getDegreeCurricularPlan()
                .getDcpDegreeModules(CurricularCourse.class);
        Collections.sort(result, new BeanComparator("name"));
        for (final CurricularCourse curricularCourse : curricularCourses) {
            if (curricularCourse != getDegreeModule()) {
                result.add(new SelectItem(curricularCourse.getIdInternal(), curricularCourse.getName()));
            }
        }
    }

    public String createCurricularRule() {
        try {
            checkSelectedAttributes();
            final Object[] args = { getDegreeModuleID(), CurricularRuleType.valueOf(getSelectedCurricularRuleType()), buildCurricularRuleParametersDTO() };
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
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String editCurricularRule() {
        try {
            final Object[] args = { getCurricularRuleID(), buildCurricularRuleParametersDTO()};
            ServiceUtils.executeService(getUserView(), "EditCurricularRule", args);
            return "setCurricularRules";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    public String deleteCurricularRule() {
        try {
            final Object[] args = { getCurricularRuleID() };
            ServiceUtils.executeService(getUserView(), "DeleteCurricularRule", args);
            addInfoMessage(bolonhaResources.getString("curricularRule.deleted"));
            return "setCurricularRules";
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }
    
    private void checkSelectedAttributes() throws FenixActionException, FenixFilterException, FenixServiceException {
        if (getSelectedCurricularRuleType() == null || getSelectedCurricularRuleType().equals(NO_SELECTION)) {
            throw new FenixActionException("must.select.curricular.rule.type");
        }        
    }
    
    private CurricularRuleParametersDTO buildCurricularRuleParametersDTO() throws FenixFilterException, FenixServiceException, NumberFormatException {
        final CurricularRuleParametersDTO parametersDTO = new CurricularRuleParametersDTO();
        parametersDTO.setPrecedenceDegreeModuleID(getSelectedPrecendenceDegreeModuleID());
        parametersDTO.setContextCourseGroupID((getSelectedContextCourseGroupID() != null && getSelectedContextCourseGroupID().equals(0)) ? null : getSelectedContextCourseGroupID() );
        parametersDTO.setCurricularPeriodInfoDTO(new CurricularPeriodInfoDTO(Integer.valueOf(getSelectedSemester()), CurricularPeriodType.SEMESTER));            
        parametersDTO.setMinimumCredits(getMinimumCredits());
        parametersDTO.setMaximumCredits(getMaximumCredits());
        parametersDTO.setMinimumLimit(getMinimumLimit());
        parametersDTO.setMaximumLimit(getMaximumLimit());
        return parametersDTO;
    }
}
