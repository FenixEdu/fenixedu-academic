<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.1" prefix="str"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="org.fenixedu.academic.domain.Person"%>
<%@page import="org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice" %>
<%@page import="org.fenixedu.academic.report.academicAdministrativeOffice.DegreeFinalizationCertificate"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.joda.time.LocalDate"%>

<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />

<html:xhtml />

<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration"/>
<bean:define id="registrationConclusionBean" name="registrationConclusionBean" type="org.fenixedu.academic.dto.student.RegistrationConclusionBean"/>

<table width="90%">
	<tr>
		<td align="center">
		<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document"/></h2>
		<br />
		<br />
		<br />
		</td>
	<tr>
		<td><b><bean:write name="degreePresentationName"/></b></td>
	<tr>
		<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.the.student"/> <b><bean:write name="registration" property="person.name"/></b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.on.registration.number"/><b><bean:write name="registration" property="number"/></b></td>
	<tr>
		<td>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.concluded.lowercase"/>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.degree.of"/>
			<bean:write name="registration" property="degreeType.name.content"/> 
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.metioned.above.with.following.units"/>:
			<br/>
			<br/>
		</td>
	</tr>
</table>


	<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
		<fr:layout>
			<fr:property name="visibleCurricularYearEntries" value="false" />
			<fr:property name="visibleEctsConvertedGrade" value="true" />
		</fr:layout>
	</fr:view>


<%
	request.setAttribute("degreeFinalizationDate", registrationConclusionBean.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", I18N.getLocale()));
	request.setAttribute("finalGrade", registrationConclusionBean.getFinalGrade());
	request.setAttribute("degreeFinalizationGrade", DegreeFinalizationCertificate.getDegreeFinalizationGrade(registrationConclusionBean.getFinalGrade()));
	request.setAttribute("degreeFinalizationEcts", String.valueOf(registrationConclusionBean.getEctsCredits()));
	request.setAttribute("creditsDescription", registration.getDegreeType().getCreditsDescription());
	
	AdministrativeOffice office = registration.getDegree().getAdministrativeOffice();
	final Person administrativeOfficeCoordinator = office.getCoordinator().getPerson();
	request.setAttribute("administrativeOfficeCoordinator", administrativeOfficeCoordinator);
	request.setAttribute("administrativeOfficeCoordinatorGender", administrativeOfficeCoordinator.isMale() ? "" : "a");
	request.setAttribute("administrativeOfficeName", office.getUnit().getName());
	request.setAttribute("day", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", I18N.getLocale()));
%>

<table class="apura-final mtop2" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="6" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.average.atribution"/></td>
	</tr>
	<tr>
		<td style="padding: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.org.fenixedu.academic.dto.student.RegistrationConclusionBean.average"/></td>
		<td style="padding: 5px;"><bean:write name="registrationConclusionBean" property="rawGrade.value"/></td>		
		
		<logic:equal name="registration" property="degreeType" value="MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.degree.coordinator"/>,</td>
		</logic:equal>
	</tr>
	<tr>
		<td style="padding: 5px;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="final.degree.average"/></td>
		<td style="padding: 5px;"><bean:write name="finalGrade" property="value"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="values"/></td>
		<td style="padding: 5px; padding-left: 100px;">	</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;"></td>
		<logic:equal name="registration" property="degreeType" value="MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;">_____________________________</td>
		</logic:equal>
	</tr>
</table>

<br/>

<table class="apura-final mtop2" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="2" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.information"/></td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;">
			<p class="apura-pt9">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.concluded.uppercase"/>  
				o <bean:write name="registrationConclusionBean" property="conclusionDegreeDescription"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.in"/> 
				<bean:write name="degreeFinalizationDate"/><bean:write name="degreeFinalizationGrade"/>, 
				tendo obtido o total de <bean:write name="degreeFinalizationEcts"/><bean:write name="creditsDescription"/>.
			</p>
		</td>
	</tr>
	<tr>
		<td style="text-align: center;">
			Conferido em <bean:write name="day"/>
		</td>
		<td style="text-align: center;">
			<div class="homologo">Homologo,</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td style="text-align: center;">_____________________________</td>
		<td style="text-align: center;">_____________________________</td>
	</tr>
	<tr>
		<td style="text-align: center;"></td>
		<td style="text-align: center;"><bean:write name="administrativeOfficeCoordinator" property="name"/></td>
	</tr>
	<tr>
		<td style="text-align: center;"></td>
		<td style="text-align: center;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.coordinator"/><bean:write name="administrativeOfficeCoordinatorGender"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.of.the.hermaphrodite"/> <bean:write name="administrativeOfficeName"/></td>
	</tr>
</table>
