<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView" >
<bean:define id="component" name="siteView" property="commonComponent" />

<h2><bean:message key="message.course.editing" />
&nbsp;<bean:write name="component" property="executionCourse.nome"/></h2>

	<logic:present  name="component" property="associatedDegrees">    
		     <logic:iterate id="degree" name="component" property="degrees">
				<small><em> <bean:write name="degree" property="sigla"/></em></small> <br/>
		      </logic:iterate> 
       </logic:present>    
       
       <hr style='color:#ccc'/>
</logic:present>
