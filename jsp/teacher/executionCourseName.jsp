<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView" >
<bean:define id="component" name="siteView" property="commonComponent" />

	<em>
		<%-- <bean:message key="message.course.editing" /> --%>
		<bean:write name="component" property="executionCourse.nome"/>

		(&nbsp;
		<logic:present name="component" property="associatedDegrees">    
			<logic:iterate id="degree" name="component" property="degrees">
				<bean:write name="degree" property="sigla"/>&nbsp;
			</logic:iterate> 
	    </logic:present>    
		)
	</em>       
<%--
   <hr style='color:#ccc'/>
--%>
</logic:present>
