<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/creditsManagementIndex">
	<b><bean:message key="label.execution-period-choose" /></b>: <br />
	<html:select property="executionPeriodId">
		<html:options collection="executionPeriods" property="idInternal" labelProperty="description"/>
	</html:select>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.change-execution-period" />
	</html:submit>
</html:form>