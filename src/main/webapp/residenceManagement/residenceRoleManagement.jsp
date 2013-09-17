<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2> <bean:message key="title.residence.Role.Management" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h2> 


<fr:form action="/residenceRoleManagement.do?method=addResidenceRoleManagemenToPerson">
	<fr:edit id="residenceRoleManagement" name="residenceRoleManagement" schema="residenceRoleManagment.searchPerson.autoComplete">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright mvert05 thmiddle"/>
			<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
	<p class="mtop05">
		<html:submit>
			<bean:message key="button.residence.assign.role" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>

<h3> <bean:message key="title.residence.list.roles" bundle="RESIDENCE_MANAGEMENT_RESOURCES"/></h3> 
<logic:present name="persons" scope="request">
	<fr:view name="persons" schema="PersonNameAndUsername">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="sortBy" value="name"/>
			<fr:property name="linkFormat(remove)" value='<%= "/residenceRoleManagement.do?method=removeResidenceRoleManagemenToPerson&amp;personUsername=${username}"%>' />
			<fr:property name="key(remove)" value="label.remove"/>
			<fr:property name="bundle(remove)" value="APPLICATION_RESOURCES" />
			<fr:property name="contextRelative(remove)" value="true"/>
			<fr:property name="confirmationKey(remove)" value="label.residence.delete.role"/>
			<fr:property name="confirmationBundle(remove)" value="RESIDENCE_MANAGEMENT_RESOURCES"/>
		</fr:layout>
	</fr:view>
</logic:present>