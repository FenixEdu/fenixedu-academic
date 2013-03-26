<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.PersonGroup"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.PersistentGroup"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.GroupUnion"%>
<%@page import="net.sourceforge.fenixedu.domain.accessControl.RoleGroup"%>
<html:xhtml/>

<logic:present role="SPACE_MANAGER">
	
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformationId"/>
	<bean:define id="accessGroupType" name="accessGroupType" type="org.apache.struts.util.LabelValueBean"/>
		
	<logic:equal name="accessGroup" property="class.name" value="<%= GroupUnion.class.getName() %>">				
		<logic:iterate id="accessGroupChild" name="accessGroup" property="children">
			<bean:define id="accessGroup" name="accessGroupChild" toScope="request" />				
			<jsp:include page="printAccessGroups.jsp"/>										
		</logic:iterate>										
		<bean:define id="stopPrint" value="true" />						
	</logic:equal>
		
	<logic:empty name="stopPrint">	
		<logic:equal name="accessGroup" property="class.name" value="<%= PersonGroup.class.getName() %>">														
			<bean:define id="accessGroupExpression" name="accessGroup" property="expressionInHex" type="java.lang.String"/>			
			<fr:view schema="ViewPersonToListAccessGroups" name="accessGroup" property="elements">
				<fr:layout name="tabular">     										  
		   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>			            		            
		            <logic:notEmpty name="withLinks">
		            	<logic:equal name="withLinks" value="true">
				            <fr:property name="link(delete)" value="<%= "/manageSpaces.do?method=removePersonFromAccessGroup&spaceInformationID=" + selectedSpaceInformationId + "&spaceAccessGroupType=" + accessGroupType.getValue() + "&expression=" + accessGroupExpression %>" />		            
					        <fr:property name="key(delete)" value="label.remove"/>
				            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>	               	
			               	<fr:property name="order(delete)" value="0"/>
		               	</logic:equal>
	               	</logic:notEmpty>	               	                                           
	     		</fr:layout>    	
			</fr:view>								
		</logic:equal>
	</logic:empty>
	
	<logic:empty name="stopPrint">
		<logic:equal name="accessGroup" property="class.name" value="<%= PersistentGroup.class.getName() %>">
			<bean:define id="accessGroupExpression" name="accessGroup" property="expressionInHex" type="java.lang.String"/>
			<table class="tstyle4 thlight tdcenter mtop05">
				<tr>
					<th><bean:message key="label.persistent.group.name" bundle="SPACE_RESOURCES"/></th>
					<logic:notEmpty name="withLinks">
						<logic:equal name="withLinks" value="true">
							<th></th>
						</logic:equal>
					</logic:notEmpty>
				</tr>
				<tr>
					<td><bean:write name="accessGroup" property="name"/></td>
					<logic:notEmpty name="withLinks">
						<logic:equal name="withLinks" value="true">
							<td>						
								<html:link page="<%= "/manageSpaces.do?method=removePersonFromAccessGroup&spaceInformationID=" + selectedSpaceInformationId + "&spaceAccessGroupType=" + accessGroupType.getValue() + "&expression=" + accessGroupExpression %>">
									<bean:message key="label.remove" bundle="SPACE_RESOURCES"/>
								</html:link>
							</td>
						</logic:equal>
					</logic:notEmpty>
				</tr>
			</table>											
		</logic:equal>			
	</logic:empty>
	
	<logic:empty name="stopPrint">
		<logic:equal name="accessGroup" property="class.name" value="<%= RoleGroup.class.getName() %>">
			<bean:define id="accessGroupExpression" name="accessGroup" property="expressionInHex" type="java.lang.String"/>
			<table class="tstyle4 thlight tdcenter mtop05">
				<tr>
					<th><bean:message key="label.role.type.group.name" bundle="SPACE_RESOURCES"/></th>
					<logic:notEmpty name="withLinks">
						<logic:equal name="withLinks" value="true">
							<th></th>
						</logic:equal>	
					</logic:notEmpty>
				</tr>
				<tr>
					<td><bean:write name="accessGroup" property="role.roleType.name"/></td>
					<logic:notEmpty name="withLinks">
						<logic:equal name="withLinks" value="true">						
							<td>
								<html:link page="<%= "/manageSpaces.do?method=removePersonFromAccessGroup&spaceInformationID=" + selectedSpaceInformationId + "&spaceAccessGroupType=" + accessGroupType.getValue() + "&expression=" + accessGroupExpression %>">
									<bean:message key="label.remove" bundle="SPACE_RESOURCES"/>
								</html:link>
							</td>
						</logic:equal>
					</logic:notEmpty>
				</tr>
			</table>											
		</logic:equal>			
	</logic:empty>
		
</logic:present>


