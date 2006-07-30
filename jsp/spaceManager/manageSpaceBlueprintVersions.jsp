<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.space.blueprints.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">

	<br/>		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/><br/>	
	
	<logic:messagesPresent message="true">
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<br/>
	</logic:messagesPresent>	
	
	<logic:notEmpty name="selectedSpaceBlueprint">					
		<bean:define id="selectedSpaceBlueprintId" name="selectedSpaceBlueprint" property="idInternal" />
		<logic:iterate id="blueprint" name="selectedSpaceInformation" property="space.orderedBlueprints">
			<bean:define id="blueprint" name="blueprint" toScope="request"/>								
			<logic:equal name="blueprint" property="idInternal" value="<%= selectedSpaceBlueprintId.toString() %>">
				<jsp:include page="spaceBlueprintVersions.jsp"/>
			</logic:equal>
			<logic:notEqual name="blueprint" property="idInternal" value="<%= selectedSpaceBlueprintId.toString() %>">
				<bean:define id="versionLink">
					/manageBlueprints.do?method=showBlueprintVersions&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="blueprint" property="idInternal"/> 
				</bean:define>
				<html:link page="<%= versionLink %>">
					<jsp:include page="spaceBlueprintVersions.jsp"/>
				</html:link>
			</logic:notEqual>					
		</logic:iterate>	
		<br/>
		<br/>
		
		<bean:define id="directDownloadUrlFormat" name="selectedSpaceBlueprint" property="blueprintFile.directDownloadUrlFormat"/>
		<html:img align="middle" src="<%= directDownloadUrlFormat.toString() %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />			
		<br/><br/>
		
		<bean:define id="editLink">
			/manageBlueprints.do?method=prepareEditBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="idInternal"/> 
		</bean:define>
		<html:link page="<%= editLink %>">
			<bean:message key="link.edit.space.information" bundle="SPACE_RESOURCES"/>
		</html:link>&nbsp;	
		
		<bean:define id="deleteLink">
			/manageBlueprints.do?method=deleteBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="idInternal"/> 
		</bean:define>
		<html:link page="<%= deleteLink %>">
			<bean:message key="link.delete.space.information" bundle="SPACE_RESOURCES"/>
		</html:link>&nbsp;											
				
	</logic:notEmpty>
	
	<logic:empty name="selectedSpaceBlueprint">
		<span class="error"><!-- Error messages go here -->
			<bean:message key="label.space.no.blueprints" bundle="SPACE_RESOURCES"/>
		</span>	
		<br/><br/>
	</logic:empty>
	
	<html:link page="/manageBlueprints.do?method=prepareCreateBlueprintVersion&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message key="link.edit.space.create.new.version" bundle="SPACE_RESOURCES"/>
	</html:link>
	&nbsp;									
	<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformationId">
		<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link>	
			
</logic:present>
