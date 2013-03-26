<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.candidacy.dfa.periodsManagement.registrationPeriod" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error">
		<bean:write name="message" />
	</span>
	<br />
</html:messages>

<h3><strong><bean:write name="executionDegree" property="degree.name"/> - <bean:write name="executionDegree" property="degreeCurricularPlan.name"/> </strong></h3>



<bean:define id="executionYearId" name="executionDegree" property="executionYear.idInternal" />
<logic:present name="registrationPeriod">
	<fr:hasMessages for="editRegistrationPeriod" type="conversion">
		<ul>
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
		</ul>
	</fr:hasMessages>
	<fr:edit id="editRegistrationPeriod"
			name="registrationPeriod"
			schema="RegistrationPeriodInDegreeCurricularPlan.edit"
			action="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:destination name="cancel" path="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>"/>
	</fr:edit>
</logic:present>

<logic:notPresent name="registrationPeriod">
	<fr:hasMessages for="createRegistrationPeriod" type="conversion">
		<ul>
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
		</ul>
	</fr:hasMessages>
	<fr:create 	id="createRegistrationPeriod"
				schema="RegistrationPeriodInDegreeCurricularPlan.create"
				type="net.sourceforge.fenixedu.domain.RegistrationPeriodInDegreeCurricularPlan"
				action="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:hidden slot="degreeCurricularPlan" name="executionDegree" property="degreeCurricularPlan"/>
		<fr:hidden slot="executionYear" name="executionDegree" property="executionYear"/>
		<fr:destination name="cancel" path="<%="/dfaPeriodsManagement.do?method=showExecutionDegrees&executionYearId=" + executionYearId %>"/>
	</fr:create>
</logic:notPresent>

