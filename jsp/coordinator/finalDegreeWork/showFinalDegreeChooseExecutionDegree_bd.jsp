<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID"/>

<h2><bean:message key="link.coordinator.managefinalDegreeWorks"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<p class="mtop2 mbottom05">
	<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
</p>

<ul class="mtop05">
	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
		<bean:define id="executionDegreeOID" name="infoExecutionDegree" property="idInternal" />
		<li>
			<html:link page="<%= "/manageFinalDegreeWork.do?method=prepare&amp;degreeCurricularPlanID="
					+ degreeCurricularPlanID.toString()
					+ "&amp;executionDegreeOID="
					+ executionDegreeOID.toString()
					%>">
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
			</html:link>
		</li>
	</logic:iterate>
</ul>

