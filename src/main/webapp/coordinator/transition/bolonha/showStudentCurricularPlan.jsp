<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.renderers.student.curriculum.StudentCurricularPlanRenderer.OrganizationType"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="role(COORDINATOR)">
	<h2><bean:message key="label.curricularPlan"
		bundle="APPLICATION_RESOURCES" /> - <bean:write name="registration" property="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/></h2>
		


	
	<logic:notEmpty name="registration" property="lastStudentCurricularPlan">
		<div class="infoop2 mbottom15">
			<bean:message  key="label.coordinator.transition.bolonha.message.part1" bundle="APPLICATION_RESOURCES"/>
			<bean:message  key="label.coordinator.transition.bolonha.message.part2" bundle="APPLICATION_RESOURCES"/>
		</div>

		<fr:edit name="registration" property="lastStudentCurricularPlan">
			<fr:layout>
				<fr:property name="organizedBy" value="<%=OrganizationType.GROUPS.name()%>" />
				<fr:property name="detailed" value="true" />
			</fr:layout>
		</fr:edit>
	</logic:notEmpty>
	<logic:empty name="registration" property="lastStudentCurricularPlan">
		<i><bean:message  key="label.transition.bolonha.noEquivalences" bundle="STUDENT_RESOURCES"/></i>
	</logic:empty>
		

</logic:present>
