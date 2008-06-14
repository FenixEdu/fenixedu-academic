<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/units.tld" prefix="un" %>

<h2><bean:message key="person.invitation.create.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

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

	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>
		
	<logic:empty name="invitedPersonBean" property="unit">				
		<p class="breadcumbs"><span class="actual"><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/> > <span><bean:message key="label.create.new.invited.person.four.step" bundle="MANAGER_RESOURCES"/></span></p>
		<bean:define id="goToPrepareCreateNewPersonInvitationURL1" type="java.lang.String">/manager/invitationsManagement.do?method=prepareCreateNewPersonInvitation&personID=<bean:write name="invitedPersonBean" property="invitedPerson.idInternal"/></bean:define>
		<un:tree initialUnit="initialUnit" unitParamName="unitID" path="<%= goToPrepareCreateNewPersonInvitationURL1 %>" state="true"/>										
	</logic:empty>

	<logic:notEmpty name="invitedPersonBean" property="unit">
		<logic:empty name="invitedPersonBean" property="responsible">

			<p class="breadcumbs"><span><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.four.step" bundle="MANAGER_RESOURCES"/></span></p>
			
			<p class="infoop2">
				<b><bean:message key="label.host.unit" bundle="MANAGER_RESOURCES"/>:</b> <bean:write name="invitedPersonBean" property="unit.presentationNameWithParents"/>
			</p>
			
			<br/>				
			&nbsp;&nbsp;<em><bean:message key="label.choose.responsibility.unit" bundle="MANAGER_RESOURCES"/></em>
			<bean:define id="goToPrepareCreateNewPersonInvitationURL2" type="java.lang.String">/manager/invitationsManagement.do?method=prepareCreateNewPersonInvitation&unitID=<bean:write name="invitedPersonBean" property="unit.idInternal"/>&personID=<bean:write name="invitedPersonBean" property="invitedPerson.idInternal"/></bean:define>
			<un:tree initialUnit="initialUnit" unitParamName="responsibilityUnitID" path="<%= goToPrepareCreateNewPersonInvitationURL2 %>" state="true"/>								
						
			<br/>			
			&nbsp;&nbsp;<em><bean:message key="label.choose.responsibility.person" bundle="MANAGER_RESOURCES"/></em>
			<fr:form action="/invitationsManagement.do?method=prepareCreateNewPersonInvitation">					
				<fr:edit name="invitedPersonBean" id="invitedPersonBeanWithResponsibilityParty" schema="ChooseInvitedPersonResponsibilityPerson" />						
				<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
			</fr:form>	
										
		</logic:empty>
	</logic:notEmpty>

	<logic:notEmpty name="invitedPersonBean" property="unit">
		<logic:notEmpty name="invitedPersonBean" property="responsible">				
			<p class="breadcumbs"><span><bean:message key="label.create.new.invited.person.first.step" bundle="MANAGER_RESOURCES"/></span> > <span><bean:message key="label.create.new.invited.person.second.step" bundle="MANAGER_RESOURCES"/></span> > <span class="actual"><bean:message key="label.create.new.invited.person.four.step" bundle="MANAGER_RESOURCES"/></span></p>
			
			<p class="infoop2">
				<b><bean:message key="label.host.unit" bundle="MANAGER_RESOURCES"/>:</b> 		
				<bean:write name="invitedPersonBean" property="unit.presentationNameWithParents"/>		
				<br/>						
				<logic:notEmpty name="invitedPersonBean" property="responsiblePerson">
					<b><bean:message key="label.responsibility.person" bundle="MANAGER_RESOURCES"/>:</b>
					<bean:write name="invitedPersonBean" property="responsible.name"/> (<bean:write name="invitedPersonBean" property="responsible.username"/>)
				</logic:notEmpty>
				<logic:empty name="invitedPersonBean" property="responsiblePerson">
					<b><bean:message key="label.responsibility.unit" bundle="MANAGER_RESOURCES"/>:</b> 		
					<bean:write name="invitedPersonBean" property="responsible.presentationNameWithParents"/>
				</logic:empty>
			</p>
			
			
			<fr:edit name="invitedPersonBean" schema="NewInvitedPersonLoginInfo" id="invitedPersonBeanWithTimeInterval" 
				action="invitationsManagement.do?method=createNewPersonInvitation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>					
			</fr:edit>														
		</logic:notEmpty>
	</logic:notEmpty>
	
</logic:present>	