<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em><bean:message key="pedagogical.council" bundle="PEDAGOGICAL_COUNCIL" /></em>
<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p class="mtop2"><bean:message key="label.choose.department" bundle="PEDAGOGICAL_COUNCIL"/></p>

<fr:view name="departments">
	<fr:schema bundle="PEDAGOGICAL_COUNCIL" type="net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit">
		<fr:slot name="name" key="label.teacher.department"/>
	</fr:schema>
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thlight mtop05"/>
		<fr:property name="linkFormat(view)" value="/viewQucResults.do?method=resumeResults&departmentUnitOID=${externalId}"/>
		<fr:property name="key(view)" value="link.inquiry.viewResults" />
		<fr:property name="bundle(view)" value="INQUIRIES_RESOURCES" />
	</fr:layout>
</fr:view>