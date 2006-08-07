<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/copySiteExecutionCourse">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="copySite"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="executionCourseID"/>		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3" />

	<p><strong><bean:message key="label.selectFeaturesToCopy" bundle="APPLICATION_RESOURCES"/>:</strong></p>
		
	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copySectionsAndItems" property="sectionsAndItems" ><bean:message key="checkbox.copySectionsAndItems"/></html:checkbox><br/>
	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copyBibliographicReference" property="bibliographicReference" ><bean:message key="checkbox.copyBibliographicReference"/></html:checkbox><br/>
	<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.copyEvaluationMethod" property="evaluationMethod" ><bean:message key="checkbox.copyEvaluationMethod"/></html:checkbox><br/>
	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="lable.choose"/>
	</html:submit>

</html:form>
