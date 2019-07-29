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
package org.fenixedu.academic.ui.struts.action.accounts;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.EmailAddress;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.contacts.PartyContactType;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.treasury.TreasuryBridgeAPIFactory;
import org.fenixedu.academic.domain.util.email.Message;
import org.fenixedu.academic.domain.util.email.SystemSender;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.portal.domain.PortalConfiguration;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.fenixedu.commons.i18n.LocalizedString;

import com.google.common.base.Strings;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = AccountManagementApp.class, path = "manage-accounts",
        titleKey = "link.accountmanagement.manageaccounts")
@Mapping(path = "/accounts/manageAccounts")
@Forward(name = "manageAccounts", path = "/accounts/manageAccounts.jsp")
@Forward(name = "createPerson", path = "/accounts/createPerson.jsp")
@Forward(name = "createPersonFillInfo", path = "/accounts/createPersonFillInfo.jsp")
@Forward(name = "viewPerson", path = "/accounts/viewPerson.jsp")
@Forward(name = "editFiscalData", path = "/accounts/editFiscalData.jsp")
@Forward(name = "fillFiscalInformation", path = "/accounts/fillFiscalInformation.jsp")
public class ManageAccountsDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward manageAccounts(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        request.setAttribute("searchParameters", new SearchParametersBean());
        return mapping.findForward("manageAccounts");
    }

    public ActionForward search(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        SearchParametersBean parameters = getRenderedObject("searchParameters");
        request.setAttribute("matches", parameters.search());
        return mapping.findForward("manageAccounts");
    }

    public ActionForward prepareCreatePerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        request.setAttribute("personBean", new PersonBean());
        return mapping.findForward("createPerson");
    }

    public ActionForward showExistentPersonsWithSameMandatoryDetails(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        PersonBean bean = getRenderedObject("personBean");
        Collection<Person> results = Collections.emptySet();
        if (!Strings.isNullOrEmpty(bean.getDocumentIdNumber())) {
            results = Person.findPersonByDocumentID(bean.getDocumentIdNumber());
        }
        if (!Strings.isNullOrEmpty(bean.getGivenNames()) || !Strings.isNullOrEmpty(bean.getFamilyNames())) {
            String name = Stream.of(bean.getGivenNames(), bean.getFamilyNames()).filter(n -> !Strings.isNullOrEmpty(n))
                    .collect(Collectors.joining(" "));
            Stream<Person> stream = Person.findPersonStream(name, Integer.MAX_VALUE);
            if (results.isEmpty()) {
                results = stream.collect(Collectors.toSet());
            } else {
                results.addAll(stream.collect(Collectors.toSet()));
            }
        }
        request.setAttribute("resultPersons", results);
        request.setAttribute("createPerson", bean);
        return mapping.findForward("createPerson");
    }

    public ActionForward prepareCreatePersonFillInformation(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        request.setAttribute("personBean", getRenderedObject("personBean"));
        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward prepareFillFiscalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PersonBean personBean = getRenderedObject("personBean");
        
        request.setAttribute("personBean", personBean);
        String fiscalCountryCode = "";
        
        if(personBean.isUsePhysicalAddress() && personBean.getCountryOfResidence() != null) {
            fiscalCountryCode = personBean.getCountryOfResidence().getCode();
        } else if(!personBean.isUsePhysicalAddress() && personBean.getFiscalAddressCountryOfResidence() != null) {
            fiscalCountryCode = personBean.getFiscalAddressCountryOfResidence().getCode();
        }
        
        request.setAttribute("fiscalCountryCode", fiscalCountryCode);
        
        return mapping.findForward("fillFiscalInformation");
    }

    public ActionForward fillFiscalInformationInvalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        return prepareFillFiscalInformation(mapping, actionForm, request, response);
    }

    public ActionForward fillFiscalInformationPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        PersonBean personBean = getRenderedObject("personBean");
        request.setAttribute("personBean", personBean);

        String fiscalCountryCode = null;
        if(personBean.isUsePhysicalAddress() && personBean.getCountryOfResidence() != null) {
            fiscalCountryCode = personBean.getCountryOfResidence().getCode();
        } else if(!personBean.isUsePhysicalAddress() && personBean.getFiscalAddressCountryOfResidence() != null) {
            fiscalCountryCode = personBean.getFiscalAddressCountryOfResidence().getCode();
        }
        
        request.setAttribute("fiscalCountryCode", fiscalCountryCode);
        
        RenderUtils.invalidateViewState();
        return mapping.findForward("fillFiscalInformation");
    }

    public ActionForward validateFiscalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final PersonBean personBean = getRenderedObject("personBean");

        final String socialSecurityNumber = personBean.getSocialSecurityNumber();

        Country fiscalAddressCountry = personBean.isUsePhysicalAddress() ? personBean.getCountryOfResidence() : personBean
                .getFiscalAddressCountryOfResidence();

        boolean fiscalInfoValid = TreasuryBridgeAPIFactory.implementation().isValidFiscalNumber(fiscalAddressCountry.getCode(),
                socialSecurityNumber);

        if (!fiscalInfoValid) {
            addActionMessage(request, "error.PartySocialSecurityNumber.invalid.socialSecurityNumber");
            return fillFiscalInformationInvalid(mapping, actionForm, request, response);
        }

        return createNewPerson(mapping, actionForm, request, response);
    }

    public ActionForward createNewPerson(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final PersonBean bean = getRenderedObject();
        try {
            Person person = createAccount(bean);
            SearchParametersBean searchParametersBean = new SearchParametersBean();
            searchParametersBean.setUsername(person.getUsername());
            request.setAttribute("searchParameters", searchParametersBean);
            request.setAttribute("matches", searchParametersBean.search());
            return mapping.findForward("manageAccounts");
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());
            request.setAttribute("personBean", bean);
            return mapping.findForward("createPersonFillInfo");
        }
    }

    @Atomic(mode = TxMode.WRITE)
    private Person createAccount(final PersonBean bean) {
        final Person person = new Person(bean);
        if (person.getUser() == null) {
            person.setUser(new User(person.getProfile()));
        }
        person.getAllPendingPartyContacts().forEach(partyContact -> partyContact.setValid());

        if (bean.isUsePhysicalAddress()) {

            person.editSocialSecurityNumber(bean.getSocialSecurityNumber(), person.getDefaultPhysicalAddress());

        } else if (!bean.isUsePhysicalAddress()) {

            final Country fiscalAddressCountryOfResidence = bean.getFiscalAddressCountryOfResidence();
            final String fiscalAddressAddress = bean.getFiscalAddressAddress();
            final String fiscalAddressAreaCode = bean.getFiscalAddressAreaCode();
            final String fiscalAddressParishOfResidence = bean.getFiscalAddressParishOfResidence();

            String fiscalAddressDistrictSubdivisionOfResidence = bean.getFiscalAddressDistrictSubdivisionOfResidence();
            String fiscalAddressDistrictOfResidence = bean.getFiscalAddressDistrictOfResidence();
            if (fiscalAddressCountryOfResidence.isDefaultCountry()
                    && bean.getFiscalAddressDistrictSubdivisionOfResidenceObject() != null) {
                fiscalAddressDistrictSubdivisionOfResidence =
                        bean.getFiscalAddressDistrictSubdivisionOfResidenceObject().getName();

                fiscalAddressDistrictOfResidence =
                        bean.getFiscalAddressDistrictSubdivisionOfResidenceObject().getDistrict().getName();
            }

            final PhysicalAddressData fiscalAddressData = new PhysicalAddressData();
            fiscalAddressData.setCountryOfResidence(fiscalAddressCountryOfResidence);
            fiscalAddressData.setAddress(fiscalAddressAddress);
            fiscalAddressData.setAreaCode(fiscalAddressAreaCode);
            fiscalAddressData.setParishOfResidence(fiscalAddressParishOfResidence);
            fiscalAddressData.setDistrictSubdivisionOfResidence(fiscalAddressDistrictSubdivisionOfResidence);
            fiscalAddressData.setDistrictOfResidence(fiscalAddressDistrictOfResidence);

            final PhysicalAddress fiscalAddress =
                    PhysicalAddress.createPhysicalAddress(person, fiscalAddressData, PartyContactType.PERSONAL, false);
            fiscalAddress.setValid();

            person.editSocialSecurityNumber(bean.getSocialSecurityNumber(), fiscalAddress);
        }

        return person;
    }

    public ActionForward invalid(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        PersonBean bean = getRenderedObject();
        request.setAttribute("personBean", bean);

        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward createNewPersonPostback(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        PersonBean bean = getRenderedObject();
        request.setAttribute("personBean", bean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("createPersonFillInfo");
    }

    public ActionForward viewPerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        return viewPerson(person, mapping, request);
    }

    public ActionForward viewPerson(final Person person, final ActionMapping mapping, final HttpServletRequest request) {
        final PersonBean personBean = new PersonBean(person);

        request.setAttribute("editPersonalInfo", false);
        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);

        return mapping.findForward("viewPerson");
    }

    public ActionForward prepareEditPersonalData(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = new PersonBean(person);

        request.setAttribute("editPersonalInfo", true);
        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);

        return mapping.findForward("viewPerson");
    }

    public ActionForward editPersonalDataInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject("personBean");

        request.setAttribute("editPersonalInfo", true);
        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);

        return mapping.findForward("viewPerson");
    }

    public ActionForward editPersonalDataPostback(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject("personBean");

        request.setAttribute("editPersonalInfo", true);
        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);

        RenderUtils.invalidateViewState();

        return mapping.findForward("viewPerson");
    }

    public ActionForward editPersonalData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject("personBean");

        try {
            personBean.save();

            return viewPerson(person, mapping, request);
        } catch (DomainException e) {
            addActionMessage(request, e.getMessage(), e.getArgs());

            return editPersonalDataInvalid(mapping, form, request, response);
        }
    }

    public ActionForward deletePerson(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final Person person = getDomainObject(request, "personId");

        try {
            FenixFramework.atomic(() -> {
                User user = person.getUser();
                String to = getSendingEmailAddress(person);

                if (StringUtils.isNotBlank(to)) {
                    sendLastEmail(to);
                }

                person.delete();
                if (user != null) {
                    user.delete();
                }
            });
            return manageAccounts(mapping, form, request, response);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getLocalizedMessage());

            return manageAccounts(mapping, form, request, response);
        }
    }

    public ActionForward prepareEditFiscalData(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");

        final PersonBean personBean = new PersonBean(person);

        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);

        return mapping.findForward("editFiscalData");
    }

    public ActionForward editFiscalData(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject();

        try {
            FenixFramework.atomic(() -> {
                person.editSocialSecurityNumber(personBean.getSocialSecurityNumber(), personBean.getFiscalAddress());
            });

            return viewPerson(person, mapping, request);
        } catch (DomainException e) {
            addActionMessage("error", request, e.getLocalizedMessage(), e.getArgs());

            request.setAttribute("person", person);
            request.setAttribute("personBean", personBean);

            return mapping.findForward("editFiscalData");
        }
    }

    public ActionForward editFiscalDataInvalid(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject();

        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);
        return mapping.findForward("editFiscalData");
    }

    public ActionForward editFiscalDataPostback(final ActionMapping mapping, final ActionForm form,
            final HttpServletRequest request, final HttpServletResponse response) {
        final Person person = getDomainObject(request, "personId");
        final PersonBean personBean = getRenderedObject();

        request.setAttribute("person", person);
        request.setAttribute("personBean", personBean);
        
        RenderUtils.invalidateViewState();
        
        return mapping.findForward("editFiscalData");
    }

    private String getSendingEmailAddress(Person person) {
        EmailAddress emailAddress = person.getEmailAddressForSendingEmails();
        String to = "";
        if (emailAddress != null) {
            to = emailAddress.getValue();
        }

        return to;
    }

    private void sendLastEmail(final String to) {
        SystemSender systemSender = Bennu.getInstance().getSystemSender();
        Unit institutionUnit = Bennu.getInstance().getInstitutionUnit();
        LocalizedString institutionName = institutionUnit.getNameI18n();
        List<Unit> parents = institutionUnit.getParentUnitsPath();
        Unit parentUnit = parents.isEmpty() ? null : parents.get(parents.size() - 1);
        LocalizedString parentName = new LocalizedString(new Locale("pt"), "").with(new Locale("en"), "");
        String separator = "";
        if (parentUnit != null) {
            parentName = parentUnit.getNameI18n();
            separator = " - ";
        }

        String supportEmail = PortalConfiguration.getInstance().getSupportEmailAddress();

        StringBuilder sb = new StringBuilder();
        sb.append(BundleUtil.getString(Bundle.STUDENT, new Locale("pt"), "label.account.ManageAccountsDA.email.subject"));
        sb.append("//");
        sb.append(BundleUtil.getString(Bundle.STUDENT, new Locale("en"), "label.account.ManageAccountsDA.email.subject"));
        String subject = sb.toString();

        sb = new StringBuilder();
        sb.append(BundleUtil.getString(Bundle.STUDENT, new Locale("pt"), "label.account.ManageAccountsDA.email.body",
                institutionName.getContent(new Locale("pt")) + separator + parentName.getContent(new Locale("pt")),
                supportEmail));
        sb.append("\n=================================\n");
        sb.append(BundleUtil.getString(Bundle.STUDENT, new Locale("en"), "label.account.ManageAccountsDA.email.body",
                institutionName.getContent(new Locale("en")) + separator + parentName.getContent(new Locale("en")),
                supportEmail));
        String body = sb.toString();

        new Message(systemSender, to, subject, body);
    }

}
