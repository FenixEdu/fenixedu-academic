<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />



<h3><bean:message key="title.addCoordinator"/></h3>
<html:form action="/addCoordinator">
<span class="error"><html:errors/></span><br/>
<bean:define id="infoExecutionDegreeId" name="infoExecutionDegreeId"/>
<html:hidden property="infoExecutionDegreeId" value="<%=  infoExecutionDegreeId.toString() %>"/>
<html:hidden property="method" value="AddCoordinator" />
<html:hidden property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>
<html:hidden property="page" value="1" />
<strong><bean:message key="label.teacherNumber"/>&nbsp;</strong><html:text property="teacherNumber" />

<html:submit><bean:message key="label.submit"/></html:submit>
<br/>
<br/>

</html:form>




