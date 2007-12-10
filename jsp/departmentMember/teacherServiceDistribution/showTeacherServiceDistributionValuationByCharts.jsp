<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionUtils" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProcessValuation"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<bean:define id="tsdProcessId" name="tsdProcess" property="idInternal"/>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProcessValuation"/>
	</em>
</p>


<html:form action="/tsdProcessValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>

<table class='tstyle5'>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.tsdProcessPhase"/>:
		</td>
		<td>
			<html:select property="tsdProcessPhase" onchange="this.form.method.value='loadTSDProcessPhase'; this.form.submit();">
				<html:options collection="tsdProcessPhaseList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
		</td>
		<td>
			<html:select property="tsd" onchange="this.form.method.value='loadTSDProcess'; this.form.submit();">
				<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<td align="right">
			<bean:message key="label.teacherServiceDistribution.semester"/>:
		</td>
		<td>
			<html:select property="executionPeriod" onchange="this.form.method.value='loadTSDProcess'; this.form.submit();">
				<html:option value="-1"><bean:message key="label.teacherServiceDistribution.both"/></html:option>
				<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
			</html:select>
		</td>
	</tr>
</table>
<br/>

<logic:notEmpty name="tsdCourseDTOEntryList">
	<ul>
		<li>
			<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='exportTSDProcessValuationToExcel'; document.forms[0].submit();">
				<bean:message key="label.teacherService.exportToExcel"/>
			</html:link>
		</li>
	</ul>
</logic:notEmpty>

<br/>

<p class="mtop15 mbottom1">
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByCourse"/>
	</html:link> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByTeacher"/>
	</html:link> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeacherAndCourses'; document.forms[0].submit();">
		<bean:message key="label.teacherService.viewByCoursesAndTeachers"/>
	</html:link> |
	<b> <bean:message key="label.teacherServiceDistribution.viewByCharts"/> </b>
</p>
</html:form>

<%-- 
<ul class="mvert15">
	<li>
		<html:link href="javascript:void(0)" anchor="hoursByGrouping"><bean:message key="label.teacherServiceDistribution.hoursByGrouping"/></html:link>
	</li>
	<li>
		<a href="#numberStudentsByGrouping"><bean:message key="label.teacherServiceDistribution.numberStudentsByGrouping"/></a>
	</li>
	<li>
		<a href="#hoursByGroupingPercentage">% <bean:message key="label.teacherServiceDistribution.hoursByGrouping"/></a>
	</li>
	<li>
		<a href="#numberStudentsByGroupingPercentage">% <bean:message key="label.teacherServiceDistribution.numberStudentsByGrouping"/></a>
	</li>
</ul>
--%>

<br/>
<br/>

<bean:define id="tsdId" name="tsdProcessValuationForm" property="tsd"/>
<bean:define id="executionPeriodId" name="tsdProcessValuationForm" property="executionPeriod"/>

<html:img imageName="hoursByGrouping" page="<%= "/tsdProcessValuation.do?method=generateValuatedHoursPerGrouping&amp;tsd=" + tsdId + "&amp;executionPeriod=" + executionPeriodId %>"/>

<br/>
<br/>

<html:img imageName="numberStudentsByGrouping" page='<%= "/tsdProcessValuation.do?method=generateValuatedNumberStudentsPerGrouping&amp;tsd=" + tsdId + "&amp;executionPeriod=" + executionPeriodId %>'/>
<br/>
<br/>

<html:img imageName="hoursByGroupingPercentage" bundle="HTMLALT_RESOURCES" altKey='img.img' page='<%= "/tsdProcessValuation.do?method=generateValuatedHoursPerGroupingPieChart&amp;tsd=" + tsdId + "&amp;executionPeriod=" + executionPeriodId %>'/>

<br/>
<br/>

<html:img imageName="numberStudentsByGroupingPercentage" bundle="HTMLALT_RESOURCES" altKey='img.img' page='<%= "/tsdProcessValuation.do?method=generateValuatedNumberStudentsPerGroupingPieChart&amp;tsd=" + tsdId + "&amp;executionPeriod=" + executionPeriodId %>'/>

<br/>
<br/>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
	<bean:message key="link.back"/>
</html:link>
