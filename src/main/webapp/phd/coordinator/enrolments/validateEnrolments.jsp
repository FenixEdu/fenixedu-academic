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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers.PhdManageEnrolmentsExecutionSemestersProvider"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers.ExistingEnrolmentsToValidate"%>
<%@page import="pt.ist.fenixWebFramework.renderers.validators.EmailValidator"%>

<logic:present role="role(COORDINATOR)">

<h2><bean:message key="label.phd.validate.enrolments" bundle="PHD_RESOURCES" /></h2>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<html:link action="/phdIndividualProgramProcess.do?method=manageEnrolments" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<br/>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<br/>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="manageEnrolments" />

	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean" visible="false" />

	<strong><bean:message key="label.phd.enrolments.performed.by.student.to.approve" bundle="PHD_RESOURCES" /></strong>
	<logic:notEmpty name="manageEnrolmentsBean" property="enrolmentsPerformedByStudent">

		<fr:edit id="manageEnrolmentsBean-select" name="manageEnrolmentsBean">
	
			<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
				<fr:slot name="enrolmentsToValidate" layout="option-select" required="true">
					<fr:property name="classes" value="nobullet noindent" />
						
					<fr:property name="providerClass" value="<%= ExistingEnrolmentsToValidate.class.getName() %>" />
					
					<fr:property name="eachLayout" value="values" />
					<fr:property name="eachSchema" value="Enrolment.view.name" />
	
					<fr:property name="sortBy" value="name" />
					<fr:property name="selectAllShown" value="true" />
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop10" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="sortBy" value="name=asc" />
			</fr:layout>
		</fr:edit>
		
		<br/>
		<br/>
		<strong><bean:message key="label.phd.email.to.send" bundle="PHD_RESOURCES" />:</strong>
		<fr:edit id="manageEnrolmentsBean-email" name="manageEnrolmentsBean">
	
			<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
				<fr:slot name="mailSubject" required="true">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="mailBody" layout="longText" required="true">
					<fr:property name="rows" value="15" />
					<fr:property name="columns" value="100" />
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		
		<p>
			<strong>Notas:</strong> Além do texto referido, irá ser acrescentada informação com as disciplinas aceites ou rejeitadas
		</p>
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='acceptEnrolments';"><bean:message bundle="PHD_RESOURCES" key="label.phd.accept.enrolments"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='rejectEnrolments';"><bean:message bundle="PHD_RESOURCES" key="label.phd.reject.enrolments"/></html:submit>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"><bean:message bundle="PHD_RESOURCES" key="label.back"/></html:cancel>	

	</logic:notEmpty>
	
	<logic:empty name="manageEnrolmentsBean" property="enrolmentsPerformedByStudent">
		<em><bean:message key="label.phd.no.enrolments.found" bundle="PHD_RESOURCES" /></em>
	</logic:empty>

</fr:form>

</logic:present>
