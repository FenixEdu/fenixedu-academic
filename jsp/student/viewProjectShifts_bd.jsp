<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.lang.Integer" %>
<%@ page import="java.lang.String" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>
<%@ page import="java.util.Calendar" %>
<%@ page import="DataBeans.InfoSiteGroupsByShift"%>




<logic:present name="infoSiteShifts">


<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.student.viewProjectShifts.description" />
		</td>
	</tr>
</table>
<br>

<h2><span class="error"><html:errors/></span></h2>

	
	<logic:empty name="infoSiteShifts">
		<h2><bean:message key="message.shifts.not.available" /></h2>
	</logic:empty>
	
	
	<logic:notEmpty name="infoSiteShifts">
	
	

	
	<table width="95%" border="0" style="text-align: left;">
	
		<tbody>
		
		<tr>
		<td>
		
	
		<html:link page="<%="/groupEnrolment.do?method=prepareEnrolment&executionCourseCode=" + request.getParameter("executionCourseCode")+ "&groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			<b><bean:message key="link.insertGroup"/></b>
		</html:link>	

		</td>
		</tr>	
		
		<logic:iterate id="infoSiteShift" name="infoSiteShifts">
     		<tr>
			<td>	
			<br>   
			<table width="100%" cellpadding="0" border="0" style="text-align: left;" > 		
																
				<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>
							
				<tr >
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.turno"/>
						</td>
						<td class="listClasses-header" colspan="4" width="60%">
							<bean:message key="property.lessons"/>
						</td>
						<td class="listClasses-header" width="20%" rowspan="2" colspan="2">
							<bean:message key="property.groups"/>
						</td>
					</tr>
					<tr>
						<td class="listClasses-header" width="20%">
							<bean:message key="property.lesson.weekDay"/>
						</td>
						<td class="listClasses-header" width="10%">
							<bean:message key="property.lesson.beginning"/>
						</td>
						<td class="listClasses-header" width="10%">
							<bean:message key="property.lesson.end"/>
						</td>
						<td class="listClasses-header" width="20%">
							<bean:message key="property.lesson.room"/>
						</td>
					</tr>	
					
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
					<tr>
						
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
							<b><bean:write name="infoShift" property="nome"/></b>
						</td>
						<td class="listClasses">
							<b><bean:write name="infoLesson" property="diaSemana"/></b> &nbsp;
						</td>
						<td class="listClasses">
							<b><%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %></b>
						</td>
						<td class="listClasses">
							<b><%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %></b>								
						</td>
							
		               	<td class="listClasses">
							<b><bean:write name="infoLesson" property="infoSala.nome"/></b>
				 		</td>
				 		
				 		
				 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
				 		<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
				 			 		
				 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>
				 			
				 			
						</td>
				 			
					
						<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
						
							
							<html:link page="<%="/viewStudentGroups.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode") + "&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="shiftCode" paramName="infoShift" paramProperty="idInternal">
               				<b><bean:message key="link.StudentGroupsList"/></b>
               				</html:link>
               				
							
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
								<b><bean:write name="infoLesson" property="diaSemana"/></b> &nbsp;
							</td>
							<td class="listClasses">
								<b><%= iH.toString()%> : <%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %></b>
							</td>
							<td class="listClasses">
								<b><%= fH.toString()%> : <%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %></b>
							</td>
							<td class="listClasses">
								<b><bean:write name="infoLesson" property="infoSala.nome"/></b>
							</td>
						</tr>
					</logic:iterate>
			</table>
		<br>
             
			
            </td>
            </tr>
            </logic:iterate>
            <br>
        </tbody>
</table>

<html:form action="/viewProjectShifts" method="get">	
	<html:submit styleClass="inputbutton"><bean:message key="button.refresh"/>                    		         	
	</html:submit>

	<html:hidden property="method" value="execute"/>
	<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
	
</html:form>
</logic:notEmpty>
	
	<html:form action="/viewExecutionCourseProjects" method="get">     
		<html:submit styleClass="inputbutton"><bean:message key="button.back"/>                    		         	
		</html:submit>
		<html:hidden property="method" value="execute"/>
		<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>

 	</html:form>





</logic:present>

<logic:notPresent name="infoSiteShifts">
<h2>
<bean:message key="message.shifts.not.available"/>
</h2>
</logic:notPresent>

