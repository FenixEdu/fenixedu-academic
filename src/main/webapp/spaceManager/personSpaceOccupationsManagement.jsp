<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message bundle="SPACE_RESOURCES" key="space.manager.page.title"/></em>
<h2><bean:message key="label.occupations.management" bundle="SPACE_RESOURCES"/></h2>


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
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="SPACE_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
	</logic:messagesPresent>
	
	<p class="mtop2 mbottom05"><strong><bean:message key="label.active.person.occupations" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="activeSize" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations"/>
	<logic:equal name="activeSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.current.personOccupations" bundle="SPACE_RESOURCES"/>
		</em>	
	</logic:equal>
	<fr:view schema="PersonSpaceOccupations" name="selectedSpaceInformation" property="space.activePersonSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			   			
   			<fr:property name="link(edit)" value="<%="/managePersonSpaceOccupations.do?method=prepareEditSpacePersonOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="externalId/spaceOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
            <fr:property name="link(delete)" value="<%="/managePersonSpaceOccupations.do?method=deleteSpacePersonOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="externalId/spaceOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>
            <fr:property name="confirmationKey(delete)" value="label.delete.person.space.occupation.confirmation"/>
            <fr:property name="confirmationBundle(delete)" value="SPACE_RESOURCES"/> 
                                                      
    	</fr:layout>
	</fr:view>			
	
	<bean:define id="exceptionLink" value="<%="/managePersonSpaceOccupations.do?method=showSpaceOccupations&spaceInformationID="+ selectedSpaceInformationId %>" />		
	<logic:empty name="personSpaceOccupation">		
		<p class="mbottom05"><strong><bean:message key="label.add.person" bundle="SPACE_RESOURCES"/></strong></p>
		<fr:hasMessages for="create" type="conversion">
			<p>
				<span class="error0">			
					<fr:message for="create" show="message"/>
				</span>
			</p>
		</fr:hasMessages>
		<fr:create id="create" type="net.sourceforge.fenixedu.domain.space.PersonSpaceOccupation" schema="AddPersonSpaceOccupation">	   	
			<fr:hidden slot="space" name="selectedSpaceInformation" property="space" />
			<fr:destination name="exception" path="<%= exceptionLink %>" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:create>
	</logic:empty>
		
	<logic:notEmpty name="personSpaceOccupation">	
		<bean:define id="exceptionLink2">/managePersonSpaceOccupations.do?method=prepareEditSpacePersonOccupation&spaceInformationID=<bean:write name="selectedSpaceInformationId"/>&spaceOccupationID=<bean:write name="personSpaceOccupation" property="externalId"/></bean:define>			
		<p class="mbottom05"><strong><bean:message key="label.edit.occupation" bundle="SPACE_RESOURCES"/></strong></p>							
		<p><i><bean:write name="personSpaceOccupation" property="person.name"/> -> <bean:write name="personSpaceOccupation" property="person.username"/></i></p>
		<fr:hasMessages for="edit" type="conversion">
			<p>
				<span class="error0">			
					<fr:message for="edit" show="message"/>
				</span>
			</p>
		</fr:hasMessages>
		<fr:edit id="edit" name="personSpaceOccupation" action="<%= exceptionLink %>" schema="EditPersonSpaceOccupation">	   				
			<fr:destination name="exception" path="<%= exceptionLink2 %>" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thright thlight mtop05"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>	
	</logic:notEmpty>
		
	<p class="mtop2 mbottom05"><strong><bean:message key="label.inactive.person.occupations" bundle="SPACE_RESOURCES"/></strong></p>
	<bean:size id="inactiveSize" name="selectedSpaceInformation" property="space.inactivePersonSpaceOccupations"/>
	<logic:equal name="inactiveSize" value="0">
		<em><!-- Error messages go here -->
			<bean:message key="label.space.no.other.personOccupations" bundle="SPACE_RESOURCES"/>
		</em>
	</logic:equal>
	<fr:view schema="PersonSpaceOccupations" name="selectedSpaceInformation" property="space.inactivePersonSpaceOccupations">
		<fr:layout name="tabular">      			
   			<fr:property name="classes" value="tstyle4 thlight tdcenter mtop05"/>
   			   			   			
   			<fr:property name="link(edit)" value="<%="/managePersonSpaceOccupations.do?method=prepareEditSpacePersonOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(edit)" value="externalId/spaceOccupationID"/>
	        <fr:property name="key(edit)" value="link.edit"/>
            <fr:property name="bundle(edit)" value="SPACE_RESOURCES"/>
            <fr:property name="order(edit)" value="0"/>
            
			<fr:property name="link(delete)" value="<%="/managePersonSpaceOccupations.do?method=deleteSpacePersonOccupation&spaceInformationID=" + selectedSpaceInformationId %>"/>
            <fr:property name="param(delete)" value="externalId/spaceOccupationID"/>
	        <fr:property name="key(delete)" value="link.delete"/>
            <fr:property name="bundle(delete)" value="SPACE_RESOURCES"/>
            <fr:property name="order(delete)" value="0"/>           
			<fr:property name="confirmationKey(delete)" value="label.delete.person.space.occupation.confirmation"/>
            <fr:property name="confirmationBundle(delete)" value="SPACE_RESOURCES"/> 
   
    	</fr:layout>
	</fr:view>			
</logic:present>