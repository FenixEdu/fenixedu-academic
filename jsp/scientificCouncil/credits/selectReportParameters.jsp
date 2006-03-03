<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h3><bean:message key="message.credits.top"/></h3>

<span class="error"><html:errors/></span>
<html:form action="/creditsReport">

	<html:hidden property="method" value="viewCreditsReport"/>
	
	<bean:message key="label.credits.choose.Period"/>:<br/><br/>
	
	<bean:message key="label.credits.from"/>: <html:select property="fromExecutionPeriodID" size="1">
	<html:options collection="executionPeriods" property="value" labelProperty="label"/>
	</html:select>
	
	&nbsp&nbsp <bean:message key="label.credits.until"/>: <html:select property="untilExecutionPeriodID" size="1">
		<html:options collection="executionPeriods" property="value" labelProperty="label"/>
	</html:select>
	
	<br/><br/>
	
	<bean:message key="label.credits.choose.Department"/>: <br/>
	<html:select property="departmentID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
		<html:option value="0" key="label.allDepartments"/>
		<html:options collection="departments" property="idInternal" labelProperty="realName"/>
	</html:select>
	
	<br/><br/>
	
	<logic:present name="units">
		<bean:message key="label.credits.choose.unit"/>:<br/>
		<html:select property="unitID" size="1">
			<html:option value="0" key="label.allUnits"/>
			<html:options collection="units" property="idInternal" labelProperty="name"/>
		</html:select>
	</logic:present>
	
	<br/>
	<html:submit styleClass="inputbutton">
		<bean:message key="submit"/>
	</html:submit>
</html:form>	