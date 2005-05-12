<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.util.TipoAula" %>

<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" type="text/css" />

<noscript>
	<font class="error">
		<bean:message key="error.message.inquiries.javascript.disabled" bundle="INQUIRIES_RESOURCES"/>
	</font>
</noscript>

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

	<%---
	TEXTO AQUI!!!!
	<h3>CONFIRME OS DADOS ANTES DE CONFIRMAR A SUBMISS&Aacute;O</h3>
	--%>

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
		<html:hidden property="attendingCourseExecutionDegreeId"/>
		<html:hidden property="studentAttendsId"/>
		
		<html:hidden property="curricularYear" />
		<html:hidden property="attendingCourseSchoolClassId" />
		<html:hidden property="firstEnrollment" />
		<html:hidden property="executionCourseQuestion22" />
		<html:hidden property="executionCourseQuestion23" />
		<html:hidden property="executionCourseQuestion24" />
		<html:hidden property="executionCourseQuestion25" />
		<html:hidden property="executionCourseQuestion26" />
		<html:hidden property="executionCourseQuestion27" />
		<html:hidden property="executionCourseQuestion28" />

		<logic:present name='<%= InquiriesUtil.INFO_ATTENDING_INQUIRIES_COURSE %>'>
			<bean:define id="infoAttendingCourseInquiry" name='<%= InquiriesUtil.INFO_ATTENDING_INQUIRIES_COURSE %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse" />
		</logic:present>

		<div id='<%= InquiriesUtil.STUDENT_FORM_ANCHOR %>' class="block">
			<h2>
				1. <bean:message key="header.inquiries.student.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>
			<table>
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
						1.2 <bean:message key="table.rowname.inquiries.student.form.curricular.year" bundle="INQUIRIES_RESOURCES"/>
					</td>
					<td class="right">
						<table class="radio2">
							<tr>
								<td>
									<c:forTokens items="1,2,3,4,5" delims="," var="cYear">
										<bean:define id="year">
											<c:out value="${cYear}"/>
										</bean:define>
										<c:out value="${cYear}"/>&ordm;
										<c:if test="${year == infoAttendingCourseInquiry.studentCurricularYear}">
											<input type="radio" name="curricularYear" value='<%= year %>' checked="checked" />
										</c:if>
										<c:if test="${year != infoAttendingCourseInquiry.studentCurricularYear}">
											<input type="radio" name="curricularYear" value='<%= year %>' disabled="disabled"/>
										</c:if>
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
						<logic:present name="infoAttendingCourseInquiry" property="studentSchoolClass">
							<option>
								<bean:write name="infoAttendingCourseInquiry" property="studentSchoolClass.nome" />
							</option>
						</logic:present>
						<logic:notPresent name="infoAttendingCourseInquiry" property="studentSchoolClass">
							<option>
								<bean:message key="label.inquiries.no.answer" bundle="INQUIRIES_RESOURCES"/>
							</option>
						</logic:notPresent>
					</td>
				</tr>

				<tr>
					<td class="left">
						1.4 <bean:message key="table.rowname.inquiries.student.form.first.enrollment" bundle="INQUIRIES_RESOURCES"/>
					</td>
					<td class="right">
						<c:if test="${infoAttendingCourseInquiry.studentFirstEnrollment == 1}">
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.yes" bundle="INQUIRIES_RESOURCES"/>
							<input type="radio" name="firstEnrollment" value="true" checked="checked"/>
							
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
							<input type="radio" name="firstEnrollment" value="false" disabled="disabled"/>
						</c:if>
						<c:if test="${infoAttendingCourseInquiry.studentFirstEnrollment == 0}">
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.yes" bundle="INQUIRIES_RESOURCES"/>
							<input type="radio" name="firstEnrollment" value="true" disabled="disabled"/>
							
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
							<input type="radio" name="firstEnrollment" value="false" checked="checked"/>
						</c:if>
					</td>
				</tr>
			</table>
		</div>

		<logic:present name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>'>
			<div id='<%= InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>' class="block">
				<h2>
					2. <bean:message key="header.inquiries.course.form" bundle="INQUIRIES_RESOURCES"/>
				</h2>

				<table>
					<tr>
						<td class="left">
							2.1 <bean:message key="table.rowname.inquiries.course.form.course" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<bean:write name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>' property="nome" />
							<br/>
							<%--
							<bean:write name="infoAttendingCourseInquiry" property="executionDegreeCourse.infoDegreeCurricularPlan.infoDegree.sigla" />
							--%>
						</td>
					</tr>
				
				
					<tr>
						<td class="top" colspan="2">
							<bean:message key="message.inquiries.how.evaluated" bundle="INQUIRIES_RESOURCES"/>
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
											<c:if test="${i == infoAttendingCourseInquiry.classCoordination}">
												<input type="radio" name="executionCourseQuestion22" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.classCoordination}">
												<input type="radio" name="executionCourseQuestion22" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.studyElementsContribution}">
												<input type="radio" name="executionCourseQuestion23" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.studyElementsContribution}">
												<input type="radio" name="executionCourseQuestion23" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.previousKnowledgeArticulation}">
												<input type="radio" name="executionCourseQuestion24" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.previousKnowledgeArticulation}">
												<input type="radio" name="executionCourseQuestion24" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.contributionForGraduation}">
												<input type="radio" name="executionCourseQuestion25" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.contributionForGraduation}">
												<input type="radio" name="executionCourseQuestion25" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.evaluationMethodAdequation}">
												<input type="radio" name="executionCourseQuestion26" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.evaluationMethodAdequation}">
												<input type="radio" name="executionCourseQuestion26" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.weeklySpentHours}">
												<input type="radio" name="executionCourseQuestion27" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.weeklySpentHours}">
												<input type="radio" name="executionCourseQuestion27" value='<%= i %>' disabled="disabled"/>
											</c:if>
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
											<c:if test="${i == infoAttendingCourseInquiry.globalAppreciation}">
												<input type="radio" name="executionCourseQuestion28" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.globalAppreciation}">
												<input type="radio" name="executionCourseQuestion28" value='<%= i %>' disabled="disabled"/>
											</c:if>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</logic:present>

		<div id='<%= InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>' class="block">
			<h2>
				3. <bean:message key="header.inquiries.teachers.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>

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
	
					<table>
						<tr>
							<td class="left" colspan="2">
								<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
									<strong>
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
								3.1 <bean:message key="table.rowname.inquiries.teacher.form.class.type" bundle="INQUIRIES_RESOURCES"/>
							</td>
							<td class="right">
								<ul class="schoolClassType">								
								<logic:iterate id="remainingClassType" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.remainingClassTypes">
									<li>
										<bean:write name="remainingClassType" property="fullNameTipoAula" />
									</li>
								</logic:iterate>
								<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes">
									<li>
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
									<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher.infoPerson.nome" />
								</logic:present>
								<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
									<bean:write name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher.name" />
								</logic:present>
							</td>
						</tr>

						<tr>
							<td class="top" colspan="2">
									<bean:message key="message.inquiries.how.evaluated" bundle="INQUIRIES_RESOURCES"/>
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
												<c:if test="${i == selectedAttendingCourseTeacher.studentAssiduity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.studentAssiduity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherAssiduity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAssiduity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherPunctuality}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherPunctuality}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherClarity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherClarity}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherAssurance}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAssurance}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherInterestStimulation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherInterestStimulation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherAvailability}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAvailability}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.teacherReasoningStimulation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherReasoningStimulation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseTeacher.globalAppreciation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.globalAppreciation}">
													<input type="radio" name='<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
											</td>
										</c:forTokens>
									</tr>
								</table>
							</td>
						</tr>
					</table>
											
				</logic:iterate>
				
			</logic:present>
		</div>



		<div id='<%= InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR  %>' class="block">			
			<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>'>
				<logic:notEmpty name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>'>
					<h2>
						4. <bean:message key="header.inquiries.rooms.form" bundle="INQUIRIES_RESOURCES"/>
					</h2>
				</logic:notEmpty>
				<html:hidden property="selectedAttendingCourseRoomId" />

				<logic:iterate id="selectedAttendingCourseRoom" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>' indexId="roomPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom">
					<bean:define id="selectedAttendingCourseRoomId" name="selectedAttendingCourseRoom" property="room.idInternal" />

					<html:hidden property="selectedAttendingCourseRoomsId" value='<%= selectedAttendingCourseRoomId.toString() %>'/>
					<html:hidden property="selectedAttendingCourseRoomsQuestion41" value='<%= "" + selectedAttendingCourseRoom.getSpaceAdequation() %>' />
					<html:hidden property="selectedAttendingCourseRoomsQuestion42" value='<%= "" + selectedAttendingCourseRoom.getEnvironmentalConditions() %>' />
					<html:hidden property="selectedAttendingCourseRoomsQuestion43" value='<%= "" + selectedAttendingCourseRoom.getEquipmentQuality() %>' />
	
					<table>
						<tr>
							<td class="top" colspan="2">
								<strong>
									<bean:write name="selectedAttendingCourseRoom" property="room.nome" />
								</strong>
							</td>
						</tr>
						<tr>
							<td class="top" colspan="2">
								<bean:message key="message.inquiries.how.evaluated" bundle="INQUIRIES_RESOURCES"/>
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
												<c:if test="${i == selectedAttendingCourseRoom.spaceAdequation}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.spaceAdequation}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseRoom.environmentalConditions}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.environmentalConditions}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
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
												<c:if test="${i == selectedAttendingCourseRoom.equipmentQuality}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.equipmentQuality}">
													<input type="radio" name='<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
												</c:if>
											</td>
										</c:forTokens>
									</tr>
								</table>
							</td>
						</tr>
					</table>

			</logic:iterate>
		</logic:present>

		<p class="navbottom" />

		<div id="submit" style="text-align: right;">
			<p><Strong>
				<a name='<%= InquiriesUtil.SUBMIT_FORM_ANCHOR  %>'>
					<bean:message key="message.inquiries.confirmation.info" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</Strong></p>
			<html:submit styleClass="inquirylargebutton" onclick='<%="this.form.method.value='saveInquiry';" %>'>
				<bean:message key="button.inquiries.confirm" bundle="INQUIRIES_RESOURCES"/>
			</html:submit>
			<html:submit styleClass="inquirylargebutton" onclick='<%="this.form.method.value='editInquiry';" %>'>
				<bean:message key="button.inquiries.edit.inquiry" bundle="INQUIRIES_RESOURCES"/>
			</html:submit>
		</div>

	</html:form>
</div>
