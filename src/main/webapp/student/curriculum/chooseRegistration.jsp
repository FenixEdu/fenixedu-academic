<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="title.student.portalTitle" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentRegistrations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:notEmpty name="validRegistrations">
<fr:view name="validRegistrations" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>	
		<fr:property name="classes" value="tstyle1 thlight mtop025"/>
		<fr:property name="columnClasses" value="acenter,acenter,,,acenter,"/>
		<fr:property name="linkFormat(view)" value="/viewCurriculum.do?method=prepare&amp;registrationOID=${externalId}" />
		<fr:property name="key(view)" value="view.curricular.plans"/>
		<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="validRegistrations">
<fr:view name="student" property="registrations" schema="student.registrationDetail.short" >
	<fr:layout name="tabular">
		<fr:property name="sortBy" value="startDate=desc"/>	
		<fr:property name="classes" value="tstyle1 thlight mtop025"/>
		<fr:property name="columnClasses" value="acenter,acenter,,,acenter,"/>
		<fr:property name="linkFormat(view)" value="/viewCurriculum.do?method=prepare&amp;registrationOID=${externalId}" />
		<fr:property name="key(view)" value="view.curricular.plans"/>
		<fr:property name="bundle(view)" value="STUDENT_RESOURCES"/>
		<fr:property name="contextRelative(view)" value="true"/>
	</fr:layout>
</fr:view>
</logic:empty>

<h3 class="mbottom025"><bean:message key="label.extraCurricularActivities" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:empty name="student" property="extraCurricularActivitySet">
    <p><em><bean:message key="label.studentExtraCurricularActivities.unavailable" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="student" property="extraCurricularActivitySet">
    <fr:view name="student" property="extraCurricularActivity" schema="student.extraCurricularActivities" >
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop025"/>
            <fr:property name="columnClasses" value=",acenter,acenter"/> 
            <fr:property name="sortBy" value="end=desc"/>	    
        </fr:layout>
    </fr:view>
</logic:notEmpty>
