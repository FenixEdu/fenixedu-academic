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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.summaries"/></h2>

<bean:define id="executionCourseID" name="executionCourse" property="externalId"/>
<%
    if (request.getParameter("ommitFilter") == null) {
%>
<html:form action="/searchSummaries">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="summaries"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="executionCourseID" value="<%= executionCourseID.toString() %>"/>

	<table class="mtop1" cellspacing="2" cellpadding="0">
		<tr>
			<td><bean:message key="label.summary.shift.type" />:</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="shiftType" onchange="this.form.submit();">
					<html:option value="" key="label.showBy.all"/>
					<logic:iterate id="shiftType" name="summariesSearchBean" property="shiftTypes">
						<html:option value="<%= shiftType.toString() %>" key="<%= shiftType.toString() %>" bundle="ENUMERATION_RESOURCES"/>
					</logic:iterate>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.shift" />:</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="shiftID" onchange="this.form.submit();">
					<html:option value="" key="label.showBy.all"/>
					<logic:iterate id="shift" name="executionCourse" property="shiftsOrderedByLessons">
						<bean:define id="shiftID" name="shift" property="externalId"/>
						<html:option value="<%= shiftID.toString() %>">
							<logic:iterate id="lesson" type="net.sourceforge.fenixedu.domain.Lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" length="1">
								<bean:write name="lesson" property="diaSemana"/>
								(<%= lesson.getBeginHourMinuteSecond().toString("HH:mm") %>
								-<%= lesson.getEndHourMinuteSecond().toString("HH:mm") %>)
								<logic:notEmpty name="lesson" property="roomOccupation">
									<bean:write name="lesson" property="roomOccupation.room.name"/>
								</logic:notEmpty>
							</logic:iterate>
							<logic:iterate id="lesson" type="net.sourceforge.fenixedu.domain.Lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" offset="1">
								,
								(<%= lesson.getBeginHourMinuteSecond().toString("HH:mm") %>
								-<%= lesson.getEndHourMinuteSecond().toString("HH:mm") %>)
								<logic:notEmpty name="lesson" property="roomOccupation">
									<bean:write name="lesson" property="roomOccupation.room.name"/>
								</logic:notEmpty>
							</logic:iterate>
						</html:option>
					</logic:iterate>
				</html:select>		
				<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.teacher" />:</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="professorshipID" onchange="this.form.submit();">
					<html:option value="0" key="label.showBy.all" />
					<logic:iterate id="professorship" name="executionCourse" property="professorshipsSortedAlphabetically">
						<bean:define id="professorshipID" name="professorship" property="externalId"/>
						<html:option value="<%= professorshipID.toString() %>">
							<bean:write name="professorship" property="person.name"/>
						</html:option>
					</logic:iterate>
					<html:option  value="-1" key="label.others" />
				</html:select>			
			</td>
		</tr>
		<tr>
			<td><bean:message key="label.order"/>:</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="order" onchange="this.form.submit();">
					<html:option  value="descendant" key="label.descendant" />
					<html:option value="ascendant" key="label.ascendant" />
				</html:select>			
				<html:submit styleId="javascriptButtonID3" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>	
</html:form>
<% } %>

<bean:define id="summaries" name="summariesSearchBean" property="summaries"/>
<logic:empty name="summaries">
	<p>
		<em><bean:message key="message.summaries.not.available" /></em>
	</p>
</logic:empty>

<logic:notEmpty name="summaries">
	<logic:iterate id="summary" name="summaries" type="net.sourceforge.fenixedu.domain.Summary">	
		<bean:define id="summaryIdDiv" type="java.lang.String">s<bean:write name ="summary" property="externalId" /></bean:define>
		<div id="<%= summaryIdDiv %>" class="public_summary">
			<logic:present name="summary" property="shift">
				<p class="mtop2 mbottom0">
					<%= summary.getSummaryDateYearMonthDay().toString("dd/MM/yyyy") %>
					<%= summary.getSummaryHourHourMinuteSecond().toString("HH:mm") %>
				   	
					<logic:present name="summary" property="room">
						<logic:notEmpty name="summary" property="room">
							(<bean:message key="label.room" /> <bean:write name="summary" property="room.name" />)
			       		</logic:notEmpty>
			       	</logic:present>

					<span class="greytxt">
						<logic:empty name="summary" property="isExtraLesson">
							<bean:message key="label.summary.lesson" />
							<bean:message name="summary" property="summaryType.name" bundle="ENUMERATION_RESOURCES"/>
						</logic:empty>
						<logic:notEmpty name="summary" property="isExtraLesson">
				       		<logic:equal name="summary" property="isExtraLesson" value="false">						       		
								<bean:message key="label.summary.lesson" />
								<bean:message name="summary" property="summaryType.name" bundle="ENUMERATION_RESOURCES"/>
				       		</logic:equal>
				       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
								<bean:message key="label.extra.lesson" />
				       		</logic:equal>
				       	</logic:notEmpty>				       	
					</span>     	
				</p>
			</logic:present>

			<logic:present name="summary" property="title">	
				<logic:notEmpty name="summary" property="title">	
					<logic:equal name="summary" property="taught" value="true">
						<h3 class="mvert05"><bean:write name="summary" property="title"/></h3>
					</logic:equal>
					<logic:equal name="summary" property="taught" value="false">
						<h3 class="mvert05"><bean:message key="label.lesson.notTaught" /></h3>
					</logic:equal>	
				</logic:notEmpty>
			</logic:present>
			
			<div class="summary_content">
				<bean:write name="summary" property="summaryText" filter="false"/>
			</div>
			
			<div class="details mtop025">
				<span class="updated-date">
					<bean:message key="message.modifiedOn" />
					<%= summary.getLastModifiedDateDateTime().toString("dd/MM/yyyy HH:mm") %>
				</span>
		
				<logic:notEmpty name="summary" property="professorship">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />				
						<bean:write name="summary" property="professorship.person.name" />	
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="teacher">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />
						<bean:write name="summary" property="teacher.person.name" />
					</span>
				</logic:notEmpty>
		
				<logic:notEmpty name="summary" property="teacherName">
					<span class="author">
						<bean:write name="summary" property="teacherName" />
					</span>
				</logic:notEmpty>				
		
				<logic:present name="summary" property="studentsNumber">
					<span class="comment">
						<bean:message key="message.presences" />			
						<logic:notEmpty name="summary" property="studentsNumber">			
							<bean:define id="studentsAttended" name="summary" property="studentsNumber" />
							
							<logic:greaterEqual name="studentsAttended" value="0">
								<bean:message key="message.students" arg0="<%= studentsAttended.toString() %>"/>
							</logic:greaterEqual>
				
							<logic:lessThan name="studentsAttended" value="0">
								<i><bean:message key="message.notSpecified" /></i>				
							</logic:lessThan>
						</logic:notEmpty>
			
						<logic:empty name="summary" property="studentsNumber">			
							<i><bean:message key="message.notSpecified" /></i>								
						</logic:empty>
					</span>
				</logic:present>				
		
				<logic:notPresent name="summary" property="studentsNumber">			
					<span class="comment">
						<bean:message key="message.presences" />
						<i><bean:message key="message.notSpecified" /></i>								
					</span>
				</logic:notPresent>
			</div>

		</div>
	</logic:iterate>
</logic:notEmpty>
