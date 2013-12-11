<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="logins.management.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="(role(MANAGER) | role(OPERATOR))">
	
	<logic:messagesPresent message="true">
		<p>
		<span class="error0"><!-- Error messages go here -->
			<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
				<bean:write name="message"/>
			</html:messages>
		</span>
		<p>
	</logic:messagesPresent>	
	
	<fr:form action="/loginsManagement.do?method=searchPerson">		
		<fr:edit name="personBean" id="personBeanID" schema="SearchPersonAttributes">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>	
		</fr:edit>			
		<html:submit><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>	
	</fr:form>
	
	<p>
		<logic:notEmpty name="resultPersons">			
			<fr:view name="resultPersons" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
					<fr:property name="rowClasses" value="listClasses"/>
		            
		            <fr:property name="link(loginTimeIntervalsManagement)" value="/loginsManagement.do?method=prepareManageLoginTimeIntervals"/>
		            <fr:property name="param(loginTimeIntervalsManagement)" value="externalId/personID"/>
			        <fr:property name="key(loginTimeIntervalsManagement)" value="link.manage.login.time.intervals"/>
		            <fr:property name="bundle(loginTimeIntervalsManagement)" value="MANAGER_RESOURCES"/>
		            <fr:property name="order(loginTimeIntervalsManagement)" value="1"/>		     
		                					
				</fr:layout>			
			</fr:view>				
		</logic:notEmpty>			
	</p>


</logic:present>


