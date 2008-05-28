<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.publicRelationOffice" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.student.reports" bundle="APPLICATION_RESOURCES"/></h2>

<fr:edit name="studentReportPredicate"
		 schema="student.report.predicate"
		 type="net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport$StudentReportPredicate"
		 >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>

<br/>
<br/>

<logic:equal name="studentReportPredicate" property="areRequiredFieldsFilledOut" value="true">
	<h3>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.report.for.criteria.prefix"/>
	<logic:notPresent name="studentReportPredicate" property="degreeType">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.report.for.criteria.all.degree.types"/>:
	</logic:notPresent>
	<logic:present name="studentReportPredicate" property="degreeType">
		<bean:define id="degreeTypeString"><bean:write name="studentReportPredicate" property="degreeType.localizedName"/></bean:define>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.report.for.criteria.degree.type" arg0="<%= degreeTypeString %>"/>:
	</logic:present>
	<bean:define id="yearString" type="java.lang.String" name="studentReportPredicate" property="executionYear.year"/>
	<ul>
		<logic:equal name="studentReportPredicate" property="concluded" value="true">
			<li>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.report.for.criteria.concluded" arg0="<%= yearString %>"/>
			</li>
		</logic:equal>
		<logic:equal name="studentReportPredicate" property="active" value="true">
			<li>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.report.for.criteria.active" arg0="<%= yearString %>"/>
			</li>
		</logic:equal>
	</ul>
	</h3>

	<br/>
	<br/>

	<bean:define id="url" type="java.lang.String">/studentReports.do?method=download&amp;executionYearId=<bean:write name="studentReportPredicate" property="executionYear.idInternal"/><logic:present name="studentReportPredicate" property="degreeType">&amp;degreeType=<bean:write name="studentReportPredicate" property="degreeType"/></logic:present>&amp;concluded=<bean:write name="studentReportPredicate" property="concluded"/>&amp;active=<bean:write name="studentReportPredicate" property="active"/></bean:define>
	<html:link action="<%= url %>">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.student.report.download"/>
	</html:link>

</logic:equal>

