<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="edit.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">
	<logic:notEmpty name="person">

		<logic:messagesPresent message="true">
			<p>
				<span class="error0"><!-- Error messages go here -->
					<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
						<bean:write name="message"/>
					</html:messages>
				</span>
			</p>
		</logic:messagesPresent>

		<fr:form action="/editPerson.do?method=prepareSearchPersonToEdit">		
						
			<p><b>a) <bean:message key="label.person.identification.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<fr:edit nested="true" name="person" id="PersonWithIdentificationInfo" schema="PersonIdentificationInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>			
								
			<p><b>b) <bean:message key="label.invitedPerson.personal.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<fr:edit nested="true" name="person" id="PersonWithPersonalInfo" schema="PersonPersonalInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>
							
			<p><b>c) <bean:message key="label.invitedPerson.filiation.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<fr:edit nested="true" name="person" id="PersonWithFiliationInfo" schema="PersonFiliationInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>		
			
			<p><b>d) <bean:message key="label.invitedPerson.residence.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<fr:edit nested="true" name="person" id="PersonWithResidenceInfo" schema="PersonResidenceInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>		
			
			<p><b>e) <bean:message key="label.invitedPerson.contacts.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<fr:edit nested="true" name="person" id="PersonWithContactsInfo" schema="PersonContactsInfo">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>				
						
			<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
		</fr:form>
		
	</logic:notEmpty>	
</logic:present>