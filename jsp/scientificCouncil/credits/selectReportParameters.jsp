<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.credits.top"/></h2>

<p><span class="error"><html:errors/></span></p>

<html:form action="/creditsReport">

	<html:hidden property="method" value="viewCreditsReport"/>
	
	<p><bean:message key="label.credits.choose.Period"/>:</p>
	<bean:message key="label.credits.from"/> 
	<html:select property="fromExecutionYearID" size="1">
		<html:options collection="executionYears" property="value" labelProperty="label"/>
	</html:select>

	<bean:message key="label.credits.until"/>
	<html:select property="untilExecutionYearID" size="1">
		<html:options collection="executionYears" property="value" labelProperty="label"/>
	</html:select>
	
	<br/><br/>
	
	<p><bean:message key="label.credits.choose.Department"/>:</p>
	<html:select property="departmentID" size="1">
		<html:option value="0" key="label.allDepartments"/>
		<html:options collection="departments" property="idInternal" labelProperty="realName"/>
	</html:select>
		
	<br/><br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="visualize"/>
	</html:submit>
</html:form>	