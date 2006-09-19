<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinatior"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.whyUnavailable"/></h2>

<bean:define id="writtenEvaluation" name="writtenEvaluation" type="net.sourceforge.fenixedu.domain.WrittenEvaluation"/>

<li><html:link page="<%= "/vigilancy/convokeManagement.do?method=prepareAddMoreVigilants&writtenEvaluationId=" + writtenEvaluation.getIdInternal() %>">
<bean:message key="label.vigilancy.back" bundle="VIGILANCY_RESOURCES"/></html:link></li>


<fr:view name="vigilant" property="person.name" />
    
<logic:equal name="reason" value="ALREADY_CONVOKED_FOR_ANOTHER_EVALUATION">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.alreadyConvokedForAnotherExam"/>
	<fr:view name="vigilant" property="convokes" schema="presentSimpleConvokes"/>
</logic:equal>

<logic:equal name="reason" value="UNAVAILABLE_PERIOD">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.hasUnavailablePeriod"/>
	<fr:view 
		name="vigilant"
		property="unavailablePeriods"
		schema="unavailableShowForCoordinator">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1" />
		<fr:property name="link(edit)" value="/vigilancy/unavailablePeriodManagement.do?method=editUnavailablePeriodOfVigilant" />
		<fr:property name="param(edit)" value="idInternal/oid" />
		<fr:property name="link(delete)" value="/vigilancy/unavailablePeriodManagement.do?method=deleteUnavailablePeriodOfVigilant" />
		<fr:property name="param(delete)" value="idInternal/oid" />
	</fr:layout>
	</fr:view>
</logic:equal>

<logic:equal name="reason" value="INCOMPATIBLE_PERSON">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.hasIncompatibility"/>
	<fr:view name="vigilant" property="incompatiblePerson.name"/> 
</logic:equal>

<logic:equal name="reason" value="NOT_AVAILABLE_ON_CAMPUS">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.notAvailableOnCampus"/>
</logic:equal>

<logic:equal name="reason" value="SERVICE_EXEMPTION">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.hasServiceExemption"/>
</logic:equal>

<logic:equal name="reason" value="UNKNOWN">
	<bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.unknownCause"/>
</logic:equal>
