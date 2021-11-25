<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<bean:define id="registration" name="registration" scope="request" type="org.fenixedu.academic.domain.student.Registration"/>

<table class="tstyle4 thright thlight table">
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.startDate" /></th>
        <td><%= registration.getStartDate() == null ? "-" : registration.getStartDate().toString("yyyy-MM-dd") %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.number" /></th>
        <td><%= registration.getNumber() == null ? "-" : registration.getNumber() %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.degree" /></th>
        <td><%= registration.getDegreeNameWithDescription() %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.currentState" /></th>
        <td><%= registration.getActiveStateType().getDescription() %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.registrationAgreement" /></th>
        <td><%= registration.getRegistrationProtocol().getDescription().getContent() %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.numberEnroledCurricularCoursesInCurrentYear" /></th>
        <td><%= registration.getNumberEnroledCurricularCoursesInCurrentYear() %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.homologationDate" /></th>
        <td><%= registration.getHomologationDate() == null ? "-" : registration.getHomologationDate().toString("yyyy-MM-dd") %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studiesStartDate" /></th>
        <td><%= registration.getStudiesStartDate() == null ? "-" : registration.getStudiesStartDate().toString("yyyy-MM-dd") %></td>
    </tr>
    <% if (registration.getIngressionType() != null) { %>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.ingression" /></th>
        <td><%= registration.getIngressionType() == null ? "-" : (registration.getIngressionType().getCode() + " - " + registration.getIngressionType().getDescription().getContent()) %></td>
    </tr>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.entryPhase" /></th>
        <td><%= registration.getEntryPhase() == null ? "-" : registration.getEntryPhase().getLocalizedName() %></td>
    </tr>
    <% } %>
    <tr>
        <th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payment.plan" /></th>
        <td><%= registration.getEventTemplate() == null ? "-" : registration.getEventTemplate().getTitle().getContent() %></td>
    </tr>
</table>
