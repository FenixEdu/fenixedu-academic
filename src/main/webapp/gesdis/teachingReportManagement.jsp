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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.teachingReport.teacherReport"/></h2>

<h3><bean:message key="title.teachingReport.evaluation"/></h3>

<logic:present name="siteView">
<html:form action="/teachingReport">
	<bean:define id="siteCourseInformation" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>

	<table class="tstyle1 thlight thright">
		<tr>
			<td><bean:message key="message.teachingReport.courseName"/></td>
			<td><bean:write name="executionCourse" property="nome" /></td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.courseInfo"/></td>
			<td>
				<bean:write name="executionCourse" property="infoExecutionPeriod.semester"/>
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.responsibleTeacher"/></td>
			<td>
				<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
					<bean:write name="infoTeacher" property="infoPerson.nome" />
				</logic:iterate>	
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.courseDepartment"/></td>
			<td>
				<logic:iterate id="infoDepartment" name="siteCourseInformation" property="infoDepartments">
					<logic:present name="infoDepartment" property="realName" >
						<bean:write name="infoDepartment" property="realName" />
					</logic:present>
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.courseSection"/></td>
			<td></td>
		</tr>
		<tr>
			<td><bean:message key="message.teachingReport.courseURL"/></td>
			<td>
				<bean:define id="objectCode" name="siteCourseInformation" property="infoExecutionCourse.externalId"/>
				<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/>publico/executionCourse.do?method=firstPage&amp;executionCourseID=<%= objectCode %>
			</td>
		</tr>
	</table>


	<h3 class="mtop2 separator2"><bean:message key="message.teachingReport.executionYear" />
		<bean:write name="executionYear" property="year" />*
	</h3>
	
	<logic:iterate id="siteEvaluationInformation" name="siteCourseInformation" property="infoSiteEvaluationInformations">
		<bean:define id="evaluated" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.evaluated" type="java.lang.Integer"/>
		<bean:define id="enrolled" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.enrolled" type="java.lang.Integer"/>
		<bean:define id="approved" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.approved" type="java.lang.Integer"/>

		<table class="tstyle2">
			<tr>
				<td><bean:message key="message.teachingReport.curricularName"/></td>
				<td>
					<bean:write name="siteEvaluationInformation" property="infoCurricularCourse.name"/>&nbsp;
					<bean:write name="siteEvaluationInformation" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
				</td>
			</tr>
			<tr>
				<td><bean:message key="message.teachingReport.IN"/></td>
				<td><bean:write name="enrolled" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teachingReport.AV"/></td>
				<td><bean:write name="evaluated"/></td>
			</tr>
			<tr>
				<td><bean:message key="message.teachingReport.AP"/></td>
				<td><bean:write name="approved"/></td>
			</tr>
			<% int ap_en = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
			<tr>
				<td><bean:message key="message.teachingReport.AP/IN"/></td>
				<td><%= ap_en %>%</td>
			</tr>
			<% int ap_ev = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
			<tr>
				<td><bean:message key="message.teachingReport.AP/AV"/></td>
				<td><%= ap_ev %>%</td>
			</tr>
		</table>
		

		<h3 class="mtop2 separator2"><bean:message key="message.teachingReport.approvalRates"/>*</h3>
	
		<table class="tstyle4">
			<tr>
				<th>&nbsp;</th>
				<th><bean:message key="message.teachingReport.AP/IN"/></th>
				<th><bean:message key="message.teachingReport.AP/AV"/></th>
			</tr>
			<logic:iterate id="siteEvaluationStatistics" name="siteEvaluationInformation" property="infoSiteEvaluationHistory">
				<bean:define id="evaluated" name="siteEvaluationStatistics" property="evaluated" type="java.lang.Integer"/>
				<bean:define id="enrolled" name="siteEvaluationStatistics" property="enrolled" type="java.lang.Integer"/>
				<bean:define id="approved" name="siteEvaluationStatistics" property="approved" type="java.lang.Integer"/>
				<tr>
					<td>
						<bean:write name="siteEvaluationStatistics" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					<% int ap_en_ch = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
					<td><%= ap_en_ch %>%</td>
					<% int ap_ev_ch = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
					<td><%= ap_ev_ch %>%</td>
				</tr>
			</logic:iterate>
			<logic:present name="infoCoursesHistoric">
				<bean:define id="curricularCourse" name="siteEvaluationInformation" property="infoCurricularCourse"/>
				<logic:iterate id="siteCourseHistoric" name="infoCoursesHistoric">
					<logic:iterate id="courseHistoric" name="siteCourseHistoric" property="infoCourseHistorics">
						<bean:define id="curricularCourseId" name="courseHistoric" property="infoCurricularCourse.externalId"/>
						<logic:equal name="curricularCourse" property="externalId" value="<%= curricularCourseId.toString() %>">
							<bean:define id="evaluated" name="courseHistoric" property="evaluated" type="java.lang.Integer"/>
							<bean:define id="enrolled" name="courseHistoric" property="enrolled" type="java.lang.Integer"/>
							<bean:define id="approved" name="courseHistoric" property="approved" type="java.lang.Integer"/>
							<tr>
								<td>
									<bean:write name="courseHistoric" property="curricularYear"/>
								</td>
								<% int ap_en_ch = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
								<td><%= ap_en_ch %>%</td>
								<% int ap_ev_ch = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
								<td><%= ap_ev_ch %>%</td>
							</tr>
						</logic:equal>
					</logic:iterate>
				</logic:iterate>
			</logic:present>		
		</table>
	</logic:iterate>

	<p class="mvert15">
		<bean:message key="message.teachingReport.note1"/>
		<br/>
		<bean:message key="message.teachingReport.note2"/>
	</p>


	<h3 class="mtop2 separator2"><bean:message key="message.teachingReport.report"/></h3>

	<p>
		<bean:message key="message.teachingReport.note3"/>
		<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.report" bundle="HTMLALT_RESOURCES" property="report" cols="70%" rows="10"/>
	</p>

	<p><strong><bean:message key="message.teachingReport.text1"/></strong></p>

	<ul>
		<li><bean:message key="message.teachingReport.text3"/></li>
		<li><bean:message key="message.teachingReport.text4"/></li>
	</ul>

	<p>
		<bean:message key="message.teachingReport.thanks"/>
	</p>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalId" property="externalId"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>

	<p class="mtop2">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
			<bean:message key="button.save"/>
		</html:submit>
		
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>
	</p>

</html:form>
</logic:present>
