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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<script type='text/javascript'>

    $(document).ready(function() {

        $("input[id$=\":otherRecipientsEmails\"]").prop({'type': 'email', 'multiple': 'multiple'});

        $("input[id$=\":toSendEmail\"]").change(function() {
        	var emails = $("input[id$=\":otherRecipientsEmails\"]");
        	if(this.checked) {
				emails.removeAttr('disabled');
			}
        	else {
				emails.prop('disabled', true);
				emails.val('');
			}
        });

        $("input[id$=\":toSendEmail\"]").trigger("change");

    });

</script>

<bean:define id="processId" name="process" property="externalId" />


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.createCustomAlert" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="/phdIndividualProgramProcess.do?method=manageAlerts" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=createCustomAlertBean" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:edit id="createCustomAlertBean"
	name="createCustomAlertBean"
	schema="PhdCustomAlertBean.edit"
	action="<%="/phdIndividualProgramProcess.do?method=createCustomAlert&processId=" + processId.toString()%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
		<fr:destination name="invalid" path="<%="/phdIndividualProgramProcess.do?method=prepareCreateCustomAlert&processId=" + processId.toString()%>" />
		<fr:destination name="cancel" path="<%="/phdIndividualProgramProcess.do?method=manageAlerts&processId=" + processId.toString()%>" />
	</fr:layout>
	
</fr:edit>


<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
