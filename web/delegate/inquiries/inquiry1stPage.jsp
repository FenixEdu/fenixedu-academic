<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<html:xhtml />

<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td>
			<bean:define id="executionCourseLink"><c:out value="${pageContext.request.contextPath}" /><c:out value="${inquiryDTO.executionCourse.site.reversePath}" /></bean:define>	
			<!-- NO_CHECKSUM --><!-- HAS_CONTEXT --><a href="<%= executionCourseLink %>" target="_blank" title="Ir para p&aacute;gina da UC"><bean:write name="inquiryDTO" property="executionCourse.nome" /></a>	
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularYear.year" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiryDTO" property="delegate.curricularYear.year" /></td>
	</tr>
</table>
<em><bean:message key="message.yearDelegateInquiries.clickCUPageLink" bundle="INQUIRIES_RESOURCES"/></em>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<style>
.thtop { vertical-align: top; width: 300px;}
.biggerTextarea textarea { width: 400px; height: 100px; }
.biggerInputText input[type="text"] { width: 400px !important; }
</style>

<div class="forminline dinline">
	<div class="relative">
		<fr:form action="/delegateInquiry.do?method=confirm">
			<h4 class="mtop15 mbottom05"><bean:message key="title.yearDelegateInquiries.workLoadOfCU" bundle="INQUIRIES_RESOURCES"/></h4>
			<div class="biggerInputText">
				<fr:edit name="inquiryDTO" property="firstBlock" />
			</div>	

			<h4 class="mtop15 mbottom05"><bean:message key="title.yearDelegateInquiries.organizationAndEvaluationOfCU" bundle="INQUIRIES_RESOURCES"/></h4>
			<div class="biggerInputText">
				<fr:edit name="inquiryDTO" property="secondBlock" />
			</div>
			
			<fr:edit name="inquiryDTO" property="thirdBlock" />
			
			<div class="biggerInputText">
				<fr:edit name="inquiryDTO" property="fourthBlock" />
			</div>
			
			<h4 class="mtop15 mbottom05"><bean:message key="title.yearDelegateInquiries.delegateReflexion" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="inquiryDTO" property="fifthBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value="thtop,biggerTextarea,,,,,,"/>
				</fr:layout>		
			</fr:edit>

			<fr:edit name="inquiryDTO" property="sixthBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value="thtop,biggerTextarea,,,,,,"/>
				</fr:layout>		
			</fr:edit>
			
			<fr:edit name="inquiryDTO" property="seventhBlock" >
				<fr:layout name="tabular-editable" >
					<fr:property name="columnClasses" value="thtop,biggerTextarea,,,,,,"/>
				</fr:layout>
			</fr:edit>

			<h4 class="mtop15 mbottom05"><bean:message key="title.yearDelegateInquiries.reportDisclosureAuthorization" bundle="INQUIRIES_RESOURCES"/></h4>
			<fr:edit name="inquiryDTO" property="eighthBlock" />

			<fr:edit name="inquiryDTO" id="inquiryDTO" visible="false"/>
		
			<p class="mtop2"><bean:message key="message.yearDelegateInquiries.submitInquiryWhenFinnish" bundle="INQUIRIES_RESOURCES"/></p>			
			
			<p class="mtop025 mbottom15"><em><bean:message key="message.inquiries.requiredFieldsMarkedWithAsterisk" bundle="INQUIRIES_RESOURCES"/></em></p>
			<html:submit styleClass="bright"><bean:message key="button.submitInquiry" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>

		<fr:form action="/delegateInquiry.do?method=showCoursesToAnswerPage">
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</fr:form>
		
		<br/>
	</div>
</div>	
