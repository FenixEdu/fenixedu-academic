<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="studentInquiry" property="inquiriesRegistry.student.degree.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="studentInquiry" property="inquiriesRegistry.executionCourse.nome" /></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.courses_1" bundle="INQUIRIES_RESOURCES"/></span></h3>

<span class="success0"><bean:message key="message.inquiries.anonimousFromNow" bundle="INQUIRIES_RESOURCES"/></span>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/studentInquiry.do?method=showInquiries2ndPage">
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.firstPageFirstBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="studentInquiry" property="firstPageFirstBlock" />
			<fr:edit name="studentInquiry" property="firstPageSecondBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value=",aleft"/>
				</fr:layout>		
			</fr:edit>
			<fr:edit name="studentInquiry" property="firstPageThirdBlock" />
			<fr:edit name="studentInquiry" property="firstPageFourthBlock" />
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.firstPageFifthBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<p><bean:message key="message.studentInquiries.firstPageFifthBlock" bundle="INQUIRIES_RESOURCES"/></p>
			<fr:edit name="studentInquiry" property="firstPageFifthBlock" />
			
			<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
						
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/studentInquiry.do?method=showCoursesToAnswer">
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<br/>
	</div>
</div>	