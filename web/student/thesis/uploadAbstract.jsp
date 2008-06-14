<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.upload.abstract"/></h2>

<div class="infoop2 mvert15">
    <p>
        <bean:message key="label.student.thesis.upload.abstract.message"/>
    </p>
</div>

<fr:form action="/thesisSubmission.do?method=uploadAbstract" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        
        <fr:destination name="cancel" path="/thesisSubmission.do?method=prepareThesisSubmission"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.submit"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel"/>
    </html:cancel>
</fr:form>
