<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.space.Space"%>
<html:xhtml/>

<logic:present name="selectedSpaceInformation">
	
	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message bundle="SPACE_RESOURCES" key="title.manage.space"/></h2>
	
	<bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>
	<bean:define id="selectedSpaceInformationIDString" type="java.lang.String"><bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>	
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
		<logic:equal name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
			<bean:define id="spaceInformation2" name="spaceInformation"/>
			<jsp:include page="spaceInformationVersion.jsp"/>
		</logic:equal>
		<logic:notEqual name="spaceInformation" property="idInternal" value="<%= selectedSpaceInformationIDString %>">
			<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="spaceInformation" paramProperty="idInternal">
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
	</table>
		 
	<%
		if(thisSpace.personHasPermissionsToManageSpace(person)){
	%>
	<p class="mtop0 mbottom2">
		<html:link page="/manageSpaces.do?method=prepareEditSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.edit.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=deleteSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal" onclick="return confirm('Tem a certeza que deseja apagar a versão?')">
			<bean:message bundle="SPACE_RESOURCES" key="link.delete.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=prepareCreateSpaceInformation&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.create.space.information"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=prepareMoveSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.move.space"/>
		</html:link>&nbsp;,
		<html:link page="/manageSpaces.do?method=deleteSpace&page=0" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.idInternal" onclick="return confirm('Tem a certeza que deseja apagar o espaço?')">
			<bean:message bundle="SPACE_RESOURCES" key="link.delete.space"/>
		</html:link>			
	</p>	
	<%
		}
	%>

	<%-- BluePrints --%>	
	<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="label.recent.bluePrint"/></h3>			
		
	<logic:notEmpty name="mostRecentBlueprint">		
		
		<bean:define id="urlToViewBlueprinNumbers">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&viewBlueprintNumbers=true</bean:define>
		<bean:define id="urlToViewDoorNumbers">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&viewDoorNumbers=true</bean:define>
		<bean:define id="urlToViewIdentifications">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&viewSpaceIdentifications=true</bean:define>		
		<bean:define id="urlToViewOriginalSpaceBlueprint">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>&viewOriginalSpaceBlueprint=true</bean:define>		
	
		(	
		   	<logic:equal name="viewSpaceIdentifications" value="false">
				<html:link page="<%= urlToViewIdentifications %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.identification"/></html:link> 	
		   	</logic:equal>
		   	<logic:equal name="viewSpaceIdentifications" value="true">	
				<bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.identification"/>
			</logic:equal>								
			|
		   	<logic:equal name="viewDoorNumbers" value="false">
				<html:link page="<%= urlToViewDoorNumbers %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.door.numbers"/></html:link> 	
		   	</logic:equal>
		   	<logic:equal name="viewDoorNumbers" value="true">	
				<bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.door.numbers"/>
			</logic:equal>														
			|
			<logic:equal name="viewBlueprintNumbers" value="false">
		   		<html:link page="<%= urlToViewBlueprinNumbers %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.numbers"/></html:link> 	
			</logic:equal>
			<logic:equal name="viewBlueprintNumbers" value="true">
					<bean:message bundle="SPACE_RESOURCES" key="link.view.blueprint.numbers"/>
			</logic:equal>											
			|
			<logic:equal name="viewOriginalSpaceBlueprint" value="false">
		   		<html:link page="<%= urlToViewOriginalSpaceBlueprint %>"><bean:message bundle="SPACE_RESOURCES" key="link.view.original.blueprint"/></html:link> 	
			</logic:equal>
			<logic:equal name="viewOriginalSpaceBlueprint" value="true">
				<bean:message bundle="SPACE_RESOURCES" key="link.view.original.blueprint"/>
			</logic:equal>	   						   		
		)

		<p>
			<bean:define id="urlToImage"><%= request.getContextPath() %>/SpaceManager/manageBlueprints.do?method=view&blueprintId=<bean:write name="mostRecentBlueprint" property="idInternal"/>&viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&suroundingSpaceBlueprint=<bean:write name="suroundingSpaceBlueprint"/>&viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/>&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>				
			<html:img src="<%= urlToImage %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" usemap="#roomLinksMap" style="border: 1px dotted #ccc; padding: 4px;"/>
			<map id ="roomLinksMap" name="roomLinksMap">
				<logic:iterate id="blueprintTextRectanglesEntry" name="blueprintTextRectangles">																	
					<bean:define id="blueprintSpace" name="blueprintTextRectanglesEntry" property="key" />					
					<logic:iterate id="blueprintTextRectangle" name="blueprintTextRectanglesEntry" property="value">
						<bean:define id="p1" name="blueprintTextRectangle" property="p1" />				
						<bean:define id="p2" name="blueprintTextRectangle" property="p2" />				
	 					<bean:define id="p3" name="blueprintTextRectangle" property="p3" />				
	 					<bean:define id="p4" name="blueprintTextRectangle" property="p4" />							
						<bean:define id="coords"><bean:write name="p1" property="x"/>,<bean:write name="p1" property="y"/>,<bean:write name="p2" property="x"/>,<bean:write name="p2" property="y"/>,<bean:write name="p3" property="x"/>,<bean:write name="p3" property="y"/>,<bean:write name="p4" property="x"/>,<bean:write name="p4" property="y"/></bean:define>				 				
						<bean:define id="urlToCoords">manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="blueprintSpace" property="spaceInformation.idInternal"/>&viewBlueprintNumbers=<bean:write name="viewBlueprintNumbers"/>&viewDoorNumbers=<bean:write name="viewDoorNumbers"/>&viewSpaceIdentifications=<bean:write name="viewSpaceIdentifications"/>&viewOriginalSpaceBlueprint=<bean:write name="viewOriginalSpaceBlueprint"/></bean:define>
						<area shape="poly" coords="<%= coords %>" href="<%= urlToCoords %>"/>									
					</logic:iterate>										
				</logic:iterate>					
			</map>								
		</p>
		
	</logic:notEmpty>			
	<logic:empty name="mostRecentBlueprint">																	
		<p class="mtop05"><em><bean:message key="label.empty.blueprint" bundle="SPACE_RESOURCES"/>.</em></p>										
	</logic:empty>
	<%
		if(thisSpace.personIsSpacesAdministrator(person)){
	%>	
	<p class="mtop025">
		<html:link page="/manageBlueprints.do?method=showBlueprintVersions&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
			<bean:message bundle="SPACE_RESOURCES" key="link.manage.blueprints"/>
		</html:link>
	</p>
	<%
		}
	%>

	<%-- Subspaces --%>
	<logic:present name="spaces">
		<h3 class="mtop2 mbottom05"><bean:message bundle="SPACE_RESOURCES" key="title.subspaces"/></h3>
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
					<bean:define id="urlToManageSubspace">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="subSpace" property="spaceInformation.idInternal"/></bean:define>
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
		<html:link page="/manageSpaces.do?method=showCreateSubSpaceForm&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
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
		if(thisSpace.personHasPermissionsToManageSpace(person)) {		 
	%>
		<p>
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES" />
		<html:link page="/manageSpaces.do?method=exportSpaceInfoToExcel" paramId="spaceID" paramName="selectedSpaceInformation" paramProperty="space.idInternal">
			<bean:message key="link.export.to.excel" bundle="SPACE_RESOURCES"/>						
		</html:link>
		</p>
	<%
		}
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
		<html:link page="/manageSpaceResponsibility.do?method=showSpaceResponsibility&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
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
		<html:link page="/manageUnitSpaceOccupations.do?method=prepareManageUnitSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
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
	<p class="mtop025"><html:link page="/managePersonSpaceOccupations.do?method=showSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
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
	<p class="mtop025"><html:link page="/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.material.occupations"/>
	</html:link></p>
	<%
		}
	%>	
		
	<%
		if(net.sourceforge.fenixedu.domain.space.Space.personIsSpacesAdministrator(person)){
	%>
	<%-- Access Groups --%>
	<h3 class="mtop2 mbottom0"><bean:message bundle="SPACE_RESOURCES" key="label.access.groups"/></h3>
	<p class="mtop025"><html:link page="/manageSpaces.do?method=manageAccessGroups&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.manage.access.groups"/>
	</html:link></p>	
	<% 
		}
	%>


	
</logic:present>	