<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="net.sourceforge.fenixedu.domain.period.CandidacyPeriod"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyProcess"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.AverageType"%>
<%@page import="net.sourceforge.fenixedu.domain.student.curriculum.ICurriculum"%>
<html:xhtml />

<h2><bean:message key="registration.curriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<%
	final Registration registration = (Registration) request.getAttribute("registration");
	request.setAttribute("registration", registration);
%>

<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM" program="<%= registration.getDegree() %>">
<p>
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
</academic:allowed>


<div style="float: right;">
	<bean:define id="personID" name="registration" property="student.person.externalId"/>
	<html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
</div>

<p class="mvert2">
	<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
		<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
			<fr:layout name="flow">
				<fr:property name="labelExcluded" value="true"/>
			</fr:layout>
		</fr:view>
	</span>
</p>

<logic:present name="registration" property="ingression">

<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>

<logic:notPresent name="registration" property="ingression">
<h3 class="mbottom05"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>
</logic:notPresent>

<%
	if (registration.getIndividualCandidacy().getDocumentsSet() != null) {
        final IndividualCandidacy individualCandidacy = registration.getIndividualCandidacy();
		final IndividualCandidacyProcess individualCandidacyProcess = individualCandidacy.getCandidacyProcess();
		final CandidacyProcess candidacyProcess = individualCandidacyProcess.getCandidacyProcess();
		final CandidacyPeriod candidacyPeriod = candidacyProcess.getCandidacyPeriod();

%>
<h3 class="separator2">
	<%= candidacyProcess.getDisplayName() %>
	<%= candidacyPeriod.getExecutionInterval().getName() %>:
	<%= individualCandidacyProcess.getProcessCode() %>
</h3>

<div style="font-weight: bold;">
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.application.documents"/>
</div>
<table class="tstyle1 thlight mtop025">
	<tr>	
		<th>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.document"/>
		</th>
		<th>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.document.submit.date"/>
		</th>
		<th>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.document.name"/>
		</th>
		<th>
		</th>
	</tr>
	<logic:iterate id="document" name="registration" property="individualCandidacy.documentsSet" type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile">
		<tr>
			<td>
				<bean:write name="document" property="candidacyFileType.localizedName"/>
			</td>
			<td>
				<%= document.getCreationDate().toString("yyyy-MM-dd HH:mm:ss") %>
			</td>
			<td>
				<bean:write name="document" property="displayName"/>
			</td>
			<td>
				<html:link href="<%= request.getContextPath() + "/academicAdministration/viewStudentApplication.do?method=downloadDocument&documentOID=" + document.getExternalId() %>">
					<bean:message key="label.download"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
<% } %>