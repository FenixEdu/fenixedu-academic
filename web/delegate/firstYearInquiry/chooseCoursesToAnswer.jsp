<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<br/>

<bean:define id="delegateID" name="delegate" property="idInternal" />

<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>

<logic:present name="executionPeriod" property="delegateInquiryResponsePeriod">
	<logic:notEmpty name="executionPeriod" property="delegateInquiryResponsePeriod.introduction">	
		<div style="border: 1px solid #ddd; padding: 5px 15px; background: #fafafa;">
			<bean:write name="executionPeriod" property="delegateInquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.yearDelegateInquiries.separator.notAnswered" bundle="INQUIRIES_RESOURCES"/></span></h3>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.curricularYear.year" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="delegate" property="curricularYear.year" /></td>
	</tr>
</table>

<table class="tstyle2 tdtop">
	<logic:iterate id="executionCourse" name="notAnsweredExecutionCourses">
		<bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
		<tr>
			<td>
				<bean:write name="executionCourse" property="nome"/>		
			</td>
			<td>
				<html:link page="<%= "/delegateInquiry.do?method=showFillInquiryPage&executionCourseID=" + executionCourseID + "&delegateID=" + delegateID %>" >	
					<bean:message key="link.teachingInquiries.fillInquiry" bundle="INQUIRIES_RESOURCES"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.yearDelegateInquiries.separator.answered" bundle="INQUIRIES_RESOURCES"/></span></h3>

<table class="tstyle2 tdtop">
	<logic:iterate id="executionCourse" name="answeredExecutionCourses">
		<tr><td>
		<bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
		<bean:write name="executionCourse" property="nome"/>
		</td></tr>
	</logic:iterate>
</table>	