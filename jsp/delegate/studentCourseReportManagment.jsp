<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.studentReport"/></h2>
<logic:present name="infoSiteStudentCourseReport">
<html:form action="/studentReport">
	<bean:define id="infoStudentCourseReport" name="infoSiteStudentCourseReport" property="infoStudentCourseReport"/>
	<bean:define id="curricularCourse" name="infoStudentCourseReport" property="infoCurricularCourse"/>
	<table width="90%" border="0" cellspacing="1" style="border: 1px solid #666;">
		<tr>
			<td width="25%"><strong><bean:message key="message.studentReport.courseName"/></strong></td>
			<td><bean:write name="curricularCourse" property="name" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.studentReport.courseInfo"/></strong></td>
			<td>
				<logic:iterate id="infoScope" name="curricularCourse" property="infoScopes">
					<bean:write name="infoScope" property="infoCurricularSemester.infoCurricularYear.year"/>
					<bean:write name="infoScope" property="infoCurricularSemester.semester"/>
					<bean:write name="infoScope" property="infoBranch.acronym"/>
					<br />
				</logic:iterate>
			</td>
		</tr>
	</table>
	<br />
	<h3 class="bluetxt"><bean:message key="message.studentReport.executionYear" />
	<%--&nbsp;<bean:write name="executionYear" property="year" />--%>2003/2004 * <%--HARDCODED, MAS TEMPORÁRIO... :s --%></h3>
	<bean:define id="infoSiteEvaluationStatistics" name="infoSiteStudentCourseReport" property="infoSiteEvaluationStatistics"/>
	<bean:define id="evaluated" name="infoSiteEvaluationStatistics" property="evaluated" type="java.lang.Integer"/>
	<bean:define id="enrolled" name="infoSiteEvaluationStatistics" property="enrolled" type="java.lang.Integer"/>
	<bean:define id="approved" name="infoSiteEvaluationStatistics" property="approved" type="java.lang.Integer"/>
	<table width="90%" border="0" cellspacing="1">
		<tr>
			<td width="30%"><strong><bean:message key="message.studentReport.IN"/></strong></td>
			<td><bean:write name="enrolled" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.studentReport.AV"/></strong></td>
			<td><bean:write name="evaluated"/></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.studentReport.AP"/></strong></td>
			<td><bean:write name="approved"/></td>
		</tr>
		<% int ap_en = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.studentReport.AP/IN"/></strong></td>
			<td><%= ap_en %>%</td>
		</tr>
		<% int ap_ev = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
		<tr>
			<td><strong><bean:message key="message.studentReport.AP/AV"/></strong></td>
			<td><%= ap_ev %>%</td>
		</tr>
	</table>
	<br/>
	<h3 class="bluetxt"><bean:message key="message.studentReport.approvalRates"/>*</h3>
	<table width="50%">
		<tr>
			<th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header"><bean:message key="message.studentReport.AP/IN"/></th>
			<th class="listClasses-header"><bean:message key="message.studentReport.AP/AV"/></th>
		</tr>
		<logic:iterate id="infoSiteEvaluationStatistics" name="infoSiteStudentCourseReport" property="infoSiteEvaluationHistory">
			<bean:define id="evaluated" name="infoSiteEvaluationStatistics" property="evaluated" type="java.lang.Integer"/>
			<bean:define id="enrolled" name="infoSiteEvaluationStatistics" property="enrolled" type="java.lang.Integer"/>
			<bean:define id="approved" name="infoSiteEvaluationStatistics" property="approved" type="java.lang.Integer"/>
			<tr>
				<td class="listClasses">
					<bean:write name="infoSiteEvaluationStatistics" property="infoExecutionPeriod.infoExecutionYear.year"/>
				</td>
				<% int ap_en_h = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
				<td class="listClasses"><%= ap_en_h %>%</td>
				<% int ap_ev_h = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
				<td class="listClasses"><%= ap_ev_h %>%</td>
			</tr>
		</logic:iterate>
		<logic:present name="infoSiteCourseHistoric">
			<logic:iterate id="infoCourseHistoric" name="infoSiteCourseHistoric" property="infoCourseHistorics">
				<bean:define id="evaluated" name="infoCourseHistoric" property="evaluated" type="java.lang.Integer"/>
				<bean:define id="enrolled" name="infoCourseHistoric" property="enrolled" type="java.lang.Integer"/>
				<bean:define id="approved" name="infoCourseHistoric" property="approved" type="java.lang.Integer"/>
				<tr>
					<td class="listClasses">
						<bean:write name="infoCourseHistoric" property="curricularYear"/>
					</td>
					<% int ap_en_ch = Math.round(((float) approved.intValue() / (float) enrolled.intValue()) * 100); %>
					<td class="listClasses"><%= ap_en_ch %>%</td>
					<% int ap_ev_ch = Math.round(((float) approved.intValue() / (float) evaluated.intValue()) * 100); %>
					<td class="listClasses"><%= ap_ev_ch %>%</td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>
	<br/>
	<br/>
	<hr width="90%"/>
	<br />
	<p>
		<bean:message key="message.studentReport.note1"/>
		<br />
		<bean:message key="message.studentReport.note2"/>
	</p>
	<h3 class="bluetxt"><bean:message key="message.studentReport.strongPoints"/></h3>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.strongPoints" property="strongPoints" cols="70%" rows="10"/>
	<h3 class="bluetxt"><bean:message key="message.studentReport.weakPoints"/></h3>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.weakPoints" property="weakPoints" cols="70%" rows="10"/>
	<h3 class="bluetxt"><bean:message key="message.studentReport.studentReport"/></h3>
	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.studentReport" property="studentReport" cols="70%" rows="10"/>
	<br />
	<br/>
	<bean:message key="message.teachingReport.thanks"/>
	<br />
	<br />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.universityId" property="universityId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId"/>
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