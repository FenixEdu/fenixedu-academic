<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="title.edit.material.occupations" bundle="SPACE_RESOURCES"/></h2>
	
<logic:present name="materialSpaceOccupation">	
	
	<logic:messagesPresent message="true">
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>
	
	<bean:define id="material" name="materialSpaceOccupation" property="material" />
	<div class="infoop3 mvert1">
		<fr:view name="material" type="net.sourceforge.fenixedu.domain.material.Material" schema="ViewMaterialDetails" layout="tabular"/>
	</div>	
	
	<bean:define id="showMaterialSpaceOccupationLink">/manageMaterialSpaceOccupations.do?method=showMaterialSpaceOccupations&page=0&spaceInformationID=<bean:write name="selectedSpaceInformation" property="idInternal"/></bean:define>	
	<bean:define id="schemaName">Edit<bean:write name="materialSpaceOccupation" property="material.class.simpleName"/>SpaceOccupation</bean:define>		
	<bean:define id="type" name="materialSpaceOccupation" property="material.materialSpaceOccupationSubClass.name"/>		
	<bean:define id="materialSlotName" name="materialSpaceOccupation" property="material.materialSpaceOccupationSlotName" />

	<fr:hasMessages for="edit" type="conversion">
		<p>
			<span class="error0">			
				<fr:message for="edit" show="message"/>
			</span>
		</p>
	</fr:hasMessages>
	<fr:edit id="edit" name="materialSpaceOccupation" type="<%= type.toString() %>" schema="<%= schemaName %>" action="<%= showMaterialSpaceOccupationLink %>"/>

</logic:present>
