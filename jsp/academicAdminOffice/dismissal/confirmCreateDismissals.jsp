<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentDismissal.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<br/>
<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
	<br/>
</html:messages>
<fr:view name="dismissalBean" schema="DismissalBean.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4" />
	</fr:layout>
</fr:view>
<br/>
<h3><u><bean:message key="label.studentDismissal.confirmCreateDismissals" bundle="ACADEMIC_OFFICE_RESOURCES"/></u></h3>

<fr:form action="/studentDismissals.do">
	<html:hidden property="method" value="createDismissals"/>
	
	<fr:edit id="dismissalBean" name="dismissalBean" visible="false"/>
	
	<bean:define id="dismissalType" name="dismissalBean" property="dismissalType.name"/>
	<fr:edit id="dismissalBean-information" name="dismissalBean" schema="<%= "DismissalBean.DismissalType." + dismissalType %>">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
		<fr:destination name="invalid" path="/studentDismissals.do?method=stepThree"/>
	</fr:edit>

	<h3><bean:message key="label.studentDismissal.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:notEmpty name="dismissalBean" property="selectedEnrolments">
	<fr:view name="dismissalBean" property="selectedEnrolments" schema="equivalence.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
		</fr:layout>
	</fr:view>
	</logic:notEmpty>
	<logic:empty name="dismissalBean" property="selectedEnrolments">
		<em><bean:message key="label.studentDismissal.no.selected.equivalences" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		<br/>
	</logic:empty>

	<h3><bean:message key="label.studentDismissal.equivalents" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	
	<logic:notEmpty name="dismissalBean" property="courseGroup">
		<fr:view name="dismissalBean" property="courseGroup" schema="DismissalBean.CourseGroup">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	<logic:notEmpty name="dismissalBean" property="dismissals">
		<fr:view name="dismissalBean" property="dismissals" schema="DismissalBean.SelectedCurricularCourse">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

	<br/>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='stepTwo'; return true;"><bean:message key="button.back" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
	<html:cancel onclick="this.form.method.value='back'; return true;"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</fr:form>