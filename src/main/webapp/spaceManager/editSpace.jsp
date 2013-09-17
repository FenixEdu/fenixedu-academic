<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<logic:present name="selectedSpaceInformation">
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>

	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message bundle="SPACE_RESOURCES" key="title.edit.space"/></h2>

	<jsp:include page="spaceCrumbs.jsp"/>
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>
	
	<logic:greaterThan name="selectedSpaceInformation" property="space.resourceAllocationsCount" value="0">
		<p class="mtop15 warning0"><bean:message key="label.space.contains.allocations" bundle="SPACE_RESOURCES"/></p>
	</logic:greaterThan>
	
	<p class="mtop15 mbottom05"><strong><bean:message bundle="SPACE_RESOURCES" key="link.edit.space.information"/></strong></p>
	
	<bean:define id="invalidLink">/manageSpaces.do?method=prepareEditSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>
	<bean:define id="url" type="java.lang.String">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>

	<fr:hasMessages type="conversion" for="editCampus">
		<p>
			<span class="error0">			
				<fr:message for="editCampus" show="message"/>
			</span>
		</p>	
	</fr:hasMessages>			
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
		<fr:edit id="editCampus" name="selectedSpaceInformation" schema="EditCampusInformation" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
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
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
		<fr:edit id="editBuilding" name="selectedSpaceInformation" schema="EditBuildingInformation" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
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
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
		<fr:edit id="editFloor" name="selectedSpaceInformation" schema="EditFloorInformation" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
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
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
		
		<bean:define id="person" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" type="net.sourceforge.fenixedu.domain.Person"/>
		<%
			if(net.sourceforge.fenixedu.domain.space.Space.personIsSpacesAdministrator(person)){
		%>
		<fr:edit id="editRoom" name="selectedSpaceInformation" schema="EditRoomInformation" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>
		<%
			} else {
		%>				
		<fr:edit id="editRoom" name="selectedSpaceInformation" schema="EditRoomInformationLimited" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>
		<%
			}
		%>
	</logic:equal>
	
	<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.RoomSubdivision">		
		<bean:define id="person" name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person" type="net.sourceforge.fenixedu.domain.Person"/>		
		<fr:edit id="editRoomSubdivision" name="selectedSpaceInformation" schema="EditRoomSubdivisionInformation" action="<%= url %>">
			<fr:destination name="invalid" path="<%= invalidLink %>"/>
			<fr:destination name="exception" path="<%= invalidLink %>"/>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
			</fr:layout>
		</fr:edit>		
	</logic:equal>

</logic:present>
