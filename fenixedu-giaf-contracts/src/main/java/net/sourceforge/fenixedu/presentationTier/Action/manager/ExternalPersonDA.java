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
package org.fenixedu.academic.ui.struts.action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.dto.person.ExternalPersonBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Person.AnyPersonSearchBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.ExternalContract;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ExternalPersonDA extends FenixDispatchAction {

    public static class ExternalPersonBeanFactoryCreator extends ExternalPersonBean implements FactoryExecutor {
        public ExternalPersonBeanFactoryCreator() {
            super();
        }

        @Override
        public Object execute() {
            final Person person = new Person(this);
            Unit unit = getUnit();
            if (unit == null) {
                unit = Unit.findFirstUnitByName(getUnitName());
                if (unit == null) {
                    throw new DomainException("error.unit.does.not.exist");
                }
            }
            new ExternalContract(person, unit, new YearMonthDay(), null);
            return person;
        }
    }

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        AnyPersonSearchBean anyPersonSearchBean = getRenderedObject();
        if (anyPersonSearchBean == null) {
            anyPersonSearchBean = new AnyPersonSearchBean();
        }
        final String name = request.getParameter("name");
        if (isSpecified(name)) {
            anyPersonSearchBean.setName(name);
        }
        request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);

        return mapping.findForward("showSearch");
    }

    public ActionForward prepareCreate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        ExternalPersonBeanFactoryCreator externalPersonBean = getRenderedObject();
        if (externalPersonBean == null) {
            externalPersonBean = new ExternalPersonBeanFactoryCreator();
        }
        setRequestParameters(request, externalPersonBean);
        request.setAttribute("externalPersonBean", externalPersonBean);

        return mapping.findForward("showCreateForm");
    }

    private void setRequestParameters(final HttpServletRequest request, final ExternalPersonBeanFactoryCreator externalPersonBean) {
        final String name = request.getParameter("name");
        if (isSpecified(name)) {
            externalPersonBean.setName(name);
        }
        final String idDocumentType = request.getParameter("idDocumentType");
        if (isSpecified(idDocumentType)) {
            externalPersonBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
        }
        final String documentIdNumber = request.getParameter("documentIdNumber");
        if (isSpecified(documentIdNumber)) {
            externalPersonBean.setDocumentIdNumber(documentIdNumber);
        }
    }

    private boolean isSpecified(final String string) {
        return string != null && string.length() > 0;
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final Person person = (Person) executeFactoryMethod();
        request.setAttribute("person", person);
        RenderUtils.invalidateViewState();
        return mapping.findForward("showCreatedPerson");
    }
}