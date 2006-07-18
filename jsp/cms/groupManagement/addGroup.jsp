<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.GroupTypes"%>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.Group"%>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label"/></h2>
<bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.saved.info"/>

<bean:define id="group" type="net.sourceforge.fenixedu.domain.PersonalGroup" name="group"/>
<%
    GroupTypes groupType = GroupTypes.userGroupTypeByClass(group.getGroup().getClass());
    request.setAttribute("groupType", groupType);
%>

<e:define id="readableGroupType" enumeration="groupType" bundle="ENUMERATION_RESOURCES"/>

<table>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.type.label"/></b>
			:
		</td>
		<td>
			<bean:write name="readableGroupType"/><br/>
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.name.label"/></b>
			:
		</td>
		<td>
			<bean:write name="group" property="name"/><br/>
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.description.label"/></b>
			:
		</td>
		<td>
			<bean:write name="group" property="description"/><br/>
		</td>
	</tr>
</table>
	<%
	Map params = new HashMap();
	params.put("method", "viewElements");
	params.put("groupId", group.getIdInternal());
	request.setAttribute("params",params);
	 %>
<html:link  name="params" module="/cms" action="/personalGroupsManagement" target="_blank" ><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.viewElements.link"/></html:link><br/>
<html:link  module="/cms" page="/personalGroupsManagement.do?method=prepare" ><bean:message bundle="MANAGER_RESOURCES" bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.label"/></html:link><br/>