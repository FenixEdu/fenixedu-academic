<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<h2><bean:message key="title.student.thesis.submission"/></h2>

<div class="infoop2">
    <p>
        <bean:message key="label.student.thesis.confirmed"/>
    </p>
</div>

<fr:view name="thesis" schema="student.thesis.details">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.abstract"/></h3>

<fr:view name="thesis" schema="student.thesis.details.abstract">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.keywords"/></h3>

<fr:view name="thesis" schema="student.thesis.details.keywords">
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle5 thlight mtop05"/>
    </fr:layout>
</fr:view>

<h3><bean:message key="title.student.thesis.submit.dissertation"/></h3>

<logic:empty name="thesis" property="dissertation">
    <bean:message key="label.student.thesis.submit.noDissertation"/>
</logic:empty>

<logic:notEmpty name="thesis" property="dissertation">
    <fr:view name="thesis" property="dissertation" schema="student.thesis.file">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<h3><bean:message key="title.student.thesis.submit.extendedAbstract"/></h3>

<logic:empty name="thesis" property="extendedAbstract">
    <bean:message key="label.student.thesis.submit.noExtendedAbstract"/>
</logic:empty>

<logic:notEmpty name="thesis" property="extendedAbstract">
    <fr:view name="thesis" property="extendedAbstract" schema="student.thesis.file">
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle5 tdtop thlight thright"/>
        </fr:layout>
    </fr:view>
</logic:notEmpty>
