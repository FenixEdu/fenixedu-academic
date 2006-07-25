<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="resultType" name="result" property="class.simpleName"/>
	<bean:define id="eventAssociations" name="result" property="resultEventAssociations"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- Title --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.useCaseTitle"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Event associations list --%>
	<logic:empty name="eventAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.emptyList"/></em></p>
	</logic:empty>	
	<logic:notEmpty name="eventAssociations">
		<fr:view name="eventAssociations" layout="tabular" schema="resultEventAssociation.summary">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/result/resultAssociationsManagement.do?method=removeEventAssociation" + 
				        									"&resultId=" + resultId + 
				        									"&resultType=" + resultType %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>	
		</fr:view>
	</logic:notEmpty>

	<%-- Create new Result Event association --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.addNewEventAssociation"/></h3>
	<%-- With existing event --%>	
	<logic:present name="simpleBean">
		<fr:edit 	id="simpleBean" name="simpleBean" schema="resultEventAssociation.simpleCreation"
					action="<%="/result/resultAssociationsManagement.do?method=createEventAssociationSimple&resultId="+resultId+"&resultType="+resultType%>">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId + "&resultType=" + resultType %>"/>		
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId + "&resultType=" + resultType %>"/>
		</fr:edit>
	</logic:present>
	<%-- With event creation --%>	
	<logic:present name="fullBean">
		<fr:edit 	id="fullBean" name="fullBean" schema="resultEventAssociation.fullCreation"
					action="<%="/result/resultAssociationsManagement.do?method=createEventAssociationFull&resultId=" + resultId + "&resultType=" + resultType %>">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId + "&resultType=" + resultType %>"/>		
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId + "&resultType=" + resultType %>"/>
		</fr:edit>
	</logic:present>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId + "&resultType=" + resultType %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView" />
	</html:link>
</logic:present>