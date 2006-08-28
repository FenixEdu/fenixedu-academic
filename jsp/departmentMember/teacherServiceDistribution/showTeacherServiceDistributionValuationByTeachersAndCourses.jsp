<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ProfessorshipValuationDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<style>
table.vtsbc {
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
margin-bottom: 1em;
border: 2px solid #aaa;
text-align: center;
border-collapse: collapse;
}
table.vtsbc th {
padding: 0.2em 0.2em;
border: 1px solid #bbb;
border-bottom: 1px solid #aaa;
background-color: #cacaca;
font-weight: bold;
}
table.vtsbc td {
background-color: #eaeaea;
border: none;
border: 1px solid #ccc;
padding: 0.25em 0.5em;
}
table.vtsbc td.courses {
background-color: #f4f4f8;
width: 300px;
padding: 0.25em 0.25em;
text-align: left;
}
.center {
text-align: center;
}

.right td {
text-align: right;
}

table.search {
background-color: #f5f5f5;
border-collapse: collapse;
}
table.search tr td {
border: 1px solid #fff;
padding: 0.3em;
}
.leftcolumn {
text-align: right;
}
.left td {
text-align: left;
}

</style>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal"/>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%-- 		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>º<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/>
</h3>

<html:form action="/teacherServiceDistributionValuation">
<html:hidden property="method" value=""/>
<html:hidden property="teacherServiceDistribution"/>
<html:hidden property="viewType"/>

<table class='search'>
	<tr class='left'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.valuationPhase"/>:</b>
		</td>
		<td>
			<html:select property="valuationPhase" onchange="this.form.method.value='loadValuationPhase'; this.form.submit();">
				<html:options collection="valuationPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
			&nbsp;
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
	<tr class='left'>
		<td>
			<b><bean:message key="label.teacherServiceDistribution.ValuationGrouping"/>:</b>
		</td>
		<td colspan="2">
			<html:select property="valuationGrouping" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
				
				<html:options collection="valuationGroupingOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>
<logic:notEmpty name="valuationTeacherDTOEntryList">
	<logic:notEmpty name="courseValuationDTOEntryList">
		<b>&bull;</b>&nbsp;
		<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='exportTeacherServiceDistributionValuationToExcel'; document.teacherServiceDistributionValuationForm.submit();">
			<bean:message key="label.teacherService.exportToExcel"/>
		</html:link>
	</logic:notEmpty>
</logic:notEmpty>
<br/>
<br/>
<br/>

<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/> </b> 
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<b> <bean:message key="label.teacherService.viewByCoursesAndTeachers"/> </b> |
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCharts'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
</html:link>
<br/>
<br/>

<table class='vtsbc'>
	<tr>
		<th>
		</th>
<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<th>
			<bean:write name="courseValuationDTOEntry" property="courseValuation.acronym"/>
		</th>
</logic:iterate>
		<th>
		</th>
	</tr>
	
<logic:iterate name="valuationTeacherDTOEntryList" id="valuationTeacherDTOEntry">
<bean:define id="valuationTeacherDTOEntry" name="valuationTeacherDTOEntry"/>
	<tr >
		<th>
			<bean:write name="valuationTeacherDTOEntry" property="emailUserId"/>
		</th>
		<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<bean:define id="courseValuationDTOEntry" name="courseValuationDTOEntry"/>
		<td align="right">
			<%
				ProfessorshipValuationDTOEntry professorshipValuationDTOEntry = ((ValuationTeacherDTOEntry) valuationTeacherDTOEntry).getProfeshipValuationDTOEntryByCourseValuationDTOEntry((CourseValuationDTOEntry) courseValuationDTOEntry);
				
				if(professorshipValuationDTOEntry != null) {
			%>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= professorshipValuationDTOEntry.getProfessorshipValuation().getTotalHours() %>
			</fmt:formatNumber>
			<%
				}
			%>
		</td>
		</logic:iterate>
		<th>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="valuationTeacherDTOEntry" property="totalHoursLectured"/>
			</fmt:formatNumber>
		</th>
	</tr>
</logic:iterate>

	<tr>
		<th>
		</th>
	<logic:iterate name="courseValuationDTOEntryList" id="courseValuationDTOEntry">
		<th>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="courseValuationDTOEntry" property="courseValuation.totalHoursLectured"/>
			</fmt:formatNumber>
		</th>
	</logic:iterate>
		<th>
		</th>
	</tr>

</table>

<br/>
<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>