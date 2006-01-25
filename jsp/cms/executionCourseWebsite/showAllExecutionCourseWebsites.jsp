<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ page import="net.sourceforge.fenixedu.domain.cms.website.ExecutionCourseWebsite"%>

<logic:present name="websites">
	<bean:define id="websites" name="websites"/>
	<bean:size id="numberOfWebsites" name="websites"/>
	
	<h3><bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.label" /></h3>
	<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.count.label" arg0="<%=numberOfWebsites.toString()%>"/>	

	<logic:greaterThan name="numberOfWebsites" value="0">	
	<table width="100%" class="style1">
		<tr>
			<td class="listClasses-header" width="15%">Título</td>
			<td class="listClasses-header" width="15%">Nome Disciplina</td>
			<td class="listClasses-header" width="15%">Período Execução</td>
			<td class="listClasses-header" width="0%">Anos Curriculares</td>
			<td class="listClasses-header" width="15%">Cursos</td>
			<td class="listClasses-header" width="33%">Descrição</td>
			<td class="listClasses-header" width="0%">&nbsp;</td>
			<td class="listClasses-header" width="0%">&nbsp;</td>
		</tr>
		<logic:iterate id="website" name="websites" type="net.sourceforge.fenixedu.domain.cms.website.ExecutionCourseWebsite">
		<tr>
			<td class="listClasses">
				<bean:write name="website" property="name"/>
			</td>
			<td class="listClasses">
				<bean:write name="website" property="executionCourse.nome"/>
			</td>					
			<td class="listClasses">
				<bean:write name="website" property="executionCourse.executionPeriod.qualifiedName"/>
			</td>
			<td class="listClasses">
				<%boolean firstCurricularYear=true; %>
				<logic:iterate id="curricularCourse" name="website" property="executionCourse.associatedCurricularCourses"  type="net.sourceforge.fenixedu.domain.CurricularCourse">
					<logic:iterate id="scope" name="curricularCourse" property="scopes" type="net.sourceforge.fenixedu.domain.CurricularCourseScope">
						<logic:equal name="scope" property="active" value="true">
							<%if (!firstCurricularYear)
								out.println(", ");
							 %>
							<bean:write name="scope" property="curricularSemester.curricularYear.year"/>
							<%firstCurricularYear=false; %>
						</logic:equal>
					</logic:iterate>			
				</logic:iterate>
			</td>						
			<td class="listClasses">
				<%firstCurricularYear=true; %>
				<logic:iterate id="curricularCourse" name="website" property="executionCourse.associatedCurricularCourses"  type="net.sourceforge.fenixedu.domain.CurricularCourse">
					<logic:iterate id="scope" name="curricularCourse" property="scopes" type="net.sourceforge.fenixedu.domain.CurricularCourseScope">
						<logic:equal name="scope" property="active" value="true">
							<%if (!firstCurricularYear)
								out.println(", ");
							 %>
							<bean:write name="scope" property="curricularCourse.degreeCurricularPlan.name"/>
							<%firstCurricularYear=false; %>
						</logic:equal>
					</logic:iterate>			
				</logic:iterate>
			</td>								
			<td class="listClasses">
				<bean:write name="website" property="description"/>
			</td>		
			<td class="listClasses">
				<%
				java.util.Map params = new java.util.HashMap();
				params.put("method","delete");
				params.put("websiteId",website.getIdInternal());
				request.setAttribute("params",params);
				 %>
				<html:link name="params" action="/executionCourseWebsiteManagement" module="/cms">
					<bean:message key="cms.executionCourseWebsiteManagement.deleteWebsite.label" bundle="CMS_RESOURCES"/>
				</html:link>
			</td>
			<td class="listClasses">
				<%
				params = new java.util.HashMap();
				params.put("method","view");
				params.put("websiteId",website.getIdInternal());
				request.setAttribute("params",params);
				 %>
				<html:link name="params" action="/executionCourseWebsiteManagement" module="/cms">
					<bean:message key="cms.executionCourseWebsiteManagement.viewWebsite.label" bundle="CMS_RESOURCES"/>
				</html:link>
			</td>
		</tr>												
		</logic:iterate>
	</table>			
	</logic:greaterThan>
</logic:present>

<logic:present name="executionPeriods">
	<html:form action="/executionCourseWebsiteManagement" method="get">  
		<html:hidden property="method" value="prepareChooseExecDegreeAndCurYear" />
		<table width="100%">
			<tr>
				<td width="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.executionPeriodSelection.label" />
					:
				</td>
				<td width="90%">
					<html:select property="executionPeriodID">
						<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/></html:submit>
	</html:form>
</logic:present>

<logic:present name="degrees">	
	<html:form action="/executionCourseWebsiteManagement" method="get">
		<html:hidden property="executionPeriodID"/>
		<html:hidden property="method" value="prepareChooseExecutionCourse" />

		<table width="100%">		
			<tr>
				<td witdh="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.degreeSelection.label"/>
					:
				</td>
				<td witdh="90%">
					<html:select property="executionDegreeID">
						<html:options collection="degrees" property="idInternal" labelProperty="qualifiedName"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td witdh="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.curricularYearSelection.label"/>
					:
				</td>
				<td witdh="90%">
					<html:select property="curricularYear">
						<html:option value="1">1</html:option>
						<html:option value="2">2</html:option>
						<html:option value="3">3</html:option>
						<html:option value="4">4</html:option>
						<html:option value="5">5</html:option>
					</html:select>
				</td>
			</tr>		
		</table>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/>
		</html:submit>
	</html:form>
</logic:present>


<logic:present name="courses">
	<html:form action="/executionCourseWebsiteManagement" method="get">
		<html:hidden property="method" value="createWebsite" />	
			<table width="100%">
				<tr>
					<td>
						<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.website.title.label"/>
					</td>
					<td>
						<html:text property="name"/>
					</td>																
				</tr>
				<tr>
					<td>
						<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.website.description.label"/>
					</td>							
					<td>
						<html:text property="description"/>
					</td>
				</tr>										
				<tr>
					<td valign="top" width="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.courseSelection.label"/>
					:
					</td>
					<td width="100%">
						<table width="90%">
							<tr>	
								<td>		
									<logic:notEmpty name="courses">
										<table width="100%" cellpadding="0" border="0">
											<tr>
												<td>&nbsp;
												</td>
												<td class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.course.label" />
												</td>
											</tr>													
											<bean:define id="viewAction" name="viewAction" type="java.lang.String"/>				
											<logic:iterate id="executionCourse" name="courses" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
												<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
												<tr>	 			
													<td class="listClasses">
														<html:radio idName="executionCourse" property="executionCourseID" value="idInternal"/>
													</td>
													<td class="listClasses" style="text-align:left"><bean:write name="executionCourse" property="nome"/>
													</td>												
								 				</tr>
								 			</logic:iterate>						
										</table>
									</logic:notEmpty>	 	
								</td>
							</tr>
						</table>						
					</td>
				</tr>
			</table>
	<html:submit styleClass="inputbutton">
			<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/>
		</html:submit>
	</html:form>
</logic:present>