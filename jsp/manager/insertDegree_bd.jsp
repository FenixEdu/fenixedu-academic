<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<h2><bean:message key="message.insertDegree" /></h2>
<br/>
<table>
<html:form action="/insertDegree">
	    
		<html:hidden property="page" value="1"/>
<tr>
	<td>
		<bean:message key="message.degreeName"/>
	</td>
	<td>
		<html:text property="name" />
	</td>
	<td>
		<span class="error"><html:errors property="name"  /></span>
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.degreeCode"/>
	</td>
	<td>
		<html:text property="code" />
	</td>
	<td>
		<span class="error"><html:errors property="code" /></span>
	</td>
</tr>
				
<tr>
	<td>
		<bean:message key="message.degreeType"/>
	</td>
	<td>
		<html:select property="degreeType">
		<option value="1"selected>Licenciatura
		<option value="2">Mestrado
		</html:select>
		
	</td>
</tr>
</table>
<br />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			
<html:hidden property="method" value="insert" />
</html:form>