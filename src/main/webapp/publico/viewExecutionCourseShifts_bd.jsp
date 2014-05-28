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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:present name="siteView" property="component" >
	<bean:define id="component" name="siteView" property="component"/>

	<logic:empty name="component" property="shifts" >
		<h2><bean:message key="message.shifts.not.available" /></h2>
	</logic:empty>
	
	<logic:notEmpty name="component" property="shifts" >
	<h2><bean:message key="label.shifts" /></h2>
	<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
		<tr>
			<th rowspan="2">
				<bean:message key="property.turno"/>
			</th>
			<th colspan="4">
				<bean:message key="label.lesson"/>
			</th>
			<th rowspan="2">
				<bean:message key="label.classOrClasses"/>
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="label.day"/>
			</th>
			<th>
				<bean:message key="property.lesson.beginning"/>
			</th>
			<th>
				<bean:message key="property.lesson.end"/>
			</th>
			<th>
				<bean:message key="property.lesson.room"/>
			</th>
		</tr>
			
		<logic:iterate id="infoShift" name="component" property="shifts"  indexId="infoShiftIndex">
			<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
				<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                 <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                 <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                 <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
				<tr>
					<td rowspan="<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>">
						<bean:write name="infoShift" property="infoShift.nome"/>
					</td>
					<td >
						<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
					</td>
					<td>
						<%= iH.toString()%>:<%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
					</td>
					<td>
						<%= fH.toString()%>:<%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
					</td>
					<td>
					    <logic:notEmpty name="infoLesson" property="infoSala">
							<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;objectCode=<bean:write name="executionPeriodCode" />&amp;executionPeriodOID=<%= request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID).toString() %>'>
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</a>
						</logic:notEmpty>
					</td>
					<td rowspan=<%=((InfoShiftWithAssociatedInfoClassesAndInfoLessons) infoShift).getInfoLessons().size() %>>
						<logic:iterate id="infoClass" name="infoShift" property="infoClasses">
							<bean:define id="classId" name="infoClass" property="externalId" toScope="request"/>
							<bean:define id="className" name="infoClass" property="nome" toScope="request"/>
							<a href="viewClassTimeTableWithClassNameAndDegreeInitialsAction.do?classId=<%= request.getAttribute("classId").toString() %>&amp;className=<%= request.getAttribute("className").toString() %>">
								<bean:write name="infoClass" property="nome" /><br/>
							</a>
						</logic:iterate>
					</td>
				</tr>
			</logic:iterate>		

			<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
				<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                 <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                 <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                 <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
				<tr>
					<td>
						<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
					</td>
					<td>
						<%= iH.toString()%>:<%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
					</td>
					<td>
						<%= fH.toString()%>:<%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
					</td>
					<td>
					    <logic:notEmpty name="infoLesson" property="infoSala">
							<a href='siteViewer.do?method=roomViewer&amp;roomName=<bean:write name="infoLesson" property="infoSala.nome"/>&amp;objectCode=<bean:write name="executionPeriodCode" />&amp;executionPeriodOID=<%= request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID).toString() %>'>
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</a>
						</logic:notEmpty>	
					</td>
				</tr>
			</logic:iterate>

		</logic:iterate>
	</table>	
	</logic:notEmpty>	
</logic:present>
	
<logic:notPresent name="siteView" property="component" >
	<bean:message key="message.public.notfound.infoShifts"/>
</logic:notPresent>		



