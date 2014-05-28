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
	<bean:message key="link.program"/>
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
				<fr:view name="degree" property="nameI18N">
					<fr:layout name="html"> 
						<fr:property name="escaped" value="false" />
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</logic:iterate>
		</h3>

			<h4 class="mbottom05 greytxt">
				<bean:message key="title.program"/>
			</h4>
			<div class="mtop05 coutput2" style="line-height: 1.5em;">
				<% request.setAttribute("programI18N", ((CompetenceCourse) competenceCourse).getProgramI18N(executionPeriod));  %>
				<fr:view name="programI18N">
					<fr:layout name="html">
						<fr:property name="newlineAware" value="true" />
					</fr:layout>
				</fr:view>
			</div>
	   </div>
	</logic:equal>
</logic:iterate>

	<logic:iterate id="curricularCourse" name="executionCourse" property="curricularCoursesSortedByDegreeAndCurricularCourseName" type="net.sourceforge.fenixedu.domain.CurricularCourse">
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
							<bean:message key="title.program"/>
						</h4>
						<div class="mtop05 coutput2" style="line-height: 1.5em;">
						<fr:view name="curriculum" property="programI18N"/>
						</div>
					</logic:present>
					<logic:notPresent name="curriculum">
						<bean:message key="message.program.not.defined"/>
					</logic:notPresent>
			</div>
		</logic:notEqual>
	</logic:iterate>
	