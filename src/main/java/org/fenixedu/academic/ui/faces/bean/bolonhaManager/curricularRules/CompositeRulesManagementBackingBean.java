/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Feb 21, 2006
 */
package org.fenixedu.academic.ui.faces.bean.bolonhaManager.curricularRules;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UISelectItems;
import javax.faces.model.SelectItem;

import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.util.LogicOperator;
import org.fenixedu.academic.service.services.bolonhaManager.CreateCompositeRule;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
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
        for (final CurricularRule curricularRule : getDegreeModule().getCurricularRulesSet()) {
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
            setSelectedCurricularRuleIDs(new String[] {});
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
