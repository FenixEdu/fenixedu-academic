<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>


<span class="error"><html:errors/></span>


<br />
<html:form action="/generateNewPassword.do?method=findPerson">
	<html:hidden property="page" value="1" />


	<h3><bean:message key="label.operator.choosePerson" /></h3>
	<br>

	<table>
		<tr>
			<td>
				Tipo de Utilizador:			
			
			</td>
			<td>
				<logic:iterate id="user" name="userTypes">
					<tr>
						<td>
							<bean:define id="userValue" name="user" property="value" />
							<html:radio property="userType" value="<%= (String) userValue %>"/><bean:write name="user" property="label" /><br />
						</td>
					</tr>
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td>
				Número
			</td>
			<td>
				<html:text property="number" />
			</td>
		
		</tr>
	</table>




	<html:submit value="Seguinte" styleClass="inputbutton" property="OK"/>
</html:form> 
