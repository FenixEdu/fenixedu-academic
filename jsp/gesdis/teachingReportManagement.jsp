<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="title.teachingReport"/></h2>
<logic:present name="siteView">
<html:form action="/teachingReport">
	<bean:define id="siteCourseInformation" name="siteView" property="component"/>
	<bean:define id="executionCourse" name="siteCourseInformation" property="infoExecutionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
	<bean:define id="executionYear" name="executionPeriod" property="infoExecutionYear"/>
	<table width="90%" border="0" cellspacing="1">
		<tr>
			<td width="25%"><strong><bean:message key="message.teachingReport.courseName"/></strong></td>
			<td><bean:write name="executionCourse" property="nome" /></td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.courseInfo"/></strong></td>
			<td>
				<logic:iterate id="curricularCourse" name="siteCourseInformation" property="infoCurricularCourses">
			  			<logic:iterate id="curricularCourseScope" name="curricularCourse" property="infoScopes">
							<bean:write name="curricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year" />&nbsp;
							<bean:write name="curricularCourseScope" property="infoCurricularSemester.semester" />&nbsp;
							<bean:write name="curricularCourseScope" property="infoBranch.code" />
							<br />			
					 	</logic:iterate>	
				</logic:iterate>
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.responsibleTeacher"/></strong></td>
			<td>
				<logic:iterate id="infoTeacher" name="siteCourseInformation" property="infoResponsibleTeachers">
					<bean:write name="infoTeacher" property="infoPerson.nome" />
				</logic:iterate>	
			</td>
		</tr>
		<tr>
			<td><strong><bean:message key="message.teachingReport.courseDepartment"/></strong></td>
			<td>
				<logic:iterate id="infoDepartment" name="siteCourseInformation" property="infoDepartments">
					<bean:write name="infoDepartment" property="name" />
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
						https://fenix.ist.utl.pt/publico/viewSiteExecutionCourse.do?method=firstPage&objectCode=<%= objectCode %>
			</td>
		</tr>
	</table>
	<br />
	<logic:iterate id="siteEvaluationInformation" name="siteCourseInformation" property="infoSiteEvaluationInformations">
		<h3 class="bluetxt"><bean:message key="message.teachingReport.executionYear" />
		&nbsp;<bean:write name="executionYear" property="year" />*</h3>
		<bean:define id="evaluated" name="siteEvaluationInformation" property="evaluated"/>
		<bean:define id="enrolled" name="siteEvaluationInformation" property="enrolled"/>
		<bean:define id="approved" name="siteEvaluationInformation" property="approved"/>
		<table width="90%" border="0" cellspacing="1">
			<tr>
				<td width="35%"><strong><bean:message key="message.teachingReport.degreeName"/></strong></td>
				<td><bean:write name="siteEvaluationInformation" 
								property="infoCurricularCourse.name"/>
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
			<% int ap_en = (int) (((double) ((Integer) approved).intValue() / (double) ((Integer) enrolled).intValue()) * 100); %>
			<tr>
				<td><strong><bean:message key="message.teachingReport.AP/IN"/></strong></td>
				<td><%= ap_en %>%</td>
			</tr>
			<% int ap_ev = (int) (((double) ((Integer) approved).intValue() / (double) ((Integer) evaluated).intValue()) * 100); %>
			<tr>
				<td><strong><bean:message key="message.teachingReport.AP/AV"/></strong></td>
				<td><%= ap_ev %>%</td>
			</tr>
		</table>
		<br/>
		<p>
			<bean:message key="message.teachingReport.note1"/> dd/mm/yyyy
			<br />
			<bean:message key="message.teachingReport.note2"/>
		</p>
		<h3 class="bluetxt"><bean:message key="message.teachingReport.approvalRates"/></h3>
		<table width="50%">
			<tr>
				<td class="listClasses-header">&nbsp;</td>
				<td class="listClasses-header"><bean:message key="message.teachingReport.AP/IN"/></td>
				<td class="listClasses-header"><bean:message key="message.teachingReport.AP/AV"/></td>
			</tr>
			<%-- FALTA UM LOGIC ITERATE --%>
			<tr>
				<td class="listClasses"><%--<bean:write name=""/>--%>1</td>
				<td class="listClasses"><%--<bean:write name=""/>--%>2</td>
				<td class="listClasses"><%--<bean:write name=""/>--%>3</td>
			</tr>
		</table>
	</logic:iterate>
	<br />
	<h3 class="bluetxt"><bean:message key="message.teachingReport.report"/></h3>
	<bean:message key="message.teachingReport.note3"/>
	<html:textarea property="report" cols="70%" rows="10"/>
	<br />
	<br />
	<strong><bean:message key="message.teachingReport.text1"/>
	dd/mm/YYYY
	<bean:message key="message.teachingReport.text2"/></strong>
	<br />
	<ul>
		<li><bean:message key="message.teachingReport.text3"/></li>
		<li><bean:message key="message.teachingReport.text4"/></li>
	</ul>
	<br/>
	<bean:message key="message.teachingReport.thanks"/>
	<br />
	<br />
	<html:hidden property="page" value="1"/>
	<html:hidden property="idInternal"/>
    <html:hidden property="executionCourseId"/>
    <html:hidden property="executionPeriodId"/>
    <html:hidden property="executionYearId"/>
	<html:hidden property="method" value="edit"/>
	<table>
		<tr align="center">	
			<td>
				<html:submit styleClass="inputbutton" property="confirm">
					<bean:message key="button.save"/>
				</html:submit>
			</td>
			<td>
				<html:reset styleClass="inputbutton">
					<bean:message key="label.clear"/>
				</html:reset>
			</td>
		</tr>
	</table>
</html:form>
</logic:present>
