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
package net.sourceforge.fenixedu.presentationTier.jsf.components.degreeStructure;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class DegreeCurricularPlanTag extends UIComponentTag {
    private String dcp;
    private String toEdit;
    private String showRules;
    private String organizeBy;
    private String onlyStructure;
    private String toOrder;
    private String hideCourses;
    private String executionYear;
    private String reportsAvailable;
    private String module;

    public String getDcp() {
        return dcp;
    }

    public void setDcp(String dcp) {
        this.dcp = dcp;
    }

    public String getToEdit() {
        return toEdit;
    }

    public void setToEdit(String toEdit) {
        this.toEdit = toEdit;
    }

    public String getShowRules() {
        return showRules;
    }

    public void setShowRules(String showRules) {
        this.showRules = showRules;
    }

    public String getOrganizeBy() {
        return organizeBy;
    }

    public void setOrganizeBy(String structureBy) {
        this.organizeBy = structureBy;
    }

    public String getOnlyStructure() {
        return onlyStructure;
    }

    public void setOnlyStructure(String onlyStructure) {
        this.onlyStructure = onlyStructure;
    }

    public String getToOrder() {
        return toOrder;
    }

    public void setToOrder(String toOrder) {
        this.toOrder = toOrder;
    }

    public String getHideCourses() {
        return hideCourses;
    }

    public void setHideCourses(String hideCourses) {
        this.hideCourses = hideCourses;
    }

    public String getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(String executionYear) {
        this.executionYear = executionYear;
    }

    public String getReportsAvailable() {
        return reportsAvailable;
    }

    public void setReportsAvailable(String reportsAvailable) {
        this.reportsAvailable = reportsAvailable;
    }

    @Override
    public String getComponentType() {
        return UIDegreeCurricularPlan.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);

        JsfTagUtils.setString(component, "dcp", this.dcp);
        JsfTagUtils.setString(component, "toEdit", this.toEdit);
        JsfTagUtils.setString(component, "showRules", this.showRules);
        JsfTagUtils.setString(component, "organizeBy", this.organizeBy);
        JsfTagUtils.setString(component, "onlyStructure", this.onlyStructure);
        JsfTagUtils.setString(component, "toOrder", this.toOrder);
        JsfTagUtils.setString(component, "hideCourses", this.hideCourses);
        JsfTagUtils.setString(component, "executionYear", this.executionYear);
        JsfTagUtils.setString(component, "reportsAvailable", this.reportsAvailable);
        JsfTagUtils.setString(component, "module", this.module);
    }

    @Override
    public void release() {
        super.release();
        this.dcp = null;
        this.toEdit = null;
        this.showRules = null;
        this.organizeBy = null;
        this.onlyStructure = null;
        this.toEdit = null;
        this.hideCourses = null;
        this.executionYear = null;
        this.reportsAvailable = null;
        this.module = null;
    }

}
