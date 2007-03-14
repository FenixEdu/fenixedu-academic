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
		<logic:equal name="type" value="true">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithLink.description" />
			</div>
		</logic:equal>
		<logic:equal name="type" value="false">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithoutLink.description" />
			</div>
		</logic:equal>
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<logic:equal name="type" value="true">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithLink.description" />
			</div>
		</logic:equal>
		<logic:equal name="type" value="false">
			<div class="infoop2">
				<bean:message key="label.teacher.viewStudentsAndGroupsByShift.WithoutLink.description" />
			</div>
		</logic:equal>
	</logic:notEmpty>		
	

<table class="tstyle4 tdcenter thlight">	
	<tbody>		
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
		
	 	<bean:define id="infoShift" name="infoSiteStudentsAndGroups" property="infoShift"/>
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
								<logic:notEmpty name="infoLesson" property="infoSala.nome">
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</logic:notEmpty>	
							</td>
						</tr>
					</logic:iterate>

        </tbody>
    
	</table>


<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>



<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">

	<ul>
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
				<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>
	</ul>
	
	<logic:equal name="type" value="true">
	<ul>
		<li>
			<html:link page="<%="/editStudentGroupsShift.do?method=prepareEditStudentGroupsShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>">
	   		<bean:message key="link.editStudentGroupsShift"/>
	   		</html:link>
   		</li>
   	</ul>
	</logic:equal>


		
	<p class="mtop2">
		<span class="warning0">
			<bean:message key="message.infoSiteStudentsAndGroupsList.not.available" />
		</span>
	</p>
 	
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	<ul>
		<li>
			<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			   	<bean:message key="link.backToShiftsAndGroups"/>
		   	</html:link>
	   	</li>
   	</ul>
    	
    	
	<logic:equal name="type" value="true">
	<ul>
		<li>
			<html:link page="<%="/editStudentGroupsShift.do?method=prepareEditStudentGroupsShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>">
		    	<bean:message key="link.editStudentGroupsShift"/>
	    	</html:link>
    	</li>
    </ul>
	</logic:equal>

	

 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>

	<p class="mtop2 mbottom05">
		<bean:message key="label.teacher.NumberOfStudentsInShift" /><%= count %>
	</p>
	
	<table class="tstyle4 mtop05">
	<tr>
		<th width="10%"><bean:message key="label.studentGroupNumber" />
		</th>
		<th width="16%"><bean:message key="label.numberWord" />
		</th>
		<th width="53%"><bean:message key="label.nameWord" />
		</th>
		<th width="26%"><bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<tr>	
		
			<td>
			<bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>
			
			<td>
			<bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td>
			<bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td>
			<bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		</tr>				
	 </logic:iterate>

</tbody>
</table>



</logic:notEmpty>

 
</logic:present>
