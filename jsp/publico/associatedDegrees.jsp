<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<logic:present name="publico.infoCurricularCourses" scope="session">
	<div id="associated-degrees">
		<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
			<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/><br>
		</logic:iterate>
	</div>
</logic:present>	
