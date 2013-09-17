<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<br />
<h2><bean:message key="label.manage.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:create id="createActivityType" action="/manageExtraCurricularActivities.do?method=list"
    type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType">
    <fr:schema bundle="ACADEMIC_OFFICE_RESOURCES"
        type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivityType">
        <fr:slot name="namePt" key="label.extraCurricularActivityTypes.namePt" required="true" />
        <fr:slot name="nameEn" key="label.extraCurricularActivityTypes.nameEn" required="true" />
    </fr:schema>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle4 thlight tdcenter mtop05" />
    </fr:layout>
</fr:create>