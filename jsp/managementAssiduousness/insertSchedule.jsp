<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule" /></h2>

<bean:define id="employee" name="UserView" property="person.employee" />

<span class="error0 mtop0"><html:messages id="validation" message="true">
	<bean:write name="validation" />
	<br />
</html:messages></span>
<logic:present name="workScheduleTypeFactory">
<bean:define id="method" value="insertSchedule"/>

<logic:notEmpty name="workScheduleTypeFactory" property="oldIdInternal">
		<bean:define id="method" value="editSchedule"/>
</logic:notEmpty>		

	<fr:form
		action="<%="/assiduousnessParametrization.do?method="+method.toString()%>" encoding="multipart/form-data">
		<fr:edit name="workScheduleTypeFactory" id="acronym"
			schema="workScheduleTypeFactory.acronym"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:hidden slot="modifiedBy" name="UserView"
				property="person.employee" />
		</fr:edit>
		<p><strong>Validade:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="validity"
			schema="workScheduleTypeFactory.validity"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>
		<p><strong>Horário dia:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="dayTime"
			schema="workScheduleTypeFactory.dayTime"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>
		<p><strong>Horário marcações:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="clockingTime"
			schema="workScheduleTypeFactory.clockingTime"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>

		<p><strong>Periodo Normal:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="normalWorkFirstPeriod"
			schema="workScheduleTypeFactory.normalWorkFirstPeriod"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>
		<p></p>
		<fr:edit name="workScheduleTypeFactory" id="normalWorkSecondPeriod"
			schema="workScheduleTypeFactory.normalWorkSecondPeriod"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>

		<p><strong>Periodo Fixo:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="fixedWorkFirstPeriod"
			schema="workScheduleTypeFactory.fixedWorkFirstPeriod"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>
		<p></p>
		<fr:edit name="workScheduleTypeFactory" id="fixedWorkSecondPeriod"
			schema="workScheduleTypeFactory.fixedWorkSecondPeriod"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>

		<p><strong>Refeição:</strong></p>
		<fr:edit name="workScheduleTypeFactory" id="meal"
			schema="workScheduleTypeFactory.meal"
			type="net.sourceforge.fenixedu.domain.assiduousness.util.WorkScheduleTypeFactory">
			<fr:layout name="flow" />
		</fr:edit>


		<p class="mtop2"><input type="submit" /></p>
	</fr:form>
</logic:present>
