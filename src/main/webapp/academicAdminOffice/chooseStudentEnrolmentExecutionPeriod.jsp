<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<html:xhtml/>

<h2><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<p class="mtop15 mbottom025"><strong><bean:message key="label.student.enrolment.chooseExecutionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong></p>

<fr:form action="/studentEnrolments.do?method=showDegreeModulesToEnrol">
	<fr:edit id="studentEnrolment"
			 name="studentEnrolmentBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean"
			 schema="student.enrolment.choose.executionPeriod">
		<fr:destination name="postBack" path="/studentEnrolments.do?method=postBack"/>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<logic:present name="studentEnrolmentBean" property="executionPeriod">

	<bean:define id="degree" name="studentEnrolmentBean" property="studentCurricularPlan.degree" 
				type="net.sourceforge.fenixedu.domain.AcademicProgram"/>

	<ul class="mvert1">
		<academic:allowed operation="ENROLMENT_WITHOUT_RULES" program="<%= degree %>">
			<li>
				<bean:define id="url1">/bolonhaStudentEnrollment.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/>&amp;withRules=false</bean:define>
				<html:link action='<%= url1 %>'><bean:message key="label.course.enrolmentWithoutRules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
			</li>
		</academic:allowed>
		<li>
			<bean:define id="url2">/bolonhaStudentEnrollment.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/>&amp;withRules=true</bean:define>
			<html:link action='<%= url2 %>'><bean:message key="label.course.enrolmentWithRules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<br />
		<li>
			<bean:define id="url5">/improvementBolonhaStudentEnrollment.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url5 %>'><bean:message key="label.course.improvementEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>		
		<li>
			<bean:define id="url6">/specialSeasonBolonhaStudentEnrollment.do?method=checkPermission&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url6 %>'><bean:message key="label.course.specialSeasonEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<br />			
		<li>
			<bean:define id="url3">/studentPropaeudeuticEnrolments.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url3 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="PROPAEDEUTICS" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url4">/studentExtraEnrolments.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url4 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="EXTRA_CURRICULAR" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<li>
			<bean:define id="url4">/studentStandaloneEnrolments.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url4 %>'><bean:message key="label.course.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message key="STANDALONE" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<br />
		<academic:allowed operation="MOVE_CURRICULUM_LINES_WITHOUT_RULES" program="<%= degree %>">
			<li>
				<bean:define id="url5">/curriculumLinesLocationManagement.do?method=prepareWithoutRules&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
				<html:link action='<%= url5 %>'><bean:message key="label.course.moveEnrolments.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
			</li>
		</academic:allowed>
		<li>
			<bean:define id="url5">/curriculumLinesLocationManagement.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url5 %>'><bean:message key="label.course.moveEnrolments.with.rules" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		<logic:equal name="studentEnrolmentBean" property="studentCurricularPlan.bolonhaDegree" value="true">
		<li>
			<bean:define id="url6">/optionalCurricularCoursesLocation.do?method=prepare&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/></bean:define>
			<html:link action='<%= url6 %>'><bean:message key="label.optionalCurricularCourses.move" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>
		</logic:equal>
	</ul>

	<br/>
	<fr:form action="/studentEnrolments.do?method=backViewRegistration">
		<fr:edit id="studentEnrolment-back" name="studentEnrolmentBean" visible="false" />
		<html:cancel><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	</fr:form>

	<p class="mtop2 mbottom0"><strong><bean:message key="label.student.enrolments.executionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/>: </strong></p>
	<logic:notEmpty name="studentEnrolments">
		<fr:view name="studentEnrolments" schema="student.show.enrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value="nowrap,acenter,nowrap,smalltxt color888,acenter"/>
				<fr:property name="sortBy" value="name"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentEnrolments">
		<p class="mtop15">
			<em><bean:message key="label.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:empty>
	
	<p class="mtop2 mbottom0"><strong><bean:message key="label.student.improvement.enrolments.executionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/>: </strong></p>
	<logic:notEmpty name="studentImprovementEnrolments">
		<fr:view name="studentImprovementEnrolments" schema="student.show.enrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value="nowrap,acenter,nowrap,smalltxt color888,acenter"/>
				<fr:property name="sortBy" value="name"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentImprovementEnrolments">
		<p class="mtop15">
			<em><bean:message key="label.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:empty>
	
	<p class="mtop2 mbottom0"><strong><bean:message key="label.student.specialSeason.enrolments.executionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/>: </strong></p>
	<logic:notEmpty name="studentSpecialSeasonEnrolments">
		<fr:view name="studentSpecialSeasonEnrolments" schema="student.show.enrolments">
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value="nowrap,acenter,nowrap,smalltxt color888,acenter"/>
				<fr:property name="sortBy" value="name"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	<logic:empty name="studentSpecialSeasonEnrolments">
		<p class="mtop15">
			<em><bean:message key="label.no.enrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em>
		</p>
	</logic:empty>	
	
</logic:present>

<fr:form action="/studentEnrolments.do?method=backViewRegistration">
	<fr:edit id="studentEnrolment-back" name="studentEnrolmentBean" visible="false" />
	<html:cancel><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>
