<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
input.bright { position: absolute; bottom: 0; left: 70px; }
 
.question {
border-collapse: collapse;
margin: 10px 0;
 
width: 100%;
}
.question th {
padding: 5px 10px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
background: #f5f5f5;
vertical-align: bottom;
}
.question th.firstcol {
vertical-align: top;
}
.question td {
padding: 5px;
text-align: center;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
background-color: #fff;
}
 
th.firstcol {
width: 300px;
text-align: left;
}
 
.q1col td { text-align: left; }
 
.q9col .col1, .q9col .col9  { width: 30px; }
.q10col .col1, .q10col .col2, .q10col .col10  { width: 20px; }
.q11col .col1, .q11col .col2, .q11col .col3, .q11col .col11  { width: 20px; }
 
th.col1, th.col2, th.col3, th.col4, th.col5, th.col6, th.col7, th.col8, th.col9, th.col10 {
text-align: center !important;
}
 
 
.max-width {
min-width: 650px;
max-width: 900px;
}
</style>

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiry.teacher" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="person" property="name"/> - <bean:write name="executionCourse" property="name"/> - <bean:write name="executionCourse" property="sigla"/>  
 (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p>
	<html:link action="/regentInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		<bean:message key="label.return"/>
	</html:link>
</p>
<!-- Teacher Inquiry -->	
<logic:iterate id="inquiryBlockDTO" name="teacherInquiryBlocks">
	<h3 class="separator2 mtop25">
		<span style="font-weight: normal;">
			<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
		</span>
	</h3>					
	<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups">					
		<fr:edit name="inquiryGroup">
			<fr:layout>
				<fr:property name="readOnly" value="true"/>
			</fr:layout>
		</fr:edit>
	</logic:iterate>
</logic:iterate>