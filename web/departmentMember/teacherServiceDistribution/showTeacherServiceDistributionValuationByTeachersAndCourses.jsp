<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDProfessorshipDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>

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

<logic:notEmpty name="tsdTeacherDTOEntryList">
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
		<bean:message key="label.teacherService.navigateByCourse"/> </b> 
	</html:link> | 
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewTeachers'; document.forms[0].submit();">
		<bean:message key="label.teacherService.navigateByTeacher"/>
	</html:link> | 
	<b> <bean:message key="label.teacherService.viewByCoursesAndTeachers"/> </b> |
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewCharts'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.viewByCharts"/>
	</html:link> |
	<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='changeToViewPlanning'; document.forms[0].submit();">
		<bean:message key="label.teacherServiceDistribution.viewPlanning"/>
	</html:link>
</p>

<table class='tstyle4 thlight'>
	<tr>
		<th>
		</th>
<logic:iterate name="tsdCourseDTOEntryList" id="tsdCourseDTOEntry">
		<th>
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.acronym"/>
		</th>
</logic:iterate>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.total"/></b>
		</th>
	</tr>
	
<logic:iterate name="tsdTeacherDTOEntryList" id="tsdTeacherDTOEntry">
	<tr>
		<td>
			<bean:write name="tsdTeacherDTOEntry" property="shortName"/>
			<logic:notEmpty name="tsdTeacherDTOEntry" property="teacherNumber">
				(<bean:write name="tsdTeacherDTOEntry" property="teacherNumber"/>)
			</logic:notEmpty>
		</td>
		<logic:iterate name="tsdCourseDTOEntryList" id="tsdCourseDTOEntry">
		<td class="aright">
			<%
				TSDProfessorshipDTOEntry tsdProfessorshipDTOEntry = ((TSDTeacherDTOEntry) tsdTeacherDTOEntry).getTSDProfessorshipDTOEntryByTSDCourseDTOEntry((TSDCourseDTOEntry) tsdCourseDTOEntry);
				
				if(tsdProfessorshipDTOEntry != null) {
			%>
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= tsdProfessorshipDTOEntry.getTotalHours() %>
			</fmt:formatNumber>
			<%
				}
			%>
		</td>
		</logic:iterate>
		<th class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="tsdTeacherDTOEntry" property="totalHoursLectured"/>
			</fmt:formatNumber>
		</th>
	</tr>
</logic:iterate>
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.total"/></b>
		</th>
	<logic:iterate name="tsdCourseDTOEntryList" id="tsdCourseDTOEntry">
		<th class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			<bean:write name="tsdCourseDTOEntry" property="TSDCourse.totalHoursLectured"/>
			</fmt:formatNumber>
		</th>
	</logic:iterate>
		<td>
		</td>
	</tr>

</table>

<br/>
<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
	<bean:message key="link.back"/>
</html:link>

</html:form>