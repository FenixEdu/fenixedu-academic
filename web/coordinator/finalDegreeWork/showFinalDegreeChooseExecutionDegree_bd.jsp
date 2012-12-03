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


<p class="mtop1 mbottom05">
	<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
</p>

<div class="infoop2">
	<p><bean:message key="choose.execution.year.for.final.degree.work.managment.create.info.part1"/></p>
	<p><bean:message key="choose.execution.year.for.final.degree.work.managment.create.info.part2"/></p>
	<p><bean:message key="dissertation.style.guide.info"/></p>
</div>


<ul class="mtop15">
	<logic:iterate id="infoExecutionDegree" name="infoExecutionDegrees">
		<bean:define id="executionDegreeOID" name="infoExecutionDegree" property="externalId" />
		<li>
			<logic:empty name="infoExecutionDegree" property="executionDegree.scheduling">
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.nextExecutionYearYear"/>
				<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfoWithNewScheduling&amp;degreeCurricularPlanID="
				    + degreeCurricularPlanOID
					+ "&amp;executionDegreeOID="
					+ executionDegreeOID
					%>">(Criar)
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

