<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="selectedSpaceInformation">

	<bean:define id="backLink">
		/manageBlueprints.do?method=showBlueprintVersions&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>
	</bean:define>		
	
	<logic:empty name="editBlueprint">	
		<em>Gestão de Espaços</em>		
		<h2><bean:message key="label.add.new.blueprint.version" bundle="SPACE_RESOURCES"/></h2>		

		<bean:define id="createLink">
			/manageBlueprints.do?method=createBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>
		</bean:define>	
		
		<fr:edit name="blueprintBean" id="spaceBlueprintVersion" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean" 
			schema="BlueprintSubmission.edit" action="<%= createLink %>">
			<fr:layout>
				<fr:property name="classes" value="thlight"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= backLink %>"/>
		</fr:edit>
		
	</logic:empty>
	
	<logic:notEmpty name="editBlueprint">
		<em>Gestão de Espaços</em>			
		<h2><bean:message key="label.edit.blueprint.version" bundle="SPACE_RESOURCES"/></h2>				
		<bean:define id="editLink">
			/manageBlueprints.do?method=editBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="idInternal"/>
		</bean:define>	
		
		<fr:edit name="blueprintBean" id="spaceBlueprintVersion" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean" 
			schema="BlueprintSubmission.edit" action="<%= editLink %>">
			<fr:layout>
				<fr:property name="classes" value="thlight"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= backLink %>"/>
		</fr:edit>	
		
	</logic:notEmpty>

<%--
	<p>
		<html:link page="/manageBlueprints.do?method=showBlueprintVersions&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
		</html:link>	
	</p>
--%>

</logic:present>	