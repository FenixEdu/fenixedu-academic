<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 1</strong>: <bean:message key="label.personManagement.merge.choose.persons" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 2</strong>: <bean:message key="label.personManagement.merge.transfer.personal.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 3</strong>: <bean:message key="label.personManagement.merge.transfer.events.and.accounts" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 4</strong>: <bean:message key="label.personManagement.merge.transfer.student.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 5</strong>: <bean:message key="label.personManagement.merge.transfer.registrations" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 6</strong>: <bean:message key="label.personManagement.merge.delete.student" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 7</strong>: <bean:message key="label.personManagement.merge.delete.person" bundle="MANAGER_RESOURCES" /> </span>
</p>

<bean:define id="linkLeft">
	/mergePersons.do?method=mergeProperty&classToMerge=<bean:write name="classToMerge" />&object2ExternalId=<bean:write name="mergePersonsBean" property="rightOid"/>&object1ExternalId=<bean:write name="mergePersonsBean" property="leftOid" />&source=2&slotName=
</bean:define>
<bean:define id="linkRight">
	/mergePersons.do?method=mergeProperty&classToMerge=<bean:write name="classToMerge" />&object2ExternalId=<bean:write name="mergePersonsBean" property="rightOid" />&object1ExternalId=<bean:write name="mergePersonsBean" property="leftOid" />&source=1&slotName=
</bean:define>

<table>
	<logic:iterate id="slot" name="slots" >	
		<bean:define id="currentLinkLeft">
			<bean:write name="linkLeft" /><bean:write name="slot" property="name" />		
		</bean:define>
		<bean:define id="currentLinkRight">
			<bean:write name="linkRight" /><bean:write name="slot" property="name" />		
		</bean:define>
		<tr >
			<td class="infoop"><strong><bean:write name="slot" property="name" /></strong></td>		
			<td class="infoop"><bean:write name="slot" property="type" /></td>		
			<td> 
				<logic:notEmpty name="slot" property="value1Link" >
					<bean:define id="referenceLinkLeft" name="slot" property="value1Link" type="java.lang.String" />
					<html:link module="" page="<%= referenceLinkLeft %>" target="blank" > <bean:write name="slot" property="value1" /> </html:link>
				</logic:notEmpty>
				<logic:empty name="slot" property="value1Link" >
					<bean:write name="slot" property="value1" />
				</logic:empty>				
			</td>		
			<td class="infoop" >
				 <-- 
				 | 
				 --> 
			</td>		
			<td> 
				<logic:notEmpty name="slot" property="value2Link" >
					<bean:define id="referenceLinkRight" name="slot" property="value2Link" type="java.lang.String" />
					<html:link module="" page="<%= referenceLinkRight %>" target="blank" > <bean:write name="slot" property="value2" /> </html:link>				
				</logic:notEmpty>
				<logic:empty name="slot" property="value2Link" >
					<bean:write name="slot" property="value2" />
				</logic:empty>
			</td>					
		</tr>
	</logic:iterate>
</table>

<p>&nbsp;</p>

<fr:form action="/mergePersons.do?method=transferEventsAndAccounts">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<html:submit><bean:message key="label.transfer.events.and.accounts" bundle="MANAGER_RESOURCES" /></html:submit>

</fr:form>

<fr:form action="/mergePersons.do?method=mergeStudents">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<html:submit><bean:message key="label.continue" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
