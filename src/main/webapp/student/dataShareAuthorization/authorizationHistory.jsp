<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml />

<logic:present name="student">
    <em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES" /></em>
    <h2><bean:message key="title.student.dataShareAuthorizations" bundle="STUDENT_RESOURCES" /></h2>

    <html:link action="/studentDataShareAuthorization.do?method=manageAuthorizations">
        <bean:message key="link.back" bundle="COMMON_RESOURCES" />
    </html:link>

    <fr:view name="student" property="studentDataShareAuthorizationSet">
        <fr:schema bundle="STUDENT_RESOURCES" type="net.sourceforge.fenixedu.domain.student.StudentDataShareAuthorization">
            <fr:slot name="since" key="label.student.dataShareAuthorizationDate" />
            <fr:slot name="authorizationChoice" key="label.student.dataShareAuthorization" />
        </fr:schema>
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 mtop1 tdcenter mtop05" />
            <fr:property name="sortBy" value="since=desc" />
        </fr:layout>
    </fr:view>
</logic:present>