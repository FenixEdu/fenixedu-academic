<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<h2>
	<bean:message key="title.student.shift.enrollment" />
</h2>
<span class="error"><html:errors/></span>
<logic:notPresent name="infoShiftEnrollment" >
	<span class="error"><bean:message key="error.notAuthorized.ShiftEnrollment" /></span>
</logic:notPresent>
<logic:present name="infoShiftEnrollment" >
	<bean:define id="executionDegreeId" name="infoShiftEnrollment" property="infoExecutionDegree.idInternal" />
	<bean:define id="studentId" name="infoShiftEnrollment" property="infoStudent.idInternal" />
	<table>
		<tr>
			<td class="infoop" colspan='5'>
				<bean:message key="message.shift.enrollment.help"/>
			</td>
		</tr>
		<tr>
			<td colspan='5'>
				<br />
				<ul>
					<li>
						<html:link page="<%= "/studentShiftEnrolmentManager.do?method=start&amp;selectCourses=true"%>">
							<bean:message key="link.shift.chooseCourses" />
						</html:link>
					</li>
					<li>
						<bean:define id="link">
							<bean:message key="link.shift.enrolement.edit"/>
						</bean:define>
						<html:link page="<%="/studentShiftEnrolmentManagerLoockup.do?method=" + link + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()%>">
					  		<bean:write name="link"/>
						</html:link>
					</li>
				</ul>
			</td>
		</tr>
		<tr>
			<td colspan='5'>
				<br />
				<b><bean:message key="message.student.shift.enrollment" /></b>
			</td>
		</tr>
		<logic:present name="infoShiftEnrollment" property="infoShiftEnrollment">
			<bean:define id="elem" value="" type="java.lang.String"/>
			<logic:iterate id="infoShift" name="infoShiftEnrollment" property="infoShiftEnrollment" type="DataBeans.InfoShift">
				<logic:present name="elem">
					<logic:notEqual name="elem" value="<%=infoShift.getInfoDisciplinaExecucao().getNome()%>">
						<tr>
							<td colspan='5'>
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>	
						<tr>
							<td class="listClasses-subheader" style="text-align:left" colspan='5'>
								<bean:message key="label.course" />:&nbsp;
								<bean:write name="infoShift" property="infoDisciplinaExecucao.nome" />
							</td>
						</tr>	
					</logic:notEqual>
				</logic:present>
				<tr>
					<td colspan='5' class="listClasses-header" style="text-align:left">
						<bean:message key="property.turno" />:</b>&nbsp;
						<bean:write name="infoShift" property="nome" />
						<bean:define id="infoShiftId" name="infoShift" property="idInternal" />
						-
						<html:link page="<%= "/studentShiftEnrolmentManager.do?method=unEnroleStudentFromShift&amp;studentId="
													+ pageContext.findAttribute("studentId").toString()
													+ "&amp;shiftId="
													+ pageContext.findAttribute("infoShiftId").toString()
													%>">
							<bean:message key="link.unenrole.shift" />
						</html:link>
					</td>
				</tr>
				<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
					<tr>
						<td style="text-align:center">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</td>
						<td style="text-align:center">
							<bean:write name="infoLesson" property="tipo" />								
						</td>
						<td style="text-align:center">
							<bean:write name="infoLesson" property="diaSemana" />
						</td>
						<td style="text-align:center">
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="inicio.timeInMillis" />
							</dt:format>								
							&nbsp;-&nbsp;
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="fim.timeInMillis" />
							</dt:format>
						</td>
						<td style="text-align:center">
							<bean:write name="infoLesson" property="infoSala.nome" />
						</td>
					</tr>			
				</logic:iterate>
				<bean:define id="elem" name="infoShift" property="infoDisciplinaExecucao.nome" type="java.lang.String"/> 
			</logic:iterate>
		</table>
	</logic:present>
</logic:present>