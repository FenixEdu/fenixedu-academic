<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.space.responsibility.management" bundle="SPACE_RESOURCES"/></h2>

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

	<bean:define id="backLink">
		/manageSpaceResponsibility.do?method=showSpaceResponsibility&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>	
		
	<logic:empty name="spaceResponsibility">
	<div class="infoop2">
		<p><b><bean:message key="label.unit.name" bundle="SPACE_RESOURCES"/>:</b>&nbsp;<bean:write name="unit" property="name"/></p> 
		<p><b><bean:message key="label.unit.costCenterCode" bundle="SPACE_RESOURCES"/>:</b>&nbsp;<bean:write name="unit" property="costCenterCode"/></p> 			
	</div>		
		<br/>
		<fr:create id="create" action="<%= backLink %>" type="net.sourceforge.fenixedu.domain.space.SpaceResponsibility"
				   schema="CreateSpaceResponsibilityInterval">	   	
			<fr:hidden slot="space" name="selectedSpaceInformation" property="space" />
			<fr:hidden slot="unit" name="unit" />
		</fr:create>		
	</logic:empty>	
	<logic:notEmpty name="spaceResponsibility">
		<div class="infoop2">
			<p><b><bean:message key="label.unit.name" bundle="SPACE_RESOURCES"/>:</b>&nbsp;<bean:write name="spaceResponsibility" property="unit.name"/></p> 
			<p><b><bean:message key="label.unit.costCenterCode" bundle="SPACE_RESOURCES"/>:</b>&nbsp;<bean:write name="spaceResponsibility" property="unit.costCenterCode"/></p> 			
		</div>		
		<br/>
		<fr:edit name="spaceResponsibility" action="<%= backLink %>" schema="EditSpaceResponsibility" />
	</logic:notEmpty>
		
	<br/>		
	<html:link page="<%= backLink %>">
		<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link>	
	
</logic:present>