<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="STUDENT">
    <h2><bean:message key="link.shift.enrollment.item2" /></h2>
    
    <html:form action="/studentTimeTable.do" target="_blank">
       	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showTimeTable"/>
       
       <logic:notEmpty name="registrations">
       		<p class="mtop2">
       			<bean:message  key="label.studentCurricularPlan"/>
            	   	<html:select property="registrationId">
       				<html:options collection="registrations" property="externalId" labelProperty="degreeNameWithDegreeCurricularPlanName"/>
       			</html:select>
      	 	</p>
       
       		<p class="mtop2">
       			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
       				<bean:message key="button.continue" />
       			</html:submit>
       		</p>
       	</logic:notEmpty>
       	<logic:empty  name="registrations">
       		<p class="mvert15"><em><bean:message key="message.no.registration" bundle="STUDENT_RESOURCES"/></em></p>
       	</logic:empty>
    </html:form>
</logic:present>

