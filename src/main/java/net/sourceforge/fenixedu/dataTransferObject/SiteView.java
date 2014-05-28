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
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject;

/**
 * @author João Mota
 * @author Fernanda Quitério
 * 
 */
public class SiteView extends DataTranferObject {

    private ISiteComponent bodyComponent;

    @Override
    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof SiteView) {
            SiteView siteView = (SiteView) arg0;
            if (getComponent() == null && siteView.getComponent() == null) {
                result = true;
            } else if (getComponent() != null && getComponent().equals(siteView.getComponent())) {
                result = true;
            }
        }

        return result;
    }

    /**
     *  
     */
    public SiteView() {
    }

    public SiteView(ISiteComponent bodyComponent) {
        setComponent(bodyComponent);
    }

    /**
     * @return
     */
    public ISiteComponent getComponent() {
        return bodyComponent;
    }

    /**
     * @param component
     */
    public void setComponent(ISiteComponent component) {
        this.bodyComponent = component;
    }

}