<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<logic:present name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>" >
<table>
	<tr>
		<th>
			<h2><bean:message key="label.responsableProfessor"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoResponsableTeacher" name="<%= SessionConstants.RESPONSIBLE_TEACHERS_LIST %>">
		<tr>
			<td>
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</BR>
</logic:present>
<logic:present name="<%= SessionConstants.TEACHERS_LIST %>" >
<table>
	<tr>
		<th>
			<h2><bean:message key="label.professorShips"/></h2>	
		</th>
	</tr>	

	<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">
		<tr>
			<td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
			</td>
		</tr>
	</logic:iterate>	
</table>
</logic:present>