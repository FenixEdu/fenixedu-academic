<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.domain.PersonalGroup" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.Group" %>
<%@ page import="net.sourceforge.fenixedu.domain.accessControl.GroupTypes"%>

<span class="error"><html:errors/></span>
	<logic:present name="group">
		<bean:define id="group" name="group" type="net.sourceforge.fenixedu.domain.PersonalGroup"/>
		<bean:define id="readableGroupType" value="<%= GroupTypes.userGroupTypeByClass(group.getGroup().getClass()).toString() %>"/>
        
		<h2><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.title.label"/></h2>	
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.creating.type.label"/></b> <bean:message  bundle="CMS_RESOURCES" name="readableGroupType" bundle="ENUMERATION_RESOURCES"/><br/>
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>:</b> <bean:write name="group" property="name"/><br/>
		<b><bean:message  bundle="CMS_RESOURCES" key="cms.description.label"/>:</b> <bean:write name="group" property="description"/><br/>

        <br/>
		<bean:define id="numberOfElements" type="java.lang.Integer" name="group" property="group.elementsCount"/>	
		<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.elementsNumber.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
				</th>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.email.label"/>
				</th>
			</tr>
			<logic:iterate id="person" name="group" property="group.elementsIterator" type="net.sourceforge.fenixedu.domain.Person">
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