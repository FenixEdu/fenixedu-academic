package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriodType;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRuleType;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionDoneDegreeModule;
import net.sourceforge.fenixedu.domain.curricularRules.RestrictionEnroledDegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class UIDegreeModule extends UIInput {
    public static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    public static final String COMPONENT_FAMILY = "net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure.UIDegreeModule";

    protected DegreeModule degreeModule;
    protected Context previousContext;
    protected Boolean toEdit;
    protected Boolean showRules = Boolean.FALSE;
    protected int depth;
    protected String tabs;

    protected FacesContext facesContext;
    protected ResponseWriter writer;

    protected static final int BASE_DEPTH = UIDegreeCurricularPlan.ROOT_DEPTH + 1;
    
    public UIDegreeModule() {
        super();
        this.setRendererType(null);
    }

    public UIDegreeModule(DegreeModule degreeModule, Context previousContext, Boolean toEdit, Boolean showRules, int depth, String tabs) {
        this();
        this.degreeModule = degreeModule;
        this.previousContext = previousContext;
        this.toEdit = toEdit;
        this.showRules = showRules;
        this.depth = depth;
        this.tabs = tabs;
    }

    public String getFamily() {
        return UIDegreeModule.COMPONENT_FAMILY;
    }

    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }
        
        setFromAttributes();
        
        if (this.degreeModule instanceof CurricularCourse) {
            new UICurricularCourse(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs).encodeBegin(facesContext);
        } else if (this.degreeModule instanceof CourseGroup) {
            new UICourseGroup(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs, Boolean.FALSE).encodeBegin(facesContext);
        }
    }
    
    private void setFromAttributes() {
        if (this.degreeModule == null) {
            this.degreeModule = (DegreeModule) this.getAttributes().get("degreeModule");
        }
        if (this.toEdit == null) {
            this.toEdit = (Boolean) this.getAttributes().get("toEdit");
        }
        if (this.tabs == null) {
            this.depth = UIDegreeModule.BASE_DEPTH;
            this.tabs = "";
        }
    }

    protected String getBundleValue(String bundleName, String bundleKey) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources/" + bundleName, facesContext.getViewRoot().getLocale());
        return bundle.getString(bundleKey);
    }

    protected Locale getLocale() {
        return facesContext.getViewRoot().getLocale();
    }

    protected void encodeLink(String href, String bundleKey) throws IOException {
        writer.startElement("a", this);
        writer.writeAttribute("href", href, null);
        writer.write(this.getBundleValue("BolonhaManagerResources", bundleKey));
        writer.endElement("a");
    }

    protected void encodeCurricularRules() throws IOException {
        List<CurricularRule> curricularRulesToEncode = new ArrayList<CurricularRule>();

        for (CurricularRule curricularRule : this.degreeModule.getCurricularRules()) {
            if (curricularRule.getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE)
                    || curricularRule.getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE)) {
                if (ruleAppliesToCurricularCourseContex(curricularRule) && ruleAppliesToCurricularCoursePeriod(curricularRule)) {
                    curricularRulesToEncode.add(curricularRule);
                }
            } else if (ruleAppliesToCurricularCourseContex(curricularRule) ) {
                curricularRulesToEncode.add(curricularRule);
            }
        }

        if (!curricularRulesToEncode.isEmpty()) {
            writer.startElement("tr", this);
            writer.writeAttribute("class", "smalltxt", null);
        
            writer.startElement("td", this);
            writer.writeAttribute("class", "p_mvert015", null);
            writer.writeAttribute("colspan", (this.toEdit) ? 5 : 6, null);
            writer.writeAttribute("align", "right", null);
            writer.writeAttribute("rowspan", curricularRulesToEncode.size(), null);
            for (CurricularRule curricularRule : curricularRulesToEncode) {
                encodeCurricularRule(curricularRule);    
            }
            writer.endElement("td");

            writer.startElement("td", this);
            writer.writeAttribute("class", "p_mvert015", null);
            writer.writeAttribute("align", "right", null);
            writer.writeAttribute("style", "width: 7em;", null);
            writer.writeAttribute("rowspan", curricularRulesToEncode.size(), null);
            if (this.toEdit) {
                for (CurricularRule curricularRule : curricularRulesToEncode) {
                    encodeCurricularRuleOptions(curricularRule);    
                }
            }
            writer.endElement("td");
            
            writer.endElement("tr");
            
            for (int i = 0; i < curricularRulesToEncode.size(); i++) {
                writer.startElement("tr", this);
                writer.endElement("tr");
            }
        }
    }
    
    private boolean ruleAppliesToCurricularCourseContex(CurricularRule curricularRule) {
        return (curricularRule.getContextCourseGroup() == null 
                || curricularRule.getContextCourseGroup().equals(this.previousContext.getCourseGroup()));
    }

    private boolean ruleAppliesToCurricularCoursePeriod(CurricularRule curricularRule) {
        if (curricularRule.getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_APPROVED_DEGREE_MODULE)) {
            RestrictionDoneDegreeModule concreteRule = (RestrictionDoneDegreeModule) curricularRule;
            
            if ((concreteRule.getCurricularPeriodType().equals(CurricularPeriodType.SEMESTER) && concreteRule.getCurricularPeriodOrder().equals(0)) 
                    || (concreteRule.getCurricularPeriodType() != null 
                            && concreteRule.getCurricularPeriodOrder() != null 
                            && concreteRule.getCurricularPeriodType().equals(previousContext.getCurricularPeriod().getPeriodType()) 
                            && concreteRule.getCurricularPeriodOrder().equals(previousContext.getCurricularPeriod().getOrder()))) {
                return true;
            }
        } else if (curricularRule.getCurricularRuleType().equals(CurricularRuleType.PRECEDENCY_ENROLED_DEGREE_MODULE)) {
            RestrictionEnroledDegreeModule concreteRule = (RestrictionEnroledDegreeModule) curricularRule; 
            
            if ((concreteRule.getCurricularPeriodType().equals(CurricularPeriodType.SEMESTER) && concreteRule.getCurricularPeriodOrder().equals(0)) 
                    || (concreteRule.getCurricularPeriodType() != null 
                            && concreteRule.getCurricularPeriodOrder() != null 
                            && concreteRule.getCurricularPeriodType().equals(previousContext.getCurricularPeriod().getPeriodType()) 
                            && concreteRule.getCurricularPeriodOrder().equals(previousContext.getCurricularPeriod().getOrder()))) {
                return true;
            }
        }
        return false;
    }

    private void encodeCurricularRule(CurricularRule curricularRule) throws IOException {
        writer.startElement("p", this);
        writer.append(CurricularRuleLabelFormatter.getLabel(curricularRule));
        writer.endElement("p");
    }

    private void encodeCurricularRuleOptions(CurricularRule curricularRule) throws IOException {
        writer.startElement("p", this);
        encodeLink("../curricularRules/editCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&curricularRuleID=" + curricularRule.getIdInternal(), "edit");
        writer.append(" , ");
        encodeLink("../curricularRules/deleteCurricularRule.faces?degreeCurricularPlanID=" + this.facesContext.getExternalContext().getRequestParameterMap()
                .get("degreeCurricularPlanID") + "&curricularRuleID=" + curricularRule.getIdInternal(), "delete");
        writer.endElement("p");
    }

}
