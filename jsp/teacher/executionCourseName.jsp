<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView" >
<bean:define id="component" name="siteView" property="commonComponent" />

<h2><bean:message key="message.course.editing" />
&nbsp;<bean:write name="component" property="executionCourse.nome"/></h2>

	<logic:present  name="component" property="associatedDegrees">    
			(&nbsp;
			<logic:iterate id="degree" name="executionCourse" property="degreesSortedByDegreeName">
					<em><bean:write name="degree" property="sigla"/>&nbsp;</em>
			</logic:iterate>
			)
	</logic:present>    
       
       <hr style='color:#ccc'/>
</logic:present>
