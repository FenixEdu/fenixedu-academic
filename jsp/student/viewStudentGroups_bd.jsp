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



<logic:present name="infoSiteGroupsByShift">


<html:form action="/viewStudentGroups" method="get">



<table width="100%" cellpadding="2" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.student.viewStudentGroups.description" />
		</td>
	</tr>
</table>

<br>
	

	<h2><span class="error"><html:errors/></span></h2>
	
	
<table width="95%" border="0" style="text-align: left;">	
	<tbody>
	<tr>
	<td>
	
	<table width="100%" cellpadding="2" border="0" style="text-align: left;" > 		
																
				<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>
				<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>
							
					<tr >
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.turno"/>
						</td>
						<td class="listClasses-header" colspan="4" width="60%">
							<bean:message key="property.lessons"/>
						</td>
						<td class="listClasses-header" width="20%" rowspan="2">
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
				 		
				 	<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>
				 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
				 		<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
				 			 		
				 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>
				 			
				 			
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
	<br>
	
	

	<logic:empty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available"/></h2>             
	</logic:empty>
  
	
  	<logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
  		
  		<table width="100%" cellpadding="2" border="0" style="text-align: left;">
       		<tbody>
	
            	<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
        		<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
        		
        		<tr>
        		<td>
        			          			
          			<br>
             		<td class="listClasses">
               		
						     			
               				<html:link page="<%="/viewStudentGroupInformation.do?method=execute&executionCourseCode=" + request.getParameter("executionCourseCode")+"&groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
								<b><bean:message key="label.groupWord"/>
                				<bean:write name="infoStudentGroup" property="groupNumber"/></b>
							</html:link>
						
	
					</td>
  
               		<td class="listClasses">
               	 	
    
            			<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&executionCourseCode=" + request.getParameter("executionCourseCode")+"&groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               			<b><bean:message key="link.enrolment"/></b>
               			</html:link>
                	
               		 
            		</td>
            	
               		<td class="listClasses">
               		
     
                   		<html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&executionCourseCode=" + request.getParameter("executionCourseCode")+"&groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.removeEnrolment"/></b>
               			</html:link>
               		
               			
            	   	</td>
            	   	
            	              	   	
            	   	<td class="listClasses">
    
                   		<html:link page="<%="/editStudentGroupShift.do?method=prepareEdit&executionCourseCode=" + request.getParameter("executionCourseCode")+"&groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editStudentGroupShift"/></b>
               			</html:link>
               		
            	   	</td>
            	   	
            	   	
            	   	<bean:define id="nrOfElements" name="infoSiteStudentGroup" property="nrOfElements"/>
				 		<td  class="listClasses">
				 			 		
				 		<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfElements"/>
				 			
				 			
						</td>
          
            	 	  	   	
            	</td>
                </tr>
				</logic:iterate> 
            	
            	</tbody>
			</table>
		
           	
   </tr>
   </td>  
	<tr><td><br/>

	
	<html:submit styleClass="inputbutton"><bean:message key="button.refresh"/>                    		         	
	</html:submit>
	
	<html:hidden property="method" value="execute"/>
	<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
	<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>	
	
</logic:notEmpty>



	</tr>
    </td>
	</tbody>
	

		
</table>
 </html:form>    
 
 <html:form action="/viewProjectShifts" method="get">
 	<html:submit styleClass="inputbutton"><bean:message key="button.back"/>                    		         	
	</html:submit>
	
	<html:hidden property="method" value="execute"/>
	<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
  </html:form>  

</logic:present>

<logic:notPresent name="infoSiteGroupsByShift">
<h2>
<bean:message key="message.infoSiteGroupsByShiftList.not.available"/>
</h2>             
</logic:notPresent>

