<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.space.Space"%>
<html:xhtml/>

<h2><bean:message key="merge.space.title" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="SPACE_MANAGER">

	<logic:present name="moveSpaceBean">
	
		<logic:notEmpty name="moveSpaceBean" property="space">

			<bean:define id="space" name="moveSpaceBean" property="space" toScope="request"/>	
			<bean:define id="selectedSpace" name="moveSpaceBean" property="space" toScope="request"/>	
			
			<div class="mbottom2">
				<jsp:include page="spaceCrumbs.jsp"/>
			</div>	
								
			<bean:define id="backLink">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="moveSpaceBean" property="space.spaceInformation.idInternal"/></bean:define>		
			<ul class="mvert15 list5">
				<li>
					<html:link page="<%= backLink %>">
						<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
					</html:link>
				</li>
			</ul>	
				
			<p class="mtop2"><b><bean:message key="label.find.destination.rooms" bundle="SPACE_RESOURCES"/></b></p>
			
			<fr:form action="/manageSpaces.do?method=findDestinationRoomForProcessMerge">	
				<fr:edit id="findMergeDestinationRoomBean" name="moveSpaceBean" schema="FindMergeDestinationRoom">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>						
					</fr:layout>										
				</fr:edit>
				<html:submit><bean:message key="link.submit" bundle="SPACE_RESOURCES"/></html:submit>
			</fr:form>		
			
			<logic:notEmpty name="spaces">									
			
				<p class="mtop2"><b><bean:message key="label.found.rooms" bundle="SPACE_RESOURCES"/></b></p>				
				<bean:define id="space" name="moveSpaceBean" property="space"/>
								
				<fr:view name="spaces" schema="ViewDestinationRoomInfo">					
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1"/>																
																					
			   			<fr:property name="link(merge)" value="<%= "/manageSpaces.do?method=compareDestinationRoomWithFromRoom&fromRoomID=" + ((Space)space).getIdInternal().toString() %>"/>
			            <fr:property name="param(merge)" value="idInternal/destinationRoomID"/>
				        <fr:property name="key(merge)" value="label.choose"/>
			            <fr:property name="bundle(merge)" value="SPACE_RESOURCES"/>
			            <fr:property name="order(merge)" value="0"/>	
		            </fr:layout>		         							
				</fr:view>
			</logic:notEmpty>
		
		</logic:notEmpty>
		
	</logic:present>
	
</logic:present>