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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message key="label.marksSheet" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>
</p>


<logic:present name="infoCurriculumHistoricReport">
	<h3>
		<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.name"/>
		-
		<bean:write name="infoCurriculumHistoricReport" property="curricularCourse.degreeCurricularPlan.name"/>
	</h3>

	<p class="mtop15 mbottom1"><em class="highlight5"><bean:write name="infoCurriculumHistoricReport" property="academicInterval.pathName"/>*</em></p>
	
	<table class="tstyle1 mtop05">
		<tr>
			<td><bean:message key="message.teachingReport.IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></td>
			<td class="aright"><bean:write name="infoCurriculumHistoricReport" property="enroled"/></td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></td>
			<td  class="aright"><bean:write name="infoCurriculumHistoricReport" property="evaluated"/></td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.AP" bundle="CURRICULUM_HISTORIC_RESOURCES"/></td>
			<td  class="aright"><bean:write name="infoCurriculumHistoricReport" property="approved"/></td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.AP/IN" bundle="CURRICULUM_HISTORIC_RESOURCES"/></td>
			<td  class="aright"><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEnroled"/>%</td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.AP/AV" bundle="CURRICULUM_HISTORIC_RESOURCES"/></td>
			<td class="aright"><bean:write name="infoCurriculumHistoricReport" property="ratioApprovedEvaluated"/>%</td>
		</tr>
	</table>

	<logic:notEmpty name="infoCurriculumHistoricReport" property="enrolments">
		<h3 class="mbottom05"><bean:message key="label.students.enrolled.exam" bundle="CURRICULUM_HISTORIC_RESOURCES"/></h3>

		<fr:view name="infoCurriculumHistoricReport" property="enrolments" schema="info.enrolment.historic.report">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thbold tdcenter mtop05" />
				<fr:property name="columnClasses" value=",aleft,,,,," />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<p>
		<em><bean:message key="message.teachingReport.note1" bundle="CURRICULUM_HISTORIC_RESOURCES"/></em>
	</p>
	<p>
		<em><bean:message key="message.teachingReport.note2" bundle="CURRICULUM_HISTORIC_RESOURCES"/></em>
	</p>

</logic:present>
