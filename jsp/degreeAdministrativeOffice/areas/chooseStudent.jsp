<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2><bean:message key="title.student.change.areas"/></h2>

<span class="error"><html:errors/></span>

<html:form action="/changeStudentAreas.do" focus="studentNumber">
	<html:hidden property="method" value="showAndChooseStudentAreas"/>
	<html:hidden property="page" value="1"/>
	<logic:present name="degreeType">
		<html:hidden property="degreeType" value="<%= pageContext.findAttribute("degreeType").toString()%>"/>
	</logic:present>
	<table>
		<tr>
			<td><bean:message key="label.choose.student"/>&nbsp;</td>
			<td>
				<input type="text" name="studentNumber" size="5" maxlength="5"/>
			</td>
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="button.clean"/>
	</html:reset>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='exit';this.form.submit();">
		<bean:message key="button.cancel"/>
	</html:cancel>			
</html:form>
