<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h3><bean:message key="title.program"/></h3>
<html:form action="/programManagerDA">
<html:hidden property="page" value="1"/>	
<table>		
	
	<tr>
		<td><h2><bean:message key="label.program" />	</h2>
		</td>
		<td><html:textarea rows="4" cols="56" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program" >
	</html:textarea>
		</td>
		<td> <span class="error" ><html:errors property="program"/></span>	
		</td>
	</tr>
</table>		
	
	<h3><html:reset  styleClass="inputbutton">
    <bean:message key="label.clear"/>
    </html:reset>
	
			 <html:hidden property="method" value="editProgram"/>
    <html:submit styleClass="inputbutton">
	<bean:message key="button.save"/>
	</html:submit></h3>
		
	
	
	
   

</html:form>