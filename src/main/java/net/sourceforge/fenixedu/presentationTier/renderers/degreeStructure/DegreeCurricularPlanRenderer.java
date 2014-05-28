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
package net.sourceforge.fenixedu.presentationTier.renderers.degreeStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.utl.ist.fenix.tools.util.Pair;

public class DegreeCurricularPlanRenderer extends OutputRenderer {

    static private final String CELL_CLASSES = "scplancolident, scplancolcurricularcourse, scplancolcurricularcourse, "
            + "scplancolenrollmentstate, scplancolenrollmenttype, scplancolgrade, scplancolweight, scplancolects, scplancolects";

    // TODO: change this (create constants and correct css classes)

    private String degreeCurricularPlanClass = "scplan";
    private String courseGroupRowClass = "scplangroup";
    private String curricularCourseRowClass = "scplanenrollment";
    private String curricularRuleRowClass = "scprules";
    private String cellClasses = CELL_CLASSES;

    public String getDegreeCurricularPlanClass() {
        return degreeCurricularPlanClass;
    }

    public void setDegreeCurricularPlanClass(String degreeCurricularPlanClass) {
        this.degreeCurricularPlanClass = degreeCurricularPlanClass;
    }

    public String getCourseGroupRowClass() {
        return courseGroupRowClass;
    }

    public void setCourseGroupRowClass(String courseGroupRowClass) {
        this.courseGroupRowClass = courseGroupRowClass;
    }

    public String getCurricularCourseRowClass() {
        return curricularCourseRowClass;
    }

    public void setCurricularCourseRowClass(String curricularCourseRowClass) {
        this.curricularCourseRowClass = curricularCourseRowClass;
    }

    public String getCurricularRuleRowClass() {
        return curricularRuleRowClass;
    }

    public void setCurricularRuleRowClass(String curricularRuleRowClass) {
        this.curricularRuleRowClass = curricularRuleRowClass;
    }

    private String[] getCellClasses() {
        return this.cellClasses.split(",");
    }

    protected String getTabCellClass() {
        return getCellClasses()[0];
    }

    protected String getLabelCellClass() {
        return getCellClasses()[1];
    }

    protected String getCurriclarCourseCellClass() {
        return getCellClasses()[2];
    }

    protected String getCurricularPeriodCellClass() {
        return getCellClasses()[3];
    }

    protected String getRegimeCellClass() {
        return getCellClasses()[4];
    }

    protected String getCourseLoadCellClass() {
        return getCellClasses()[5];
    }

    protected String getEctsCreditsCellClass() {
        return getCellClasses()[6];
    }

    protected String getOptionalInformationCellClass() {
        return getCellClasses()[7];
    }

    private DegreeCurricularPlanRendererConfig config;

    protected DegreeCurricularPlan getDegreeCurricularPlan() {
        return config.getDegreeCurricularPlan();
    }

    protected ExecutionYear getExecutionInterval() {
        return config.getExecutionInterval();
    }

    private boolean organizeByGroups() {
        return config.organizeByGroups();
    }

    private boolean organizeByYears() {
        return config.organizeByYears();
    }

    protected boolean showRules() {
        return config.isShowRules();
    }

    protected boolean showCourses() {
        return config.isShowCourses();
    }

    protected String getViewCurricularCourseUrl() {
        return config.getViewCurricularCourseUrl();
    }

    protected List<Pair<String, String>> getViewCurricularCourseUrlParameters() {
        return config.getViewCurricularCourseUrlParameters();
    }

    protected boolean isCurricularCourseLinkable() {
        return config.isCurricularCourseLinkable();
    }

    protected String getDegreeModuleIdAttributeName() {
        return config.getDegreeModuleIdAttributeName();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        config = (DegreeCurricularPlanRendererConfig) object;

        if (organizeByGroups()) {
            return new DegreeCurricularPlanGroupsLayout(this);

        } else if (organizeByYears()) {
            return new DegreeCurricularPlanYearsLayout(this);
        }

        throw new RuntimeException("error.DegreeCurricularPlanRenderer.unexpected.organization");
    }

}
