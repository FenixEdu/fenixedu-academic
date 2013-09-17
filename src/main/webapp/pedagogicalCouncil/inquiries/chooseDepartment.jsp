<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
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