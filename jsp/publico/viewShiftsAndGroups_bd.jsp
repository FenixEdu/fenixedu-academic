<%@ page language="java" %>
<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="DataBeans.InfoShift"%>
<%@ page import="java.util.Calendar" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
	<logic:empty name="component" property="infoSiteGroupsByShiftList">
		<h2><bean:message key="message.shifts.not.available" /></h2>
	</logic:empty>
	
	<logic:notEmpty name="component" property="infoSiteGroupsByShiftList">	
		<h2>
			<bean:define id="groupProperties" name="component" property="infoGroupProperties" />
			<bean:message key="label.grouping"/>:
			<bean:write name="groupProperties" property="name" />
		</h2>
		<table class="tab_complex" width="70%" cellspacing="1" cellpadding="2">
			<tr>
				<th rowspan="2">
					<bean:message key="property.shift"/>
				</th>
				<th colspan="4">
					<bean:message key="label.lesson"/>
				</th>
				<th rowspan="2">
					<bean:message key="property.groupOrGroups"/>
				</th>
			</tr>
					
			<tr>
				<th>
					<bean:message key="label.day"/>
				</th>
				<th>
					<bean:message key="property.lesson.beginning"/>
				</th>
				<th>
					<bean:message key="property.lesson.end"/>
				</th>
				<th>
					<bean:message key="property.lesson.room"/>
				</th>
			</tr>		
    		
			<logic:iterate id="infoSiteGroupsByShift" name="component" property="infoSiteGroupsByShiftList" >
				<bean:define id="infoSiteShift" name="infoSiteGroupsByShift" property="infoSiteShift"/>	
					
					<logic:notEmpty name="infoSiteShift" property="infoShift">
					<bean:define id="infoShift" name="infoSiteShift" property="infoShift"/>	
																	
						<logic:iterate id="infoLesson" name="infoShift" property="infoLessons" length="1" indexId="infoLessonIndex">
							<% Integer iH = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.HOUR_OF_DAY)); %>
							<% Integer iM = new Integer(((InfoLesson) infoLesson).getInicio().get(Calendar.MINUTE)); %>
							<% Integer fH = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.HOUR_OF_DAY)); %>
							<% Integer fM = new Integer(((InfoLesson) infoLesson).getFim().get(Calendar.MINUTE)); %>
							<tr>
								
								<td rowspan="<%=((InfoShift) infoShift).getInfoLessons().size() %>">
									<bean:write name="infoShift" property="nome"/>
								</td>
								<td>
									<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
								</td>
								<td>
									<%= iH.toString()%>:<%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
								</td>
								<td>
									<%= fH.toString()%>:<%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>								
								</td>
									
								<td>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</td>
								
								 <td class="listClasses" rowspan="<%=((InfoShift) infoShift).getInfoLessons().size()%>">
								<logic:notEmpty name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList">
								[<logic:iterate id="infoSiteStudentGroup" name="infoSiteGroupsByShift" property="infoSiteStudentGroupsList" >
									<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>	
									<html:link page="<%= "/viewSite.do" + "?method=viewStudentGroupInformationAction&amp;objectCode=" + pageContext.findAttribute("objectCode")  + "&amp;executionPeriodOID=" + pageContext.findAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;groupProperties=" + pageContext.findAttribute("groupProperties") %>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
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
							<tr>
								<td>
									<bean:write name="infoLesson" property="diaSemana"/> &nbsp;
								</td>
								<td>
									<%= iH.toString()%>:<%= iM.toString()%><% if (iM.intValue() == 0) { %>0<% } %>
								</td>
								<td>
									<%= fH.toString()%>:<%= fM.toString()%><% if (fM.intValue() == 0) { %>0<% } %>
								</td>
								<td>
									<bean:write name="infoLesson" property="infoSala.nome"/>
								</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
			</logic:iterate>
		</table>
	</logic:notEmpty>
</logic:present>

<logic:notPresent name="siteView" property="component">
	<h2><bean:message key="message.shifts.not.available" /></h2>
</logic:notPresent>
