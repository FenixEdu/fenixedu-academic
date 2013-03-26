<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style> .bgcolorgrey { background-color: #fafafa !important; } </style>

<h2><bean:message key="label.candidacy.workflow.fillPersonalData.inquiryStep"/></h2>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<p class="mvert2">
	<em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em>
</p>

<div class="forminline dinline">
	<div class="relative">
	
		<div class="inquiries-container">
			<fr:form action="/degreeCandidacyManagement.do">		
				<html:hidden property="method" value="saveInquiry"/>	
				<logic:iterate id="inquiryBlockDTO" name="studentInquiryBean" property="studentInquiryBlocks" indexId="blockIndex">
				
					<logic:notEmpty name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader">
					
						<h4 class="mtop15 mbottom05"><fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/></h4>
					</logic:notEmpty>				
					<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="groupIndex">		
						<a name="<%= "inquiry" + blockIndex + groupIndex %>"> </a>								
						<fr:edit id="<%= "iter" + groupIndex --%>" name="inquiryGroup">
							<fr:layout>
								<fr:property name="postBackMethod" value="postBackStudentInquiry"/>
								<fr:property name="postBackAnchor" value="<%= "inquiry" + blockIndex + groupIndex %>"/>
							</fr:layout>
						</fr:edit>
					</logic:iterate>
				</logic:iterate>
				
				<fr:edit name="studentInquiryBean" id="studentInquiryBean" visible="false"/>
				<br/>		
				<html:submit><bean:message key="button.continue" bundle="INQUIRIES_RESOURCES"/></html:submit>
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
width: 250px;
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