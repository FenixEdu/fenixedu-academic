<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.objectives"/></h2>
<html:form action="/objectivesManagerDA">
	<html:hidden property="page" value="1"/>
		<table>		
			<tr>
				<td width="200" valign="top"><strong><bean:message key="label.generalObjectives" /></strong>
				</td>
			</tr>
			<tr>
				<td><html:textarea rows="10" cols="100" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="generalObjectives" ></html:textarea>
				</td>
				<td><span class="error" ><html:errors property="generalObjectives"/></span>	
				</td>
			</tr>
		</table>
		<br />
		<table>
			<tr>
				<td width="200" valign="top"><strong><bean:message key="label.operacionalObjectives" /></strong>
				</td>
			</tr>
			<tr>
				<td><html:textarea rows="10" cols="100" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="operacionalObjectives" ></html:textarea>
				</td>
				<td><span class="error" ><html:errors property="operacionalObjectives"/></span>	
				</td>
			</tr>
		</table>
<br />	
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>		
<html:hidden property="method" value="editObjectives"/>
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
</html:form>