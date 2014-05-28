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
	<h2><bean:message key="label.phd.editPhdThesisProcessInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
	<html:link action="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>">
		<bean:message bundle="PHD_RESOURCES" key="label.back"/>
	</html:link>
	
	<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=phdThesisProcessBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.thesisProcess" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdThesisProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdThesisProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />

	<strong><bean:message key="label.phd.editPhdThesisProcessInformation.details" bundle="PHD_RESOURCES" /></strong>
	<fr:edit id="phdThesisProcessBean" name="phdThesisProcessBean" schema="PhdThesisProcessBean.editDetails">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="columnClasses" value=",,error0" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
	
		<fr:destination name="invalid" path="<%= "/phdThesisProcess.do?method=editPhdThesisProcessInformationInvalid&amp;processId=" + processId %>" />
		<fr:destination name="cancel" path="<%= "/phdThesisProcess.do?method=viewIndividualProgramProcess&processId=" + processId %>" />
	</fr:edit>

	
	<html:submit onclick="this.form.method.value='editPhdThesisProcessInformation'"><bean:message key="label.edit" bundle="PHD_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>

</fr:form>

<%--  ### End of Operation Area  ### --%>
