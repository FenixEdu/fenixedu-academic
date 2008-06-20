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
		<td><bean:message key="label.teacher" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="teacherInquiry" property="teacherName" /></td>		
	</tr>
	<tr>
		<td><bean:message key="label.typeOfClass" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:message name="teacherInquiry" property="shiftType.name" bundle="ENUMERATION_RESOURCES"/></td>
	</tr>
</table>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.teachers" bundle="INQUIRIES_RESOURCES"/></span></h3>

<div class="forminline dinline">
	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="showTeachersToAnswer"/>
	
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		
		<fr:edit name="teacherInquiry" property="thirdPageFirstBlock" />
		<fr:edit name="teacherInquiry" property="thirdPageThirdBlock" />
		<fr:edit name="teacherInquiry" property="thirdPageSixthBlock" />
	
		<html:submit><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>		
</div>