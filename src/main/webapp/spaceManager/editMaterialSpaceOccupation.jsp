<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>	
<h2><bean:message key="title.edit.material.occupations" bundle="SPACE_RESOURCES"/></h2>
	
<logic:present name="materialSpaceOccupation">	
	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
	
	<bean:define id="space" name="selectedSpaceInformation" property="space"/>
	
	<logic:messagesPresent message="true">
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>
	
	<bean:define id="material" name="materialSpaceOccupation" property="material" />	
	<fr:view name="material" type="net.sourceforge.fenixedu.domain.material.Material" schema="ViewMaterialDetails" layout="tabular">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mvert15" />
			<fr:property name="rowClasses" value="bold,,," />						
		</fr:layout>
	</fr:view>
		
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
	<fr:edit id="edit" name="materialSpaceOccupation" type="<%= type.toString() %>" schema="<%= schemaName %>" action="<%= showMaterialSpaceOccupationLink %>">
		<fr:layout name="tabular">      										  
			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>			
   		</fr:layout>
	</fr:edit>

</logic:present>
