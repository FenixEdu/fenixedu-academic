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
		<bean:define id="group" name="group" type="net.sourceforge.fenixedu.domain.accessControl.IUserGroup"/>
		<bean:define id="readableGroupType" value="<%=UserGroupTypes.userGroupTypeByClass(group.getClass()).toString()%>"/>
		<h2><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.title.label"/></h2>	
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.creating.type.label"/></b> <bean:message  bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/><br/>
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write name="group" property="name"/><br/>
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="group" property="description"/><br/>

		<bean:define id="numberOfElements" type="java.lang.Integer" property="elementsCount" name="group"/>	
		<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.elementsNumber.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
				</td>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.email.label"/>
				</td>
			</tr>
			<logic:iterate id="person" name="group" property="elementsIterator" type="net.sourceforge.fenixedu.domain.IPerson">
				<tr>
					<td class="listClasses">
						<bean:write name="person" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="person" property="email"/>
					</td>
				</tr>
			</logic:iterate>
	    </table>									
	</logic:present>