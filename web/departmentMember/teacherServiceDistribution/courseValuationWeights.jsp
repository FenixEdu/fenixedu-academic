<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
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
<h2><bean:message key="link.teacherServiceDistribution.valuateWeights"/></h2>

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
		<bean:message key="label.teacherServiceDistribution.valuateWeights"/>
	</em>
</p>

<html:form action="/tsdCourseValuation">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setTSDCourseWeights"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdCourse" property="tsdCourse"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.forward" property="forward" value="courseValuationWeights"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstTimeEnrolledStudentsManual" property="firstTimeEnrolledStudentsManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.secondTimeEnrolledStudentsManual" property="secondTimeEnrolledStudentsManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentsPerShiftManual" property="studentsPerShiftManual"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftFrequency" property="shiftFrequency"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="weightFirstTimeEnrolledStudentsPerShiftType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="weightSecondTimeEnrolledStudentsPerShiftType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />
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

<logic:empty name="selectedTSDLoad">
	<p class="mtop15">
		<em>
			<bean:message key="label.teacherServiceDistribution.shiftTypeNotAvailableForTSDCourse"/>
		</em>
	</p>
</logic:empty>

<logic:notEmpty name="selectedTSDLoad">

<table class='tstyle4 mtop05 thlight'>
	<tr>
		<th>
			<b><bean:message name="selectedTSDLoad" property="type.name"/></b>
		</th>
		<th>
		</th>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerShift"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="weightFirstTimeEnrolledStudentsPerShift" />
		</td>
	</tr>
	<tr>
		<td class='highlight7'>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerShift"/>
		</td>
		<td class='aright'>
			<bean:write name="selectedTSDLoad" property="weightSecondTimeEnrolledStudentsPerShift" />
		</td>
	</tr>
</table>

<br/>
<br/>

<%  TSDCourse selectedTSDCourse = (TSDCourse)request.getAttribute("selectedTSDCourse"); 
	ShiftType shiftType = (ShiftType)request.getAttribute("shiftType");
%>

<b><bean:message key="label.teacherServiceDistribution.newValues"/>:</b>
<br/>
<table class="tstyle5 thlight thmiddle mtop05 mbottom05">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.shiftType"/>:
		</th>
		<td>
			<html:select property="shiftType" onchange="this.form.method.value='loadTSDCurricularLoadForWeights'; this.form.submit();">
				<html:options collection="shiftsList" property="name" labelProperty="fullNameTipoAula"/>
			</html:select>			
		</td>		
	</tr>
</table>

<table class='tstyle4 mtop0 mbottom05'>
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.value"/>
		</th>
	</tr>
		
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.weightFirstTimeEnrolledStudentsPerShift"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.weightFirstTimeEnrolledStudentsPerShiftManual" property="weightFirstTimeEnrolledStudentsPerShiftManual" size="3" maxlength="3" styleClass="aleft"/>
		</td>
	</tr>
	<tr>
		<td>
			<bean:message key="label.teacherServiceDistribution.weightSecondTimeEnrolledStudentsPerShift"/>
		</td>
		<td class='aright'>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.weightSecondTimeEnrolledStudentsPerShiftManual" property="weightSecondTimeEnrolledStudentsPerShiftManual" size="3" maxlength="3" styleClass="aleft"/>
		</td>
	</tr>	
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>
<span class="error">
	<html:errors/>
</span>
<br/>

</logic:notEmpty>

</html:form>

<html:link page='<%= "/tsdCourse.do?method=prepareLinkForTSDCourse&amp;tsd=" + tsdId + "&amp;tsdProcess=" + tsdProcessId + "&amp;tsdCourse=" + tsdCourseId + "&amp;notTSDCourseViewLink=true" %>'>
	<bean:message key="link.back"/>
</html:link>


