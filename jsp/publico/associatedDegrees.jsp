<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<logic:present name="siteView" >
	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCourses" name="component" property="associatedDegrees" />
	<div id="associated-degrees">
		<logic:iterate id="curricularCourse" name="curricularCourses" >
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/> em 
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/><br>
		</logic:iterate>
	</div>
</logic:present>	
