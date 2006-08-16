<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="link.insert.material.occupations" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">	

	<bean:define id="actionLink">
		/manageMaterialSpaceOccupations.do?method=prepareInsertMaterialOccupation&page=0
	</bean:define>
	<bean:define id="backLink">
		/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/>
	</bean:define>

	<logic:messagesPresent message="true">
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<br/>
	</logic:messagesPresent>

	<fr:edit name="materialTypeBean" id="materialType" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean"
		schema="ChooseMaterialType" action="<%= actionLink %>">			
		<fr:destination name="cancel" path="<%= backLink %>"/>
	</fr:edit>
	
	<br/><br/>
	<logic:notEmpty name="materialTypeBean" property="materialType">
				
		<bean:define id="schemaName">
			AddNew<bean:write name="materialTypeBean" property="materialType.simpleName"/>SpaceOccupation
		</bean:define>
		
		<bean:define id="type" name="type" />
		
		<fr:form action="<%= backLink %>">
			<fr:create id="create" type="<%= type.toString() %>" schema="<%= schemaName %>">			
				<fr:hidden slot="space" name="selectedSpaceInformation" property="space" />								
				<fr:destination name="invalid" path="<%= actionLink %>"/>
			</fr:create>			
			
			<fr:edit name="materialTypeBean" id="materialTypeToReturn" visible="false" />			
			
			<html:submit bundle="SPACE_RESOURCES">
				<bean:message key="link.submit" bundle="SPACE_RESOURCES"/>
			</html:submit>
			<html:cancel>
				<bean:message key="link.cancel" bundle="SPACE_RESOURCES"/>
			</html:cancel>
		</fr:form>		
	
	</logic:notEmpty>

</logic:present>
