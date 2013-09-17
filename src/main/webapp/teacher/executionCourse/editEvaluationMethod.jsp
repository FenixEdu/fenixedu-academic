<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.evaluationMethod" /></h2>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<div class="infoop2">
	<bean:message key="label.editEvaluationMethod.explanation" />
</div>


<style>
span.flowblock div div {
margin: 0.5em 0 1.25em 0;
}
span.flowblock div div div {
border: none;
margin: 0;
}
</style>


<logic:present name="executionCourse">
	<h3 class="mbottom05">
		<bean:message key="title.evaluationMethod"/>
	</h3>
	<bean:define id="url" type="java.lang.String">/manageExecutionCourse.do?method=evaluationMethod&amp;executionCourseID=<bean:write name="executionCourse" property="externalId"/></bean:define>

	<fr:edit name="executionCourse" property="evaluationMethod"
			schema="net.sourceforge.fenixedu.domain.EvaluationMethod"
			action="<%= url %>">
		<fr:layout name="flow">
			<fr:property name="eachClasses" value="flowblock"/>
			<fr:property name="labelExcluded" value="true" />
			<fr:property name="labelTerminator" value="" />
		</fr:layout>
	</fr:edit>
</logic:present>