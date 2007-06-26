<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.evaluationMethod" /></h2>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>


<div class="infoop2">
	<bean:message key="label.evaluationMethod.explanation" />
</div>

<logic:present name="executionCourse">

	<h3 class="mtop15">
		<bean:message key="title.evaluationMethod"/>
	</h3>
	<blockquote>
		<logic:present name="executionCourse" property="evaluationMethod">
			<logic:present name="executionCourse" property="evaluationMethod.evaluationElements">
				<bean:define id="evaluationElements" type="net.sourceforge.fenixedu.util.MultiLanguageString"
						name="executionCourse" property="evaluationMethod.evaluationElements"/>
				<%= evaluationElements.getContent(net.sourceforge.fenixedu.domain.Language.pt) %>
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

	<h3 class="mtop2">
		<bean:message key="title.evaluationMethod.eng"/>
	</h3>
	<blockquote>
		<logic:present name="executionCourse" property="evaluationMethod">
			<logic:present name="executionCourse" property="evaluationMethod.evaluationElements">
				<bean:define id="evaluationElements" type="net.sourceforge.fenixedu.util.MultiLanguageString"
						name="executionCourse" property="evaluationMethod.evaluationElements"/>
				<%= evaluationElements.getContent(net.sourceforge.fenixedu.domain.Language.en) %>
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

	<p>
		<html:link page="/editEvaluationMethod.do?method=prepareEditEvaluationMethod" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="button.edit"/>
		</html:link>
		<br/>
		<html:link page="/manageExecutionCourse.do?method=prepareImportEvaluationMethod&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
			<bean:message key="link.import.evaluationMethod"/>
		</html:link>
	</p>

</logic:present>