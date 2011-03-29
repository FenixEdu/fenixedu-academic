<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<style>
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
}
.question td {
padding: 5px;
border: none;
border-bottom: 1px solid #ccc;
border-top: 1px solid #ccc;
}
 
textarea {
border: none;
}
</style>

<em><bean:message key="title.teacherPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiry.delegate" bundle="INQUIRIES_RESOURCES"/></h2>

<bean:define id="degreeSigla" name="executionDegree" property="degree.sigla" type="java.lang.String"/>

<h3><span class="highlight1"><bean:write name="executionCourse" property="name"/> (<bean:write name="executionCourse" property="sigla"/>)</span> -  
<bean:write name="degreeSigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<p>
<logic:notEmpty name="year">
	<bean:define id="delegateYear" name="year"/>
	<bean:message key="message.teacher.delegate.inquiry" bundle="INQUIRIES_RESOURCES" arg0="<%= delegateYear.toString() %>" arg1="<%= degreeSigla %>"/>
</logic:notEmpty>
<logic:empty name="year">
	<bean:message key="message.teacher.delegate.inquiry.notAnswered" bundle="INQUIRIES_RESOURCES" arg0="<%= degreeSigla %>"/>
</logic:empty>
</p>

<p>
	<html:link action="/teachingInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
		<bean:message key="label.return"/>
	</html:link>
</p>
<!-- Delegate Inquiry -->	
<logic:iterate id="inquiryBlockDTO" name="delegateInquiryBlocks">
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