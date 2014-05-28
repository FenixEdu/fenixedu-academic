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
<%@page import="net.sourceforge.fenixedu.domain.CourseLoad"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>
<%@page import="net.sourceforge.fenixedu.domain.Shift"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>

<p class="mtop2 mbottom05"><strong>Aulas já atribuidas ao turno:</strong></p>

<%
	final Shift shift = ((InfoShift) request.getAttribute("shift")).getShift();
	if (shift.isTotalShiftLoadExceeded()) {
	    for (final CourseLoad courseLoad : shift.getCourseLoadsSet()) {
	        if (shift.getTotalHours().compareTo(courseLoad.getTotalQuantity()) == 1) {
%>
				<p class="error2">
					<bean:message key="error.Lesson.shift.load.total.quantity.exceeded" arg0="<%= shift.getTotalHours().toString() %>" arg1="<%= courseLoad.getTotalQuantity().toString() %>"/>
				</p>
<% } } } %>

<logic:present name="shift" property="infoLessons">
  <html:form action="/manageShiftMultipleItems">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteLessons"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

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

		<table class="tstyle4 thlight tdcenter">
			<tr>
				<th>
				</th>
				<th>
					<bean:message bundle="SOP_RESOURCES" key="label.lesson.week"/>
				</th>
				<th>
					<bean:message key="property.weekday"/>
				</th>
				<th>
					<bean:message key="property.time.start"/>
				</th>
		        <th>
		        	<bean:message key="property.time.end"/>
	    	    </th>
				<th>
					<bean:message key="property.room"/>
				</th>
				<th>
		        	<bean:message key="property.capacity"/>
		        </th>
				<th> </th>
				<th> </th>
				<th> </th>
		        <th> </th>
			</tr>
			<bean:define id="deleteConfirm">
				return confirm('<bean:message key="message.confirm.delete.lesson"/>')
			</bean:define>			
			<logic:iterate id="lesson" name="shift" property="infoLessons">
				<bean:define id="lessonOID" name="lesson" property="externalId"/>
				<tr>
              		<td>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
							<bean:write name="lesson" property="externalId"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="lesson" property="occurrenceWeeksAsString"/>
					</td>
					<td>
						<bean:write name="lesson" property="diaSemana"/>
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="inicio.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<dt:format pattern="HH:mm">
							<bean:write name="lesson" property="fim.timeInMillis"/>
						</dt:format>
					</td>
					<td>
						<logic:notEmpty name="lesson" property="infoSala">
							<bean:write name="lesson" property="infoSala.nome"/>
						</logic:notEmpty>	
					</td>
					<td>
						<logic:notEmpty name="lesson" property="infoSala">
							<bean:write name="lesson" property="infoSala.capacidadeNormal"/>
						</logic:notEmpty>
					</td>
					<td>
						<logic:empty name="lesson" property="lesson.lessonInstancesSet">
	               		<html:link page="<%= "/manageLesson.do?method=prepareEdit&amp;page=0&amp;"
    	           							+ PresentationConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ PresentationConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ PresentationConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ PresentationConstants.ACADEMIC_INTERVAL
  											+ "="
	  										+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  											+ "&amp;"
  											+ PresentationConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ PresentationConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:message key="link.edit"/>
						</html:link>
						</logic:empty>
					</td>
					<td>
	               		<html:link page="<%= "/manageLesson.do?method=prepareChangeRoom&amp;page=0&amp;"
    	           							+ PresentationConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ PresentationConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ PresentationConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ PresentationConstants.ACADEMIC_INTERVAL
  											+ "="
	  										+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  											+ "&amp;"
  											+ PresentationConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ PresentationConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:message key="link.change.room"/>
						</html:link>
					</td>
					<td>
						<html:link page="<%= "/manageLesson.do?method=deleteLesson&amp;page=0&amp;"
    	           							+ PresentationConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ PresentationConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ PresentationConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ PresentationConstants.ACADEMIC_INTERVAL
  											+ "="
	  										+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  											+ "&amp;"
  											+ PresentationConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ PresentationConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>"
  								onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
								<bean:message key="link.delete"/>
						</html:link>					
					</td>
					<td>
						<html:link page="<%= "/manageLesson.do?method=viewAllLessonDates&amp;page=0&amp;"
    	           							+ PresentationConstants.LESSON_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("lessonOID")
               					   			+ "&amp;"
    	           							+ PresentationConstants.SHIFT_OID
				  							+ "="
            	   				   			+ pageContext.findAttribute("shiftOID")
               					   			+ "&amp;"
			  								+ PresentationConstants.EXECUTION_COURSE_OID
  											+ "="
  											+ pageContext.findAttribute("executionCourseOID")
               				   				+ "&amp;"
			  								+ PresentationConstants.ACADEMIC_INTERVAL
  											+ "="
	  										+ pageContext.findAttribute(PresentationConstants.ACADEMIC_INTERVAL)
  											+ "&amp;"
  											+ PresentationConstants.CURRICULAR_YEAR_OID
				  							+ "="
  											+ pageContext.findAttribute("curricularYearOID")
  											+ "&amp;"
			  								+ PresentationConstants.EXECUTION_DEGREE_OID
  											+ "="
  											+ pageContext.findAttribute("executionDegreeOID") %>">
								<bean:message key="link.show.all.lesson.dates"/>
						</html:link>					
					</td>
				</tr>
			</logic:iterate>
		</table>
		<p>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick='<%= pageContext.findAttribute("deleteConfirm").toString() %>'>
				<bean:message key="link.delete"/>
			</html:submit>
		</p>
	  </html:form>
	</logic:present>

	<logic:notPresent name="shift" property="infoLessons">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:message key="message.shift.lessons.none"/>
			</span>
		</p>
	</logic:notPresent>