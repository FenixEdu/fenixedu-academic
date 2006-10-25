<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="materialTypeBean">
	<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
	<h2><bean:message key="title.insert.material.occupations" bundle="SPACE_RESOURCES"/></h2>	

	<bean:define id="spaceInformationID" name="materialTypeBean" property="spaceInformation.idInternal"/>
	<bean:define id="showMaterialSpaceOccupationLink">/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0&spaceInformationID=<bean:write name="spaceInformationID"/></bean:define>
	<bean:define id="prepareInsertMaterialOccupationLink">/manageMaterialSpaceOccupations.do?method=prepareInsertMaterialOccupation&page=0</bean:define>	

	<logic:messagesPresent message="true">
		<p class="mtop15">
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		</p>
	</logic:messagesPresent>

	<p class="mtop15 mbottom05"><strong><bean:message key="title.choose.material.type" bundle="SPACE_RESOURCES"/></strong></p>
	<fr:form>
		<fr:edit nested="true" name="materialTypeBean" id="materialTypeWithMaterialType" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean"
			schema="ChooseMaterialType">						
			<fr:destination name="postBack" path="/manageMaterialSpaceOccupations.do?method=prepareChooseMaterial&page=0"/>
				<fr:layout name="tabular">      										  
		   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
      			</fr:layout> 
		</fr:edit>
	</fr:form>						
	
	<logic:notEmpty name="materialTypeBean" property="materialType">	
		<p class="mtop1 mbottom05"><strong><bean:message key="title.choose.material" bundle="SPACE_RESOURCES"/></strong></p>
		<bean:define id="chooseMaterialSchema">Choose<bean:write name="materialTypeBean" property="materialType.simpleName"/></bean:define>

		<fr:edit name="materialTypeBean" id="materialTypeWithMaterial" type="net.sourceforge.fenixedu.dataTransferObject.spaceManager.MaterialTypeBean"
			schema="<%= chooseMaterialSchema %>" action="<%= prepareInsertMaterialOccupationLink %>">			
			<fr:destination name="cancel" path="<%= showMaterialSpaceOccupationLink %>"/>			
			<fr:destination name="invalid" path="<%= prepareInsertMaterialOccupationLink %>"/>
				<fr:layout name="tabular">      										  
		   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
		   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
      			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	

	<logic:notEmpty name="materialTypeBean" property="material">					
		<p class="mtop1 mbottom05"><strong><bean:message key="title.insert.material.occupation.details" bundle="SPACE_RESOURCES"/></strong></p>
		
		<bean:define id="schemaName">AddNew<bean:write name="materialTypeBean" property="materialType.simpleName"/>SpaceOccupation</bean:define>		
		<bean:define id="type" name="materialTypeBean" property="material.materialSpaceOccupationSubClass.name"/>		
		<bean:define id="materialSlotName" name="materialTypeBean" property="material.materialSpaceOccupationSlotName" />

		<fr:form action="<%= showMaterialSpaceOccupationLink %>">
			<fr:create nested="true" id="materialTypeWithDates" type="<%= type.toString() %>" schema="<%= schemaName %>">			
				<fr:hidden slot="space" name="materialTypeBean" property="spaceInformation.space"/>								
				<fr:hidden slot="<%= materialSlotName.toString() %>" name="materialTypeBean" property="material" />								
				<fr:destination name="invalid" path="<%= prepareInsertMaterialOccupationLink %>"/>
				<fr:layout name="tabular">      										  
		   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
		   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
      			</fr:layout> 
			</fr:create>								

			<fr:edit name="materialTypeBean" id="materialTypeToCreate" visible="false" />						

			<p>
				<html:submit bundle="SPACE_RESOURCES">
					<bean:message key="link.submit" bundle="SPACE_RESOURCES"/>
				</html:submit>
				<html:cancel>
					<bean:message key="link.cancel" bundle="SPACE_RESOURCES"/>
				</html:cancel>
			</p>
		</fr:form>			
	</logic:notEmpty>

</logic:present>
