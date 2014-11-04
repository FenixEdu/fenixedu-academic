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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.organizationalStructure.Function;
import org.fenixedu.academic.domain.organizationalStructure.PersonFunction;
import org.fenixedu.academic.ui.struts.action.webSiteManager.PersonFunctionsBean;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;

public class PersonUnitFunctionsTreeRenderer extends AbstractUnitFunctionsTreeRenderer {

    private String addPersonFunctionLink;
    private String editPersonFunctionLink;
    private String deletePersonFunctionLink;

    private String activePersonFunctionImage;

    public String getAddPersonFunctionLink() {
        return addPersonFunctionLink;
    }

    public void setAddPersonFunctionLink(String addPersonFunctionLink) {
        this.addPersonFunctionLink = addPersonFunctionLink;
    }

    public String getDeletePersonFunctionLink() {
        return deletePersonFunctionLink;
    }

    public void setDeletePersonFunctionLink(String deletePersonFunctionLink) {
        this.deletePersonFunctionLink = deletePersonFunctionLink;
    }

    public String getEditPersonFunctionLink() {
        return editPersonFunctionLink;
    }

    public void setEditPersonFunctionLink(String editPersonFunctionLink) {
        this.editPersonFunctionLink = editPersonFunctionLink;
    }

    public String getActivePersonFunctionImage() {
        return activePersonFunctionImage;
    }

    public void setActivePersonFunctionImage(String activePersonfunctionImage) {
        this.activePersonFunctionImage = activePersonfunctionImage;
    }

    @Override
    public boolean isIncludeImage() {
        return super.isIncludeImage() && getActivePersonFunctionImage() == null;
    }

    @Override
    protected String getLinkSequenceFor(Function function) {
        if (function.isVirtual()) {
            return createLinkSequence(getAddPersonFunctionLink());
        }
        return null;
    }

    @Override
    protected String getLinkSequenceFor(PersonFunction personFunction) {
        if (personFunction.hasCredits() || !personFunction.getFunction().isVirtual()) {
            return null;
        }

        YearMonthDay today = new YearMonthDay();

        if (personFunction.belongsToPeriod(today, null)) {
            return createLinkSequence(getEditPersonFunctionLink(), getDeletePersonFunctionLink());
        }

        if (personFunction.isActive(today)) {
            return createLinkSequence(getEditPersonFunctionLink());
        }

        if (isFirstInactive(personFunction)) {
            return createLinkSequence(getEditPersonFunctionLink());
        }

        return null;
    }

    private boolean isFirstInactive(PersonFunction personFunction) {
        for (PersonFunction pf : getChildrenCollectionFor(personFunction.getFunction())) {
            if (pf == personFunction) {
                return true;
            }

            return false;
        }

        return false;
    }

    @Override
    protected String getNoChildrenFor(Object object) {
        if (object instanceof PersonFunction) {
            return "true";
        } else {
            return super.getNoChildrenFor(object);
        }
    }

    @Override
    protected Collection<PersonFunction> getChildrenCollectionFor(Function function) {
        Person person = getPerson();

        SortedSet<PersonFunction> result =
                new TreeSet<PersonFunction>(new ReverseComparator(PersonFunction.COMPARATOR_BY_BEGIN_DATE));
        result.addAll(PersonFunction.getPersonFunctions(person, function));

        return result;
    }

    @Override
    protected String getImagePathFor(PersonFunction personFunction) {
        if (personFunction.isActive(new YearMonthDay())) {
            return getActivePersonFunctionImage();
        } else {
            return null;
        }
    }

    private Person getPerson() {
        return ((PersonFunctionsBean) getContext().getMetaObject().getObject()).getPerson();
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        PersonFunctionsBean bean = (PersonFunctionsBean) object;
        return super.render(Collections.singleton(bean.getUnit()), Set.class);
    }
}
