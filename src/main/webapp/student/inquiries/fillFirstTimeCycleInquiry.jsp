<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<style> .bgcolorgrey { background-color: #fafafa !important; } </style>

<h2><bean:message key="title.inquiries.student.inquiry" bundle="INQUIRIES_RESOURCES"/></h2>

<logic:notPresent name="studentInquiryBean">
	<bean:message key="message.inquiries.answered" bundle="INQUIRIES_RESOURCES"/>
</logic:notPresent>

<logic:present name="studentInquiryBean">

	<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>
	
	<p class="mvert2">
		<em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em>
	</p>
	
	<div class="forminline dinline">
		<div class="relative">
		
			<div class="inquiries-container">
				<fr:form action="/studentCycleInquiry.do">		
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
					<html:submit><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>
				</fr:form>
				
			</div>
			
			<br/>
		</div>
	</div>	
</logic:present>


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