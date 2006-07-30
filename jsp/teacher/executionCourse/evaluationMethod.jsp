<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.evaluationMethod.explanation" />
		</td>
	</tr>
</table>

<logic:present name="executionCourse">

	<h3>
		<bean:message key="title.evaluationMethod"/>
	</h3>
	<blockquote>
		<logic:present name="executionCourse" property="evaluationMethod">
			<logic:present name="executionCourse" property="evaluationMethod.evaluationElements">
				<bean:write name="executionCourse" property="evaluationMethod.evaluationElements" filter="false"/>
			</logic:present>
			<logic:notPresent name="executionCourse" property="evaluationMethod.evaluationElements">
				<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses" length="1">
					<bean:write name="competenceCourse" property="evaluationMethod" filter="false"/>
				</logic:iterate>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="executionCourse" property="evaluationMethod">
			<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses" length="1">
				<bean:write name="competenceCourse" property="evaluationMethod" filter="false"/>
			</logic:iterate>
		</logic:notPresent>
	</blockquote>

	<br/>

	<h3>
		<bean:message key="title.evaluationMethod.eng"/>
	</h3>
	<blockquote>
		<logic:present name="executionCourse" property="evaluationMethod">
			<logic:present name="executionCourse" property="evaluationMethod.evaluationElementsEn">
				<bean:write name="executionCourse" property="evaluationMethod.evaluationElementsEn" filter="false"/>
			</logic:present>
			<logic:notPresent name="executionCourse" property="evaluationMethod.evaluationElementsEn">
				<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses" length="1">
					<bean:write name="competenceCourse" property="evaluationMethodEn" filter="false"/>
				</logic:iterate>
			</logic:notPresent>
		</logic:present>
		<logic:notPresent name="executionCourse" property="evaluationMethod">
			<logic:iterate id="competenceCourse" name="executionCourse" property="competenceCourses" length="1">
				<bean:write name="competenceCourse" property="evaluationMethodEn" filter="false"/>
			</logic:iterate>
		</logic:notPresent>
	</blockquote>

	<html:link page="/editEvaluationMethod.do?method=prepareEditEvaluationMethod" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		<bean:message key="button.edit"/>
	</html:link>

</logic:present>