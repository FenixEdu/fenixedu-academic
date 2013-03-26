<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.mergePersons" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:define id="linkLeft">
	/mergeObjects.do?method=mergeProperty&classToMerge=<bean:write name="classToMerge" />&object2IdInternal=<bean:write name="object2IdInternal" />&object1IdInternal=<bean:write name="object1IdInternal" />&source=2&slotName=
</bean:define>
<bean:define id="linkRight">
	/mergeObjects.do?method=mergeProperty&classToMerge=<bean:write name="classToMerge" />&object2IdInternal=<bean:write name="object2IdInternal" />&object1IdInternal=<bean:write name="object1IdInternal" />&source=1&slotName=
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
				<html:link module="/manager" page="<%= currentLinkLeft %>" > <-- </html:link>
				 | 
				<html:link module="/manager" page="<%= currentLinkRight %>" > --> </html:link>
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
	
	<bean:define id="linkDeleteLeft">
		/mergeObjects.do?method=delete&classToMerge=<bean:write name="classToMerge" />&object2IdInternal=<bean:write name="object2IdInternal" />&object1IdInternal=<bean:write name="object1IdInternal" />&objectIdInternal=<bean:write name="object1IdInternal" />
	</bean:define>
	<bean:define id="linkDeleteRight">
		/mergeObjects.do?method=delete&classToMerge=<bean:write name="classToMerge" />&object2IdInternal=<bean:write name="object2IdInternal" />&object1IdInternal=<bean:write name="object1IdInternal" />&objectIdInternal=<bean:write name="object2IdInternal" />
	</bean:define>		
	<tr>
		<td></td>
		<td></td>
		<td><html:link module="/manager" page="<%= linkDeleteLeft %>" ><strong><bean:message bundle="MANAGER_RESOURCES" key="message.manager.delete" /></strong></html:link></td>
		<td></td>
		<td><html:link module="/manager" page="<%= linkDeleteRight %>" ><strong><bean:message bundle="MANAGER_RESOURCES" key="message.manager.delete" /></strong></html:link></td>
	</tr>
</table>
<p><html:link module="/manager" page="/mergeObjects.do?method=prepare" >voltar</html:link></p>