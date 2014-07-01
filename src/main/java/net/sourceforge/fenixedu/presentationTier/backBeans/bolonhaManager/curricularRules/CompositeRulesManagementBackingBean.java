/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Feb 21, 2006
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Servico.bolonhaManager.CreateCompositeRule;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.LogicOperator;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.CurricularRuleLabelFormatter;

import org.fenixedu.bennu.core.i18n.BundleUtil;

public class CompositeRulesManagementBackingBean extends CurricularRulesManagementBackingBean {

    private UISelectItems curricularRuleItems;
    private UISelectItems beginExecutionPeriodItemsForCompositeRule;
    private UISelectItems endExecutionPeriodItemsForCompositeRule;

    public String[] getSelectedCurricularRuleIDs() {
        return (String[]) getViewState().getAttribute("selectedCurricularRuleIDs");
    }

    public void setSelectedCurricularRuleIDs(String[] selectedCurricularRuleIDs) {
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

    public UISelectItems getCurricularRuleItems() throws FenixServiceException {
        if (curricularRuleItems == null) {
            curricularRuleItems = new UISelectItems();
            curricularRuleItems.setValue(readCurricularRulesLabels());
        }
        return curricularRuleItems;
    }

    public void setCurricularRuleItems(UISelectItems curricularRuleItems) {
        this.curricularRuleItems = curricularRuleItems;
    }

    private List<SelectItem> readCurricularRulesLabels() throws FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final CurricularRule curricularRule : getDegreeModule().getCurricularRules()) {
            result.add(new SelectItem(curricularRule.getExternalId(), CurricularRuleLabelFormatter.getLabel(curricularRule)));
        }
        return result;
    }

    public UISelectItems getBeginExecutionPeriodItemsForCompositeRule() throws FenixServiceException {
        if (beginExecutionPeriodItemsForCompositeRule == null) {
            beginExecutionPeriodItemsForCompositeRule = new UISelectItems();
            beginExecutionPeriodItemsForCompositeRule.setValue(readExecutionPeriodItems());
        }
        return beginExecutionPeriodItemsForCompositeRule;
    }

    public void setBeginExecutionPeriodItemsForCompositeRule(UISelectItems beginExecutionPeriodItemsForCompositeRule) {
        this.beginExecutionPeriodItemsForCompositeRule = beginExecutionPeriodItemsForCompositeRule;
    }

    public UISelectItems getEndExecutionPeriodItemsForCompositeRule() throws FenixServiceException {
        if (endExecutionPeriodItemsForCompositeRule == null) {
            endExecutionPeriodItemsForCompositeRule = new UISelectItems();
            final List<SelectItem> values = new ArrayList<SelectItem>(readExecutionPeriodItems());
            values.add(0, new SelectItem(NO_SELECTION_STRING, BundleUtil.getString(Bundle.BOLONHA, "opened")));
            endExecutionPeriodItemsForCompositeRule.setValue(values);
        }
        return endExecutionPeriodItemsForCompositeRule;
    }

    public void setEndExecutionPeriodItemsForCompositeRule(UISelectItems endExecutionPeriodItemsForCompositeRule) {
        this.endExecutionPeriodItemsForCompositeRule = endExecutionPeriodItemsForCompositeRule;
    }

    public String createCompositeRule() {
        try {
            CreateCompositeRule.run(LogicOperator.valueOf(getSelectedLogicOperator()), getSelectedCurricularRuleIDs());
            setSelectedCurricularRuleIDs(null);
            getCurricularRuleItems().setValue(readCurricularRulesLabels());
        } catch (NotAuthorizedException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, "error.notAuthorized"));
        } catch (FenixServiceException e) {
            addErrorMessage(BundleUtil.getString(Bundle.BOLONHA, e.getMessage()));
        } catch (DomainException e) {
            addErrorMessage(BundleUtil.getString(Bundle.DOMAIN_EXCEPTION, e.getMessage()));
        }
        return "";
    }

}
