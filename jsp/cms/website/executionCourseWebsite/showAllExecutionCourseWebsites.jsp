<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
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
	<table width="100%">
		<tr>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.type.label" bundle="CMS_RESOURCES"/></th>
            <th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.title.label" bundle="CMS_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.executionCourse.label" bundle="CMS_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.executionPeriod.label" bundle="CMS_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.curricularYear.label" bundle="CMS_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.degree.label" bundle="CMS_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="cms.executionCourseWebsiteManagement.website.description.label" bundle="CMS_RESOURCES"/></th>
            <th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header">&nbsp;</th>
			<th class="listClasses-header">&nbsp;</th>
		</tr>
		<logic:iterate id="website" name="websites" type="net.sourceforge.fenixedu.domain.cms.website.ExecutionCourseWebsite">
		<tr>
        
            <td class="listClasses">
                <bean:write name="website" property="websiteType.name"/>
            </td>
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
                params.put("method","view");
                params.put("websiteId",website.getIdInternal());
                request.setAttribute("params",params);
                 %>
                <html:link name="params" action="/executionCourseWebsiteManagement" module="/cms">
                    <bean:message key="cms.executionCourseWebsiteManagement.viewWebsite.label" bundle="CMS_RESOURCES"/>
                </html:link>
            </td>
            <td class="listClasses">
                <%
                params = new java.util.HashMap();
                params.put("method", "edit");
                params.put("oid", website.getIdInternal());
                request.setAttribute("params",params);
                 %>
                <html:link name="params" action="/websiteManagement" module="/cms">
                    <bean:message key="cms.executionCourseWebsiteManagement.editWebsite.label" bundle="CMS_RESOURCES"/>
                </html:link>
            </td>
			<td class="listClasses">
				<%
				params = new java.util.HashMap();
				params.put("method","delete");
				params.put("websiteId",website.getIdInternal());
				request.setAttribute("params",params);
				 %>
				<html:link name="params" action="/executionCourseWebsiteManagement" module="/cms">
					<bean:message key="cms.executionCourseWebsiteManagement.deleteWebsite.label" bundle="CMS_RESOURCES"/>
				</html:link>
			</td>
		</tr>												
		</logic:iterate>
	</table>			
	</logic:greaterThan>
</logic:present>

<logic:present name="executionPeriods">
	<html:form action="/executionCourseWebsiteManagement" method="get">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareChooseExecDegreeAndCurYear" />
		<table width="100%">
			<tr>
				<td width="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.executionPeriodSelection.label" />
					:
				</td>
				<td width="90%">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID">
						<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/></html:submit>
	</html:form>
</logic:present>

<logic:present name="degrees">	
	<html:form action="/executionCourseWebsiteManagement" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodID" property="executionPeriodID"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareChooseExecutionCourse" />

		<table width="100%">		
			<tr>
				<td witdh="10%">
					<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.degreeSelection.label"/>
					:
				</td>
				<td witdh="90%">
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID">
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
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear">
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
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/>
		</html:submit>
	</html:form>
</logic:present>


<logic:present name="courses">
	<html:form action="/executionCourseWebsiteManagement" method="get">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createWebsite" />	
			<table width="100%">
				<tr>
					<td>
						<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.website.title.label"/>
					</td>
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name"/>
					</td>																
				</tr>
				<tr>
					<td>
						<bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.website.description.label"/>
					</td>							
					<td>
						<html:text bundle="HTMLALT_RESOURCES" altKey="text.description" property="description" size="60"/>
					</td>
				</tr>		
                <tr>
                    <td>
                        <bean:message bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.website.type.label"/>
                    </td>                           
                    <td>
                        <html:select bundle="HTMLALT_RESOURCES" altKey="select.websiteTypeID" property="websiteTypeID">
						  <html:options collection="websiteTypes" property="idInternal" labelProperty="name"/>
        					</html:select>
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
												<th class="listClasses-header"><bean:message  bundle="CMS_RESOURCES" key="cms.course.label" />
												</th>
											</tr>													
											<bean:define id="viewAction" name="viewAction" type="java.lang.String"/>				
											<logic:iterate id="executionCourse" name="courses" type="net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse">
												<bean:define id="infoExecutionPeriod" name="executionCourse" property="infoExecutionPeriod"/>
												<tr>	 			
													<td class="listClasses">
														<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseID" idName="executionCourse" property="executionCourseID" value="idInternal"/>
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
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message  bundle="CMS_RESOURCES" key="cms.executionCourseWebsiteManagement.choose.button"/>
		</html:submit>
	</html:form>
</logic:present>