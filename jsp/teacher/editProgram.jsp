<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



		
	

<span class="error"><html:errors/></span>	
<html:form action="/programManagerDA">
<table>		
	
	<tr>
		<td><bean:message key="label.program" />	
		</td>
		<td><html:text name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program" >
	</html:text>
		</td>
		
	</tr>
	<tr>
		<td>
			<html:reset  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
		<td>
			 <html:hidden property="method" value="editProgram"/>
    <html:submit >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		
	</tr>
	
	
	
   
</table>	
</html:form>