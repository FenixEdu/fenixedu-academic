<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
		<span class="error">
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>
		
	<logic:notEmpty name="blueprint">
		<div class="infoop2">
			<p><b><bean:message key="label.current.blueprint.version" bundle="SPACE_RESOURCES"/>:</b>&nbsp;		
			<bean:write name="blueprint" property="validFrom"/></p> 	
		</div>		
		<br/>
	</logic:notEmpty>	
	<logic:notEmpty name="directDownloadUrlFormat">
		<bean:define id="directDownloadUrlFormat" name="directDownloadUrlFormat" />
		<html:img align="middle" src="<%= directDownloadUrlFormat.toString() %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />			
		<br/>
	</logic:notEmpty>

	<bean:define id="backLink">
		/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>	
	
	<h3><bean:message key="label.add.new.version" bundle="SPACE_RESOURCES"/>:</h3>
	<fr:edit name="blueprintBean" id="spaceBlueprintVersion" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.CreateBlueprintSubmissionBean" 
		schema="BlueprintSubmission.edit" action="<%="/manageBlueprints.do?method=submitBlueprint&spaceInformationID" + selectedSpaceInformationId.toString()%>">
		<fr:destination name="cancel" path="<%= backLink %>"/>
	</fr:edit>

	<br/>	
	<html:link page="<%= backLink %>">
		<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link>	
			
</logic:present>
