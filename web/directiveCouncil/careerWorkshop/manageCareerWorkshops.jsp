<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<em><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="directiveCouncil"/></em>
<h2><bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="title.manageCareerWorkshop" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<fr:form action="/careerWorkshopApplication.do?method=addApplicationEvent">
	<fr:edit name="applicationsBean" id="applicationsBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.careerWorkshop.ManageCareerWorkshopApplicationsBean" bundle="DIRECTIVE_COUNCIL_RESOURCES">
			<fr:slot name="newEventStartDate" key="label.manageCareerWorkshop.startDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="newEventEndDate" key="label.manageCareerWorkshop.endDate">
				<fr:property name="required" value="true"/>
			</fr:slot>
			<fr:slot name="newEventInformation" key="label.manageCareerWorkshop.relatedInformation"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thleft thlight thmiddle mtop05 mbottom05" />
				<fr:property name="columnClasses" value=",,,,tdclear tderror1" />
			</fr:layout>
		</fr:schema>
	</fr:edit>
	<html:submit>
		<bean:message bundle="DIRECTIVE_COUNCIL_RESOURCES" key="button.add" />
	</html:submit>
</fr:form>

<p class="mtop25"></p>
<fr:view name="applicationsBean" property="careerWorkshopApplicationEvents">
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="beginDate=desc"/>
		
		<fr:property name="linkFormat(download)" value="<%="/careerWorkshopApplication.do?method=downloadApplications&eventId=${externalId}"%>"/>
		<fr:property name="order(download)" value="1" />
		<fr:property name="key(download)" value="label.manageCareerWorkshop.downloadApplications" />
		<fr:property name="bundle(download)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(setConfirmationPeriod)" value="<%="/careerWorkshopApplication.do?method=setConfirmationPeriod&eventId=${externalId}"%>"/>
		<fr:property name="order(setConfirmationPeriod)" value="2" />
		<fr:property name="key(setConfirmationPeriod)" value="label.manageCareerWorkshop.setConfirmationPeriod" />
		<fr:property name="bundle(setConfirmationPeriod)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		
		<fr:property name="linkFormat(delete)" value="<%="/careerWorkshopApplication.do?method=deleteApplicationEvent&eventId=${externalId}"%>"/>
		<fr:property name="order(delete)" value="3" />
		<fr:property name="key(delete)" value="label.manageCareerWorkshop.cancelPeriod" />
		<fr:property name="bundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES" />
		<fr:property name="confirmationKey(delete)" value="label.manageCareerWorkshop.cancellationConfirmation"/>
		<fr:property name="confirmationBundle(delete)" value="DIRECTIVE_COUNCIL_RESOURCES"/>
		<fr:property name="confirmationArgs(delete)" value="<%="${formattedBeginDate},${formattedEndDate}"%>"/>
		
		<fr:property name="classes" value="tstyle1 thleft thlight mvert05" />
		<fr:property name="columnClasses" value=",,,,,,,tdclear tderror1" />
	</fr:layout>
	<fr:schema type="net.sourceforge.fenixedu.domain.careerWorkshop.CareerWorkshopApplicationEvent" bundle="DIRECTIVE_COUNCIL_RESOURCES">
		<fr:slot name="formattedBeginDate" key="label.manageCareerWorkshop.startDate" />
		<fr:slot name="formattedEndDate" key="label.manageCareerWorkshop.endDate"/>
		<fr:slot name="relatedInformation" key="label.manageCareerWorkshop.relatedInformation"/>
		<fr:slot name="numberOfApplications" key="label.managerCareerWorkshop.numberOfApplications"/>
		<fr:slot name="confirmationBeginDate" key="label.manageCareerWorkshop.confirmationStartDate"/>
		<fr:slot name="confirmationEndDate" key="label.manageCareerWorkshop.confirmationEndDate"/>
	</fr:schema>
</fr:view>