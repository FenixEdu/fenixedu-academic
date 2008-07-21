<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
table.tdwith50px td { width: 50px; }
table.tdwith90px td { width: 90px; }
.aleft { text-align: left !important; }
.acenter { text-align: center !important; }
.bold { font-weight: bold !important; }
</style>

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

<div class="forminline dinline">
	<fr:form action="/studentInquiry.do?method=showInquiries2ndPage">
		<p><bean:message key="title.studentInquiries.firstPageFirstBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="studentInquiry" property="firstPageFirstBlock" />
		<fr:edit name="studentInquiry" property="firstPageSecondBlock" />
		<fr:edit name="studentInquiry" property="firstPageThirdBlock" />
		<fr:edit name="studentInquiry" property="firstPageFourthBlock" />
		<p><bean:message key="title.studentInquiries.firstPageFifthBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="studentInquiry" property="firstPageFifthBlock" />
		
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
	
		<html:submit><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="/studentInquiry.do?method=showCoursesToAnswer">
		<html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
</div>	