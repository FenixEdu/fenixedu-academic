<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuation" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.ProfessorshipValuation" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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

<logic:present name="nonValuatedCompetenceCourses">
	<b>&bull;</b>&nbsp;
	<html:link href="#noValuationCourses">
		<bean:message key="label.teacherServiceDistribution.coursesWithoutValuations"/>
	</html:link>
	<br/>
</logic:present>
<logic:notEmpty name="courseValuationDTOEntryList">
	<b>&bull;</b>&nbsp;
	<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='exportTeacherServiceDistributionValuationToExcel'; document.teacherServiceDistributionValuationForm.submit();">
		<bean:message key="label.teacherService.exportToExcel"/>
	</html:link>
</logic:notEmpty>
<br/>
<br/>
<br/>

<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeachers'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.navigateByTeacher"/>
</html:link> | 
<html:link href="javascript:document.teacherServiceDistributionValuationForm.method.value='changeToViewTeacherAndCourses'; document.teacherServiceDistributionValuationForm.submit();">
	<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
</html:link> |
<b> <bean:message key="label.teacherServiceDistribution.viewByCharts"/> </b>
<br/>
<br/>
<b>&bull;</b>&nbsp;<a href="#hoursByGrouping"><bean:message key="label.teacherServiceDistribution.hoursByGrouping"/></a>
<br/>
<b>&bull;</b>&nbsp;<a href="#numberStudentsByGrouping"><bean:message key="label.teacherServiceDistribution.numberStudentsByGrouping"/></a>
<br/>
<b>&bull;</b>&nbsp;<a href="#hoursByGroupingPercentage"><bean:message key="label.teacherServiceDistribution.hoursByGrouping"/> %</a>
<br/>
<b>&bull;</b>&nbsp;<a href="#numberStudentsByGroupingPercentage"><bean:message key="label.teacherServiceDistribution.numberStudentsByGrouping"/> %</a>
<br/>
<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
	<bean:message key="link.back"/>
</html:link>
<br/>
<br/>

<bean:define id="valuationGroupingId" name="teacherServiceDistributionValuationForm" property="valuationGrouping"/>
<bean:define id="executionPeriodId" name="teacherServiceDistributionValuationForm" property="executionPeriod"/>
<a name="hoursByGrouping">
	<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedHoursPerGrouping&valuationGrouping=" + valuationGroupingId + "&executionPeriod=" + executionPeriodId %>'/>
</a>
<br/>
<br/>

<a name="numberStudentsByGrouping">
	<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedNumberStudentsPerGrouping&valuationGrouping=" + valuationGroupingId + "&executionPeriod=" + executionPeriodId %>'/>
</a>
<br/>
<br/>

<a name="hoursByGroupingPercentage">
	<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedHoursPerGroupingPieChart&valuationGrouping=" + valuationGroupingId + "&executionPeriod=" + executionPeriodId %>'/>
</a>
<br/>
<br/>

<a name="numberStudentsByGroupingPercentage">
	<html:img page='<%= "/teacherServiceDistributionValuation.do?method=generateValuatedNumberStudentsPerGroupingPieChart&valuationGrouping=" + valuationGroupingId + "&executionPeriod=" + executionPeriodId %>'/>
</a>
<br/>
<br/>


</html:form>
