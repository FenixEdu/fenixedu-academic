<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<bean:define id="degreeCurricularPlanOID" name="degreeCurricularPlanID"/>

<jsp:include page="/coordinator/context.jsp" />

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

