<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ page isELIgnored="true" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h1><bean:message key="title.see.selected.space" bundle="DEFAULT"/></h1>

<logic:notEmpty name="selectedSpace">

	<%-- <bean:define id="selectedSpaceInformation" name="selectedSpace" property="space.spaceInformation"></bean:define>--%>

	<p class="mtop15 mbottom05">
		<html:link page="/findSpaces.do?method=prepareSearchSpaces"> &laquo; <bean:message key="link.search.for.spaces.again"/></html:link>		
	</p>
	
	<fr:view name="selectedSpace">	
		<fr:schema type="org.fenixedu.academic.dto.spaceManager.FindSpacesBean" bundle="DEFAULT">
			<fr:slot name="space.presentationName" key="label.find.spaces.space.name"/>
			<fr:slot name="suroundingSpacePath" key="label.find.spaces.location" layout="list-link">
				<fr:property name="linkFormat" value="/findSpaces.do?method=${method}&amp;spaceID=${id}"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="contextRelative" value="true"/>
			</fr:slot>
			<fr:slot name="space.classification.name.content" layout="null-as-label" key="label.space.classification">
			</fr:slot>
			<fr:slot name="space.allocatableCapacity" layout="null-as-label" key="label.space.normal.capacity">
				<fr:property name="label" value="--"/>
			</fr:slot>
			<fr:slot name="examCapacity" layout="null-as-label" key="label.space.exam.capacity">
				<fr:property name="label" value="--"/>		
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop15" />
			<fr:property name="rowClasses" value="bold,,,,," />			
		</fr:layout>								
	</fr:view>

	<ul>
		<logic:equal name="selectedSpace" property="withSchedule" value="true">
			<bean:define id="viewScheduleLink">/viewRoom.do?method=roomViewer&amp;roomName=<bean:write name="selectedSpace" property="space.name"/>&amp;roomId=<bean:write name="selectedSpace" property="space.externalId"/></bean:define>				
			<li><html:link target="_blank" page="<%= viewScheduleLink %>"><bean:message key="link.view.schedule"/></html:link></li>
		</logic:equal>
			
		<logic:equal name="selectedSpace" property="isOccupiedByWrittenEvaluations" value="true">
			<bean:define id="viewWrittenEvaluationsLink">/spaces/writtenEvaluationsByRoom.faces?academicInterval=<bean:write name="selectedSpace" property="academicInterval.resumedRepresentationInStringFormat"/>&selectedRoomID=<bean:write name="selectedSpace" property="space.externalId"/></bean:define>				
			<li><html:link target="_blank" page="<%= viewWrittenEvaluationsLink %>"><bean:message key="link.view.written.evaluations"/></html:link></li>
		</logic:equal>
	</ul>
		

	<%--<logic:notEmpty name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.persons" bundle="DEFAULT"/></b></p>		
		
		<fr:view name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
			<fr:layout name="list">
	   			<fr:property name="classes" value="mvert05"/>
	   			<fr:property name="eachLayout" value="values"/>
	   			<fr:property name="eachSchema" value="ViewActivePersonSpaceOccupations"/>
	   		</fr:layout>
		</fr:view>
	</logic:notEmpty> --%>

	<logic:notEmpty name="mostRecentBlueprint">

		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.blueprint" bundle="APPLICATION_RESOURCES"/></b></p>

		<bean:define id="urlToImage"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpaceBlueprint&amp;spaceId=<bean:write name="selectedSpace" property="space.externalId"/>&amp;viewSpaceIdentifications=true</bean:define>			
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
							<bean:define id="urlToCoords"><%= request.getContextPath() %>/publico/findSpaces.do?method=viewSpace&amp;spaceID=<bean:write name="blueprintSpace" property="externalId"/>&amp;viewSpaceIdentifications=true</bean:define>
							<area shape="poly" coords="<%= coords %>" href="<%= urlToCoords %>"/>									
						</logic:iterate>										
					</logic:iterate>
				</map>
 			</div>
		</div>									
	
	</logic:notEmpty>	
	
	<logic:notEmpty name="selectedSpace" property="occupants">
		<bean:define id="users" name="selectedSpace" property="occupants"/>
		<table class="tstyle2 thlight thleft mtop15 table">
			<tbody>
				<tr>
					<th scope="row">
						<bean:message key="link.view.occupants"/>
					</th>
				</tr>
				<logic:iterate id="user" name="users">
					<bean:define id="username" name="user" property="name"/>
					<bean:define id="person" name="user" property="person"/>
					<bean:define id="justName" value="true"/>
					<logic:notEmpty name="person">
						<logic:notEmpty name="person" property="defaultWebAddressUrl">
							<tr>
								<td>
									<a href="<bean:write name="person" property="defaultWebAddressUrl"/>"><bean:write name="username"/></a>
								</td>
							</tr>
							<bean:define id="justName" value="false"/>
						</logic:notEmpty>
					</logic:notEmpty>
					<logic:equal name="justName" value="true">
						<tr>
							<td>
								<bean:write name="username"/>
							</td>
						</tr>
					</logic:equal>
				</logic:iterate>
			</tbody>
		</table>
	</logic:notEmpty>
	
	<logic:notEmpty name="containedSpaces">
		
		<p class="mtop2 mbottom05"><b><bean:message key="label.selected.space.contained.spaces" bundle="DEFAULT"/></b></p>
					
		<fr:view name="containedSpaces">	
			<fr:schema type="org.fenixedu.spaces.domain.Space" bundle="DEFAULT">
				<fr:slot name="classification" key="label.space.type">
					<fr:property name="format" value="${name.content}"></fr:property>
				</fr:slot>
				
				<fr:slot name="presentationName" key="label.find.spaces.space.name"/>
			</fr:schema>		
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight mtop05" />
				<fr:property name="columnClasses" value="acenter,acenter,acenter" />					
				<fr:property name="link(view)" value="/findSpaces.do?method=viewSpace" />
				<fr:property name="param(view)" value="externalId/spaceID" />
				<fr:property name="key(view)" value="link.view.space" />
				<fr:property name="bundle(view)" value="DEFAULT" />
				<fr:property name="order(view)" value="0" />								
			</fr:layout>
		</fr:view>
		
	</logic:notEmpty>	
		
</logic:notEmpty>
