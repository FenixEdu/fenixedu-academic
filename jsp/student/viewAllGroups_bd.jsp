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


<logic:present name="infoSiteGroupsByShiftList">
	
	

	<logic:empty name="infoSiteGroupsByShiftList">
		<h2><bean:message key="message.infoSiteGroupsByShiftList.not.available" /></h2>
	</logic:empty>
	
	<h2><span class="error"><html:errors/></span></h2>
	
	<table border="0" style="text-align: left;">
	
		<tbody>
		
		<tr>
		<td>
		<html:link page="<%="/groupEnrolment.do?method=prepareEnrolment&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>">
			<b><bean:message key="link.insertGroup"/></b>
		</html:link>	
		</td>
		</tr>	
		
		<logic:iterate id="infoSiteGroupsByShift" name="infoSiteGroupsByShiftList">
     		<tr>
			<td>	
			<br>   
			<br>
			<br>
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
        		<tr>
        		<td>
          			<br>
             		<td class="listClasses">
               			<li>
               				<html:link page="/viewStudentGroupInformation.do" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
								<bean:message key="label.groupWord"/>
                				<bean:write name="infoStudentGroup" property="groupNumber"/>
							</html:link>
						</li>
					</td>
               		
	           		<bean:define id="infoGroupProperties" name="infoStudentGroup" property="infoGroupProperties"/>
               		<bean:define id="idInternal" name="infoGroupProperties" property="idInternal"/>
					
               		<td class="listClasses">
               	
               			<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.enrolment"/></b>
               			</html:link>
                	</td>
               	
               		<td class="listClasses">
               	
                   		<html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
               				<b><bean:message key="link.removeEnrolment"/></b>
               			</html:link>
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
        </tbody>
</table>

	
	
	
	<html:form action="/viewProjectStudentGroups" method="get">

	<html:submit styleClass="inputbutton"><bean:message key="button.refresh"/>                    		         	
	</html:submit>
	
	<html:hidden property="method" value="execute"/>
	<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
	</html:form>

     

</logic:present>

<logic:notPresent name="infoSiteGroupsByShiftList">
<h4>
<bean:message key="message.infoSiteGroupsByShiftList.not.available"/>
</h4>
</logic:notPresent>

