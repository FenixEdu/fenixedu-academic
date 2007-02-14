<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="label.enrollment.courses" bundle="STUDENT_RESOURCES"/></h2>
    
    <bean:size id="registrationsSize" name="registrationsToEnrol"/>
    <logic:equal name="registrationsSize" value="0">
    	<span class="error0">
	    	<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.noRegistrationsToEnrol"/>
    	</span>
    </logic:equal>
    <logic:greaterThan name="registrationsSize" value="1">
    	<span class="error0">
    		<bean:message bundle="STUDENT_RESOURCES"  key="label.enrollment.courses.chooseRegistration.invalidNumberOfActiveRegistrations"/>
    	</span>
    </logic:greaterThan>
    
<%--    <logic:notEmpty name="registrationsToEnrol">
	    <html:form action="/bolonhaStudentEnrollment.do" >
	        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseRegistration"/>
	        	<p class="mtop2">
	        		<strong><bean:message  key="label.registration"/>:</strong>
	                	<html:select property="registrationId">
	        			<html:options collection="registrationsToEnrol" property="idInternal" labelProperty="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/>
	        		</html:select>
	        	</p>
	        
	        	<p class="mtop2"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.continue" /></html:submit></p>
	    </html:form>
    </logic:notEmpty>
--%>
    
</logic:present>

