<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiry" %>
<%@ page import="net.sourceforge.fenixedu.util.InquiriesUtil" %>
<%@ page import="net.sourceforge.fenixedu.domain.ShiftType" %>

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

	<p class="mbottom0"><strong>
		<bean:message key="message.inquiries.verify.data" bundle="INQUIRIES_RESOURCES"/>
	</strong></p>

	<html:form action="/fillInquiries">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="defaultMethod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendingExecutionCourseId" property="attendingExecutionCourseId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentExecutionDegreeId" property="studentExecutionDegreeId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendingCourseExecutionDegreeId" property="attendingCourseExecutionDegreeId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentAttendsId" property="studentAttendsId"/>

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYear" property="curricularYear" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendingCourseSchoolClassId" property="attendingCourseSchoolClassId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstEnrollment" property="firstEnrollment" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion21" property="executionCourseQuestion21" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion22" property="executionCourseQuestion22" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion23" property="executionCourseQuestion23" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion24" property="executionCourseQuestion24" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion25" property="executionCourseQuestion25" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion26" property="executionCourseQuestion26" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion27" property="executionCourseQuestion27" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseQuestion28" property="executionCourseQuestion28" />

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
											<input alt="input.curricularYear" type="radio" name="curricularYear" value='<%= year %>' checked="checked" />
										</c:if>
										<c:if test="${year != infoAttendingCourseInquiry.studentCurricularYear}">
											<input alt="input.curricularYear" type="radio" name="curricularYear" value='<%= year %>' disabled="disabled"/>
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
							<input alt="input.firstEnrollment" type="radio" name="firstEnrollment" value="true" checked="checked"/>
							
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
							<input alt="input.firstEnrollment" type="radio" name="firstEnrollment" value="false" disabled="disabled"/>
						</c:if>
						<c:if test="${infoAttendingCourseInquiry.studentFirstEnrollment == 0}">
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.yes" bundle="INQUIRIES_RESOURCES"/>
							<input alt="input.firstEnrollment" type="radio" name="firstEnrollment" value="true" disabled="disabled"/>
							
							<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
							<input alt="input.firstEnrollment" type="radio" name="firstEnrollment" value="false" checked="checked"/>
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
							2.0 <bean:message key="table.rowname.inquiries.course.form.course" bundle="INQUIRIES_RESOURCES"/>
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
							2.1 <bean:message key="table.rowname.inquiries.course.form.question21" bundle="INQUIRIES_RESOURCES"/>
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
											<c:if test="${i == infoAttendingCourseInquiry.question21}">
												<input alt="input.executionCourseQuestion21" type="radio" name="executionCourseQuestion21" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.question21}">
												<input alt="input.executionCourseQuestion21" type="radio" name="executionCourseQuestion21" value='<%= i %>' disabled="disabled"/>
											</c:if>
										</td>
									</c:forTokens>
								</tr>
							</table>
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
												<input alt="input.executionCourseQuestion22" type="radio" name="executionCourseQuestion22" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.classCoordination}">
												<input alt="input.executionCourseQuestion22" type="radio" name="executionCourseQuestion22" value='<%= i %>' disabled="disabled"/>
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
												<input alt="input.executionCourseQuestion23" type="radio" name="executionCourseQuestion23" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.studyElementsContribution}">
												<input alt="input.executionCourseQuestion23" type="radio" name="executionCourseQuestion23" value='<%= i %>' disabled="disabled"/>
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
												<input alt="input.executionCourseQuestion24" type="radio" name="executionCourseQuestion24" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.previousKnowledgeArticulation}">
												<input alt="input.executionCourseQuestion24" type="radio" name="executionCourseQuestion24" value='<%= i %>' disabled="disabled"/>
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
									<td>
										N&atilde;o Sabe
									</td>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,0.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<c:if test="${i == infoAttendingCourseInquiry.contributionForGraduation}">
												<input alt="input.executionCourseQuestion25" type="radio" name="executionCourseQuestion25" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.contributionForGraduation}">
												<input alt="input.executionCourseQuestion25" type="radio" name="executionCourseQuestion25" value='<%= i %>' disabled="disabled"/>
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
												<input alt="input.executionCourseQuestion26" type="radio" name="executionCourseQuestion26" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.evaluationMethodAdequation}">
												<input alt="input.executionCourseQuestion26" type="radio" name="executionCourseQuestion26" value='<%= i %>' disabled="disabled"/>
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
							<table class="radio2">
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
												<input alt="input.executionCourseQuestion27" type="radio" name="executionCourseQuestion27" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.weeklySpentHours}">
												<input alt="input.executionCourseQuestion27" type="radio" name="executionCourseQuestion27" value='<%= i %>' disabled="disabled"/>
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
												<input alt="input.executionCourseQuestion28" type="radio" name="executionCourseQuestion28" value='<%= i %>' checked="checked"/>
											</c:if>
											<c:if test="${i != infoAttendingCourseInquiry.globalAppreciation}">
												<input alt="input.executionCourseQuestion28" type="radio" name="executionCourseQuestion28" value='<%= i %>' disabled="disabled"/>
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
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherFormPosition" property="selectedAttendingCourseTeacherFormPosition" />

				<logic:iterate id="selectedAttendingCourseTeacher" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>' indexId="teacherPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher">
					<bean:define id="selectedAttendingCourseTeacherId" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.idInternal" />

					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersId" property="selectedAttendingCourseTeachersId" value='<%= selectedAttendingCourseTeacherId.toString() %>'/>
		            <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersClassTypeT" property="selectedAttendingCourseTeachersClassTypeT" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(ShiftType.TEORICA.name()) %>' />
            		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersClassTypeP" property="selectedAttendingCourseTeachersClassTypeP" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(ShiftType.PRATICA.name()) %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersClassTypeL" property="selectedAttendingCourseTeachersClassTypeL" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(ShiftType.LABORATORIAL.name()) %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersClassTypeTP" property="selectedAttendingCourseTeachersClassTypeTP" value='<%= "" + selectedAttendingCourseTeacher.hasClassType(ShiftType.TEORICO_PRATICA.name()) %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion33" property="selectedAttendingCourseTeachersQuestion33" value='<%= "" + selectedAttendingCourseTeacher.getStudentAssiduity() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion34" property="selectedAttendingCourseTeachersQuestion34" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAssiduity() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion35" property="selectedAttendingCourseTeachersQuestion35" value='<%= "" + selectedAttendingCourseTeacher.getTeacherPunctuality() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion36" property="selectedAttendingCourseTeachersQuestion36" value='<%= "" + selectedAttendingCourseTeacher.getTeacherClarity() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion37" property="selectedAttendingCourseTeachersQuestion37" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAssurance() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion38" property="selectedAttendingCourseTeachersQuestion38" value='<%= "" + selectedAttendingCourseTeacher.getTeacherInterestStimulation() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion39" property="selectedAttendingCourseTeachersQuestion39" value='<%= "" + selectedAttendingCourseTeacher.getTeacherAvailability() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion310" property="selectedAttendingCourseTeachersQuestion310" value='<%= "" + selectedAttendingCourseTeacher.getTeacherReasoningStimulation() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersQuestion311" property="selectedAttendingCourseTeachersQuestion311" value='<%= "" + selectedAttendingCourseTeacher.getGlobalAppreciation() %>' />
	
					<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.teacher">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherIsAffiliated" property="selectedAttendingCourseTeacherIsAffiliated" value="true"/>
					</logic:present>
					<logic:present name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.nonAffiliatedTeacher">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherIsAffiliated" property="selectedAttendingCourseTeacherIsAffiliated" value="false"/>
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
								3.1 <bean:message key="table.rowname.inquiries.teacher.form.evaluated.class.types" bundle="INQUIRIES_RESOURCES"/>
							</td>
							<td class="right">
								<ul class="schoolClassType">								
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
								<br/><bean:message key="table.rowname.inquiries.teacher.form.student.assiduity.less.50.message" bundle="INQUIRIES_RESOURCES"/>
							</td>
							<td class="right">
								<table class="radio2">
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.studentAssiduity}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion33" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
								<table class="radio2">
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAssiduity}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion34" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherPunctuality}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion35" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherClarity}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion36" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAssurance}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion37" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherInterestStimulation}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion38" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherAvailability}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion39" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.teacherReasoningStimulation}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion310" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
													<input alt="<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseTeacher.globalAppreciation}">
													<input alt="<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>" type="radio" name='<%= "currentAttendingCourseTeacherQuestion311" + teacherPosition %>' value='<%= i %>' disabled="disabled"/>
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
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomId" property="selectedAttendingCourseRoomId" />

				<logic:iterate id="selectedAttendingCourseRoom" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>' indexId="roomPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom">
					<bean:define id="selectedAttendingCourseRoomId" name="selectedAttendingCourseRoom" property="room.idInternal" />

					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsId" property="selectedAttendingCourseRoomsId" value='<%= selectedAttendingCourseRoomId.toString() %>'/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion41" property="selectedAttendingCourseRoomsQuestion41" value='<%= "" + selectedAttendingCourseRoom.getSpaceAdequation() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion42" property="selectedAttendingCourseRoomsQuestion42" value='<%= "" + selectedAttendingCourseRoom.getEnvironmentalConditions() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion43" property="selectedAttendingCourseRoomsQuestion43" value='<%= "" + selectedAttendingCourseRoom.getEquipmentQuality() %>' />
	
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
								<table class="radio2">
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
													<input alt="<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.spaceAdequation}">
													<input alt="<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion41" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
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
								<table class="radio2">
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
													<input alt="<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.environmentalConditions}">
													<input alt="<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion42" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
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
								<table class="radio2">
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
													<input alt="<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>' value='<%= i %>' checked="checked"/>
												</c:if>
												<c:if test="${i != selectedAttendingCourseRoom.equipmentQuality}">
													<input alt="<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>" type="radio" name='<%= "currentAttendingCourseRoomQuestion43" + roomPosition %>' value='<%= i %>' disabled="disabled"/>
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

			<p class="navbottom">&nbsp;</p>
		</div>


		<div id="submit">
			<p>
				<strong>
					<bean:message key="message.inquiries.edit.info" bundle="INQUIRIES_RESOURCES"/>
				</strong>
			</p>
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirylargebutton" onclick='<%="this.form.method.value='editInquiry';" %>'>
					<bean:message key="button.inquiries.edit.inquiry" bundle="INQUIRIES_RESOURCES"/>
				</html:submit>
			</p>
			<p>
				<strong>
					<bean:message key="message.inquiries.definitive.submition.info" bundle="INQUIRIES_RESOURCES"/>
				</strong>
			</p>
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirylargebutton" onclick='<%="this.form.method.value='saveInquiry';" %>'>
					<bean:message key="button.inquiries.submit" bundle="INQUIRIES_RESOURCES"/>
				</html:submit>
			</p>
		</div>

	</html:form>
</div>
