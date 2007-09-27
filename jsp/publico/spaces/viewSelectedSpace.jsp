<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="title.see.selected.space" bundle="DEFAULT"/></h2>

<logic:notEmpty name="selectedSpace">

	<bean:define id="selectedSpaceInformation" name="selectedSpace" property="space.spaceInformation"></bean:define>

	<fr:view name="selectedSpace" schema="ViewSelectedSpaceInfo">	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop15" />
			<fr:property name="rowClasses" value="bold,,,,," />			
		</fr:layout>								
	</fr:view>
			
	<p class="mtop20">
		<ul class="mvert">		
			<logic:equal name="selectedSpace" property="withSchedule" value="true">
				<bean:define id="viewScheduleLink">/viewRoom.do?method=roomViewer&amp;roomName=<bean:write name="selectedSpace" property="space.identification"/></bean:define>				
				<li>						
					<html:link target="_blank" page="<%= viewScheduleLink %>"><bean:message key="link.view.schedule"/></html:link>		
				</li>									
			</logic:equal>		
			<li>						
				<html:link page="/findSpaces.do?method=prepareSearchSpaces"><bean:message key="link.search.for.spaces.again"/></html:link>		
			</li>
		</ul>
	</p>			
							
	<logic:notEmpty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<p class="mtop20 mbottom15"><b><bean:message key="label.selected.space.persons" bundle="DEFAULT"/></b></p>		
		
		<fr:view schema="ViewActivePersonSpaceOccupations" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
			<fr:layout name="tabular">      			
	   			<fr:property name="classes" value="tstyle4 thlight tdcenter mvert0"/>
	   		</fr:layout>	
		</fr:view>	
	</logic:notEmpty>		
			
	<logic:notEmpty name="mostRecentBlueprint">		
		
		<p class="mbottom20"><b><bean:message key="label.selected.space.blueprint" bundle="DEFAULT"/></b></p>
							
		<bean:define id="urlToImage"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpaceBlueprint&amp;blueprintId=<bean:write name="mostRecentBlueprint" property="idInternal"/>&amp;viewSpaceIdentifications=true&amp;suroundingSpaceBlueprint=<bean:write name="suroundingSpaceBlueprint"/>&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>			
		<div style="width: 710px; height: 610px; border: 1px solid #ccc; padding: 10px 5px 5px 10px;">
		<div style="width: 700px; height: 600px; overflow: scroll;">
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
						<bean:define id="urlToCoords"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpace&amp;spaceID=<bean:write name="blueprintSpace" property="idInternal"/>&amp;viewSpaceIdentifications=true</bean:define>
						<area shape="poly" coords="<%= coords %>" href="<%= urlToCoords %>"/>									
					</logic:iterate>										
				</logic:iterate>					
			</map>
		</div>
		</div>					
					
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