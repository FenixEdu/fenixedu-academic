<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultId" name="result" property="idInternal" />
	<bean:define id="eventAssociations" name="result" property="resultEventAssociations"/>

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	
	<%-- Title --%>		
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.useCaseTitle"/></h2>
	
	<%-- Event associations list --%>	
	<logic:notEmpty name="eventAssociations">
		<fr:edit 	id="associationsTable" name="eventAssociations" layout="tabular-editable" schema="resultEventAssociations.edit-role"
					action="<%= "/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= "/result/resultAssociationsManagement.do?method=removeEventAssociation&resultId=" + resultId %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="researcher.result.editResult.event.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>	
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId %>"/>
		</fr:edit>
	</logic:notEmpty>
	<br/>

	<%-- Create new event association --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.eventAssociations.addNewEventAssociation"/></h3>
	<logic:present name="simpleBean">
		<fr:edit id="simpleBean" name="simpleBean" action="<%="/result/resultAssociationsManagement.do?method=createEventAssociationSimple&resultId=" + resultId %>" schema="resultEventAssociation.simpleCreation">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId %>"/>		
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>"/>
		</fr:edit>
	</logic:present>
	<logic:present name="fullBean">
		<fr:edit id="fullBean" name="fullBean" action="<%="/result/resultAssociationsManagement.do?method=createEventAssociationFull&resultId=" + resultId %>" schema="resultEventAssociation.fullCreation">
			<fr:destination name="invalid" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId %>"/>		
			<fr:destination name="cancel" path="<%="/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&resultId=" + resultId %>"/>
		</fr:edit>
	</logic:present>
	
	<br/>
	<html:link page="<%="/result/resultAssociationsManagement.do?method=backToResult&resultId=" + resultId %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView" />
	</html:link>
</logic:present>