<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<h2><bean:message  key="label.candidacy.candidacyDetails"/></h2>
<hr/>

<logic:equal name="candidacy" property="activeCandidacySituation.candidacySituationType.name" value="REGISTERED">
	<fr:view name="candidacy" schema="DegreeCandidacy.view-with-person-details">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
</logic:equal>

<logic:notEqual name="candidacy" property="activeCandidacySituation.candidacySituationType.name" value="REGISTERED">
	<fr:view name="candidacy" schema="DegreeCandidacy.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
</logic:notEqual>

<bean:define id="emptyOperations">
	<bean:write name="operations" property="empty" />
</bean:define>

<logic:notEmpty name="operations">
	<bean:define id="emptyOperations" value="true" />
	<logic:iterate id="operation" name="operations">
		<logic:equal name="operation" property="visible" value="true">
		
			<bean:define id="emptyOperations" value="false" />
			<bean:define id="operationType" name="operation" property="type.name" />
			<bean:define id="candidacyID" name="candidacy" property="idInternal" />
			
			<logic:equal name="operationType" value="PRINT_SCHEDULE">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			<logic:equal name="operationtype" value="PRINT_REGISTRATION_DECLARATION">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			<logic:equal name="operationType" value="PRINT_SYSTEM_ACCESS_DATA">
				<bean:define id="requiresNewWindow" value="true" />
			</logic:equal>
			
			<logic:present name="requiresNewWindow">
				<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&operationType=" + operationType + "&candidacyID=" + candidacyID%>" target="_blank">
					<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
				</html:link>
			</logic:present>
			<logic:notPresent name="requiresNewWindow">
				<html:link action="<%= "/degreeCandidacyManagement.do?method=doOperation&operationType=" + operationType + "&candidacyID=" + candidacyID%>">
					<bean:message name="operation" property="type.qualifiedName" bundle="ENUMERATION_RESOURCES"/>
				</html:link>
			</logic:notPresent>
			<br/>
		</logic:equal>
	</logic:iterate>
</logic:notEmpty>


<logic:equal name="emptyOperations" value="true">
	<span class="error0"><bean:message  key="label.candidacy.candidacyDetails.noOperationsToBeDone"/> </span>
</logic:equal>


<br/><br/>

<logic:equal name="candidacy" property="activeCandidacySituation.candidacySituationType" value="REGISTERED">
	<bean:define id="istUsername" name="person" property="istUsername" />
	<div class="warning0">
		<p><strong><bean:message  key="label.attention"/></strong></p>
		<p><span><bean:message  key="label.candidacy.institutional.email.creation.warning" arg0="<%=istUsername.toString()%>"/></span></p>
	</div>
</logic:equal>



