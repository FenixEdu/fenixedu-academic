<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors property="error.default" /></span>
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
		<html:text property="nome" />
	</td>
</tr>
<tr>
	<td>
		<bean:message key="message.degreeCode"/>
	</td>
	<td>
		<html:text property="sigla" />
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