<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/listExecutionCourseGroupings">
	<html:hidden property="method" value="selectExecutionPeriod"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" labelProperty="qualifiedName" property="idInternal"/>
	</html:select>
</html:form>

<logic:present name="executionPeriod">
	<html:link page="/listExecutionCourseGroupings.do?method=downloadExecutionCourseGroupings"
			paramId="executionPeriodID" paramName="executionPeriod" paramProperty="idInternal">
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="link.save"/>
	</html:link>
</logic:present>