<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="selectedSpaceInformation">

	<bean:define id="backLink">
		/manageBlueprints.do?method=showBlueprintVersions&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>
	</bean:define>		
	
	<logic:empty name="editBlueprint">	
		<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
		<h2><bean:message key="label.add.new.blueprint.version" bundle="SPACE_RESOURCES"/></h2>		

		<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>		
		<div class="mbottom2">
			<jsp:include page="spaceCrumbs.jsp"/>
		</div>

		<bean:define id="createLink">
			/manageBlueprints.do?method=createBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>
		</bean:define>	
		
		<fr:edit name="blueprintBean" id="spaceBlueprintVersion" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean" 
			schema="BlueprintSubmission.edit" action="<%= createLink %>">
			<fr:layout>
				<fr:property name="classes" value="thlight mbottom1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= backLink %>"/>
		</fr:edit>
		
	</logic:empty>
	
	<logic:notEmpty name="editBlueprint">
		<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>			
		<h2><bean:message key="label.edit.blueprint.version" bundle="SPACE_RESOURCES"/></h2>				
	
		<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>	
		<div class="mbottom2">
			<jsp:include page="spaceCrumbs.jsp"/>
		</div>
	
		<bean:define id="editLink">
			/manageBlueprints.do?method=editBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="externalId"/>
		</bean:define>	
		
		<fr:edit name="blueprintBean" id="spaceBlueprintVersion" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean" 
			schema="BlueprintSubmission.edit" action="<%= editLink %>">
			<fr:layout>
				<fr:property name="classes" value="thlight mbottom1"/>
			</fr:layout>
			<fr:destination name="cancel" path="<%= backLink %>"/>
		</fr:edit>	
		
	</logic:notEmpty>

<%--
	<p>
		<html:link page="/manageBlueprints.do?method=showBlueprintVersions&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
		</html:link>	
	</p>
--%>

</logic:present>	