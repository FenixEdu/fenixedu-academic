<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>

	

	<logic:present name="publico.infoCurricularCourses" scope="session">
			<table align="center" cellpadding="2" width="90%">
			<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
				<tr class="timeTable_line" >
					<td class="degreetablestd">
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					</td>					
				</tr>
				</logic:iterate>
			</table>	
			
</logic:present>	
