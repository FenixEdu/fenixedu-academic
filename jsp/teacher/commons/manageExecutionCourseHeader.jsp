<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="executionCourse">

	<h2>
		<bean:message key="message.course.editing"/>
		&nbsp;
		<bean:write name="executionCourse" property="nome"/>
	</h2>

	(&nbsp;
	<logic:iterate id="degree" name="executionCourse" property="degreesSortedByDegreeName">
			<em><bean:write name="degree" property="sigla"/>&nbsp;</em>
	</logic:iterate>
	)
       
	<hr style='color:#ccc'/>

</logic:present>
