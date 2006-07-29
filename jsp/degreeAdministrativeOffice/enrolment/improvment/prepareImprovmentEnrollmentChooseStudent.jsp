<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrollment.improvment" /></h2>


<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/improvmentEnrollment" focus="studentNumber">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="start"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<table>
		<tr>
			<td><bean:message key="label.choose.student" />&nbsp;</td>
			<td>
				<input alt="input.studentNumber" type="text" name="studentNumber" size="5" maxlength="5"/>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.choose.year.execution"/>&nbsp;</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod" >
					<html:optionsCollection name="executionPeriods"/>
				</html:select>
			</td>				
		</tr>
	</table>
	<br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.submit.student"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message key="button.clean"/>
	</html:reset>
</html:form>