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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.states" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=editPersonalInformationBean" />
<%--  ### End of Error Messages  ### --%>



<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>


<%-- ### List of Process States ### --%>
<strong><bean:message key="label.phd.states" bundle="PHD_RESOURCES" /></strong>
<fr:view name="process" property="states">
	<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState" bundle="PHD_RESOURCES" >
		<fr:slot name="whenCreated" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.whenCreated" />
		<fr:slot name="stateDate" layout="null-as-label" />
		<fr:slot name="type.localizedName" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.type" />
		<fr:slot name="remarks" key="label.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.remarks" />
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
		<fr:property name="sortBy" value="whenCreated=desc" />

		<fr:link 	name="editState" 
					link="<%= String.format("/phdIndividualProgramProcess.do?method=prepareEditState&processId=%s&stateId=${externalId}", processId)  %>" 
					label="link.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.editState,PHD_RESOURCES"/>

		<fr:link 	name="removeState" 
					link="<%= String.format("/phdIndividualProgramProcess.do?method=removeLastState&processId=%s", processId)  %>" 
					label="link.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.removeState,PHD_RESOURCES"
					condition="last" 
					confirmation="message.net.sourceforge.fenixedu.domain.phd.PhdProgramProcessState.removeState,PHD_RESOURCES"/>
	</fr:layout>
</fr:view>

<%-- ### End of List of Process States ### --%>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>
<p class="mtop15 mbottom05"><strong><bean:message  key="label.phd.modify.state" bundle="PHD_RESOURCES"/></strong></p>
<fr:form action="/phdIndividualProgramProcess.do">

	<input type="hidden" name="method" value="" />
	<input type="hidden" name="processId" value="<%=processId.toString()%>" />
	<fr:edit id="processBean" name="processBean" schema="PhdIndividualProgramProcess.modify.state">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean" bundle="PHD_RESOURCES">
			<fr:slot name="processState" required="true" layout="menu-select" >
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.providers.PhdIndividualProgramProcessStateProvider" />
				<fr:property name="format" value="${localizedName}" />
			</fr:slot>
			<fr:slot name="stateDate" required="true" >
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.LocalDateValidator" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:destination name="invalid" path="/phdIndividualProgramProcess.do?method=managePhdIndividualProgramProcessStateInvalid" />
		</fr:layout>
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='changePhdIndividualProgramProcessState';">
		<bean:message bundle="PHD_RESOURCES" key="label.add"/>
	</html:submit>

</fr:form>

<%--  ### End of Operation Area  ### --%>


<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>
