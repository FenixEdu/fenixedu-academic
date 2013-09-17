<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.objectives" /></h2>

<div class="infoop2">
	<bean:message key="label.objectives.explanation" />
</div>


<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="info">
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="curricularCourse">
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:write name="degree" property="presentationName"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<blockquote>
		<bean:define id="url" type="java.lang.String">/createObjectives.do?method=createObjectives&amp;executionCourseID=<bean:write name="executionCourse" property="externalId"/></bean:define>
		<logic:present name="curricularCourse" property="findLatestCurriculum">
			<fr:edit name="curricularCourse" property="curriculumFactoryEditCurriculum"
					schema="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculumObjectives"
					action="<%= url %>"
					>
				<fr:layout name="flow">
				</fr:layout>
			</fr:edit>
		</logic:present>
		<logic:notPresent name="curricularCourse" property="findLatestCurriculum">
			<%
			net.sourceforge.fenixedu.domain.ExecutionCourse executionCourse = (net.sourceforge.fenixedu.domain.ExecutionCourse) pageContext.findAttribute("executionCourse");
			net.sourceforge.fenixedu.domain.CurricularCourse curricularCourse = (net.sourceforge.fenixedu.domain.CurricularCourse) pageContext.findAttribute("curricularCourse");
			net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculum curriculumFactoryInsertCurriculum = new net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculum(curricularCourse, executionCourse); 
			request.setAttribute("curriculumFactoryInsertCurriculum", curriculumFactoryInsertCurriculum);
			%>
			<fr:edit name="curriculumFactoryInsertCurriculum"
					schema="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculumObjectives"
					action="<%= url %>"
					>
				<fr:layout name="flow">
				</fr:layout>
			</fr:edit>
		</logic:notPresent>
	</blockquote>
</logic:present>