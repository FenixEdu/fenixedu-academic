<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>	
<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>">
<jsp:include page="curriculumForm.jsp"/>
</logic:notPresent>
<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >

<bean:message key="title.program"/>
<table>
<tr>
	<td><bean:message key="label.program" />	
	</td>
	<td>
	<bean:define id="program" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program">
	</bean:define> 
	<bean:write name="program" />
	</td>
</tr>
<tr>	
	<td>	
	<html:hidden property="method" value="prepareEditProgram"/> 	
	<html:link page="/programManagerDA.do?method=prepareEditProgram">
		<bean:message key="button.edit"/>
	</html:link>	 
	
	</td>
</tr>	
</table>
</logic:present>