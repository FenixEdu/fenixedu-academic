<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.lang.String" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>
<%@ page import="java.util.Calendar" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />

<br>

<table width="100%" cellpadding="0" cellspacing="0">
	<tr>
		<td class="infoop">
			<bean:message key="label.teacher.viewProjectShifts.description" />
		</td>
	</tr>
</table>
<br>
		
<table width="95%" border="0" style="text-align: left;">
	<tbody>
	<b><html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
		<bean:message key="link.editGroupProperties"/></html:link></b>
	<br>
	<br>
	
	<logic:present name="noShifts">
	<h2><bean:message key="message.shifts.not.available"/>
	</h2>
	</logic:present>
	
	<logic:notPresent name="noShifts">
	
	
	<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
					<b><bean:message key="link.insertGroup"/></b></html:link>
    
  
	
	
    <logic:iterate id="infoSiteShift" name="component" property="infoSiteShifts" >
     <tr>
     	<td>
        <br>
        <h2>
        	<table width="100%" cellpadding="0" border="0" style="text-align: left;" > 		
																
				<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>
								
				<tr >
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.shift"/>
						</td>
						<td class="listClasses-header" colspan="4" width="55%"> 
							<bean:message key="property.lessons"/>
						</td>
						<td class="listClasses-header" width="25%" rowspan="2" colspan="2">
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
					 			
							<html:link page="<%="/viewStudentGroups.do?method=viewStudentGroups&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="shiftCode" paramName="infoShift" paramProperty="idInternal">
               				<b><bean:message key="link.groupsList"/></b>
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
           <span class="error"><html:errors/></span> 
        </tbody>
    

</logic:notPresent>	
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.shifts.not.available" />
</h2>
</logic:notPresent>