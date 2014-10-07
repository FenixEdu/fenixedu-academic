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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>


<h2 class="text-center"><bean:message key="label.periodDefinition"/></h2>

<br />

<div class="row text-center">

	<div class="col-lg-3">
		<html:link page="/teacherPersonalExpectationsDefinitionPeriod.do?method=showPeriod" styleClass="btn btn-primary">
			<bean:message key="link.teacherExpectationDefinitionPeriodManagement"/>
		</html:link>
	</div>

	<div class="col-lg-3">
		<html:link page="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod" styleClass="btn btn-primary">
			<bean:message key="link.defineAutoAvaliationPeriod"/>
		</html:link>
	</div>

	<div class="col-lg-3">
		<html:link page="/teacherPersonalExpectationsEvaluationPeriod.do?method=showPeriod" styleClass="btn btn-primary">
			<bean:message key="link.defineTeacherPersonalExpectationsEvaluationPeriod"/>
		</html:link>
	</div>

	<div class="col-lg-3">
		<html:link page="/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriod" styleClass="btn btn-primary">
			<bean:message key="link.defineTeacherPersonalExpectationsVisualizationPeriod"/>
		</html:link>
	</div>

</div>