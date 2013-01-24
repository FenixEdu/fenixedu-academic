<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<br />
<h2><bean:message key="label.manage.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:edit name="extraCurricularActivityType" action="/manageExtraCurricularActivities.do?method=list">
    <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
        type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType">
        <fr:slot name="namePt" key="label.extraCurricularActivityTypes.namePt" required="true" />
        <fr:slot name="nameEn" key="label.extraCurricularActivityTypes.nameEn" required="true" />
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle4 thlight tdcenter mtop05" />
    </fr:layout>
</fr:edit>