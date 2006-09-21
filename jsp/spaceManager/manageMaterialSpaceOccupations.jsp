<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.space.material.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">	

	<br/>		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>	
	<br/><br/>	
			
	<logic:messagesPresent message="true">
		<span class="error"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<br/>
	</logic:messagesPresent>

	<p><html:link page="/manageMaterialSpaceOccupations.do?method=prepareChooseMaterialType&page=0" paramId="spaceInformationID" paramName="selectedSpaceInformation" paramProperty="idInternal">
		<bean:message bundle="SPACE_RESOURCES" key="link.insert.material.occupations"/>
	</html:link></p>	
	
	<h3><bean:message key="label.active.material.occupations" bundle="SPACE_RESOURCES"/>:</h3>
	<bean:size id="activeSize" name="selectedSpaceInformation" property="space.activeMaterialSpaceOccupationsToLoggedPerson"/>
	<logic:equal name="activeSize" value="0">
		<span class="error"><!-- Error messages go here -->
			<bean:message key="label.space.no.current.materialOccupations" bundle="SPACE_RESOURCES"/>
		</span>	
	</logic:equal>
	<fr:view schema="ViewMaterialSpaceOccupations" name="selectedSpaceInformation" property="space.activeMaterialSpaceOccupationsToLoggedPerson">
		<fr:layout name="tabular">      			
   			<fr:property name="rowClasses" value="listClasses"/>	
   			<fr:property name="columnClasses" value="listClasses"/>
   			<fr:property name="headerClasses" value="listClasses-header"/>   			

   			<fr:property name="link(edit)" value="<%="/manageMaterialSpaceOccupations.do?method=prepareEditMaterialSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="idInternal/materialOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageMaterialSpaceOccupations.do?method=deleteMaterialSpaceOccupation&page=0&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="idInternal/materialOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
    	</fr:layout>
	</fr:view>	

	<br/>	
	<h3><bean:message key="label.other.material.occupations" bundle="SPACE_RESOURCES"/>:</h3>
	<bean:size id="inactiveSize" name="selectedSpaceInformation" property="space.inactiveMaterialSpaceOccupationsToLoggedPerson"/>
	<logic:equal name="inactiveSize" value="0">
		<span class="error"><!-- Error messages go here -->
			<bean:message key="label.space.no.other.materialOccupations" bundle="SPACE_RESOURCES"/>
		</span>	
	</logic:equal>
	<fr:view schema="ViewMaterialSpaceOccupations" name="selectedSpaceInformation" property="space.inactiveMaterialSpaceOccupationsToLoggedPerson">
		<fr:layout name="tabular">      			
   			<fr:property name="rowClasses" value="listClasses"/>	
   			<fr:property name="columnClasses" value="listClasses"/>
   			<fr:property name="headerClasses" value="listClasses-header"/>  
   			   			
   			<fr:property name="link(edit)" value="<%="/manageMaterialSpaceOccupations.do?method=prepareEditMaterialSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="idInternal/materialOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageMaterialSpaceOccupations.do?method=deleteMaterialSpaceOccupation&page=0&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="idInternal/materialOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
    	</fr:layout>
	</fr:view>	

	<br/>
	<bean:define id="backLink">
		/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>	
		
	<html:link page="<%= backLink %>">
		<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link>	
</logic:present>
