<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="generate.userUID.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:present role="OPERATOR">
	
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
				<fr:property name="classes" value="tstyle1"/>
		        <fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>	
		</fr:edit>			
		<html:submit><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>	
	</fr:form>
	
	<p>
		<logic:notEmpty name="resultPersons">			
			<fr:view name="resultPersons" schema="ShowExistentPersonsDetailsBeforeGenerateUserUID" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
					<fr:property name="rowClasses" value="listClasses"/>					
																					
		   			<fr:property name="link(aliasManagement)" value="/generateUserUID.do?method=generateUserUID"/>
		            <fr:property name="param(aliasManagement)" value="idInternal/personID"/>
			        <fr:property name="key(aliasManagement)" value="link.generate.institutional.username"/>
		            <fr:property name="bundle(aliasManagement)" value="MANAGER_RESOURCES"/>
		            <fr:property name="order(aliasManagement)" value="0"/>		     
		            		          				
				</fr:layout>			
			</fr:view>				
		</logic:notEmpty>			
	</p>

</logic:present>


