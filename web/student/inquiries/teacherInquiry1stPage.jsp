<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teacherInquiry" property="teacherDTO.name" /></td>		
	</tr>
	<tr>
		<td><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:message name="teacherInquiry" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
</table>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers_1" bundle="INQUIRIES_RESOURCES"/></span></h3>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/studentInquiry.do">
			<html:hidden property="method" value="fillTeacherInquiry"/>
		
			<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
			<fr:edit name="teacherInquiry" id="teacherInquiry" visible="false" />
			
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.thirdPageFirstBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teacherInquiry" property="thirdPageFirstBlock" >
				<fr:layout>
					<fr:property name="columnClasses" value=",error0" />
				</fr:layout>
			</fr:edit>
			<fr:edit name="teacherInquiry" property="thirdPageThirdBlock" />
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.thirdPageFourthBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teacherInquiry" property="thirdPageFourthBlock" />
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.thirdPageFifthBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teacherInquiry" property="thirdPageFifthBlock" />
			<h4 class="mtop15 mbottom05"><bean:message key="title.studentInquiries.thirdPageSixthBlock" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="teacherInquiry" property="thirdPageSixthBlock" />
		
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
			<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
			<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>		
		<br/>
	</div>
</div>