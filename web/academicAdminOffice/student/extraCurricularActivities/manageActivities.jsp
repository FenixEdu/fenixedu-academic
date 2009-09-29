<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.extraCurricularActivities.manage" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p>
        <span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<h3 class="mtop15 mbottom025"><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<table class="mtop025">
    <tr>
        <td>
            <fr:view name="student" schema="student.show.personAndStudentInformation">
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
                    <fr:property name="rowClasses" value="tdhl1,,,,"/>
                </fr:layout>
            </fr:view>
        </td>
        <td>
            <bean:define id="personID" name="student" property="person.idInternal"/>
            <html:img align="middle" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
        </td>
    </tr>
</table>

<h3 class="mbottom025"><bean:message key="label.extraCurricularActivities" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>

<logic:empty name="student" property="extraCurricularActivity">
    <p><em><bean:message key="label.studentExtraCurricularActivities.unavailable" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="student" property="extraCurricularActivity">
    <fr:view name="student" property="extraCurricularActivity" schema="student.extraCurricularActivities" >
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle4 thlight"/>
            <fr:property name="linkFormat(delete)" value="/studentExtraCurricularActivities.do?method=deleteActivity&activityId=${externalId}" />
            <fr:property name="key(delete)" value="link.student.extraCurricularActivity.delete"/>
            <fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
            <fr:property name="visibleIf(delete)" value="isDeletable"/>
            <fr:property name="contextRelative(delete)" value="true"/>      
        </fr:layout>
    </fr:view>
</logic:notEmpty>

<bean:define id="studentID" name="student" property="idInternal" />
<h3 class="mtop15 mbottom025"><bean:message key="label.addNewActivity" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<fr:create schema="student.createExtraCurricularActivity" type="net.sourceforge.fenixedu.domain.student.curriculum.ExtraCurricularActivity">
    <fr:hidden slot="student" name="student"/>
    <fr:layout name="tabular">
        <fr:property name="classes" value="tstyle4 thright thlight"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
    <fr:destination name="cancel" path="<%="/student.do?method=visualizeStudent&studentID=" + studentID%>" />
</fr:create>