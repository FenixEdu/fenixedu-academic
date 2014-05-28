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
<html:xhtml/>


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
<h2><bean:message key="label.phd.manageGuidingInformation" bundle="PHD_RESOURCES" /></h2>
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
<fr:view schema="PhdIndividualProgramProcess.view.simple" name="process">
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
	<strong><bean:message key="label.phd.guiding" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="guiding">

		<%-- ### add new guiding ### --%>
		<logic:notPresent name="guidingBean">
			<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformation&amp;processId=" + processId %>">
				<bean:message key="label.add" bundle="PHD_RESOURCES" />
			</html:link>
		</logic:notPresent>

		<logic:present name="guidingBean">
			<fr:edit id="guidingBean" name="guidingBean" visible="false" />
			<bean:define id="schema" value="" />
	
			<logic:empty name="guidingBean" property="participantSelectType">
				<%-- ### select guiding type ### --%>
				<bean:define id="schema" value="PhdProgramGuidingBean.select.type" />
			</logic:empty>

			<logic:notEmpty name="guidingBean" property="participantSelectType">
			
				<%-- ### choose existing ### --%>
				<logic:equal name="guidingBean" property="participantSelectType" value="EXISTING">
					<bean:define id="schema" value="PhdProgramGuidingBean.select.existing" />
				</logic:equal>
				
				<logic:equal name="guidingBean" property="participantSelectType" value="NEW">
					<logic:empty name="guidingBean" property="participantType">
						<%-- ### select internal or external ### --%>
						<bean:define id="schema" value="PhdProgramGuidingBean.internal.or.external" />
					</logic:empty>
	
					<logic:notEmpty name="guidingBean" property="participantType">
						<bean:define id="schema">PhdProgramGuidingBean.edit.<bean:write name="guidingBean" property="participantType.name" /></bean:define>
					</logic:notEmpty>
				</logic:equal>
				
			</logic:notEmpty>
				
			<fr:edit id="guidingBean.fill.information" name="guidingBean" schema="<%= schema %>">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="columnClasses" value=",,error0" />
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
				<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationInvalid&amp;processId=" + processId %>" />
				<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddGuidingInformationSelectType&amp;processId=" + processId %>" />
			</fr:edit>

			<html:submit onclick="this.form.method.value='addGuidingInformation'"><bean:message key="label.create" bundle="PHD_RESOURCES" /></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>

		</logic:present>
	</logic:notPresent>
	
	<%-- ### view guiding ### --%>
	<logic:iterate id="guiding" name="guidings" indexId="index" >
		<bean:define id="guidingId" name="guiding" property="externalId" />
		<bean:define id="divId">g<bean:write name="guidingId" /></bean:define>
		<%-- ### simple view ### --%>
		<div id="<%= divId + "1" %>">
			<fr:view name="guiding" schema="PhdProgramGuiding.view.simple">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<%-- ### detailed view ### --%>
		<div id="<%= divId + "2" %>" style="display:none;">
			<fr:view name="guiding">
				<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdParticipant"  bundle="PHD_RESOURCES">
					<fr:slot name="nameWithTitle" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.nameWithTitle">
						<fr:property name="classes" value="bold nowrap"/>
					</fr:slot>
					<fr:slot name="qualification" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.qualification"/>
					<fr:slot name="category" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.category"/>
					<fr:slot name="workLocation" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.workLocation"/>
					<fr:slot name="institution" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.institution"/>
					<fr:slot name="address" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.address"/>
					<fr:slot name="email" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.email"/>
					<fr:slot name="phone" key="label.net.sourceforge.fenixedu.domain.phd.PhdParticipant.phone"/>
					<fr:slot name="acceptanceLetter" layout="link" />				
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<a href="#" onclick="<%= "javascript:toggle('" + divId + "1" + "');javascript:toggle('" + divId + "2')" %>"><bean:message key="label.details" bundle="PHD_RESOURCES" /></a>
		, 
		<html:link action="<%= String.format("/phdIndividualProgramProcess.do?method=prepareUploadGuidanceAcceptanceLetter&amp;processId=%s&amp;guidingId=%s", processId, guidingId) %>">
			<bean:message key="link.phd.guidance.upload.acceptance.letter" bundle="PHD_RESOURCES" />
		</html:link>
		, 
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=deleteGuiding&amp;processId=" + processId + "&amp;guidingId=" + guidingId %>" onclick="<%= confirmationMessage %>" >
			<bean:message key="label.delete" />
		</html:link>
		<br/>
	</logic:iterate>

	<%-- ### End of Guiding ### --%>

	<%-- ### Assistant Guiding ### --%>
	<br/>
	<br/>
	<strong id="assistant"><bean:message key="label.phd.assistant.guidings" bundle="PHD_RESOURCES" /></strong>
	<logic:notPresent name="assistantGuidingBean">
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformation&amp;processId=" + processId +"#assistant" %>">
			<bean:message key="label.add" bundle="PHD_RESOURCES" />
		</html:link>
	</logic:notPresent>

	<logic:present name="assistantGuidingBean">
		<fr:edit id="assistantGuidingBean" name="assistantGuidingBean" visible="false" />
		<bean:define id="schema" value="" />

		<logic:empty name="assistantGuidingBean" property="participantSelectType">
			<%-- ### select guiding type ### --%>
			<bean:define id="schema" value="PhdProgramGuidingBean.select.type" />
		</logic:empty>

		<logic:notEmpty name="assistantGuidingBean" property="participantSelectType">
		
			<%-- ### choose existing ### --%>
			<logic:equal name="assistantGuidingBean" property="participantSelectType" value="EXISTING">
				<bean:define id="schema" value="PhdProgramGuidingBean.select.existing" />
			</logic:equal>
			
			<logic:equal name="assistantGuidingBean" property="participantSelectType" value="NEW">

				<logic:empty name="assistantGuidingBean" property="participantType">
					<%-- ### select internal or external ### --%>
					<bean:define id="schema" value="PhdProgramGuidingBean.internal.or.external" />
				</logic:empty>

				<logic:notEmpty name="assistantGuidingBean" property="participantType">
					<bean:define id="schema">PhdProgramGuidingBean.edit.<bean:write name="assistantGuidingBean" property="participantType.name" /></bean:define>
				</logic:notEmpty>
			</logic:equal>
			
		</logic:notEmpty>
		
		<fr:edit id="assistantGuidingBean.fill.information" name="assistantGuidingBean" schema="<%= schema %>">
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="columnClasses" value=",,error0" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="cancel" path="<%= "/phdIndividualProgramProcess.do?method=prepareManageGuidingInformation&amp;processId=" + processId %>" />
			<fr:destination name="invalid" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationInvalid&amp;processId=" + processId %>" />
			<fr:destination name="selectType" path="<%= "/phdIndividualProgramProcess.do?method=prepareAddAssistantGuidingInformationSelectType&amp;processId=" + processId %>" />
		</fr:edit>

		<html:submit onclick="this.form.method.value='addAssistantGuidingInformation'"><bean:message key="label.create" bundle="PHD_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="label.cancel" bundle="PHD_RESOURCES" /></html:cancel>
	</logic:present>

	<%-- ### view assistant guidings ### --%>
	<logic:iterate id="assistantGuiding" name="assistantGuidings">
		<bean:define id="assistantGuidingId" name="assistantGuiding" property="externalId" />
		<bean:define id="divId">ag<bean:write name="assistantGuidingId" /></bean:define>
		<%-- ### simple view ### --%>
		<div id="<%= divId + "1" %>">
			<fr:view name="assistantGuiding" schema="PhdProgramGuiding.view.simple">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<%-- ### detailed view ### --%>
		<div id="<%= divId + "2" %>" style="display:none;">
			<fr:view name="assistantGuiding" schema="PhdProgramGuiding.view">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
				</fr:layout>
			</fr:view>
		</div>
		<a href="#" onclick="<%= "javascript:toggle('" + divId + "1" + "');javascript:toggle('" + divId + "2')" %>"><bean:message key="label.details" bundle="PHD_RESOURCES" /></a>
		,
		<html:link action="<%= String.format("/phdIndividualProgramProcess.do?method=prepareUploadGuidanceAcceptanceLetter&amp;processId=%s&amp;guidingId=%s", processId, assistantGuidingId) %>">
			<bean:message key="link.phd.guidance.upload.acceptance.letter" bundle="PHD_RESOURCES" />
		</html:link>
		, 
		<html:link action="<%= "/phdIndividualProgramProcess.do?method=deleteAssistantGuiding&amp;processId=" + processId + "&amp;assistantGuidingId=" + assistantGuidingId %>" onclick="<%= confirmationMessage %>" >
			<bean:message key="label.delete" bundle="PHD_RESOURCES" />
		</html:link>
		<br/>
	</logic:iterate>

	<%-- ### End of Assistant Guiding ### --%>

</fr:form>
<%--  ### End of Operation Area  ### --%>

