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