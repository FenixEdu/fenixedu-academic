<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>
    
    <html:form action="/bolonhaStudentEnrollment.do" >
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseRegistration"/>
        	<p class="mtop2">
        		<strong><bean:message  key="label.studentCurricularPlan"/></strong>
                	<html:select property="registrationId">
        			<html:options collection="registrations" property="idInternal" labelProperty="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/>
        		</html:select>
        	</p>
        
        	<p class="mtop2"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.continue" /></html:submit></p>
    </html:form>
</logic:present>

