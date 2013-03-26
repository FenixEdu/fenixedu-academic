<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<%@page import="net.sourceforge.fenixedu.presentationTier.Action.coordinator.thesis.ThesisPresentationState"%><html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis"/></h2>

<div class="infoop2">
	<p><bean:message key="dissertation.style.guide.info" bundle="APPLICATION_RESOURCES"/></p>
</div>

<logic:empty name="enrolments">
	<em><bean:message key="label.student.thesis.notFound"/></em>
</logic:empty>
<logic:notEmpty name="enrolments">
<table class="tstyle2 thlight thcenter tdcenter mtop050">
	<tr>
		<th>
			<bean:message key="label.executionYear"/>
		</th>
		<th>
			<bean:message key="label.semester"/>
		</th>
		<th>
			<bean:message key="label.degree.name"/>
		</th>
		<th>
			<bean:message key="label.state"/>
		</th>
		<th>
		</th>
	</tr>
	<logic:iterate id="enrolment" type="net.sourceforge.fenixedu.domain.Enrolment" name="enrolments">
		<tr>
			<td>
				<bean:write name="enrolment" property="executionYear.year"/>
			</td>
			<td>
				<bean:write name="enrolment" property="executionPeriod.semester"/>
			</td>
			<td>
				<bean:write name="enrolment" property="curricularCourse.degreeCurricularPlan.degree.presentationName"/>
			</td>
			<td>
				<%
					final ThesisPresentationState thesisPresentationState = ThesisPresentationState.getThesisPresentationState(enrolment.getThesis());
					final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
					final String keySimple = key + ".simple";
				%>
				<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
			</td>
			<td>
				<html:link page="/thesisSubmission.do?method=prepareThesisSubmissionByEnrolment" titleKey="title.student.thesis.submission"
						paramId="enrolmentId" paramName="enrolment" paramProperty="externalId">
					<bean:message key="label.student.thesis.view"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>
</logic:notEmpty>

<br/>
<div class="color888" style="text-indent: 25px">
<%
	for (final ThesisPresentationState thesisPresentationState : ThesisPresentationState.values()) {
		final String key = ThesisPresentationState.class.getSimpleName() + "." + thesisPresentationState.name();
		final String keySimple = key + ".simple";
%>
	<p class="mvert0">
		<bean:message bundle="ENUMERATION_RESOURCES" key="<%= key %>"/>
	</p>
<%
	}
%>
</div>
