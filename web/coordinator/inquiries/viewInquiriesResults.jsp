<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<html:form action="/viewInquiriesResults.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="degreeCurricularPlanID"/>
	<label for="executionDegreeID"><bean:message key="label.inquiries.semesterAndYear" bundle="INQUIRIES_RESOURCES"/>:</label><br/>
	<html:select property="executionDegreeID" onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
		<html:option value=""><bean:message key="label.inquiries.chooseAnOption" bundle="INQUIRIES_RESOURCES"/></html:option>
 		<html:options collection="executionDegrees" property="idInternal" labelProperty="executionYear.name"/>
	</html:select>
	<br/>
	<label for="executionDegreeID"><bean:message key="label.inquiries.curricularUnit" bundle="INQUIRIES_RESOURCES"/>:</label><br/>
	<html:select property="executionCourseID" onchange="this.form.method.value='selectExecutionCourse';this.form.submit();">
		<html:option value=""><bean:message key="label.inquiries.chooseAnOption" bundle="INQUIRIES_RESOURCES"/></html:option>		
 		<html:options collection="executionCourses" property="idInternal" labelProperty="nome"/>
	</html:select>	
</html:form>

<logic:present name="studentInquiriesCourseResults">
	<p class="separator2 mtop2"><bean:message key="title.inquiries.studentResults" bundle="INQUIRIES_RESOURCES"/></p>
	<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean" >
		<p class="mtop2">
			<bean:message key="link.teachingInquiries.cuResults" bundle="INQUIRIES_RESOURCES"/> - 
			<html:link page="<%= "/viewInquiriesResults.do?method=showInquiryCourseResult&resultId=" + courseResult.getStudentInquiriesCourseResult().getIdInternal() %>" target="_blank">
				<bean:write name="courseResult" property="studentInquiriesCourseResult.executionCourse.nome" /> - 				
				<bean:write name="courseResult" property="studentInquiriesCourseResult.executionDegree.degreeCurricularPlan.name" />
			</html:link>
		</p>

		<logic:notEmpty name="courseResult" property="studentInquiriesTeachingResults">
			<ul>
				<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
					<li>
						<html:link page="<%= "/viewInquiriesResults.do?method=showInquiryTeachingResult&resultId=" + teachingResult.getIdInternal() %>" target="_blank">
							<bean:write name="teachingResult" property="professorship.teacher.person.name" />
							&nbsp;(<bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/>)<br/>
						</html:link>
					</li>			
				</logic:iterate>
			</ul>
		</logic:notEmpty>
	</logic:iterate>
	
	<p class="separator2 mtop2"><bean:message key="title.inquiries.teachingReports" bundle="INQUIRIES_RESOURCES"/></p>
	<ul>
		<logic:iterate id="professorship" name="executionCourse" property="professorships" >
			<li>
				<logic:notEmpty name="professorship" property="teachingInquiry">
					<bean:define id="teachingInquiryID" name="professorship" property="teachingInquiry.idInternal" />
					<html:link page="<%= "/viewInquiriesResults.do?method=showFilledTeachingInquiry&filledTeachingInquiryId=" + teachingInquiryID %>" target="_blank">
						<bean:write name="professorship" property="teacher.person.name"/> 
					</html:link>
				</logic:notEmpty>
				<logic:empty name="professorship" property="teachingInquiry">
					<bean:write name="professorship" property="teacher.person.name"/> 
				</logic:empty>
			</li>
		</logic:iterate>
	</ul>

	<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean">
	
		<p class="separator2 mtop2"><bean:message key="title.teachingInquiries.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></p>
		
		<table class="tstyle1 thlight tdcenter">
			<tr>		
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsCUOrganization" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsCUEvaluation" bundle="INQUIRIES_RESOURCES"/></th>
			</tr>
			<tr>
				<td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUOrganization().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
				<td><bean:message key="<%= "label.colored." + courseResult.getStudentInquiriesCourseResult().getUnsatisfactoryResultsCUEvaluation().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>			
			</tr>		
		</table>
		
		<table class="tstyle1 thlight tdcenter">
			<tr>		
				<th><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAssiduity" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPresencialLearning" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsPedagogicalCapacity" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsStudentInteraction" bundle="INQUIRIES_RESOURCES"/></th>
				<th><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></th>
			</tr>
			<logic:iterate id="teachingResult" name="courseResult" property="studentInquiriesTeachingResults" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult">
				<tr>		
					<td class="aleft"><c:out value="${teachingResult.professorship.teacher.person.name}" /></td>
					<td><bean:message name="teachingResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></td>
					<td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAssiduity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
					<td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPresencialLearning().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
					<td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsPedagogicalCapacity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
					<td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsStudentInteraction().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
					<td><bean:message key="<%= "label.colored." + teachingResult.getUnsatisfactoryResultsAuditable().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
				</tr>		
			</logic:iterate>
		</table>
		
	</logic:iterate>	
	
</logic:present>