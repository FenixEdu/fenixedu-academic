<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda" %>

<h2><bean:message key="link.weekly.work.load"/></h2>

<br/>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="degreeCurricularPlanID"/>

	<html:select property="executionPeriodID" onchange="this.form.submit();">
		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
	</html:select>
	<br/>
	<html:select property="curricularYearID" onchange="this.form.submit();">
		<html:option value=""/>
		<html:options collection="curricularYears" property="idInternal" labelProperty="year"/>
	</html:select>
	<br/>
	<html:select property="executionCourseID" onchange="this.form.submit();">
		<html:option value=""/>
		<html:options collection="executionCourses" property="idInternal" labelProperty="nome"/>
	</html:select>
</html:form>

<br/>
<br/>

<logic:present name="executionCoursesMap">

	<logic:iterate id="entry" name="executionCoursesMap">

		<bean:write name="entry" property="key.curricularYear.year"/>
		<br/>
		<logic:iterate id="executionCourse" name="entry" property="value">
			<bean:define id="weeklyWorkLoadView" name="executionCourse" property="weeklyWorkLoadView"/>

	<table class="style1">
		<tr>
			<td class="listClasses-header" rowspan="2">
			</td>
			<bean:size id="numberOfIntervals" name="weeklyWorkLoadView" property="intervals"/>
			<td class="listClasses-header" colspan="<%= numberOfIntervals %>">
				<bean:message key="title.weekly.work.load.week"/>
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="title.weekly.work.load.total"/>
			</td>
		</tr>
		<tr>
			<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<bean:define id="intervalString" type="java.lang.String">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
					[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>, <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
				</bean:define>
				<td class="listClasses-header" title="<%= intervalString %>">
					<%= i.intValue() + 1 %>
				</td>
			</logic:iterate>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.number.responses"/>
			</td>
			<logic:iterate id="responses" name="weeklyWorkLoadView" property="numberResponses">
				<td class="listClasses">
					<bean:write name="responses"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="numberResponsesTotal"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.contact"/>
			</td>
			<logic:iterate id="contact" name="weeklyWorkLoadView" property="contactSum">
				<td class="listClasses">
					<bean:write name="contact"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="contactTotal"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.autonomousStudy"/>
			</td>
			<logic:iterate id="autonomousStudy" name="weeklyWorkLoadView" property="autonomousStudySum">
				<td class="listClasses">
					<bean:write name="autonomousStudy"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="autonomousStudyTotal"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.other"/>
			</td>
			<logic:iterate id="other" name="weeklyWorkLoadView" property="otherSum">
				<td class="listClasses">
					<bean:write name="other"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="otherSumTotal"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.total"/>
			</td>
			<logic:iterate id="total" name="weeklyWorkLoadView" property="totalSum">
				<td class="listClasses">
					<bean:write name="total"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="totalSumTotal"/>
			</td>
		</tr>
	</table>

		</logic:iterate>
	</logic:iterate>
</logic:present>