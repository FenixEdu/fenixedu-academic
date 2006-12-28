<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.access.groups.management" bundle="SPACE_RESOURCES"/></h2>

<logic:notEmpty name="selectedSpaceInformation">
		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
	<bean:define id="space" name="selectedSpaceInformation" property="space"/>

	<ul class="mvert15 list5">
		<li>
			<html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformationId">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>

	<%-- AccessGroups --%>		
	<e:labelValues id="accessGroupTypes" enumeration="net.sourceforge.fenixedu.domain.space.Space$SpaceAccessGroupType" bundle="ENUMERATION_RESOURCES" />
	<logic:iterate id="accessGroupType" name="accessGroupTypes" type="org.apache.struts.util.LabelValueBean">				
		<bean:define id="slotName" type="java.lang.String"><%= SpaceAccessGroupType.valueOf(accessGroupType.getValue()).getSpaceAccessGroupSlotName() %></bean:define>				
				
		<logic:notEmpty name="space" property="<%= slotName %>">			
			<bean:define id="accessGroup" name="space" property="<%= slotName %>" type="net.sourceforge.fenixedu.domain.accessControl.Group"/>			
			<logic:notEmpty name="accessGroup" property="elements">	
				<p class="mtop2 mbottom05"><strong><bean:write name="accessGroupType" property="label"/></strong></p>
				<fr:view schema="ViewPersonToListAccessGroups" name="accessGroup" property="elements">
					<fr:layout name="tabular">     										  
			   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
			            
			            <fr:property name="link(delete)" value="<%= "/manageSpaces.do?method=removePersonFromAccessGroup&spaceInformationID=" + selectedSpaceInformationId + "&spaceAccessGroupType=" + accessGroupType.getValue() %>"/>
			            <fr:property name="param(delete)" value="idInternal/personID"/>
				        <fr:property name="key(delete)" value="label.remove"/>
			            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
	                	<fr:property name="order(delete)" value="0"/>                                           
	      			</fr:layout>    	
				</fr:view>		
			</logic:notEmpty>		
		</logic:notEmpty>
		
		<logic:notEmpty name="space" property="<%= slotName %>">				
			<bean:define id="accessGroup" name="space" property="<%= slotName %>" type="net.sourceforge.fenixedu.domain.accessControl.Group"/>			
			<logic:empty name="accessGroup" property="elements">		
				<bean:define id="slotNameWithChain" type="java.lang.String"> <%= slotName %>WithChainOfResponsibility </bean:define>
				<logic:notEmpty name="space" property="<%= slotNameWithChain %>">
					<bean:define id="accessGroup" name="space" property="<%= slotNameWithChain %>" type="net.sourceforge.fenixedu.domain.accessControl.Group"/>
					<logic:notEmpty name="accessGroup">	
						<logic:notEmpty name="accessGroup" property="elements">	
							<p class="mtop2 mbottom05"><strong><bean:write name="accessGroupType" property="label"/> (<bean:message key="label.defined.elements.in.parent.space" bundle="SPACE_RESOURCES"/>)</strong></p>																		
							<fr:view schema="ViewPersonToListAccessGroups" name="accessGroup" property="elements">
								<fr:layout name="tabular">     										  
						   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>				  
				      			</fr:layout>    	
							</fr:view>		
						</logic:notEmpty>		
					</logic:notEmpty>	
				</logic:notEmpty>						
			</logic:empty>							
		</logic:notEmpty>
						
		<logic:empty name="space" property="<%= slotName %>">		
			<bean:define id="slotNameWithChain" type="java.lang.String"> <%= slotName %>WithChainOfResponsibility </bean:define>
			<logic:notEmpty name="space" property="<%= slotNameWithChain %>">
				<bean:define id="accessGroup" name="space" property="<%= slotNameWithChain %>" type="net.sourceforge.fenixedu.domain.accessControl.Group"/>
				<logic:notEmpty name="accessGroup">	
					<logic:notEmpty name="accessGroup" property="elements">	
						<p class="mtop2 mbottom05"><strong><bean:write name="accessGroupType" property="label"/> (<bean:message key="label.defined.elements.in.parent.space" bundle="SPACE_RESOURCES"/>)</strong></p>																		
						<fr:view schema="ViewPersonToListAccessGroups" name="accessGroup" property="elements">
							<fr:layout name="tabular">     										  
					   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>				  
			      			</fr:layout>    	
						</fr:view>		
					</logic:notEmpty>		
				</logic:notEmpty>	
			</logic:notEmpty>						
		</logic:empty>			

	</logic:iterate>	
		
	<%-- Add New Person --%>
	<p class="mbottom05"><strong><bean:message key="label.add.person" bundle="SPACE_RESOURCES"/></strong></p>	
	<bean:define id="addPersonToAccessGroupUrl">/manageSpaces.do?method=addPersonToAccessGroup&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>
	<fr:form action="<%= addPersonToAccessGroupUrl %>">
		<fr:create nested="true" id="addPersonToPersonOccupationAccessGroup" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.AccessGroupPersonBean" schema="AddPersonToAccessGroup">
			<fr:layout name="tabular">  
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop05 mbottom1"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>		
		</fr:create>
		<p>
		<html:submit><bean:message key="button.add" bundle="SPACE_RESOURCES"/></html:submit>
		</p>
	</fr:form>
				
</logic:notEmpty>
