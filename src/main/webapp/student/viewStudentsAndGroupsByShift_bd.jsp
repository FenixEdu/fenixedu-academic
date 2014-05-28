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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>

<logic:present name="infoSiteStudentsAndGroups">

	<h2><bean:message key="title.viewStudentsAndGroupsByShift"/></h2>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<!-- <div class="infoop"><bean:message key="label.student.viewStudentsAndGroupsByShift.description" /></div> -->
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<!-- <div class="infoop"><bean:message key="label.student.viewStudentsAndGroupsByShift.description" /></div> -->
	</logic:notEmpty>		

	<p>
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

	<p class="mtop15 mbottom1"><strong>Agrupamento:</strong> <span class="infoop4"><bean:write name="infoSiteStudentsAndGroups" property="infoGrouping.name"/></span></p>

<%--
	ASD : mostrar disciplinas
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" length="1">
		<bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" offset="1">
		, <bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
	<br/>
--%>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<p><span class="infoop4"><strong><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></strong></span></p>
 	</logic:empty> 

<table class="tstyle4 tdnowrap mtop05">	
		<tr>
			<th rowspan="2">
				<bean:message key="property.turno"/>
			</th>
			<th colspan="4"> 
				<bean:message key="property.lessons"/>
			</th>
		</tr>
		<tr>
			<th>
				<bean:message key="property.lesson.weekDay"/>
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
		
	 	<bean:define id="infoShift" name="infoSiteStudentsAndGroups" property="infoShift"/>
			<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr>
						
						<td rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<bean:write name="infoShift" property="nome"/>
						</td>
						<td>
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
						</td>
						<td>
							<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
						</td>
						<td>
							<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
						</td>
							
		               	<td>
		               		<logic:notEmpty name="infoLesson" property="infoSala">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</logic:notEmpty>	
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
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td>
							   	<logic:notEmpty name="infoLesson" property="infoSala">
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</logic:notEmpty>	
							</td>
						</tr>
				</logic:iterate>
	</table>

	<p class="mvert0">
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</p>

	<p class="mtop05">
		<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
   			<bean:message key="link.backToShiftsAndGroups"/>
   		</html:link> - 
   		<bean:message key="link.backToShiftsAndGroups.description"/>
   	</p>
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
				 			 		
	<p class="mtop15 mbottom05">
	 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	 	<bean:message key="label.student.NumberOfStudentsInShift" /> <%= count %>
 	</p>


<table class="tstyle4 mtop05">
	<tr>
		<th><bean:message key="label.studentGroupNumber" />
		</th>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
 		<bean:define id="username" name="LOGGED_USER_ATTRIBUTE" property="username" type="java.lang.String"/>
		<logic:equal name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr class="highlight">
				<td>
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:equal>
		<logic:notEqual name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr>
				<td>
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td>
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:notEqual>
	 </logic:iterate>
</table>

</logic:notEmpty>


	 
</logic:present>
