<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
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


		
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="idInternal" />
	<jsp:include page="spaceCrumbs.jsp"/>
	
	<bean:define id="backLink">
		/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>		
	<ul class="mvert15 list5">
		<li>
			<html:link page="<%= backLink %>">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
			
	<logic:messagesPresent message="true">
		<em><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</em>
	</logic:messagesPresent>

	<p class="mtop2 mbottom05"><strong><bean:message key="label.active.responsible.units" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="activeSize" name="selectedSpaceInformation" property="space.activeSpaceResponsibility"/>
	<logic:equal name="activeSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.current.responsible.units" bundle="SPACE_RESOURCES"/>
		</em>	
	</logic:equal>
	<fr:view schema="ViewSpaceResponsibleUnitsWithInterval" name="selectedSpaceInformation" property="space.activeSpaceResponsibility">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			
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
		
	<span class="mtop2 mbottom05"><strong><bean:message key="label.choose.unit" bundle="SPACE_RESOURCES"/></strong></span>
	<bean:define id="path">
		/SpaceManager/manageSpaceResponsibility.do?method=manageResponsabilityInterval&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>
	</bean:define>	
	<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= path %>" state="true"/>
	
	<p class="mtop2 mbottom05"><strong><bean:message key="label.other.responsible.units" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="inactiveSize" name="selectedSpaceInformation" property="space.inactiveSpaceResponsibility"/>
	<logic:equal name="inactiveSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.other.responsible.units" bundle="SPACE_RESOURCES"/>
		</em>	
	</logic:equal>
	<fr:view schema="ViewSpaceResponsibleUnitsWithInterval" name="selectedSpaceInformation" property="space.inactiveSpaceResponsibility">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			   			
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