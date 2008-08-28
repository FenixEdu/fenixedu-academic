<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="title.studentPortal" bundle="INQUIRIES_RESOURCES"/></em>
<h2><bean:message key="title.inquiries" bundle="INQUIRIES_RESOURCES"/></h2>

<table class="tstyle2 tdtop">
	<tr>
		<td><bean:message key="label.degree.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiriesRegistry" property="student.degree.name" /></td>
	</tr>
	<tr>
		<td><bean:message key="label.curricularCourse.name" bundle="INQUIRIES_RESOURCES"/>:</td>
		<td><bean:write name="inquiriesRegistry" property="executionCourse.nome" /></td>
	</tr>
</table>

<h3 class="separator2 mtop2"><span style="font-weight: normal ;"><bean:message key="title.inquiries.separator.notAnswer" bundle="INQUIRIES_RESOURCES"/></span></h3>

<p class="success0"><bean:message key="label.inquiries.notAnswer.irreversibleAction" bundle="INQUIRIES_RESOURCES"/>:</p>

<p><bean:message key="label.inquiries.notAnswer.reasons" bundle="INQUIRIES_RESOURCES"/>:</p>

<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<div class="forminline dinline">
	<div class="relative">
		<html:form action="/studentInquiry.do">
			<html:hidden property="method" value="justifyNotAnswered"/>
			<html:hidden property="inquiriesRegistryID" />
			
			<p class="mvert05"><html:radio property="notAnsweredJustification" value="UNIT_NOT_ATTENDED" /><strong><bean:message key="label.inquiries.notAnswer.reasons.unitNotAttended" bundle="INQUIRIES_RESOURCES"/></strong></p>
			<p class="mvert05"><html:radio property="notAnsweredJustification" value="OTHER" /><strong><bean:message key="label.inquiries.notAnswer.reasons.other" bundle="INQUIRIES_RESOURCES"/></strong></p>
			<p class="mtop15 mbottom05"><bean:message key="label.inquiries.notAnswer.reasons.otherJustification" bundle="INQUIRIES_RESOURCES"/>:</p>
			<html:textarea property="notAnsweredOtherJustification" cols="60" rows="4"/>
			<br/><br/><br/>
			<html:submit styleClass="bright"><bean:message key="button.submit" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</html:form>
		<html:form action="/studentInquiry.do">
			<html:hidden property="method" value="showCoursesToAnswer"/>
			<html:submit styleClass="bleft"><bean:message key="button.back" bundle="INQUIRIES_RESOURCES"/></html:submit>
		</html:form>		
	</div>
</div>		