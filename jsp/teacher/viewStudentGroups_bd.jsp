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
			<bean:message key="label.teacher.viewStudentGroups.description" />
		</td>
	</tr>
</table>
<br>
	
<span class="error"><html:errors/></span> 

<br>	
<br>
<table width="95%" border="0" style="text-align: left;">
	<tbody>

     <tr>
     	<td>
       
        	<table width="100%" cellpadding="0" border="0" style="text-align: left;" > 		
																
				<bean:define id="infoSiteShift" name="component" property="infoSiteShift"/>
				<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>
					
				<tr >
						<td class="listClasses-header" width="20%" rowspan="2">
							<bean:message key="property.shift"/>
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
				 		
				 		<bean:define id="infoSiteShift" name="component" property="infoSiteShift"/>
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
    <logic:empty name="component" property="infoSiteStudentGroupsList">
	<h2><bean:message key="message.studentGroups.not.available" /></h2>
	</logic:empty>
	
	 
    <logic:notEmpty name="component" property="infoSiteStudentGroupsList">
	        <br> 
			<table width="100%" cellpadding="2" border="0" style="text-align: left;">
          	<tbody>
            	<logic:iterate id="infoSiteStudentGroup" name="component" property="infoSiteStudentGroupsList" >
            	<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
            	<bean:define id="infoGroupProperties" name="infoStudentGroup" property="infoGroupProperties"/>
               	<bean:define id="idInternal" name="infoGroupProperties" property="idInternal"/>
        		<tr>
          		<td>
             		<br>
             		<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
             		<td class="listClasses">
               		<b><html:link page="<%= "/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>
						</html:link></b>
					</td>
               		
	           		
               		
					
               		
               		<td class="listClasses">
               	
               			<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editGroupMembers"/></b></html:link>
                	</td>
               	
               		<td class="listClasses">
               	
                    	<html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editGroupShift"/></b></html:link>
                	</td>
                
                	<td class="listClasses">
               	
                		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + idInternal.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.deleteGroup"/></b></html:link>
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

           
     
       
<br>

	
</logic:notEmpty>

 	</td>
    </tr>
    </tbody>	
	</table>

	
	
	
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.studentGroups.not.available" />
</h2>
</logic:notPresent>