<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="professorship.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3><bean:message key="title.teachingInquiries.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></h3>

<logic:iterate id="courseResult" name="studentInquiriesCourseResults" type="net.sourceforge.fenixedu.dataTransferObject.inquiries.StudentInquiriesCourseResultBean">

	<h4 class="separator2 mtop15 mbottom05" style="font-weight: normal;"><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>: <c:out value="${courseResult.studentInquiriesCourseResult.executionDegree.presentationName}" /></h4>

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

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/teachingInquiry.do?method=submitInquiries3rdPage">

			<fr:edit name="teachingInquiry" property="thirdPageNinthBlock" />

			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
		
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
						
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/teachingInquiry.do?method=showInquiries2ndPage">
			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>		
		
		<br/>
	</div>
</div>	