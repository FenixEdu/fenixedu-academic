<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="merge.space.title" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="role(SPACE_MANAGER)">

	<logic:notEmpty name="spaceBean">
		
		<bean:define id="space" name="spaceBean" property="space" toScope="request"/>	
		<bean:define id="selectedSpace" name="spaceBean" property="space" toScope="request"/>	
		
		<div class="mbottom2">
			<jsp:include page="spaceCrumbs.jsp"/>
		</div>		
		
		<bean:define id="backLink">/manageSpaces.do?method=prepareMergeRoom&amp;spaceInformationID=<bean:write name="spaceBean" property="space.spaceInformation.externalId"/></bean:define>		
		<ul class="mvert15 list5">
			<li>
				<html:link page="<%= backLink %>">
					<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
				</html:link>
			</li>
		</ul>
		
		<p class="mtop2 mbottom05"><b><bean:message key="label.from.room.important.associations" bundle="SPACE_RESOURCES"/></b></p>
		<fr:view name="spaceBean" property="space" schema="ViewDestinationRoomInfo">					
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width200px,,"/>
				<fr:property name="rowClasses" value="bold,,,,," />	
            </fr:layout>		         							
		</fr:view>
		<fr:view name="spaceBean" property="space" schema="ViewRoomImportantAssociationsBeforeMerge">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright"/>
				<fr:property name="columnClasses" value="width200px,,"/>
			</fr:layout>
		</fr:view>
		
		<p class="mtop1 mbottom05"><b><bean:message key="label.destination.room.important.associations" bundle="SPACE_RESOURCES"/></b></p>
		<fr:view name="spaceBean" property="selectedParentSpace" schema="ViewDestinationRoomInfo">					
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
				<fr:property name="columnClasses" value="width200px,,"/>
				<fr:property name="rowClasses" value="bold,,,,," />		
            </fr:layout>		         							
		</fr:view>
		<fr:view name="spaceBean" property="selectedParentSpace" schema="ViewRoomImportantAssociationsBeforeMerge">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright"/>
				<fr:property name="columnClasses" value="width200px,,"/>
			</fr:layout>
		</fr:view>
						
		<bean:define id="mergeLink">/manageSpaces.do?method=mergeRooms&fromRoomID=<bean:write name="spaceBean" property="space.externalId"/>&destinationRoomID=<bean:write name="spaceBean" property="selectedParentSpace.externalId"/></bean:define>				
		<fr:form action="<%= mergeLink %>" >
			<fr:edit id="mergeRoomsBean" visible="false" name="spaceBean" />
			<html:submit><bean:message key="merge.space.title" bundle="SPACE_RESOURCES"/></html:submit>
		</fr:form>			
						
	</logic:notEmpty>

</logic:present>