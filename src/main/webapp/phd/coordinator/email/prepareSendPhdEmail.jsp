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

<html:xhtml/>

<%@page import="net.sourceforge.fenixedu.domain.phd.email.PhdProgramEmailBean" %>
<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess" %>

<logic:present role="role(COORDINATOR)">


<%-- ### Title #### --%>
<h2><bean:message key="label.phd.manage.emails.create" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%-- <html:link action="/phdIndividualProgramProcess.do?method=choosePhdEmailRecipients" paramId="phdProgramId" paramName="phdEmailBean" paramProperty="phdProgram.externalId">
	« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>--%>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>
<p><jsp:include page="createEmailStepsBreadcrumb.jsp?step=2"></jsp:include></p>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<style>
	ul ul {
		margin-bottom: 5px !important;
		margin-left: 20px !important;
	}
	
	div.compose-email table th {  }
	div.compose-email table td {  }
	
	table td.xpto table {
		width: auto !important;
		border-collapse: collapse;
	}
	table td.xpto table td {
		border: none;
		white-space: nowrap;
		padding-right: 20px;
		width: 10px !important;
	}
	
	table td.xpto div {
		overflow-y: scroll;
		overflow-x: visible;
		height: 200px;
		width: auto;
	}
	
	.tderror1 {
	white-space: normal !important;
	}
</style>

<bean:define id="phdProgramId" name="phdEmailBean" property="phdProgram.externalId"/>


<fr:form id="emailForm" action="<%="/phdIndividualProgramProcess.do?phdProgramId=" + phdProgramId.toString() %>">
	<input type="hidden" id="methodId" name="method" value="choosePhdEmailRecipients"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="true"/>
	
	<fr:edit id="phdEmailBean" name="phdEmailBean" visible="false" />
			
	<div class="compose-email">
		
		<table class="tstyle5 thlight thright mtop05 mbottom0 ulnomargin width1000px">
			<tr>
				<th style="width: 150px;">Destinatários:</th>
				<td class="xpto">
	
					<div>
	
					<fr:view name="phdEmailBean" property="selectedElements">
						<fr:schema bundle="PHD_RESOURCES" type="<%= PhdIndividualProgramProcess.class.getName() %>">
							<fr:slot name="phdIndividualProcessNumber.number"/>
							<fr:slot name="person.name" />
							<fr:slot name="executionYear.year"/>
							<fr:slot name="phdProgram.acronym" />
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="thdnone"/>
							<fr:property name="columnClasses" value=""/>
							<fr:property name="nullLabel" value="" />
						</fr:layout>
					</fr:view>
					
					</div>
			
				</td>
				<td class="width150px tdclear tderror1"></td>
			</tr>

		</table>

	
		<fr:edit id="phdEmailBean.individuals" name="phdEmailBean" >
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmailBean.class.getName() %>">
				<fr:slot name="bccs" bundle="MESSAGING_RESOURCES" key="label.receiversOfCopy" validator="net.sourceforge.fenixedu.presentationTier.Action.phd.validator.EmailListValidator">
					<fr:property name="size" value="50" />
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mvert0 tgluetop width1000px thnowrap"/>
				<fr:property name="columnClasses" value="width150px,,width150px tdclear tderror1 "/>
			</fr:layout>
		</fr:edit> 
		
		<fr:edit id="phdEmailBean.create" name="phdEmailBean" >
			
			<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmailBean.class.getName() %>">
				<fr:slot name="subject" bundle="MANAGER_RESOURCES" key="label.email.subject" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="message" bundle="MANAGER_RESOURCES" key="label.email.message" layout="longText" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="columns" value="70"/>
					<fr:property name="rows" value="10"/>
				</fr:slot>
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mvert0 tgluetop width1000px thnowrap"/>
				<fr:property name="columnClasses" value="width150px,,width150px tdclear tderror1"/>
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
				
		</fr:edit>

	
	  	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="<%= "javascript:document.getElementById('skipValidationId').value='false';javascript:document.getElementById('methodId').value='confirmSendPhdEmail';javascript:document.getElementById('emailForm').submit();" %>">
			<bean:message bundle="PHD_RESOURCES" key="label.continue" />
		</html:submit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='choosePhdEmailRecipients';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
		</html:submit>
	  	
  	</div>
  	
</fr:form> 




</logic:present>