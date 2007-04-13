<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ValuationTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.CourseValuationDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.ProfessorshipValuationDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal"/>
	<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
		<bean:write name="teacherServiceDistribution" property="name"/>&nbsp;
<%-- 		(<bean:write name="teacherServiceDistribution" property="executionPeriod.semester"/>�<bean:message key="label.common.courseSemester"/>--%>
		<bean:write name="teacherServiceDistribution" property="executionYear.year"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionValuation"/>
</h3>

<html:form action="/teacherServiceDistributionValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherServiceDistribution" property="teacherServiceDistribution"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<table class='search'>
	<tr class='tdleft'>
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
	<tr class='tdleft'>
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
		<html:link href="javascript:document.forms[0].method.value='exportTeacherServiceDistributionValuationToExcel'; document.forms[0].submit();">
			<bean:message key="label.teacherService.exportToExcel"/>
		</html:link>
	</logic:notEmpty>
</logic:notEmpty>
<br/>
<br/>
<br/>

<bean:message key="label.teacherService.navigateBy"/>:
<html:link href="javascript:document.forms[0].method.value='changeToViewCourses'; document.forms[0].submit();">
	<bean:message key="label.teacherService.navigateByCourse"/> </b> 
</html:link> | 
<html:link href="javascript:document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<b> <bean:message key="label.teacherService.viewByCoursesAndTeachers"/> </b> |
<html:link href="javascript:document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
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
		<td class="aright">
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