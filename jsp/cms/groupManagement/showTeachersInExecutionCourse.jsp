<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>
	<logic:present name="elements">
		
		<bean:size name="elements" id="numberOfElements" />		
		<bean:message  bundle="CMS_RESOURCES" key="person.personalGroupsManagement.elements.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
				</th>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.teacher.category.label"/>
				</th>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.email.label"/>
				</th>
			</tr>
			<logic:iterate id="person" name="elements" type="net.sourceforge.fenixedu.domain.Person">
			    <bean:define id="teacher" name="person" property="teacher"/>
				<tr>
					<td class="listClasses">
						<bean:write name="teacher" property="person.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacher" property="category.name.content"/>
					</td>
					<td class="listClasses">
						<bean:write name="teacher" property="person.email"/>
					</td>
				</tr>
			</logic:iterate>
	    </table>									
	</logic:present>