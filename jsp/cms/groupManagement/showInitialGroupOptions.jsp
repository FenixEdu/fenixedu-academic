<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="userGroupTypeToAdd">
	<bean:define id="userGroupTypeToAdd" type="net.sourceforge.fenixedu.domain.accessControl.GroupTypes" name="userGroupTypeToAdd"/>
	<e:define id="userGroupTypeToAddString" enumeration="userGroupTypeToAdd" bundle="ENUMERATION_RESOURCES"/>
    
	<h2><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.title.label"/></h2>
	<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.label"/> <b><bean:write name="userGroupTypeToAddString"/></b>
	
	<html:form action="/userGroupsManagement" method="get">
		<html:hidden property="userGroupType"/>
		<html:hidden property="method" value="parameterizeGroup"/>
        
		<table>
			<tr>				
				<td>
					<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.name.label"/>
				</td>
				<td>
					<html:text property="name"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.description.label"/>
				</td>
				<td>
					<html:text property="description"/>
				</td>
			</tr>
		</table>
		<html:submit styleClass="inputbutton">
					<bean:message  bundle="CMS_RESOURCES" key="cms.continue.button"/>
		</html:submit>
	</html:form>
    
</logic:present>