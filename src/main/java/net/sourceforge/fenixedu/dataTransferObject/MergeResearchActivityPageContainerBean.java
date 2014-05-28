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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.DomainObject;

public abstract class MergeResearchActivityPageContainerBean extends PageContainerBean implements Serializable {

    private List<DomainObject> selectedObjects = new ArrayList<DomainObject>();

    private PageContainerBean pageContainerBean;

    public MergeResearchActivityPageContainerBean() {
        pageContainerBean = new PageContainerBean();
    }

    public List<DomainObject> getSelectedObjects() {
        return this.selectedObjects;
    }

    @Override
    protected void setPageObjects(List<DomainObject> pageObjects) {
        this.selectedObjects = pageObjects;
    }

    public PageContainerBean getPageContainerBean() {
        return pageContainerBean;
    }

    public void setPageContainerBean(PageContainerBean pageContainerBean) {
        this.pageContainerBean = pageContainerBean;
    }

    @Override
    public List<DomainObject> getObjects() {
        return getSelectedObjects();
    }

    @Override
    public void setObjects(List<DomainObject> objects) {
        List<DomainObject> result = new ArrayList<DomainObject>(objects);
        result.removeAll(getSelectedObjects());
        getPageContainerBean().setObjects(result);
    }

    public void addSelected() {
        if (getPageContainerBean().getSelected() != null) {
            this.selectedObjects.add(getPageContainerBean().getSelected());
            getPageContainerBean().setSelected(null);
        }
    }

    public void removeSelected() {
        if (getSelected() != null) {
            this.selectedObjects.remove(getSelected());
            setSelected(null);
        }
    }

    public void reset() {
        setSelected(null);
        selectedObjects = new ArrayList<DomainObject>();
    }
}
