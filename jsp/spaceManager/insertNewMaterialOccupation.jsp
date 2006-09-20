<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="materialTypeBean">	
	<h2><bean:message key="title.insert.material.occupations" bundle="SPACE_RESOURCES"/></h2>	
	
	<bean:define id="spaceInformationID" name="materialTypeBean" property="spaceInformation.idInternal"/>
	<bean:define id="showMaterialSpaceOccupationLink">/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0&spaceInformationID=<bean:write name="spaceInformationID"/></bean:define>
	<bean:define id="prepareInsertMaterialOccupationLink">/manageMaterialSpaceOccupations.do?method=prepareInsertMaterialOccupation&page=0</bean:define>	

	<logic:messagesPresent message="true">
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<br/>
	</logic:messagesPresent>

	<br/>
	<h3><bean:message key="title.choose.material.type" bundle="SPACE_RESOURCES"/></h3>
	<fr:form>
		<fr:edit nested="true" name="materialTypeBean" id="materialTypeWithMaterialType" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean"
			schema="ChooseMaterialType">						
			<fr:destination name="postBack" path="/manageMaterialSpaceOccupations.do?method=prepareChooseMaterial&page=0"/>
		</fr:edit>
	</fr:form>						
	
	<logic:notEmpty name="materialTypeBean" property="materialType">	
		<br/>	
		<h3><bean:message key="title.choose.material" bundle="SPACE_RESOURCES"/></h3>
		<bean:define id="chooseMaterialSchema">Choose<bean:write name="materialTypeBean" property="materialType.simpleName"/></bean:define>

		<fr:edit name="materialTypeBean" id="materialTypeWithMaterial" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean"
			schema="<%= chooseMaterialSchema %>" action="<%= prepareInsertMaterialOccupationLink %>">			
			<fr:destination name="cancel" path="<%= showMaterialSpaceOccupationLink %>"/>			
			<fr:destination name="invalid" path="<%= prepareInsertMaterialOccupationLink %>"/>	
		</fr:edit>
	</logic:notEmpty>
		
	<logic:notEmpty name="materialTypeBean" property="material">					
		<br/>	
		<h3><bean:message key="title.insert.material.occupation.details" bundle="SPACE_RESOURCES"/></h3>
		
		<bean:define id="schemaName">AddNew<bean:write name="materialTypeBean" property="materialType.simpleName"/>SpaceOccupation</bean:define>		
		<bean:define id="type" name="materialTypeBean" property="material.materialSpaceOccupationSubClass.name"/>		
		<bean:define id="materialSlotName" name="materialTypeBean" property="material.materialSpaceOccupationSlotName" />

		<fr:form action="<%= showMaterialSpaceOccupationLink %>">
			<fr:create nested="true" id="materialTypeWithDates" type="<%= type.toString() %>" schema="<%= schemaName %>">			
				<fr:hidden slot="space" name="materialTypeBean" property="spaceInformation.space"/>								
				<fr:hidden slot="<%= materialSlotName.toString() %>" name="materialTypeBean" property="material" />								
				<fr:destination name="invalid" path="<%= prepareInsertMaterialOccupationLink %>"/>
			</fr:create>								

			<fr:edit name="materialTypeBean" id="materialTypeToCreate" visible="false" />						

			<html:submit bundle="SPACE_RESOURCES">
				<bean:message key="link.submit" bundle="SPACE_RESOURCES"/>
			</html:submit>
			<html:cancel>
				<bean:message key="link.cancel" bundle="SPACE_RESOURCES"/>
			</html:cancel>
		</fr:form>			
	</logic:notEmpty>

</logic:present>
