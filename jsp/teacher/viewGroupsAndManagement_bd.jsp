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
		
<table border="0" style="text-align: left;">
	<tbody>
	<b><html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
		<bean:message key="link.editGroupProperties"/></html:link></b>
	<br>
	<br>
	
	<logic:notPresent name="noShifts">
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
	<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
	
	<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
					<b><bean:message key="link.insertGroup"/></b></html:link>
    <logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
     <tr>
     	<td>
        <br>
        <h2>
        	<table width="500" cellpadding="0" border="6" style="text-align: left;" > 		
																
				<bean:define id="infoShift" name="infoSiteGroupsByShift" property="infoShift"/>
						
					
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
          
             
			<table width="500" cellpadding="0" border="0" style="text-align: left;">
          	<tbody>
            	<logic:iterate id="infoStudentGroup" name="infoSiteGroupsByShift" property="infoStudentGroupsList" >
            	<bean:define id="infoGroupProperties" name="infoStudentGroup" property="infoGroupProperties"/>
               	<bean:define id="idInternal" name="infoGroupProperties" property="idInternal"/>
        		<tr>
          		<td>
             		<br>
             		<td class="listClasses">
               		<li><html:link page="<%= "/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;objectCode=" + pageContext.findAttribute("objectCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
						<bean:message key="label.groupWord"/>
                		<bean:write name="infoStudentGroup" property="groupNumber"/>
						</html:link></li>
					</td>
               		
	           		
               		
					<bean:define id="shiftCode" name="infoShift" property="idInternal"/>
               		
               		<td class="listClasses">
               	
               			<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editGroupMembers"/></b></html:link>
                	</td>
               	
               		<td class="listClasses">
               	
                    	<html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + idInternal.toString()+ "&amp;shiftCode=" + shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.editGroupShift"/></b></html:link>
                	</td>
                
                	<td class="listClasses">
               	
                		<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + idInternal.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.deleteGroup"/></b></html:link>
             		</td>
             	</td>
                </tr>

            	</logic:iterate>
            </tbody>
			</table>   
  
            </td>
            </tr>
            </logic:iterate>
 
          

            <br>
           <span class="error"><html:errors/></span> 
        </tbody>
       
<table border="0" style="text-align: left;">
<tbody>
<tr>
<td><br/>
  <html:form action="/viewProjectStudentGroups" method="get">
	<html:submit styleClass="inputbutton"><bean:message key="button.refresh"/>                    		         	
	</html:submit>
	
	<html:hidden property="method" value="viewProjectStudentGroups"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
	<html:hidden  property="objectCode" value="<%= request.getParameter("objectCode")%>"/>
	</html:form> 
</td>
</tr>
</tbody>
</table>
	
</logic:notPresent>	
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available" />
</h4>
</logic:notPresent>