<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<html:xhtml/>

<bean:define id="person" name="professorship" property="person" />
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

