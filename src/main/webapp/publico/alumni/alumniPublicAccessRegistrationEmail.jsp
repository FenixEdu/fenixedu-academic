<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h1><bean:message key="label.alumni.registration" bundle="ALUMNI_RESOURCES" /></h1>

<div class="alumnilogo">

	<h2><bean:message key="label.confirm.email" bundle="ALUMNI_RESOURCES" /> <span class="color777 fwnormal"><bean:message key="label.step.2.3" bundle="ALUMNI_RESOURCES" /></span></h2>
	
	<p><bean:message key="message.confirm.email" bundle="ALUMNI_RESOURCES" /></p>
	<logic:present name="alumniEmailErrorMessage">
		<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
		</html:messages>
		<bean:write name="alumniEmailErrorMessage"  scope="request" />
	</logic:present>

</div>