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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="org.fenixedu.academic.domain.serviceRequests.IDeferableRequest" %>

<html:xhtml/>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript"></script>
<script type="text/javascript">
	jQuery.noConflict();

	function turnSubmitterVisible(){
		jQuery("#submitter").removeAttr('disabled');
	}
</script>

<bean:define id="academicServiceRequest" name="academicServiceRequest" type="org.fenixedu.academic.domain.serviceRequests.RegistrationAcademicServiceRequest"/>

<h2 class="mbottom1"><bean:write name="academicServiceRequest" property="description"/></h2>

<html:messages id="messages" message="true">
	<p><span class="error0"><bean:write name="messages" bundle="ACADEMIC_OFFICE_RESOURCES"/></span></p>
</html:messages>

<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="request.information"/></strong></p>
<bean:define id="simpleClassName" name="academicServiceRequest" property="class.simpleName" />
<fr:view name="academicServiceRequest" schema="<%= simpleClassName + ".view"%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
		<fr:property name="rowClasses" value=",tdhl1,,,,,,"/>
	</fr:layout>
</fr:view>


<logic:present name="academicServiceRequest" property="activeSituation">
	<p class="mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="request.situation"/></strong></p>
	<bean:define id="schema" name="academicServiceRequest" property="activeSituation.class.simpleName" />
	<fr:view name="academicServiceRequest" property="activeSituation" schema="<%= schema.toString() + ".view" %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025"/>
			<fr:property name="rowClasses" value="tdhl1,,,"/>
		</fr:layout>
	</fr:view>
</logic:present>

<bean:define id="registrationID" name="academicServiceRequest" property="registration.externalId" />
<bean:define id="academicServiceRequestId" name="academicServiceRequest" property="externalId" />

<html:form action="<%= "/academicServiceRequestsManagement.do?registrationID=" + registrationID.toString() + "&amp;academicServiceRequestId=" + academicServiceRequestId.toString() %>">
	<html:hidden property="method" value="concludeAcademicServiceRequest" />
	
	<br/>
	<strong><bean:message key="label.serviceRequests.sendEmailToStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong><html:radio property="sendEmailToStudent" value="true"><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio><html:radio property="sendEmailToStudent" value="false"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio>
	<%
	if (academicServiceRequest instanceof IDeferableRequest) {
	%>
	<br/>
	<br/>
	<br/>
	<strong><bean:message key="label.serviceRequests.deferRequest" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong><html:radio property="deferRequest" value="true" onclick="turnSubmitterVisible();"><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio><html:radio property="deferRequest" value="false" onclick="turnSubmitterVisible();"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:radio>
	<br/>
	<br/>
	<logic:notPresent name="academicServiceRequestsManagementForm" property="deferRequest">
		<html:submit styleId="submitter" disabled="true"><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</logic:notPresent>
	<%
	} else {
	%>
	<br/>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<%
	}
	%>
	<html:cancel onclick="this.form.method.value='backToViewRegistration'"><bean:message key="back" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
</html:form>
