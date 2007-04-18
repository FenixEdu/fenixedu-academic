<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.summaries"/></h2>

<bean:define id="executionCourseID" name="executionCourse" property="idInternal"/>
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
						<bean:define id="shiftID" name="shift" property="idInternal"/>
						<html:option value="<%= shiftID.toString() %>">
							<logic:iterate id="lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" length="1">
								<bean:write name="lesson" property="diaSemana"/>
								(<dt:format pattern="HH:mm"><bean:write name="lesson" property="inicio.time.time"/></dt:format>
								-<dt:format pattern="HH:mm"><bean:write name="lesson" property="fim.time.time"/></dt:format>)
								<bean:write name="lesson" property="roomOccupation.room.nome"/>
							</logic:iterate>
							<logic:iterate id="lesson" name="shift" property="lessonsOrderedByWeekDayAndStartTime" offset="1">
								,
								(<dt:format pattern="HH:mm"><bean:write name="lesson" property="inicio.time.time"/></dt:format>
								-<dt:format pattern="HH:mm"><bean:write name="lesson" property="fim.time.time"/></dt:format>)
								<bean:write name="lesson" property="roomOccupation.room.nome"/>
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
						<bean:define id="professorshipID" name="professorship" property="idInternal"/>
						<html:option value="<%= professorshipID.toString() %>">
							<bean:write name="professorship" property="teacher.person.name"/>
						</html:option>
					</logic:iterate>
					<html:option  value="-1" key="label.others" />
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
	<logic:iterate id="summary" name="summaries" >	
		<bean:define id="summaryId" type="java.lang.Integer" name ="summary" property="idInternal" />
		<div id="<%= "s" + summaryId %>">
			<logic:present name="summary" property="shift">
				<p class="mtop2 mbottom0">
					<dt:format pattern="dd/MM/yyyy">
						<bean:write name="summary" property="summaryDate.time"/>
					</dt:format>
					<dt:format pattern="HH:mm">
						<bean:write name="summary" property="summaryHour.time"/>
					</dt:format>
				   	
					<logic:present name="summary" property="room">
						<logic:notEmpty name="summary" property="room">
							(<bean:message key="label.room" /> <bean:write name="summary" property="room.nome" />)
			       		</logic:notEmpty>
			       	</logic:present>


					<span class="greytxt">
						<logic:empty name="summary" property="isExtraLesson">
							<bean:message key="label.summary.lesson" />
						</logic:empty>
						<logic:notEmpty name="summary" property="isExtraLesson">
				       		<logic:equal name="summary" property="isExtraLesson" value="false">		
								<bean:message key="label.summary.lesson" />
				       		</logic:equal>
				       		<logic:equal name="summary" property="isExtraLesson" value="true">		     
								<bean:message key="label.extra.lesson" />
				       		</logic:equal>
				       	</logic:notEmpty>
				       	<bean:message name="summary" property="shift.tipo.name" bundle="ENUMERATION_RESOURCES"/>
					</span>     	
				</p>
			</logic:present>

			<%--		
			<logic:notPresent name="summary" property="infoShift">
				<bean:message key="label.summary.lesson" />
				&nbsp;
				<dt:format pattern="dd/MM/yyyy">
					<bean:write name="summary" property="summaryDate.time"/>
				</dt:format>
				&nbsp;
				<dt:format pattern="HH:mm">
					<bean:write name="summary" property="summaryHour.time"/>
				</dt:format>
			</logic:notPresent>
			--%>
					
			<logic:present name="summary" property="title">	
				<logic:notEmpty name="summary" property="title">		
					<h3 class="mvert05"><bean:write name="summary" property="title"/></h3>
				</logic:notEmpty>
			</logic:present>
			
			<p class="mvert0">
				<bean:write name="summary" property="summaryText" filter="false"/>
			</p>
			
			<div class="details mtop025">
				<span class="updated-date">
					<bean:message key="message.modifiedOn" />
					<dt:format pattern="dd/MM/yyyy HH:mm">
						<bean:write name="summary" property="lastModifiedDate.time"/>
					</dt:format>
				</span>
		
				<logic:notEmpty name="summary" property="professorship">
					<span class="author">
						<bean:message key="label.teacher.abbreviation" />				
						<bean:write name="summary" property="professorship.teacher.person.name" />	
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
