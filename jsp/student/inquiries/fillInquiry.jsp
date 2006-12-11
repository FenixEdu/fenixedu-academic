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

<logic:present name='<%= InquiriesUtil.ANCHOR %>'>
	<script type="text/javascript">
		if(navigator.appName=="Microsoft Internet Explorer") {
			location.hash='<%= request.getAttribute(InquiriesUtil.ANCHOR) %>';
		}
	</script>	
</logic:present>


<div id="inquiry">

	<p>
		<em><bean:message key="title.inquiries.GEP" bundle="INQUIRIES_RESOURCES"/></em>
	</p>
	<h2>
		<bean:message key="title.inquiries.course.evaluation" bundle="INQUIRIES_RESOURCES"/>
	</h2>
	<h3>
		<bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/>: <span class="bluetxt"><bean:write name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>' property="nome" /></span>
	</h3>

<!-- 
	<p><span class="bluetxt"><bean:write name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>' property="nome" /></span></p>
-->
	
	<div class="infoop2">
	<bean:message key="message.inquiries.instructions" bundle="INQUIRIES_RESOURCES"/>
	</div>
	
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
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="defaultMethod" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendingExecutionCourseId" property="attendingExecutionCourseId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentExecutionDegreeId" property="studentExecutionDegreeId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentAttendsId" property="studentAttendsId" />
				
		<div id='<%= InquiriesUtil.STUDENT_FORM_ANCHOR %>' class="block">
			<h2>
				1. <bean:message key="header.inquiries.student.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>
			<bean:message key="message.inquiries.mandatory.answers" bundle="INQUIRIES_RESOURCES"/>

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
						1.2 <bean:message key="table.rowname.inquiries.student.form.curricular.year" bundle="INQUIRIES_RESOURCES"/> <span class="redtext">*</span>
					</td>
					<td class="right">
						<table class="radio2">
							<tr>
								<td>
									<c:forTokens items="1,2,3,4,5" delims="," var="cYear">
										<bean:define id="year">
											<c:out value="${cYear}"/>
										</bean:define>
										<c:out value="${cYear}"/>&ordm;<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.curricularYear" property="curricularYear" value='<%= year %>'/>
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
						1.4 <bean:message key="table.rowname.inquiries.student.form.first.enrollment" bundle="INQUIRIES_RESOURCES"/> <span class="redtext">*</span>
					</td>
					<td class="right">
						<bean:message key="table.rowname.inquiries.student.form.first.enrollment.yes" bundle="INQUIRIES_RESOURCES"/>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.firstEnrollment" property="firstEnrollment" value="true"/>
						
						<bean:message key="table.rowname.inquiries.student.form.first.enrollment.no" bundle="INQUIRIES_RESOURCES"/>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.firstEnrollment" property="firstEnrollment" value="false"/>
					</td>
				</tr>

			</table>

			<%--
			<p align="center">
				1.
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				2.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				3.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				4.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>
			--%>

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
							2.0 <bean:message key="table.rowname.inquiries.course.form.course" bundle="INQUIRIES_RESOURCES"/>
						</td>
						<td class="right">
							<bean:write name='<%= InquiriesUtil.ATTENDING_EXECUTION_COURSE %>' property="nome" />
							<br/>
							<logic:notPresent name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>'>
								<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES %>'>
									<html:select bundle="HTMLALT_RESOURCES" altKey="select.attendingCourseExecutionDegreeId" property="attendingCourseExecutionDegreeId">
										<html:options property="idInternal" 
													  labelProperty="infoDegreeCurricularPlan.infoDegree.sigla" 
													  collection='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREES %>' />
									</html:select>
								</logic:present>
							</logic:notPresent>
							<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>'>
								<bean:define id="attendingCourseExecutionDegreeId" name='<%= InquiriesUtil.ATTENDING_COURSE_EXECUTION_DEGREE %>' property="idInternal" />
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.attendingCourseExecutionDegreeId" property="attendingCourseExecutionDegreeId" value='<%= attendingCourseExecutionDegreeId.toString() %>' />
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
							2.1 <bean:message key="table.rowname.inquiries.course.form.onlineInfo" bundle="INQUIRIES_RESOURCES"/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion21" property="executionCourseQuestion21" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion22" property="executionCourseQuestion22" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion23" property="executionCourseQuestion23" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion24" property="executionCourseQuestion24" value='<%= i %>'/>
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
										<bean:message key="label.inquiries.unknown" bundle="INQUIRIES_RESOURCES"/>
									</td>
								</tr>
								<tr>
									<c:forTokens items="1.0,1.5,2.0,2.5,3.0,3.5,4.0,4.5,5.0,0.0" delims="," var="int">
											<bean:define id="i">
												<c:out value="${int}"/>
											</bean:define>
										<td>
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion25" property="executionCourseQuestion25" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion26" property="executionCourseQuestion26" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion27" property="executionCourseQuestion27" value='<%= i %>'/>
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
											<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseQuestion28" property="executionCourseQuestion28" value='<%= i %>'/>
										</td>
									</c:forTokens>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				<%--
				<p align="center">
					1.
					<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					2.
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
						<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					3.
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
					</a>
					&nbsp;|&nbsp;
					4.
					<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
						<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
					</a>
				</p>
				--%>
	
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

			<%
			Integer selectedTeacherFormPosition = 0;
			%>

			<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_TEACHERS %>'>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newAttendingCourseTeacherId" property="newAttendingCourseTeacherId" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newAttendingCourseNonAffiliatedTeacherId" property="newAttendingCourseNonAffiliatedTeacherId" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherFormPosition" property="selectedAttendingCourseTeacherFormPosition" />

				<bean:define id="evaluateTeacher">
					<bean:message key="message.inquiries.anchor.title.evaluate.teacher" bundle="INQUIRIES_RESOURCES"/>
				</bean:define>

				<div class="block2">
					<bean:define id="evaluateButtonValue">
						<bean:message key="button.inquiries.evaluate" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>
					<bean:define id="evaluateButtonTitle">
						<bean:message key="button.title.inquiries.new.teacher.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>

					<bean:define id="editButtonValue">
						<bean:message key="button.inquiries.edit.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>
					<bean:define id="editButtonTitle">
						<bean:message key="button.title.inquiries.edit.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>

					<bean:define id="removeButtonValue">
						<bean:message key="button.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>
					<bean:define id="removeButtonTitle">
						<bean:message key="button.title.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>

					<p><bean:message key="title.inquiries.choose.teacher" bundle="INQUIRIES_RESOURCES"/></p>
					<ul>
						<logic:iterate id="attendingCourseTeacher" name='<%= InquiriesUtil.ATTENDING_COURSE_TEACHERS %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes">
							<logic:present name="attendingCourseTeacher" property="teacher">
								<bean:define id="currentTeacherId" name="attendingCourseTeacher" property="idInternal"/>
								<li class="border">									
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled" value='<%= evaluateButtonValue %>' />
									</logic:empty>
									
									<logic:notEmpty name="attendingCourseTeacher" property="remainingClassTypes">
										<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton"
											onclick='<%="this.form.method.value='prepareNewTeacher';this.form.newAttendingCourseTeacherId.value='" + currentTeacherId + "';" %>'
											value='<%= evaluateButtonValue %>' title='<%= evaluateButtonTitle %>' />
									</logic:notEmpty>
									&gt;
									<bean:write name="attendingCourseTeacher" property="teacher.infoPerson.nome" />
									
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<c:if test="${completeAttendingCourseTeacherId == currentTeacherId}">
											<font class="error"><bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/></font>
										</c:if>
										<%--
										<c:if test="${completeAttendingCourseTeacherId != currentTeacherId}">
											<strong>
												<bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/>
											</strong>
										</c:if>
										--%>
									</logic:empty>
		
									<c:if test="${attendingCourseTeacher.hasEvaluations}">
										<table class="eval">
											<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>'>
									
												<bean:define id="evaluatedClassTypesRowName">
													<bean:message key="table.rowname.inquiries.teacher.form.evaluated.class.types" bundle="INQUIRIES_RESOURCES"/>
												</bean:define>
												
												<logic:iterate id="selectedAttendingCourseTeacher" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>' indexId="teacherPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher">
													<bean:define id="selectedAttendingCourseTeacherId" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.idInternal" />
										
													<%
																		
													Integer currentAttendingCourseTeacherFormPosition =
														(Integer) request.getAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION);
													
													if((selectedAttendingCourseTeacherId == currentTeacherId) &&
														!((currentAttendingCourseTeacherFormPosition != null) &&
														   (currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition))) {
													%>
														<tr>
															<td class="evaltype">
																<bean:write name="evaluatedClassTypesRowName" />
																<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes" indexId="index">
																	<c:if test="${index != 0}">
																		,&nbsp;
																	</c:if>
																	<strong>
																		<bean:write name="classType" property="fullNameTipoAula" />
																	</strong>
																</logic:iterate>
															</td>
										
															<td>
																<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" value='<%= editButtonValue %>' title='<%= editButtonTitle %>' 
																	onclick='<%="this.form.method.value='editTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + selectedTeacherFormPosition + "';" %>' />
																	
																<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" value='<%= removeButtonValue %>' title='<%= removeButtonTitle %>' 
																	onclick='<%="this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + selectedTeacherFormPosition + "';" %>'/>
															</td>
														</tr>
														<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersId" property="selectedAttendingCourseTeachersId" value='<%= "" + selectedAttendingCourseTeacherId %>'/>
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
														<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherIsAffiliated" property="selectedAttendingCourseTeacherIsAffiliated" value="true"/>
									
													<%
													selectedTeacherFormPosition++;
													} else if((selectedAttendingCourseTeacherId == currentTeacherId) &&
																((currentAttendingCourseTeacherFormPosition != null) &&
																	(currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition))) {
													%>
														<logic:notEmpty name="selectedAttendingCourseTeacher" property="classTypes">
															<tr>
																<td class="evaltypeedit">
																	<bean:write name="evaluatedClassTypesRowName" />
																	<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes" indexId="index">
																		<c:if test="${index != 0}">
																			,&nbsp;
																		</c:if>
																		<strong>
																			<bean:write name="classType" property="fullNameTipoAula" />
																		</strong>
																	</logic:iterate>
																</td>
											
																<td>
																	<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled"
																		value='<%= editButtonValue %>' >
																		
																	<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled"
																		value='<%= removeButtonValue %>' >
																</td>
															</tr>
														</logic:notEmpty>
													<%   	
													}
													%>
												</logic:iterate>
											</logic:present>
										</table>
									</c:if>
								</li>
							</logic:present>

							<%-- NON AFFILITED TEACHER --%>
							<logic:present name="attendingCourseTeacher" property="nonAffiliatedTeacher">
								<bean:define id="currentTeacherId" name="attendingCourseTeacher" property="idInternal"/>
								<li class="border">
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled" value='<%= evaluateButtonValue %>' />
									</logic:empty>

									<logic:notEmpty name="attendingCourseTeacher" property="remainingClassTypes">
										<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton"
											onclick='<%="this.form.method.value='prepareNewTeacher';this.form.newAttendingCourseNonAffiliatedTeacherId.value='" + currentTeacherId + "';" %>'
											value='<%= evaluateButtonValue %>' title='<%= evaluateButtonTitle %>' />
									</logic:notEmpty>
									&gt;
									<bean:write name="attendingCourseTeacher" property="nonAffiliatedTeacher.name" />
									
									<logic:empty name="attendingCourseTeacher" property="remainingClassTypes">
										<c:if test="${completeAttendingCourseNonAffiliatedTeacherId == currentTeacherId}">
											<font class="error"><bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/></font>
										</c:if>
										<%--
										<c:if test="${completeAttendingCourseNonAffiliatedTeacherId != currentTeacherId}">
											<strong>
												<bean:message key="message.inquiries.complete" bundle="INQUIRIES_RESOURCES"/>
											</strong>
										</c:if>
										--%>
									</logic:empty>
									
									<c:if test="${attendingCourseTeacher.hasEvaluations}">
										<table class="eval">
											<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>'>
								
												<bean:define id="evaluatedClassTypesRowName">
													<bean:message key="table.rowname.inquiries.teacher.form.evaluated.class.types" bundle="INQUIRIES_RESOURCES"/>
												</bean:define>
												
												<logic:iterate id="selectedAttendingCourseTeacher" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>' indexId="teacherPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher">
													<bean:define id="selectedAttendingCourseTeacherId" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.idInternal" />
										
													<%
																		
													Integer currentAttendingCourseTeacherFormPosition =
														(Integer) request.getAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION);
													
													if((selectedAttendingCourseTeacherId == currentTeacherId) &&
														!((currentAttendingCourseTeacherFormPosition != null) &&
														   (currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition))) {
													%>
														<tr>
															<td class="evaltype">
																<bean:write name="evaluatedClassTypesRowName" />
																<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes" indexId="index">
																	<c:if test="${index != 0}">
																		,&nbsp;
																	</c:if>
																	<strong>
																		<bean:write name="classType" property="fullNameTipoAula" />
																	</strong>
																</logic:iterate>
															</td>
										
															<td>
																<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" value='<%= editButtonValue %>' title='<%= editButtonTitle %>' 
																	onclick='<%="this.form.method.value='editTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + selectedTeacherFormPosition + "';" %>' />
																	
																<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" value='<%= removeButtonValue %>' title='<%= removeButtonTitle %>' 
																	onclick='<%="this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + selectedTeacherFormPosition + "';" %>'/>
															</td>
														</tr>
														<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersId" property="selectedAttendingCourseTeachersId" value='<%= "" + selectedAttendingCourseTeacherId %>'/>
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
														<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeacherIsAffiliated" property="selectedAttendingCourseTeacherIsAffiliated" value="false"/>
			
								
													<%
													selectedTeacherFormPosition++;
													} else if((selectedAttendingCourseTeacherId == currentTeacherId) &&
																((currentAttendingCourseTeacherFormPosition != null) &&
																	(currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition))) {
													%>
														<logic:notEmpty name="selectedAttendingCourseTeacher" property="classTypes">
															<tr>
																<td class="evaltypeedit">
																	<bean:write name="evaluatedClassTypesRowName" />
																	<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes" indexId="index">
																		<c:if test="${index != 0}">
																			,&nbsp;
																		</c:if>
																		<strong>
																			<bean:write name="classType" property="fullNameTipoAula" />
																		</strong>
																	</logic:iterate>
																</td>
											
																<td>
																	<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled"
																		value='<%= editButtonValue %>' >
																		
																	<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled"
																		value='<%= removeButtonValue %>' >
																</td>
															</tr>
														</logic:notEmpty>
													<%   	
													}
													%>
												</logic:iterate>
											</logic:present>
										</table>
									</c:if>
								</li>

							</logic:present>
						</logic:iterate>
					</ul>
				</div>
			</logic:present>

			<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>'>

				<logic:iterate id="selectedAttendingCourseTeacher" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_TEACHERS %>' indexId="teacherPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher">
					<bean:define id="selectedAttendingCourseTeacherId" name="selectedAttendingCourseTeacher" property="teacherOrNonAffiliatedTeacher.idInternal" />

								
		
					<%
										
					Integer currentAttendingCourseTeacherFormPosition =
						(Integer) request.getAttribute(InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_POSITION);
					
					if((currentAttendingCourseTeacherFormPosition != null) &&
					   (currentAttendingCourseTeacherFormPosition.intValue() == teacherPosition)) {
					%>
						
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentAttendingCourseTeacherFormPosition" property="currentAttendingCourseTeacherFormPosition" value='<%= "" + selectedTeacherFormPosition %>' />
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseTeachersId" property="selectedAttendingCourseTeachersId" value='<%= "" + selectedAttendingCourseTeacherId %>'/>
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
						
						<table id='<%= InquiriesUtil.CURRENT_ATTENDING_COURSE_TEACHER_FORM_ANCHOR  %>'>
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
											<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.currentAttendingCourseTeacherClassType" property="currentAttendingCourseTeacherClassType">
												<bean:write name="remainingClassType" property="name" />
											</html:multibox>
											<bean:write name="remainingClassType" property="fullNameTipoAula" />
										</li>
									</logic:iterate>
									<logic:iterate id="classType" name="selectedAttendingCourseTeacher" property="classTypes">
										<li>
											<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.currentAttendingCourseTeacherClassType" property="currentAttendingCourseTeacherClassType">
												<bean:write name="classType" property="name" />
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion33" property="currentAttendingCourseTeacherQuestion33" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion34" property="currentAttendingCourseTeacherQuestion34" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion35" property="currentAttendingCourseTeacherQuestion35" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion36" property="currentAttendingCourseTeacherQuestion36" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion37" property="currentAttendingCourseTeacherQuestion37" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion38" property="currentAttendingCourseTeacherQuestion38" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion39" property="currentAttendingCourseTeacherQuestion39" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion310" property="currentAttendingCourseTeacherQuestion310" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseTeacherQuestion311" property="currentAttendingCourseTeacherQuestion311" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>

							<tr>
								<td class="left" colspan="2">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='closeTeacher';" %>'>
										<bean:message key="button.inquiries.confirm.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + selectedTeacherFormPosition + "';" %>'>
										<bean:message key="button.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
							</tr>
						</table>

					<%
					} else {
					%>
						<%--
						<table>
							<tr>
								<td class="top" colspan="2">
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
									<bean:message key="table.rowname.inquiries.teacher.form.evaluated.class.types" bundle="INQUIRIES_RESOURCES"/>
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
								<td class="left" colspan="2">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='editTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + teacherPosition + "';" %>'>
										<bean:message key="button.inquiries.edit.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='removeTeacher';this.form.selectedAttendingCourseTeacherFormPosition.value='" + teacherPosition + "';" %>'>
										<bean:message key="button.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
							</tr>
						</table>
						--%>
					<%
					}
					%>
		
		
				</logic:iterate>
				
			</logic:present>

			<%--
			<p align="center">
				1.
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				2.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				3.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				4.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>
			--%>


		</div>



		<div id='<%= InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR  %>' class="block">
			<h2>
				4. <bean:message key="header.inquiries.rooms.form" bundle="INQUIRIES_RESOURCES"/>
			</h2>
			
			<logic:present name='<%= InquiriesUtil.ATTENDING_COURSE_ROOMS %>'>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newAttendingCourseRoomId" property="newAttendingCourseRoomId" />
				<bean:define id="evaluateRoom">
					<bean:message key="message.inquiries.anchor.title.evaluate.room" bundle="INQUIRIES_RESOURCES"/>
				</bean:define>

				<div class="block2">
					<bean:define id="buttonValue">
						<bean:message key="button.inquiries.evaluate" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>
					<bean:define id="buttonTitle">
						<bean:message key="button.title.inquiries.new.room.evaluation" bundle="INQUIRIES_RESOURCES"/>
					</bean:define>
					
					<p><bean:message key="title.inquiries.choose.room" bundle="INQUIRIES_RESOURCES"/></p>
					<ul>
						<logic:iterate id="attendingCourseRoom" name='<%= InquiriesUtil.ATTENDING_COURSE_ROOMS %>' type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoRoomWithInfoInquiriesRoom">
							<bean:define id="currentRoomId" name="attendingCourseRoom" property="idInternal"/>
							<li>
								<logic:notPresent name="attendingCourseRoom" property="inquiriesRoom">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton"
										onclick='<%="this.form.method.value='prepareNewRoom';this.form.newAttendingCourseRoomId.value='" + currentRoomId + "';" %>'
										value='<%= buttonValue %>' title='<%= buttonTitle %>'/>
									&gt;
								</logic:notPresent>
								<logic:present name="attendingCourseRoom" property="inquiriesRoom">
									<input alt="input.input" type="submit" class="inquirybutton" disabled="disabled" value='<%= buttonValue %>' />
									&gt;
								</logic:present>
								<bean:message key="label.inquiries.room" bundle="INQUIRIES_RESOURCES"/>
								<bean:write name="attendingCourseRoom" property="nome" />

								<%--c:if test="${attendingCourseRoom.alreadyEvaluatedFlag}">
									<bean:write name="attendingCourseRoom" property="nome" />
								</c:if--%>
							</li>
					</logic:iterate>
					</ul>
				</div>
			</logic:present>

			<logic:present name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>'>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomId" property="selectedAttendingCourseRoomId" />

				<logic:iterate id="selectedAttendingCourseRoom" name='<%= InquiriesUtil.SELECTED_ATTENDING_COURSE_ROOMS %>' indexId="roomPosition" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom">
					<bean:define id="selectedAttendingCourseRoomId" name="selectedAttendingCourseRoom" property="room.idInternal" />

					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsId" property="selectedAttendingCourseRoomsId" value='<%= "" + selectedAttendingCourseRoomId %>'/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion41" property="selectedAttendingCourseRoomsQuestion41" value='<%= "" + selectedAttendingCourseRoom.getSpaceAdequation() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion42" property="selectedAttendingCourseRoomsQuestion42" value='<%= "" + selectedAttendingCourseRoom.getEnvironmentalConditions() %>' />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedAttendingCourseRoomsQuestion43" property="selectedAttendingCourseRoomsQuestion43" value='<%= "" + selectedAttendingCourseRoom.getEquipmentQuality() %>' />
		
					<c:if test="${selectedAttendingCourseRoomId == currentAttendingCourseRoom.room.idInternal}">
						<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.currentAttendingCourseRoomId" property="currentAttendingCourseRoomId" value='<%= selectedAttendingCourseRoomId.toString() %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseRoomQuestion41" property="currentAttendingCourseRoomQuestion41" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseRoomQuestion42" property="currentAttendingCourseRoomQuestion42" value='<%= i %>'/>
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
													<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.currentAttendingCourseRoomQuestion43" property="currentAttendingCourseRoomQuestion43" value='<%= i %>'/>
												</td>
											</c:forTokens>
										</tr>
									</table>
								</td>
							</tr>
							
							<tr>
								<td class="left" colspan="2">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='closeRoom';" %>'>
										<bean:message key="button.inquiries.confirm.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='removeRoom';this.form.selectedAttendingCourseRoomId.value='" + selectedAttendingCourseRoomId + "';" %>'>
										<bean:message key="button.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
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
								<td class="left" colspan="2">
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='editRoom';this.form.selectedAttendingCourseRoomId.value='" + selectedAttendingCourseRoomId + "';" %>'>
										<bean:message key="button.inquiries.edit.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
									<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirybutton" onclick='<%="this.form.method.value='removeRoom';this.form.selectedAttendingCourseRoomId.value='" + selectedAttendingCourseRoomId + "';" %>'>
										<bean:message key="button.inquiries.remove.evaluation" bundle="INQUIRIES_RESOURCES"/>
									</html:submit>
								</td>
							</tr>
						</table>

					</c:if>

				</logic:iterate>
			</logic:present>

			<p class="navbottom">
				1.
				<a href='<%= "#" + InquiriesUtil.STUDENT_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.student" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				2.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_FORM_ANCHOR  %>'>
					<bean:message key="link.inquiries.course" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				3.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_TEACHER_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.teachers" bundle="INQUIRIES_RESOURCES"/>
				</a>
				&nbsp;|&nbsp;
				4.
				<a href='<%= "#" + InquiriesUtil.ATTENDING_COURSE_ROOM_FORM_ANCHOR %>'>
					<bean:message key="link.inquiries.rooms" bundle="INQUIRIES_RESOURCES"/>
				</a>
			</p>

			
		</div>	

		<div id="submit">
			<p>
				<strong>
					<bean:message key="message.inquiries.preview.info" bundle="INQUIRIES_RESOURCES"/>
				</strong>
			</p>
			<p>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inquirylargebutton" onclick='<%="this.form.method.value='submitInquiry';" %>'>
					<bean:message key="button.inquiries.preview.inquiry" bundle="INQUIRIES_RESOURCES"/>
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

<logic:present name='<%= InquiriesUtil.ANCHOR %>'>
	<script type="text/javascript">
		if(navigator.appName != "Microsoft Internet Explorer") {
			location.hash='<%= request.getAttribute(InquiriesUtil.ANCHOR) %>';
		}
	</script>	
</logic:present>
