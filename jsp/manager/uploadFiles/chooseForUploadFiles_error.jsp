<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="label.uploadFiles"/></h2>


<html:messages id="msg" message="true">
	<span class="sucessfulOperarion"><bean:write name="msg"/></span><br>
</html:messages>
<span class="error">
	<html:errors/>
</span><br/>
	