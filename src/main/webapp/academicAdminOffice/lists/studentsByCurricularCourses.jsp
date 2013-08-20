<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0">
			<!-- Error messages go here -->
			<bean:write name="message" />
		</span>
	</p>
</html:messages>
<fr:form id="searchForm" action="/studentsListByCurricularCourse.do">

	<html:hidden property="method" value="searchByCurricularCourse" />
	<bean:define id="searchBean" name="searchBean" />
	<bean:define id="semester" name="semester" />
	<bean:define id="curricularYear" name="curricularYear" property="year" />
	<bean:define id="year" name="year" />
	<bean:define id="curricularCourseCode" name="searchBean" property="curricularCourse.externalId" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.searchBean" name="searchBean" property="searchBean"
		value="<%=searchBean.toString()%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.semester" property="semester" value="<%=semester.toString()%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYear" property="curricularYear"
		value="<%=curricularYear.toString()%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="curricularCourseCode"
		value="<%=curricularCourseCode.toString()%>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="year"
		value="<%=year.toString()%>" />

	<h2>
		<bean:write name="searchBean" property="curricularCourse.name" />
		(
		<bean:write name="searchBean" property="degreeCurricularPlan.name" />
		)
	</h2>

	<p class="mtop15 mbottom1">
		<em class="highlight5"> <bean:write name="searchBean" property="executionYear.year" /> - <bean:message
				key="label.period" arg0="<%=year.toString()%>" arg1="<%=semester.toString()%>" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</em>
	</p>

	<logic:present name="enrolmentList">

		<bean:size id="enrolmentListSize" name="enrolmentList" />

		<p class="mtop2">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.studentCurricularPlan.lists.total"
				arg0="<%=enrolmentListSize.toString()%>" />
		</p>

		<logic:greaterThan name="enrolmentListSize" value="0">
			<p class="mtop15 mbottom15">
				<a
					href="javascript:var form = document.getElementById('searchForm');form.method.value='exportInfoToExcel';form.submit();">
					<html:image border="0" src='<%=request.getContextPath() + "/images/excel.gif"%>' altKey="excel"
						bundle="IMAGE_RESOURCES"></html:image> <bean:message key="link.lists.xlsFileToDownload"
						bundle="ACADEMIC_OFFICE_RESOURCES" />
				</a>
			</p>
			<p class="mtop15 mbottom15">
				<a
					href="javascript:var form = document.getElementById('searchForm');form.method.value='exportDetailedInfoToExcel';form.submit();">
					<html:image border="0" src='<%=request.getContextPath() + "/images/excel.gif"%>' altKey="excel"
						bundle="IMAGE_RESOURCES"></html:image> <bean:message key="link.lists.xlsFileToDownload.extended.info"
						bundle="ACADEMIC_OFFICE_RESOURCES" />
				</a>
			</p>
		</logic:greaterThan>

		<fr:view name="enrolmentList">
			<fr:schema type="net.sourceforge.fenixedu.domain.Enrolment" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="studentCurricularPlan.registration.number" key="label.number" readOnly="true" layout="link">
					<fr:property name="linkFormat"
						value="/academicAdministration/viewStudentCurriculum.do?method=prepare&amp;registrationOID=${studentCurricularPlan.registration.externalId}" />
					<fr:property name="contextRelative" value="true" />
					<fr:property name="useParent" value="true" />
				</fr:slot>
				<fr:slot name="studentCurricularPlan.registration.person.istUsername" key="label.username" readOnly="true" />
				<fr:slot name="studentCurricularPlan.registration.person.name" key="label.name" readOnly="true" />
				<fr:slot name="studentCurricularPlan.registration.registrationAgreement" key="label.registrationAgreement"
					readOnly="true" />
				<fr:slot name="studentCurricularPlan.degreeCurricularPlan.degree.sigla" key="label.degree" readOnly="true" />
				<fr:slot name="enrollmentState" key="label.state" />
				<fr:slot name="enrolmentEvaluationType" key="label.epoca" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter" />
			</fr:layout>
		</fr:view>
	</logic:present>
</fr:form>
