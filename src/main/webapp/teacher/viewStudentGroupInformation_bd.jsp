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

	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="externalId"/>
	<bean:define id="infoAttends" name="infoStudentGroup" property="infoAttends"/>
	<bean:define id="infoGrouping" name="infoStudentGroup" property="infoGrouping"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="externalId"/>
	
<logic:empty name="component" property="infoSiteStudentInformationList">
	<logic:lessEqual name="ShiftType" value="2">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyStudentGroupInformation.normalShift.description" />
		</div>
	</logic:lessEqual>	
	<logic:greaterEqual name="ShiftType" value="3">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyStudentGroupInformation.notNormalShift.description" />
		</div>
	</logic:greaterEqual>
</logic:empty>	

<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	<logic:lessEqual name="ShiftType" value="2">
		<div class="infoop2">
			<bean:message key="label.teacher.viewStudentGroupInformation.normalShift.description" />
		</div>
	</logic:lessEqual>
	<logic:greaterEqual name="ShiftType" value="3">
		<div class="infoop2">
			<bean:message key="label.teacher.viewStudentGroupInformation.notNormalShift.description" />
		</div>
	</logic:greaterEqual> 
</logic:notEmpty>


	<logic:lessEqual name="ShiftType" value="2">
	<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
	<bean:define id="shiftCode" name="infoShift" property="externalId"/>

	<table class="tstyle4 tdcenter">	
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
		
	 	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>	
						
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
	</table>

</logic:lessEqual>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>


<logic:empty name="component" property="infoSiteStudentInformationList">

<ul>
	<li>		
		<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
			<bean:message key="link.backToShiftsAndGroups"/>
		</html:link>
	</li>
</ul>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>

	<p> 		
		<b><bean:message key="label.nrOfElements"/></b> <bean:write name="nrOfElements"/>
	</p>

	<p>
		<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
	</p>
	
	<p>
		<bean:message key="label.groupManagement"/>&nbsp&nbsp
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>
	 	</logic:lessEqual>
	 	
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:greaterEqual> 
	 
	 
	   <logic:equal name="ShiftType" value="1">
	    <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
	    
	    <html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/></html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/studentGroupManagement.do?method=prepareEnrollStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/></html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/studentGroupManagement.do?method=unEnrollStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/></html:link> | 
		</logic:equal>
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
		<html:link page="<%= "/studentGroupManagement.do?method=deleteStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:lessEqual>
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%= "/studentGroupManagement.do?method=deleteStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")  + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:greaterEqual> 
	</p>
	
	
	</logic:empty> 
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	<ul class="mbottom05">
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
		    	<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>
	</ul>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>
				 			 		
	<bean:define id="sendMailLink" type="java.lang.String">/sendMailToWorkGroupStudents.do?method=sendEmail&amp;executionCourseID=<%= pageContext.findAttribute("executionCourseID").toString() %>&amp;groupPropertiesCode=<%= groupPropertiesCode.toString() %>&amp;studentGroupCode=<%= studentGroupCode.toString() %></bean:define>
	<ul class="mtop05">
		<li>
		   <html:link page="<%= sendMailLink %>">
				<bean:message key="link.sendEmailToAllStudents"/>
		   </html:link>
	   </li>
	</ul>

 	<p class="mtop15 mbottom1">
		<b><bean:message key="label.GroupNumber"/></b> <bean:write name="infoStudentGroup" property="groupNumber" /> <br/>
		<b><bean:message key="label.nrOfElements"/></b> <bean:write name="nrOfElements"/>
	</p>
	
	<logic:present name="infoStudentGroup" property="infoShift">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
	
		<logic:present name="showPhotos">
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.viewPhoto"/>
			</html:link>
		</logic:present>
		<logic:notPresent name="showPhotos">
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.notViewPhoto"/>
			</html:link>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="infoStudentGroup" property="infoShift">
		<logic:present name="showPhotos">
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.viewPhoto"/>
			</html:link>
		</logic:present>
		<logic:notPresent name="showPhotos">
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.notViewPhoto"/>
			</html:link>
		</logic:notPresent>
	</logic:notPresent>

	<table class="tstyle4 mtop05">
	<tr>
		<th>
			<bean:message key="label.numberWord" />
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
	
	<logic:iterate id="element" name="groupMembers">
		<bean:define id="person" name="element" property="registration.person" /> 
		<bean:define id="student" name="element" property="registration.student" /> 
		<tr>		
			<td class="acenter">
				<bean:write name="student" property="number"/>
			</td>
			<logic:notPresent name="showPhotos">
				<td class="acenter">
					<bean:define id="personID" name="person" property="externalId"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>
			<td>
				<bean:write name="person" property="name"/>
			</td>
			<td>
				<logic:present name="person" property="email">
					<bean:define id="mail" name="person" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>">
						<bean:write name="person" property="email"/>
					</html:link>
				</logic:present>
				<logic:notPresent name="person" property="email">
					&nbsp;
				</logic:notPresent>
			</td>
		</tr>	
	</logic:iterate>
	
	</table>

	<p>
		<b><bean:message key="label.groupManagement"/></b>&nbsp
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:lessEqual>
	 	
	 	<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:greaterEqual> 
	 	
	    <logic:equal name="ShiftType" value="1">
	     <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		 <bean:define id="shiftCode" name="infoShift" property="externalId"/>
	    <html:link page="<%="/studentGroupManagement.do?method=prepareEditStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/>
	    </html:link> | 
    	</logic:equal>
	    	
	    <logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/studentGroupManagement.do?method=prepareEnrollStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/>
	    </html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/studentGroupManagement.do?method=unEnrollStudentGroupShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/>
	    </html:link> | 
		</logic:equal>

		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="externalId"/>
		<html:link page="<%= "/studentGroupManagement.do?method=deleteStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/>
	    </html:link>
		</logic:lessEqual>
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%= "/studentGroupManagement.do?method=deleteStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")  + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/>
	    </html:link>
		</logic:greaterEqual> 
	</p>
  
 	
</logic:notEmpty>
	 