<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
		<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCreation"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionCopy"/>
</h3>

<html:form action="/teacherServiceDistributionCopy">
<html:hidden property="method" value="copyTeacherServiceDistribution"/>
<html:hidden property="page" value="0"/>

<table class='vtsbc'>
<tr>
	<th colspan="2">
		<b><bean:write name="departmentName"/></b>
	</th>
</tr>
<tr>
	<td align="left">
		<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionYear" onchange="this.form.method.value='prepareForTeacherServiceDistributionCopy'; this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
	<td>
		<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionPeriod">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
</table>
<br/>

<logic:empty name="teacherServiceDistributionList">
	<span class="error">
		<b><bean:message key="label.teacherServiceDistribution.nonAvailableTeacherServiceDistributions"/></b>
	</span>
</logic:empty>
<logic:notEmpty name="teacherServiceDistributionList">
	<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
	<br/>
	<table class='vtsbc'>
		<tr>
			<th/>
			<th>
				<bean:message key="label.teacherServiceDistribution.name"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.executionYear"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.semesters"/>
			</th>
		</tr>
	<logic:iterate name="teacherServiceDistributionList" id="teacherServiceDistribution"> 
		<tr>
			<td>
				<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal" />
				<html:radio property="teacherServiceDistribution" value="<%= ((Integer)teacherServiceDistributionId).toString() %>"/>
			</td>
			<td class="courses" align="left" width="200">
				<bean:write name="teacherServiceDistribution" property="name"/>
			</td>
			<td class="courses" align="center">
				<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
			</td>
			<td class="courses" align="center">
				<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
					<bean:write name="executionPeriod" property="semester"/>º&nbsp;
				</logic:iterate>
			</td>
		</tr>
	</logic:iterate>
	</table>

	<br/>
	<br/>

	<table class='vtsbc'>
	<tr>
		<th colspan="2">
			<b><bean:message key="label.teacherServiceDistribution.newDistributionData"/></b>
		</th>
	</tr>
	<tr>
		<td align="left">
			<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
			&nbsp;&nbsp;
			<html:select property="executionYearForCopy" onchange="this.form.method.value='prepareForTeacherServiceDistributionCopy'; this.form.submit();">
				<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
			</html:select>
		</td>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
			&nbsp;&nbsp;
			<html:select property="executionPeriodForCopy">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodsListForCopy" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="2">
			<b><bean:message key="label.teacherServiceDistribution.name"/>:<b>
			&nbsp;&nbsp;
			<html:text property="name" size="40" maxlength="240" />
		</td>
	</tr>
	</table>

	<html:button property="" onclick="this.form.method.value='copyTeacherServiceDistribution'; this.form.page.value=1; this.form.submit()">
		<bean:message key="label.teacherServiceDistribution.create"/>
	</html:button>
</logic:notEmpty>

<br/>
<span class="error"><html:errors property="name"/></span>
<br/>
<br/>

<html:link page="/teacherServiceDistribution.do?method=prepareForTeacherServiceDistributionCreation">
	<bean:message key="link.back"/>
</html:link>
</html:form>