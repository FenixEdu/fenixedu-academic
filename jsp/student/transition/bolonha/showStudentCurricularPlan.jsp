<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="STUDENT">
	<h2><bean:message key="label.curricularPlan"
		bundle="APPLICATION_RESOURCES" /></h2>

	<div class="infoop2">
		<bean:message  key="label.transition.bolonha.message.part1" bundle="STUDENT_RESOURCES"/>
		<bean:message  key="label.transition.bolonha.message.part2" bundle="STUDENT_RESOURCES"/>
	</div>
	<br/>	
	<logic:notEmpty name="registration" property="lastStudentCurricularPlan.credits">
		<fr:view	name="registration" 
					property="lastStudentCurricularPlan.credits" 
					schema="student.Dismissal.view.dismissals">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="registration" property="lastStudentCurricularPlan.credits">
		<i><bean:message  key="label.transition.bolonha.noEquivalences"/></i>
	</logic:empty>
		

</logic:present>


