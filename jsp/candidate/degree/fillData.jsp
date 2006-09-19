<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="currentFormPosition" name="operation" property="currentFormPosition" />
<bean:define id="totalForms" name="operation" property="totalForms" />
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

<fr:form action="<%="/degreeCandidacyManagement.do?method=processForm&currentFormPosition=" + currentFormPosition%>">

	<bean:define id="isInputForm" name="currentForm" property="input" />
	<bean:define id="formClass" name="currentForm" property="class.simpleName" />
	<bean:define id="candidacyID" name="candidacy" property="idInternal" />
	<bean:define id="schemaSuffix" name="schemaSuffix" />
	
	<input type="hidden" name="candidacyID" value="<%=candidacyID%>" />
	
	<fr:edit id="operation-view-state" visible="false" name="operation" />
	
	<logic:equal name="isInputForm" value="true">
		<fr:edit id="<%=editViewStateId%>"
			name="currentForm"
			schema="<%=formClass.toString() + schemaSuffix%>">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright" />
				<fr:property name="columnClasses" value=",,tdclear tdwarning" />
			</fr:layout>
			<fr:destination name="invalid" path="<%="/degreeCandidacyManagement.do?method=showCurrentForm&currentFormPosition=" + currentFormPosition%>"/>
		</fr:edit>	
	</logic:equal>
	<logic:equal name="isInputForm" value="false">
		<fr:edit id="<%=editViewStateId%>"
			name="currentForm" visible="false">
		</fr:edit>	
	</logic:equal>	
		
	<html:submit ><bean:message key="button.next" /></html:submit>
	
</fr:form>

