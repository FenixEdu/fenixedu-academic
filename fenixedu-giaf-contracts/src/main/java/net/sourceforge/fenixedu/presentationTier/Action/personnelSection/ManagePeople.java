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
package org.fenixedu.academic.ui.struts.action.personnelSection;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fenixedu.academic.service.services.manager.CreateNewInternalPerson;
import org.fenixedu.academic.service.services.person.SearchPersonMatchingAnyParameter;
import org.fenixedu.academic.dto.person.InternalPersonBean;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Person.AnyPersonSearchBean;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.CollectionPager;

@StrutsFunctionality(app = PersonnelSectionApplication.class, path = "create-people", titleKey = "link.manage.people.create")
@Mapping(path = "/personnelManagePeople", module = "personnelSection")
@Forwards({ @Forward(name = "searchPeople", path = "/personnelSection/people/searchPeople.jsp"),
        @Forward(name = "createPerson", path = "/personnelSection/people/createPerson.jsp"),
        @Forward(name = "createPersonFillInfo", path = "/personnelSection/people/createPersonFillInfo.jsp"),
        @Forward(name = "viewPerson", path = "/personnelSection/people/viewPerson.jsp") })
public class ManagePeople extends FenixDispatchAction {

    public ActionForward search(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        return mapping.findForward("searchPeople");
    }

    @EntryPoint
    public ActionForward prepareCreatePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final AnyPersonSearchBean anyPersonSearchBean = new AnyPersonSearchBean();
        request.setAttribute("anyPersonSearchBean", anyPersonSearchBean);
        return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        final IViewState viewState = RenderUtils.getViewState("anyPersonSearchBeanId");
        AnyPersonSearchBean bean = (AnyPersonSearchBean) viewState.getMetaObject().getObject();

        CollectionPager<Person> result =
                SearchPersonMatchingAnyParameter.run(bean.getName(), null, null, bean.getDocumentIdNumber(),
                        bean.getIdDocumentType(), null, null, null, null, null, null);

        request.setAttribute("resultPersons", result.getCollection());
        request.setAttribute("anyPersonSearchBean", bean);
        return mapping.findForward("createPerson");
    }

    private void setRequestParametersToCreateInvitedPerson(final HttpServletRequest request, final InternalPersonBean personBean) {

        AnyPersonSearchBean anyPersonSearchBean = getRenderedObject("anyPersonSearchBeanId");
        if (anyPersonSearchBean != null) {
            personBean.setName(anyPersonSearchBean.getName());
            personBean.setIdDocumentType(anyPersonSearchBean.getIdDocumentType());
            personBean.setDocumentIdNumber(anyPersonSearchBean.getDocumentIdNumber());
        } else {
            final String name = request.getParameter("name");
            if (isSpecified(name)) {
                personBean.setName(name);
            }
            final String idDocumentType = request.getParameter("idDocumentType");
            if (isSpecified(idDocumentType)) {
                personBean.setIdDocumentType(IDDocumentType.valueOf(idDocumentType));
            }
            final String documentIdNumber = request.getParameter("documentIdNumber");
            if (isSpecified(documentIdNumber)) {
                personBean.setDocumentIdNumber(documentIdNumber);
            }
        }
        request.setAttribute("personBean", personBean);
    }

    private boolean isSpecified(final String string) {
        return !StringUtils.isEmpty(string);
    }

    public ActionForward prepareCreatePersonFillInformation(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        setRequestParametersToCreateInvitedPerson(request, new InternalPersonBean());
        request.setAttribute("initialUnit", UnitUtils.readInstitutionUnit());
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPerson(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final InternalPersonBean bean = getRenderedObject();
        try {
            final Person person = CreateNewInternalPerson.run(bean);
            return viewPerson(person, mapping, request);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage());
            request.setAttribute("invitedPersonBean", bean);
            return mapping.findForward("createPersonFillInfo");
        }
    }

    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InternalPersonBean bean = getRenderedObject();
        request.setAttribute("invitedPersonBean", bean);
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request)
            throws Exception {
        request.setAttribute("person", person);
        return mapping.findForward("viewPerson");
    }

    public ActionForward attributeRole(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final RoleType roleType = RoleType.valueOf(request.getParameter("roleType"));
        CreateNewInternalPerson.attributeRoles(person, Collections.singleton(roleType));
        return viewPerson(person, mapping, request);
    }

}
