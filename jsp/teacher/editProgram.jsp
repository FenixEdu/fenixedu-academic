<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>	
<html:form action="/programManagerDA">
		
	<html:text name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program >
	</html:text>
	<html:reset value="clean" styleClass="inputbutton">
          <bean:message key="label.clear"/>
    </html:reset>
    <html:submit property="method" value="editObjectives" titleKey="button.save">
	</html:submit>
</html:form>

