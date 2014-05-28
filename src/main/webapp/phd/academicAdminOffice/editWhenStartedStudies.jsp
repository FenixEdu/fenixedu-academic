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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.academicAdminOffice.PhdProgramCandidacyProcessDA"%><html:xhtml/>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.edit.when.started.studies" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<bean:define id="processId" name="process" property="externalId" />

<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<fr:form action="<%= "/phdIndividualProgramProcess.do?method=editWhenStartedStudies&processId=" + processId.toString() %>">
  	
  	<fr:edit id="registrationFormalizationBean" name="registrationFormalizationBean" visible="false" /> 
  	
	<fr:edit id="bean.whenStartedStudies" name="registrationFormalizationBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= RegistrationFormalizationBean.class.getName() %>">
			<fr:slot name="whenStartedStudies" required="true" />
		</fr:schema>

		<fr:layout name="layout">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
	</fr:edit>
	
	<%--  ### Buttons (e.g. Submit)  ### --%>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
	<%--  ### End of Buttons (e.g. Submit)  ### --%>

</fr:form>

<%--  ### End of Operation Area  ### --%>
