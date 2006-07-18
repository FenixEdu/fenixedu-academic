<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="link.manage.final.degree.works"/></h2>
<br />
<span class="error"><html:errors/></span>
<bean:message key="choose.execution.year.for.final.degree.work.managment"/>
<br />
<table>
	<logic:iterate id="executionDegree" name="executionDegrees">
		<bean:define id="executionDegreeOID" name="executionDegree" property="idInternal"/>
		<bean:define id="degreeCurricularPlan" name="executionDegree" property="degreeCurricularPlan"/>
		<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlan" property="idInternal"/>
		<tr>
			<td class="listClasses">
				<bean:write name="degreeCurricularPlan" property="degree.presentationName"/>
			</td>
			<td class="listClasses">
				<bean:write name="degreeCurricularPlan" property="name"/>
			</td>
			<td class="listClasses">
				<html:link action="<%= "/manageFinalDegreeWork.do?method=prepare&amp;degreeCurricularPlanID="
						+ degreeCurricularPlanID.toString()
						+ "&amp;executionDegreeOID="
						+ executionDegreeOID.toString()
						%>">
					<bean:write name="executionDegree" property="executionYear.nextYearsYearString"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>