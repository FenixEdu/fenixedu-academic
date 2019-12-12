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
package org.fenixedu.academic.ui.renderers;

import org.fenixedu.academic.domain.Grade;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class GradeRenderer extends OutputRenderer {

    private boolean showGradeScale = true;

    private String gradeClasses;

    private String gradeScaleClasses;

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Grade grade = (Grade) object;
                if (grade == null || grade.isEmpty()) {
                    return new HtmlText();
                }

                HtmlInlineContainer container = new HtmlInlineContainer();
                HtmlText gradeValue = new HtmlText(grade.getValue());
                gradeValue.setClasses(getGradeClasses());
                container.addChild(gradeValue);
                if (isShowGradeScale()) {
                    HtmlText gradeScale = new HtmlText("(" + grade.getGradeScale().getName().getContent() + ")");
                    gradeScale.setClasses(getGradeScaleClasses());
                    container.addChild(gradeScale);
                }

                return container;
            }

        };
    }

    public boolean isShowGradeScale() {
        return showGradeScale;
    }

    public void setShowGradeScale(boolean showGradeScale) {
        this.showGradeScale = showGradeScale;
    }

    public String getGradeClasses() {
        return gradeClasses;
    }

    public void setGradeClasses(String gradeClasses) {
        this.gradeClasses = gradeClasses;
    }

    public String getGradeScaleClasses() {
        return gradeScaleClasses;
    }

    public void setGradeScaleClasses(String gradeScaleClasses) {
        this.gradeScaleClasses = gradeScaleClasses;
    }

}
