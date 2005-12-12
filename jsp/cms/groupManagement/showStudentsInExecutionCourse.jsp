<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="net.sourceforge.fenixedu.domain.IStudent" %>

<span class="error"><html:errors/></span>
	<logic:present name="elements">
		
		<bean:size name="elements" id="numberOfElements" />		
		<bean:message  bundle="CMS_RESOURCES" key="cms.userGroupsManagement.elements.label" arg0="<%=numberOfElements.toString()%>"/>
		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.label.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.label.number"/>
				</td>
				<td class="listClasses-header">
					<bean:message  bundle="CMS_RESOURCES" key="cms.label.email"/>
				</td>
			</tr>
			<logic:iterate id="person" name="elements" type="net.sourceforge.fenixedu.domain.IPerson">
			<%
				IStudent student = (IStudent) person.getStudents().get(0);
				request.setAttribute("student",student);
			 %>
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