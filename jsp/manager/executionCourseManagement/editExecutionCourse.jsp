<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName"/>
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
<logic:present name="<%=SessionConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome"/>
 	> <bean:write name="executionCourseName"/>
	<bean:define id="executionCourseId" name="<%=SessionConstants.EXECUTION_COURSE%>" property="idInternal"/>
	<ul>
		<li>
			<html:link page="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegreeName=" + pageContext.findAttribute("executionDegreeName") + "&amp;executionPeriodName=" + pageContext.findAttribute("executionPeriodName") + "&amp;executionPeriodId=" + pageContext.findAttribute("executionPeriodId") + "&amp;executionCourseName=" + executionCourseName.toString()%>">
				<bean:message key="link.manager.executionCourseManagement.associate"/>
			</html:link>
		</li>
	</ul>
	<bean:define id="curricularCourses" name="<%=SessionConstants.EXECUTION_COURSE%>" property="associatedInfoCurricularCourses"/>
	<table>
		<tr>	
			<td>	
				<b><bean:message key="label.manager.executionCourseManagement.curricularCoursesList" /></b>
				<logic:notEmpty name="curricularCourses">
					<table width="100%" cellpadding="0" border="0">
						<tr>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.curricularCourse" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.code" />
							</td>
							<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.underGraduate" />
							</td>
							<td class="listClasses-header">&nbsp;
							</td>
						</tr>
			
						<logic:iterate id="curricularCourse" name="curricularCourses" type="DataBeans.InfoCurricularCourse">
							<tr>	 			
								<td class="listClasses" style="text-align:left"><bean:write name="curricularCourse" property="name"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
								</td>
								<td class="listClasses"><bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
								</td>
								<td class="listClasses">
								<bean:define id="curricularCourseId" name="curricularCourse" property="idInternal"/> 
									&nbsp;<html:link page="<%="/editExecutionCourseManageCurricularCourses.do?method=dissociateCurricularCourse&amp;curricularCourseId=" + pageContext.findAttribute("curricularCourseId") + "&amp;executionCourseId=" + pageContext.findAttribute("executionCourseId") + "&amp;executionDegreeName=" + pageContext.findAttribute("executionDegreeName") + "&amp;executionPeriodName=" + pageContext.findAttribute("executionPeriodName") + "&amp;executionPeriodId=" + pageContext.findAttribute("executionPeriodId")%>">
										<bean:message key="button.manager.teachersManagement.dissociate"/>
									</html:link>&nbsp;
								</td>
			 				</tr>
			 			</logic:iterate>						
					</table>
				</logic:notEmpty>	
				<logic:empty name="curricularCourses">
					<bean:define id="executionCourseName" name="<%=SessionConstants.EXECUTION_COURSE%>" property="nome"/>
					<p><i><bean:message key="message.manager.executionCourseManagement.noCurricularCourses" arg0="<%=executionCourseName.toString()%>" /></i></p>
				</logic:empty> 	
			</td>
		</tr>
	</table>
</logic:present>