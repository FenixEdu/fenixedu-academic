<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>

<html:form action="/announcementManagementAction">
		
	<html:text name="insertAnnouncementForm" property="title" >
	</html:text>
	<html:text name="insertAnnouncementForm" property="information" >
	</html:text>

	<html:reset value="clean" styleClass="inputbutton"><bean:message key="label.clear"/>
    </html:reset>

    <html:submit property="method" value="button.save" titleKey="button.save">
	</html:submit>
	
</html:form>