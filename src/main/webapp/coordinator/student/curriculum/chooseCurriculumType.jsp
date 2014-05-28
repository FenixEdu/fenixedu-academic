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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<jsp:include page="/coordinator/context.jsp" />

<h2><bean:message key="title.student.curriculum" /></h2>

<span class="error0"><!-- Error messages go here --><html:errors /></span>


<logic:present role="role(COORDINATOR)">

	<bean:define id="studentNumber" name="student" property="number" />
	<bean:define id="studentId" name="student" property="externalId" />
	<bean:define id="executionDegreeId" name="viewStudentCurriculumForm"
		property="executionDegreeId" />
	<bean:define id="degreeCurricularPlanId"
		name="viewStudentCurriculumForm" property="degreeCurricularPlanId" />

	<ul>
		<li>
			<html:link
				action="<%="/viewStudentCurriculum.do?method=prepareReadByStudentNumber&studentNumber=" + studentNumber.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanId.toString() + "&executionDegreeId=" + executionDegreeId.toString()%>">
				<bean:message key="label.preBolonha" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link
				action="<%="/bolonhaTransitionManagement.do?method=prepare&studentId=" + studentId.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanId.toString() + "&executionDegreeId=" + executionDegreeId.toString() %>">
				<bean:message key="label.bolonha" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</li>
		<li>
			<html:link
				action="<%="/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan&studentNumber=" + studentNumber.toString() + "&degreeCurricularPlanID=" + degreeCurricularPlanId.toString() + "&executionDegreeId=" + executionDegreeId.toString() %>">
				<bean:message key="label.equivalency.plan.applied.to.student" bundle="APPLICATION_RESOURCES" />
			</html:link>
		</li>
	</ul>

</logic:present>