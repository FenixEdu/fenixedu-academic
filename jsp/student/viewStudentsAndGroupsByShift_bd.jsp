<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

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

<span class="error"><!-- Error messages go here --><html:errors /></span>

	<p><strong>Agrupamento:</strong> <span class="infoop4"><bean:write name="infoSiteStudentsAndGroups" property="infoGrouping.name"/></span></p>

<%-- ASD : mostrar disciplinas
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

<table class="style1" width="75%" cellpadding="0" border="0">	
	<tbody>		
	
		<tr >
			<th class="listClasses-header" width="30%" rowspan="2">
				<bean:message key="property.turno"/>
			</th>
			<th class="listClasses-header" colspan="4" width="70%"> 
				<bean:message key="property.lessons"/>
			</th>
		</tr>
		<tr>
			<th class="listClasses-header" width="25%">
				<bean:message key="property.lesson.weekDay"/>
			</th>
			<th class="listClasses-header" width="15%">
				<bean:message key="property.lesson.beginning"/>
			</th>
			<th class="listClasses-header" width="15%">
				<bean:message key="property.lesson.end"/>
			</th>
			<th class="listClasses-header" width="15%">
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
						
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<bean:write name="infoShift" property="nome"/>
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
        </tbody>
	</table>

<span class="error"><!-- Error messages go here --><html:errors /></span>

	<p>
		<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
   	</p>


	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
				 			 		
 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/> <bean:message key="label.student.NumberOfStudentsInShift" /> <%= count %>
	<br/><br/>

<table class="style1" width="75%" cellpadding="0" border="0">
	<tbody>
	
	<tr>
		<th class="listClasses-header" width="10%"><bean:message key="label.studentGroupNumber" />
		</th>
		<th class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</th>
		<th class="listClasses-header" width="53%"><bean:message key="label.nameWord" />
		</th>
		<th class="listClasses-header" width="26%"><bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
 		<bean:define id="username" name="UserView" property="utilizador" type="java.lang.String"/>
		<logic:equal name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr class="highlight">
				<td class="listClasses">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:equal>
		<logic:notEqual name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr>
				<td class="listClasses">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:notEqual>
	 </logic:iterate>

</tbody>
</table>

<br/>

  </logic:notEmpty>

</td>
  </tr>
 </tbody>
	
</table>
	 
</logic:present>
