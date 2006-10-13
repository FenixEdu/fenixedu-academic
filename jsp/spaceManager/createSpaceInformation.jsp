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

	<bean:define id="cancelLink">
		/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="space" property="spaceInformation.idInternal"/>
	</bean:define>
		
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:edit name="SpaceFactoryEditor" schema="CampusFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:equal>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:edit name="SpaceFactoryEditor" schema="BuildingFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>	
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:equal>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:edit name="SpaceFactoryEditor" schema="FloorFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod">
			<fr:destination name="cancel" path="<%= cancelLink %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>
	<logic:equal name="SpaceFactoryEditor" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<fr:edit name="SpaceFactoryEditor" schema="RoomFactoryEditor"
				action="/manageSpaces.do?method=executeFactoryMethod">				
			<fr:destination name="cancel" path="<%= cancelLink %>"/>	
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>
	</logic:equal>

</logic:present>
