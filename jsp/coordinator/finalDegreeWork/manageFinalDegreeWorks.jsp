<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<html:form action="/manageFinalDegreeWorks">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>

	<bean:define id="executionDegrees" name="degreeCurricularPlan" property="executionDegrees"/>
	<html:select property="executionDegreeID" onchange="this.form.submit();">
		<html:option value=""/>
		<html:options collection="executionDegrees" property="idInternal" labelProperty="executionYear.nextExecutionYear.year"/>
	</html:select>
</html:form>

<logic:present name="executionDegree">
	<bean:write name="executionDegree" property="idInternal"/>
</logic:present>