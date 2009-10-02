<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<bean:define id="degreeCurricularPlanOID" name="degreeCurricularPlanID"/>

<h2><bean:message key="label.coordinator.thesis"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<p class="mtop2 mbottom05">
	<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
</p>
<p class="infoop2">
	<bean:message key="choose.execution.year.for.final.degree.work.managment.create.info"/>
</p>
<ul class="mtop05">
	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
		<bean:define id="executionDegreeOID" name="infoExecutionDegree" property="externalId" />
		<li>
			<logic:empty name="infoExecutionDegree" property="executionDegree.scheduling">
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
				<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfoWithNewScheduling&amp;degreeCurricularPlanID="
				    + degreeCurricularPlanOID
					+ "&amp;executionDegreeOID="
					+ executionDegreeOID
					%>">(criar)
				</html:link>
			</logic:empty>
			<logic:notEmpty name="infoExecutionDegree" property="executionDegree.scheduling">
				<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&amp;degreeCurricularPlanID="
						+ degreeCurricularPlanOID
						+ "&amp;executionDegreeOID="
						+ executionDegreeOID
						%>">
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
				</html:link>
			</logic:notEmpty>
		</li>
	</logic:iterate>
</ul>

