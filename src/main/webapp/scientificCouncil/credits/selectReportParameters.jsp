<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.credits.top"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<html:form action="/creditsReport">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>


<table class="tstyle5 thlight thright thmiddle">
	<tr>
		<th><bean:message key="label.credits.choose.Period"/>:</th>
		<td>
			<bean:message key="label.credits.from"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.fromExecutionYearID" property="fromExecutionYearID" size="1">
				<html:options collection="executionYears" property="value" labelProperty="label"/>
			</html:select>
			<bean:message key="label.credits.until"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.untilExecutionYearID" property="untilExecutionYearID" size="1">
				<html:options collection="executionYears" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.credits.choose.Department"/>:
		</th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentID" property="departmentID" size="1">
				<html:option value="0" key="label.allDepartments"/>
				<html:options collection="departments" property="idInternal" labelProperty="realName"/>
			</html:select>
		</td>
	</tr>
</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewDetailedCreditsReport';">
			<bean:message key="button.detailed.credits.report"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewGlobalCreditsReport';">
			<bean:message key="button.global.credits.report"/>
		</html:submit>
	</p>
</html:form>	