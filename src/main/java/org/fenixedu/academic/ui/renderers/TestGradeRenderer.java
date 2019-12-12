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

import org.fenixedu.academic.domain.curriculum.IGrade;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

@Deprecated
public class TestGradeRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES", "msg.enrolled"));
                }

                IGrade grade = (IGrade) object;
                switch (grade.getGradeType()) {
                case GRADENA:
                case GRADERE:
                case GRADEAP:
                    return new HtmlText(RenderUtils.getResourceString("ENUMERATION_RESOURCES", grade.getGradeType().toString()));
                case GRADEFIVE:
                case GRADETWENTY:
                    return new HtmlText(((Integer) grade.getGradeValue()).toString());
                }

                return new HtmlText("");
            }

        };
    }

}
