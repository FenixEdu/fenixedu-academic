<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="selectedSpaceInformation">
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/>
	<br/>

	<H2><bean:message bundle="SPACE_RESOURCES" key="link.edit.space"/></H2>
	<br/>
	<H3><bean:message bundle="SPACE_RESOURCES" key="link.edit.space.edit.version"/></H3>
	<br/>

	<bean:define id="url" type="java.lang.String">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>

	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:edit name="selectedSpaceInformation" schema="CampusInformation" action="<%= url %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:edit name="selectedSpaceInformation" schema="BuildingInformation" action="<%= url %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:edit name="selectedSpaceInformation" schema="FloorInformation" action="<%= url %>"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<fr:edit name="selectedSpaceInformation" schema="RoomInformation" action="<%= url %>"/>
	</logic:equal>
	<br/>
	<br/>
</logic:present>
