<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<h3><bean:message key="title.objectives"/></h3>
<html:form action="/objectivesManagerDA">
	<html:hidden property="page" value="1"/>
<table>		
	
	<tr>
		<td><h2><bean:message key="label.generalObjectives" /></h2>	
		</td>
		<td><html:text name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives" >
	</html:text>
		</td>
		<td> <span class="error" ><html:errors property="generalObjectives"/></span>	
		</td>
	</tr>
	<tr>
		<td><h2><bean:message key="label.operacionalObjectives" /></h2>
		</td>
		<td><html:text name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives" >
	</html:text>
		</td>
		<td> <span class="error" ><html:errors property="operacionalObjectives"/></span>	
		</td>
	</tr>
	
	<tr>
		<td>
			<html:reset  styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
		</td>
		<td>
			 <html:hidden property="method" value="editObjectives"/>
    <html:submit >
	<bean:message key="button.save"/>
	</html:submit>
		</td>
		
	</tr>
	
	
	
   
</table>	
</html:form>