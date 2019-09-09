<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java"%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="currentFormPosition" name="operation" property="currentFormPosition" />
<bean:define id="totalForms" name="operation" property="totalForms" type="java.lang.Integer"/>
<h2>
	<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/> (<bean:write name="currentFormPosition"/>/<bean:write name="totalForms"/>)
</h2>

<h3 class="mtop15">
	<bean:message name="currentForm" property="formName" />
</h3>

<logic:notEmpty name="currentForm" property="formDescription">
	<div class="mvert1">
		<bean:message name="currentForm" property="formDescription" />
	</div>
</logic:notEmpty>


<logic:present name="formMessages">
	<ul>
	<logic:iterate id="formMessage" name="formMessages">
		<li><span class="error0"><bean:write name="formMessage"/></span></li>
	</logic:iterate>
	</ul>		
</logic:present>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<bean:define id="editViewStateId" value="<%="fillData" + currentFormPosition%>"></bean:define>
	
<fr:hasMessages for="<%=editViewStateId%>" type="conversion">
	<ul>
	<fr:messages>
		<li><span class="error0"><fr:message/></span></li>
	</fr:messages>
	</ul>
</fr:hasMessages>

<logic:equal name="currentFormPosition" value="1">
	<%@include file="/candidate/degree/firstTimeCandidacyStep1ExtraInfo.jsp"%>
</logic:equal>

<logic:equal name="currentFormPosition" value="7">
	<%@include file="/candidate/degree/firstTimeCandidacyStep7ExtraInfo.jsp"%>
</logic:equal>

<fr:form id="candidacyForm" action="<%="/degreeCandidacyManagement.do?method=processForm&currentFormPosition=" + currentFormPosition%>">

	<bean:define id="isInputForm" name="currentForm" property="input" />
	<bean:define id="schemaName" name="currentForm" property="schemaName" type="java.lang.String" />
	<bean:define id="candidacyID" name="candidacy" property="externalId" />
	
	<input alt="input.candidacyID" type="hidden" name="candidacyID" value="<%=candidacyID%>" />
	
	<fr:edit id="operation-view-state" visible="false" name="operation" />
	
	<logic:equal name="isInputForm" value="true">
		<fr:edit id="<%=editViewStateId%>"
			name="currentForm"
			schema="<%=schemaName%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight thwidth inobullet" />
				<fr:property name="columnClasses" value="width250px,,tdclear tderror1" />
			</fr:layout>
			<fr:destination name="invalid" path="<%="/degreeCandidacyManagement.do?method=showCurrentForm&currentFormPosition=" + currentFormPosition%>"/>
			<fr:destination name="districtSelectionPostback" path="<%="/degreeCandidacyManagement.do?method=showCurrentForm&postback=true&currentFormPosition=" + currentFormPosition%>"/>
			<fr:destination name="countrySelectionPostback" path="<%="/degreeCandidacyManagement.do?method=showCurrentForm&postback=true&country=true&currentFormPosition=" + currentFormPosition%>"/>			
			<fr:destination name="institutionPostBack" path="<%="/degreeCandidacyManagement.do?method=showCurrentForm&postback=true&currentFormPosition=" + currentFormPosition%>"/>
		</fr:edit>	
	</logic:equal>
	<logic:equal name="isInputForm" value="false">
		<fr:edit id="<%=editViewStateId%>"
			name="currentForm" visible="false">
		</fr:edit>	
	</logic:equal>

	<input type='submit' name='myButton' value='<bean:message key="button.next" />' onclick="return checkForm();"/>

</fr:form>

<script type="text/javascript">

	function checkForm()
	{
		var candidacyForm = document.forms["candidacyForm"];
		candidacyForm.myButton.disabled = true;
		candidacyForm.myButton.value = '<bean:message key="please.wait" />';
		candidacyForm.submit();
		return true;
	}

</script>