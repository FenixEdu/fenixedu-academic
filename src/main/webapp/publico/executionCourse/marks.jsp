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
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<bean:size id="attendsSize" name="attendsMap"/>
<h2><bean:message key="title.evaluation" /></h2> 
<br/>
<bean:message key="classification.nonOfficial.information" />
<br/>
<br/>
<em>(<bean:message key="label.students.size"/>: <bean:write name="attendsSize"/>)</em>
<table class="tab_complex" cellspacing="1" cellpadding="2">
	<tr>
		<th nowrap>
			<bean:message key="label.number"/>
		</th>
		<th nowrap>
			<bean:message key="label.name"/>
		</th>
		<th nowrap>
			<bean:message key="label.Degree"/>
		</th>
		<logic:iterate id="evaluation" name="executionCourse" property="orderedAssociatedEvaluations">
			<logic:present name="evaluation" property="publishmentMessage">
				<th nowrap>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.onlineTests.OnlineTest">
						<bean:write name="evaluation" property="distributedTest.evaluationTitle"/>
					</logic:equal>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Project">
						<bean:write name="evaluation" property="name"/>
					</logic:equal>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.WrittenTest">
						<bean:write name="evaluation" property="description"/>
					</logic:equal>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.Exam">
						<bean:write name="evaluation" property="season"/>
					</logic:equal>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.FinalEvaluation">
						<bean:message key="label.final.evaluation"/>
					</logic:equal>
					<logic:equal name="evaluation" property="class.name" value="net.sourceforge.fenixedu.domain.AdHocEvaluation">
						<bean:write name="evaluation" property="name"/>
					</logic:equal>
				</th>
			</logic:present>
		</logic:iterate>
	</tr>
	<logic:iterate id="attendEntry" name="attendsMap">
		<bean:define id="attends" name="attendEntry" property="key"/>
		<tr>
			<td nowrap>
				<bean:write name="attends" property="registration.number"/>
			</td>
			<td nowrap>
				<bean:write name="attends" property="registration.person.name"/>
			</td>
			<td nowrap>
				<bean:write name="attends" property="registration.degreeCurricularPlanName"/>
			</td>
			<logic:iterate id="evaluationEntry" name="attendEntry" property="value">
				<td nowrap>
				<logic:present name="evaluationEntry" property="value">
					<bean:define id="mark" name="evaluationEntry" property="value"/>
						<logic:present name="mark">
							<bean:write name="mark" property="publishedMark"/>
						</logic:present>
				</logic:present>
				</td>
			</logic:iterate>
		</tr>
	</logic:iterate>
</table>
