<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

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


<h2><bean:message key="title.student.enrollment.simple" bundle="STUDENT_RESOURCES"/></h2>

<p>
	<span class="error0"><html:errors/></span>
</p>

<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<html:form action="/curricularCoursesEnrollment">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrollmentConfirmation" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	
	<logic:present name="curricularCoursesEnrollmentForm" property="registrationId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.registrationId" property="registrationId" />
	</logic:present>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
	
	<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="idInternal" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentCurricularPlanId" property="studentCurricularPlanId" value="<%=studentCurricularPlanId.toString()%>"/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.courseType" property="courseType"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourse" property="curricularCourse"/>
	
	<logic:present name="executionDegreeId">
		<bean:define id="executionDegreeId" name="executionDegreeId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=executionDegreeId.toString() %>"/>
	</logic:present>
	
	<div class="infoop2 mtop1 mbottom15">
		<p><b><bean:message bundle="APPLICATION_RESOURCES" key="label.student"/>:</b> <bean:write name="studentCurricularPlan" property="student.person.name" /> / 
		<bean:message key="label.student.number"/> <bean:write name="studentCurricularPlan" property="student.number" /></p>
		<p><b><bean:message key="label.student.enrollment.executionPeriod"/></b>: <bean:write name="executionPeriod" property="name" /> <bean:write name="executionPeriod" property="executionYear.year" /></p>
	</div>
	

	<logic:equal name="studentCurricularPlan" property="student.payedTuition" value="false">
		<logic:equal name="studentCurricularPlan" property="student.interruptedStudies" value="false">
			<p>
				<span class="error0"><bean:message key="message.student.noPayed.tuition" bundle="DEGREE_ADM_OFFICE"/></span>
			</p>
		</logic:equal>
	</logic:equal>

	<logic:equal name="studentCurricularPlan" property="student.flunked" value="true">
		<p>
			<span class="error0"><bean:message key="message.student.flunked" bundle="DEGREE_ADM_OFFICE"/></span>
		</p>
	</logic:equal>

	<logic:equal name="studentCurricularPlan" property="student.requestedChangeDegree" value="true">
		<p>
			<span class="error0"><bean:message key="message.student.change.degree" bundle="DEGREE_ADM_OFFICE"/></span>
		</p>
	</logic:equal>

	<bean:define id="registrationIdParameter" value="" />
	<logic:present name="curricularCoursesEnrollmentForm" property="registrationId">
		<bean:define id="registrationId" name="curricularCoursesEnrollmentForm" property="registrationId" />
		<bean:define id="registrationIdParameter" value="<%="&amp;registrationId=" + registrationId%>" />
	</logic:present>


	<table class="tstyle3 mtop05">	
		<tr>
			<th><bean:message key="label.student.enrollment.specializationArea" />/<bean:message key="label.student.enrollment.branch" bundle="STUDENT_RESOURCES" /></th>
			<th><bean:message key="label.student.enrollment.secondaryArea" /></th>
			<th>&nbsp;</th>
		</tr>
		<logic:present name="studentCurricularPlan" property="branch">
			<bean:define id="name" name="studentCurricularPlan" property="student.person.name"/>
			<bean:define id="number" name="studentCurricularPlan" property="student.number"/>
			<bean:define id="executionPeriodName" name="executionPeriod" property="name"/>
			<bean:define id="executionYear" name="executionPeriod" property="executionYear.year"/>	
			<tr>
				<td>
					<bean:write name="studentCurricularPlan" property="branch.name" />
				</td>
				<bean:define id="specialization" name="studentCurricularPlan" property="branch.idInternal"/>
				<logic:present name="studentCurricularPlan" property="secundaryBranch">
				<td>
					<bean:write name="studentCurricularPlan" property="secundaryBranch.name" />
				</td>
					<bean:define id="secondary" name="studentCurricularPlan" property="secundaryBranch.idInternal"/>
					<td>
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;secondaryArea=" + secondary + "&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
				</logic:present>
				<logic:notPresent name="studentCurricularPlan" property="secundaryBranch">
					<td>&nbsp;</td>
					<td>
						<logic:present name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:present>
						<logic:notPresent name="executionDegreeId">
							<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;specializationArea=" + specialization +"&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
								<bean:message key="button.student.modify"/>
							</html:link>
						</logic:notPresent>
					</td>
				</logic:notPresent>
			</tr>
		</logic:present>
		
		<logic:notPresent name="studentCurricularPlan" property="branch">
			<tr>
				<td>
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td>
					<bean:message key="label.student.enrollment.no.area" />
				</td>
				<td>
				
					<bean:define id="name" name="studentCurricularPlan" property="student.person.name"/>
					<bean:define id="number" name="studentCurricularPlan" property="student.number"/>
					<bean:define id="executionPeriodName" name="executionPeriod" property="name"/>
					<bean:define id="executionYear" name="executionPeriod" property="executionYear.year"/>
					
					<logic:present name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;executionDegreeId=" + pageContext.findAttribute("executionDegreeId") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:present>
					<logic:notPresent name="executionDegreeId">
						<html:link page="<%="/curricularCoursesEnrollment.do?method=prepareEnrollmentPrepareChooseAreas&amp;studentNumber=" + number + "&amp;studentName=" + name + "&amp;studentCurricularPlanId="+ studentCurricularPlanId + "&amp;executionPeriod=" + executionPeriodName + "&amp;executionYear=" + executionYear + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + registrationIdParameter %>">
							<bean:message key="button.student.modify"/>
						</html:link>
					</logic:notPresent>
				</td>
			</tr>
		</logic:notPresent>
	</table>

<ul>
	<li>Para mais esclarecimentos consultar <em>"II.7. Normas a observar na Inscrição"</em> no documento <a href="http://www.ist.utl.pt/html/destaques/regulamento0506.pdf">Licenciaturas: Regulamentos e Calendário Escolar 2005/2006 (.pdf)</a></li>
</ul>


	<h4 class="mtop2 mbottom05"><bean:message key="message.student.enrolled.curricularCourses" />:</h4>
	<table class="tstyle3 mtop05">
		<tr class="header">
		<th><bean:message key="label.courses" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.degree.name" bundle="STUDENT_RESOURCES"/></th>		
		<th><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>		
		<th><bean:message key="label.course.enrollment.ectsCredits" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.course.enrollment.cancel" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="enrollmentElem" name="studentCurrentSemesterEnrollments" type="net.sourceforge.fenixedu.domain.Enrolment">
			<tr>
				<td class="highlight5"><bean:write name="enrollmentElem" property="curricularCourse.name"/></td>
				
				<td class="highlight5"><bean:write name="enrollmentElem" property="curricularCourse.degreeCurricularPlan.degree.sigla"/></td>
				
	            <td><bean:message bundle="ENUMERATION_RESOURCES" name="enrollmentElem" property="enrolmentTypeName"/></td>
				
				<td class="acenter"><bean:write name="enrollmentElem" property="accumulatedEctsCredits"/></td>
				<td>
					<bean:message name="enrollmentElem" property="condition.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
				<td>
					<bean:define id="enrollmentIndex" name="enrollmentElem" property="idInternal"/>
					<bean:define id="onClickString">this.form.method.value='unenrollFromCurricularCourse'; this.form.curricularCourse.value='<bean:write name="enrollmentIndex" />'; disableAllElements(this.form); this.form.submit();</bean:define>
					<html:button alt="<%=enrollmentIndex.toString()%>" property="<%=enrollmentIndex.toString()%>"   onclick="<%= onClickString %>">
						<bean:message key="button.anull" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	

	<h4 class="mtop2 mbottom05"><bean:message key="message.student.unenrolled.curricularCourses" />:</h4>
	<table class="tstyle3 mtop05">
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
		<th><bean:message key="label.course" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.degree.name" bundle="STUDENT_RESOURCES"/></th>				
		<th><bean:message key="label.course.type" bundle="STUDENT_RESOURCES"/></th>	
		<th><bean:message key="label.course.enrollment.ectsCredits" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.course.enrollment.curricularYear" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.course.enrollment.state" bundle="STUDENT_RESOURCES"/></th>
		<th><bean:message key="label.course.enrollment.enroll" bundle="STUDENT_RESOURCES"/></th>		
		</tr>
		<logic:iterate id="curricularCourse2Enrol" name="curricularCourses2Enroll" type="net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll">
			<bean:define id="curricularCourse2EnrolIndex" name="curricularCourse2Enrol" property="curricularCourse.idInternal"/>
			<tr>
				<td class="highlight5"><bean:write name="curricularCourse2Enrol" property="curricularCourse.name"/></td>
				<td class="highlight5"><bean:write name="curricularCourse2Enrol" property="curricularCourse.degreeCurricularPlan.degree.sigla"/></td>
				<td>
					<% if (curricularCourse2Enrol.isOptionalCurricularCourse()) {%>
						<bean:message bundle="APPLICATION_RESOURCES" key="option.curricularCourse.optional" />
						
					<% } else { %>
						<bean:message bundle="ENUMERATION_RESOURCES" name="curricularCourse2Enrol" property="curricularCourse.type.name" />
						
					<% } %>
				</td>
				<td class="acenter"><bean:write name="curricularCourse2Enrol" property="ectsCredits"/></td>
				<td><bean:write name="curricularCourse2Enrol" property="curricularYear.year"/></td>
				<td><bean:message name="curricularCourse2Enrol" property="enrollmentType.name" bundle="ENUMERATION_RESOURCES"/></td>
				<td>
					<bean:define id="curricularCourse2EnrolString">
						<bean:write name="curricularCourse2EnrolIndex"/>-<bean:write name="curricularCourse2Enrol" property="enrollmentType"/>
					</bean:define>
					<bean:define id="onClickString" >
						<% if (curricularCourse2Enrol.isOptionalCurricularCourse()) {%>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourse2EnrolString" />'; this.form.courseType.value='2'; disableAllElements(this.form); this.form.submit();
							
						<% } else { %>
							this.form.method.value='enrollInCurricularCourse';  this.form.curricularCourse.value='<bean:write name="curricularCourse2EnrolString" />'; this.form.courseType.value='1'; disableAllElements(this.form); this.form.submit();
							
						<% } %>
					</bean:define>
					<html:button bundle="HTMLALT_RESOURCES" altKey="button.curricularCourse2EnrolString" property="curricularCourse2EnrolString" onclick="<%=onClickString%>">
						<bean:message key="button.enroll" bundle="STUDENT_RESOURCES"/>
					</html:button>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.finish"/>
		</html:submit>
	</p>
	
</html:form>