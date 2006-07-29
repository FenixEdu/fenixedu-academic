<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@ page import="java.lang.String" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>
<%@ page import="java.util.Calendar" %>



<logic:present name="siteView" property="component"> 
<bean:define id="component" name="siteView" property="component" />

<table align="left" width="100%">
<tbody>
<tr>
<td>

<h2><bean:message key="title.editStudentGroupsShift"/></h2>
<br/>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.editStudentGroupsShift.description" />
			</td>
		</tr>
	</table>	
	<br/>


<table align="left" width="95%" cellspacing='1' cellpadding='1'>	
		<tbody>			
			<tr >
				<th class="listClasses-header" width="15%" rowspan="2">
					<bean:message key="property.shift"/>
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
			
			<bean:define id="infoSiteShiftsAndGroups" name="component" property="infoSiteShiftsAndGroups"/>	
	<logic:iterate id="infoSiteGroupsByShift" name="infoSiteShiftsAndGroups" property="infoSiteGroupsByShiftList" >

		<logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
				<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>	
				<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>	
				
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
						 		
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td class="listClasses" width="10%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
						 			 		
						 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>
						 			
						 			
								</td>
							
								 <td class="listClasses" width="17%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        <logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
		                        		[<bean:write name="infoStudentGroup" property="groupNumber"/>]
								</logic:iterate>
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
</td>
</tr>

<tr>
<td>
	<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<br/>

<html:link page="<%="/viewStudentsAndGroupsByShift.do?method=viewStudentsAndGroupsByShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>">
    <bean:message key="link.backToViewStudentsAndGroupsByShift"/></html:link><br/><br/>


<logic:empty name="component" property="infoSiteStudentsAndShiftByStudentGroupList">
<h2><bean:message key="message.infoSiteStudentsAndShiftByStudentGroupList.not.available" /></h2>
</logic:empty> 	


<logic:notEmpty name="component" property="infoSiteStudentsAndShiftByStudentGroupList">
<html:form action="/executeEditStudentGroupsShift" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<bean:message key="message.editStudentGroupsShift"/>

<br/>
<br/>		 
<table width="60%" cellpadding="0" border="0">

	
	<tr>
		<th class="listClasses-header">
		</th>
		
		<th class="listClasses-header"><bean:message key="label.studentGroupNumber" />
		</th>
		
		<th class="listClasses-header"><bean:message key="label.studentsNumber" />
		</th>
		
		<th class="listClasses-header"><bean:message key="label.shiftName" />
		</th>
		
	</tr>
	
	<logic:iterate id="infoSiteStudentsAndShiftByStudentGroup" name="component" property="infoSiteStudentsAndShiftByStudentGroupList">			
	<bean:define id="infoStudentGroup" name="infoSiteStudentsAndShiftByStudentGroup" property="infoStudentGroup"/>
		<tr>	
			<td class="listClasses">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentGroupsCodes" property="studentGroupsCodes">
			<bean:write name="infoStudentGroup" property="idInternal" />
			</html:multibox>
			</td>	
			
			<td class="listClasses">
			<bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>	
			
			</td>	
			<td class="listClasses">
			<logic:notEmpty name="infoSiteStudentsAndShiftByStudentGroup" property="infoSiteStudentInformationList">
			                    <logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentsAndShiftByStudentGroup" property="infoSiteStudentInformationList" >
									<bean:write name="infoSiteStudentInformation" property="number"/>
								</logic:iterate>
						</logic:notEmpty>
								
						<logic:empty name="infoSiteStudentsAndShiftByStudentGroup" property="infoSiteStudentInformationList">
									<bean:message key="message.studentGroup.without.students"/>
						</logic:empty>
			</td>
			
			</td>	
			
			<td class="listClasses">
			<logic:notEmpty name="infoSiteStudentsAndShiftByStudentGroup" property="infoShift">
			<bean:define id="infoShift" name="infoSiteStudentsAndShiftByStudentGroup" property="infoShift"/>
			<bean:write name="infoShift" property="nome"/>
			</logic:notEmpty>
			
			<logic:empty name="infoSiteStudentsAndShiftByStudentGroup" property="infoShift">
			Sem Turno
			</logic:empty>
			
			</td>
		
	 	</tr>	
	 </logic:iterate>
 </table>

<br/>
<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.join"/>                    		         	
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br/>
<br/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editStudentGroupsShift"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />

</html:form>

</logic:notEmpty> 	

</td>
  </tr>
 </tbody>
</table>
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoSiteStudentsAndShiftByStudentGroupList.not.available" />
</h2>
</logic:notPresent>

