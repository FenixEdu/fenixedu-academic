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
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="executionDegree.degree.presentationName" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="professorship.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/teachingInquiry.do?method=showInquiries2ndPage">
			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.teachingAndLearningConditions" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="firstPageFirstBlock" />
			
			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.studentsEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="firstPageSecondBlockFirstPart" />
			
			<fr:edit name="teachingInquiry" property="firstPageSecondBlockSecondPart" />
			<fr:edit name="teachingInquiry" property="firstPageSecondBlockThirdPart" />
			<fr:edit name="teachingInquiry" property="firstPageSecondBlockFourthPart" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.autoEvaluation" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="firstPageThirdBlock" />

			<h4 class="mtop15 mbottom05"><bean:message key="title.teachingInquiries.relativePedagogicalInitiatives" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teachingInquiry" property="firstPageFourthBlock" />

			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
		
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
						
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/teachingInquiry.do?method=showDegreesToAnswer">
			<bean:define id="executionCourseID" ><bean:write name="teachingInquiry" property="professorship.executionCourse.idInternal" /></bean:define>
			<html:hidden property="executionCourseID" value="<%= executionCourseID %>"/>
			
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<br/>
	</div>
</div>	