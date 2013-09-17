<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.space.responsibility.management" bundle="SPACE_RESOURCES"/></h2>

<logic:present name="selectedSpaceInformation">
	
	<bean:define id="space" name="selectedSpaceInformation" property="space" toScope="request"/>
	<bean:define id="selectedSpaceInformationId" name="selectedSpaceInformation" property="externalId" />
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
   			<fr:property name="columnClasses" value="aleft,,,,"/>
   			
   			<fr:property name="link(edit)" value="<%="/manageSpaceResponsibility.do?method=prepareEditSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="externalId/spaceResponsibilityID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageSpaceResponsibility.do?method=deleteSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="externalId/spaceResponsibilityID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>                                           
            <fr:property name="confirmationKey(delete)" value="label.delete.space.responsability.confirmation"/>
            <fr:property name="confirmationBundle(delete)" value="SPACE_RESOURCES"/> 
            
    	</fr:layout>
	</fr:view>
	
	<p class="mtop15 mbottom05"><strong><bean:message key="label.choose.unit" bundle="SPACE_RESOURCES"/></strong></p>
	<logic:notEmpty name="possibleInternalUnits">	
		<logic:iterate id="internalUnit" name="possibleInternalUnits">
			<bean:define id="path">/manageSpaceResponsibility.do?method=manageResponsabilityInterval&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>&unitID=<bean:write name="internalUnit" property="externalId"/></bean:define>	
			<p>
				<html:link page="<%= path %>"><bean:write name="internalUnit" property="nameWithAcronym"/></html:link>
			</p>
		</logic:iterate>
	</logic:notEmpty>
	<logic:empty name="possibleInternalUnits">
		<em><bean:message key="label.empty.internal.units" bundle="SPACE_RESOURCES"/></em>
	</logic:empty>
					
	<p class="mtop2 mbottom05"><strong><bean:message key="label.choose.external.unit" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:define id="pathToAddExternalUnit">/manageSpaceResponsibility.do?method=prepareAddExternalUnit&spaceInformationID=<bean:write name="selectedSpaceInformationId"/></bean:define>	

	<fr:form action="<%= pathToAddExternalUnit %>">	
		<fr:edit id="externalUnit" name="searchExternalPartyBean" schema="search.external.party.autocomplete" >
			<fr:layout name="tabular">      										  
	   			<fr:property name="classes" value="tstyle5 thmiddle thlight mtop05 mbottom1"/>
	   			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
     		</fr:layout>
		</fr:edit>
		<html:submit><bean:message key="button.next" bundle="SPACE_RESOURCES" /></html:submit>
	</fr:form>

			
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
   			<fr:property name="columnClasses" value="aleft,,,,"/>
   			   			
   			<fr:property name="link(edit)" value="<%="/manageSpaceResponsibility.do?method=prepareEditSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="externalId/spaceResponsibilityID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/manageSpaceResponsibility.do?method=deleteSpaceResponsibility&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="externalId/spaceResponsibilityID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>  
            <fr:property name="confirmationKey(delete)" value="label.delete.space.responsability.confirmation"/>
            <fr:property name="confirmationBundle(delete)" value="SPACE_RESOURCES"/> 
                                                     
    	</fr:layout>
	</fr:view>			
	
</logic:present>	