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
		<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.emptyShiftsAndGroups.description" />
			</td>
		</tr>
		</table>
		
		
		<h2><bean:message key="message.shifts.not.available"/></h2>
		
		<span class="error"><html:errors/></span> 	
		<br/>
		<br/>
		<br/>
		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
		<table align="left" width="95%" cellspacing='1' cellpadding='1'>	
		<tbody>			
			
			<tr>
				<td  class="listClasses">
				<font color="red">ATEN��O: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></font>
				</td>
				<td  class="listClasses">
				<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
    			<bean:message key="link.insertStudentsInAttendsSet"/>
		    	</html:link>
				</td>

			</tr>
	  </tbody>    
	 </table>
	 <br/>
		<br/><br/>
		<br/><br/>
		</logic:greaterThan>
			
		<table width="95%" border="0" style="text-align: left;">
		<tbody>
		<b><bean:message key="label.groupPropertiesManagement"/></b>&nbsp
		
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
			<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/deleteGroupProperties.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" onclick='<%= onclick.toString() %>'>
				<bean:message key="link.deleteGroupProperties"/>
			</html:link>
			</tbody>
		</table>
	</logic:present>
	
		
	<logic:notPresent name="noShifts">
		
		<h2><bean:message key="title.ShiftsAndGroups"/></h2>
	
	
	<logic:notPresent name="hasGroups">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewShiftsAndNoGroups.description" />
				</td>
			</tr>
		</table>
		
		<br/>
		<br/>
		
		<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.viewAllStudentsAndGroups"/>
    	</html:link>
		<br/>
		<span class="error"><html:errors/></span> 	
		<br/>
		<br/>
	<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
		<table align="left" width="95%" cellspacing='1' cellpadding='1'>	
		<tbody>			
			
			<tr>
				<td  class="listClasses">
				<font color="red">ATEN��O: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></font>
				</td>
				<td  class="listClasses">
				<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
    			<bean:message key="link.insertStudentsInAttendsSet"/>
		    	</html:link>
				</td>

			</tr>
	  </tbody>    
	 </table>
	 <br/>
		<br/><br/>
		<br/><br/>
		</logic:greaterThan>
		<table width="95%" border="0" style="text-align: left;">
		<tbody>
		<b><bean:message key="label.groupPropertiesManagement"/></b>&nbsp
		
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/deleteGroupProperties.do?method=deleteGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" onclick='<%= onclick.toString() %>'>
				<bean:message key="link.deleteGroupProperties"/>
			</html:link>
			</tbody>
		</table>
			</logic:notPresent>
	
	<logic:present name="hasGroups">
		<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewShiftsAndGroups.description" />
				</td>
			</tr>
		</table>
		
		<br/>
		<br/>
		
		<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.viewAllStudentsAndGroups"/>
    	</html:link>
    	<br/>
		 <span class="error"><html:errors/></span> 	
		<br/>
		<br/>
		<logic:greaterThan name="numberOfStudentsOutsideAttendsSet" value="0">
		<table align="left" width="95%" cellspacing='1' cellpadding='1'>	
		<tbody>			
			
			<tr>
				<td  class="listClasses">
				<font color="red">ATEN��O: <b><bean:write name="numberOfStudentsOutsideAttendsSet"/></b> <bean:message key="message.numberOfStudentsOutsideAttendsSet"/></font>
				</td>
				<td  class="listClasses">
				<html:link page="<%="/prepareInsertStudentsInAttendsSet.do?method=prepareInsertStudentsInAttendsSet&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;groupingOID=" + groupingOID.toString()%>">
    			<bean:message key="link.insertStudentsInAttendsSet"/>
		    	</html:link>
				</td>

			</tr>
	  </tbody>    
	 </table>
	 <br/>
		<br/><br/>
		<br/><br/>
		</logic:greaterThan>
		
		<table width="95%" border="0" style="text-align: left;">
		<tbody>
		<b><bean:message key="label.groupPropertiesManagement"/></b>&nbsp
		
			<html:link page="<%= "/editGroupProperties.do?method=prepareEditGroupProperties&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.editGroupProperties"/>
			</html:link>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/viewAttendsSet.do?method=viewAttendsSet&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") + "&amp;groupingOID=" + groupingOID.toString()%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.viewAttendsSet"/>
			</html:link></b>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<html:link page="<%= "/exportGroupProperties.do?method=preparePublic&amp;nextPage=executionCourseSearch&amp;inputPage=chooseContext&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="objectCode" paramName="objectCode" >
				<bean:message key="link.exportGroupProperties"/>
			</html:link>
		</table>
			</logic:present>
		
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
			
			

			
			<logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
				<logic:empty name="infoSiteGroupsByShift" property="infoSiteShift.infoShift">
			<tr>
								
								<td  class="listClasses">
									<html:link page="<%="/viewStudentsAndGroupsWithoutShift.do?method=viewStudentsAndGroupsWithoutShift&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
		               					Sem Turno
									</html:link>
								</td>
								
								<td class="listClasses">
								---
								</td>
								
								<td class="listClasses">
								---	
								</td>
								
								<td class="listClasses">
								---	
								</td>
								
								
				               	<td class="listClasses">
								---	
						 		</td>
						 		<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>
						 		<bean:define id="nrOfGroups" name="infoSiteShift" property="nrOfGroups"/>
						 		<td class="listClasses">
						 			<b><bean:message key="label.nrOfGroups"/></b><bean:write name="nrOfGroups"/>					 			
						 		</td>
								
								<td class="listClasses">
									<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>		   								
								</td>						
			
								 <td class="listClasses">
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
								
			 		<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
		            		<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
		                	<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
		                	<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
		                	<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
							<tr>
								
								<td  class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
									<html:link page="<%="/viewStudentsAndGroupsByShift.do?method=viewStudentsAndGroupsByShift&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode") %>" >
		               					<bean:write name="infoShift" property="nome"/>
									</html:link>
									
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
								
								<td class="listClasses" width="13%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">							 		
		   																
								 	<html:link page="<%= "/insertStudentGroup.do?method=prepareCreateStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString()+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
										<bean:message key="link.insertGroup"/>
									</html:link>	   
									
								</td>					
			
			
								 <td class="listClasses" width="17%" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
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
		
	</logic:notPresent>	

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.shifts.not.available" />
</h2>
</logic:notPresent>