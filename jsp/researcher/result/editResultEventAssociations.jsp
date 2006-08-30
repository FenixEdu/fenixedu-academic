<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="associations" name="result" property="resultEventAssociations"/>
	
	<!-- Action paths definitions -->
	<bean:define id="requestParameters">
		resultId=<bean:write name="result" property="idInternal"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>	
	<bean:define id="createPath">
		/result/resultAssociationsManagement.do?method=createEventAssociation&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="prepareEditPath">
		/result/resultAssociationsManagement.do?method=prepareEditEventAssociations&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="removeLink">
		/result/resultAssociationsManagement.do?method=removeEventAssociation&<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="backLink">
		/result/resultAssociationsManagement.do?method=backToResult&<bean:write name="requestParameters"/>		
	</bean:define>
	<bean:define id="cancelPath" value="<%= backLink %>"/>
	<logic:equal name="creationSchema" value="resultEventAssociation.fullCreation">
		<bean:define id="cancelPath" value="<%= prepareEditPath %>"/>
	</logic:equal>
	
	<!-- Schema definitions -->
	<bean:define id="creationSchema" name="creationSchema" type="java.lang.String"/>

	<%-- Title --%>		
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.Result.superUseCase.title"/></em>
	<h2>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.useCase.title"/>: 
		<fr:view name="result" property="title"/>
	</h2>

	<%-- Warning/Error messages --%>
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<%-- Event associations list --%>
	<h4><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.title.label"/></h4>
	<logic:empty name="associations">
		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.emptyList"/></em></p>
	</logic:empty>	
	<logic:notEmpty name="associations">
		<fr:view name="associations" schema="resultEventAssociation.summary">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2"/>
				<fr:property name="columnClasses" value=",,,acenter"/>
				
				<fr:property name="link(remove)" value="<%= removeLink %>"/>
				<fr:property name="param(remove)" value="idInternal/associationId"/>
				<fr:property name="key(remove)" value="link.remove"/>
				<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
			</fr:layout>	
		</fr:view>
	</logic:notEmpty>

	<%-- Create new Result Event association --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultEventAssociation.add"/></h3>
	<fr:edit id="bean" name="bean" schema="<%= creationSchema %>" action="<%= createPath %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="style1" />
			<fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
		<fr:destination name="exception" path="<%= prepareEditPath %>"/>		
		<fr:destination name="invalid" path="<%= prepareEditPath %>"/>		
		<fr:destination name="cancel" path="<%= cancelPath %>"/>
	</fr:edit>
	<br/>
	
	<%-- Go to previous page --%>
	<html:link page="<%= backLink %>"><bean:message bundle="RESEARCHER_RESOURCES" key="link.goBackToView"/></html:link>
</logic:present>