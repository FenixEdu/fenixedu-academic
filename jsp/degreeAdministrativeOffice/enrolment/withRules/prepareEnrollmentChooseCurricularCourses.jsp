<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoCurricularCourse2Enroll" %>

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
	
	function disableAllElements(form){
		var elements = form.elements;
		var i = 0;
		for (i = 0; i < elements.length ; i++){
			var element = elements[i];
 			if(element.type == 'button'){
 				element.disabled = true;
 			}
		}
	}
// -->
</script>


<style>
input { font-size: 11px; }
</style>


<h2><bean:message key="title.student.enrollment.simple" bundle="STUDENT_RESOURCES"/></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/curricularCoursesEnrollment">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrollmentConfirmation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
	<bean:define id="studentCurricularPlanId" name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanId" property="studentCurricularPlanId" value="<%=studentCurricularPlanId.toString()%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseType" property="courseType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourse" property="curricularCourse"/>
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	
	<div class="infoselected">
	<p><b><bean:message bundle="APPLICATION_RESOURCES" key="label.student"/>:</b> <bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.infoPerson.nome" /> / 
	<bean:message key="label.student.number"/> <bean:write name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.number" /></p>
	<p><b><bean:message key="label.student.enrollment.executionPeriod"/></b>: <bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.name" /> <bean:write name="infoStudentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" /></p>
	</div>
	
	
	<table>
		<tr>
			<td>
				<logic:equal name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.payedTuition" value="false">
					<logic:equal name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.interruptedStudies" value="false">
						<span class="error"><bean:message key="message.student.noPayed.tuition" bundle="DEGREE_ADM_OFFICE"/></span>
					</logic:equal>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:equal name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.flunked" value="true">
					<span class="error"><bean:message key="message.student.flunked" bundle="DEGREE_ADM_OFFICE"/></span>
				</logic:equal>
			</td>
		</tr>
		<tr>
			<td>
				<logic:equal name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoStudent.requestedChangeDegree" value="true">
					<span class="error"><bean:message key="message.student.change.degree" bundle="DEGREE_ADM_OFFICE"/></span>
				</logic:equal>
			</td>
		</tr>
	</table>


	<br />

	<table class="style1">	
		<tr>
			<th class="listClasses-header"><bean:message key="label.student.enrollment.specializationArea" />/<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES" /></th>
			<th class="listClasses-header"><bean:message key="label.student.enrollment.secondaryArea" /></th>
			<th class="listClasses-header">&nbsp;</th>
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
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">

						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
				</logic:present>
				<logic:notPresent name="infoStudentEnrolmentContext" property="infoStudentCurricularPlan.infoSecundaryBranch">
					<td class="listClasses">&nbsp;</td>
					<td class="listClasses">
						<logic:present name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:present>
						<logic:notPresent name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:notPresent>
					</td>
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
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriod + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID")%>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
			</tr>
		</logic:notPresent>
	</table>

<ul>
	<li><a href="@enrollment.faq.url@" target="_blank">FAQ Inscrição em Disciplinas - Glossï¿½rio</a></li>
	<li>Para mais esclarecimentos consultar <em>"II.7. Normas a observar na Inscrição"</em> no documento <a href="http://www.ist.utl.pt/html/destaques/regulamento0506.pdf">Licenciaturas: Regulamentos e Calendário Escolar 2005/2006 (.pdf)</a></li>
</ul>


	<h4><bean:message key="message.student.enrolled.curricularCourses" />:</h4>
	<table class="style1">
		<tr class="header">
		<th class="listClasses-header"><bean:message key="label.courses" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>		
		<th class="listClasses-header"><bean:message key="label.course.enrollment.weight2" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.acumulated.enrollments2" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.cancel" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="enrollmentElem" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" type="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment">
			<tr>
				<td class="listClasses courses"><bean:write name="enrollmentElem" property="infoCurricularCourse.name"/></td>
				<td  class="listClasses"><bean:message bundle="APPLICATION_RESOURCES" name="enrollmentElem" property="enrollmentTypeResourceKey" /></td>				
				<td class="listClasses"><bean:write name="enrollmentElem" property="infoCurricularCourse.enrollmentWeigth"/></td>
				<td class="listClasses"><bean:write name="enrollmentElem" property="accumulatedWeight"/></td>
				<td class="listClasses">
					<bean:define id="condition" name="enrollmentElem" property="condition"/>
					<bean:message key='<%=condition.toString()%>' bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td class="listClasses">
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
					<bean:define id="onClickString" >this.form.method.value='unenrollFromCurricularCourse'; this.form.curricularCourse.value='<bean:write name="enrollmentIndex" />'; disableAllElements(this.form); this.form.submit();</bean:define>
					<html:button alt="<%=enrollmentIndex.toString()%>" property="<%=enrollmentIndex.toString()%>"   onclick="<%= onClickString %>">
						<bean:message key="button.anull" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	

	<h4><bean:message key="message.student.unenrolled.curricularCourses" />:</h4>
	<table class="style1">
		<logic:present name="warnings">
			<tr>
				<td colspan="5">
					<ul>
						<logic:iterate id="messageKey" name="warnings">
							<li><span style="color: red"><b><bean:message key='<%=messageKey.toString()%>' bundle="STUDENT_RESOURCES"/></b></span></li>
						</logic:iterate>
					</ul>
				</td>
			</tr>
		</logic:present>	
		<tr class="header">
		<th class="listClasses-header"><bean:message key="label.course" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>	
		<th class="listClasses-header"><bean:message key="label.course.enrollment.weight2" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.acumulated.enrollments2" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.curricularYear" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th class="listClasses-header"><bean:message key="label.course.enrollment.enroll" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="curricularCourse" name="infoStudentEnrolmentContext" property="curricularCourses2Enroll">
			<bean:define id="curricularCourseIndex" name="curricularCourse" property="infoCurricularCourse.idInternal"/>
			<tr>
				<td class="listClasses courses"><bean:write name="curricularCourse" property="infoCurricularCourse.name"/></td>
				<td class="listClasses">
					<% if ( ((InfoCurricularCourse2Enroll) curricularCourse).isOptionalCurricularCourse() ) {%>
						<bean:message bundle="APPLICATION_RESOURCES" key="option.curricularCourse.optional" />
					<% } else { %>
						<bean:message bundle="APPLICATION_RESOURCES" name="curricularCourse" property="infoCurricularCourse.type.keyName" />
					<% } %>
				</td>
				<td class="listClasses"><bean:write name="curricularCourse" property="infoCurricularCourse.enrollmentWeigth"/></td>
				<td class="listClasses"><bean:write name="curricularCourse" property="accumulatedWeight"/></td>
				<td class="listClasses"><bean:write name="curricularCourse" property="curricularYear.year"/></td>
				<td class="listClasses"><bean:message name="curricularCourse" property="enrollmentType.name" bundle="ENUMERATION_RESOURCES"/></td>
				<td class="listClasses">
					<bean:define id="curricularCourseString">
						<bean:write name="curricularCourseIndex"/>-<bean:write name="curricularCourse" property="enrollmentType"/>
					</bean:define>
					<bean:define id="onClickString" >
						<% if ( ((InfoCurricularCourse2Enroll) curricularCourse).isOptionalCurricularCourse() ) {%>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourseString" />'; this.form.courseType.value='2'; disableAllElements(this.form); this.form.submit();
						<% } else { %>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourseString" />'; this.form.courseType.value='1'; disableAllElements(this.form); this.form.submit();
						<% } %>
					</bean:define>
					<html:button bundle="HTMLALT_RESOURCES" altKey="button.curricularCourseString" property="curricularCourseString" onclick="<%=onClickString%>">
						<bean:message key="button.enroll" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
	</html:submit>
</html:form>