<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<span class="error"><html:errors bundle="INQUIRIES_RESOURCES" /></span>
<html:messages id="message" message="true" bundle="INQUIRIES_RESOURCES">
    <p><span class="error"><bean:write name="message" /></span></p>
</html:messages>

<h2><bean:message key="title.inquiries.studentInquiry.uploadCourseResults" bundle="INQUIRIES_RESOURCES"/></h2>

<fr:edit id="uploadCourseFileBean" name="uploadCourseFileBean" schema="studentInquiry.uploadCourseResults" action="/uploadStudentInquiriesResults.do?method=submitCourseFile" >
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
        </fr:layout>
</fr:edit>

<br/><br/><br/>

<h2><bean:message key="title.inquiries.studentInquiry.uploadTeachingResults" bundle="INQUIRIES_RESOURCES"/></h2>

<fr:edit id="uploadTeachingFileBean" name="uploadTeachingFileBean" schema="studentInquiry.uploadTeachingResults" action="/uploadStudentInquiriesResults.do?method=submitTeachingFile" >
        <fr:layout name="tabular">
            <fr:property name="classes" value="tstyle1 thlight mtop05 thleft"/>
        </fr:layout>
</fr:edit>
