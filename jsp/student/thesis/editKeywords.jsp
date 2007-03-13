<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="title.student.thesis.submission"/></h2>

<h3><bean:message key="title.student.thesis.edit.keywords"/></h3>

<div class="infoop2">
    <p>
        <bean:message key="label.student.thesis.edit.keywords.message"/>
    </p>
</div>

<fr:edit name="thesis" schema="student.thesis.keywords.edit"
         action="/thesisSubmission.do?method=prepareThesisSubmission">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:edit>
