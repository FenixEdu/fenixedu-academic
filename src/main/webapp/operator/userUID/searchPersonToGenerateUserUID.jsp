<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="generate.userUID.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="role(OPERATOR)">
	
	<logic:messagesPresent message="true">
		<p>
			<span class="error0"><!-- Error messages go here -->
				<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
					<bean:write name="message"/>
				</html:messages>
			</span>
		<p>
	</logic:messagesPresent>	
	
	<fr:form action="/generateUserUID.do?method=searchPerson">		
		<fr:edit name="personBean" id="personBeanID" schema="SearchPersonAttributesToGenerateUserUID">
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>	
		</fr:edit>			
		<html:submit><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>	
	</fr:form>
	
	<p>
		<logic:notEmpty name="resultPersons">			
			<fr:view name="resultPersons" schema="ShowExistentPersonsDetailsBeforeGenerateUserUID" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 tdcenter"/>
					<fr:property name="columnClasses" value=",,,,bold"/>
																					
		   			<fr:property name="link(aliasManagement)" value="/generateUserUID.do?method=generateUserUID"/>
		            <fr:property name="param(aliasManagement)" value="externalId/personID"/>
			        <fr:property name="key(aliasManagement)" value="link.generate.institutional.username"/>
		            <fr:property name="bundle(aliasManagement)" value="MANAGER_RESOURCES"/>
		            <fr:property name="order(aliasManagement)" value="0"/>		     
		            		          				
				</fr:layout>			
			</fr:view>				
		</logic:notEmpty>			
	</p>

</logic:present>


