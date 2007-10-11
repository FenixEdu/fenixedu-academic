<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<h1><bean:message key="title.see.selected.space" bundle="DEFAULT"/></h1>

<logic:notEmpty name="selectedSpace">

	<bean:define id="selectedSpaceInformation" name="selectedSpace" property="space.spaceInformation"></bean:define>

	<p class="mtop15 mbottom05">
		<html:link page="/findSpaces.do?method=prepareSearchSpaces"> &laquo; <bean:message key="link.search.for.spaces.again"/></html:link>		
	</p>

	<fr:view name="selectedSpace" schema="ViewSelectedSpaceInfo">	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop15" />
			<fr:property name="rowClasses" value="bold,,,,," />			
		</fr:layout>								
	</fr:view>

	<logic:equal name="selectedSpace" property="withSchedule" value="true">
		<bean:define id="viewScheduleLink">/viewRoom.do?method=roomViewer&amp;roomName=<bean:write name="selectedSpace" property="space.identification"/></bean:define>				
		<p><html:link target="_blank" page="<%= viewScheduleLink %>"><bean:message key="link.view.schedule"/></html:link></p>
	</logic:equal>
		
	<logic:notEmpty name="selectedSpace" property="space.writtenEvaluationSpaceOccupations">
		<bean:define id="viewWrittenEvaluationsLink">/spaces/writtenEvaluationsByRoom.faces?executionPeriodOID=<bean:write name="selectedSpace" property="executionPeriod.idInternal"/>&selectedRoomID=<bean:write name="selectedSpace" property="space.idInternal"/></bean:define>				
		<p><html:link target="_blank" page="<%= viewWrittenEvaluationsLink %>"><bean:message key="link.view.written.evaluations"/></html:link></p>
	</logic:notEmpty>
		

	<logic:notEmpty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.persons" bundle="DEFAULT"/></b></p>		
		
		<fr:view name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
			<fr:layout name="list">
	   			<fr:property name="classes" value="mvert05"/>
	   			<fr:property name="eachLayout" value="values"/>
	   			<fr:property name="eachSchema" value="ViewActivePersonSpaceOccupations"/>
	   		</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<%-- 
	<logic:notEmpty name="mostRecentBlueprint">

		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.blueprint" bundle="DEFAULT"/></b></p>

		<bean:define id="urlToImage"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpaceBlueprint&amp;blueprintId=<bean:write name="mostRecentBlueprint" property="idInternal"/>&amp;viewSpaceIdentifications=true&amp;suroundingSpaceBlueprint=<bean:write name="suroundingSpaceBlueprint"/>&amp;spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>			
		<div style="width: 710px; height: 510px; border: 1px solid #ccc; padding: 10px 5px 5px 10px;">
			<div style="width: 700px; height: 500px; overflow: scroll;">
				<html:img src="<%= urlToImage %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" usemap="#roomLinksMap" style="border: 1px solid #ddd; padding: 10px;"/>
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
	--%>
	
	<logic:notEmpty name="containedSpaces">
		
		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.contained.spaces" bundle="DEFAULT"/></b></p>
					
		<fr:view name="containedSpaces" schema="ViewSelectedSpaceContainedSpaces">			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop05" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter" />					
				<fr:property name="link(view)" value="/findSpaces.do?method=viewSpace" />
				<fr:property name="param(view)" value="idInternal/spaceID" />
				<fr:property name="key(view)" value="link.view.space" />
				<fr:property name="bundle(view)" value="DEFAULT" />
				<fr:property name="order(view)" value="0" />								
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>	
		
</logic:notEmpty>