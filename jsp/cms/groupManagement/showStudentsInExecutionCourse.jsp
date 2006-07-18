<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="net.sourceforge.fenixedu.domain.Student" %>

<span class="error"><html:errors/></span>
	<logic:present name="elements">
		
		<bean:size name="elements" id="numberOfElements" />		
		<bean:message  bundle="CMS_RESOURCES" key="cms.personalGroupsManagement.elements.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.name.label"/>
				</th>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.number.label"/>
				</th>
				<th class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.email.label"/>
				</th>
			</tr>
			<logic:iterate id="person" name="elements" type="net.sourceforge.fenixedu.domain.Person">
                <bean:define id="student" name="person" property="students[0]"/>
				<tr>
					<td class="listClasses">
						<bean:write name="student" property="person.nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="number"/>
					</td>
					<td class="listClasses">
						<bean:write name="student" property="person.email"/>
					</td>
				</tr>
			</logic:iterate>
	    </table>									
	</logic:present>