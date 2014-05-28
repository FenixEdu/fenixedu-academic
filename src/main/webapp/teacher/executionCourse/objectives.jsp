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

<h2><bean:message key="link.objectives"/></h2>

<div class="infoop2">
	<bean:message key="label.objectives.explanation" />
</div>

<p>
	<span class="error"><!-- Error messages go here -->
		<html:errors/>
	</span>
	<span class="info">
	<!-- w3c Complient -->
		<html:messages id="info" message="true"/>
	</span>
</p>

<logic:present name="executionCourse">

	<bean:define id="showNote" value="false" toScope="request"/>
	
	<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester"/>

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
					<bean:message key="label.generalObjectives"/>
				</h4>
				<%= ((CompetenceCourse) competenceCourse).getObjectives(executionPeriod) %>
				<bean:define id="objectivesEn" value="<%= ((CompetenceCourse) competenceCourse).getObjectivesEn(executionPeriod) %>" />
				<logic:present name="generalObjectivesEn">
					<br/>
					<h4>
						<bean:message key="label.generalObjectives.eng"/>
					</h4>
					<%= objectivesEn %>
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
							<bean:message key="label.generalObjectives"/>
						</h4>
						<bean:write name="curriculum" property="generalObjectives" filter="false"/>
						<logic:present name="curriculum" property="generalObjectivesEn">
							<br/>
							<h4>
								<bean:message key="label.generalObjectives.eng"/>
							</h4>
							<bean:write name="curriculum" property="generalObjectivesEn" filter="false"/>
						</logic:present>
						<h4>
							<bean:message key="label.operacionalObjectives"/>
						</h4>
						<bean:write name="curriculum" property="operacionalObjectives" filter="false"/>
						<logic:present name="curriculum" property="operacionalObjectivesEn">
							<br/>
							<h4>
								<bean:message key="label.operacionalObjectives.eng"/>
							</h4>
							<bean:write name="curriculum" property="operacionalObjectivesEn" filter="false"/>
						</logic:present>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.objectives.not.defined"/>
					</logic:notPresent>
				</blockquote>
				<logic:present name="curriculum">
					<% if ((lastCurriculum == curriculum && (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()))
						|| curriculum.getOperacionalObjectives() == null || curriculum.getOperacionalObjectivesEn() == null
						|| curriculum.getOperacionalObjectives().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getOperacionalObjectivesEn().equals(org.apache.commons.lang.StringUtils.EMPTY)
						|| curriculum.getGeneralObjectives() == null || curriculum.getGeneralObjectivesEn() == null
						|| curriculum.getGeneralObjectives().equals(org.apache.commons.lang.StringUtils.EMPTY) || curriculum.getGeneralObjectivesEn().equals(org.apache.commons.lang.StringUtils.EMPTY)) { %>

						<bean:define id="url" type="java.lang.String">/editObjectives.do?method=prepareEditObjectives&amp;curriculumID=<bean:write name="curriculum" property="externalId"/></bean:define>
						<html:link page="<%= url %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
							<bean:message key="button.edit"/>
						</html:link>
					<% } %>
				</logic:present>
				<logic:notPresent name="curriculum">
					<% if (curricularCourse.getBasic() == null || !curricularCourse.getBasic().booleanValue()) { %>
						<bean:define id="url" type="java.lang.String">/createObjectives.do?method=prepareCreateObjectives&amp;curricularCourseID=<bean:write name="curricularCourse" property="externalId"/></bean:define>
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