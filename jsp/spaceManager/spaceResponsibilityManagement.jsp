<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present name="selectedSpaceInformation">

	<br/>
		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
	
	<br/><br/>	
			
	<logic:messagesPresent message="true">
		<span class="error">
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>

	<h3><bean:message key="label.active.responsible.units" bundle="SPACE_RESOURCES"/>:</h3>
	<fr:view schema="ViewSpaceResponsibleUnits" name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
		<fr:layout name="tabular">      			
   			<fr:property name="rowClasses" value="listClasses"/>	
   			<fr:property name="columnClasses" value="listClasses"/>
   			   			
   			<fr:property name="link(edit)" value="<%="/manageSpaceResponsibility.do?method=prepareEditSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="idInternal/spaceResponsibilityID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageSpaceResponsibility.do?method=deleteSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="idInternal/spaceResponsibilityID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
    	</fr:layout>
	</fr:view>			


</logic:present>	