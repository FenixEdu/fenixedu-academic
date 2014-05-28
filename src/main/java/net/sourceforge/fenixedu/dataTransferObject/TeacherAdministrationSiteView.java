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
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author Fernanda Quitério
 * 
 */
public class TeacherAdministrationSiteView extends SiteView {

    public TeacherAdministrationSiteView() {
    }

    /**
     * @param commonComponent
     * @param bodyComponent
     */
    public TeacherAdministrationSiteView(ISiteComponent commonComponent, ISiteComponent bodyComponent) {
        setCommonComponent(commonComponent);
        setComponent(bodyComponent);
    }

    private ISiteComponent commonComponent;

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String result = "[TeacherAdministrationSiteView";
        result += ", commonComponent=" + getCommonComponent();
        result += ", bodyComponent=" + getComponent();
        result += "]";
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TeacherAdministrationSiteView) {
            TeacherAdministrationSiteView siteView = (TeacherAdministrationSiteView) obj;
            boolean result1 = getCommonComponent().equals(siteView.getCommonComponent());
            boolean result2 = getComponent().equals(siteView.getComponent());

            return result1 && result2;
        }
        return false;
    }

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

}