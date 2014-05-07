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



