<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.edit.abstract"/></h2>

<div class="infoop2">
    <p class="mvert0"><bean:message key="label.student.thesis.edit.abstract.message"/></p>
</div>

<bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=prepareThesisSubmission&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<fr:edit name="thesis" schema="student.thesis.abstract.edit" action="<%= callbackUrl %>">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight thright mtop05 tdtop"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1 "/>
    </fr:layout>
</fr:edit>
