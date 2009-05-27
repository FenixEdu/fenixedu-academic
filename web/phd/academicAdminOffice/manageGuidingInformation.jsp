<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>


<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<script type="text/javascript">
function toggle(obj) {
	var el = document.getElementById(obj);
	if ( el.style.display != 'none' ) {
		el.style.display = 'none';
	}
	else {
		el.style.display = '';
	}
}
</script>


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

	<bean:define id="confirmationMessage" type="java.lang.String">
		return confirm('<bean:message key="label.confirmation.delete.message" bundle="PHD_RESOURCES" />')
	</bean:define>

	<%-- ### Guiding ### --%>
	<br/>
	<strong><bean:message key="label.phd.academicAdminOffice.guiding" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="guiding">

		<%-- ### add new guiding ### --%>
		<logic:notPresent name="guidingBean">
			<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformation&amp;processId=" + processId %>">
				<bean:message key="label.add" bundle="PHD_RESOURCES" />
			</html:link>
		</logic:notPresent>

		<logic:present name="guidingBean">
			<fr:edit id="guidingBean" name="guidingBean" visible="false" />

			<%-- ### select guiding type ### --%>
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

			<%-- ### fill information ### --%>
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
	
	<%-- ### view guiding ### --%>
	<logic:present name="guiding">
		<div id="g1">
			<fr:view name="guiding" schema="PhdProgramGuiding.view.simple">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<div id="g2" style="display:none;">
			<fr:view name="guiding" schema="PhdProgramGuiding.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<a href="#" onclick="javascript:toggle('g1');javascript:toggle('g2')"><bean:message key="label.details" bundle="PHD_RESOURCES" /></a>
		, <html:link action="<%= "/phdIndividualProgramProcess.do?method=deleteGuiding&amp;processId=" + processId %>" onclick="<%= confirmationMessage %>" >
			<bean:message key="label.delete" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:present>

	<%-- ### End of Guiding ### --%>

	<%-- ### Assistant Guiding ### --%>
	<br/>
	<br/>
	<br/>
	<strong id="assistant"><bean:message key="label.phd.academicAdminOffice.assistant.guidings" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="assistantGuidingBean">
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformation&amp;processId=" + processId +"#assistant" %>">
			<bean:message key="label.add" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:notPresent>

	<logic:present name="assistantGuidingBean">
		<fr:edit id="assistantGuidingBean" name="assistantGuidingBean" visible="false" />

			<%-- ### select guiding type ### --%>
			<logic:empty name="assistantGuidingBean" property="guidingType">
				<fr:edit id="assistantGuidingBean.select.type" name="assistantGuidingBean" schema="PhdProgramGuidingBean.select.type">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thlight mtop15" />
						<fr:property name="columnClasses" value=",,error0" />
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
					<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationInvalid&amp;processId=" + processId %>" />
					<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationSelectType&amp;processId=" + processId %>" />
				</fr:edit>
			</logic:empty>

			<%-- ### fill information ### --%>
			<logic:notEmpty name="assistantGuidingBean" property="guidingType">
				<bean:define id="schema">PhdProgramGuidingBean.edit.<bean:write name="assistantGuidingBean" property="guidingType.name" /></bean:define>

				<fr:edit id="assistantGuidingBean.edit.information" name="assistantGuidingBean" schema="<%= schema.toString() %>">
					<fr:layout name="tabular-editable">
						<fr:property name="classes" value="tstyle2 thlight mtop15" />
						<fr:property name="columnClasses" value=",,error0" />
						<fr:property name="requiredMarkShown" value="true" />
					</fr:layout>
					<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
					<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationInvalid&amp;processId=" + processId %>" />
					<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationSelectType&amp;processId=" + processId %>" />
				</fr:edit>
			</logic:notEmpty>

			<html:submit onclick="this.form.method.value='addAssistantGuidingInformation'"><bean:message key="label.create" bundle="PHD_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
	</logic:present>

	<%-- ### view assistant guidings ### --%>
	<logic:iterate id="assistantGuiding" name="assistantGuidings" indexId="index" >
		<bean:define id="divId">ag<bean:write name="index" /></bean:define>
		<div id="<%= divId + "1" %>">
			<fr:view name="assistantGuiding" schema="PhdProgramGuiding.view.simple">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<div id="<%= divId + "2" %>" style="display:none;">
			<fr:view name="assistantGuiding" schema="PhdProgramGuiding.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<a href="#" onclick="<%= "javascript:toggle('" + divId + "1" + "');javascript:toggle('" + divId + "2')" %>"><bean:message key="label.details" bundle="PHD_RESOURCES" /></a>
		<bean:define id="assistantGuidingId" name="assistantGuiding" property="externalId" />
		, <html:link action="<%= "/phdIndividualProgramProcess.do?method=deleteAssistantGuiding&amp;processId=" + processId + "&amp;assistantGuidingId=" + assistantGuidingId %>" onclick="<%= confirmationMessage %>" >
			<bean:message key="label.delete" bundle="PHD_RESOURCES" />
		</html:link>
		<br/>
		<br/>
	</logic:iterate>

	<%-- ### End of Assistant Guiding ### --%>

</fr:form>
<%--  ### End of Operation Area  ### --%>

</logic:present>
