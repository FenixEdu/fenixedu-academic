<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teachingReport"/></h2>
<logic:present name="siteView">
<html:form action="/teachingReport">
	<bean:define id="siteCourseInformation" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
	<table width="90%" border="0" cellspacing="1" style="border: 1px solid #666;">
		<tr>
			<td width="25%"><strong><bean:message key="message.teachingReport.courseName"/></strong></td>
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
				<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
					<bean:write name="infoTeacher" property="infoPerson.nome" />
					<br />
				</logic:iterate>	
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.courseDepartment"/></strong></td>
			<td>
				<logic:iterate id="infoDepartment" name="siteCourseInformation" property="infoDepartments">
					<logic:present name="infoDepartment" property="realName" >
						<bean:write name="infoDepartment" property="realName" />
					</logic:present>
					<br />
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.courseSection"/></strong></td>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.courseURL"/></strong></td>
			<td>
					<bean:define id="objectCode" name="siteCourseInformation" property="infoExecutionCourse.idInternal"/>
					<bean:message key="fenix.url" bundle="GLOBAL_RESOURCES"/>publico/executionCourse.do?method=firstPage&amp;executionCourseID=<%= objectCode %>
			</td>
		</tr>
	</table>
	<br />
	<h3 class="bluetxt"><bean:message key="message.teachingReport.executionYear" />
	&nbsp;<bean:write name="executionYear" property="year" />*</h3>
	<logic:iterate id="siteEvaluationInformation" name="siteCourseInformation" property="infoSiteEvaluationInformations">
		<bean:define id="evaluated" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.evaluated" type="java.lang.Integer"/>
		<bean:define id="enrolled" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.enrolled" type="java.lang.Integer"/>
		<bean:define id="approved" name="siteEvaluationInformation" property="infoSiteEvaluationStatistics.approved" type="java.lang.Integer"/>
		<table width="90%" border="0" cellspacing="1">
			<tr>
				<td width="35%"><strong><bean:message key="message.teachingReport.curricularName"/></strong></td>
				<td>
					<bean:write name="siteEvaluationInformation" property="infoCurricularCourse.name"/>&nbsp;
					<bean:write name="siteEvaluationInformation" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
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
					<% int ap_en_ch = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
					<td class="listClasses"><%= ap_en_ch %>%</td>
					<% int ap_ev_ch = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
					<td class="listClasses"><%= ap_ev_ch %>%</td>
				</tr>
			</logic:iterate>
			<logic:present name="infoCoursesHistoric">
				<bean:define id="curricularCourse" name="siteEvaluationInformation" property="infoCurricularCourse"/>
				<logic:iterate id="siteCourseHistoric" name="infoCoursesHistoric">
					<logic:iterate id="courseHistoric" name="siteCourseHistoric" property="infoCourseHistorics">
						<bean:define id="curricularCourseId" name="courseHistoric" property="infoCurricularCourse.idInternal"/>
						<logic:equal name="curricularCourse" property="idInternal" value="<%= curricularCourseId.toString() %>">
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
	<bean:message key="message.teachingReport.note3"/>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.report" bundle="HTMLALT_RESOURCES" property="report" cols="70%" rows="10"/>
	<br />
	<br />
	<strong><bean:message key="message.teachingReport.text1"/></strong>
	<br />
	<ul>
		<li><bean:message key="message.teachingReport.text3"/></li>
		<li><bean:message key="message.teachingReport.text4"/></li>
	</ul>
	<br/>
	<bean:message key="message.teachingReport.thanks"/>
	<br />
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearId" property="executionYearId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<table>
		<tr align="center">	
			<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
					<bean:message key="button.save"/>
				</html:submit>
			</td>
			<td>
				<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		</tr>
	</table>
</html:form>
</logic:present>
