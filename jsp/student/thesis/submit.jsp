<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.submission"/></h2>

<ul>
    <li>
        <html:link page="/thesisSubmission.do?method=downloadIdentificationSheet">
            <bean:message key="link.student.thesis.identification.download"/>
        </html:link>
    </li>
</ul>

<%-- pending items --%>
<logic:notEmpty name="todo">
    <div class="warning0" style="padding: 1em;">
        <strong><bean:message key="label.attention"/>:</strong><br/>
        <ul class="mbottom05">
            <logic:iterate id="condition" name="todo">
                <li>
                    <bean:define id="key" name="condition" property="key" type="java.lang.String"/>
                    <bean:message key="<%= key %>"/>
                </li>
            </logic:iterate>
        </ul>
    </div>
</logic:notEmpty>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.details"/></h3>

<logic:notPresent name="changeDetails">
    <fr:view name="thesis" schema="student.thesis.details">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            <fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
    
    <html:link page="/thesisSubmission.do?method=changeThesisDetails">
        <bean:message key="link.student.thesis.submit.edit"/>
    </html:link>
</logic:notPresent>

<logic:present name="changeDetails">
    <fr:form action="/thesisSubmission.do?method=prepareThesisSubmission">
        <fr:edit id="details" name="thesis" schema="student.thesis.details.edit">
            <fr:layout name="tabular">
            		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
            		<fr:property name="columnClasses" value="width12em,,tderror1"/>
            </fr:layout>
            
            <fr:destination name="invalid" path="/thesisSubmission.do?method=changeThesisDetails"/>
        </fr:edit>
        
        <html:submit>
            <bean:message key="button.submit"/>
        </html:submit>
        <html:cancel>
            <bean:message key="button.cancel"/>
        </html:cancel>
    </fr:form>
</logic:present>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.abstract"/></h3>

<logic:notEqual name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <p class="mvert05"><em><bean:message key="label.thesis.abstract.empty" bundle="APPLICATION_RESOURCES"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="thesisAbstractInBothLanguages" value="true">
    <fr:view name="thesis" schema="student.thesis.details.abstract">
        <fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    		<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:equal>

<html:link page="/thesisSubmission.do?method=editAbstract">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.keywords"/></h3>

<logic:notEqual name="thesis" property="keywordsInBothLanguages" value="true">
    <p class="mvert05"><em><bean:message key="label.thesis.keywords.empty" bundle="APPLICATION_RESOURCES"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="keywordsInBothLanguages" value="true">
    <fr:view name="thesis" schema="student.thesis.details.keywords">
        <fr:layout name="tabular">
        		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        		<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:equal>

<html:link page="/thesisSubmission.do?method=editKeywords">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.declaration"/></h3>

<fr:view name="thesis" schema="student.thesis.details.declaration">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
        <fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<html:link page="/thesisSubmission.do?method=viewDeclaration">
    <bean:message key="label.student.thesis.declaration.view"/>
</html:link>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:notEqual name="thesis" property="declarationAccepted" value="true">
    <p class="mvert0"><em><bean:message key="label.student.thesis.file.declaration.notAccepted"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="declarationAccepted" value="true">
    <logic:empty name="thesis" property="dissertation">
        <p class="mvert0"><em><bean:message key="label.student.thesis.submit.noDissertation"/></em></p>
    </logic:empty>
    
    <logic:notEmpty name="thesis" property="dissertation">
        <fr:view name="thesis" property="dissertation" schema="student.thesis.file.dissertation">
            <fr:layout name="tabular">
    			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
    			<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
    
    <p class="mtop05">
        <logic:notEmpty name="thesis" property="dissertation">
            <html:link page="/thesisSubmission.do?method=removeDissertation">
                <bean:message key="link.student.thesis.file.remove"/>
            </html:link>, 
        </logic:notEmpty>
        <html:link page="/thesisSubmission.do?method=prepareUploadDissertation">
            <bean:message key="link.student.thesis.file.upload"/>
        </html:link>
    </p>
</logic:equal>

<%-- 
<fr:form action="/thesisSubmission.do?method=uploadDissertation" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="submit"/>
    </html:submit>
</fr:form>
--%>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

<logic:notEqual name="thesis" property="declarationAccepted" value="true">
    <p class="mvert0"><em><bean:message key="label.student.thesis.file.declaration.notAccepted"/></em></p>
</logic:notEqual>

<logic:equal name="thesis" property="declarationAccepted" value="true">
    <logic:empty name="thesis" property="extendedAbstract">
        <p class="mvert0"><em><bean:message key="label.student.thesis.submit.noExtendedAbstract"/></em></p>
    </logic:empty>
    
    <logic:notEmpty name="thesis" property="extendedAbstract">
        <fr:view name="thesis" property="extendedAbstract" schema="student.thesis.file">
            <fr:layout name="tabular">
    			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05 thmiddle"/>
    			<fr:property name="columnClasses" value="width12em,,"/>
            </fr:layout>
        </fr:view>
    </logic:notEmpty>
    
    <p class="mtop05">
        <logic:notEmpty name="thesis" property="extendedAbstract">
            <html:link page="/thesisSubmission.do?method=removeAbstract">
                <bean:message key="link.student.thesis.file.remove"/>
            </html:link>,
        </logic:notEmpty>
        <html:link page="/thesisSubmission.do?method=prepareUploadAbstract">
            <bean:message key="link.student.thesis.file.upload"/>
        </html:link>
    </p>
</logic:equal>

<%-- 
<fr:form action="/thesisSubmission.do?method=uploadAbstract" encoding="multipart/form-data">
    <fr:edit id="abstractFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright thmiddle"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="submit"/>
    </html:submit>
</fr:form>
--%>
