<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.student.thesis.submission"/></h2>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.details"/></h3>

<fr:view name="thesis" schema="student.thesis.details">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.abstract"/></h3>

<fr:view name="thesis" schema="student.thesis.details.abstract">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<html:link page="/thesisSubmission.do?method=editAbstract">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.keywords"/></h3>

<fr:view name="thesis" schema="student.thesis.details.keywords">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<html:link page="/thesisSubmission.do?method=editKeywords">
    <bean:message key="link.student.thesis.submit.edit"/>
</html:link>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <p class="mvert0"><em><bean:message key="label.student.thesis.submit.noDissertation"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" schema="student.thesis.file">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<logic:notEmpty name="thesis" property="dissertation">
    <html:link page="/thesisSubmission.do?method=removeDissertation">
        <bean:message key="link.student.thesis.file.remove"/>
    </html:link>
</logic:notEmpty>

<fr:form action="/thesisSubmission.do?method=uploadDissertation" encoding="multipart/form-data">
    <fr:edit id="dissertationFile" name="fileBean" schema="student.thesisBean.upload">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
            <fr:property name="columnClasses" value=",,tdclear tderror1"/>
        </fr:layout>
    </fr:edit>
    
    <html:submit>
        <bean:message key="submit"/>
    </html:submit>
</fr:form>


<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

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

<logic:notEmpty name="thesis" property="extendedAbstract">
    <html:link page="/thesisSubmission.do?method=removeAbstract">
        <bean:message key="link.student.thesis.file.remove"/>
    </html:link>
</logic:notEmpty>

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

<%-- file submit experience
<table class="tstyle5 tdtop thlight thright">
    <tr>
    <th>
        <bean:message key="label.thesis.file"/>:
    </th>
    <td>
        <fr:form action="/thesisSubmission.do?method=uploadAbstract" encoding="multipart/form-data">
            <fr:edit id="abstractFile2" name="fileBean" slot="file">
                <fr:validator name="net.sourceforge.fenixedu.renderers.validators.FileValidator">
                    <fr:property name="required" value="true"/>
                    <fr:property name="acceptedTypes" value="application/pdf"/>
                </fr:validator>
                
                <fr:layout>
                    <fr:property name="size" value="60"/>
                    <fr:property name="fileNameSlot" value="fileName"/>
                    <fr:property name="fileSizeSlot" value="fileSize"/>
                </fr:layout>
            </fr:edit>
            <html:submit>
                <bean:message key="submit"/>
            </html:submit>
        </fr:form>
        <span class="tderror1 dblock">
            <fr:message for="abstractFile2"/>
        </span>
    </td>
</table>
--%>

