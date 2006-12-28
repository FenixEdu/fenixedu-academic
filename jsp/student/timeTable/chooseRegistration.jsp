<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="link.shift.enrollment.item2" /></h2>
    
    <html:form action="/studentTimeTable.do" target="_blank" method="GET">
        	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTimeTable"/>
        
        	<p class="mtop2">
        		<bean:message  key="label.studentCurricularPlan"/>
                	<html:select property="registrationId">
        			<html:options collection="registrations" property="idInternal" labelProperty="lastStudentCurricularPlan.degreeCurricularPlan.presentationName"/>
        		</html:select>
        	</p>
        
        	<p class="mtop2"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue" /></html:submit></p>
    </html:form>
</logic:present>

