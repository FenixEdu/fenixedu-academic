<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	

<logic:notPresent name="<%= SessionConstants.TEACHERS_LIST %>">

</logic:notPresent>

<logic:present name="<%= SessionConstants.TEACHERS_LIST %>" >

<bean:message key="title.teachers"/>
<table>
<tr>
<th><bean:message key="label.teacherNumber"/>	
</th>
<th><bean:message key="label.name"/>	
</th>
</tr>	
<logic:iterate id="infoTeacher" name="<%= SessionConstants.TEACHERS_LIST %>">

<tr>
	<td><bean:write name="infoTeacher"  property="teacherNumber"/>	
	</td>
	<td>
	<bean:write name="infoTeacher" property="infoPerson.nome" /> 
	</td>
</tr>
</logic:iterate>	
</table>
</logic:present>