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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.CompetenceCourse"%> 

<h2 class="mbottom1">
	<bean:message key="link.objectives"/>
</h2>

<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod" type="net.sourceforge.fenixedu.domain.ExecutionSemester"/>

<logic:iterate id="entry" name="executionCourse" property="curricularCoursesIndexedByCompetenceCourse">
	<bean:define id="competenceCourse" name="entry" property="key"/>
	<logic:equal name="competenceCourse" property="curricularStage.name" value="APPROVED">
		<div class="mbottom2">
		<% request.setAttribute("nameI18N", ((CompetenceCourse) competenceCourse).getNameI18N(executionPeriod));  %>
		<p class="mbottom05"><em><fr:view name="nameI18N"/></em></p>
		<h3 class="mvert0">
			<logic:iterate id="curricularCourse" name="entry" property="value" indexId="i">
				<logic:notEqual name="i" value="0"><br/></logic:notEqual>
				<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
				<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
				<bean:message key="label.in"/>
				<fr:view name="degree" property="nameI18N"/>
			</logic:iterate>
		</h3>
			<h4 class="mbottom05 greytxt">
				<bean:message key="label.generalObjectives"/>
			</h4>
			<bean:define id="objectives" value="<%= ((CompetenceCourse) competenceCourse).getObjectives(executionPeriod) %>" />
			<logic:present name="objectives">
				<div class="coutput2 mvert0">
				<% request.setAttribute("objectivesI18N", ((CompetenceCourse) competenceCourse).getObjectivesI18N(executionPeriod));  %>
				<fr:view name="objectivesI18N">
					<fr:layout>
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="false" />
					</fr:layout>
				</fr:view>
				</div>
			</logic:present>
			</div>
	</logic:equal>
</logic:iterate>

	<logic:iterate id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName">
		<bean:define id="degree" name="curricularCourse" property="degreeCurricularPlan.degree"/>
		<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
			<% net.sourceforge.fenixedu.domain.Curriculum curriculum = curricularCourse.findLatestCurriculumModifiedBefore(executionPeriod.getExecutionYear().getEndDate()); %>
			<% net.sourceforge.fenixedu.domain.Curriculum lastCurriculum = curricularCourse.findLatestCurriculum(); %>
			<% request.setAttribute("curriculum", curriculum); %>
			<% request.setAttribute("lastCurriculum", lastCurriculum); %>
			<div class="mbottom2">
				<p class="mbottom05"><em><fr:view name="curricularCourse" property="nameI18N"/></em></p>
				<h3 class="mvert0">
					<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
					<bean:message key="label.in"/>
					<fr:view name="degree" property="nameI18N"/>
				</h3>

					<logic:present name="curriculum">
						<h4 class="mbottom05 greytxt">
							<bean:message key="label.generalObjectives"/>
						</h4>
						<div class="coutput2 mvert0">
						<fr:view name="curriculum" property="generalObjectivesI18N">
							<fr:layout name="html">
								<fr:property name="escaped" value="false" />
							</fr:layout>
						</fr:view>
						</div>
						<logic:notEmpty name="curriculum" property="operacionalObjectives">
							<h4 class="mbottom05 greytxt">
								<bean:message key="label.operacionalObjectives"/>
							</h4>
							<div class="coutput2 mvert0">
							<fr:view name="curriculum" property="operacionalObjectivesI18N">
								<fr:layout name="html">
									<fr:property name="escaped" value="false" />
									<fr:property name="newlineAware" value="false" />
								</fr:layout>
							</fr:view>
							</div>
						</logic:notEmpty>
					</logic:present>
					<logic:notPresent name="curriculum">
						<p><em><bean:message key="message.objectives.not.defined"/></em></p>
					</logic:notPresent>
			</div>
		</logic:notEqual>
	</logic:iterate>
	