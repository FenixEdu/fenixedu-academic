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

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.edit.state" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<bean:define id="processId" name="process" property="externalId" />

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<%--
<div class="breadcumbs">
	<span class="actual">Step 1: Step Name</span> > 
	<span>Step N: Step name </span>
</div>
--%>
<html:link action="<%= String.format("/phdThesisProcess.do?method=manageStates&processId=%s", processId) %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>


<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=" />
<%--  ### End of Error Messages  ### --%>



<bean:define id="stateId" name="bean" property="state.externalId" />
<bean:define id="processId" name="bean" property="phdProcess.externalId" />

<fr:form action="<%= String.format("/phdThesisProcess.do?method=editState&processId=%s&stateId=%s", processId, stateId) %>" >
	<fr:edit id="bean" name="bean" visible="false" />
	
	<fr:edit id="bean-edit" name="bean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessStateBean" bundle="PHD_RESOURCES" >
			<fr:slot name="stateDate" required="true">
				<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.DateTimeValidator" />
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular" >
			<fr:property name="columnClasses" value=",,tderror1" />
		</fr:layout>
		
		<fr:destination name="invalid" path = "<%= String.format("/phdThesisProcess.do?method=editStateInvalid&processId=%s&stateId=%s", processId, stateId) %>"/>
		<fr:destination name="cancel" path = "<%= String.format("/phdThesisProcess.do?method=manageStates&processId=%s", processId) %>"/>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	</p>
</fr:form> 
