<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml/>

<bean:define id="person" name="professorship" property="teacher.person" />
<logic:present name="person" property="homepage">
	<logic:notPresent name="person" property="homepage.activated">
		<bean:write name="person" property="nickname"/>
	</logic:notPresent>
	<logic:present name="person" property="homepage.activated">
		<logic:equal name="person" property="homepage.activated" value="true">			
			<app:contentLink name="person" property="homepage">
				<bean:write name="person" property="nickname"/>
			</app:contentLink>									
		</logic:equal>
		<logic:equal name="person" property="homepage.activated" value="false">
			<p style="margin-top: 6px; margin-bottom: 6px;"><bean:write name="person" property="name"/>
		</logic:equal>
	</logic:present>
</logic:present>
<logic:notPresent name="person" property="homepage">
	<bean:write name="person" property="nickname"/>
</logic:notPresent>

