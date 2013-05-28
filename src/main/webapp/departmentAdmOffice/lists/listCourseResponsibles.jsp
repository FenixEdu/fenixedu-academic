<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>

<em><bean:message key="label.departmentAdmOffice"/></em>
<h2><bean:message key="link.listCourseResponsibles" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<fr:form action="/listCourseResponsibles.do?method=chooseExecutionYearPostBack">
	<fr:edit id="searchBean" name="searchBean" schema="student.list.searchByCurricularCourse.chooseDegree">
		<fr:destination name="executionYearPostBack" path="/listCourseResponsibles.do?method=chooseExecutionYearPostBack"/>
		<fr:destination name="invalid" path="/listCourseResponsibles.do?method=prepareByCurricularCourse"/>	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
</fr:form>

<p/>

<br/>
<logic:present name="searchBean" property="executionYear">
	<bean:define id="statsUrl">/listCourseResponsibles.do?method=downloadStatistics&executionYearId=<bean:write name="searchBean" property="executionYear.externalId"/></bean:define>
	<html:link action="<%= statsUrl %>">
		<html:img border="0" src='<%= request.getContextPath() + "/images/excel.gif"%>' altKey="excel" bundle="IMAGE_RESOURCES"/>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.download.statistics"/>
	</html:link>
</logic:present>

<logic:present name="courseResponsibles">
	<fr:view name="courseResponsibles">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchCourseResponsiblesParametersBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="curricularCourse.name" key="label.curricular.course.from.curriculum"/>
			<fr:slot name="competenceCourse.name" key="label.competence.course.name"/>
			<fr:slot name="degree.sigla" key="degree"/>
			<fr:slot name="campus.name" key="campus"/>
			<fr:slot name="responsible.name" key="label.responsible" layout="link">
				<fr:property name="useParent" value="true"/>
				<fr:property name="linkFormat" value="https://fenix.ist.utl.pt/homepage/${responsible.user.userUId}"/>
			</fr:slot>
			<fr:slot name="executionSemester.semester" key="label.semester"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thleft thlight"></fr:property>
			<fr:property name="sortBy" value="curricularCourse.name,competenceCourse.name,executionSemester.semester,degree.sigla,campus.name,responsible.name"/>
		</fr:layout>
	</fr:view>
</logic:present>