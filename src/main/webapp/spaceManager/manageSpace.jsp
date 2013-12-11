<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.space.Space"%>
<%@page import="net.sourceforge.fenixedu.domain.space.AllocatableSpace"%>
<html:xhtml/>

<logic:present name="selectedSpaceInformation">
	
	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message bundle="SPACE_RESOURCES" key="title.manage.space"/></h2>
	
	<bean:define id="person" name="USER_SESSION_ATTRIBUTE" property="user.person" type="net.sourceforge.fenixedu.domain.Person"/>
	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="externalId"/></bean:define>	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	
	<div class="mbottom2">
		<jsp:include page="spaceCrumbs.jsp"/>
	</div>
	
	<bean:define id="thisSpace" name="selectedSpaceInformation" property="space" type="net.sourceforge.fenixedu.domain.space.Space"/>
				
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>
	
	<div class="mtop1 mbottom0">
	<bean:message bundle="SPACE_RESOURCES" key="label.versions"/>: 
	<logic:iterate id="spaceInformation" name="selectedSpaceInformation" property="space.orderedSpaceInformations">
		<bean:define id="spaceInformation" name="spaceInformation" toScope="request"/>
		<logic:equal name="spaceInformation" property="externalId" value="<%= selectedSpaceInformationIDString %>">
			<bean:define id="spaceInformation2" name="spaceInformation"/>
			<jsp:include page="spaceInformationVersion.jsp"/>
		</logic:equal>
		<logic:notEqual name="spaceInformation" property="externalId" value="<%= selectedSpaceInformationIDString %>">
			<html:link page="/manageSpaces.do?method=manageSpace" paramId="spaceInformationID" paramName="spaceInformation" paramProperty="externalId">
				<jsp:include page="spaceInformationVersion.jsp"/>
			</html:link>
		</logic:notEqual>
	</logic:iterate>
	</div>

	<table class="tstyle4 thleft thlight">
		<tr>
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.type"/>:
			</th>
			<td>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.floor"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Room">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.room"/>
				</logic:equal>
				<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.RoomSubdivision">
					<bean:message bundle="SPACE_RESOURCES" key="select.item.roomSubdivision"/>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>:
			</th>
			<td style="background: #fafaca;">				
				<logic:empty name="selectedSpaceInformation" property="presentationName">
				-
				</logic:empty>
				<bean:write name="selectedSpaceInformation" property="presentationName"/>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.last.version.date"/>:
			</th>
			<td>
				<bean:define id="spaceInformation" name="spaceInformation2" toScope="request"/>
				<jsp:include page="spaceInformationVersion.jsp"/>
			</td>
		</tr>
		<tr>	
			<th>
				<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>:
			</th>						
			<td>
				<bean:write name="selectedSpaceInformation" property="space.containedSpacesCount"/>
			</td>
		</tr>
<%-- 		<logic:equal name="selectedSpaceInformation" property="space.class.name" value="net.sourceforge.fenixedu.domain.space.Building"> --%>
			<tr>
				<th>
					<bean:message bundle="SPACE_RESOURCES" key="title.building.emails"/>
				</th>
				<td>	
					<bean:write name="selectedSpaceInformation" property="emails"/>
				</td>
			</tr>
<%-- 		</logic:equal> --%>
	</table>

	<%
		if(thisSpace.personHasPermissionsToManageSpace(person)) {
	%>
	<p class="mtop0 mbottom2">		
		<bean:message bundle="SPACE_RESOURCES" key="label.version"/>: 
		<html:link page="/manageSpaces.do?method=prepareEditSpace" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="label.edit"/>
		</html:link>  
		<%
			if(Space.personIsSpacesAdministrator(person)){
		%>
		|
		<html:link page="/manageSpaces.do?method=deleteSpaceInformation" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId" onclick="return confirm('Tem a certeza que deseja apagar a versão?')">
			<bean:message bundle="SPACE_RESOURCES" key="label.delete"/>
		</html:link> | 
		<html:link page="/manageSpaces.do?method=prepareCreateSpaceInformation" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="label.create"/>
		</html:link>		
		<%
			}
		%>		
		&nbsp;&nbsp; <bean:message bundle="SPACE_RESOURCES" key="label.space"/>:
		<%
			if(Space.personIsSpacesAdministrator(person)){
		%>
		<logic:equal name="selectedSpaceInformation" property="space.class.superclass.superclass.name" value="net.sourceforge.fenixedu.domain.space.AllocatableSpace">			
			<html:link page="/manageSpaces.do?method=prepareMergeRoom" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
				<bean:message bundle="SPACE_RESOURCES" key="label.merge"/>
			</html:link>
			|
		</logic:equal>
		<%
			}
		%> 
		<html:link page="/manageSpaces.do?method=prepareMoveSpace" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="label.move"/>
		</html:link> | 
		<html:link page="/manageSpaces.do?method=deleteSpace" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.externalId" onclick="return confirm('Tem a certeza que deseja apagar o espaço?')">
			<bean:message bundle="SPACE_RESOURCES" key="label.delete"/>
		</html:link>
		
		<%
			if(thisSpace.isAllocatableSpace() && ((AllocatableSpace)thisSpace).isForEducation()) { 
		%>
		&nbsp;&nbsp; <bean:message bundle="SPACE_RESOURCES" key="label.consult"/>: 
		<html:link page="/manageSpaces.do?method=viewEventSpaceOccupations" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="link.event.space.occupations"/>
		</html:link>		
		<%
			} 
		%>
	</p>	
	<%
		}
	%>
		
	<%-- BluePrints --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.recent.bluePrint"/></h3>			
		
	<logic:notEmpty name="mostRecentBlueprint">		
		
		<bean:define id="urlToViewBlueprinNumbers">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewBlueprintNumbers=true</bean:define>
		<bean:define id="urlToViewDoorNumbers">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewDoorNumbers=true</bean:define>
		<bean:define id="urlToViewIdentifications">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewSpaceIdentifications=true</bean:define>		
		<bean:define id="urlToViewOriginalSpaceBlueprint">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewOriginalSpaceBlueprint=true</bean:define>		
	
		<p>
			<bean:message bundle="SPACE_RESOURCES" key="label.show"/>: 
		   	<logic:equal name="viewSpaceIdentifications" value="false">
				<html:link page="<%= urlToViewIdentifications %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.identification"/></html:link> 	
		   	</logic:equal>
		   	<logic:equal name="viewSpaceIdentifications" value="true">	
				<span class="highlight2" style="padding: 2px;"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.identification"/></span>
			</logic:equal>								
			|
		   	<logic:equal name="viewDoorNumbers" value="false">
				<html:link page="<%= urlToViewDoorNumbers %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.door.numbers"/></html:link> 	
		   	</logic:equal>
		   	<logic:equal name="viewDoorNumbers" value="true">	
				<span class="highlight2" style="padding: 2px;"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.door.numbers"/></span>
			</logic:equal>														
			|
			<logic:equal name="viewBlueprintNumbers" value="false">
		   		<html:link page="<%= urlToViewBlueprinNumbers %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.numbers"/></html:link> 	
			</logic:equal>
			<logic:equal name="viewBlueprintNumbers" value="true">
				<span class="highlight2" style="padding: 2px;"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.numbers"/></span>
			</logic:equal>											
			|
			<logic:equal name="viewOriginalSpaceBlueprint" value="false">
		   		<html:link page="<%= urlToViewOriginalSpaceBlueprint %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.original.blueprint"/></html:link> 	
			</logic:equal>
			<logic:equal name="viewOriginalSpaceBlueprint" value="true">
				<span class="highlight2" style="padding: 2px;"><bean:message bundle="SPACE_RESOURCES" key="link.view.original.blueprint"/></span>
			</logic:equal>	   						   		
			
			<logic:notEmpty name="scalePercentage">				
			| 
				<logic:equal name="scalePercentage" value="100">
					<bean:define id="viewBlueprintWithZoom">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&amp;viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&amp;viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&amp;viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/>&amp;scalePercentage=50</bean:define>
					<img src="<%= request.getContextPath() %>/images/zoom.png"/>
					<html:link page="<%= viewBlueprintWithZoom %>">
						<bean:message key="label.zoom.out" bundle="SPACE_RESOURCES"/>
					</html:link>																		
				</logic:equal>
				<logic:notEqual name="scalePercentage" value="100">
					<bean:define id="viewBlueprintWithZoom">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&amp;viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&amp;viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&amp;viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/>&amp;scalePercentage=100</bean:define>
					<img src="<%= request.getContextPath() %>/images/zoom.png"/>
					<html:link page="<%= viewBlueprintWithZoom %>">
						<bean:message key="label.zoom.in" bundle="SPACE_RESOURCES"/>
					</html:link>																		
				</logic:notEqual>
			</logic:notEmpty>	
		</p>
				
		<logic:notEmpty name="scalePercentage">			
			<bean:define id="urlToImage"><%= request.getContextPath() %>/SpaceManager/manageBlueprints.do?method=view&amp;blueprintId=<bean:write name="mostRecentBlueprint" property="externalId"/>&amp;viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&amp;viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&amp;viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&amp;suroundingSpaceBlueprint=<bean:write name="suroundingSpaceBlueprint"/>&amp;viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/>&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;scalePercentage=<bean:write name="scalePercentage" /></bean:define>														
			<html:img src="<%= urlToImage %>" altKey="Image not available" bundle="IMAGE_RESOURCES" usemap="#roomLinksMap" style="border: 1px dotted #ccc; padding: 4px;"/>
			<map id ="roomLinksMap" name="roomLinksMap">
				<logic:present name="blueprintTextRectangles">
					<logic:iterate id="blueprintTextRectanglesEntry" name="blueprintTextRectangles">																	
						<bean:define id="blueprintSpace" name="blueprintTextRectanglesEntry" property="key" />					
						<logic:iterate id="blueprintTextRectangle" name="blueprintTextRectanglesEntry" property="value">
							<bean:define id="p1" name="blueprintTextRectangle" property="p1" />				
							<bean:define id="p2" name="blueprintTextRectangle" property="p2" />				
	 						<bean:define id="p3" name="blueprintTextRectangle" property="p3" />				
	 						<bean:define id="p4" name="blueprintTextRectangle" property="p4" />							
							<bean:define id="coords"><bean:write name="p1" property="x"/>,<bean:write name="p1" property="y"/>,<bean:write name="p2" property="x"/>,<bean:write name="p2" property="y"/>,<bean:write name="p3" property="x"/>,<bean:write name="p3" property="y"/>,<bean:write name="p4" property="x"/>,<bean:write name="p4" property="y"/></bean:define>				 				
							<bean:define id="urlToCoords"><%= request.getContextPath() %>/SpaceManager/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="blueprintSpace" property="spaceInformation.externalId"/>&amp;viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&amp;viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&amp;viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&amp;viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/></bean:define>
							<area shape="poly" coords="<%= coords %>" href="<%= urlToCoords %>"/>									
						</logic:iterate>										
					</logic:iterate>
				</logic:present>
			</map>			
		</logic:notEmpty>					
		
	</logic:notEmpty>			
	<logic:empty name="mostRecentBlueprint">																	
		<p class="mtop05"><em><bean:message key="label.empty.blueprint" bundle="SPACE_RESOURCES"/>.</em></p>										
	</logic:empty>
	<%
		if(Space.personIsSpacesAdministrator(person)){
	%>	
	<p class="mtop025">
		<html:link page="/manageBlueprints.do?method=showBlueprintVersions" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.blueprints"/>
		</html:link>
	</p>
	<%
		}
	%>

	<%-- Subspaces --%>
	<logic:present name="spaces">
		<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="title.subspaces"/></h3>
						
		<bean:define id="ViewSubSpacesStateURL">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="externalId"/>&amp;viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&amp;viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&amp;viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&amp;viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/></bean:define>		
		<fr:form action="<%= ViewSubSpacesStateURL %>">
			<fr:edit id="subSpacesStateBeanID" name="subSpacesStateBean" schema="ViewSubSpacesState" nested="true">
				<fr:destination name="postBack" path="<%= ViewSubSpacesStateURL %>"/>
				<fr:layout name="tabular">
					<fr:property name="classes" value="vamiddle thlight" />
				</fr:layout>
			</fr:edit>
		</fr:form>
		
		<bean:size id="spacesSize" name="spaces"/>
		<logic:equal name="spacesSize" value="0">
			<p class="mtop05"><em><bean:message key="label.empty.space" bundle="SPACE_RESOURCES"/>.</em></p>		
		</logic:equal>
		<logic:greaterEqual name="spacesSize" value="1">
			<table class="tstyle4 thlight mtop05 mbottom05">				
				<tr>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.type"/>
					</th>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.space.Space"/>
					</th>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="label.blueprintNumber"/>
					</th>
					<th>
						<bean:message bundle="SPACE_RESOURCES" key="title.space.number.subspaces"/>
					</th>
					<th>
					</th>
				</tr>
				<logic:iterate id="subSpace" name="spaces">
					<bean:define id="urlToManageSubspace">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="subSpace" property="spaceInformation.externalId"/></bean:define>
					<tr>
						<td>
							<logic:equal name="subSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Campus">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.campus"/>
							</logic:equal>
							<logic:equal name="subSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Building">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.building"/>
							</logic:equal>
							<logic:equal name="subSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Floor">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.floor"/>
							</logic:equal>
							<logic:equal name="subSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.Room">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.room"/>
							</logic:equal>
							<logic:equal name="subSpace" property="class.name" value="net.sourceforge.fenixedu.domain.space.RoomSubdivision">
								<bean:message bundle="SPACE_RESOURCES" key="select.item.roomSubdivision"/>
							</logic:equal>
						</td>
						<td class="acenter">							
							<logic:empty name="subSpace" property="spaceInformation.presentationName">
							-
							</logic:empty>
							<html:link page="<%= urlToManageSubspace %>">
								<bean:write name="subSpace" property="spaceInformation.presentationName"/>
							</html:link>
						</td>
						<td class="acenter">							
							<logic:empty name="subSpace" property="spaceInformation.blueprintNumber">
								-
							</logic:empty>							
							<logic:notEmpty name="subSpace" property="spaceInformation.blueprintNumber">
								<bean:write name="subSpace" property="spaceInformation.blueprintNumber"/>															
							</logic:notEmpty>
							
						</td>
						<td class="acenter">
							<bean:write name="subSpace" property="containedSpacesCount"/>
						</td>
						<td>
							<bean:define id="subSpaceToCheck" name="subSpace" type="net.sourceforge.fenixedu.domain.space.Space"/>
							<%
								if(!subSpaceToCheck.personHasPermissionsToManageSpace(person)){
							%>
							<html:link page="<%= urlToManageSubspace %>">
								<bean:message bundle="SPACE_RESOURCES" key="label.view"/>
							</html:link>
							<% 
								} else {
							%>															
							<html:link page="<%= urlToManageSubspace %>">
								<bean:message bundle="SPACE_RESOURCES" key="link.manage.space"/>
							</html:link>
							<% 
								}
							%>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:greaterEqual>
	</logic:present>
	<%
		if(thisSpace.personHasPermissionsToManageSpace(person)){
	%>	
	<p class="mtop025">
		<html:link page="/manageSpaces.do?method=showCreateSubSpaceForm" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="link.create.subspace"/>
		</html:link>
	</p>
	<%
		}
	%>

	<%-- Space Details --%>
	<bean:define id="SpaceInformationDetailsSchema">View<bean:write name="selectedSpaceInformation" property="space.class.simpleName"/>Information</bean:define>
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.space.details"/></h3>
	<fr:view name="selectedSpaceInformation" schema="<%= SpaceInformationDetailsSchema %>" layout="tabular">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight thright mvert0"/>
   		</fr:layout>
	</fr:view>
	<%	
//		if(thisSpace.personHasPermissionsToManageSpace(person)) {		 
	%>
		<p>
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
		<html:link page="/manageSpaces.do?method=exportSpaceInfoToExcel" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.externalId">
			<bean:message key="link.export.to.excel" bundle="SPACE_RESOURCES"/>						
		</html:link>
		</p>
	<%
//		}
	%>

	<%-- Responsability --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.responsible.units"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
		<fr:view schema="ViewSpaceResponsibleUnits" name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
		<p class="mtop05 mbottom025"><em><bean:message key="label.empty.responsibility" bundle="SPACE_RESOURCES"/>.</em></p>		
	</logic:empty>
	<%
		if(thisSpace.personHasPermissionsToManageSpace(person)){
	%>	
	<p class="mtop025">
		<html:link page="/manageSpaceResponsibility.do?method=showSpaceResponsibility" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.space.responsibility"/>
		</html:link>
	</p>
	<%
		}
	%>
	
	<%-- Unit Occupations --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.unit.occupations"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activeUnitSpaceOccupations">
		<fr:view schema="ViewUnitSpaceOccupations" name="selectedSpaceInformation" property="space.activeUnitSpaceOccupations">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="selectedSpaceInformation" property="space.activeUnitSpaceOccupations">
		<p class="mtop05 mbottom025"><em><bean:message key="label.empty.unitSpaceOccupations" bundle="SPACE_RESOURCES"/>.</em></p>		
	</logic:empty>
	<%
		if(thisSpace.personHasPermissionsToManageSpace(person)){
	%>	
	<p class="mtop025">
		<html:link page="/manageUnitSpaceOccupations.do?method=prepareManageUnitSpaceOccupations" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.unit.space.occupations"/>
		</html:link>
	</p>
	<%
		}
	%>
	
	<%-- Person Occupations --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.person.occupations"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<fr:view schema="PersonSpaceOccupationsWithUsername" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>	
	</logic:notEmpty>
	<logic:empty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<p class="mtop05 mbottom025"><em><bean:message key="label.empty.person.occupations" bundle="SPACE_RESOURCES"/>.</em></p>		
	</logic:empty>
	<%
		if(thisSpace.personHasPermissionToManagePersonOccupations(person) || thisSpace.personHasPermissionsToManageSpace(person)){
	%>
	<p class="mtop025"><html:link page="/managePersonSpaceOccupations.do?method=showSpaceOccupations" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.person.occupations"/>
	</html:link></p>
	<% 
		}
	%>

	<%-- Material Occupations --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.active.material.occupations"/></h3>
	<logic:notEmpty name="selectedSpaceInformation" property="space.activeSpaceMaterial">
		<fr:view schema="ViewSpaceMaterial" name="selectedSpaceInformation" property="space.activeSpaceMaterial">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>  			
	   		</fr:layout>	
		</fr:view>			
	</logic:notEmpty>
	<logic:empty name="selectedSpaceInformation" property="space.activeSpaceMaterial">
		<p class="mtop05 mbottom025"><em><bean:message key="label.empty.material.occupations" bundle="SPACE_RESOURCES"/>.</em></p>		
	</logic:empty>
	<%
		if(thisSpace.personHasPermissionToManageExtensionOccupations(person) || thisSpace.personHasPermissionsToManageSpace(person)){
	%>
	<p class="mtop025"><html:link page="/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.material.occupations"/>
	</html:link></p>
	<%
		}
	%>	
		
	<%
		if(Space.personIsSpacesAdministrator(person)){
	%>
	<%-- Access Groups --%>
	<h3 class="mtop2 mbottom0"><bean:message bundle="SPACE_RESOURCES" key="label.access.groups"/></h3>
	<p class="mtop025"><html:link page="/manageSpaces.do?method=manageAccessGroups" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="externalId">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.access.groups"/>
	</html:link></p>	
	<% 
		}
	%>
	
</logic:present>		