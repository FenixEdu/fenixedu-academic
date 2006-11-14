<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.space.blueprints.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">
	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<div>
		<jsp:include page="spaceCrumbs.jsp"/>
	</div>
	
	<bean:define id="space" name="selectedSpaceInformation" property="space"/>
	<ul class="mvert15 list5">
		<li>	
			<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformationId">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageBlueprints.do?method=prepareCreateBlueprintVersion&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
				<bean:message key="link.edit.space.create.new.version" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>	
	
	<logic:notEmpty name="selectedSpaceBlueprint">
		<div class="mvert15">
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
		</div>
		
		<bean:define id="url"><%= request.getContextPath() %>/SpaceManager/manageBlueprints.do?method=view&blueprintId=<bean:write name="selectedSpaceBlueprintId"/>&viewBlueprintNumbers=true</bean:define>
		<p>
			<html:img src="<%= url %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />
		</p>
		
		<bean:define id="downloadLink"><bean:write name="selectedSpaceBlueprint" property="blueprintFile.directDownloadUrlFormat"/> </bean:define>
		
		<a href="<%= downloadLink %>"> 		
			<bean:message key="link.download.blueprint" bundle="SPACE_RESOURCES"/>
		</a>,
		
		<bean:define id="editLink">
			/manageBlueprints.do?method=prepareEditBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="idInternal"/> 
		</bean:define>
		<html:link page="<%= editLink %>">
			<bean:message key="link.edit.space.information" bundle="SPACE_RESOURCES"/>
		</html:link>, 
		
		<bean:define id="deleteLink">
			/manageBlueprints.do?method=deleteBlueprintVersion&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&spaceBlueprintID=<bean:write name="selectedSpaceBlueprint" property="idInternal"/> 
		</bean:define>
		<html:link page="<%= deleteLink %>">
			<bean:message key="link.delete.space.information" bundle="SPACE_RESOURCES"/>
		</html:link>, 											
				
	</logic:notEmpty>
	
	<logic:empty name="selectedSpaceBlueprint">
		<p>
			<em><!-- Error messages go here -->
				<bean:message key="label.space.no.blueprints" bundle="SPACE_RESOURCES"/>
			</em>
		</p>
	</logic:empty>
			
</logic:present>
