<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.CurricularCourseType, DataBeans.InfoEnrolmentInOptionalCurricularCourse" %>

<script type="text/javascript" language="JavaScript">
<!--
	function disableAllElementsInEnrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement && !element.checked) || (element.name == enrolledElement))){
				element.disabled = true;
			}
		}
	}
	function disableAllElementsInUnenrollment(form, unenrolledElement, enrolledElement){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if (element.name && ((element.name == unenrolledElement) || (element.name == enrolledElement && element.checked))){
				element.readonly = true;
			}
		}
	}
// -->
</script>
<h2><bean:message key="title.student.enrollment"/></h2>
<span class="error"><html:errors/></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden property="method" value="enrollmentConfirmation" />
	<html:hidden property="studentNumber" />
	<bean:define id="studentCurricularPlanId" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.idInternal" />
	<html:hidden property="studentCurricularPlanId" value="<%=studentCurricularPlanId.toString()%>"/>
	<logic:present name="executionDegreeId">
		<html:hidden property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<b><bean:message key="label.student.enrollment.number"/></b>
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" />&nbsp;-&nbsp;
				<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" />
				<br />
				<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
				<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name" />&nbsp;				
				<bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" />
			</td>
		</tr>
	</table>
	<br />
	<table>	
		<tr>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.specializationArea" />/<bean:message key="label.student.enrollment.branch" /></td>
			<td class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" /></td>
			<td class="listClasses-header">&nbsp;</td>
		</tr>
		<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
					<bean:define id="name" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					<bean:define id="number" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number"/>
					<bean:define id="executionPeriod" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name"/>
					<bean:define id="executionYear" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
					
			<tr>
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.name" />
				</td>
					<bean:define id="specialization" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch.idInternal"/>
				<logic:present name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
				<td class="listClasses">
					<bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.name" />
				</td>
					<bean:define id="secondary" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch.idInternal"/>
					<td class="listClasses">
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
				</logic:present>
				<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
					<td class="listClasses">&nbsp;</td>
					<td class="listClasses">&nbsp;</td>
				</logic:notPresent>
			</tr>
		</logic:present>
		<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoBranch">
			<tr>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td class="listClasses">
					<bean:define id="name" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome"/>
					<bean:define id="executionPeriod" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name"/>
					<bean:define id="executionYear" name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
					<bean:define id="number" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number"/>
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
			</tr>
		</logic:notPresent>
	</table>
	
	<table>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="message.student.enrolled.curricularCourses" /></b>
			</td>
		</tr>
		<tr>
		<td class="listClasses-header"><bean:message key="label.course"/></td>
		<td class="listClasses-header"><bean:message key="label.course.type"/></td>		
		<td class="listClasses-header"><bean:message key="label.course.enrollment.weight"/></td>
		<td class="listClasses-header"><bean:message key="label.course.enrollment.acumulated.enrollments"/></td>
		<td class="listClasses-header"><bean:message key="label.course.enrollment.state"/></td>
		<td class="listClasses-header">&nbsp;</td>		
		</tr>
		<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="DataBeans.InfoEnrolment">
			<bean:define id="onclick">
				if (this.checked == false) {this.form.method.value='unenrollFromCurricularCourse'; disableAllElementsInUnenrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
			</bean:define>
			<html:hidden property="enrolledCurricularCoursesBefore" value="<%=enrollmentElem.getIdInternal().toString()%>"/>
			<tr>
				<td class="listClasses">
					<bean:write name="enrollmentElem" property="infoCurricularCourse.name"/>
					<logic:equal name="enrollmentElem" property="infoCurricularCourse.type" value="<%= CurricularCourseType.OPTIONAL_COURSE_OBJ.toString() %>">
						<% if (pageContext.findAttribute("enrollmentElem") instanceof InfoEnrolmentInOptionalCurricularCourse)
						   {%>
							<logic:notEmpty name="enrollmentElem" property="infoCurricularCourseForOption">
								-&nbsp;<bean:write name="enrollmentElem" property="infoCurricularCourseForOption.name"/>
							</logic:notEmpty>
							<logic:empty name="enrollmentElem" property="infoCurricularCourseForOption">
								-&nbsp;<bean:message key="message.not.regular.optional.enrollment"/>
							</logic:empty>
						   <%}
						%>
					</logic:equal>
				</td>
				<td  class="listClasses">
					<bean:message name="enrollmentElem" property="infoCurricularCourse.type.keyName" bundle="DEFAULT"/>
				</td>				
				<td class="listClasses">
					<bean:write name="enrollmentElem" property="infoCurricularCourse.enrollmentWeigth"/>
				</td>
				<td class="listClasses">
					<bean:write name="enrollmentElem" property="accumulatedWeight"/>
				</td>
				<td class="listClasses">
					<bean:message name="enrollmentElem" property="condition.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td class="listClasses">
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
					<html:multibox property="enrolledCurricularCoursesAfter" onclick="<%=onclick.toString()%>" >
													<bean:write name="enrollmentIndex"/>
					</html:multibox>
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan="2">
				<br />
				<b><bean:message key="message.student.unenrolled.curricularCourses" /></b>
			</td>
		</tr>
		<tr>
		<td class="listClasses-header"><bean:message key="label.course"/></td>
		<td class="listClasses-header"><bean:message key="label.course.type"/></td>		
		<td class="listClasses-header"><bean:message key="label.course.enrollment.weight"/></td>
		<td class="listClasses-header"><bean:message key="label.course.enrollment.acumulated.enrollments"/></td>
		<td class="listClasses-header"><bean:message key="label.course.enrollment.state"/></td>
		<td class="listClasses-header">&nbsp;</td>		
		</tr>
		<logic:iterate id="curricularCourse" name="infoStudentEnrolmentContext" property="curricularCourses2Enroll">
			<bean:define id="curricularCourseIndex" name="curricularCourse" property="infoCurricularCourse.idInternal"/>
			<bean:define id="onclick">
				if (this.checked == true) {this.form.method.value='enrollInCurricularCourse'; disableAllElementsInEnrollment(this.form,'unenrolledCurricularCourses','enrolledCurricularCoursesAfter');this.form.submit();}	
			</bean:define>
			<tr>
				<td class="listClasses">
					<bean:write name="curricularCourse" property="infoCurricularCourse.name"/>
				</td>
				<td class="listClasses">
					<bean:message name="curricularCourse" property="infoCurricularCourse.type.keyName" bundle="DEFAULT"/>
				</td>
				<td class="listClasses">
					<bean:write name="curricularCourse" property="infoCurricularCourse.enrollmentWeigth"/>
				</td>
				<td class="listClasses">
					<bean:write name="curricularCourse" property="accumulatedWeight"/>
				</td>
				<td class="listClasses">
					<bean:message name="curricularCourse" property="enrollmentType.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				
				<td class="listClasses">
					<html:multibox property="unenrolledCurricularCourses" onclick="<%=onclick.toString()%>" >
						<bean:write name="curricularCourseIndex"/>-<bean:write name="curricularCourse" property="enrollmentType.value"/>
					</html:multibox>
					
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.student.end"/>
	</html:submit>
</html:form>