<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="title.program"/></h2>
<html:form action="/programManagerDA">
<html:hidden property="page" value="1"/>	
<table>		
	<tr>
		<td><strong><bean:message key="label.program" /></strong>
		</td>
	</tr>
	<tr>
		<td><html:textarea rows="10'" cols="80" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program" ></html:textarea>
		</td>
		<td> <span class="error" ><html:errors property="program"/></span>	
		</td>
	</tr>
	<tr>
		<td><strong><bean:message key="label.program.eng" /></strong>
		</td>
	</tr>

	<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="programEn">
	<tr>
		<td><html:textarea rows="10'" cols="80"  property="programEn" ></html:textarea>
		</td>
		<td> <span class="error" ><html:errors property="programEn"/></span>	
		</td>
	</tr>
	</logic:notPresent>
	
	<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="programEn">
	<tr>
		<td><html:textarea rows="10'" cols="80" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="programEn" ></html:textarea>
		</td>
		<td> <span class="error" ><html:errors property="programEn"/></span>	
		</td>
	</tr>
	</logic:present>

</table>
<br />
<html:hidden property="method" value="editProgram"/>
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>
</html:form>