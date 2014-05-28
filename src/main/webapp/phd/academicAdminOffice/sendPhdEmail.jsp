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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.academicAdminOffice.PhdIndividualProgramProcessDA" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean" %>

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="emailBean" name="emailBean" />
<bean:define id="process" name="emailBean" property="process" />
<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<p>
	<html:link action="<%= "/phdIndividualProgramProcess.do?method=preparePhdEmailsManagement&amp;processId=" + processId.toString() %>">
		« <bean:message key="label.back" bundle="PHD_RESOURCES" />
	</html:link>
</p>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%-- ### Operational Area ### --%>

<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" >
</script>

<fr:form action="<%= String.format("/phdIndividualProgramProcess.do?method=sendPhdEmail&processId=%s", processId.toString()) %>">

	<!--
	<strong><bean:message key="label.phd.email.to.send" bundle="PHD_RESOURCES" />:</strong>
	-->
	
	<fr:edit id="emailBean" name="emailBean" visible="false" />

<style>


ul ul {
margin-bottom: 5px !important;
margin-left: 20px !important;
}

/*
div.compose-email table th { width: 150px; }
div.compose-email table td { width: 700px; }
*/

.hide-theader th {
display: none;
}
.hide-theader td {
padding: 0;
background: none;
border: none;
width: auto !important;
}


.recipients div {
float: left;
width: 325px;
margin: 5px 0 10px 0;
}

div.compose-email table .col1 { width: 150px; }
div.compose-email table .col2 { width: 700px; }


</style>





<div class="compose-email">
	<table class="tstyle5 thlight thright mtop05 mbottom0 ulnomargin ">
		<tr>
			<th class="col1">Destinatários (grupos):</th>
			<td class="col2 recipients">
			
				
				<logic:iterate id="group" name="emailBean" property="possibleParticipantsGroups" indexId="i">
					
				
					<div id='<%= "checkbox-" + i %>' class="hide-theader">
						
						<p>
							<b><fr:view name="group" property="groupLabel" ></fr:view></b>
				
							<bean:define id="groupName" value='<%= group.getClass().getSimpleName() %>'/>
					
							(<html:link href="#" onclick="<%= "$('div#checkbox-" + i + " input[type=checkbox]').attr('checked','true')" %>">
								Seleccionar todos
							</html:link>)
						</p>
					
						<fr:edit id='<%= "emailBean.groups.edit." + group.toString() %>' name="emailBean">
							<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean">
								<fr:slot name="selectedParticipants" layout="option-select">
									<fr:property name="providerClass" value='<%= group.getClass().getName() %>' />
									<fr:property name="eachSchema" value="PhdParticipant.view.name.with.title"/>
							        <fr:property name="eachLayout" value="values"/>
									<fr:property name="classes" value="nobullet noindent" />
									<fr:property name="saveOptions" value="true"/>					
								</fr:slot>
							</fr:schema>
							
							<fr:layout name="list">
								<fr:property name="classes" value="participant-groups"/>
								<fr:property name="columnClasses" value=",,tdclear tderror1"/>
								<fr:property name="nullLabel" value="" />
							</fr:layout>
						
						</fr:edit>
					
					</div>
			
				</logic:iterate>
	
			
		
			</td>
		</tr>

	</table>

	<fr:edit id="emailBean.individuals" name="emailBean" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean">
			<fr:slot name="bccs" bundle="MESSAGING_RESOURCES" key="label.receiversOfCopy" validator="net.sourceforge.fenixedu.presentationTier.Action.phd.validator.EmailListValidator">
				<fr:property name="size" value="60" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert0 tgluetop"/>
			<fr:property name="columnClasses" value="col1,col2,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<fr:edit id="emailBean.template" name="emailBean" >
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean">
			<fr:slot name="template" layout="menu-postback" >
				<fr:property name="destination" value="template-postBack"/>
				<fr:property name="layout" value="phd-enum-renderer" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mvert0 tgluetop"/>
			<fr:property name="columnClasses" value="col1,col2,tdclear tderror1"/>
		</fr:layout>
		
		<fr:destination name="template-postBack" path='<%="/phdIndividualProgramProcess.do?method=sendEmailPostback&processId=" + processId.toString() %>' />
	</fr:edit>
	
	<fr:edit id="emailBean.create" name="emailBean" >
		
		<fr:schema bundle="PHD_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean">
			<fr:slot name="subject" bundle="MANAGER_RESOURCES" key="label.email.subject" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="size" value="60" />
			</fr:slot>
			<fr:slot name="message" bundle="MANAGER_RESOURCES" key="label.email.message" layout="longText" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="columns" value="80"/>
				<fr:property name="rows" value="10"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop0 tgluetop"/>
			<fr:property name="columnClasses" value="col1,col2,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
			
	</fr:edit>

</div> <!-- compose-email -->
  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>	
</fr:form>

