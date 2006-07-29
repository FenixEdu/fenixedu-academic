<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.lang.String" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>

<logic:present name="infoSiteShiftsAndGroups">


	<logic:empty name="infoSiteShiftsAndGroups" property="infoSiteGroupsByShiftList">
		<html:link page="<%="/viewExecutionCourseProjects.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode")%>">
			<bean:message key="link.backToProjects"/>
		</html:link>
		<br />
		<h2><bean:message key="message.shifts.not.available" /></h2>
	</logic:empty>
	
	
	<logic:notEmpty name="infoSiteShiftsAndGroups" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="title.ShiftsAndGroups"/>: <span class="infoop4"><bean:write name="infoSiteShiftsAndGroups" property="infoGrouping.name"/></span></h2>
	
<%--
	<strong>Disciplinas:</strong> 
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" length="1">
		<bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" offset="1">
		, <bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
--%>
	
	<span class="error"><!-- Error messages go here --><html:errors /></span>

	<ul>
	<li><html:link page="<%="/viewExecutionCourseProjects.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode")%>"><bean:message key="link.backToProjects"/></html:link> - <bean:message key="link.backToProjects.description"/></li>
	<li><html:link page="<%="/viewAllStudentsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>"><bean:message key="link.viewAllStudentsAndGroups"/></html:link> - <bean:message key="link.viewAllStudentsAndGroups.description"/></li>
	</ul>
	
	<br/>
	
<%-- ASD : nome do agrupamento --%>
<%-- ASD : nome das disciplinas --%>

<%--
	<p>
	<strong>Agrupamento:</strong> <bean:write name="infoSiteShiftsAndGroups" property="infoGrouping.name"/>
	</p>
--%>
	
  <table class="style1" cellspacing='1' cellpadding='1'>	
	<tbody>		
	
		<tr >
			<th class="listClasses-header" width="15%" rowspan="2">
				<bean:message key="property.turno"/>
			</th>
			<th class="listClasses-header" colspan="4" width="45%"> 
				<bean:message key="property.lessons"/>
			</th>
			<th class="listClasses-header" width="40%" rowspan="2" colspan="3">
				<bean:message key="property.groups"/>
			</th>
		</tr>
		<tr>
			<th class="listClasses-header" width="15%">
				<bean:message key="property.lesson.weekDay"/>
			</th>
			<th class="listClasses-header" width="10%">
				<bean:message key="property.lesson.beginning"/>
			</th>
			<th class="listClasses-header" width="10%">
				<bean:message key="property.lesson.end"/>
			</th>
			<th class="listClasses-header" width="10%">
				<bean:message key="property.lesson.room"/>
			</th>
		</tr>
		
	 <logic:iterate id="infoSiteGroupsByShift" name="infoSiteShiftsAndGroups" property="infoSiteGroupsByShiftList" >
	 
	<logic:empty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
			<tr>
								
								<td  class="listClasses">
								<html:link page="<%="/viewStudentsAndGroupsWithoutShift.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
									Sem Turno
								</html:link>	
								</td>
								
								<td class="listClasses">
								---
								</td>
								
								<td class="listClasses">
								---	
								</td>
								
								<td class="listClasses">
								---	
								</td>
								
								
				               	<td class="listClasses">
								---	
						 		</td>
						 		<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td class="listClasses">
						 			<b><bean:message key="label.nrOfGroups"/></b><bean:write name="nrOfGroups"/>
						 			
						 		</td>
								
								<td class="listClasses">
							 		<logic:equal name="nrOfGroups" value="0">
								 		<bean:message key="link.insertGroup"/>										
									</logic:equal>	
							 		<logic:notEqual name="nrOfGroups" value="0">
								 		<html:link page="<%="/groupEnrolment.do?method=prepareEnrolment&executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
									       <bean:message key="link.insertGroup"/>
								        </html:link>	
									</logic:notEqual>									        	   
								</td>					
			
			
								 <td class="listClasses">
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
		                        <html:link page="<%="/viewStudentGroupInformation.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
		               					<bean:write name="infoStudentGroup" property="groupNumber"/>
									</html:link>
								</logic:iterate>]
								</logic:notEmpty>
								
								<logic:empty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
									<bean:message key="message.shift.without.groups"/>
								</logic:empty>
								
								 </td>
							
						 						
						 	</tr>
			</logic:empty>		
	
		<logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
			<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>	
			<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>	
			<bean:define id="shiftCode" name="infoShift" property="idInternal"/>	
						
	 		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
					<tr>
						
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<html:link page="<%="/viewStudentsAndGroupsByShift.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
								<bean:write name="infoShift" property="nome"/>
							</html:link>	
						</td>
						
						<td class="listClasses">
							<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
						</td>
						<td class="listClasses">
							<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
						</td>
						<td class="listClasses">
							<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
						</td>
							
		               	<td class="listClasses">
		               		<logic:notEmpty name="infoLesson" property="infoSala.nome">
								<bean:write name="infoLesson" property="infoSala.nome"/>
							</logic:notEmpty>	
				 		</td>
				 		
				 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
				 		<td class="listClasses" width="10%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">				 			 		
				 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>				 							 			
						</td>
				 		
				 		<td class="listClasses" width="13%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
					 		<logic:equal name="nrOfGroups" value="0">
								 		<bean:message key="link.insertGroup"/>										
							</logic:equal>	
							<logic:notEqual name="nrOfGroups" value="0">
						 		<html:link page="<%="/groupEnrolment.do?method=prepareEnrolment&executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
									<bean:message key="link.insertGroup"/>
								</html:link>	   
							</logic:notEqual>	
						</td>						 		
						
						<td class="listClasses" width="20%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
							<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
                        	<html:link page="<%="/viewStudentGroupInformation.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               					<bean:write name="infoStudentGroup" property="groupNumber"/>
							</html:link>
						</logic:iterate>]
						</logic:notEmpty>
						
						<logic:empty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
							<bean:message key="message.shift.without.groups"/>
						</logic:empty>
						
						 </td>
					
				 						
				 	</tr>
				</logic:iterate>
				
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" offset="1">
                       <% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                       <% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                       <% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
						<tr >
							<td class="listClasses">
								<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
							</td>
							<td class="listClasses">
								<%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
							</td>
							<td class="listClasses">
								<logic:notEmpty name="infoLesson" property="infoSala.nome">
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</logic:notEmpty>	
							</td>
						</tr>
					</logic:iterate>
            
 			</logic:notEmpty>          
      
          </logic:iterate>
        </tbody>
	</table>
	
	<br />
	
	<div class="infoop">
	<ul>
	<li><bean:message key="label.student.viewShiftsAndGroups.description.item1" /></li>
	<li><bean:message key="label.student.viewShiftsAndGroups.description.item2" /></li>
	<li><bean:message key="label.student.viewShiftsAndGroups.description.item3" /></li>
	<li><bean:message key="label.student.viewShiftsAndGroups.description.item4" /></li>
	</ul>
	</div>
	
</logic:notEmpty>	

</logic:present>

<logic:notPresent name="infoSiteShiftsAndGroups">
<h2>
<bean:message key="message.shifts.not.available" />
</h2>
</logic:notPresent>













