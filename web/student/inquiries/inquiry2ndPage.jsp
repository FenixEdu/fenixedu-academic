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

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.courses_2" bundle="INQUIRIES_RESOURCES"/></span></h3>

<div class="forminline dinline">
	<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">
		<p><bean:message key="title.studentInquiries.secondPageFirstBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="studentInquiry" property="secondPageFirstBlock" />
		<p><bean:message key="title.studentInquiries.secondPageSecondBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="studentInquiry" property="secondPageSecondBlock" />
		<p><bean:message key="title.studentInquiries.secondPageThirdBlock" bundle="INQUIRIES_RESOURCES"/></p>
		<fr:edit name="studentInquiry" property="secondPageThirdBlock" />
		
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
	
		<html:submit><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>
	
	<fr:form action="/studentInquiry.do?method=showInquiries1stPage">
		<fr:edit name="studentInquiry" id="studentInquiry" visible="false"/>
		<html:submit><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
	</fr:form>	
</div>