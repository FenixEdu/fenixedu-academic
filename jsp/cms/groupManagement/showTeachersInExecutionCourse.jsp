<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>
	<logic:present name="elements">
		
		<bean:size name="elements" id="numberOfElements" />		
		<bean:message  bundle="CMS_RESOURCES" key="person.userGroupsManagement.elements.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
				</td>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.teacher.category.label"/>
				</td>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.email.label"/>
				</td>
			</tr>
			<logic:iterate id="person" name="elements" type="net.sourceforge.fenixedu.domain.Person">
			    <bean:define id="teacher" name="person" property="teacher"/>
				<tr>
					<td class="listClasses">
						<bean:write name="teacher" property="person.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacher" property="category.longName"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacher" property="person.email"/>
					</td>
				</tr>
			</logic:iterate>
	    </table>									
	</logic:present>