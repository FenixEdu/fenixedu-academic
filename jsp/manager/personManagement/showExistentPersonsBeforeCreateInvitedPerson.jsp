<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="create.invited.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="MANAGER">

	<logic:notEmpty name="createdPerson">
		<b><bean:message key="label.invitedPerson.created.with.success" bundle="MANAGER_RESOURCES"/>:</b>
		<fr:view name="createdPerson" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
				<fr:property name="rowClasses" value="listClasses"/>					
			</fr:layout>
		</fr:view>	
	</logic:notEmpty>

	<logic:empty name="createdPerson">
		<b><bean:message key="label.verify.if.invitedPerson.already.exists" bundle="MANAGER_RESOURCES"/></b>
		<fr:form action="/createInvitedPerson.do?method=showExistentPersonsWithSameMandatoryDetails">		
			<fr:edit name="anyPersonSearchBean" id="anyPersonSearchBeanId" schema="InsertMandatoryInvitedPersonDetails">
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle1"/>
			        <fr:property name="columnClasses" value=",,noborder"/>
				</fr:layout>	
			</fr:edit>			
			<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>	
		</fr:form>
		
		<p>
			<logic:notEmpty name="resultPersons">		
				<fr:view name="resultPersons" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons" >
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle4"/>
						<fr:property name="rowClasses" value="listClasses"/>					
					</fr:layout>			
				</fr:view>	
				
				<bean:define id="url" type="java.lang.String">/createInvitedPerson.do?method=prepareCreateInvitedPerson&name=<bean:write name="anyPersonSearchBean" property="name"/>&idDocumentType=<bean:write name="anyPersonSearchBean" property="idDocumentType"/>&documentIdNumber=<bean:write name="anyPersonSearchBean" property="documentIdNumber"/></bean:define>
				<html:link action="<%= url %>">
					<bean:message key="link.create.invited.person.because.does.not.exist" bundle="MANAGER_RESOURCES"/>
				</html:link>			
			</logic:notEmpty>				
		</p>
	</logic:empty>	
					
</logic:present>

