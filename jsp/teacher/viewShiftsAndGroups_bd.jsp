<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.lang.String" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShift"%>

<%@ page import="java.util.Calendar" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<bean:define id="numberOfStudentsOutsideAttendsSet" name="component" property="numberOfStudentsOutsideAttendsSet" />
	<bean:define id="numberOfStudentsInsideAttendsSet" name="component" property="numberOfStudentsInsideAttendsSet" />
	<bean:define id="groupingOID" name="component" property="infoGrouping.idInternal" />
	<bean:define id="onclick">
		return confirm('<bean:message key="message.confirm.delete.groupProperties"/>')
	</bean:define>

	<logic:present name="noShifts">

		<div class="infoop2">
			<bean:message key="label.teacher.emptyShiftsAndGroups.description" />
		</div>

		
		<p><span class="warning0"><bean:message key="message.shifts.not.available"/></span></p>
		
		<p>
			<span class="error0"><!-- Error messages go here --><html:errors /></span>
		</p>
	
	
		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
			<p class="mbottom05">
				<span class="warning0">Atenção: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></span>
			</p>
			<ul class="mtop05">
				<li>
					<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    			<bean:message key="link.insertStudentsInAttendsSet"/>
			    	</html:link>
		    	</li>
			</ul>
		</logic:greaterThan>
			
		<p class="mtop15">
			<b><bean:message key="label.groupPropertiesManagement"/></b>
		</p>
	
		<p class="mbottom05">	
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
			<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			, 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/deleteGroupProperties.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" onclick='<%= onclick.toString() %>'>
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
				<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
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
				<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
	   			<bean:message key="link.insertStudentsInAttendsSet"/>
		    	</html:link>
			</p>
		</logic:greaterThan>
		
		<p class="mtop15"><b><bean:message key="label.groupPropertiesManagement"/></b>
		
		<p class="mbottom05">
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			, 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/deleteGroupProperties.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" onclick='<%= onclick.toString() %>'>
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
				<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
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
					<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    			<bean:message key="link.insertStudentsInAttendsSet"/>
			    	</html:link>
		    	</li>
		    </ul>
		</logic:greaterThan>
		
		<p class="mtop15"><b><bean:message key="label.groupPropertiesManagement"/></b></p>
		
		<p class="mbottom05">		
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			, 
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			, 
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
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
									<html:link page="<%="/viewStudentsAndGroupsWithoutShift.do?method=viewStudentsAndGroupsWithoutShift&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
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
									<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>		   								
								</td>						
			
								 <td>
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
									<bean:define id="urlString" type="java.lang.String">/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=<%= pageContext.findAttribute("objectCode") %>&amp;groupPropertiesCode=<%= request.getParameter("groupPropertiesCode") %></bean:define>
		                        	<html:link page="<%= urlString %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
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
				<bean:define id="shiftCode" name="infoShift" property="idInternal"/>	
								
			 		<logic:empty name="infoShift" property="infoLessons">
							<tr>
								<td  rowspan="1">
									<html:link page="<%="/viewStudentsAndGroupsByShift.do?method=viewStudentsAndGroupsByShift&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
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
		                        	<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
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
									<html:link page="<%="/viewStudentsAndGroupsByShift.do?method=viewStudentsAndGroupsByShift&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
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
				               		<logic:notEmpty name="infoLesson" property="infoSala.nome">
										<bean:write name="infoLesson" property="infoSala.nome"/>
									</logic:notEmpty>	
						 		</td>
						 		
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td width="10%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
						 			 		
						 			<b><bean:message key="label.nrOfGroups"/> </b><bean:write name="nrOfGroups"/>
						 			
						 			
								</td>
								
								<td width="13%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">							 		
		   																
								 	<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>	   
									
								</td>					
			
			
								 <td width="17%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
		                        <logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
		                        [<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
		                        	<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
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
		
	</logic:notPresent>	

</logic:present>

<logic:notPresent name="siteView" property="component">
<p>
	<span class="warning0"><bean:message key="message.shifts.not.available" /></span>
</p>
</logic:notPresent>