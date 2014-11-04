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
package org.fenixedu.academic.ui.renderers;

import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.Unit;

public class UnitFunctionsTreeRenderer extends AbstractUnitFunctionsTreeRenderer {

    private String addFunctionLink;
    private String orderFunctionsLink;
    private String editFunctionLink;
    private String deleteFunctionLink;

    public String getAddFunctionLink() {
        return addFunctionLink;
    }

    public void setAddFunctionLink(String addFunctionLink) {
        this.addFunctionLink = addFunctionLink;
    }

    public String getDeleteFunctionLink() {
        return deleteFunctionLink;
    }

    public void setDeleteFunctionLink(String deleteFunctionLink) {
        this.deleteFunctionLink = deleteFunctionLink;
    }

    public String getEditFunctionLink() {
        return editFunctionLink;
    }

    public void setEditFunctionLink(String editFunctionLink) {
        this.editFunctionLink = editFunctionLink;
    }

    public String getOrderFunctionsLink() {
        return orderFunctionsLink;
    }

    public void setOrderFunctionsLink(String orderFunctionsLink) {
        this.orderFunctionsLink = orderFunctionsLink;
    }

    @Override
    protected String getLinkSequenceFor(Unit unit) {
        return createLinkSequence(getAddFunctionLink(), unit.getFunctionsSet().size() > 1 ? getOrderFunctionsLink() : null);
    }

    @Override
    protected String getLinkSequenceFor(Function function) {
        if (function.isVirtual()) {
            return createLinkSequence(getEditFunctionLink(), getDeleteFunctionLink());
        } else {
            return null;
        }
    }

    @Override
    protected String getNoChildrenFor(Object object) {
        if (object instanceof Function) {
            return "true";
        } else {
            return super.getNoChildrenFor(object);
        }
    }

}
