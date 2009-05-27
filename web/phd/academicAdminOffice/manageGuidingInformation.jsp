<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<%-- ### Title #### --%>
<em><bean:message  key="label.phd.academicAdminOffice.breadcrumb" bundle="PHD_RESOURCES"/></em>
<h2><bean:message key="label.phd.academicAdminOffice.manageGuidingInformation" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />
<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&amp;processId=" + processId %>">
	<bean:message key="label.back" bundle="PHD_RESOURCES" />
</html:link>
<br/><br/>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>



<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<fr:form action="<%= "/phdIndividualProgramProcess.do?processId=" + processId %>">
	<input type="hidden" name="method" value="" />

	<%-- ### Guiding ### --%>
	<br/>
	<strong><bean:message key="label.phd.academicAdminOffice.guiding" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="guiding">

		<logic:notPresent name="guidingBean">
			<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformation&amp;processId=" + processId %>">
				<bean:message key="label.add" bundle="PHD_RESOURCES" />
			</html:link>
		</logic:notPresent>

		<logic:present name="guidingBean">
			<fr:edit id="guidingBean" name="guidingBean" visible="false" />

			<logic:empty name="guidingBean" property="guidingType">
				<fr:edit id="guidingBean.select.type" name="guidingBean" schema="PhdProgramGuidingBean.select.type">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thlight mtop15" />
						<fr:property name="columnClasses" value=",,error0" />
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
					<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationInvalid&amp;processId=" + processId %>" />
					<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationSelectType&amp;processId=" + processId %>" />
				</fr:edit>
			</logic:empty>

			<logic:notEmpty name="guidingBean" property="guidingType">
				<bean:define id="schema">PhdProgramGuidingBean.edit.<bean:write name="guidingBean" property="guidingType.name" /></bean:define>

				<fr:edit id="guidingBean.edit.information" name="guidingBean" schema="<%= schema.toString() %>">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thlight mtop15" />
						<fr:property name="columnClasses" value=",,error0" />
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
					<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationInvalid&amp;processId=" + processId %>" />
					<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationSelectType&amp;processId=" + processId %>" />
				</fr:edit>
			</logic:notEmpty>

			<html:submit onclick="this.form.method.value='addGuidingInformation'"><bean:message key="label.create" bundle="PHD_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>

		</logic:present>
	</logic:notPresent>
	
	<logic:present name="guiding">
		<fr:view name="guiding" schema="PhdProgramGuiding.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
			</fr:layout>
		</fr:view>
		<bean:define id="confirmationMessage" type="java.lang.String">
			return confirm('<bean:message key="label.confirmation.delete.message" bundle="PHD_RESOURCES" />')
		</bean:define>
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=deleteGuiding&amp;processId=" + processId %>" onclick="<%= confirmationMessage %>" >
			<bean:message key="label.delete" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:present>
	<%-- ### End of Guiding ### --%>

</fr:form>
<%--  ### End of Operation Area  ### --%>

</logic:present>
