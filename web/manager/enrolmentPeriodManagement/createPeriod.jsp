<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period"/></h2>

<h3>Criar Periodo</h3>

<bean:define id="executionSemester" name="executionSemester" />
<bean:define id="executionSemesterId" name="executionSemester" property="externalId" />

<fr:form action="<%= "/manageEnrolementPeriods.do?method=createPeriods&executionSemesterId=" + executionSemesterId %>">
	
	<fr:edit id="enrolmentPeriodManagementBean" name="enrolmentPeriodManagementBean" visible="false" />
	
	<fr:edit id="enrolmentPeriodManagementBean-edit" name="enrolmentPeriodManagementBean">
	
		<fr:schema type="net.sourceforge.fenixedu.domain.enrolmentPeriods.EnrolmentPeriodManagementBean" bundle="MANAGER_RESOURCES">
		
			<fr:slot name="degreeType" required="true" layout="menu-postback">
				<fr:property name="destination" value="degreeTypePostback" />
			</fr:slot>
			
			<fr:slot name="type" required="true" layout="menu-postback">
				<fr:property name="destination" value="enrolmentPeriodManagementTypePostback" />
			</fr:slot>
			
			<fr:slot name="begin" required="true">
			</fr:slot>
			
			<fr:slot name="end" required="true">
			</fr:slot>

			<fr:slot name="degreeCurricularPlanList" required="true" layout="option-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.manager.enrolmentPeriods.DegreeCurricularPlanForCreationProvider" />
				<fr:property name="eachLayout" value="values" />
				<fr:property name="eachSchema" value="DegreeCurricularPlan.view" />
			</fr:slot>

		</fr:schema>
		
		<fr:destination name="degreeTypePostback" path="<%= "/manageEnrolementPeriods.do?method=createPeriodsPostback&executionSemesterId=" + executionSemesterId %>" />
		<fr:destination name="enrolmentPeriodManagementTypePostback" path="<%= "/manageEnrolementPeriods.do?method=createPeriodsPostback&executionSemesterId=" + executionSemesterId %>" />
		<fr:destination name="invalid" path="<%= "/manageEnrolementPeriods.do?method=createPeriodsInvalid&executionSemesterId=" + executionSemesterId %>" />
		<fr:destination name="cancel" path="<%= "/manageEnrolementPeriods.do?method=prepare" %>" />
		
		<fr:layout>
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,terror1 tdclear" />
		</fr:layout>
		
	</fr:edit>
	
	
	<p>
		<html:submit>Criar</html:submit>
		<html:cancel>Cancelar</html:cancel>
	</p>
	
</fr:form>