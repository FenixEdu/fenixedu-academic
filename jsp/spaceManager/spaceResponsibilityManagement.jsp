<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.space.responsibility.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">

	<script language="JavaScript">
		function check(e,v)
		{	
			var contextPath = '<%= request.getContextPath() %>';	
			if (e.style.display == "none")
			  {
			  e.style.display = "";
			  v.src = contextPath + '/images/toggle_minus10.gif';
			  }
			else
			  {
			  e.style.display = "none";
			  v.src = contextPath + '/images/toggle_plus10.gif';
			  }
		}
	</script>

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
	<bean:size id="activeSize" name="selectedSpaceInformation" property="space.activeSpaceResponsibility"/>
	<logic:equal name="activeSize" value="0">
		<span class="error">
			<bean:message key="label.space.no.current.responsible.units" bundle="SPACE_RESOURCES"/>
		</span>	
	</logic:equal>
	<fr:view schema="ViewSpaceResponsibleUnitsWithInterval" name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
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
		
	<h3><bean:message key="label.choose.unit" bundle="SPACE_RESOURCES"/>:</h3>
	<bean:write name="unitsList" filter="false"/>	
	
	<h3><bean:message key="label.other.responsible.units" bundle="SPACE_RESOURCES"/>:</h3>
	<bean:size id="inactiveSize" name="selectedSpaceInformation" property="space.inactiveSpaceResponsibility"/>
	<logic:equal name="inactiveSize" value="0">
		<span class="error">
			<bean:message key="label.space.no.other.responsible.units" bundle="SPACE_RESOURCES"/>
		</span>	
	</logic:equal>
	<fr:view schema="ViewSpaceResponsibleUnitsWithInterval" name="selectedSpaceInformation" property="space.inactiveSpaceResponsibility">
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

	<br/>
	<bean:define id="backLink">
		/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>	
		
	<html:link page="<%= backLink %>">
		<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
	</html:link>	
	
</logic:present>	