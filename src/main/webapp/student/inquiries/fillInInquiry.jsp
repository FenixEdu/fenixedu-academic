<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<style> .bgcolorgrey { background-color: #fafafa !important; } </style>

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries.student" bundle="INQUIRIES_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<h3 class="separator2 mtop2">
	<span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.courses_1" bundle="INQUIRIES_RESOURCES"/>:</span>
	<b><bean:write name="inquiryBean" property="inquiryRegistry.executionCourse.nome" /></b>
</h3>

<p class="mvert2">
	<em><bean:message key="message.inquiries.anonimousFromNow" bundle="INQUIRIES_RESOURCES"/></em> 
	<em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em>
</p>

<div class="forminline dinline">
	<div class="relative">
	
		<div class="inquiries-container">
			<fr:form action="/studentInquiry.do?method=showTeachersToAnswer">			
				<logic:iterate id="inquiryBlockDTO" name="inquiryBean" property="curricularCourseBlocks">
					<logic:notEmpty name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader">
						<h4 class="mtop15 mbottom05"><fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/></h4>
					</logic:notEmpty>				
					<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="index">					
						<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
					</logic:iterate>
				</logic:iterate>
				
				<fr:edit name="inquiryBean" id="inquiryBean" visible="false"/>
				<br/>		
				<html:submit styleClass="bright"><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
			</fr:form>
			
			
			<fr:form action="/studentInquiry.do?method=showCoursesToAnswer">
				<html:submit styleClass="bleft"><bean:message key="button.cancel"/></html:submit>
			</fr:form>
		</div>
		
		<br/>
	</div>
</div>	

<style>

input.bright { position: absolute; bottom: 0; left: 75px; }

div.inquiries-container {
max-width: 900px;
}

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


th.col1, th.col2, th.col3, th.col4, th.col5, th.col6, th.col7, th.col8, th.col9, th.col10, th.col11 {
text-align: center !important;
}

</style>