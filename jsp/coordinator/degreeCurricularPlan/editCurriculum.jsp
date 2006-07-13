<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.util.Data" %>
<logic:equal name="canEdit" value="true">
	<logic:present name="infoCurriculum">
		<h2><str:upperCase><bean:message key="link.cooordinator.degreeCurricularPlan.edit.curriculum"/></str:upperCase>&nbsp;<str:upperCase><bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></str:upperCase>&nbsp;-&nbsp;<bean:write name="infoCurriculum" property="infoCurricularCourse.name"/></h2>
		<html:form action="/degreeCurricularPlanManagement">
			<bean:define id="infoCurriculumId" name="infoCurriculum" property="idInternal"/>
			<html:hidden property="method" value="editCurriculum"/>
			<html:hidden property="infoCurriculumCode" value="<%=infoCurriculumId.toString()%>"/>
			<html:hidden property="infoExecutionDegreeCode" value="<%=pageContext.findAttribute("infoExecutionDegreeCode").toString()%>"/>
			<html:hidden property="infoCurricularCourseCode" value="<%=pageContext.findAttribute("infoCurricularCourseCode").toString()%>"/>
			<html:hidden property="degreeCurricularPlanID" value="<%=pageContext.findAttribute("degreeCurricularPlanID").toString()%>"/>
			<table>
				<tr>
					<td>
						<table>
							<tr>
								<td width="75%">
										<bean:define id="curricularCourseCode" name="infoCurriculum" property="infoCurricularCourse.idInternal"/>
										(<html:link page="<%="/degreeCurricularPlanManagement.do?method=viewActiveCurricularCourseInformation&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") +"&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode %>">
											<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.current.information"/></font>
										</html:link>)
										<br />
										<bean:define id="curricularCourseName" name="infoCurriculum" property="infoCurricularCourse.name"/>
										(<html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareViewCurricularCourseInformationHistory&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") +"&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + curricularCourseCode + "&amp;infoCurricularCourseName=" + curricularCourseName%>">
											<font color="#0066CC"><bean:message key="link.coordinator.degreeCurricularPlan.see"/>&nbsp;<bean:message key="label.coordinator.degreeCurricularPlan.history.information"/></font>
										</html:link>)
									<br />
								</td>								
								<td width="5%">
									<br />
								</td>
								<td width="20%">
									<%-- <html:link page="<%="/degreeCurricularPlanManagement.do?method=prepareEditCurriculum&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID") + "&amp;infoExecutionDegreeCode=" + pageContext.findAttribute("infoExecutionDegreeCode") +"&amp;infoCurricularCourseCode=" + pageContext.findAttribute("infoCurricularCourseCode") + "&amp;language=English"%>"><bean:message key="label.coordinator.degreeCurricularPlan.curriculum.english"/></html:link> --%>
									<br />
								</td>
							</tr>
						</table>
					</td>
				</tr>
			
				<tr>
					<td><br />
						<b><bean:message key="label.generalObjectives"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="generalObjectives" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.operacionalObjectives"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="operacionalObjectives" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.program"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="program" cols="80" rows="6"/></td>
				</tr>
<%--				<tr>
					<td><br />
						<b><bean:message key="label.generalObjectives.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="generalObjectivesEn" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.operacionalObjectives.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="operacionalObjectivesEn" cols="80" rows="6"/></td>
				</tr>
				<tr>
					<td><br />
						<b><bean:message key="label.program.eng"/></b>
					</td>
				</tr>
				<tr>
					<td><html:textarea property="programEn" cols="80" rows="6"/><br /></td>
				</tr>
--%>				
				<logic:notEmpty name="infoCurriculum" property="lastModificationDate">
					<tr>
						<td>
							<br />
							<bean:define id="lastModificationDate" name="infoCurriculum" property="lastModificationDate" type="java.util.Date"/>
							<b><bean:message key="label.coordinator.degreeSite.lastModificationDate"/>
							<%= Data.format2DayMonthYear(lastModificationDate)%><b>		
							<br />
						</td>
					</tr>
				</logic:notEmpty>
			</table>
			<br />
			<html:submit styleClass="inputbutton"><bean:message key="button.save"/>                    		         	
			</html:submit> 
			<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
			</html:reset>  
		</html:form>
	</logic:present>
</logic:equal>