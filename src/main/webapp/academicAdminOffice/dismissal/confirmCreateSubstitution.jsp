<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="link.student.create.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<p class="mtop2">
<span class="showpersonid">
	<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
	<fr:view name="dismissalBean" property="studentCurricularPlan.student" schema="student.show.personAndStudentInformation.short">
		<fr:layout name="flow">
			<fr:property name="labelExcluded" value="true"/>
		</fr:layout>
	</fr:view>
</span>
</p>

<p class="breadcumbs">
	<span><bean:message key="label.studentDismissal.step.one.substitution" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span><bean:message key="label.studentDismissal.step.two" bundle="ACADEMIC_OFFICE_RESOURCES"/></span> &gt; 
	<span class="actual"><bean:message key="label.studentDismissal.step.three" bundle="ACADEMIC_OFFICE_RESOURCES"/></span>
</p>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.substitution.origin" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
<logic:notEmpty name="dismissalBean" property="selectedEnrolments">
	<fr:view name="dismissalBean" property="selectedEnrolments" schema="student.dismissal.view.equivalences">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05"/>
			<fr:property name="columnClasses" value="acenter,,acenter,"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="dismissalBean" property="selectedEnrolments">
	<p><em><bean:message key="label.studentDismissal.no.selected.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></em></p>
</logic:empty>


<logic:equal name="dismissalBean" property="dismissalType.name" value="CURRICULAR_COURSE_CREDITS">
	<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />
	<fr:form action="<%= "/studentSubstitutions.do?scpID=" + scpID.toString() %>">
		<html:hidden property="method" value="createDismissals"/>
		<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>
		
		<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.equivalents" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<logic:notEmpty name="dismissalBean" property="dismissals">
			<fr:hasMessages for="dismissalBean-dismissals">
				<fr:messages for="dismissalBean-dismissals" type="validation">
					<p class="mvert05"><span class="error0"><fr:message show="label"/>: <fr:message /></span></p>
				</fr:messages>
			</fr:hasMessages>
			<fr:edit id="dismissalBean-dismissals" name="dismissalBean" property="dismissals" schema="DismissalBean.SelectedCurricularCourse.chooseParent">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight mtop05"/>
					<fr:property name="columnClasses" value="acenter,,"/>
				</fr:layout>
				<fr:destination name="invalid" path="/studentSubstitutions.do?method=stepThree"/>
			</fr:edit>
		</logic:notEmpty>
		<logic:notEmpty name="dismissalBean" property="optionalDismissals">
			<logic:notEmpty name="dismissalBean" property="dismissals"><br/></logic:notEmpty>
			<fr:hasMessages for="dismissalBean-optionalDismissals">
				<fr:messages for="dismissalBean-optionalDismissals" type="validation">
					<p class="mvert05"><span class="error0"><fr:message show="label"/>: <fr:message /></span></p>
				</fr:messages>
			</fr:hasMessages>
			<fr:edit id="dismissalBean-optionalDismissals" name="dismissalBean" property="optionalDismissals" schema="DismissalBean.SelectedOptionalCurricularCourse.chooseParent">
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="tstyle4 thlight mtop05"/>
					<fr:property name="columnClasses" value=",,,"/>
				</fr:layout>
				<fr:destination name="invalid" path="/studentCredits.do?method=stepThree"/>
			</fr:edit>
		</logic:notEmpty>
	
		<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>
		<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "substitution.DismissalBean.DismissalType." + dismissalType %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/studentSubstitutions.do?method=stepThree"/>
		</fr:edit>
	
		<p class="mtop15">
			<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
			<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
			<html:cancel onclick="this.form.method.value='manage'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		</p>
	</fr:form>

</logic:equal>


<logic:equal name="dismissalBean" property="dismissalType.name" value="CURRICULUM_GROUP_CREDITS">
	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.final.group" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<fr:view name="dismissalBean" schema="student.dismissal.view.final.group">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05" />
		</fr:layout>
	</fr:view>
	<logic:notEmpty name="dismissalBean" property="allDismissals">
		<p class="mvert05"><strong><bean:message key="label.studentDismissal.not.need.to.enrol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
		<fr:view name="dismissalBean" property="allDismissals" schema="student.dismissal.view.not.need.to.enrol">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight mtop05" />
				<fr:property name="columnClasses" value="acenter,,"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
	<div class="warning0 mvert15" style="padding: 0.5em; width: 400px;">
		<p class="mtop0 mbottom05">
			<bean:message key="label.studentDismissal.add.not.need.to.enrol" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</p>
		<html:form action="/studentSubstitutions.do?method=prepareChooseNotNeedToEnrol">
			<fr:edit id="dismissalBeanNotNeedToEnrol" name="dismissalBean" visible="false"/>
			<html:submit><bean:message key="button.insert" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit> 
		</html:form>	
	</div>
	


	<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />
	<fr:form action="<%= "/studentSubstitutions.do?scpID=" + scpID.toString() %>">
		<html:hidden property="method" value="createDismissals"/>
		
		<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>
	
		<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>
		<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "substitution.DismissalBean.DismissalType." + dismissalType %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/studentSubstitutions.do?method=stepThree"/>
		</fr:edit>
	
		<p>
			<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
			<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
			<html:cancel onclick="this.form.method.value='manage'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		</p>
	</fr:form>
</logic:equal>


<logic:equal name="dismissalBean" property="dismissalType.name" value="NO_COURSE_GROUP_CURRICULUM_GROUP_CREDITS">
	<p class="mtop15 mbottom05"><strong><bean:message key="label.studentDismissal.final.group" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></p>
	
	<fr:view name="dismissalBean" schema="student.dismissal.view.final.no.course.group">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight" />
		</fr:layout>
	</fr:view>
	
	
	<bean:define id="scpID" name="dismissalBean" property="studentCurricularPlan.externalId" />
	<fr:form action="<%= "/studentSubstitutions.do?scpID=" + scpID.toString() %>">
		<html:hidden property="method" value="createDismissals"/>
		
		<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>
	
		<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>
		<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "substitution.DismissalBean.DismissalType." + dismissalType %>">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/studentSubstitutions.do?method=stepThree"/>
		</fr:edit>
	
		<p>
			<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
			<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
			<html:cancel onclick="this.form.method.value='manage'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
		</p>
	</fr:form>
</logic:equal>

