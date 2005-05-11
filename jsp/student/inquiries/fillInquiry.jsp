<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoAula" %>
<%@ page import="net.sourceforge.fenixedu.util.StringAppender" %>

<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<noscript>
	<font class="error">
		<bean:message key="error.message.inquiries.javascript.disabled" bundle="INQUIRIES_RESOURCES"/>
	</font>
</noscript>
<logic:present name='<%= InquiriesUtil.ANCHOR %>'>
	<script>
		if(navigator.appName=="Microsoft Internet Explorer") {
			location.hash='<%= request.getAttribute(InquiriesUtil.ANCHOR) %>';
		}
	</script>	
</logic:present>


<div id="inquiry">

	<p class="center">
		<bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/>
	</p>
	<h2 class="center caps">
		<bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/>
	</h2>
	<h3 class="center caps">
		<bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/>
	</h3>

	<bean:message key="message.inquiries.instructions" bundle="INQUIRIES_RESOURCES"/>

	<p><strong>
		<bean:message key="title.inquiries.inquiry.structure" bundle="INQUIRIES_RESOURCES"/>
	</strong></p>
	
	<ol>
		<li>
			<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
				<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
			</a>
		</li>
		<li>
			<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
				<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
			</a>
		</li>
		<li>
			<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
				<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
			</a>
		</li>
		<li>
			<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
				<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
			</a>
		</li>
	</ol>

	<html:form action="/fillInquiries">
		<html:hidden property="method" value="defaultMethod"/>
		<html:hidden property="attendingExecutionCourseId"/>
		<html:hidden property="studentExecutionDegreeId"/>
		<html:hidden property="studentAttendsId"/>
		
		<div id='<%= InquiriesUtil.STUDENT_FORM_ANCHOR %>' class="block">
			<h2>
				1. <bean:message key="header.inquiries.student.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>
						
			<table>
				<logic:present name='<%= InquiriesUtil.STUDENT_FORM_ERROR %>'>
					<tr>
						<td class="top" colspan="2">
							<font class="error">
								<bean:message key="error.message.inquiries.student.form" bundle="INQUIRIES_RESOURCES" />
							</font>
						</td>
					</tr>				
				</logic:present>

				<tr>
					<td class="left">
						1.1 <bean:message key="table.rowname.inquiries.student.form.degree" bundle="INQUIRIES_RESOURCES"/>

					</td>
					<td class="right">
						<bean:write name='<%= InquiriesUtil.STUDENT_EXECUTION_DEGREE %>' property="infoDegreeCurricularPlan.infoDegree.sigla"/>
					</td>
				</tr>

				<tr>
					<td class="left">
						1.2 <bean:message key="table.rowname.inquiries.student.form.curricular.year" bundle="INQUIRIES_RESOURCES"/> *
					</td>
					<td class="right">
						<table class="radio2">
							<tr>
								<td>
									<c:forTokens items="1,2,3,4,5" delims="," var="cYear">
										<bean:define id="year">
											<c:out value="${cYear}"/>
										</bean:define>
										<c:out value="${cYear}"/>&ordm;<html:radio property="curricularYear" value='<%= year %>'/>
									</c:forTokens>
								</td>
							</tr>
						</table>

					</td>
				</tr>

				<tr>
					<td class="left">
						1.3 <bean:message key="table.rowname.inquiries.student.form.school.class" bundle="INQUIRIES_RESOURCES"/>
					</td>
					<td class="right">
						<html:select property="attendingCourseSchoolClassId">
							<bean:define id="noChoice">
								<bean:message key="value.inquiries.no.choice" bundle="INQUIRIES_RESOURCES"/>
							</bean:define>
							<html:option key="label.inquiries.choose" bundle="INQUIRIES_RESOURCES" value='<%= noChoice %>'/>
							<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_SCHOOL_CLASSES %>'>
								<html:options property="idInternal" 
											  labelProperty="nome" 
											  collection='<%= InquiriesUtil.ATTENDING_COURSE_SCHOOL_CLASSES %>' />
							</logic:present>
						</html:select>
							

					</td>
				</tr>

				<tr>
					<td class="left">
						1.4 <bean:message key="table.rowname.inquiries.student.form.first.enrollment" bundle="INQUIRIES_RESOURCES"/> *
					</td>
					<td class="right">
						<bean:message key="table.rowname.inquiries.student.form.first.enrollment.yes" bundle="INQUIRIES_RESOURCES"/>
						<html:radio property="firstEnrollment" value="true"/>
						
						<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
						<html:radio property="firstEnrollment" value="false"/>
					</td>
				</tr>

			</table>

			<p align="center">
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>

		</div>

		<logic:present name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>'>
			<div id='<%= InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>' class="block">
				<h2>
					2. <bean:message key="header.inquiries.course.form" bundle="INQUIRIES_RESOURCES"/>
				</h2>
				
				<table>
					<logic:present name='<%= InquiriesUtil.COURSE_FORM_ERROR %>'>
						<tr>
							<td class="top" colspan="2">
								<font class="error">
									<bean:message key="error.message.inquiries.course.form" bundle="INQUIRIES_RESOURCES" />
								</font>
							</td>
						</tr>				
					</logic:present>
					<tr>
						<td class="left">
							2.1 <bean:message key="table.rowname.inquiries.course.form.course" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<bean:write name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>' property="nome" />
							<br/>
							<logic:notPresent name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>'>
								<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES %>'>
									<html:select property="attendingCourseExecutionDegreeId">
										<html:options property="idInternal" 
													  labelProperty="infoDegreeCurricularPlan.infoDegree.sigla" 
													  collection='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES %>' />
									</html:select>
								</logic:present>
							</logic:notPresent>
							<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>'>
								<bean:define id="attendingCourseExecutionDegreeId" name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>' property="idInternal" />
								<html:hidden property="attendingCourseExecutionDegreeId" value='<%= attendingCourseExecutionDegreeId.toString() %>' />
							</logic:present>
						</td>
					</tr>

					<tr>
						<td class="top" colspan="2">
							<bean:message key="message.inquiries.how.evaluate" bundle="INQUIRIES_RESOURCES"/>
						</td>
					</tr>				

					<tr>
						<td class="left">
							2.2 <bean:message key="table.rowname.inquiries.course.form.class.coordination" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion22" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.3 <bean:message key="table.rowname.inquiries.course.form.study.elements.contribution" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion23" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.4 <bean:message key="table.rowname.inquiries.course.form.previous.knowlegde.articulation" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion24" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.5 <bean:message key="table.rowname.inquiries.course.form.course.contribution.for.graduation" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion25" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.6 <bean:message key="table.rowname.inquiries.course.form.evaluation.method.adequation" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion26" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.7 <bean:message key="table.rowname.inquiries.course.form.weekly.spent.hours" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="0-1h,2-5h,6-10h,11-15h,+15h" delims="," var="hours">
										<td>
											<c:out value="${hours}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1,2,3,4,5" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion27" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>

					<tr>
						<td class="left">
							2.8 <bean:message key="table.rowname.inquiries.course.form.global.appreciation" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<table class="radio">
								<tr>
									<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
										<td>
											<c:out value="${int}"/>
										</td>
									</c:forTokens>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio property="executionCourseQuestion28" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>
				</table>

				<p align="center">
					<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
						<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
					</a>
				</p>
	
			</div>
		</logic:present>


		<div id='<%= InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>' class="block">
			<h2>
				3. <bean:message key="header.inquiries.teachers.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>

			<logic:present name='<%= InquiriesUtil.NO_ATTENDING_COURSE_TEACHER_FORM_ERROR %>'>
				<font class="error">
					<bean:message key="error.message.inquiries.no.attending.course.teacher.form" bundle="INQUIRIES_RESOURCES" />
				</font>
			</logic:present>

			<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_TEACHERS %>'>
				<html:hidden property="newAttendingCourseTeacherId" />
				<html:hidden property="newAttendingCourseNonAffiliatedTeacherId" />
				<bean:define id="evaluateTeacher">
					<bean:message key="message.inquiries.anchor.title.evaluate.teacher" bundle="INQUIRIES_RESOURCES"/>
				</bean:define>

				<div class="block2">
					<p><bean:message key="title.inquiries.choose.teacher" bundle="INQUIRIES_RESOURCES"/></p>
					<ul>
						<logic:iterate id="attendingCourseTeacher" name='<%= InquiriesUtil.ATTENDING_COURSE_TEACHERS %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes">
							<logic:present name="attendingCourseTeacher" property="teacher">
								<bean:define id="currentTeacherId" name="attendingCourseTeacher" property="idInternal"/>
								<li>
									<%--
									<html:submit styleClass="inputbuttonlink"
										onmouseover="this.style.fontWeight='bold';"
										onmouseout="this.style.fontWeight='';"
										onclick='<%="this.form.method.value='prepareNewTeacher';this.form.newAttendingCourseTeacherId.value='" + currentTeacherId + "';" %>'
										title='<%= evaluateTeacher %>'
										>
										<bean:write name="attendingCourseTeacher" property="teacher.infoPerson.nome" />
									</html:submit>
									--%>
									<html:submit styleClass="inputbuttonlink"
										onmouseover="this.style.textDecoration='none';"
										onmouseout="this.style.textDecoration='underline';"
										onclick='<%= StringAppender.append("this.form.method.value='prepareNewTeacher';this.form.newAttendingCourseTeacherId.value='",
											currentTeacherId.toString(), "';") %>'
										title='<%= evaluateTeacher %>'
										>
										<bean:write name="attendingCourseTeacher" property="teacher.infoPerson.nome" />
									</html:submit>
									-
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<c:if test="${completeAttendingCourseTeacherId == currentTeacherId}">
											<font class="error"><bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/></font>
										</c:if>
										<c:if test="${completeAttendingCourseTeacherId != currentTeacherId}">
											<bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/>
										</c:if>
									</logic:empty>
									<logic:notEmpty name="attendingCourseTeacher" property="remainingClassTypes">
										<logic:iterate id="remainingClassType" name="attendingCourseTeacher" property="remainingClassTypes" indexId="index">
											<c:if test="${index != 0}">
												,&nbsp;
											</c:if>
											<bean:write name="remainingClassType" property="fullNameTipoAula" />
										</logic:iterate>
									</logic:notEmpty>
								</li>
							</logic:present>
							<logic:present name="attendingCourseTeacher" property="nonAffiliatedTeacher">
								<bean:define id="currentTeacherId" name="attendingCourseTeacher" property="idInternal"/>
								<li>
									<html:submit styleClass="inputbuttonlink"
										onmouseover="this.style.textDecoration='none';"
										onmouseout="this.style.textDecoration='underline';"
										onclick='<%= StringAppender.append("this.form.method.value='prepareNewTeacher';this.form.newAttendingCourseNonAffiliatedTeacherId.value='",
											currentTeacherId.toString(), "';") %>'
										title='<%= evaluateTeacher %>'
										>
										<bean:write name="attendingCourseTeacher" property="nonAffiliatedTeacher.name" />
									</html:submit>
									-
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<c:if test="${completeAttendingCourseNonAffiliatedTeacherId == currentTeacherId}">
											<font class="error"><bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/></font>
										</c:if>
										<c:if test="${completeAttendingCourseNonAffiliatedTeacherId != currentTeacherId}">
											<bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/>
										</c:if>
									</logic:empty>
									<logic:notEmpty name="attendingCourseTeacher" property="remainingClassTypes">
										<logic:iterate id="remainingClassType" name="attendingCourseTeacher" property="remainingClassTypes" indexId="index">
											<c:if test="${index != 0}">
												,&nbsp;
											</c:if>
											<bean:write name="remainingClassType" property="fullNameTipoAula" />
										</logic:iterate>
									</logic:notEmpty>
								</li>
							</logic:present>
						</logic:iterate>
					</ul>
				</div>
			</logic:present>

			<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>'>
				<html:hidden property="selectedAttendingCourseTeacherFormPosition" />

				<logic:iterate id="selectedAttendingCourseTeacher" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>' indexId="teacherPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher">
					<bean:define id="selectedAttendingCourseTeacherId" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.idInternal" />

								
					<html:hidden property="selectedAttendingCourseTeachersId" value='<%= selectedAttendingCourseTeacherId.toString() %>'/>
		            <html:hidden property="selectedAttendingCourseTeachersClassTypeT" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(TipoAula.TEORICA) %>' />
            		<html:hidden property="selectedAttendingCourseTeachersClassTypeP" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(TipoAula.PRATICA) %>' />
					<html:hidden property="selectedAttendingCourseTeachersClassTypeL" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(TipoAula.LABORATORIAL) %>' />
					<html:hidden property="selectedAttendingCourseTeachersClassTypeTP" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(TipoAula.TEORICO_PRATICA) %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion33" value='<%= "" + selectedAttendingCourseTeacher.getStudentAssiduity() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion34" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAssiduity() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion35" value='<%= "" + selectedAttendingCourseTeacher.getTeacherPunctuality() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion36" value='<%= "" + selectedAttendingCourseTeacher.getTeacherClarity() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion37" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAssurance() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion38" value='<%= "" + selectedAttendingCourseTeacher.getTeacherInterestStimulation() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion39" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAvailability() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion310" value='<%= "" + selectedAttendingCourseTeacher.getTeacherReasoningStimulation() %>' />
					<html:hidden property="selectedAttendingCourseTeachersQuestion311" value='<%= "" + selectedAttendingCourseTeacher.getGlobalAppreciation() %>' />
					
					<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
						<html:hidden property="selectedAttendingCourseTeacherIsAffiliated" value="true"/>
					</logic:present>
					<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
						<html:hidden property="selectedAttendingCourseTeacherIsAffiliated" value="false"/>
					</logic:present>
		
					<%
										
					Integer currentAttendingCourseTeacherFormPosition =
						(Integer) request.getAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION);
					
					if((currentAttendingCourseTeacherFormPosition != null) &&
					   (currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition)) {
					%>
						
						<%--html:hidden property="currentAttendingCourseTeacherId" value='<%= selectedAttendingCourseTeacherId.toString() %>'/--%>
						<html:hidden property="currentAttendingCourseTeacherFormPosition" value='<%= "" + teacherPosition %>' />
						
						<table id='<%= InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR  %>'>
							<tr>
								<td class="left" colspan="2">
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
										<strong>
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.teacherNumber" />
											-
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.infoPerson.nome" />
										</strong>
									</logic:present>
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
										<strong>
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher.name" />
										</strong>
									</logic:present>
								</td>
							</tr>


							<logic:present name='<%= InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ERROR %>'>
								<tr>
									<td class="top" colspan="2">
										<font class="error">
											<bean:message key="error.message.inquiries.current.attending.course.teacher.form" bundle="INQUIRIES_RESOURCES" />
										</font>
									</td>
								</tr>				
							</logic:present>

							<tr>
								<td class="left">
									3.1 <bean:message key="table.rowname.inquiries.teacher.form.class.type" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<ul class="schoolClassType">								
									<logic:iterate id="remainingClassType" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.remainingClassTypes">
										<li>
											<html:multibox property="currentAttendingCourseTeacherClassType">
												<bean:write name="remainingClassType" property="tipo" />
											</html:multibox>
											<bean:write name="remainingClassType" property="fullNameTipoAula" />
										</li>
									</logic:iterate>
									<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes">
										<li>
											<html:multibox property="currentAttendingCourseTeacherClassType">
												<bean:write name="classType" property="tipo" />
											</html:multibox>
											<bean:write name="classType" property="fullNameTipoAula" />
										</li>
									</logic:iterate>
									</ul>
								</td>
							</tr>
						
							<tr>
								<td class="left">
									3.2 <bean:message key="table.rowname.inquiries.teacher.form.teacher.name" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="left">
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
										<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.teacherNumber" />
										-
										<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.infoPerson.nome" />
									</logic:present>
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
										<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher.name" />
									</logic:present>
								</td>
							</tr>

							<tr>
								<td class="top" colspan="2">
									<bean:message key="message.inquiries.how.evaluate" bundle="INQUIRIES_RESOURCES"/>
								</td>
							</tr>				

							<tr>
								<td class="left">
									3.3 <bean:message key="table.rowname.inquiries.teacher.form.student.assiduity" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="<50%,50%-75%,75%-85%,85%-95%,95%-100%" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
												<bean:define id="i">
													<c:out value="${int}"/>
												</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion33" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.4 <bean:message key="table.rowname.inquiries.teacher.form.teacher.assiduity" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="<50%,50%-75%,75%-85%,85%-95%,95%-100%" delims="," var="int">
											<td>
												<c:out value="${int}"/>
											</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion34" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.5 <bean:message key="table.rowname.inquiries.teacher.form.teacher.punctuality" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
												<bean:define id="i">
													<c:out value="${int}"/>
												</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion35" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.6 <bean:message key="table.rowname.inquiries.teacher.form.teacher.clarity" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion36" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.7 <bean:message key="table.rowname.inquiries.teacher.form.teacher.assurance" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion37" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.8 <bean:message key="table.rowname.inquiries.teacher.form.teacher.interest.stimulation" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion38" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.9 <bean:message key="table.rowname.inquiries.teacher.form.teacher.availability" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion39" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.10 <bean:message key="table.rowname.inquiries.teacher.form.teacher.reasoning.stimulation" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion310" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									3.11 <bean:message key="table.rowname.inquiries.teacher.form.teacher.global.appreciation" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1, ,2, ,3, ,4, ,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseTeacherQuestion311" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									<html:submit styleClass="inputbutton" onclick='<%="this.form.method.value='closeTeacher';" %>'>
										<bean:message key="button.confirm" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='", teacherPosition.toString(), "';") %>'>
										<bean:message key="button.remove" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
								<td class="left"></td>
							</tr>
						</table>

					<%
					} else {
					%>
						<table>
							<tr>
								<td class="top" colspan="2">
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
										<strong>
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.teacherNumber" />
											-
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.infoPerson.nome" />
										</strong>
									</logic:present>
									<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
										<strong>
											<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher.name" />
										</strong>
									</logic:present>
								</td>
							</tr>
							<tr>
								<td class="left">
									<bean:message key="table.rowname.inquiries.teacher.form.class.type" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="left">
									<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes" indexId="index">
										<c:if test="${index != 0}">
											,&nbsp;
										</c:if>
										<bean:write name="classType" property="fullNameTipoAula" />
									</logic:iterate>
								</td>
							</tr>
							<tr>
								<td class="left">
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='editTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='", teacherPosition.toString(), "';") %>'>
										<bean:message key="button.edit" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='", teacherPosition.toString(),  "';") %>'>
										<bean:message key="button.remove" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
							</tr>
						</table>

					<%
					}
					%>
		
		
				</logic:iterate>
				
			</logic:present>

			<p align="center">
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>


		</div>


<%--


<div class="block">
<h3>Informa&ccedil;&atilde;o</h3>
<p>Os campos m&iacute;nimos necess&aacute;rios j&aacute; foram preenchidos, no entanto pode continuar a avalia&ccedil;&atilde;o de docentes ou passar &agrave; avalia&ccedil;&atilde;o de salas</p>
</div>

--%>


		<div id='<%= InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR  %>' class="block">
			<h2>
				4. <bean:message key="header.inquiries.rooms.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>
			
			<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_ROOMS %>'>
				<html:hidden property="newAttendingCourseRoomId" />
				<bean:define id="evaluateRoom">
					<bean:message key="message.inquiries.anchor.title.evaluate.room" bundle="INQUIRIES_RESOURCES"/>
				</bean:define>

				<div class="block2">
					<p><bean:message key="title.inquiries.choose.room" bundle="INQUIRIES_RESOURCES"/></p>
					<ul>
						<logic:iterate id="attendingCourseRoom" name='<%= InquiriesUtil.ATTENDING_COURSE_ROOMS %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoRoomWithInquiryRoomFlag">
							<bean:define id="currentRoomId" name="attendingCourseRoom" property="idInternal"/>
								<li>
									<c:if test="${!attendingCourseRoom.alreadyEvaluatedFlag}">
										<html:submit styleClass="inputbuttonlink"
											onmouseover="this.style.textDecoration='none';"
											onmouseout="this.style.textDecoration='underline';"
											onclick='<%= StringAppender.append("this.form.method.value='prepareNewRoom';this.form.newAttendingCourseRoomId.value='", currentRoomId.toString(), "';") %>'
											title='<%= evaluateRoom %>'
											>
											<bean:write name="attendingCourseRoom" property="nome" />
										</html:submit>
									</c:if>
									<c:if test="${attendingCourseRoom.alreadyEvaluatedFlag}">
										<bean:write name="attendingCourseRoom" property="nome" />
									</c:if>
								</li>
						</logic:iterate>
					</ul>
				</div>
			</logic:present>

			<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>'>
				<html:hidden property="selectedAttendingCourseRoomId" />

				<logic:iterate id="selectedAttendingCourseRoom" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>' indexId="roomPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom">
					<bean:define id="selectedAttendingCourseRoomId" name="selectedAttendingCourseRoom" property="room.idInternal" />

					<html:hidden property="selectedAttendingCourseRoomsId" value='<%= selectedAttendingCourseRoomId.toString() %>'/>
					<html:hidden property="selectedAttendingCourseRoomsQuestion41" value='<%= "" + selectedAttendingCourseRoom.getSpaceAdequation() %>' />
					<html:hidden property="selectedAttendingCourseRoomsQuestion42" value='<%= "" + selectedAttendingCourseRoom.getEnvironmentalConditions() %>' />
					<html:hidden property="selectedAttendingCourseRoomsQuestion43" value='<%= "" + selectedAttendingCourseRoom.getEquipmentQuality() %>' />
		
					<c:if test="${selectedAttendingCourseRoomId == currentAttendingCourseRoom.room.idInternal}">
						<html:hidden property="currentAttendingCourseRoomId" value='<%= selectedAttendingCourseRoomId.toString() %>'/>
						<table id='<%= InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ANCHOR  %>'>
							<tr>
								<td class="top" colspan="2">
									<strong>
										<bean:write name="selectedAttendingCourseRoom" property="room.nome" />
									</strong>
								</td>
							</tr>
		
							<logic:present name='<%= InquiriesUtil.CURRENT_ATTENDING_COURSE_ROOM_FORM_ERROR %>'>
								<tr>
									<td class="top" colspan="2">
										<font class="error">
											<bean:message key="error.message.inquiries.current.attending.course.room.form" bundle="INQUIRIES_RESOURCES" />
										</font>
									</td>
								</tr>				
							</logic:present>

							<tr>
								<td class="top" colspan="2">
									<bean:message key="message.inquiries.how.evaluate" bundle="INQUIRIES_RESOURCES"/>
								</td>
							</tr>				

							<tr>
								<td class="left">
									4.1 <bean:message key="table.rowname.inquiries.room.form.space.adequation" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseRoomQuestion41" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									4.2 <bean:message key="table.rowname.inquiries.room.form.environmental.conditions" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseRoomQuestion42" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left">
									4.3 <bean:message key="table.rowname.inquiries.room.form.equipment.quality" bundle="INQUIRIES_RESOURCES"/>
								</td>
								<td class="right">
									<table class="radio">
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
												<td>
													<c:out value="${int}"/>
												</td>
											</c:forTokens>
										</tr>
										<tr>
											<c:forTokens items="1,2,3,4,5" delims="," var="int">
													<bean:define id="i">
														<c:out value="${int}"/>
													</bean:define>
												<td>
													<html:radio property="currentAttendingCourseRoomQuestion43" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr>
								<td class="left">
									<html:submit styleClass="inputbutton" onclick='<%="this.form.method.value='closeRoom';" %>'>
										<bean:message key="button.confirm" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='removeRoom';this.form.selectedAttendingCourseRoomId.value='", selectedAttendingCourseRoomId.toString(), "';") %>'>
										<bean:message key="button.remove" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
								<td class="left"></td>
							</tr>
						</table>
					</c:if>

					<c:if test="${selectedAttendingCourseRoomId != currentAttendingCourseRoom.room.idInternal}">
						<table>
							<tr>
								<td class="top" colspan="2">
									<strong>
								<bean:write name="selectedAttendingCourseRoom" property="room.nome" />
									</strong>
								</td>
							</tr>
							<tr>
								<td class="left">
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='editRoom';this.form.selectedAttendingCourseRoomId.value='", selectedAttendingCourseRoomId.toString(), "';") %>'>
										<bean:message key="button.edit" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit styleClass="inputbutton" onclick='<%= StringAppender.append("this.form.method.value='removeRoom';this.form.selectedAttendingCourseRoomId.value='", selectedAttendingCourseRoomId.toString(), "';") %>'>
										<bean:message key="button.remove" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
							</tr>
						</table>

					</c:if>

				</logic:iterate>
			</logic:present>

			<p align="center">
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>

			
		</div>	

		<hr/>

		<div id='<%= InquiriesUtil.SUBMIT_FORM_ANCHOR  %>' class="block2">
			<html:submit styleClass="inputbutton" onclick='<%="this.form.method.value='submitInquiry';" %>'>
				<bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/>
			</html:submit>
		</div>

	</html:form>
</div>

<logic:present name='<%= InquiriesUtil.ANCHOR %>'>
	<script>
		if(navigator.appName != "Microsoft Internet Explorer") {
			location.hash='<%= request.getAttribute(InquiriesUtil.ANCHOR) %>';
		}
	</script>	
</logic:present>
