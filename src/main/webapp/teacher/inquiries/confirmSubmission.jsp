<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.teachingInquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teachingInquiry" property="professorship.executionCourse.nome" /></td>
	</tr>
</table>

<p><bean:message key="message.teachingInquiries.editInquiryWhenFinnish" bundle="INQUIRIES_RESOURCES"/></p>

<p class="mbottom0">
	<div class="forminline dinline">
		<fr:form action="/teachingInquiry.do?method=backFromPrepareConfirm">
			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
			<html:submit><bean:message key="button.backWithArrow" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>	
		<fr:form action="/teachingInquiry.do?method=confirm">
			<fr:edit name="teachingInquiry" id="teachingInquiry" visible="false"/>
			<html:submit><bean:message key="button.submitInquiry" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
	</div>
</p>
