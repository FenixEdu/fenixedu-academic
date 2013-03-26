<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCurricularLoad" %>
<%@ page import="net.sourceforge.fenixedu.domain.CompetenceCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>


<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.tsdProfessorshipService"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.tsdProfessorshipService"/>
	</em>
</p>

<html:form action="/tsdProfessorship">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProfessorship" property="tsdProfessorship"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributionViewAnchor" property="distributionViewAnchor"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="hoursType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />

<b> <bean:message key="label.teacherService.navigateByCourse"/> </b> |
<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='loadTSDProfessorships'; document.forms[0].viewType.value=0; document.forms[0].submit()">
	<bean:message key="label.teacherService.navigateByTeacher"/> 
</html:link>
<br/>

<table class="tstyle5 thlight thright mbottom0 thmiddle">
<tr>
	<th style="width: 100px;">
		<bean:message key="label.teacherServiceDistribution.TeacherServiceDistribution"/>:
	</th>
	<td>
		<html:select property="tsd" onchange="this.form.method.value='loadTeacherServiceDistribution'; this.form.submit();">
			<html:options collection="tsdOptionEntryList" property="idInternal" labelProperty="name"/>
		</html:select>
	</td>
</tr>
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.semester"/>:
	</th>
	<td>
		<html:select property="executionPeriod" onchange="this.form.method.value='loadExecutionPeriod'; this.form.submit();">
			<html:options collection="executionPeriodList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>

<logic:notPresent name="notAvailableCompetenceCourses">
<tr>
	<th><bean:message key="label.teacherServiceDistribution.competenceCourse"/>:</th>
	<td>
		<html:select property="competenceCourse" onchange="this.form.method.value='loadCompetenceCourse'; this.form.submit();">
			<html:options collection="competenceCourseList" property="idInternal" labelProperty="name"/>
		</html:select>							

	</td>
</tr>
<tr>
	<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION.toString() %>">
	<th><bean:message key="label.teacherServiceDistribution.course"/>:</th>
	<td>
		<html:select property="tsdCurricularCourse" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
			<html:options collection="tsdCurricularCourseList" property="idInternal" labelProperty="degreeName"/>
		</html:select>
	</td>
	</logic:equal>
		
	<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
	<th><bean:message key="label.teacherServiceDistribution.tsdCurricularCourseGroup"/>:</th>
	<td>
		<html:select property="tsdCurricularCourseGroup" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
			<html:options collection="tsdCurricularCourseGroupList" property="idInternal" labelProperty="name"/>
		</html:select>
	</td>
	</logic:equal>
</tr>
</table>

<br/>
<br/>

<logic:notEqual name="selectedTSDCourseType" value="<%= TSDCourseType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="tsdCourseNotSelected">

<bean:define id="curricularLoadsList" name="selectedTSDCourse" property="TSDCurricularLoads"/>


<b><bean:message key="label.teacherServiceDistribution.associatedTeachers"/>:</b>
<br/>
<table class='tstyle1 thlight mtop05'>
	<tr class='center'>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<logic:iterate name="curricularLoadsList" id="curricularLoad">
		<th>
			H. <bean:write name="curricularLoad" property="type.fullNameTipoAula"/>
		</th>
		</logic:iterate>
		<th>
			&nbsp;
		</th>				
		<th>
			<bean:message key="label.teacherServiceDistribution.total"/>
		</th>
		<th>
		</th>
		<th>
		</th>					
	</tr>

<% 	
	TSDCourse selectedTSDCourse = (TSDCourse)request.getAttribute("selectedTSDCourse"); 
%>

<logic:iterate name="selectedTSDCourse" property="associatedTSDTeachers" id="tsdTeacher">
	<tr>
		<td width="250">
			<bean:write name="tsdTeacher" property="name"/>
		</td>
		<% TSDProfessorship tsdProfessorship = null; %>
		<logic:iterate name="curricularLoadsList" id="curricularLoad">
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= ((TSDTeacher)tsdTeacher).getTotalHoursLecturedOnTSDCourseByShiftType(selectedTSDCourse, ((TSDCurricularLoad)curricularLoad).getType()) %>
			</fmt:formatNumber>
		</td>
		<% tsdProfessorship = tsdProfessorship == null ? ((TSDTeacher)tsdTeacher).getTSDProfessorShipByCourseAndShiftType(selectedTSDCourse, ((TSDCurricularLoad)curricularLoad).getType()) : tsdProfessorship; %>
		</logic:iterate>
		<td>
			&nbsp;
		</td>					
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 	<%= ((TSDTeacher)tsdTeacher).getTotalHoursLecturedOnTSDCourse(selectedTSDCourse) %>
			</fmt:formatNumber>
		</td>
		<td>
		</td>
		<td class="aright">
			<% if(tsdProfessorship != null) { %>
			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].method.value='removeTSDProfessorships'; document.forms[0].tsdProfessorship.value=" + tsdProfessorship.getIdInternal().toString() + ";document.forms[0].submit();" %>' >
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
			<% } %>
		</td>
	</tr>
</logic:iterate>
	<tr>
		<td>
			&nbsp;
		</td>
		<logic:iterate name="curricularLoadsList" id="curricularLoad">
		<td>
			&nbsp;
		</td>
		</logic:iterate>
		<td>
			&nbsp;
		</td>				
		<td>
			&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>	
	<tr>
		<td width="250">
			<bean:message key="label.teacherServiceDistribution.total"/>
		</td>
		<logic:iterate name="curricularLoadsList" id="curricularLoad">
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 	<%= selectedTSDCourse.getTotalHoursLecturedByShiftType(((TSDCurricularLoad)curricularLoad).getType()) %>
			</fmt:formatNumber> 
			/
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 	<%= selectedTSDCourse.getHours(((TSDCurricularLoad)curricularLoad).getType()) %>
			</fmt:formatNumber> 
		</td>
		</logic:iterate>
		<td>
			&nbsp;
		</td>				
		<td class="aright">
			<b>
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<bean:write name="selectedTSDCourse" property="totalHoursLectured"/>
				</fmt:formatNumber>
				/
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"> 
				<bean:write name="selectedTSDCourse" property="totalHours"/>
				</fmt:formatNumber>
			</b>
		</td>
		<td>
		</td>		
		<td>
		</td>	
	</tr>	
</table>
<logic:notPresent name="notAvailableTSDTeachers">

<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
<table class="tstyle5 thlight thright mbottom05 mtop05">
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.tsdTeacher"/>:
		</th>
		<td>
			<html:select property="tsdTeacher" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
				<html:options collection="tsdTeacherList" property="idInternal" labelProperty="name"/>
			</html:select>
		</td>
		<td style="background-color: white;">
			<b><bean:message key="label.teacherServiceDistribution.availability"/>:</b>
			<logic:greaterThan name="selectedTSDTeacher" property="availability" value="0.0">
			<span style="color: green;">
			</logic:greaterThan>
			<logic:lessThan name="selectedTSDTeacher" property="availability" value="0.0">
			<span style="color: red;">
			</logic:lessThan>
			<logic:equal name="selectedTSDTeacher" property="availability" value="0.0">
			<span style="color: #aaaaaa;">
			</logic:equal>
				<b>
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
					<bean:write name="selectedTSDTeacher" property="availability"/>
				</fmt:formatNumber>
				</b>
			</span>
			<bean:message key="label.teacherService.hours"/>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.teacherServiceDistribution.shiftType"/>:
		</th>
		<td>
			<html:select property="shiftType" onchange="this.form.method.value='loadTSDCurricularLoad'; this.form.submit();">
				<html:options collection="shiftsList" property="name" labelProperty="fullNameTipoAula"/>
			</html:select>			
		</td>		
	</tr>
</table>

<%  
	ShiftType shiftType = (ShiftType)request.getAttribute("shiftType");
	TSDTeacherDTOEntry selectedTSDTeacher = (TSDTeacherDTOEntry)request.getAttribute("selectedTSDTeacher");
%>

<table class='tstyle4 mtop0 mbottom05'>
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.value"/>
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
			<bean:message key="label.teacherServiceDistribution.weeklyHours"/>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.hoursManual" property="hoursManual" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= selectedTSDTeacher.getTSDTeachers().get(0).getRealHoursByShiftTypeAndExecutionCourses(shiftType, selectedTSDCourse.getAssociatedExecutionCoursesLastYear()).toString() %>
			</fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= selectedTSDTeacher.getTSDTeachers().get(0).getRealHoursByShiftTypeAndExecutionCourses(shiftType, selectedTSDCourse.getAssociatedExecutionCourses()) %>
			</fmt:formatNumber>
		</td>
	</tr>
</table>

<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setTSDProfessorship'; this.form.page.value='1'; this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>

<span class="error">
	<html:errors/>
</span>
<br/>


</logic:notPresent>

<logic:present name="notAvailableTSDTeachers">
	<br/>
	<br/>
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.notExistsTSDTeachers"/>
		</em>
	</p>
	<br/>
</logic:present>

</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.NOT_DETERMINED.toString() %>">
	<br/>
	<br/>
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.nonValuatedCourse"/>
		</em>
	</p>
	<br/>
</logic:equal>
</logic:notPresent>
<br/>

<logic:present name="notAvailableCompetenceCourses">
</table>
	<br/>
	<br/>
	<p>
		<em>
		<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</em>
	</p>
	<br/>
</logic:present>


<logic:notEqual name="tsdProfessorshipForm" property="distributionViewAnchor" value="0">
	<bean:define id="tsdCourseID" name="tsdProfessorshipForm" property="distributionViewAnchor"/>
	<html:link page='<%= "/tsdProcessValuation.do?method=prepareForTSDProcessValuation&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() 
	+ "#" + tsdCourseID %>'>
		<bean:message key="link.teacherServiceDistribution.backTotsdProcessVisualization"/>
	</html:link>
</logic:notEqual>
<logic:equal name="tsdProfessorshipForm" property="distributionViewAnchor" value="0">
	<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:equal>


</html:form>
