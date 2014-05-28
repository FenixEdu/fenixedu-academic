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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%><html:xhtml/>

<h2><bean:message key="title.student.thesis"/></h2>

<div class="infoop2">
	<p><bean:message key="dissertation.style.guide.info" bundle="APPLICATION_RESOURCES"/></p>
</div>

<logic:empty name="enrolments">
	<em><bean:message key="label.student.thesis.notFound"/></em>
</logic:empty>
<logic:notEmpty name="enrolments">
<table class="tstyle2 thlight thcenter tdcenter mtop050">
	<tr>
		<th>
			<bean:message key="label.executionYear"/>
		</th>
		<th>
			<bean:message key="label.semester"/>
		</th>
		<th>
			<bean:message key="label.degree.name"/>
		</th>
		<th>
			<bean:message key="label.state"/>
		</th>
		<th>
		</th>
	</tr>
	<logic:iterate id="enrolment" type="net.sourceforge.fenixedu.domain.Enrolment" name="enrolments">
		<tr>
			<td>
				<bean:write name="enrolment" property="executionYear.year"/>
			</td>
			<td>
				<bean:write name="enrolment" property="executionPeriod.semester"/>
			</td>
			<td>
				<bean:write name="enrolment" property="curricularCourse.degreeCurricularPlan.degree.presentationName"/>
			</td>
			<td>
				<%
					final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(enrolment.getThesis());
					final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
					final String keySimple = key + ".simple";
				%>
				<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
			</td>
			<td>
				<html:link page="/thesisSubmission.do?method=prepareThesisSubmissionByEnrolment" titleKey="title.student.thesis.submission"
						paramId="enrolmentId" paramName="enrolment" paramProperty="externalId">
					<bean:message key="label.student.thesis.view"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<br/>
<div class="color888" style="text-indent: 25px">
<%
	for (final ThesisPresentationState thesisPresentationState : ThesisPresentationState.values()) {
		final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
		final String keySimple = key + ".simple";
%>
	<p class="mvert0">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
	</p>
<%
	}
%>
</div>
