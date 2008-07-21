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
<style>
a.help {
position: relative;
text-decoration: none;
/*color: black !important;*/
border: none !important;
width: 20px;
}
a.help span {
display: none;
}
a.help:hover {
/*background: none;*/ /* IE hack */
z-index: 100;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
</style>

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
	<fr:form action="/studentInquiry.do">
		<html:hidden property="method" value="fillTeacherInquiry"/>
	
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<fr:edit name="teacherInquiry" id="teacherInquiry" visible="false" />
		
		<p><bean:message key="title.studentInquiries.thirdPageFirstBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="teacherInquiry" property="thirdPageFirstBlock" />
		<fr:edit name="teacherInquiry" property="thirdPageThirdBlock" />
		<p><bean:message key="title.studentInquiries.thirdPageFourthBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="teacherInquiry" property="thirdPageFourthBlock" />
		<p><bean:message key="title.studentInquiries.thirdPageFifthBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="teacherInquiry" property="thirdPageFifthBlock" />
		<p><bean:message key="title.studentInquiries.thirdPageSixthBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="teacherInquiry" property="thirdPageSixthBlock" />
	
		<html:submit><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>		
</div>