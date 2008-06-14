<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<logic:present role="MANAGER">
	<h3><bean:message key="student.registrationConclusionProcess"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

	<fr:view name="registrationConclusionBeans"
		schema="RegistrationConclusionBean.viewForCycleWithConclusionProcessedInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight" />
			<fr:property name="rowClasses" value=",,tdhl1,,,,,," />
			
			<fr:property name="linkFormat(edit)" value="/registrationConclusion.do?method=prepareEditForCycle&amp;cycleCurriculumGroupId=${cycleCurriculumGroup.idInternal}"/>
			<fr:property name="key(edit)" value="label.edit"/>
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			<fr:property name="visibleIf(edit)" value="conclusionProcessed" />
			
		</fr:layout>
	</fr:view>
	
	<br/>
	
	<bean:define id="studentId" name="student" property="idInternal" />
	<fr:form action="<%="/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans&amp;studentId=" + studentId%>">
		<html:cancel altKey="cancel.cancel" bundle="HTMLALT_RESOURCES">
			<bean:message  key="label.back" bundle="APPLICATION_RESOURCES"/>
		</html:cancel>
	</fr:form>
</logic:present>


