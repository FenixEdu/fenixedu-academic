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

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.CurricularPeriodInfoDTO;
import net.sourceforge.fenixedu.dataTransferObject.bolonhaManager.CurricularRuleParametersDTO;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.AnyCurricularCourse;
import net.sourceforge.fenixedu.domain.curricularRules.CreditsLimit;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.DegreeModulesSelectionLimit;
import net.sourceforge.fenixedu.domain.curricularRules.Exclusiveness;
import net.sourceforge.fenixedu.domain.curricularRules.MinimumNumberOfCreditsToEnrol;
import net.sourceforge.fenixedu.domain.curricularRules.PrecedenceRule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionBetweenDegreeModules;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.apache.commons.beanutils.BeanComparator;

public class CurricularRulesManagementBackingBean extends FenixBackingBean {
    protected final ResourceBundle bolonhaResources = getResourceBundle("resources/BolonhaManagerResources");
    protected final ResourceBundle enumerationResources = getResourceBundle("resources/EnumerationResources");
    protected final ResourceBundle domainResources = getResourceBundle("resources/DomainExceptionResources");
    protected final String NO_SELECTION_STRING = "";
    protected final Integer NO_SELECTION_INTEGER = Integer.valueOf(0);
    
    private Integer degreeModuleID = null;
    private DegreeModule degreeModule = null;

    private DegreeCurricularPlan degreeCurricularPlan = null;
    private CurricularRule curricularRule = null;

    private UISelectItems curricularRuleTypeItems;
    private UISelectItems degreeModuleItems;
    private UISelectItems courseGroupItems;
    private UISelectItems degreeItems;
    private UISelectItems departmentUnitItems;
    private UISelectItems beginExecutionPeriodItemsForRule;
    private UISelectItems endExecutionPeriodItemsForRule;

    public Integer getDegreeCurricularPlanID() {
        return getAndHoldIntegerParameter("degreeCurricularPlanID");
    }
    
    public Integer getExecutionYearID() {
        return getAndHoldIntegerParameter("executionYearID");
    }
    
    public String getOrganizeBy() {
        return getAndHoldStringParameter("organizeBy");
    }
    
    public String getShowRules() {
        return getAndHoldStringParameter("showRules");
    }
    
    public String getHideCourses() {
        return getAndHoldStringParameter("hideCourses");
    }
    
    public String getAction() {
        return getAndHoldStringParameter("action");
    }
    
    public String getType() {
        return getAndHoldStringParameter("type");
    }

    public Integer getDegreeModuleID() {
        if (degreeModuleID == null) {
            degreeModuleID = getAndHoldIntegerParameter("degreeModuleID"); 
            if (degreeModuleID == null && getCurricularRule() != null) {
                degreeModuleID = getCurricularRule().getDegreeModuleToApplyRule().getIdInternal();
            }
        }
        return degreeModuleID;
    }

    public DegreeModule getDegreeModule() {
        return (degreeModule == null) ? (degreeModule = rootDomainObject.readDegreeModuleByOID(getDegreeModuleID())) : degreeModule;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return (degreeCurricularPlan == null) ? (degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(getDegreeCurricularPlanID())) : degreeCurricularPlan;
    }

    public String getSelectedCurricularRuleType() {
        if (getViewState().getAttribute("selectedCurricularRuleType") == null && getCurricularRule() != null) {
            if (getCurricularRule().getCurricularRuleType() != null) {
                setSelectedCurricularRuleType(getCurricularRule().getCurricularRuleType().getName());
            }
        }
        return (String) getViewState().getAttribute("selectedCurricularRuleType");
    }

    public void setSelectedCurricularRuleType(String selectedCurricularRuleType) {
        getViewState().setAttribute("selectedCurricularRuleType", selectedCurricularRuleType);
    }

    private List<SelectItem> getRuleTypes() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final CurricularRuleType curricularRuleType : CurricularRuleType.values()) {
            switch (curricularRuleType) {

            case CREDITS_LIMIT:
                if (getDegreeModule().isLeaf()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
                    if (curricularCourse.getType() == CurricularCourseType.OPTIONAL_COURSE) {
                        result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                                .getString(curricularRuleType.getName())));
                        break;
                    }
                }

            case DEGREE_MODULES_SELECTION_LIMIT:
            case PRECEDENCY_BETWEEN_DEGREE_MODULES:
                if (!getDegreeModule().isLeaf()) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                            .getString(curricularRuleType.getName())));
                }
                break;

            case PRECEDENCY_APPROVED_DEGREE_MODULE:
            case PRECEDENCY_ENROLED_DEGREE_MODULE:
            case ENROLMENT_TO_BE_APPROVED_BY_COORDINATOR:
                if (getDegreeModule().isLeaf()) {
                    result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                            .getString(curricularRuleType.getName())));
                }
                break;

            case EXCLUSIVENESS:
                result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                        .getString(curricularRuleType.getName())));
                break;

            case ANY_CURRICULAR_COURSE:
                if (getDegreeModule().isLeaf()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) getDegreeModule();
                    if (curricularCourse.getType() == CurricularCourseType.OPTIONAL_COURSE) {
                        result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                                .getString(curricularRuleType.getName())));
                    }
                }
                break;
                
            case MINIMUM_NUMBER_OF_CREDITS_TO_ENROL:
                result.add(new SelectItem(curricularRuleType.getName(), enumerationResources
                        .getString(curricularRuleType.getName())));
                break;

            default:
                break;
            }
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(NO_SELECTION_STRING, bolonhaResources.getString("choose")));
        return result;
    }

    public List<String> getRulesLabels() {
        final List<String> resultLabels = new ArrayList<String>();
        for (CurricularRule curricularRule : getDegreeModule().getCurricularRules()) {
            resultLabels.add(CurricularRuleLabelFormatter.getLabel(curricularRule));
        }
        return resultLabels;
    }

    public String getRuleLabel() {
        return CurricularRuleLabelFormatter.getLabel(getCurricularRule());
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

    public void onChangeCurricularRuleTypeDropDown(ValueChangeEvent event) {
        getDegreeModuleItems().setValue(readDegreeModules((String) event.getNewValue()));
        getCourseGroupItems().setValue(readParentCourseGroups((String) event.getNewValue()));
        getDegreeItems().setValue(readBolonhaDegrees((String) event.getNewValue(), getSelectedDegreeType()));
        getDepartmentUnitItems().setValue(readDepartmentUnits((String) event.getNewValue()));
    }
    
    public void onChangeDegreeTypeDropDown(ValueChangeEvent event) {
        getDegreeItems().setValue(readBolonhaDegrees(getSelectedCurricularRuleType(), (String) event.getNewValue()));
    }

    public Integer getSelectedDegreeModuleID() {
        if (getViewState().getAttribute("selectedDegreeModuleID") == null && getCurricularRule() != null) {
            if (getCurricularRule() instanceof PrecedenceRule) {
                setSelectedDegreeModuleID(((PrecedenceRule) getCurricularRule()).getPrecedenceDegreeModule().getIdInternal());                
            } else if (getCurricularRule() instanceof Exclusiveness) {
                setSelectedDegreeModuleID(((Exclusiveness) getCurricularRule()).getExclusiveDegreeModule().getIdInternal());
            }
        }
        return (Integer) getViewState().getAttribute("selectedDegreeModuleID");
    }

    public void setSelectedDegreeModuleID(Integer selectedDegreeModuleID) {
        getViewState().setAttribute("selectedDegreeModuleID", selectedDegreeModuleID);
    }
   
    public Integer getSelectedContextCourseGroupID() {
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
    
    public Integer getMinimumLimit() {
        if (getViewState().getAttribute("minimumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
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
    
    public Integer getMaximumLimit() {
        if (getViewState().getAttribute("maximumLimit") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof DegreeModulesSelectionLimit) {
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
   
    public Double getMinimumCredits() {
        if (getViewState().getAttribute("minimumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
                setMinimumCredits(((CreditsLimit) getCurricularRule()).getMinimum());               
            } else if (getCurricularRule() != null && getCurricularRule() instanceof RestrictionBetweenDegreeModules) {
                setMinimumCredits(((RestrictionBetweenDegreeModules) getCurricularRule()).getMinimum());
            } else if (getCurricularRule() != null && getCurricularRule() instanceof MinimumNumberOfCreditsToEnrol) {
                setMinimumCredits(((MinimumNumberOfCreditsToEnrol) getCurricularRule()).getMinimum());
            } else {
                setMinimumCredits(Double.valueOf(0));
            }
        }
        return (Double) getViewState().getAttribute("minimumCredits");
    }

    public void setMinimumCredits(Double minimumCredits) {
        getViewState().setAttribute("minimumCredits", minimumCredits);
    }
    
    public Double getMaximumCredits() {
        if (getViewState().getAttribute("maximumCredits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof CreditsLimit) {
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
    
    public String getSelectedSemester() {
        if (getViewState().getAttribute("selectedSemester") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof PrecedenceRule) {
                final PrecedenceRule precedenceRule = (PrecedenceRule) getCurricularRule();
                setSelectedSemester(precedenceRule.getCurricularPeriodOrder() != null ? precedenceRule
                        .getCurricularPeriodOrder().toString() : "0");
            } else if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                setSelectedSemester(((AnyCurricularCourse) getCurricularRule()).getCurricularPeriodOrder().toString());
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
    
    public CurricularRule getCurricularRule() {
        return (curricularRule == null) ? (curricularRule = rootDomainObject.readCurricularRuleByOID(getCurricularRuleID())) : curricularRule;
    }
    
    public UISelectItems getDegreeModuleItems() {
        if (degreeModuleItems == null) {
            degreeModuleItems = new UISelectItems();
            degreeModuleItems.setValue(readDegreeModules(getSelectedCurricularRuleType()));
        }
        return degreeModuleItems;
    }

    public void setDegreeModuleItems(UISelectItems degreeModuleItems) {
        this.degreeModuleItems = degreeModuleItems;
    }

    public UISelectItems getCourseGroupItems() {
        if (courseGroupItems == null) {
            courseGroupItems = new UISelectItems();
            courseGroupItems.setValue(readParentCourseGroups(getSelectedCurricularRuleType()));
        }
        return courseGroupItems;
    }

    public void setCourseGroupItems(UISelectItems courseGroupItems) {
        this.courseGroupItems = courseGroupItems;
    }
    
    public Integer getMinimumYear() {
        if (getViewState().getAttribute("minimumYear") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getMinimumYear() != null) {
                    setMinimumYear(anyCurricularCourse.getMinimumYear());
                }
            }
        }
        return (Integer) getViewState().getAttribute("minimumYear");
    }
    
    public void setMinimumYear(Integer minimumYear) {
        if (minimumYear == null) {
            getViewState().removeAttribute("minimumYear");
        } else {
            getViewState().setAttribute("minimumYear", minimumYear);
        }
    }
    
    public Integer getMaximumYear() {
        if (getViewState().getAttribute("maximumYear") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getMaximumYear() != null) {
                    setMaximumYear(anyCurricularCourse.getMaximumYear());
                }
            }
        }
        return (Integer) getViewState().getAttribute("maximumYear");
    }
    
    public void setMaximumYear(Integer maximumYear) {
        if (maximumYear == null) {
            getViewState().removeAttribute("maximumYear");
        } else {
            getViewState().setAttribute("maximumYear", maximumYear);
        }
    }
    
    public Double getCredits() {
        if (getViewState().getAttribute("credits") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getCredits() != null) {
                    setCredits(anyCurricularCourse.getCredits());                    
                }
            }
        }
        return (Double) getViewState().getAttribute("credits");
    }
    
    public void setCredits(Double credits) {
        if (credits == null) {
          getViewState().removeAttribute("credits");  
        } else {
            getViewState().setAttribute("credits", credits);
        }
    }
    
    public String getSelectedDegreeType() {
        if (getViewState().getAttribute("selectedDegreeType") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                if (anyCurricularCourse.getBolonhaDegreeType() != null) {
                    setSelectedDegreeType(anyCurricularCourse.getBolonhaDegreeType().name());
                }
            }
        }
        return (String) getViewState().getAttribute("selectedDegreeType");
    }
    
    public void setSelectedDegreeType(String selectedDegreeType) {
        getViewState().setAttribute("selectedDegreeType", selectedDegreeType);
    }
    
    public Integer getSelectedDegreeID() {
        if (getViewState().getAttribute("selectedDegreeID") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                setSelectedDegreeID(anyCurricularCourse.getDegree() != null ? anyCurricularCourse
                        .getDegree().getIdInternal() : Integer.valueOf(0));
            } else {
                setSelectedDegreeID(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("selectedDegreeID");
    }
    
    public void setSelectedDegreeID(Integer selectedDegreeID) {
        getViewState().setAttribute("selectedDegreeID", selectedDegreeID);
    }
    
    public Integer getSelectedDepartmentUnitID() {
        if (getViewState().getAttribute("selectedDepartmentUnitID") == null) {
            if (getCurricularRule() != null && getCurricularRule() instanceof AnyCurricularCourse) {
                AnyCurricularCourse anyCurricularCourse = (AnyCurricularCourse) getCurricularRule();
                setSelectedDepartmentUnitID(anyCurricularCourse.getDepartmentUnit() != null ? anyCurricularCourse
                        .getDepartmentUnit().getIdInternal()
                        : Integer.valueOf(0));
            } else {
                setSelectedDepartmentUnitID(Integer.valueOf(0));
            }
        }
        return (Integer) getViewState().getAttribute("selectedDepartmentUnitID");
    }
    
    public void setSelectedDepartmentUnitID(Integer selectedDepartmentUnitID) {
        getViewState().setAttribute("selectedDepartmentUnitID", selectedDepartmentUnitID);
    }
    
    public UISelectItems getDegreeItems() {
        if (degreeItems == null) {
            degreeItems = new UISelectItems();
            degreeItems.setValue(readBolonhaDegrees(getSelectedCurricularRuleType(), getSelectedDegreeType()));
        }
        return degreeItems;
    }

    public void setDegreeItems(UISelectItems degreeItems) {
        this.degreeItems = degreeItems;
    }
    
    private List<SelectItem> readBolonhaDegrees(String selectedCurricularRuleType,
            String selectedDegreeType) {

        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null
                && selectedCurricularRuleType.equals(CurricularRuleType.ANY_CURRICULAR_COURSE.name())) {
            
            final List<Degree> allDegrees = rootDomainObject.getDegrees();
            final DegreeType bolonhaDegreeType = (selectedDegreeType == null || selectedDegreeType
                    .equals(NO_SELECTION_STRING)) ? null : DegreeType.valueOf(selectedDegreeType);
            for (final Degree degree : allDegrees) {
                if (degree.isBolonhaDegree()
                        && (bolonhaDegreeType == null || degree.getDegreeType() == bolonhaDegreeType)) {
                    result.add(new SelectItem(degree.getIdInternal(), "["
                            + enumerationResources.getString(degree.getDegreeType().name()) + "] "
                            + degree.getNome()));
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("any.one")));
        return result;
    }
    
    public UISelectItems getDepartmentUnitItems() {
        if (departmentUnitItems == null) {
            departmentUnitItems = new UISelectItems();
            departmentUnitItems.setValue(readDepartmentUnits(getSelectedCurricularRuleType()));
        }
        return departmentUnitItems;
    }

    public void setDepartmentUnitItems(UISelectItems departmentUnitItems) {
        this.departmentUnitItems = departmentUnitItems;
    }
    
    public Integer getBeginExecutionPeriodID() {
        if (getViewState().getAttribute("beginExecutionPeriodID") == null && getCurricularRule() != null) {
            setBeginExecutionPeriodID(getCurricularRule().getBegin().getIdInternal());
        }
        return (Integer) getViewState().getAttribute("beginExecutionPeriodID");
    }
    
    public void setBeginExecutionPeriodID(Integer beginExecutionPeriodID) {
        getViewState().setAttribute("beginExecutionPeriodID", beginExecutionPeriodID);
    }
    
    public Integer getEndExecutionPeriodID() {
        if (getViewState().getAttribute("endExecutionPeriodID") == null && getCurricularRule() != null) {
            setEndExecutionPeriodID((getCurricularRule().getEnd() == null) ? 
                    Integer.valueOf(0) : getCurricularRule().getEnd().getIdInternal());
        }
        return (Integer) getViewState().getAttribute("endExecutionPeriodID");
    }
    
    public void setEndExecutionPeriodID(Integer endExecutionPeriodID) {
        getViewState().setAttribute("endExecutionPeriodID", endExecutionPeriodID);
    }
    
    public UISelectItems getBeginExecutionPeriodItemsForRule() {
        if (beginExecutionPeriodItemsForRule == null) {
            beginExecutionPeriodItemsForRule = new UISelectItems();
            beginExecutionPeriodItemsForRule.setValue(readExecutionPeriodItems());
        }
        return beginExecutionPeriodItemsForRule;
    }

    public void setBeginExecutionPeriodItemsForRule(UISelectItems beginExecutionPeriodItemsForRule) {
        this.beginExecutionPeriodItemsForRule = beginExecutionPeriodItemsForRule;
    }
    
    public UISelectItems getEndExecutionPeriodItemsForRule() {
        if (endExecutionPeriodItemsForRule == null) {
            endExecutionPeriodItemsForRule = new UISelectItems();
            final List<SelectItem> values = new ArrayList<SelectItem>(readExecutionPeriodItems());
            values.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("opened")));
            endExecutionPeriodItemsForRule.setValue(values);
        }
        return endExecutionPeriodItemsForRule;
    }

    public void setEndExecutionPeriodItemsForRule(UISelectItems endExecutionPeriodItemsForRule) {
        this.endExecutionPeriodItemsForRule = endExecutionPeriodItemsForRule;
    }
    
    private List<SelectItem> executionPeriodItems;
    protected List<SelectItem> readExecutionPeriodItems() {
        if (executionPeriodItems != null) {
            return executionPeriodItems;
        }
        final List<SelectItem> result = new ArrayList<SelectItem>();
        final ExecutionPeriod currentExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
        final List<ExecutionPeriod> notClosedExecutionPeriods = ExecutionPeriod.readNotClosedExecutionPeriods();
        Collections.sort(notClosedExecutionPeriods);
        for (final ExecutionPeriod notClosedExecutionPeriod : notClosedExecutionPeriods) {
            if (notClosedExecutionPeriod.isAfterOrEquals(currentExecutionPeriod)) {                
                result.add(new SelectItem(notClosedExecutionPeriod.getIdInternal(),
                        notClosedExecutionPeriod.getName() + " " + notClosedExecutionPeriod.getExecutionYear().getYear()));
            }
        }
        return (executionPeriodItems = result);
    }
        
    private Object readDepartmentUnits(String selectedCurricularRuleType) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null
                && selectedCurricularRuleType.equals(CurricularRuleType.ANY_CURRICULAR_COURSE.name())) {
            for (final Unit unit : UnitUtils.readAllDepartmentUnits()) {
                result.add(new SelectItem(unit.getIdInternal(), unit.getName()));
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("choose")));
        return result;
    }

    private Object readParentCourseGroups(String selectedCurricularRuleType) {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION_STRING)) {
            for (final Context context : getDegreeModule().getParentContexts()) {
                final CourseGroup courseGroup = context.getParentCourseGroup();
                if (!courseGroup.isRoot()) {
                    result.add(new SelectItem(courseGroup.getIdInternal(), courseGroup.getName()));
                }
            }
            Collections.sort(result, new BeanComparator("label"));
        }
        result.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("all")));
        return result;
    }

    private List<SelectItem> readDegreeModules(String selectedCurricularRuleType)
            {
        
        final List<SelectItem> result = new ArrayList<SelectItem>();
        if (selectedCurricularRuleType != null && !selectedCurricularRuleType.equals(NO_SELECTION_STRING)) {
            switch (CurricularRuleType.valueOf(selectedCurricularRuleType)) {
            case PRECEDENCY_APPROVED_DEGREE_MODULE:
            case PRECEDENCY_ENROLED_DEGREE_MODULE:
                getDegreeModules(CurricularCourse.class, result);
                break;
                
            case CREDITS_LIMIT:
            case DEGREE_MODULES_SELECTION_LIMIT:
            case PRECEDENCY_BETWEEN_DEGREE_MODULES:
                getDegreeModules(CourseGroup.class, result);
                break;
                
            case EXCLUSIVENESS:
                getDegreeModules(getDegreeModule().getClass(), result);
                break;
            default:
                break;
            }
        }
        result.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("choose")));
        return result;
    }
    
    private void getDegreeModules(final Class<? extends DegreeModule> clazz, final List<SelectItem> result)
            {
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(getExecutionYearID());
        
        final List<List<DegreeModule>> degreeModulesSet = getDegreeCurricularPlan().getDcpDegreeModulesIncludingFullPath(clazz, executionYear);
        for (final List<DegreeModule> degreeModules : degreeModulesSet) {
            final DegreeModule lastDegreeModule = (degreeModules.size() > 0) ? degreeModules.get(degreeModules.size() - 1) : null; 
            if (lastDegreeModule != getDegreeModule()) {
                final StringBuilder pathName = new StringBuilder();
                for (final DegreeModule degreeModule : degreeModules) {
                    pathName.append((pathName.length() == 0) ? "" : " > ").append(degreeModule.getName());
                }
                result.add(new SelectItem(lastDegreeModule.getIdInternal(), pathName.toString()));
            }
        }
        Collections.sort(result, new BeanComparator("label"));
    }
    
    private Integer getFinalEndExecutionPeriodID() {
        return (getViewState().getAttribute("endExecutionPeriodID") == null || getViewState()
                .getAttribute("endExecutionPeriodID").equals(NO_SELECTION_INTEGER)) ? null
                : (Integer) getViewState().getAttribute("endExecutionPeriodID");
    }

    public String createCurricularRule() {
        try {
            checkSelectedAttributes();
            final Object[] args = { 
                    getDegreeModuleID(),
                    CurricularRuleType.valueOf(getSelectedCurricularRuleType()),
                    buildCurricularRuleParametersDTO(),
                    getBeginExecutionPeriodID(),
                    getFinalEndExecutionPeriodID() };
            ServiceUtils.executeService(getUserView(), "CreateRule", args);
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
            final Object[] args = { getCurricularRuleID(), getBeginExecutionPeriodID(), getFinalEndExecutionPeriodID() };
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
            addErrorMessage(getFormatedMessage(domainResources, e.getKey(), e.getArgs()));
        }
        return "";
    }
    
    private void checkSelectedAttributes() throws FenixActionException, FenixFilterException, FenixServiceException {
        if (getSelectedCurricularRuleType() == null || getSelectedCurricularRuleType().equals(NO_SELECTION_STRING)) {
            throw new FenixActionException("must.select.curricular.rule.type");
        }        
    }
    
    private CurricularRuleParametersDTO buildCurricularRuleParametersDTO() throws FenixFilterException, FenixServiceException, NumberFormatException {
        final CurricularRuleParametersDTO parametersDTO = new CurricularRuleParametersDTO();
        parametersDTO.setSelectedDegreeModuleID(getSelectedDegreeModuleID());
        parametersDTO.setContextCourseGroupID((getSelectedContextCourseGroupID() == null || getSelectedContextCourseGroupID().equals(NO_SELECTION_INTEGER)) ? null : getSelectedContextCourseGroupID());
        parametersDTO.setCurricularPeriodInfoDTO(new CurricularPeriodInfoDTO(Integer.valueOf(getSelectedSemester()), CurricularPeriodType.SEMESTER));            
        parametersDTO.setMinimumCredits(getMinimumCredits());
        parametersDTO.setMaximumCredits(getMaximumCredits());
        parametersDTO.setMinimumLimit(getMinimumLimit());
        parametersDTO.setMaximumLimit(getMaximumLimit());
        parametersDTO.setSelectedDegreeID((getSelectedDegreeID() == null || getSelectedDegreeID().equals(NO_SELECTION_INTEGER)) ? null : getSelectedDegreeID());
        parametersDTO.setSelectedDepartmentUnitID((getSelectedDepartmentUnitID() == null || getSelectedDepartmentUnitID().equals(NO_SELECTION_INTEGER)) ? null : getSelectedDepartmentUnitID());
        parametersDTO.setDegreeType((getSelectedDegreeType() == null || getSelectedDegreeType().equals(NO_SELECTION_STRING)) ? null : DegreeType.valueOf(getSelectedDegreeType()));
        // must get these values like this to prevent override values
        parametersDTO.setMinimumYear((Integer) getViewState().getAttribute("minimumYear"));
        parametersDTO.setMaximumYear((Integer) getViewState().getAttribute("maximumYear"));
        parametersDTO.setCredits((Double) getViewState().getAttribute("credits"));
        return parametersDTO;
    }
}
