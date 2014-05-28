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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.CompetenceCourse"%>

<h2><bean:message key="link.program" /></h2>

<div class="infoop2">
	<bean:message key="label.program.explanation" />
</div>

<p>
	<span class="error0"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="warning0"><!-- w3c complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="executionCourse">

	<bean:define id="showNote" value="false" toScope="request"/>
	
	<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester" />

	<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" type="net.sourceforge.fenixedu.domain.CurricularCourse">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>

		<logic:equal name="curricularCourse" property="bolonhaDegree" value="true">
			<bean:define id="competenceCourse" name="curricularCourse" property="competenceCourse"/>
			
			<p class="mtop15 mbottom025 color777">
				<bean:write name="degree" property="presentationName"/>
			</p>
			<h3 class="mtop025">
				<%= ((CompetenceCourse) competenceCourse).getName(executionPeriod) %>
				<logic:equal name="competenceCourse" property="curricularStage.name" value="PUBLISHED">
					<bean:define id="showNote" value="true" toScope="request"/>
					<span style="font-size: 0.7em; font-weight: normal; background: #ffa;"><bean:message key="label.competenceCourse.notApproved"/>(*)</span>
				</logic:equal>
			</h3>
			<blockquote>
				<h4>
					<bean:message key="title.program"/>
				</h4>
				<%= ((CompetenceCourse) competenceCourse).getProgram(executionPeriod) %>
				<bean:define id="programEn" value="<%= ((CompetenceCourse) competenceCourse).getProgramEn(executionPeriod) %>" />
				<logic:present name="programEn">
					<br/>
					<h4>
						<bean:message key="title.program.eng"/>
					</h4>
					<%= programEn %>
				</logic:present>
			</blockquote>
		</logic:equal>

		<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>

				<h3 class="mtop2">
					<bean:write name="degree" property="presentationName"/>
					<br/>
					<bean:write name="curricularCourse" property="name"/>
				</h3>
				<blockquote>
					<logic:present name="curriculum">
						<h4>
							<bean:message key="title.program"/>
						</h4>
						<bean:write name="curriculum" property="program" filter="false"/>
						<logic:present name="curriculum" property="programEn">
							<br/>
							<h4>
								<bean:message key="title.program.eng"/>
							</h4>
							<bean:write name="curriculum" property="programEn" filter="false"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.program.not.defined"/>
					</logic:notPresent>
				</blockquote>
				<logic:present name="curriculum">
					<% if ((lastCurriculum == curriculum && (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()))
						|| curriculum.getProgram() == null || curriculum.getProgramEn() == null
						|| curriculum.getProgram().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getProgramEn().equals(org.apache.commons.lang.StringUtils.EMPTY)) { %>

						<bean:define id="url" type="java.lang.String">/editProgram.do?method=prepareEditProgram&amp;curriculumID=<bean:write name="curriculum" property="externalId"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
							<bean:message key="button.edit"/>
						</html:link>
					<% } %>
				</logic:present>
				<logic:notPresent name="curriculum">
					<% if (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()) { %>
						<bean:define id="url" type="java.lang.String">/createProgram.do?method=prepareCreateProgram&amp;curricularCourseID=<bean:write name="curricularCourse" property="externalId"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
							<bean:message key="button.create"/>
						</html:link>
					<% } %>
				</logic:notPresent>
		</logic:notEqual>

	</logic:iterate>

<logic:equal name="showNote" value="true">
		<p  class="mtop2"><em>* <bean:message key="label.competenceCourse.notApproved.note"/></em></p>
	</logic:equal>
	
</logic:present>