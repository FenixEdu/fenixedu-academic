<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.upload.abstract"/></h2>

<div class="infoop2 mvert15">
    <p>
        <bean:message key="label.student.thesis.upload.abstract.message"/>
    </p>
</div>

<bean:define id="callbackUrl" type="java.lang.String">/thesisSubmission.do?method=prepareThesisSubmission&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<bean:define id="uploadUrl" type="java.lang.String">/thesisSubmission.do?method=uploadAbstract&amp;thesisId=<bean:write name="thesis" property="externalId"/></bean:define>
<fr:form action="<%= uploadUrl %>" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
        
        <fr:destination name="cancel" path="<%= callbackUrl %>"/>
    </fr:edit>
    
    <html:submit>
        <bean:message key="button.submit"/>
    </html:submit>
    <html:cancel>
        <bean:message key="button.cancel"/>
    </html:cancel>
</fr:form>
