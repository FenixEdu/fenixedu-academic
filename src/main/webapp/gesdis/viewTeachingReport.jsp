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
<%@ page import="java.lang.Math" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.teachingReport"/></h2>

<logic:present name="siteView">
<bean:define id="infoSiteCourseInformation" name="siteView" property="component"/>
<bean:define id="executionCourse" name="infoSiteCourseInformation" property="infoExecutionCourse"/>
<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>


<table class="tstyle1">
	<tr>
		<td><strong><bean:message key="message.teachingReport.courseName"/></strong></td>
		<td><bean:write name="executionCourse" property="nome" /></td>
	</tr>
	<tr>
		<td><strong><bean:message key="message.teachingReport.courseInfo"/></strong></td>
		<td>
			<bean:write name="executionCourse" property="infoExecutionPeriod.semester"/>
		</td>
	</tr>
	<tr>
		<td><strong><bean:message key="message.teachingReport.responsibleTeacher"/></strong></td>
		<td>
			<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
				<bean:write name="infoTeacher" property="infoPerson.nome" />
				<br />
			</logic:iterate>	
		</td>
	</tr>
	<tr>
		<td><strong><bean:message key="message.teachingReport.courseDepartment"/></strong></td>
		<td>
			<logic:iterate id="infoDepartment" name="infoSiteCourseInformation" property="infoDepartments">
				<logic:present name="infoDepartment" property="name" >
					<bean:write name="infoDepartment" property="name" />				
				</logic:present>
			</logic:iterate>
		</td>
	</tr>
	<tr>
		<td><strong><bean:message key="message.teachingReport.courseSection"/></strong></td>
		<td></td>
	</tr>
	<tr>
		<td><strong><bean:message key="message.teachingReport.courseURL"/></strong></td>
		<td>
			<bean:define id="objectCode" name="infoSiteCourseInformation" property="infoExecutionCourse.externalId"/>
			<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/>publico/executionCourse.do?method=firstPage&amp;executionCourseID=<%= objectCode %>
		</td>
	</tr>
</table>



<h3 class="bluetxt"><bean:message key="message.teachingReport.executionYear" />
&nbsp;<bean:write name="executionYear" property="year" />*</h3>

<logic:iterate id="siteEvaluationInformation" name="infoSiteCourseInformation" property="infoSiteEvaluationInformations">
	<bean:define id="evaluated" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.evaluated" type="java.lang.Integer"/>
	<bean:define id="enrolled" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.enrolled" type="java.lang.Integer"/>
	<bean:define id="approved" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.approved" type="java.lang.Integer"/>
	<table width="90%" border="0" cellspacing="1">
		<tr>
			<td width="35%"><strong><bean:message key="message.teachingReport.curricularName"/></strong></td>
			<td><bean:write name="siteEvaluationInformation" 
							property="infoCurricularCourse.name"/>
							&nbsp;- &nbsp;
				<bean:write name="siteEvaluationInformation" 
							property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.IN"/></strong></td>
			<td><bean:write name="enrolled" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AV"/></strong></td>
			<td><bean:write name="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP"/></strong></td>
			<td><bean:write name="approved"/></td>
		</tr>
		<% int ap_en = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/IN"/></strong></td>
			<td><%= ap_en %>%</td>
		</tr>
		<% int ap_ev = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.teachingReport.AP/AV"/></strong></td>
			<td><%= ap_ev %>%</td>
		</tr>
	</table>
	<br/>
	<h3 class="bluetxt"><bean:message key="message.teachingReport.approvalRates"/>*</h3>
	<table width="50%">
		<tr>
			<th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header"><bean:message key="message.teachingReport.AP/IN"/></th>
			<th class="listClasses-header"><bean:message key="message.teachingReport.AP/AV"/></th>
		</tr>
		<logic:iterate id="siteEvaluationStatistics" name="siteEvaluationInformation" property="infoSiteEvaluationHistory">
			<bean:define id="evaluated" name="siteEvaluationStatistics" property="evaluated" type="java.lang.Integer"/>
			<bean:define id="enrolled" name="siteEvaluationStatistics" property="enrolled" type="java.lang.Integer"/>
			<bean:define id="approved" name="siteEvaluationStatistics" property="approved" type="java.lang.Integer"/>
			<tr>
				<td class="listClasses">
					<bean:write name="siteEvaluationStatistics" property="infoExecutionPeriod.infoExecutionYear.year"/>
				</td>
				<% int ap_en_h = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
				<td class="listClasses"><%= ap_en_h %>%</td>
				<% int ap_ev_h = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
				<td class="listClasses"><%= ap_ev_h %>%</td>
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
							<td class="listClasses">
								<bean:write name="courseHistoric" property="curricularYear"/>
							</td>
							<% int ap_en_ch = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
							<td class="listClasses"><%= ap_en_ch %>%</td>
							<% int ap_ev_ch = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
							<td class="listClasses"><%= ap_ev_ch %>%</td>
						</tr>
					</logic:equal>
				</logic:iterate>
			</logic:iterate>
		</logic:present>		
	</table>
	<br/>
	<br/>
	<hr width="90%"/>
</logic:iterate>
<br />
<p>
	<bean:message key="message.teachingReport.note1"/>
	<br />
	<bean:message key="message.teachingReport.note2"/>
</p>
<h3 class="bluetxt"><bean:message key="message.teachingReport.report"/></h3>

<p>
	<logic:present name="infoSiteCourseInformation" property="infoCourseReport">
		<logic:notEmpty name="infoSiteCourseInformation" property="infoCourseReport.report">
			<bean:write name="infoSiteCourseInformation" property="infoCourseReport.report" filter="false"/>
		</logic:notEmpty>
	</logic:present>
	<logic:empty name="infoSiteCourseInformation" property="infoCourseReport">
		<bean:message key="message.courseInformation.notYetAvailable"/>
	</logic:empty>
</p>

<strong><bean:message key="message.teachingReport.text1"/>
<br />
<ul>
	<li><bean:message key="message.teachingReport.text3"/></li>
	<li><bean:message key="message.teachingReport.text4"/></li>
</ul>
<br/>
<bean:message key="message.teachingReport.thanks"/>
<br />
<br />
</logic:present>

