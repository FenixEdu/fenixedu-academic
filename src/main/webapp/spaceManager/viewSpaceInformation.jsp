<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="selectedSpaceInformation">
	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>

	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<jsp:include page="spaceCrumbs.jsp"/>
	<br/>
	<br/>

	<bean:message bundle="SPACE_RESOURCES" key="link.space.information"/>
	<br/>
	<br/>
	<br/>

	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:view name="selectedSpaceInformation" schema="ViewCampusInformation"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:view name="selectedSpaceInformation" schema="ViewBuildingInformation"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:view name="selectedSpaceInformation" schema="ViewFloorInformation"/>
	</logic:equal>
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		<fr:view name="selectedSpaceInformation" schema="ViewRoomInformation"/>
	</logic:equal>
	<br/>
	<br/>
</logic:present>
