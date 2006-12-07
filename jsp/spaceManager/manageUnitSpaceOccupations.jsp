<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.unit.space.occupations.management" bundle="SPACE_RESOURCES"/></h2>

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
	<bean:define id="space" name="selectedSpaceInformation" property="space"/>
		
	<bean:define id="backLink">/manageSpaces.do?method=manageSpace&page=0&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>		
	<ul class="mvert15 list5">
		<li>
			<html:link page="<%= backLink %>">
				<bean:message key="link.return" bundle="SPACE_RESOURCES"/>
			</html:link>
		</li>
	</ul>
			
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		</p>
	</logic:messagesPresent>

	<p class="mtop2 mbottom05"><strong><bean:message key="label.active.unit.occupations" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="activeSize" name="selectedSpaceInformation" property="space.activeUnitSpaceOccupations"/>
	<logic:equal name="activeSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.current.unit.occupations" bundle="SPACE_RESOURCES"/>
		</em>	
	</logic:equal>
	<fr:view schema="ViewUnitSpaceOccupationsWithInterval" name="selectedSpaceInformation" property="space.activeUnitSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			<fr:property name="columnClasses" value="aleft,,,,"/>
   
   			<fr:property name="link(edit)" value="<%="/manageUnitSpaceOccupations.do?method=prepareEditUnitSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="idInternal/unitSpaceOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageUnitSpaceOccupations.do?method=deleteUnitSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="idInternal/unitSpaceOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
            
    	</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.unit" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:define id="path">/SpaceManager/manageUnitSpaceOccupations.do?method=prepareManageUnitOccupationInterval&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>	
	<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= path %>" state="true"/>
				
	<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.external.unit" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:define id="pathToAddExternalUnit">/manageUnitSpaceOccupations.do?method=prepareAddExternalUnit&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>	

	<fr:form action="<%= pathToAddExternalUnit %>">	
		<fr:edit id="externalUnit" name="searchExternalPartyBean" schema="search.external.party.autocomplete" >
			<fr:layout name="tabular">      										  
	   			<fr:property name="classes" value="tstyle1 thmiddle thlight mtop05 mbottom1"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
     		</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.next" bundle="SPACE_RESOURCES" /></html:submit>
	</fr:form>

			
	<p class="mtop2 mbottom05"><strong><bean:message key="label.other.unit.space.occupations" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="inactiveSize" name="selectedSpaceInformation" property="space.inactiveUnitSpaceOccupations"/>
	<logic:equal name="inactiveSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.other.unit.occupations" bundle="SPACE_RESOURCES"/>
		</em>	
	</logic:equal>
	<fr:view schema="ViewUnitSpaceOccupationsWithInterval" name="selectedSpaceInformation" property="space.inactiveUnitSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			<fr:property name="columnClasses" value="aleft,,,,"/>
   			   			
   			<fr:property name="link(edit)" value="<%="/manageUnitSpaceOccupations.do?method=prepareEditUnitSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="idInternal/unitSpaceOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageUnitSpaceOccupations.do?method=deleteUnitSpaceOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="idInternal/unitSpaceOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
    	</fr:layout>
	</fr:view>			
	
</logic:present>	