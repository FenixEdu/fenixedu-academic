<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:present name="userGroupTypeToAdd">
	<bean:define id="userGroupTypeToAdd" type="net.sourceforge.fenixedu.domain.accessControl.GroupTypes" name="userGroupTypeToAdd"/>
	<e:define id="userGroupTypeToAddString" enumeration="userGroupTypeToAdd" bundle="ENUMERATION_RESOURCES"/>
    
	<h2><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label"/></h2>
	<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.label"/> <b><bean:write name="userGroupTypeToAddString"/></b>
	
	<html:form action="/personalGroupsManagement" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.userGroupType" property="userGroupType"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="parameterizeGroup"/>
        
		<table>
			<tr>				
				<td>
					<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.name.label"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.description.label"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.description" property="description"/>
				</td>
			</tr>
		</table>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message  bundle="CMS_RESOURCES" key="cms.continue.button"/>
		</html:submit>
	</html:form>
    
</logic:present>