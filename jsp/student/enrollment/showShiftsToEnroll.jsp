<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<h2><bean:message key="link.shift.enrolment" /></h2>
<span class="error"><html:errors/></span>
<bean:define id="hoursPattern">HH : mm</bean:define>
<html:form action="/enrollStudentInShifts" >
	<html:hidden property="studentId"/>
	<logic:iterate id="infoClass" name="infoClassEnrollmentDetails" property="infoClassList">
		<!-- CLASS -->
		<bean:define id="infoClassId" name="infoClass" property="idInternal"/>		
			<logic:present name="classId">
				<bean:define id="classIdSelected" name="classId" />		
				<logic:equal name="infoClassId" value="<%= classIdSelected.toString() %>">
				<h2 class="redtxt" style="text-align:left"><bean:message key="label.class" />&nbsp;<bean:write name="infoClass" property="nome"/></h2>
				<br />
				<table border="0" width="75%" cellspacing="1" cellpadding="5">
					<tr>
						<td colspan="4" class="px9">
							<bean:message key="message.shif.type.help" />							
						</td>
					</tr>
					<!-- MAP -->
					<logic:iterate id="executionCourseDetailsElem" name="infoClassEnrollmentDetails" property="classExecutionCourseShiftEnrollmentDetailsMap">
						<logic:equal name="executionCourseDetailsElem" property="key" value="<%= infoClassId.toString()%>" >					
							<logic:iterate id="executionCourseDetails" name="executionCourseDetailsElem" property="value">		
								<!-- COURSE -->
								<tr>
									<td colspan="4" class="listClasses-subheader" style="background:#4F82B5">
									<bean:write name="executionCourseDetails" property="infoExecutionCourse.nome"/></td>
								</tr>
						
								<!-- SHIFT -->
								<logic:iterate id="shiftEnrollmentDetails" name="executionCourseDetails" property="shiftEnrollmentDetailsList">						
									<tr>
										<td colspan="3" class="listClasses-header">
											<b><bean:message key="label.shift" /></b>&nbsp;<bean:write name="shiftEnrollmentDetails" property="infoShift.nome"/>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<bean:message key="label.vacancies" />:&nbsp;<bean:write name="shiftEnrollmentDetails" property="vacancies"/>
										</td>
										<td  class="listClasses-header" style="text-align:right">
											<bean:message key="label.enroll" />?
											<!-- Radio button -->
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
				</logic:equal>
			</logic:present>		
	</logic:iterate>
	<p>
		<table>
			<tr>
				<td>
						<html:submit styleClass="inputbutton">
							<bean:message key="button.enroll"/>
						</html:submit>
					</html:form>
				</td>
				<td>
					<html:form action="/studentShiftEnrollmentManagerLoockup">
						<html:hidden property="classId" value="<%=pageContext.findAttribute("classId").toString()%>"/>
						<html:hidden property="studentId" value="<%=pageContext.findAttribute("studentId").toString()%>"/>
						<html:submit property="method" styleClass="inputbutton">
							<bean:message key="button.clean"/>
						</html:submit>
					</html:form>
				</td>
			</tr>
		</table>
	</p>
