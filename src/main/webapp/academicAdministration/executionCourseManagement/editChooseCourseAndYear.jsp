<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<fr:view name="sessionBean" property="executionPeriod.qualifiedName" />


<p class="infoop">
	<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseLinkedCourses" />
</p>

<div class="dinline forminline">
	<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
	<!-- fr:edit para permitir a escolha do curso e do ano curricular -->
	<fr:edit id="sessionBeanJSP" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="executionDegree" layout="menu-select-postback" key="label.manager.executionDegrees">
				<fr:property name="format" value="${presentationName}" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionDegreeForExecutionPeriodAcademicAdminProvider" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
			<fr:slot name="curricularYear" layout="menu-select-postback" key="label.manager.executionCourseManagement.curricularYear">
				<fr:property name="format" value="${year}" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CurricularYearsProvider" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="postBack" path="/editExecutionCourseChooseExPeriod.do?method=secondPrepareEditExecutionCourse" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight" />
			<fr:property name="columnClasses" value=",, tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<p class="infoop"><bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseNotLinked" /></p>
	<fr:edit id="sessionBeanJSPAux" name="sessionBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement.ExecutionCourseBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="chooseNotLinked" layout="option-select-postback" key="label.manager.chooseNotLinked">
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="postBack" path="/editExecutionCourseChooseExPeriod.do?method=secondPrepareEditExecutionCourse" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight" />
			<fr:property name="columnClasses" value=",, tdclear tderror1" />
		</fr:layout>
	</fr:edit>
	

	<!-- 
		Se chooseNotLinked=True
			Submit.enable();
		Es
		
		Se chooseNotLinked~=True
			Se executionDegree=Present
				Se curYear=Present
					Submit.enable();
				Es
			Es
		Es
		
		Se chooseNotLinked~=False
			Se executionDegree~=Present
				Submit.disable();
			Es
			Se executionDegree=Present
				Se curYear~=Present
					Submit.disable();
				Es
			ES
		Es
	 -->
	 
	<logic:equal name="sessionBean" property="chooseNotLinked" value="true">
		<html:submit disabled="false">
			<bean:message bundle="MANAGER_RESOURCES" key="button.continue" />
		</html:submit>
	</logic:equal>
	
	<logic:notEqual name="sessionBean" property="chooseNotLinked" value="true">
		<logic:present name="sessionBean" property="executionDegree">
			<logic:present name="sessionBean" property="curricularYear">
				<html:submit disabled="false">
					<bean:message bundle="MANAGER_RESOURCES" key="button.continue" />
				</html:submit>
			</logic:present>
		</logic:present>
	</logic:notEqual>
	
	<logic:notEqual name="sessionBean" property="chooseNotLinked" value="true">
		<logic:notPresent name="sessionBean" property="executionDegree">
			<html:submit disabled="true">
				<bean:message bundle="MANAGER_RESOURCES" key="button.continue" />
			</html:submit>
		</logic:notPresent>
		<logic:present name="sessionBean" property="executionDegree">
			<logic:notPresent name="sessionBean" property="curricularYear">
				<html:submit disabled="true">
					<bean:message bundle="MANAGER_RESOURCES" key="button.continue" />
				</html:submit>
			</logic:notPresent>
		</logic:present>
	</logic:notEqual>
	

</fr:form>
<fr:form action="/editExecutionCourseChooseExPeriod.do?method=prepareEditExecutionCourse">
	<html:submit>
		<bean:message bundle="MANAGER_RESOURCES" key="button.cancel" />
	</html:submit>
</fr:form></div>
<!-- Label e checkbox para ver disciplinas nao associadas??.. 
<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseNotLinked" />
</p>-->