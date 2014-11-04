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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="link.program" /></h2>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
</p>

<td class="infoop2">
	<bean:message key="label.program.explanation" />
</td>


<logic:present name="curricularCourse">
	<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
	<h3>
		<bean:write name="degree" property="presentationName"/>
		<br/>
		<bean:write name="curricularCourse" property="name"/>
	</h3>
	<blockquote>
		<bean:define id="url" type="java.lang.String">/createProgram.do?method=createProgram&amp;executionCourseID=<bean:write name="executionCourse" property="externalId"/></bean:define>
		<logic:present name="curricularCourse" property="findLatestCurriculum">
			<fr:edit name="curricularCourse" property="curriculumFactoryEditCurriculum"
					schema="CurriculumFactoryInsertCurriculumProgram"
					action="<%= url %>"
					>
				<fr:layout name="flow">
				</fr:layout>
			</fr:edit>
		</logic:present>
		<logic:notPresent name="curricularCourse" property="findLatestCurriculum">
			<%
			org.fenixedu.academic.domain.ExecutionCourse executionCourse = (org.fenixedu.academic.domain.ExecutionCourse) pageContext.findAttribute("executionCourse");
			org.fenixedu.academic.domain.CurricularCourse curricularCourse = (org.fenixedu.academic.domain.CurricularCourse) pageContext.findAttribute("curricularCourse");
			org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseObjectivesDA.CurriculumFactoryInsertCurriculum curriculumFactoryInsertCurriculum = new org.fenixedu.academic.ui.struts.action.teacher.executionCourse.ExecutionCourseObjectivesDA.CurriculumFactoryInsertCurriculum(curricularCourse, executionCourse); 
			request.setAttribute("curriculumFactoryInsertCurriculum", curriculumFactoryInsertCurriculum);
			%>
			<fr:edit name="curriculumFactoryInsertCurriculum"
					schema="CurriculumFactoryInsertCurriculumProgram"
					action="<%= url %>"
					>
				<fr:layout name="flow">
				</fr:layout>
			</fr:edit>
		</logic:notPresent>

	</blockquote>
</logic:present>