<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	

<table>
	<tr>
		<th>
			<h2><bean:message key="label.teacherNumber"/></h2>	
		</th>
		<th>
			<h2><bean:message key="label.name"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">
		<tr>
			<td>
				<bean:write name="infoTeacher"  property="teacherNumber"/>	
			</td>
			<td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
