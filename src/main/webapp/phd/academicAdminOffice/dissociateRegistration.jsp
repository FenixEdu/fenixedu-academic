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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manageAlerts" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<%-- Registration Details --%>
<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:present name="process" property="registration.ingression">
<fr:view name="process" property="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight"/>
		<fr:property name="rowClasses" value=",,,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="process" property="registration.ingression">
<fr:view name="process" property="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
		<fr:property name="rowClasses" value=",,,,,,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<strong class="mtop2 mbottom05"><bean:message key="message.phd.dissociate.registration.confirmation" bundle="PHD_RESOURCES"/></strong>

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=dissociateRegistration&processId=" + processId.toString() %>" >
	<fr:edit id="something" name="something" visible="false" >
		<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>"/>
	</fr:edit>
	
	<html:submit ><bean:message key="button.yes" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="button.no" bundle="APPLICATION_RESOURCES" /></html:cancel>
</fr:form>
