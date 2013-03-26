<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

	
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="idInternal"/>
	<bean:define id="infoAttends" name="infoStudentGroup" property="infoAttends"/>
	<bean:define id="infoGrouping" name="infoStudentGroup" property="infoGrouping"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="idInternal"/>
	
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
	<bean:define id="shiftCode" name="infoShift" property="idInternal"/>

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
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>	
						
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
		<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
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
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link>
	 	</logic:lessEqual>
	 	
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:greaterEqual> 
	 
	 
	   <logic:equal name="ShiftType" value="1">
	    <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
	    
	    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/></html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/></html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/></html:link> | 
		</logic:equal>
		
		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:lessEqual>
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/></html:link>
		</logic:greaterEqual> 
	</p>
	
	
	</logic:empty> 
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	<ul class="mbottom05">
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
		    	<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>
	</ul>
		
	<bean:define id="nrOfElements" name="component" property="nrOfElements"/>
				 			 		
	<bean:define id="sendMailLink" type="java.lang.String">/sendMailToWorkGroupStudents.do?method=sendEmail&amp;objectCode=<%= pageContext.findAttribute("objectCode").toString() %>&amp;groupPropertiesCode=<%= groupPropertiesCode.toString() %>&amp;studentGroupCode=<%= studentGroupCode.toString() %></bean:define>
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
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
	
		<logic:present name="showPhotos">
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.viewPhoto"/>
			</html:link>
		</logic:present>
		<logic:notPresent name="showPhotos">
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.notViewPhoto"/>
			</html:link>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="infoStudentGroup" property="infoShift">
		<logic:present name="showPhotos">
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
		    		<bean:message key="label.viewPhoto"/>
			</html:link>
		</logic:present>
		<logic:notPresent name="showPhotos">
			<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
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
					<bean:define id="personID" name="person" property="idInternal"/>
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
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:lessEqual>
	 	
	 	<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupMembers"/>
	    </html:link> | 
	 	</logic:greaterEqual> 
	 	
	    <logic:equal name="ShiftType" value="1">
	     <bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		 <bean:define id="shiftCode" name="infoShift" property="idInternal"/>
	    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.editGroupShift"/>
	    </html:link> | 
    	</logic:equal>
	    	
	    <logic:equal name="ShiftType" value="3">
	    <html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.enrollStudentGroupInShift"/>
	    </html:link> | 
		</logic:equal>
		
		<logic:equal name="ShiftType" value="2">
	    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.unEnrollStudentGroupShift"/>
	    </html:link> | 
		</logic:equal>

		<logic:lessEqual name="ShiftType" value="2">
		<bean:define id="infoShift" name="infoStudentGroup" property="infoShift"/>	
		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/>
	    </html:link>
		</logic:lessEqual>
		
		<logic:greaterEqual name="ShiftType" value="3">
		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
	    	<bean:message key="link.deleteGroup"/>
	    </html:link>
		</logic:greaterEqual> 
	</p>
  
 	
</logic:notEmpty>
	 
</logic:present>

<logic:notPresent name="siteView" property="component">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:notPresent>
