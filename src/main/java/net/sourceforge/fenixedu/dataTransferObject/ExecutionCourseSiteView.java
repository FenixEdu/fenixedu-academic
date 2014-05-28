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
 * Created on 5/Mai/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author João Mota
 * 
 * 
 */
public class ExecutionCourseSiteView extends SiteView {

    private ExecutionCourse executionCourse;

    /**
     * @param commonComponent
     * @param bodyComponent
     */
    public ExecutionCourseSiteView(ISiteComponent commonComponent, ISiteComponent bodyComponent) {
        setCommonComponent(commonComponent);
        setComponent(bodyComponent);
    }

    private ISiteComponent commonComponent;

    /**
     * @return
     */
    public ISiteComponent getCommonComponent() {

        return commonComponent;
    }

    /**
     * @param component
     */
    public void setCommonComponent(ISiteComponent component) {
        commonComponent = component;
    }

    @Override
    public boolean equals(Object obj) {

        boolean resultado = false;

        if (obj instanceof ExecutionCourseSiteView) {
            ExecutionCourseSiteView siteView = (ExecutionCourseSiteView) obj;

            resultado =
                    getCommonComponent().equals(siteView.getCommonComponent()) && getComponent().equals(siteView.getComponent());
        }

        return resultado;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

}
