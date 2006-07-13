<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/listExecutionCourseGroupings">
	<html:hidden property="method" value="selectExecutionPeriod"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<logic:notPresent name="executionPeriod">
			<html:option value=""></html:option>
		</logic:notPresent>
		<html:options collection="executionPeriods" labelProperty="qualifiedName" property="idInternal"/>
	</html:select>
</html:form>

<logic:present name="executionPeriod">
	<html:link page="/listExecutionCourseGroupings.do?method=downloadExecutionCourseGroupings"
			paramId="executionPeriodID" paramName="executionPeriod" paramProperty="idInternal">
		<html:img border="0" src="<%= request.getContextPath() + "/images/excel.bmp"%>" altKey="excel" bundle="IMAGE_RESOURCES"/>
	</html:link>
</logic:present>