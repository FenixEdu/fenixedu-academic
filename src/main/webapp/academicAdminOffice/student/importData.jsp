<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.academicAdminOffice.students.importData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<!-- Extra Curricular Activities -->
<h3 class="mbottom025"><bean:message key="label.extraCurricularActivities.import" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
<p class="mvert05">
    <img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
    <html:link action="/importData.do?method=prepareImportExtraCurricularActivities">
        <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.extraCurricularActivities.import" />
    </html:link>
</p>

