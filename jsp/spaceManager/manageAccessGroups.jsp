<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.domain.space.Space.SpaceAccessGroupType" %>

<h2><bean:message key="label.access.groups.management" bundle="SPACE_RESOURCES"/></h2>

<logic:notEmpty name="selectedSpaceInformation">
		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
			
	<logic:messagesPresent message="true">
		<br/><br/>
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>
	
	<%-- AccessGroups --%>		
	<e:labelValues id="accessGroupTypes" enumeration="net.sourceforge.fenixedu.domain.space.Space$SpaceAccessGroupType" bundle="ENUMERATION_RESOURCES" />
	<logic:iterate id="accessGroupType" name="accessGroupTypes" type="org.apache.struts.util.LabelValueBean">
		<logic:notEmpty name="space" property="<%= SpaceAccessGroupType.valueOf(accessGroupType.getValue()).getSpaceAccessGroupSlotName() %>">	
			<logic:notEmpty name="space" property="<%= SpaceAccessGroupType.valueOf(accessGroupType.getValue()).getSpaceAccessGroupSlotName() + ".elements"%>">	
				<h3 class="mtop2 mbottom0"><bean:write name="accessGroupType" property="label"/></h3>				
				<fr:view schema="ViewPersonToListAccessGroups" name="space" property="<%= SpaceAccessGroupType.valueOf(accessGroupType.getValue()).getSpaceAccessGroupSlotName() + ".elements" %>">
					<fr:layout name="tabular">      										  
			   			<fr:property name="rowClasses" value="listClasses"/>	
			   			<fr:property name="columnClasses" value="listClasses"/>
			   			<fr:property name="headerClasses" value="listClasses-header"/>
			            
			            <fr:property name="link(delete)" value="<%= "/manageSpaces.do?method=removePersonFromAccessGroup&spaceInformationID=" + selectedSpaceInformationId + "&spaceAccessGroupType=" + accessGroupType.getValue() %>"/>
			            <fr:property name="param(delete)" value="idInternal/personID"/>
				        <fr:property name="key(delete)" value="label.remove"/>
			            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
	                	<fr:property name="order(delete)" value="0"/>                                           
	      			</fr:layout>    	
				</fr:view>		
			</logic:notEmpty>		
		</logic:notEmpty>	
	</logic:iterate>	
		
	<%-- Add New Person --%>
	<h3><bean:message key="label.add.person" bundle="SPACE_RESOURCES"/></h3>				
	<bean:define id="addPersonToAccessGroupUrl">/manageSpaces.do?method=addPersonToAccessGroup&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>
	<fr:form action="<%= addPersonToAccessGroupUrl %>">
		<fr:create nested="true" id="addPersonToPersonOccupationAccessGroup" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.AccessGroupPersonBean" schema="AddPersonToAccessGroup" />	   			
		<html:submit><bean:message key="button.add" bundle="SPACE_RESOURCES"/></html:submit>
	</fr:form>
			
	<p><html:link page="/manageSpaces.do?method=manageSpace&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformationId">
			<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link></p>
	
</logic:notEmpty>
