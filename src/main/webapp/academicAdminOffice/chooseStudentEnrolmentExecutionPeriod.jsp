<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
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
			 type="org.fenixedu.academic.dto.administrativeOffice.studentEnrolment.StudentEnrolmentBean"
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
				type="org.fenixedu.academic.domain.AcademicProgram"/>

	<ul class="mvert1">
		<academic:allowed operation="ENROLMENT_WITHOUT_RULES" permission="ACADEMIC_OFFICE_ENROLMENTS_ADMIN" program="<%= degree %>">
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
			<bean:define id="url5">/improvementBolonhaStudentEnrollment.do?method=prepareChooseEvaluationSeason&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
			<html:link action='<%= url5 %>'><bean:message key="label.course.improvementEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
		</li>		
		<li>
			<bean:define id="url6">/specialSeasonBolonhaStudentEnrollment.do?method=prepareChooseEvaluationSeason&amp;scpID=<bean:write name="studentEnrolmentBean" property="studentCurricularPlan.externalId"/>&amp;executionPeriodID=<bean:write name="studentEnrolmentBean" property="executionPeriod.externalId"/></bean:define>
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
		<academic:allowed operation="MOVE_CURRICULUM_LINES_WITHOUT_RULES" permission="ACADEMIC_OFFICE_CURRICULUM_MOVE_LINES_ADMIN" program="<%= degree %>">
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
		      	<fr:property name="columnClasses" value=",smalltxt color888,acenter,acenter,nowrap smalltxt,nowrap smalltxt,acenter"/>
				<fr:property name="sortBy" value="name"/>

				<fr:property name="linkFormat(activate)" value="/studentEnrolments.do?method=activateEnrolment&enrolmentId=\${externalId}&scpID=\${studentCurricularPlan.externalId}&executionPeriodId=\${executionPeriod.externalId}" />
				<fr:property name="key(activate)" value="label.enrolment.activate"/>
				<fr:property name="bundle(activate)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="visibleIf(activate)" value="annulled"/>

				<fr:property name="linkFormat(annul)" value="/studentEnrolments.do?method=annulEnrolment&enrolmentId=\${externalId}&scpID=\${studentCurricularPlan.externalId}&executionPeriodId=\${executionPeriod.externalId}" />
				<fr:property name="key(annul)" value="label.enrolment.annul"/>
				<fr:property name="bundle(annul)" value="ACADEMIC_OFFICE_RESOURCES"/>
				<fr:property name="visibleIfNot(annul)" value="annulled"/>

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
		<fr:view name="studentImprovementEnrolments">
			<fr:schema type="org.fenixedu.academic.domain.EnrolmentEvaluation" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="enrolment.name" key="label.name" />
				<fr:slot name="enrolment.curriculumGroup.fullPath" key="label.group"/>
				<fr:slot name="enrolment.weigthForCurriculum" key="label.set.evaluation.enrolment.weight" />
				<fr:slot name="enrolment.ectsCreditsForCurriculum" key="label.ects.credits" />
				<fr:slot name="enrollmentStateByGrade.description" key="label.set.evaluation.enrolment.state"/>
				<fr:slot name="grade" key="label.set.evaluation.grade.value.simple"/>
				<fr:slot name="evaluationSeason" key="label.evaluationSeason">
					<fr:property name="format" value="${name.content}" />
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value=",smalltxt color888,acenter,acenter,smalltxt,nowrap smalltxt, acenter smalltxt"/>
				<fr:property name="sortBy" value="enrolment.name"/>
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
		<fr:view name="studentSpecialSeasonEnrolments">
			<fr:schema type="org.fenixedu.academic.domain.EnrolmentEvaluation" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="enrolment.name" key="label.name" />
				<fr:slot name="enrolment.curriculumGroup.fullPath" key="label.group"/>
				<fr:slot name="enrolment.weigthForCurriculum" key="label.set.evaluation.enrolment.weight" />
				<fr:slot name="enrolment.ectsCreditsForCurriculum" key="label.ects.credits" />
				<fr:slot name="enrollmentStateByGrade.description" key="label.set.evaluation.enrolment.state"/>
				<fr:slot name="grade" key="label.set.evaluation.grade.value.simple"/>
				<fr:slot name="evaluationSeason" key="label.evaluationSeason">
					<fr:property name="format" value="${name.content}" />
				</fr:slot>
			</fr:schema>
		
			<fr:layout name="tabular">	 
				<fr:property name="classes" value="tstyle2"/>
		      	<fr:property name="columnClasses" value=",smalltxt color888,acenter,acenter,smalltxt,nowrap smalltxt, acenter smalltxt"/>
				<fr:property name="sortBy" value="enrolment.name"/>
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
