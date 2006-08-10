<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="unitAssociations" name="result" property="resultUnitAssociations"/>
	
	<!-- Action paths definitions -->
	<bean:define id="requestParameters">
		resultId=<bean:write name="result" property="idInternal"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>
	<bean:define id="createActionPath">
		/result/resultAssociationsManagement.do?method=createUnitAssociation&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="prepareEditActionPath">
		/result/resultAssociationsManagement.do?method=prepareEditUnitAssociations&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="backLink">
		/result/resultAssociationsManagement.do?method=backToResult&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="removeLink">
		/result/resultAssociationsManagement.do?method=removeUnitAssociation&<bean:write name="requestParameters"/>
	</bean:define>

	<%-- Title --%>		
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.useCaseTitle"/>: <fr:view name="result" property="title"/></h2>
	
	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>

	<%-- List of existing unit associations --%>
	<logic:empty name="unitAssociations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.emptyList"/></em></p>
	</logic:empty>
	<logic:notEmpty name="unitAssociations">
		<fr:view name="unitAssociations" layout="tabular" schema="resultUnitAssociation.summary">
			<fr:layout>
				<fr:property name="link(remove)" value="<%= removeLink %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<%-- Create new result unit association --%>
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.unitAssociations.addNewUnitAssociation"/> </h3>
	<logic:present name="bean">
		<fr:edit 	id="bean" name="bean" schema="resultUnitAssociation.create"
					action="<%= createActionPath %>">
			<fr:destination name="invalid" path="<%= prepareEditActionPath %>"/>	
			<fr:destination name="cancel" path="<%= backLink %>"/>	
		</fr:edit>
	</logic:present>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%= backLink %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>