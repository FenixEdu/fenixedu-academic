<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style> .bgcolorgrey { background-color: #fafafa !important; } </style>

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiryBean" property="inquiryRegistry.student.degree.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiryBean" property="inquiryRegistry.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.courses_1" bundle="INQUIRIES_RESOURCES"/></span></h3>

<p><span class="warning0"><bean:message key="message.inquiries.anonimousFromNow" bundle="INQUIRIES_RESOURCES"/></span></p>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">			
			<logic:iterate id="inquiryBlockDTO" name="inquiryBean" property="curricularCourseBlocks">
				<logic:notEmpty name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader">
					<h4 class="mtop15 mbottom05"><fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/></h4>
				</logic:notEmpty>				
				<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups"indexId="index">					
					<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
				</logic:iterate>
			</logic:iterate>
			
			<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
		
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
						
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/studentInquiry.do?method=showCoursesToAnswer">
			<html:submit styleClass="bleft"><bean:message key="button.cancel"/></html:submit>
		</fr:form>
		
		<br/>
	</div>
</div>	

<style>
.width300px {
width: 300px !important;
}
.width6cols {
width: 85px !important;
}
.width1col {
width: 610px !important;
}
.width10cols {
width: 38px !important;
}
.width9cols {
width: 46px !important;
}
.width11cols {
width: 31px !important;
}

input.bright { position: absolute; bottom: 0; left: 70px; }

</style>