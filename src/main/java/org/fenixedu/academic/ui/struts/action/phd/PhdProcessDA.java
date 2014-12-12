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
package org.fenixedu.academic.ui.struts.action.phd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.caseHandling.Activity;
import org.fenixedu.academic.domain.caseHandling.Process;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.academic.dto.contacts.PendingPartyContactBean;
import org.fenixedu.academic.dto.person.PersonBean;
import org.fenixedu.academic.service.services.caseHandling.ExecuteProcessActivity;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixframework.FenixFramework;

abstract public class PhdProcessDA extends PhdDA {

    static protected final Pattern AREA_CODE_REGEX = Pattern.compile("\\d{4}-\\d{3}");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final Process process = getProcess(request);
        if (process != null) {
            request.setAttribute("processId", process.getExternalId());
            request.setAttribute("process", process);
        }

        final Person loggedPerson = getLoggedPerson(request);
        if (loggedPerson != null) {
            request.setAttribute("alertMessagesToNotify", loggedPerson.getUnreadedPhdAlertMessages());
            request.setAttribute("isTeacher", RoleType.TEACHER.isMember(loggedPerson.getUser())
                    || !loggedPerson.getProfessorshipsSet().isEmpty());
        }

        return super.execute(mapping, actionForm, request, response);
    }

    /**
     * First read value from request attribute to allow overriding of processId
     * value
     */
    protected PhdProgramProcess getProcess(HttpServletRequest request) {
        final String processIdAttribute = (String) request.getAttribute("processId");
        return FenixFramework.getDomainObject(processIdAttribute != null ? processIdAttribute : (String) request
                .getParameter("processId"));
    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activity, Object activityParameter,
            HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward) {
        return executeActivity(activity, activityParameter, request, mapping, errorForward, sucessForward, null);
    }

    protected ActionForward executeActivity(Class<? extends Activity<? extends Process>> activityClass, Object activityParameter,
            HttpServletRequest request, ActionMapping mapping, String errorForward, String sucessForward, String sucessMessage,
            String... sucessMessageArgs) {

        try {

            ExecuteProcessActivity.run(getProcess(request), activityClass.getSimpleName(), activityParameter);

            if (!StringUtils.isEmpty(sucessMessage)) {
                addSuccessMessage(request, sucessMessage, sucessMessageArgs);
            }

            return mapping.findForward(sucessForward);

        } catch (DomainException e) {
            addErrorMessage(request, e.getKey(), e.getArgs());

            return mapping.findForward(errorForward);
        }
    }

    protected boolean validateAreaCodeAndAreaOfAreaCode(HttpServletRequest request, final Person person, Country country,
            String areaCode, String areaOfAreaCode) {

        if (country.isDefaultCountry() && !StringUtils.isEmpty(areaCode) && !AREA_CODE_REGEX.matcher(areaCode).matches()) {
            addErrorMessage(request, "error.areaCode.invalidFormat.for.national.address");

            return false;
        }

        if (!StringUtils.isEmpty(areaCode) && StringUtils.isEmpty(areaOfAreaCode)) {
            addErrorMessage(request, "error.areaOfAreaCode.is.required.if.areaCode.is.specified");

            return false;
        }

        return true;
    }

    protected String getMessageFromResource(final String key, String... args) {
        return BundleUtil.getString(Bundle.PHD, key, args);
    }

    protected String getZipDocumentsFilename(PhdIndividualProgramProcess process) {
        return process.getProcessNumber().replace("/", "-") + "-Documents.zip";
    }

    protected byte[] createZip(final Collection<PhdProgramProcessDocument> documents) throws IOException {
        return PhdDocumentsZip.zip(documents);
    }

    protected byte[] createZip(HttpServletRequest request) throws IOException {
        return PhdDocumentsZip.zip(getProcess(request).getLatestDocumentVersions());
    }

    protected byte[] createGuidanceDocumentsZip(HttpServletRequest request) throws IOException {
        return PhdDocumentsZip.zip(new ArrayList<PhdProgramProcessDocument>(((PhdIndividualProgramProcess) getProcess(request))
                .getLatestGuidanceDocumentVersions()));
    }

    protected void initPersonBeanUglyHack(final PersonBean personBean, Person person) {
        personBean.setGivenNames(person.getGivenNames());
        personBean.setFamilyNames(person.getFamilyNames());
        personBean.setUsername(person.getUsername());
        personBean.setGender(person.getGender());
        personBean.setMaritalStatus(person.getMaritalStatus());
        personBean.setFatherName(person.getNameOfFather());
        personBean.setMotherName(person.getNameOfMother());
        personBean.setProfession(person.getProfession());
        personBean.setNationality(person.getCountry());

        personBean.setCountryOfBirth(person.getCountryOfBirth());
        personBean.setDateOfBirth(person.getDateOfBirthYearMonthDay());
        personBean.setParishOfBirth(person.getParishOfBirth());
        personBean.setDistrictOfBirth(person.getDistrictOfBirth());
        personBean.setDistrictSubdivisionOfBirth(person.getDistrictSubdivisionOfBirth());

        personBean.setDocumentIdEmissionDate(person.getEmissionDateOfDocumentIdYearMonthDay());
        personBean.setDocumentIdEmissionLocation(person.getEmissionLocationOfDocumentId());
        personBean.setDocumentIdExpirationDate(person.getExpirationDateOfDocumentIdYearMonthDay());
        personBean.setDocumentIdNumber(person.getDocumentIdNumber());
        personBean.setIdDocumentType(person.getIdDocumentType());
        personBean.setSocialSecurityNumber(person.getSocialSecurityNumber());

        PendingPartyContactBean pendingPartyContactBean = new PendingPartyContactBean(person);
        if (pendingPartyContactBean.getDefaultPhysicalAddress() != null) {
            final PhysicalAddress physicalAddress = pendingPartyContactBean.getDefaultPhysicalAddress();
            personBean.setAddress(physicalAddress.getAddress());
            personBean.setArea(physicalAddress.getArea());
            personBean.setAreaCode(physicalAddress.getAreaCode());
            personBean.setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
            personBean.setParishOfResidence(physicalAddress.getParishOfResidence());
            personBean.setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
            personBean.setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
            personBean.setCountryOfResidence(physicalAddress.getCountryOfResidence());
        }

        personBean.setPhone(pendingPartyContactBean.getDefaultPhone() != null ? pendingPartyContactBean.getDefaultPhone()
                .getNumber() : null);
        personBean.setMobile(pendingPartyContactBean.getDefaultMobilePhone() != null ? pendingPartyContactBean
                .getDefaultMobilePhone().getNumber() : null);

        if (pendingPartyContactBean.getDefaultEmailAddress() != null) {
            personBean.setEmail(pendingPartyContactBean.getDefaultEmailAddress().getValue());
        }

        personBean.setEmailAvailable(person.getAvailableEmail());
        personBean.setHomepageAvailable(person.getAvailableWebSite());

        personBean.setPerson(person);
    }

}
