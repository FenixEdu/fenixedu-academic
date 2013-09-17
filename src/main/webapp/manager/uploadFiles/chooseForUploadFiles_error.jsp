<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.uploadFiles"/></h2>


<html:messages id="msg" message="true">
	<span class="success0"><bean:write name="msg"/></span><br/>
</html:messages>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
	