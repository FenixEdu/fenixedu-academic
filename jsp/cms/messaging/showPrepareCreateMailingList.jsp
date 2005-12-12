<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="net.sourceforge.fenixedu.domain.IStudent" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.UserGroup" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.UserGroupTypes"%>

<span class="error"><html:errors/></span>
<logic:present name="group">
	<bean:define id="group" name="group" type="net.sourceforge.fenixedu.domain.accessControl.UserGroup"/>
	<bean:define id="readableGroupType" value="<%=UserGroupTypes.userGroupTypeByClass(group.getClass()).toString()%>"/>
	<h2><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.label"/></h2>	
	<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.type.label"/></b> <bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/><br/>
	<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write name="group" property="name"/><br/>
	<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="group" property="description"/><br/>
	
	<html:form action="/mailingListManagement" method="get">
		<html:hidden property="method" value="create"/>
		<html:hidden property="groupID"/>
		<table>
			<tr>				
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.name.label"/>
				</td>
				<td>
					<html:text property="name"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.description.label"/>
				</td>
				<td>
					<html:text property="description"/>
				</td>
			</tr>
			<tr>				
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.messaging.mailingList.creating.address.label"/>
				</td>
				<td>
					<html:text property="address"/>@lists.fenix.ist.utl.pt
				</td>
			</tr>			
		</table>
		<html:submit styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.save.button"/>
		</html:submit>
	</html:form>
	
</logic:present>