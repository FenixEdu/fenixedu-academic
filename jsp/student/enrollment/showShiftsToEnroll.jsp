<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="link.shift.enrolment" /></h2>

<%--<logic:present name="classId">
<bean:define id="classId" name="<%= pageContext.findAttribute("classId").toString()%>" />--%>
<bean:define id="hoursPattern">HH : mm</bean:define>
	<%-- :AQUI: mudei a action e acrescentei o studentId --%>
	<html:form action="/enrollStudentInShifts" >
	<html:hidden property="studentId"/>
	<logic:iterate id="infoClass" name="infoClassEnrollmentDetails" property="infoClassList">
		<!-- CLASS -->
		<bean:define id="infoClassId" name="infoClass" property="idInternal"/>		
		<%--<logic:equal name="infoClassId" value="<%= classId.toString() %>">--%>
			<h2 class="redtxt" style="text-align:left"><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome"/></h2>
			<br />
			<table border="0" width="75%" cellspacing="1" cellpadding="5">
				<!-- MAP -->
				<logic:iterate id="executionCourseDetailsElem" name="infoClassEnrollmentDetails" property="classExecutionCourseShiftEnrollmentDetailsMap">
					<logic:equal name="executionCourseDetailsElem" property="key" value="<%= infoClassId.toString()%>" >					
						<logic:iterate id="executionCourseDetails" name="executionCourseDetailsElem" property="value">		
							<!-- COURSE -->	
							<tr>
								<td colspan="4" class="listClasses-subheader"><b><bean:message key="label.course" /></b>&nbsp;<bean:write name="executionCourseDetails" property="infoExecutionCourse.nome"/></td>
							</tr>
					
							<!-- SHIFT -->
							<logic:iterate id="shiftEnrollmentDetails" name="executionCourseDetails" property="shiftEnrollmentDetailsList">						
								<tr>
									<td colspan="4" class="listClasses-header">
										<b><bean:message key="label.shift" /></b>&nbsp;<bean:write name="shiftEnrollmentDetails" property="infoShift.nome"/>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<bean:message key="label.enroll" />?
										<!-- Radio button -->
										<%-- :AQUI: a key estava executionId-shiftId --%>
										<bean:define id="shiftKey">
											shiftMap(<bean:write name="executionCourseDetails" property="infoExecutionCourse.idInternal"/>-<bean:write name="shiftEnrollmentDetails" property="infoShift.tipo"/>)							
										</bean:define>
										<bean:define id="shiftValue" name="shiftEnrollmentDetails" property="infoShift.idInternal" />
										<html:radio property="<%= shiftKey %>" value="<%= shiftValue.toString() %>"/>	
									</td>
								</tr>									
								<tr>
									<th bgcolor="#EBECED" align="center" width="5%"><bean:message key="label.lesson.type"/></td>
									<th bgcolor="#EBECED" align="center" width="10%"><bean:message key="label.lesson.weekDay"/></td>
									<th bgcolor="#EBECED" align="center" width="10%"><bean:message key="label.lesson.time"/></td>
									<th bgcolor="#EBECED" align="center" width="10%"><bean:message key="label.lesson.room"/></td>			
								</tr> 
							
								<!-- LESSONS -->
								<logic:iterate id="infoLesson" name="shiftEnrollmentDetails" property="infoShift.infoLessons">
									<tr>
										<td align='center'><bean:write name="shiftEnrollmentDetails" property="infoShift.tipo.siglaTipoAula" /></td>
										<td align='center'><bean:write name="infoLesson" property="diaSemana" /></td>
										<td align='center'><dt:format patternId="hoursPattern"><bean:write name="infoLesson" property="inicio.time.time" /></dt:format>
											&nbsp;-&nbsp;<dt:format patternId="hoursPattern"><bean:write name="infoLesson" property="fim.time.time" /></dt:format></td>
										<td align='center'><bean:write name="infoLesson" property="infoSala.nome" /></td>						
									</tr>
								</logic:iterate>
							</logic:iterate>
						</logic:iterate>
					</logic:equal>
				</logic:iterate>	
			</table>
		<%--</logic:equal>--%>
	</logic:iterate>
	
	<p>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.enroll"/>
	</html:submit>
	</p>
	</html:form>
</logic:present>
