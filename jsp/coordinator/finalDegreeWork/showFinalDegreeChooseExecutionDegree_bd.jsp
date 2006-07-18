<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<br />
<span class="error"><html:errors/><br /><br /></span>

<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
<br />
<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
	<bean:define id="executionDegreeOID" name="infoExecutionDegree" property="idInternal" />
	<html:link page="<%= "/manageFinalDegreeWork.do?method=prepare&amp;degreeCurricularPlanID="
			+ degreeCurricularPlanID.toString()
			+ "&amp;executionDegreeOID="
			+ executionDegreeOID.toString()
			%>">
		<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
	</html:link>
	<br />
</logic:iterate>
