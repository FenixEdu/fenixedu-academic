<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.credits.top"/></h2>

<p><span class="error"><html:errors/></span></p>

<html:form action="/creditsReport">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>
	
	<p><bean:message key="label.credits.choose.Period"/>:</p>
	<bean:message key="label.credits.from"/> 
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.fromExecutionYearID" property="fromExecutionYearID" size="1">
		<html:options collection="executionYears" property="value" labelProperty="label"/>
	</html:select>

	<bean:message key="label.credits.until"/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.untilExecutionYearID" property="untilExecutionYearID" size="1">
		<html:options collection="executionYears" property="value" labelProperty="label"/>
	</html:select>
	
	<br/><br/>
	
	<p><bean:message key="label.credits.choose.Department"/>:</p>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentID" property="departmentID" size="1">
		<html:option value="0" key="label.allDepartments"/>
		<html:options collection="departments" property="idInternal" labelProperty="realName"/>
	</html:select>
		
	<br/><br/>	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewDetailedCreditsReport';" styleClass="inputbuttonSmall">
		<bean:message key="button.detailed.credits.report"/>
	</html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewGlobalCreditsReport';" styleClass="inputbuttonSmall">
		<bean:message key="button.global.credits.report"/>
	</html:submit>
</html:form>	