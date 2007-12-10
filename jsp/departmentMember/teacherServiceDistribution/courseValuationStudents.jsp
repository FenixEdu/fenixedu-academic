<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<bean:define id="tsdProcessId" name="tsdProcess" property="idInternal"/>
<bean:define id="tsdCourseId" name="selectedTSDCourse" property="idInternal"/>
<bean:define id="tsdId" name="selectedTSD" property="idInternal"/>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.valuateStudents"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + tsdProcessId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<html:link page='<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&amp;tsd=" + tsdId + "&amp;tsdProcess=" + tsdProcessId + "&amp;tsdCourse=" + tsdCourseId + "&amp;notTSDCourseViewLink=true" %>'>
			<bean:message key="link.teacherServiceDistribution.tsdCourseService"/>
		</html:link>
		> 
		<bean:message key="label.teacherServiceDistribution.valuateStudents"/>
	</em>
</p>

<html:form action="/tsdCourseValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setTSDCourseStudents"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourse" property="tsdCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.forward" property="forward" value="courseValuationStudents"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="firstTimeEnrolledStudentsType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="secondTimeEnrolledStudentsType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="tsd"/>

<table class="tstyle1 thright thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.teacherService.course.name"/>:
		</th>
		<td>
			<bean:write name="selectedTSDCourse" property="name" />
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherService.course.semester"/>:
		</th>
		<td>
			<bean:write name="selectedTSDCourse" property="executionPeriod.name" />
		</td>
	</tr>
</table>

<br/>

<b><bean:message key="label.teacherServiceDistribution.currentValues"/>:</b>
<br/>
<table class='tstyle4 mtop05 thlight'>
	<tr>
		<th>
			<b><bean:message key="label.teacherServiceDistribution.studentsTotal"/></b>
		</th>
		<th>
		</th>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="firstTimeEnrolledStudents" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="secondTimeEnrolledStudents" />
		</td>
	</tr>
</table>

<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.newValues"/>:</b>
<table class='tstyle4 mtop05 mbottom05'>
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.manualValue"/>
		</th>
		<th>
			<bean:write name="selectedTSDCourse" property="lastYearExecutionPeriod.executionYear.year"/>
		</th>
		<th>
			<bean:write name="selectedTSDCourse" property="executionPeriod.executionYear.year"/>
		</th>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.firstTimeEnrolledStudents"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.firstTimeEnrolledStudentsManual" property="firstTimeEnrolledStudentsManual" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="realFirstTimeEnrolledStudentsNumberLastYear"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="realFirstTimeEnrolledStudentsNumber"/>
		</td>
	</tr>
	
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.secondTimeEnrolledStudents"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.secondTimeEnrolledStudentsManual" property="secondTimeEnrolledStudentsManual" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="realSecondTimeEnrolledStudentsNumberLastYear"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDCourse" property="realSecondTimeEnrolledStudentsNumber"/>
		</td>
	</tr>	
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
</html:form>
<br/>
<br/>
<span class="error">
	<html:errors/>
</span>
<br/>

<logic:present name="notAvailableCompetenceCourses">
	<em>
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
	</em>
	<br/>
	<br/>
</logic:present>

<html:link page='<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&amp;tsd=" + tsdId + "&amp;tsdProcess=" + tsdProcessId + "&amp;tsdCourse=" + tsdCourseId + "&amp;notTSDCourseViewLink=true" %>'>
	<bean:message key="link.back"/>
</html:link>


