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
<h2><bean:message key="label.phd.manage.emails" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>

<p><jsp:include page="createEmailStepsBreadcrumb.jsp?step=3"></jsp:include></p>

<p class="mvert15"><strong><bean:message key="label.phd.coordinator.email.confirmEmail" bundle="PHD_RESOURCES"/></strong></p>


<style>

	table.xpto2 {
		width: auto !important;
		border-collapse: collapse;
	}
	table.xpto2 td {
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
	
</style>



<bean:define id="phdProgramId" name="phdEmailBean" property="phdProgram.externalId" />

<fr:form id="emailForm" action="<%="/phdIndividualProgramProcess.do?phdProgramId=" + phdProgramId.toString() %>">
	<input type="hidden" id="methodId" name="method" value="prepareSendPhdEmail"/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="true"/>

	<fr:edit id="phdEmailBean" name="phdEmailBean" visible="false" />
	
	<fr:view name="phdEmailBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmailBean.class.getName() %>">
			<fr:slot name="creator.name"/>
			<fr:slot name="creationDate" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thtop thright mvert0 width800px" />
			<fr:property name="columnClasses" value="width150px,,"/>
		</fr:layout>
	</fr:view>
	
	<table class="tstyle2 thlight thright mvert0 tgluetop mbottom0 ulnomargin width800px">
		<tr>
			<th class="width150px">Destinatários:</th>
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
						<fr:property name="classes" value="xpto2 thdnone" />
					</fr:layout>
				</fr:view>
				
			</div>
		
			</td>
		</tr>
	</table>
	
	<fr:view name="phdEmailBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= PhdProgramEmailBean.class.getName() %>">
			<fr:slot name="bccs"/>
			<fr:slot name="subject" />
			<fr:slot name="message" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mvert0 tgluetop width800px" />
			<fr:property name="columnClasses" value="width150px,,"/>
		</fr:layout>
		
	</fr:view>

	<p class="mtop15">
	  	<html:submit bundle="PHD_RESOURCES" altKey="label.submitEmail" onclick="<%= "javascript:document.getElementById('skipValidationId').value='false';javascript:document.getElementById('methodId').value='sendPhdEmail';javascript:document.getElementById('emailForm').submit();" %>">
			<bean:message bundle="PHD_RESOURCES" key="label.submitEmail" />
		</html:submit>
	
	 	<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="this.form.method.value='prepareSendPhdEmail';">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
		</html:submit>
	</p>

</fr:form> 

</logic:present>