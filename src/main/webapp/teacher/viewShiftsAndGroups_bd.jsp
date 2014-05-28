<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.lang.String" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>

	<bean:define id="numberOfStudentsOutsideAttendsSet" name="component" property="numberOfStudentsOutsideAttendsSet" />
	<bean:define id="numberOfStudentsInsideAttendsSet" name="component" property="numberOfStudentsInsideAttendsSet" />
	<bean:define id="groupingOID" name="component" property="infoGrouping.externalId" />
	<bean:define id="onclick">
		return confirm('<bean:message key="message.confirm.delete.groupProperties"/>')
	</bean:define>

	<logic:present name="noShifts">

		<h2><bean:message key="label.groupPropertiesManagement"/></h2>
		
		<div class="infoop2">
			<bean:message key="label.teacher.emptyShiftsAndGroups.description" />
		</div>

		
		<p class="mtop15"><em><bean:message key="message.shifts.not.available"/></em></p>
		
		<p>
			<span class="error0"><!-- Error messages go here --><html:errors /></span>
		</p>
	
	
		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
			<p class="mbottom05">
				<span class="warning0">Atenção: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></span>
			</p>
			<ul class="mtop05">
				<li>
					<html:link page="<%="/studentGroupManagement.do?method=prepareInsertStudentsInAttendsSet&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    			<bean:message key="link.insertStudentsInAttendsSet"/>
			    	</html:link>
		    	</li>
			</ul>
		</logic:greaterThan>
			
		<p class="mtop15">
			<b><bean:message key="label.groupPropertiesManagement"/>:</b>
		</p>
	
		<p class="mbottom05">	
			<html:link page="<%= "/studentGroupManagement.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			| 
			<html:link page="<%= "/studentGroupManagement.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="executionCourseID" paramName="executionCourseID" >
			<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			| 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			| 
			<html:link page="<%= "/studentGroupManagement.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" onclick='<%= onclick.toString() %>'>
				<bean:message key="link.deleteGroupProperties"/>
			</html:link>
		</p>
	</logic:present>
	
		
	<logic:notPresent name="noShifts">
		
		<h2><bean:message key="title.ShiftsAndGroups"/></h2>
	
	
	<logic:notPresent name="hasGroups">
		<div class="infoop2">
			<bean:message key="label.teacher.viewShiftsAndNoGroups.description" />
		</div>
		
		<ul class="mtop15">
			<li>
				<html:link page="<%="/studentGroupManagement.do?method=viewAllStudentsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			    	<bean:message key="link.viewAllStudentsAndGroups"/>
		    	</html:link>
	    	</li>
		</ul>

		<p>
			<span class="error0"><!-- Error messages go here --><html:errors /></span> 	
		</p>

		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
			<p>
				<span color="red">Atenção: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></span>
				<html:link page="<%="/studentGroupManagement.do?method=prepareInsertStudentsInAttendsSet&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
	   			<bean:message key="link.insertStudentsInAttendsSet"/>
		    	</html:link>
			</p>
		</logic:greaterThan>
		
		<p class="mtop15"><b><bean:message key="label.groupPropertiesManagement"/></b>
		
		<p class="mbottom05">
			<html:link page="<%= "/studentGroupManagement.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/studentGroupManagement.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			, 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/studentGroupManagement.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" onclick='<%= onclick.toString() %>'>
				<bean:message key="link.deleteGroupProperties"/>
			</html:link>
		</p>
		
	</logic:notPresent>
	
	<logic:present name="hasGroups">
		<div class="infoop2">
			<bean:message key="label.teacher.viewShiftsAndGroups.description" />
		</div>
		
		<ul class="mtop15">
			<li>
				<html:link page="<%="/studentGroupManagement.do?method=viewAllStudentsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			    	<bean:message key="link.viewAllStudentsAndGroups"/>
		    	</html:link>
	    	</li>
    	</ul>

		<p><span class="error0"><!-- Error messages go here --><html:errors /></span></p>
		
		
		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
			<p class="mbottom05">
				<span class="warning0">ATENÇÃO: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></span>
			</p>
			<ul class="mtop05">
				<li>
					<html:link page="<%="/studentGroupManagement.do?method=prepareInsertStudentsInAttendsSet&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    			<bean:message key="link.insertStudentsInAttendsSet"/>
			    	</html:link>
		    	</li>
		    </ul>
		</logic:greaterThan>
		
		<p class="mtop15"><b><bean:message key="label.groupPropertiesManagement"/></b></p>

		<p class="mbottom05">		
			<html:link page="<%= "/studentGroupManagement.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/studentGroupManagement.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			, 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="executionCourseID" paramName="executionCourseID" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
		</p>
		
	</logic:present>
		

	
	  	<table class="tstyle4 tdcenter">	
		<tbody>			
			<tr>
				<th width="15%" rowspan="2">
					<bean:message key="property.shift"/>
				</th>
				<th colspan="4" width="45%"> 
					<bean:message key="property.lessons"/>
				</th>
				<th width="40%" rowspan="2" colspan="3">
					<bean:message key="property.groups"/>
				</th>
			</tr>
			<tr>
				<th width="15%">
					<bean:message key="property.lesson.weekDay"/>
				</th>
				<th width="10%">
					<bean:message key="property.lesson.beginning"/>
				</th>
				<th width="10%">
					<bean:message key="property.lesson.end"/>
				</th>
				<th width="10%">
					<bean:message key="property.lesson.room"/>
				</th>
			</tr>
			
			

			
			<logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
				<logic:empty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
			<tr>
								
								<td>
									<html:link page="<%="/studentGroupManagement.do?method=viewStudentsAndGroupsWithoutShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
		               					Sem Turno
									</html:link>
								</td>
								
								<td>
								---
								</td>
								
								<td>
								---	
								</td>
								
								<td>
								---	
								</td>
								
								
				               	<td>
								---	
						 		</td>
						 		<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td>
						 			<b><bean:message key="label.nrOfGroups"/></b><bean:write name="nrOfGroups"/>					 			
						 		</td>
								
								<td>
									<html:link page="<%= "/studentGroupManagement.do?method=prepareCreateStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>		   								
								</td>						
			
								 <td>
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
									<bean:define id="urlString" type="java.lang.String">/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=<%= pageContext.findAttribute("executionCourseID") %>&amp;groupPropertiesCode=<%= request.getParameter("groupPropertiesCode") %></bean:define>
		                        	<html:link page="<%= urlString %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
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
				<bean:define id="shiftCode" name="infoShift" property="externalId"/>	
								
			 		<logic:empty name="infoShift" property="infoLessons">
							<tr>
								<td  rowspan="1">
									<html:link page="<%="/studentGroupManagement.do?method=viewStudentsAndGroupsByShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
		               					<bean:write name="infoShift" property="nome"/>
									</html:link>
								</td>
								<td>
								---	
								</td>
								<td>
								---	
								</td>
								<td>
								---	
								</td>
									
				               	<td>
								---	
						 		</td>
						 		
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
			
								 <td width="17%" rowspan="1" colspan="3">
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
		                        	<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
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
								
			 		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
		            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
		                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
		                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
		                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
							<tr>
								
								<td  rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
									<html:link page="<%="/studentGroupManagement.do?method=viewStudentsAndGroupsByShift&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
		               					<bean:write name="infoShift" property="nome"/>
									</html:link>
									
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
				               		<logic:notEmpty name="infoLesson" property="infoSala">
										<bean:write name="infoLesson" property="infoSala.nome"/>
									</logic:notEmpty>	
						 		</td>
						 		
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td width="10%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
						 			 		
						 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>
						 			
						 			
								</td>
								
								<td width="13%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">							 		
		   																
								 	<html:link page="<%= "/studentGroupManagement.do?method=prepareCreateStudentGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>	   
									
								</td>					
			
			
								 <td width="17%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
		                        	<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="externalId">
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
								<logic:notEmpty name="infoLesson" property="infoSala">
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</logic:notEmpty>	
							</td>
						</tr>
					</logic:iterate>
					</logic:notEmpty>
	          </logic:iterate>

	        </tbody>    
		</table>
		
	</logic:notPresent>	
