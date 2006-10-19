<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="link.program" /></h2>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.program.explanation" />
		</td>
	</tr>
</table>

<logic:present name="curricularCourse">
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
		<bean:message key="label.in"/>
		<bean:write name="degree" property="nome"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<blockquote>
		<bean:define id="url" type="java.lang.String">/createProgram.do?method=createProgram&amp;executionCourseID=<bean:write name="executionCourse" property="idInternal"/></bean:define>
		<logic:present name="curricularCourse" property="findLatestCurriculum">
			<fr:edit name="curricularCourse" property="curriculumFactoryEditCurriculum"
					schema="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculumProgram"
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
					schema="net.sourceforge.fenixedu.domain.CurricularCourse.CurriculumFactoryInsertCurriculumProgram"
					action="<%= url %>"
					>
				<fr:layout name="flow">
				</fr:layout>
			</fr:edit>
		</logic:notPresent>

	</blockquote>
</logic:present>