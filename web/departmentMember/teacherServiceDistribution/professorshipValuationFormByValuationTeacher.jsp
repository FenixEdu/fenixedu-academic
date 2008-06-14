<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDValueType" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProfessorship" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher" %>
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


<h3>
	
</h3>

<html:form action="/tsdProfessorship">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value=""/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProcess" property="tsdProcess"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.viewType" property="viewType"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tsdProfessorship" property="tsdProfessorship"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributionViewAnchor" property="distributionViewAnchor"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hidden" property="hoursType" value="<%= TSDValueType.MANUAL_VALUE.toString() %>" />

<html:link href="javascript:void(0)" onclick="document.forms[0].method.value='loadTSDProfessorships'; document.forms[0].viewType.value=1; document.forms[0].submit()">
	<bean:message key="label.teacherService.navigateByCourse"/>
</html:link> |
<b> <bean:message key="label.teacherService.navigateByTeacher"/> </b>
<br/>

<table class="tstyle5 thlight thright mbottom0">
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
<logic:notPresent name="notAvailableTSDTeachers">
<tr>
	<th>
		<bean:message key="label.teacherServiceDistribution.tsdTeacher"/>:
	</th>
	<td>
		<html:select property="tsdTeacher" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
			<html:options collection="tsdTeacherList" property="idInternal" labelProperty="name"/>
		</html:select>							
	</td>
</tr>
</table>
<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.associatedCourses"/>:</b>
<br/>
<table class='tstyle1 thlight mtop05'>
	<tr class='acenter'>
		<th>
			<bean:message key="label.teacherServiceDistribution.name"/>
		</th>
		<logic:iterate name="selectedTeacher" property="associatedShiftTypes" id="type">
		<th>
			H. <bean:write name="type" property="fullNameTipoAula"/>
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
   TSDTeacher tsdTeacher = (TSDTeacher) request.getAttribute("selectedTeacher");
%>
	
<logic:iterate name="selectedTeacher" property="associatedTSDCourses" id="tsdCourse">
	<tr>
		<td  width="250">
			<bean:write name="tsdCourse" property="name"/>
		</td>
		<% TSDProfessorship tsdProfessorship = null; %>
		<logic:iterate name="selectedTeacher" property="associatedShiftTypes" id="type">
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= tsdTeacher.getTotalHoursLecturedOnTSDCourseByShiftType((TSDCourse)tsdCourse, (ShiftType)type) %>
			</fmt:formatNumber>
		</td>
		<% tsdProfessorship = tsdProfessorship == null ? tsdTeacher.getTSDProfessorShipByCourseAndShiftType((TSDCourse)tsdCourse, (ShiftType)type) : tsdProfessorship; %>
		</logic:iterate>
		<td>
			&nbsp;
		</td>					
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 	<%= tsdTeacher.getTotalHoursLecturedOnTSDCourse((TSDCourse)tsdCourse) %>
			</fmt:formatNumber>
		</td>
		<td>
		</td>
		<td class="aright">
			<html:link href="javascript:void(0)" onclick='<%= "document.forms[0].method.value='removeTSDProfessorships'; document.forms[0].tsdProfessorship.value=" + tsdProfessorship.getIdInternal().toString() + ";document.forms[0].submit();" %>' >
				<bean:message key="label.teacherServiceDistribution.delete"/>
			</html:link>
		</td>
	</tr>
</logic:iterate>
<logic:equal name="selectedTSDTeacher" property="usingExtraCredits" value="true">
	<tr>
		<td  width="250">
			<bean:write name="selectedTSDTeacher" property="extraCreditsName"/>
		</td>
		<logic:iterate name="selectedTeacher" property="associatedShiftTypes" id="type">
		<td>
			&nbsp;
		</td>
		</logic:iterate>
		<td>
			&nbsp;
		</td>				
		<td class="aright">
			<bean:write name="selectedTSDTeacher" property="extraCreditsValue"/>
		</td>
		<td>
		</td>
		<td>
			&nbsp;
		</td>
	</tr>
</logic:equal>
	<tr>
		<td  width="250">
			&nbsp;
		</td>
	<logic:iterate name="selectedTeacher" property="associatedShiftTypes" id="type">
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
		<td  width="250">
			<bean:message key="label.teacherServiceDistribution.total"/>
		</td>
		<logic:iterate name="selectedTeacher" property="associatedShiftTypes" id="type">
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
			 	<%= tsdTeacher.getTotalHoursLecturedByShiftType((ShiftType)type, null) %>
			</fmt:formatNumber> 
		</td>
		</logic:iterate>
		<td>
			&nbsp;
		</td>				
		<td class="aright">
			<b>
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
					<bean:write name="selectedTSDTeacher" property="totalHoursLecturedPlusExtraCredits"/>
				</fmt:formatNumber>
				/
				<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1"> 
					<bean:write name="selectedTSDTeacher" property="requiredHours"/>
				</fmt:formatNumber>
			</b>
		</td>
		<td>
		</td>		
		<td>
		</td>	
	</tr>	
</table>
<br/>

<b><bean:message key="label.teacherServiceDistribution.extraCredits"/>:</b>
<br/>
<table class='tstyle4 mtop05 thlight'>
	<tr class='acenter'>
		<th>
			<bean:message key="label.teacherServiceDistribution.extraCreditsName"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.value"/>
		</th>
		<th>
			<bean:message key="label.teacherServiceDistribution.creditsInUse"/>
		</th>
		<th/>
	</tr>
	<tr>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.extraCreditsName" property="extraCreditsName" size="28" maxlength="240"/>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.extraCreditsValue" property="extraCreditsValue" size="3" maxlength="4" styleClass="aleft"/>
		</td>
		<td>
			<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.usingExtraCredits" property="usingExtraCredits"/>
		</td>
		<td>
			<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setExtraCredits'; this.form.page.value='3'; this.form.submit();">
				<bean:message key="label.teacherServiceDistribution.assign"/>
			</html:button>
		</td>
	</tr>
</table>

<br/>
<br/>

<b><bean:message key="label.teacherServiceDistribution.associateTeacherToCourse"/>:</b>
<table class="tstyle5 thlight thright mbottom05 mtop05">
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
		<th>
			<bean:message key="label.teacherServiceDistribution.competenceCourse"/>:
		</th>
		<td>
			<html:select property="competenceCourse" onchange="this.form.method.value='loadCompetenceCourse'; this.form.submit();">
				<html:options collection="competenceCourseList" property="idInternal" labelProperty="name"/>
			</html:select>									
		</td>
	</tr>
	<tr>
		<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION.toString() %>">
		<th>
			<bean:message key="label.teacherServiceDistribution.course"/>:
		</th>
		<td>
			<html:select property="tsdCurricularCourse" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
				<html:options collection="tsdCurricularCourseList" property="idInternal" labelProperty="degreeName"/>
			</html:select>								
		</td>
		</logic:equal>
		<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.CURRICULAR_COURSE_VALUATION_GROUP.toString() %>">
		<th>
			<bean:message key="label.teacherServiceDistribution.tsdCurricularCourseGroup"/>:
		</th>
		<td>
			<html:select property="tsdCurricularCourseGroup" onchange="this.form.method.value='loadTSDProfessorships'; this.form.submit();">
				<html:options collection="tsdCurricularCourseGroupList" property="idInternal" labelProperty="name"/>
			</html:select>			
		</td>
		</logic:equal>
	</tr>
	</logic:notPresent>
</table>

<logic:notPresent name="notAvailableCompetenceCourses">
<logic:notEqual name="selectedTSDCourseType" value="<%= TSDCourseType.NOT_DETERMINED.toString() %>">
<logic:notPresent name="tsdCourseNotSelected">


<%  
	ShiftType shiftType = (ShiftType)request.getAttribute("shiftType");
	TSDCourse selectedTSDCourse = (TSDCourse)request.getAttribute("selectedTSDCourse"); 
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
				<%= tsdTeacher.getRealHoursByShiftTypeAndExecutionCourses(shiftType, selectedTSDCourse.getAssociatedExecutionCoursesLastYear()) %>
			</fmt:formatNumber>
		</td>
		<td class="aright">
			<fmt:formatNumber maxFractionDigits="2" minFractionDigits="1">
				<%= tsdTeacher.getRealHoursByShiftTypeAndExecutionCourses(shiftType, selectedTSDCourse.getAssociatedExecutionCourses()) %>
			</fmt:formatNumber>
		</td>
	</tr>
</table>


<html:button bundle="HTMLALT_RESOURCES" altKey="button." property="" onclick="this.form.method.value='setTSDProfessorship'; this.form.page.value='1'; this.form.submit();">
	<bean:message key="label.teacherServiceDistribution.assign"/>
</html:button>
<br/>
<br/>

</logic:notPresent>
</logic:notEqual>
<logic:equal name="selectedTSDCourseType" value="<%= TSDCourseType.NOT_DETERMINED.toString() %>">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoPratHoursManual" property="theoPratHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.praticalHoursManual" property="praticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.theoreticalHoursManual" property="theoreticalHoursManual" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.laboratorialHoursManual" property="laboratorialHoursManual" value="0"/>
	<br/>
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.nonValuatedCourse"/>
		</em>
	</p>
</logic:equal>

</logic:notPresent>
<logic:present name="notAvailableCompetenceCourses">
	<br/>
	<br/>
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.noCompetenceCoursesForExecutionPeriod"/>
		</em>
	</p>
	<br/>
</logic:present>

</logic:notPresent>
<logic:present name="notAvailableTSDTeachers">
</table>
	<br/>
	<br/>
	<p>
		<em>
			<bean:message key="label.teacherServiceDistribution.notExistsTSDTeachers"/>
		</em>
	</p>
	<br/>
</logic:present>

<span class="error">
	<html:errors/>
</span>


<br/>
<logic:notEqual name="tsdProfessorshipForm" property="distributionViewAnchor" value="0">
	<bean:define id="tsdTeacherID" name="tsdProfessorshipForm" property="distributionViewAnchor"/>
	<html:link page='<%= "/tsdProcessValuation.do?method=prepareForTSDProcessValuationByTeachers&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() 
	+ "#" + tsdTeacherID %>'>
		<bean:message key="link.teacherServiceDistribution.backTotsdProcessVisualization"/>
	</html:link>
</logic:notEqual>
<logic:equal name="tsdProfessorshipForm" property="distributionViewAnchor" value="0">
	<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + ((TSDProcess) request.getAttribute("tsdProcess")).getIdInternal().toString() %>'>
		<bean:message key="link.back"/>
	</html:link>
</logic:equal>

</html:form>


