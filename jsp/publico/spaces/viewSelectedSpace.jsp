<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.see.selected.space" bundle="DEFAULT"/></h2>

<logic:notEmpty name="selectedSpace">

	<fr:view name="selectedSpace" schema="ViewSelectedSpaceInfo">	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 mtop15" />
			<fr:property name="columnClasses" value="bold,,,,," />			
		</fr:layout>								
	</fr:view>
			
	<logic:equal name="selectedSpace" property="withSchedule" value="true">
		<bean:define id="viewScheduleLink">/viewRoom.do?method=roomViewer&roomName=<bean:write name="selectedSpace" property="space.identification"/></bean:define>		
		<p class="mtop15 mbottom15">
			<ul class="mvert">
				<li>						
					<html:link page="<%= viewScheduleLink %>"><bean:message key="link.view.schedule"/></html:link>		
				</li>
				<li>						
					<html:link page="/findSpaces.do?method=prepareSearchSpaces"><bean:message key="link.search.for.spaces.again"/></html:link>		
				</li>			
			</ul>	
		</p>		
	</logic:equal>	
			
	<logic:notEmpty name="mostRecentBlueprint">		
		
		<p class="mbottom15"><b><bean:message key="label.selected.space.blueprint" bundle="DEFAULT"/></b></p>
			
		<bean:define id="selectedSpaceInformation" name="selectedSpace" property="space.spaceInformation"></bean:define>		
		<bean:define id="urlToImage"><%= request.getContextPath() %>/SpaceManager/manageBlueprints.do?method=view&blueprintId=<bean:write name="mostRecentBlueprint" property="idInternal"/>&viewSpaceIdentifications=true&suroundingSpaceBlueprint=<bean:write name="suroundingSpaceBlueprint"/>&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>			
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
					<bean:define id="urlToCoords"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpace&spaceID=<bean:write name="blueprintSpace" property="idInternal"/>&viewSpaceIdentifications=true</bean:define>
					<area shape="poly" coords="<%= coords %>" href="<%= urlToCoords %>"/>									
				</logic:iterate>										
			</logic:iterate>					
		</map>					
					
	</logic:notEmpty>
	
	<logic:notEmpty name="containedSpaces">
		
		<p class="mbottom15"><b><bean:message key="label.selected.space.contained.spaces" bundle="DEFAULT"/></b></p>
		
		<fr:view name="containedSpaces" schema="ViewSelectedSpaceContainedSpaces">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 mtop05" />
				<fr:property name="columnClasses" value="acenter,acenter" />					
				<fr:property name="link(view)" value="/findSpaces.do?method=viewSpace" />
				<fr:property name="param(view)" value="idInternal/spaceID" />
				<fr:property name="key(view)" value="link.view.space" />
				<fr:property name="bundle(view)" value="DEFAULT" />
				<fr:property name="order(view)" value="0" />								
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>	
		
</logic:notEmpty>