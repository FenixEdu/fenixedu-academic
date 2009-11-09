<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="link.manage.final.degree.works"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<p class="mtop1 mbottom05">
	<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
</p>

<div class="infoop2">
	<p><bean:message key="choose.execution.year.for.final.degree.work.managment.create.info.part1" bundle="APPLICATION_RESOURCES"/></p>
	<p><bean:message key="choose.execution.year.for.final.degree.work.managment.create.info.part2" bundle="APPLICATION_RESOURCES"/></p>
</div>

<table>
	<logic:iterate id="executionDegree" name="executionDegrees">
		<bean:define id="executionDegreeOID" name="executionDegree" property="externalId" />
		<bean:define id="degreeCurricularPlan" name="executionDegree" property="degreeCurricularPlan"/>	
		<bean:define id="degreeCurricularPlanOID" name="degreeCurricularPlan" property="externalId"/>
		<tr>
			<td class="listClasses">
				<bean:write name="degreeCurricularPlan" property="degree.presentationName"/>
			</td>
			<td class="listClasses">
				<bean:write name="degreeCurricularPlan" property="name"/>
			</td>
			<td class="listClasses">
				<logic:empty name="executionDegree" property="scheduling">
					<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
					<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfoWithNewScheduling&amp;degreeCurricularPlanID="
					    + degreeCurricularPlanOID
						+ "&amp;executionDegreeOID="
						+ executionDegreeOID
						%>">(criar)
					</html:link>
				</logic:empty>
				<logic:notEmpty name="executionDegree" property="scheduling">
					<html:link page="<%= "/manageFinalDegreeWork.do?method=finalDegreeWorkInfo&amp;degreeCurricularPlanID="
							+ degreeCurricularPlanOID
							+ "&amp;executionDegreeOID="
							+ executionDegreeOID
							%>">
						<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
					</html:link>
				</logic:notEmpty>
			</td>
		</tr>
	</logic:iterate>
</table>

