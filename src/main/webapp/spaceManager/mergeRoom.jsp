<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.space.Space"%>
<html:xhtml/>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="merge.space.title" bundle="SPACE_RESOURCES"/></h2>

<logic:present role="role(SPACE_MANAGER)">

	<logic:present name="moveSpaceBean">
	
		<logic:notEmpty name="moveSpaceBean" property="space">

			<bean:define id="space" name="moveSpaceBean" property="space" toScope="request"/>	
			<bean:define id="selectedSpace" name="moveSpaceBean" property="space" toScope="request"/>	
			
			<div class="mbottom2">
				<jsp:include page="spaceCrumbs.jsp"/>
			</div>	
								
			<bean:define id="backLink">/manageSpaces.do?method=manageSpace&amp;spaceInformationID=<bean:write name="moveSpaceBean" property="space.spaceInformation.externalId"/></bean:define>		
			<ul class="mvert15 list5">
				<li>
					<html:link page="<%= backLink %>">
						<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
					</html:link>
				</li>
			</ul>	
				
			<p class="mtop2 mbottom05"><b><bean:message key="label.find.destination.rooms" bundle="SPACE_RESOURCES"/></b></p>
			
			<fr:form action="/manageSpaces.do?method=findDestinationRoomForProcessMerge">	
				<fr:edit id="findMergeDestinationRoomBean" name="moveSpaceBean" schema="FindMergeDestinationRoom">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
						<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
					</fr:layout>										
				</fr:edit>
				<html:submit><bean:message key="link.submit" bundle="SPACE_RESOURCES"/></html:submit>
			</fr:form>		
			
			<logic:notEmpty name="spaces">									
			
				<p class="mtop2 mbottom05"><b><bean:message key="label.found.rooms" bundle="SPACE_RESOURCES"/></b></p>				
				<bean:define id="space" name="moveSpaceBean" property="space"/>
								
				<fr:view name="spaces" schema="ViewDestinationRoomInfo">					
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4"/>
						<fr:property name="columnClasses" value=",acenter,acenter,acenter,acenter,acenter,,,"/>
			   			<fr:property name="link(merge)" value="<%= "/manageSpaces.do?method=compareDestinationRoomWithFromRoom&fromRoomID=" + ((Space)space).getExternalId().toString() %>"/>
			            <fr:property name="param(merge)" value="externalId/destinationRoomID"/>
				        <fr:property name="key(merge)" value="label.choose"/>
			            <fr:property name="bundle(merge)" value="SPACE_RESOURCES"/>
			            <fr:property name="order(merge)" value="0"/>	
		            </fr:layout>		         							
				</fr:view>
			</logic:notEmpty>
		
		</logic:notEmpty>
		
	</logic:present>
	
</logic:present>