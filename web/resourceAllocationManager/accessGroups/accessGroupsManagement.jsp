<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@page import="net.sourceforge.fenixedu.domain.ResourceAllocationRole.ResourceAllocationAccessGroupType"%>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.GroupUnion"%>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.PersonGroup"%>
<html:xhtml/>

<em><bean:message bundle="SOP_RESOURCES" key="title.resourceManager.management"/></em>
<h2><bean:message key="title.access.groups.management" bundle="SOP_RESOURCES"/></h2>

<logic:present role="RESOURCE_ALLOCATION_MANAGER">

	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SOP_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>

	<%-- AccessGroups --%>		
	<e:labelValues id="accessGroupTypes" enumeration="net.sourceforge.fenixedu.domain.ResourceAllocationRole$ResourceAllocationAccessGroupType" bundle="ENUMERATION_RESOURCES" />
	<logic:iterate id="accessGroupType" name="accessGroupTypes" type="org.apache.struts.util.LabelValueBean">				
	
		<bean:define id="slotName" type="java.lang.String"><%= ResourceAllocationAccessGroupType.valueOf(accessGroupType.getValue()).getAccessGroupSlotName() %></bean:define>											
		<bean:define id="accessGroupType" name="accessGroupType" type="org.apache.struts.util.LabelValueBean"/>
							
		<logic:notEmpty name="resourceAllocationRole" property="<%= slotName %>">																											
			
			<bean:define id="accessGroup" name="resourceAllocationRole" property="<%= slotName %>" type="net.sourceforge.fenixedu.domain.accessControl.Group"/>
								
			<logic:equal name="accessGroup" property="class.name" value="<%= GroupUnion.class.getName() %>">
				<p class="mtop1 mbottom05"><strong><bean:write name="accessGroupType" property="label"/></strong></p>				
				<logic:iterate id="accessGroupChild" name="accessGroup" property="children">
					<bean:define id="accessGroupChild" name="accessGroupChild" toScope="request" />									
					<bean:define id="accessGroupExpression" name="accessGroupChild" property="expressionInHex" type="java.lang.String"/>																																	
					<logic:notEmpty name="accessGroupChild" property="elements">																					
						<fr:view schema="ViewPersonToListAccessGroupsInResourceAllocationManagement" name="accessGroupChild" property="elements">
							<fr:layout name="tabular">     										  
					   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>			            		            			            			           
					            <fr:property name="link(delete)" value="<%= "/accessGroupsManagement.do?method=removePersonFromAccessGroup&amp;accessGroupType=" + accessGroupType.getValue() + "&amp;expression=" + accessGroupExpression %>"/>		            
						        <fr:property name="key(delete)" value="label.remove"/>
					            <fr:property name="bundle(delete)" value="SOP_RESOURCES"/>	               	
				               	<fr:property name="order(delete)" value="0"/>			               	
				     		</fr:layout>    	
						</fr:view>																	
					</logic:notEmpty>							
				</logic:iterate>																	
			</logic:equal>
						
			<logic:equal name="accessGroup" property="class.name" value="<%= PersonGroup.class.getName() %>">
				<bean:define id="accessGroupExpression" name="accessGroup" property="expressionInHex" type="java.lang.String"/>																																	
				<logic:notEmpty name="accessGroup" property="elements">					
					<p class="mtop1 mbottom05"><strong><bean:write name="accessGroupType" property="label"/></strong></p>											
					<fr:view schema="ViewPersonToListAccessGroupsInResourceAllocationManagement" name="accessGroup" property="elements">
						<fr:layout name="tabular">     										  
				   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>			            		            			            			           
				            <fr:property name="link(delete)" value="<%= "/accessGroupsManagement.do?method=removePersonFromAccessGroup&amp;accessGroupType=" + accessGroupType.getValue() + "&amp;expression=" + accessGroupExpression %>"/>		            
					        <fr:property name="key(delete)" value="label.remove"/>
				            <fr:property name="bundle(delete)" value="SOP_RESOURCES"/>	               	
			               	<fr:property name="order(delete)" value="0"/>			               	
			     		</fr:layout>    	
					</fr:view>																	
				</logic:notEmpty>
			</logic:equal>
																																									
		</logic:notEmpty>									
		
	</logic:iterate>
	
	<%-- Add New Person Group --%>		
	<bean:define id="addPersonToAccessGroupUrl">/accessGroupsManagement.do?method=addPersonToAccessGroup</bean:define>
	<p class="mvert1"><strong><bean:message key="label.add.person" bundle="SOP_RESOURCES"/></strong></p>			
	<fr:form action="<%= addPersonToAccessGroupUrl %>">
		<fr:create id="PersonToAccessGroupBeanID" type="net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.AccessGroupBean" schema="AddPersonToAccessGroupInResourceAllocationManagement">
			<fr:layout name="tabular">  
				<fr:property name="classes" value="tstyle5 thmiddle thright thlight mtop05 mbottom05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>		
		</fr:create>
		<p><html:submit><bean:message key="button.add" bundle="SOP_RESOURCES"/></html:submit></p>
	</fr:form>

</logic:present>