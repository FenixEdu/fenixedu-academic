<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="directiveCouncil"/></em>
<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.manageCareerWorkshop" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
<bean:define id="event" name="eventsBean" property="affectedEvent" type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" />
<h3>
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
</h3>

<p class="mtop2"><html:link action="/careerWorkshopApplication.do?method=prepare">
	<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="link.back" />
</html:link></p>

<h4>
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.subTitle" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="formattedBeginDate" />
	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.subTitleSeparator" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="formattedEndDate" />
<%
	if (event.getRelatedInformation() != null) {
 %>
 	<bean:message key="label.manageCareerWorkshop.setConfirmationPeriod.dashSeparator" bundle="DIRECTIVE_COUNCIL_RESOURCES"/>
	<bean:write name="event" property="relatedInformation" />
<%
     }
 %>
</h4>

<fr:form action="/careerWorkshopApplication.do?method=addConfirmationPeriod">
	<fr:edit name="eventsBean" id="eventsBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop.ManageCareerWorkshopApplicationsBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="confirmationStartDate" key="label.manageCareerWorkshop.confirmationStartDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="confirmationEndDate" key="label.manageCareerWorkshop.confirmationEndDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
			<fr:property name="columnClasses" value=",,,,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.confirm" />
	</html:submit>
</fr:form>