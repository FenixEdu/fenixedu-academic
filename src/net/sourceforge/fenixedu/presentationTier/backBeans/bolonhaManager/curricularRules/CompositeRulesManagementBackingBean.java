/*
 * Created on Feb 21, 2006
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperators;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

public class CompositeRulesManagementBackingBean extends CurricularRulesManagementBackingBean {

    private UISelectItems curricularRuleItems;
    private UISelectItems beginExecutionPeriodItemsForCompositeRule;
    private UISelectItems endExecutionPeriodItemsForCompositeRule;

    public Integer[] getSelectedCurricularRuleIDs() {
        return (Integer[]) getViewState().getAttribute("selectedCurricularRuleIDs");
    }

    public void setSelectedCurricularRuleIDs(Integer[] selectedCurricularRuleIDs) {
        getViewState().setAttribute("selectedCurricularRuleIDs", selectedCurricularRuleIDs);
    }

    private void removeSelectedCurricularRuleIDs() {
        getViewState().removeAttribute("selectedCurricularRuleIDs");
    }

    public String getSelectedLogicOperator() {
        if (getViewState().getAttribute("selectedLogicOperator") == null) {
            setSelectedLogicOperator("AND");
        }
        return (String) getViewState().getAttribute("selectedLogicOperator");
    }

    public void setSelectedLogicOperator(String selectedLogicOperator) {
        getViewState().setAttribute("selectedLogicOperator", selectedLogicOperator);
    }

    public UISelectItems getCurricularRuleItems() throws FenixFilterException, FenixServiceException {
        if (curricularRuleItems == null) {
            curricularRuleItems = new UISelectItems();
            curricularRuleItems.setValue(readCurricularRulesLabels());
        }
        return curricularRuleItems;
    }

    public void setCurricularRuleItems(UISelectItems curricularRuleItems) {
        this.curricularRuleItems = curricularRuleItems;
    }

    private List<SelectItem> readCurricularRulesLabels() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final CurricularRule curricularRule : getDegreeModule().getCurricularRules()) {
            result.add(new SelectItem(curricularRule.getIdInternal(), CurricularRuleLabelFormatter.getLabel(curricularRule)));
        }
        return result;
    }
    
    public UISelectItems getBeginExecutionPeriodItemsForCompositeRule() throws FenixFilterException, FenixServiceException {
        if (beginExecutionPeriodItemsForCompositeRule == null) {
            beginExecutionPeriodItemsForCompositeRule = new UISelectItems();
            beginExecutionPeriodItemsForCompositeRule.setValue(readExecutionPeriodItems());
        }
        return beginExecutionPeriodItemsForCompositeRule;
    }

    public void setBeginExecutionPeriodItemsForCompositeRule(UISelectItems beginExecutionPeriodItemsForCompositeRule) {
        this.beginExecutionPeriodItemsForCompositeRule = beginExecutionPeriodItemsForCompositeRule;
    }
    
    public UISelectItems getEndExecutionPeriodItemsForCompositeRule() throws FenixFilterException, FenixServiceException {
        if (endExecutionPeriodItemsForCompositeRule == null) {
            endExecutionPeriodItemsForCompositeRule = new UISelectItems();
            final List<SelectItem> values = new ArrayList<SelectItem>(readExecutionPeriodItems());
            values.add(0, new SelectItem(NO_SELECTION_INTEGER, bolonhaResources.getString("opened")));
            endExecutionPeriodItemsForCompositeRule.setValue(values);
        }
        return endExecutionPeriodItemsForCompositeRule;
    }

    public void setEndExecutionPeriodItemsForCompositeRule(UISelectItems endExecutionPeriodItemsForCompositeRule) {
        this.endExecutionPeriodItemsForCompositeRule = endExecutionPeriodItemsForCompositeRule;
    }

    public String createCompositeRule() throws FenixFilterException {
        try {
            final Object args[] = { 
                    LogicOperators.valueOf(getSelectedLogicOperator()), 
                    getSelectedCurricularRuleIDs() };
            ServiceUtils.executeService(getUserView(), "CreateCompositeRule", args);
            removeSelectedCurricularRuleIDs();
            getCurricularRuleItems().setValue(readCurricularRulesLabels());
        } catch (FenixFilterException e) {
            addErrorMessage(bolonhaResources.getString("error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(bolonhaResources.getString(e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(domainResources.getString(e.getMessage()));
        }
        return "";
    }

}
