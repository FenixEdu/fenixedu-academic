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
		<logic:equal name="type" value="true">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithLink.description" />
			</div>
		</logic:equal>
		<logic:equal name="type" value="false">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithoutLink.description" />
			</div>
		</logic:equal>
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<logic:equal name="type" value="true">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithLink.description" />
			</div>
		</logic:equal>
		<logic:equal name="type" value="false">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithoutLink.description" />
			</div>
		</logic:equal>
	</logic:notEmpty>		
	

<table class="tstyle4 tdcenter thlight">	
	<tbody>		
		<tr>
			<th width="30%" rowspan="2">
				<bean:message key="property.shift"/>
			</th>
			<th colspan="4" width="70%"> 
				<bean:message key="property.lessons"/>
			</th>
		</tr>
		<tr>
			<th width="25%">
				<bean:message key="property.lesson.weekDay"/>
			</th>
			<th width="15%">
				<bean:message key="property.lesson.beginning"/>
			</th>
			<th width="15%">
				<bean:message key="property.lesson.end"/>
			</th>
			<th width="15%">
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
						
						<td  rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
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
						<tr >
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

        </tbody>
    
	</table>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>



<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">

	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
				<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>
	</ul>
	
	<logic:equal name="type" value="true">
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupsShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>">
	   		<bean:message key="link.editStudentGroupsShift"/>
	   		</html:link>
   		</li>
   	</ul>
	</logic:equal>


		
	<p class="mtop2">
		<span class="warning0">
			<bean:message key="message.infoSiteStudentsAndGroupsList.not.available" />
		</span>
	</p>
 	
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			   	<bean:message key="link.backToShiftsAndGroups"/>
		   	</html:link>
	   	</li>
   	</ul>
    	
    	
	<logic:equal name="type" value="true">
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupsShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>">
		    	<bean:message key="link.editStudentGroupsShift"/>
	    	</html:link>
    	</li>
    </ul>
	</logic:equal>

	

 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>

	<p class="mtop2 mbottom05">
		<bean:message key="label.teacher.NumberOfStudentsInShift" /><%= count %>
	</p>

	<logic:present name="showPhotos">
		<html:link page="<%="/studentGroupManagement.do?method=viewStudentsAndGroupsByShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;shiftCode=" + request.getParameter("shiftCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/studentGroupManagement.do?method=viewStudentsAndGroupsByShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;shiftCode=" + request.getParameter("shiftCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>

	<table class="tstyle4 mtop05">
		<tr>
			<th>
				<bean:message key="label.numberWord" />
			</th>
			<th>
				<bean:message key="label.studentGroupNumber" />
			</th>
			<logic:notPresent name="showPhotos">
				<th>
					<bean:message key="label.photo" />
				</th>
			</logic:notPresent>
			<th>
				<bean:message key="label.nameWord" />
			</th>
			<th>
				<bean:message key="label.emailWord" />
			</th>
		</tr>
			
		<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
			<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
			<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
			<tr>	
				<td class="acenter">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td class="acenter">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
				<logic:notPresent name="showPhotos">
					<td class="acenter">
						<bean:define id="personID" name="infoSiteStudentInformation" property="personID"/>
						<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
					</td>
				</logic:notPresent>
				<td>
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>
				<td>
					<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>">
						<bean:write name="infoSiteStudentInformation" property="email"/>
					</html:link>
				</td>
			</tr>				
	 	</logic:iterate>
	</table>

</logic:notEmpty>

 
</logic:present>
