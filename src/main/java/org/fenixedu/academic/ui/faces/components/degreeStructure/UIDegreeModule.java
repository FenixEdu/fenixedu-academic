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
package org.fenixedu.academic.ui.faces.components.degreeStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.predicate.AcademicPredicates;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.CurricularRuleLabelFormatter;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.commons.i18n.I18N;

public class UIDegreeModule extends UIInput {
    public static final String COMPONENT_TYPE = "org.fenixedu.academic.ui.faces.components.degreeStructure.UIDegreeModule";

    public static final String COMPONENT_FAMILY = "org.fenixedu.academic.ui.faces.components.degreeStructure.UIDegreeModule";

    protected DegreeModule degreeModule;
    protected Context previousContext;
    protected ExecutionYear executionYear;
    protected ExecutionSemester lastExecutionPeriod;
    protected Boolean toEdit;
    protected Boolean showRules = Boolean.FALSE;
    protected int depth;
    protected String tabs;
    protected String module;
    protected String currentPage;
    protected Boolean expandable;

    protected FacesContext facesContext;
    protected ResponseWriter writer;

    protected static final int BASE_DEPTH = UIDegreeCurricularPlan.ROOT_DEPTH + 1;

    public UIDegreeModule() {
        super();
        this.setRendererType(null);
    }

    public UIDegreeModule(DegreeModule degreeModule, Context previousContext, Boolean toEdit, Boolean showRules, int depth,
            String tabs, ExecutionYear executionYear, String module, String currentPage, Boolean expandable) {
        this();
        this.degreeModule = degreeModule;
        this.previousContext = previousContext;
        this.toEdit = toEdit;
        this.showRules = showRules;
        this.depth = depth;
        this.tabs = tabs;

        this.executionYear = executionYear;
        if (this.executionYear == null) {
            // this.executionYear = ExecutionYear.readCurrentExecutionYear();
            this.executionYear = ExecutionYear.readLastExecutionYear();
        }
        this.lastExecutionPeriod = this.executionYear.getLastExecutionPeriod();

        this.module = module;

        this.currentPage = currentPage;
        this.expandable = expandable;

    }

    @Override
    public String getFamily() {
        return UIDegreeModule.COMPONENT_FAMILY;
    }

    @Override
    public void encodeBegin(FacesContext facesContext) throws IOException {
        if (!isRendered()) {
            return;
        }

        setFromAttributes();

        if (this.degreeModule.isLeaf()) {
            new UICurricularCourse(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs,
                    this.executionYear, this.module, null, false).encodeBegin(facesContext);
        } else if (!this.degreeModule.isLeaf()) {
            new UICourseGroup(this.degreeModule, null, this.toEdit, this.showRules, this.depth, this.tabs, Boolean.FALSE,
                    Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, null, this.module, this.currentPage, this.expandable)
                    .encodeBegin(facesContext);
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

    private static final String CODE_NAME_SEPARATOR = " - ";

    protected void appendCodeAndName() throws IOException {
        final String code = degreeModule.getCode();
        if (code != null) {
            writer.append(code).append(CODE_NAME_SEPARATOR);
        }

        writer.append(degreeModule.getNameI18N(lastExecutionPeriod).getContent());
    }

    protected void encodeLink(String page, String aditionalParameters, boolean blank, String... bundleKeys) throws IOException {
        writer.startElement("a", this);
        encodeLinkHref(page, aditionalParameters, blank);
        for (String bundleKey : bundleKeys) {
            writer.write(BundleUtil.getString(Bundle.BOLONHA, bundleKey));
        }
        writer.endElement("a");
    }

    protected void encodeLinkHref(String page, String aditionalParameters, boolean blank) throws IOException {
        Map<String, String> requestParameterMap = this.facesContext.getExternalContext().getRequestParameterMap();
        StringBuilder href = new StringBuilder();
        href.append(page).append("?");

        if (requestParameterMap.get("degreeID") != null) {
            href.append("degreeID=").append(requestParameterMap.get("degreeID")).append("&");
        }

        Object dcpId = null;
        if (requestParameterMap.get("degreeCurricularPlanID") != null) {
            dcpId = requestParameterMap.get("degreeCurricularPlanID");
        } else if (requestParameterMap.get("dcpId") != null) {
            dcpId = requestParameterMap.get("dcpId");
        }
        href.append("degreeCurricularPlanID=").append(dcpId);

        if (this.executionYear != null) {
            href.append("&executionYearID=").append(this.executionYear.getExternalId());
        } else if (requestParameterMap.get("executionPeriodOID") != null) {
            href.append("&executionPeriodOID=").append(requestParameterMap.get("executionPeriodOID"));
        }

        if (aditionalParameters != null) {
            href.append(aditionalParameters);
        }

        href.append("&organizeBy=").append(requestParameterMap.get("organizeBy"));
        href.append("&showRules=").append(requestParameterMap.get("showRules"));
        href.append("&hideCourses=").append(requestParameterMap.get("hideCourses"));
        href.append("&action=").append(requestParameterMap.get("action"));

        writer.writeAttribute("href", href.toString(), null);
        if (blank) {
            writer.writeAttribute("target", "_blank", null);
        }
    }

    protected void encodeCurricularRules() throws IOException {
        List<CurricularRule> curricularRulesToEncode = new ArrayList<CurricularRule>();

        for (CurricularRule curricularRule : this.degreeModule.getVisibleCurricularRules(this.executionYear)) {
            if (curricularRule.appliesToContext(this.previousContext)) {
                curricularRulesToEncode.add(curricularRule);
            }
        }

        if (!curricularRulesToEncode.isEmpty()) {
            writer.startElement("tr", this);

            writer.startElement("td", this);
            writer.writeAttribute("colspan", (this.toEdit) ? "6" : "5", null);
            writer.writeAttribute("style", "padding:0; margin: 0;", null);

            writer.startElement("table", this);
            writer.writeAttribute("class", "smalltxt noborder", null);
            writer.writeAttribute("style", "width: 100%;", null);
            for (CurricularRule curricularRule : curricularRulesToEncode) {
                writer.startElement("tr", this);
                encodeCurricularRule(curricularRule);
                if (this.toEdit && loggedPersonCanManageDegreeCurricularPlans()) {
                    encodeCurricularRuleOptions(curricularRule);
                }
                writer.endElement("tr");
            }
            writer.endElement("table");
            writer.endElement("td");

            writer.endElement("tr");
        }
    }

    private void encodeCurricularRule(CurricularRule curricularRule) throws IOException {
        writer.startElement("td", this);
        if (!this.toEdit) {
            writer.writeAttribute("colspan", "2", null);
        }
        writer.writeAttribute("style", "color: #888;", null);
        writer.append(CurricularRuleLabelFormatter.getLabel(curricularRule, I18N.getLocale()));
        writer.endElement("td");
    }

    private void encodeCurricularRuleOptions(CurricularRule curricularRule) throws IOException {
        writer.startElement("td", this);
        writer.writeAttribute("class", "aright", null);
        if (loggedPersonCanManageDegreeCurricularPlans()) {
            if (this.executionYear != null) {
                encodeLink(module + "/curricularRules/editCurricularRule.faces",
                        "&curricularRuleID=" + curricularRule.getExternalId(), false, "edit");
                writer.append(", ");
            }
            encodeLink(module + "/curricularRules/deleteCurricularRule.faces",
                    "&curricularRuleID=" + curricularRule.getExternalId(), false, "delete");
        }
        writer.endElement("td");
    }

    protected Boolean loggedPersonCanManageDegreeCurricularPlans() {
        return AcademicPredicates.MANAGE_DEGREE_CURRICULAR_PLANS.evaluate(degreeModule.getDegree())
                || (degreeModule.getParentDegreeCurricularPlan().isDraft() && degreeModule.getParentDegreeCurricularPlan().getCurricularPlanMembersGroup().isMember(Authenticate.getUser()));
    }

}
