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
		<td class="infoop">
			<bean:message key="message.shift.enrollment.help"/>
		</td>
	</tr>
	<tr>
		<td>
		<br />
			<ul>
				<li>
					<html:link page="<%= "/studentShiftEnrolmentManager.do?method=start&amp;selectCourses=true"%>">
						<bean:message key="link.shift.chooseCourses" />
					</html:link>
				</li>
				<li>
<%--				<html:form action="/studentShiftEnrolmentManagerLoockup">
					<html:hidden property="studentId" value="<%=pageContext.findAttribute("studentId").toString()%>"/>--%>
					
<%--					<html:link page="<%="/studentShiftEnrolmentManagerLoockup.do?method=proceedToShiftEnrolment&amp;studentId=" + pageContext.findAttribute("studentId").toString()%>" onclick="this.form.submit()">
						<bean:message key="link.shift.enrolement.edit" />
					</html:link>
				</html:form>--%>

					<bean:define id="initCreateForm">
					  <bean:message key="link.shift.enrolement.edit"/>
					</bean:define>
					<html:link page="<%="/studentShiftEnrolmentManagerLoockup.do?method=" + initCreateForm + "&amp;studentId=" + pageContext.findAttribute("studentId").toString()%>">
					           <%--action="<%="/studentShiftEnrolmentManagerLoockup"%>"
					           paramId="method"
					           paramName="initCreateForm"--%>
					  <bean:write name="initCreateForm"/>
					</html:link>
				</li>
			</ul>
		</td>
	</tr>
	<tr>
		<td>
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
						<td class="listClasses-subheader" style="text-align:left">
							<bean:message key="label.course" />:&nbsp;
							<bean:write name="infoShift" property="infoDisciplinaExecucao.nome" />
						</td>
					</tr>	
				</logic:notEqual>
			</logic:present>
			<tr>
				<td>
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
			<tr>
				<td>
					<b><bean:message key="property.lessons" />:</b>&nbsp;
					<ul>
						<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
							<li>
								<bean:write name="infoLesson" property="diaSemana" />
								&nbsp;
								<dt:format pattern="HH:mm">
									<bean:write name="infoLesson" property="inicio.timeInMillis" />
								</dt:format>								
								&nbsp;-&nbsp;
								<dt:format pattern="HH:mm">
									<bean:write name="infoLesson" property="fim.timeInMillis" />
								</dt:format>
								&nbsp;
								<bean:write name="infoLesson" property="tipo" />								
								&nbsp;
								<bean:write name="infoLesson" property="infoSala.nome" />
							</li>
						</logic:iterate>
					</ul>
				</td>
			</tr>			
			<bean:define id="elem" name="infoShift" property="infoDisciplinaExecucao.nome" type="java.lang.String"/> 
		</logic:iterate>
		</table>
	</logic:present>
</logic:present>