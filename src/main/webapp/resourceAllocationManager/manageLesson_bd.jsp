<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@page import="org.fenixedu.bennu.core.domain.Bennu"%>
<%@page import="org.joda.time.Weeks"%>
<%@page import="org.joda.time.YearMonthDay"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionCourse"%>
<%@page import="pt.ist.fenixframework.FenixFramework"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<jsp:include page="/commons/contextLessonAndShiftAndExecutionCourseAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turnos"/></h2>

<jsp:include page="context.jsp"/>

<h3><bean:message key="title.manage.aulas"/></h3>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>


<bean:message key="message.weekdays"/>

<%
	final ExecutionCourse executionCourse = FenixFramework.getDomainObject(pageContext.findAttribute("executionCourseOID").toString());
	final YearMonthDay firstPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getLeft();
	final YearMonthDay lastPossibleLessonDay = executionCourse.getMaxLessonsPeriod().getRight();
%>

<script src="https://rawgithub.com/timrwood/moment/2.0.0/moment.js"></script>
<script type="text/javascript">
	function changeStartWeek(lessonPeriodStartDate) {
		var startWeekValue = $('input[name="newBeginDateWeek"]').attr("value") - 1;
		var lessonPeriodStartMoment = moment(lessonPeriodStartDate, "YYYY-MM-DD HH:mm:ss");
		var newDate = lessonPeriodStartMoment.add('weeks', startWeekValue);
		$('input[name="newBeginDate"]').val(newDate.format("DD/MM/YYYY"));
	};
	function changeEndWeek(lessonPeriodStartDate, lessonPeriodEndDate) {
		var startWeekValue = $('input[name="newEndDateWeek"]').attr("value") - 1;
		var lessonPeriodStartMoment = moment(lessonPeriodStartDate, "YYYY-MM-DD HH:mm:ss");
		var lessonPeriodEndMoment = moment(lessonPeriodEndDate, "YYYY-MM-DD HH:mm:ss");
		var newDate = lessonPeriodStartMoment.add('weeks', startWeekValue).add('days', 6);
		if (newDate.isAfter(lessonPeriodEndMoment)) {
			newDate = lessonPeriodEndMoment;
		}
		$('input[name="newEndDate"]').val(newDate.format("DD/MM/YYYY"));
	};
	function changeStartDate(lessonPeriodStartDate) {
		var lessonPeriodStartMoment = moment(lessonPeriodStartDate, "YYYY-MM-DD HH:mm:ss");
		var startDateValue = $('input[name="newBeginDate"]').attr("value") + "00:00:00";
		var startDateMoment = moment(startDateValue, "DD/MM/YYYY HH:mm:ss");
		var weeks = startDateMoment.diff(lessonPeriodStartMoment, 'weeks') + 1;
		$('input[name="newBeginDateWeek"]').val(weeks);
	};
	function changeEndDate(lessonPeriodStartDate, lessonPeriodEndDate) {
		var lessonPeriodStartMoment = moment(lessonPeriodStartDate, "YYYY-MM-DD HH:mm:ss");
		var lessonPeriodEndMoment = moment(lessonPeriodEndDate, "YYYY-MM-DD HH:mm:ss");
		var endDateValue = $('input[name="newEndDate"]').attr("value") + "00:00:00";
		var endDateMoment = moment(endDateValue, "DD/MM/YYYY HH:mm:ss");
		var weeks = endDateMoment.diff(lessonPeriodStartMoment, 'weeks') + 1;
		$('input[name="newEndDateWeek"]').val(weeks);
	};
</script>

<html:form action="/manageLesson" focus="diaSemana">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseRoom"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<logic:present name="action">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.action" property="action" value="<%= pageContext.findAttribute("action").toString() %>"/>
	</logic:present>
    <html:hidden alt="<%= PresentationConstants.ACADEMIC_INTERVAL %>" property="<%= PresentationConstants.ACADEMIC_INTERVAL %>"
                 value="<%= pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL).toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_DEGREE_OID %>" property="<%= PresentationConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.CURRICULAR_YEAR_OID %>" property="<%= PresentationConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.EXECUTION_COURSE_OID %>" property="<%= PresentationConstants.EXECUTION_COURSE_OID %>"
				 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
	<html:hidden alt="<%= PresentationConstants.SHIFT_OID %>" property="<%= PresentationConstants.SHIFT_OID %>"
				 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>
	<logic:present name="lessonOID">
		<html:hidden alt="<%= PresentationConstants.LESSON_OID %>" property="<%= PresentationConstants.LESSON_OID %>"
					 value="<%= pageContext.findAttribute("lessonOID").toString() %>"/>
	</logic:present>

	<table class="tstyle5 thlight thright">
		<tr>
	      	<th><bean:message key="property.aula.weekDay"/>:</th>
	        <td>
    	    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.diaSemana" property="diaSemana" size="2"/>
	       	</td>
   		</tr>
	   	<tr>
    		<th><bean:message key="property.aula.time.begining"/>:</th>
        	<td>
	          	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaInicio" property="horaInicio"  size="2"/> :
    	        <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosInicio" property="minutosInicio" size="2"/>
	     	</td>
   		</tr>
	    <tr>
	        <th><bean:message key="property.aula.time.end"/>:</th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.horaFim" property="horaFim"  size="2"/>
            		:
	            <html:text bundle="HTMLALT_RESOURCES" altKey="text.minutosFim" property="minutosFim"  size="2"/>
    	    </td> 
	 	</tr> 
	 	<tr>
	        <th><bean:message key="property.aula.time.quinzenal"/>:</th>
    	    <td>
        	 	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.quinzenal" property="quinzenal"/>
    	    </td> 
	 	</tr>	 	
	 	<tr>
	        <th><bean:message key="property.lesson.new.begin.date"/>:</th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newBeginDate" size="10" maxlength="10"
        	 			onchange="<%= "changeStartDate('" + firstPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00');" %>"/>        	 	
        	 	<b><bean:message key="label.until"/></b>        	 	
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newEndDate" size="10" maxlength="10"
        	 			onchange="<%= "changeEndDate('" + firstPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00', '" + lastPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00');" %>"/>
        	 	<bean:message key="property.lesson.new.begin.date.format"/>        	 	        	 
    	    </td> 
	 	</tr>
	 	<tr>
	        <th><bean:message key="property.lesson.new.begin.date.week"/>:</th>
    	    <td>
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newBeginDateWeek" size="3" maxlength="2" value="1"
        	 			onchange="<%= "changeStartWeek('" + firstPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00');" %>"/>        	 	
        	 	<b><bean:message key="label.until"/></b>        	 	
        	 	<html:text bundle="HTMLALT_RESOURCES" altKey="text.week" property="newEndDateWeek" size="3" maxlength="2" value="<%= "" + (Weeks.weeksBetween(firstPossibleLessonDay, lastPossibleLessonDay).getWeeks() + 1) %>"
        	 			onchange="<%= "changeEndWeek('" + firstPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00', '" + lastPossibleLessonDay.toString("yyyy-MM-dd") + " 00:00:00');" %>"/>
    	    </td> 
	 	</tr>
	 	<logic:present name="action">
		 	<logic:equal name="action" value="edit">			 	 	
			 	<tr>
			 		<th><bean:message key="property.lesson.create.previous.instances"/>:</th>
		    	    <td>		    	    	
						<b><bean:write name="lesson_" property="nextPossibleLessonInstanceDate" /></b> <bean:message key="label.until"/> <b><bean:message key="label.new.specified.beginDate"/></b>					
						<p class="mtop05">
							<bean:message key="option.manager.true"/> <html:radio property="createLessonInstances" value="<%= Boolean.TRUE.toString() %>" disabled="false" />
							<bean:message key="option.manager.false"/> <html:radio property="createLessonInstances" value="<%= Boolean.FALSE.toString() %>" disabled="false"/>						
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.week" property="createLessonInstances"/>								
						</p>										    	    	    	    	
		    	    </td>
			 	</tr>		 
		 	</logic:equal>
	 	</logic:present>
	 	<tr>
	        <th><bean:message key="property.lesson.period.dates"/>:</th>
    	    <td>
        	 	<b><bean:write name="executionDegreeLessonsStartDate"/> - <bean:write name="executionDegreeLessonsEndDate"/></b>
    	    </td> 
	 	</tr>	 		 	
	</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="lable.chooseRoom"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>
	</p>
</html:form>

<jsp:include page="shiftLessonList.jsp"/>
