<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="SpaceFactoryEditor">
	<bean:define id="space" name="SpaceFactoryEditor" property="space" toScope="request"/>

	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message bundle="SPACE_RESOURCES" key="title.edit.space"/></h2>
		
	<jsp:include page="spaceCrumbs.jsp"/>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="SPACE_RESOURCES" key="link.create.space.information"/></strong></p>  

	<bean:define id="space" name="SpaceFactoryEditor" property="space"/>
	<bean:define id="cancelLink">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="space" property="spaceInformation.idInternal"/></bean:define>
	<bean:define id="submitLink">/manageSpaces.do?method=executeFactoryMethod&page=0&spaceInformationID=<bean:write name="space" property="spaceInformation.idInternal"/></bean:define>
	<bean:define id="invalidLink">/manageSpaces.do?method=prepareCreateSpaceInformation&page=0&spaceInformationID=<bean:write name="space" property="spaceInformation.idInternal"/></bean:define>		
		
	<fr:hasMessages type="conversion" for="editCampus">
		<p>
			<span class="error0">			
				<fr:message for="editCampus" show="message"/>
			</span>
		</p>	
	</fr:hasMessages>			
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:edit id="editCampus" name="SpaceFactoryEditor" schema="CampusFactoryEditor" action="<%= submitLink %>">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>	
			<fr:destination name="invalid" path="<%= invalidLink %>"/>					
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:equal>
	
	<fr:hasMessages type="conversion" for="editBuilding">
		<p>
			<span class="error0">			
				<fr:message for="editBuilding" show="message"/>
			</span>
		</p>	
	</fr:hasMessages>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:edit id="editBuilding" name="SpaceFactoryEditor" schema="BuildingFactoryEditor" action="<%= submitLink %>">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>	
			<fr:destination name="exception" path="<%= invalidLink %>"/>	
			<fr:destination name="invalid" path="<%= invalidLink %>"/>		
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:equal>
	
	<fr:hasMessages type="conversion" for="editFloor">
		<p>
			<span class="error0">			
				<fr:message for="editFloor" show="message"/>
			</span>
		</p>	
	</fr:hasMessages>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:edit id="editFloor" name="SpaceFactoryEditor" schema="FloorFactoryEditor" action="<%= submitLink %>">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>	
			<fr:destination name="invalid" path="<%= invalidLink %>"/>		
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>			
		</fr:edit>
	</logic:equal>
	
	<fr:hasMessages type="conversion" for="editRoom">
		<p>
			<span class="error0">			
				<fr:message for="editRoom" show="message"/>
			</span>
		</p>	
	</fr:hasMessages>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<fr:edit id="editRoom" name="SpaceFactoryEditor" schema="RoomFactoryEditor" action="<%= submitLink %>">				
			<fr:destination name="cancel" path="<%= cancelLink %>"/>	
			<fr:destination name="exception" path="<%= invalidLink %>"/>	
			<fr:destination name="invalid" path="<%= invalidLink %>"/>		
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>

</logic:present>
