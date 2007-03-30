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

<div class="infoop2">
    <p>
        <bean:message key="label.student.thesis.confirmed"/>
    </p>
</div>

<h3 class="mtop15 mbottom05"><bean:message key="title.student.thesis.submit.details"/></h3>

<fr:view name="thesis" schema="student.thesis.details">

    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.abstract"/></h3>

<fr:view name="thesis" schema="student.thesis.details.abstract">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.keywords"/></h3>

<fr:view name="thesis" schema="student.thesis.details.keywords">
    <fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
		<fr:property name="columnClasses" value="width12em,,"/>
    </fr:layout>
</fr:view>

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

<h3><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.student.thesis.submit.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" schema="student.thesis.file.dissertation">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<h3><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <p><em><bean:message key="label.student.thesis.submit.noExtendedAbstract"/></em></p>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" schema="student.thesis.file">
        <fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05 mbottom05"/>
			<fr:property name="columnClasses" value="width12em,,"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
