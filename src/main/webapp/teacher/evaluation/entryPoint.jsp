<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<div class="jumbotron">
	<h2><bean:message key="label.teacher.evaluation.title" bundle="RESEARCHER_RESOURCES"/></h2>
</div>

<div class="row">
	<div class="col-lg-4">
		<h3><bean:message key="label.teacher.evaluation.autoevaluation.title" bundle="RESEARCHER_RESOURCES"/></h3>
		<p><bean:message key="label.teacher.evaluation.autoevaluation.message" bundle="RESEARCHER_RESOURCES"/></p>
		<html:link page="/teacherEvaluation.do?method=viewAutoEvaluation" styleClass="btn btn-primary">
			<bean:message key="label.teacher.evaluation.autoevaluation.title" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</div>
	<c:if test="${! empty LOGGED_USER_ATTRIBUTE.person.teacherEvaluationProcessFromEvaluator}">
	<div class="col-lg-4">
		<h3><bean:message key="label.teacher.evaluation.evaluation.title" bundle="RESEARCHER_RESOURCES"/></h3>
		<p><bean:message key="label.teacher.evaluation.evaluation.message" bundle="RESEARCHER_RESOURCES"/></p>
		<html:link page="/teacherEvaluation.do?method=viewEvaluees" styleClass="btn btn-primary">
			<bean:message key="label.teacher.evaluation.evaluation.title" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</div>
	</c:if>
	<c:if test="${LOGGED_USER_ATTRIBUTE.person.isTeacherEvaluationCoordinatorCouncilMember()}">
	<div class="col-lg-4">
		<h3><bean:message key="label.teacher.evaluation.management.title" bundle="RESEARCHER_RESOURCES"/></h3>
		<p><bean:message key="label.teacher.evaluation.management.message" bundle="RESEARCHER_RESOURCES"/></p>
		<html:link page="/teacherEvaluation.do?method=viewManagementInterface" styleClass="btn btn-primary">
			<bean:message key="label.teacher.evaluation.management.title" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</div>
	</c:if>
</div>



