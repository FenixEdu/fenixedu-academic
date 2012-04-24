<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
 
/*
Coordinator Inquiry Specific Questions
*/

.question {
border-collapse: collapse;
margin: 10px 0;
}
.question th {
padding: 5px;
font-weight: normal;
text-align: left;
border: none;
border-top: 1px solid #ccc;
border-bottom: 1px solid #ccc;
vertical-align: top;
font-weight: bold;
width: 300px;
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
}

</style>

<h2><bean:message key="title.inquiry.quc.coordinator" bundle="INQUIRIES_RESOURCES"/></h2>

<h3><bean:write name="degreeAcronym"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p><em><bean:message key="message.coordinator.details.inquiry" bundle="INQUIRIES_RESOURCES"/></em></p>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<fr:form action="/viewInquiriesResults.do?method=saveInquiry">
	<fr:edit id="coordinatorInquiryBean" name="coordinatorInquiryBean" visible="false"/>
		
	<!-- Coordinator Inquiry -->
	<logic:iterate id="inquiryBlockDTO" name="coordinatorInquiryBean" property="coordinatorInquiryBlocks">
		<h3 class="separator2 mtop25">
			<span style="font-weight: normal;">
				<fr:view name="inquiryBlockDTO" property="inquiryBlock.inquiryQuestionHeader.title"/>
			</span>
		</h3>		
		<div class="max-width"> 			
		<logic:iterate id="inquiryGroup" name="inquiryBlockDTO" property="inquiryGroups" indexId="index">					
			<fr:edit id="<%= "iter" + index --%>" name="inquiryGroup"/>
		</logic:iterate>
		</div>
	</logic:iterate>
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.saveInquiry" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</p>
		
</fr:form>
