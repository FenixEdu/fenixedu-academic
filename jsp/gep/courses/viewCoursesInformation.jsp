<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="java.util.HashMap, java.util.Iterator, DataBeans.InfoCurriculum" %>
<logic:present name="infoSiteCoursesInformation">
	<h2>
		<bean:message key="link.gep.executionCoursesInformation"
					  bundle="GEP_RESOURCES"/>
  			(<dt:format pattern="dd/MM/yyyy">
  				<dt:currentTime/>
  			</dt:format>)
  	</h2>
	<logic:present name="infoExecutionDegree">
		<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
		<table width="90%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" class="infoselected">
					<p>
						<strong><bean:message key="title.gep.teachersInformationSelectedDegree"
											  bundle="GEP_RESOURCES"/>:</strong> 
						<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						<br />
						<strong><bean:message key="title.gep.executionYear"
											  bundle="GEP_RESOURCES"/>:</strong>
						<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
					</p>			
				</td>
			</tr>
		</table>
	</logic:present>
	<logic:notPresent name="infoExecutionDegree">
		<table width="90%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center" class="infoselected">
					<p>
						<strong><bean:message key="title.gep.executionYear"
											  bundle="GEP_RESOURCES"/>:</strong>
						<bean:write name="executionYear"/>
					</p>			
				</td>
			</tr>
		</table>
	</logic:notPresent>	
	<br />
	<logic:present name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesInformation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ects" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesInformationEnglish.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ectsEnglish" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesInformation.do?method=doSearch&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ects" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesInformationEnglish.do?method=doSearch&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ectsEnglish" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=false&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:notPresent>
	</logic:present> 
	<logic:notPresent name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=false&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:notPresent>
	</logic:notPresent>
	<table width="90%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseYear" bundle="GEP_RESOURCES"/><br />
				<bean:message key="label.gep.courseSemester" bundle="GEP_RESOURCES"/><br />
				<bean:message key="label.gep.branch" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseName" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.code" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.executionPeriod" bundle="GEP_RESOURCES"/>
			</td>
	        <logic:notPresent name="infoExecutionDegree">
				<td class="listClasses-header">
					<bean:message key="label.gep.degree" bundle="GEP_RESOURCES"/>
				</td>
			</logic:notPresent>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.basic" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.professorships" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="title.gep.teachersInformationSituation" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.lastModificationDate" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.teachingReport" bundle="GEP_RESOURCES"/>	
			</td>
		</tr>
		<% 
			HashMap statistics = new HashMap();
		%>
		<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation" type="DataBeans.gesdis.InfoSiteCourseInformation">		
			<% 
                Integer numberOfFields = infoSiteCourseInformation.getNumberOfFieldsFilled();
				if (!statistics.containsKey(numberOfFields))
					statistics.put(numberOfFields, new Integer(1));
				else
				{
					int value = ((Integer) statistics.get(numberOfFields)).intValue();
					value++;
					statistics.put(numberOfFields, new Integer(value));
				}
			%>
			<logic:iterate id="infoCurricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
					<logic:equal name="infoCurricularCourse" 
	  						 property="infoDegreeCurricularPlan.idInternal" 
	  						 value="<%= degreeCurricularPlanId.toString() %>">
						<tr>
							<td class="listClasses">
								<table>
									<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
										<tr>
			   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
			   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/></td>
		   				         	 	 	<td><bean:write name="infoCurricularCourseScope" property="infoBranch.acronym"/>&nbsp;</td>
	   				         	 	 	</tr>
		   				         	 </logic:iterate>
	   				         	 </table>
   				         	</td>
   				         	<td class="listClasses" >&nbsp;
								<bean:define id="idInternal" name="infoExecutionDegree" property="idInternal"/>
								<html:link page='<%= "/readCourseInformation.do?executionDegreeId=" + idInternal %>'
									       paramId="executionCourseId" 
									       paramName="infoSiteCourseInformation"
									       paramProperty="infoExecutionCourse.idInternal">
			   					        <bean:write name="infoCurricularCourse" property="name"/>-
					       		   		<bean:write name="infoCurricularCourse" property="code"/>
						      	</html:link>
			       			</td>
			       			<td class="listClasses">&nbsp;
		   				   		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
				         	</td>
				         	<td class="listClasses" >&nbsp;
   				        		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/>
			         		</td>
			         		<td class="listClasses" >
					       		<logic:equal name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
				       			</logic:equal>
	   							<logic:notEqual name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.no" bundle="GEP_RESOURCES"/>
				       			</logic:notEqual>
				       		</td>
							<td class="listClasses" >&nbsp;
								<ul>
								<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
									<li><bean:write name="infoTeacher" property="infoPerson.nome" />
									 <% if (
								    	 ((DataBeans.gesdis.InfoSiteCourseInformation)infoSiteCourseInformation).getInfoResponsibleTeachers().contains(infoTeacher)
								    	 ){%>
								    	 (<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>) <% }  %> 
									</li>
								</logic:iterate>
								</ul>
							</td>
							<td class="listClasses" >&nbsp;
						    	<logic:lessThan name="infoSiteCourseInformation" property="numberOfFieldsFilled" value="5">
						    		<font color="#FF0000"><bean:write name="infoSiteCourseInformation" property="numberOfFieldsFilled"/>/5</font>
						    	</logic:lessThan>
						    	<logic:greaterEqual name="infoSiteCourseInformation" property="numberOfFieldsFilled" value="5">
						    		<font color="#008000"><bean:write name="infoSiteCourseInformation" property="numberOfFieldsFilled"/>/5</font>
						    	</logic:greaterEqual>							
							</td>
							<td class="listClasses" >&nbsp;
								<logic:present name="infoSiteCourseInformation" property="lastModificationDate"> 
									<bean:define id="formatDate">
										<dt:format pattern="dd/MM/yyyy">
											<bean:write name="infoSiteCourseInformation" property="lastModificationDate.time"/>
										</dt:format>
									</bean:define>
									<logic:notEqual name="formatDate" value="03/12/2003">
										<logic:notEqual name="formatDate" value="01/01/2003">
											<dt:format pattern="dd/MM/yyyy HH:mm">
												<bean:write name="infoSiteCourseInformation" property="lastModificationDate.time"/>
											</dt:format>
										</logic:notEqual>
									</logic:notEqual>
									<logic:equal name="formatDate" value="03/12/2003">
										<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
									</logic:equal>
									<logic:equal name="formatDate" value="01/01/2003">
										<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
									</logic:equal>								
								</logic:present>
								<logic:notPresent name="infoSiteCourseInformation" property="lastModificationDate"> 
									<bean:message key="label.gep.courseInformation.notModified" 
												  bundle="GEP_RESOURCES"/>
								</logic:notPresent>
							</td>
							<td class="listClasses">
								<html:link page="/viewTeachingReport.do?method=read" paramId="executionCourseId" paramName="infoSiteCourseInformation" paramProperty="infoExecutionCourse.idInternal">
									<bean:message key ="label.courseInformation.view" bundle="GEP_RESOURCES"/>
								</html:link>
							</td>
						</tr>			
		         	</logic:equal>
	         	</logic:present>
	         	<logic:notPresent name="infoExecutionDegree">
	         		<tr>
						<td class="listClasses">
							<table>
								<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
									<tr>
		   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
		   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/></td>
	   				         	 	 	<td><bean:write name="infoCurricularCourseScope" property="infoBranch.acronym"/>&nbsp;</td>
   				         	 	 	</tr>
	   				         	 </logic:iterate>
   				         	 </table>
   				         </td>
   				         <td class="listClasses" >&nbsp;
							<html:link page="/readCourseInformation.do"
								       paramId="executionCourseId" 
								       paramName="infoSiteCourseInformation"
								       paramProperty="infoExecutionCourse.idInternal">
		   					        <bean:write name="infoCurricularCourse" property="name"/>-
				       		   		<bean:write name="infoCurricularCourse" property="code"/>
					      	</html:link>
		       			</td>
		       			<td class="listClasses">&nbsp;
	   				   		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
			         	</td>
			         	<td class="listClasses" >&nbsp;
   				        	<bean:write name="infoSiteCourseInformation" 
   				        				property="infoExecutionCourse.infoExecutionPeriod.name"/>
		         		</td>
			         	<td class="listClasses" >&nbsp;
   				        	<bean:write name="infoCurricularCourse" 
   				        				property="infoDegreeCurricularPlan.name"/>
		         		</td>
		         		<td class="listClasses" >
				       		<logic:equal name="infoCurricularCourse" property="basic" value="true">
				       			<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
			       			</logic:equal>
   							<logic:notEqual name="infoCurricularCourse" property="basic" value="true">
				       			<bean:message key="label.no" bundle="GEP_RESOURCES"/>
			       			</logic:notEqual>
			       		</td>
						<td class="listClasses" style="text-align:left">&nbsp;
							<ul>
							<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
								<li><bean:write name="infoTeacher" property="infoPerson.nome" />
								 <% if (
							    	 ((DataBeans.gesdis.InfoSiteCourseInformation)infoSiteCourseInformation).getInfoResponsibleTeachers().contains(infoTeacher)
							    	 ){%>
							    	 (<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>) <% }  %> 
								</li>
							</logic:iterate>
							</ul>
						</td>
						<td class="listClasses" >&nbsp;
					    	<logic:lessThan name="infoSiteCourseInformation" property="numberOfFieldsFilled" value="5">
					    		<font color="#FF0000"><bean:write name="infoSiteCourseInformation" property="numberOfFieldsFilled"/>/5</font>
					    	</logic:lessThan>
					    	<logic:greaterEqual name="infoSiteCourseInformation" property="numberOfFieldsFilled" value="5">
					    		<font color="#008000"><bean:write name="infoSiteCourseInformation" property="numberOfFieldsFilled"/>/5</font>
					    	</logic:greaterEqual>							
						</td>
						<td class="listClasses" >&nbsp;
							<logic:present name="infoSiteCourseInformation" property="lastModificationDate"> 
								<bean:define id="formatDate">
									<dt:format pattern="dd/MM/yyyy">
										<bean:write name="infoSiteCourseInformation" property="lastModificationDate.time"/>
									</dt:format>
								</bean:define>
								<logic:notEqual name="formatDate" value="03/12/2003">
									<logic:notEqual name="formatDate" value="01/01/2003">
										<dt:format pattern="dd/MM/yyyy HH:mm">
											<bean:write name="infoSiteCourseInformation" property="lastModificationDate.time"/>
										</dt:format>
									</logic:notEqual>
								</logic:notEqual>
								<logic:equal name="formatDate" value="03/12/2003">
									<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
								</logic:equal>
								<logic:equal name="formatDate" value="01/01/2003">
									<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
								</logic:equal>
							
							</logic:present>
							<logic:notPresent name="infoSiteCourseInformation" property="lastModificationDate"> 
								<bean:message key="label.gep.courseInformation.notModified" 
											  bundle="GEP_RESOURCES"/>
							</logic:notPresent>
						</td>
						<td class="listClasses">
							<html:link page="/viewTeachingReport.do?method=read" paramId="executionCourseId" paramName="infoSiteCourseInformation" paramProperty="infoExecutionCourse.idInternal">
								<bean:message key ="label.courseInformation.view" bundle="GEP_RESOURCES"/>
							</html:link>
						</td>
					</tr>			
	         	</logic:notPresent>
         	</logic:iterate>         	
 	  	</logic:iterate>
	</table>
	<br/>
	<h2><bean:message key="label.gep.statistics" bundle="GEP_RESOURCES"/>:</h2>
	<bean:size id="length" name="infoSiteCoursesInformation"/>
	<bean:message key="label.gep.numberOfCourses" bundle="GEP_RESOURCES"/>:
	<bean:write name="length"/>
	<br />
	<br />
	<table width="50%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header"><bean:message key="label.gep.situation" bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header"><bean:message key="label.gep.numberOfFieldsWithInfo" bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header"><bean:message key="label.gep.stats" bundle="GEP_RESOURCES"/>&nbsp;(%)</td>
			
		</tr>
		<tr>
			<td class="listClasses"><font color="#008000">5/5</font></td>
			<td class="listClasses">
			<%
				Integer value5 = new Integer(0);
				if (statistics.containsKey(new Integer(5)))
					value5 = (Integer) statistics.get(new Integer(5));
			%>
			<%= value5 %>
			</td>
			<td class="listClasses"><%= new Float((value5.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
		<tr>
			<td class="listClasses"><font color="#FF0000">4/5</font></td>
			<td class="listClasses">			
			<%
				Integer value4 = new Integer(0);
				if (statistics.containsKey(new Integer(4)))
					value4 = (Integer) statistics.get(new Integer(4));
			%>
			<%= value4 %>
			</td>
			<td class="listClasses"><%= new Float((value4.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
		<tr>
			<td class="listClasses"><font color="#FF0000">3/5</font></td>
			<td class="listClasses">
			<%
				Integer value3 = new Integer(0);
				if (statistics.containsKey(new Integer(3)))
					value3 = (Integer) statistics.get(new Integer(3));
			%>
			<%= value3 %>
			</td>
			<td class="listClasses"><%= new Float((value3.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
		<tr>
			<td class="listClasses"><font color="#FF0000">2/5</font></td>
			<td class="listClasses">
			<%
				Integer value2 = new Integer(0);
				if (statistics.containsKey(new Integer(2)))
					value2 = (Integer) statistics.get(new Integer(2));
			%>
			<%= value2 %>
			</td>
			<td class="listClasses"><%= new Float((value2.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
		<tr>
			<td class="listClasses"><font color="#FF0000">1/5</font></td>
			<td class="listClasses">
			<%
				Integer value1 = new Integer(0);
				if (statistics.containsKey(new Integer(1)))
					value1 = (Integer) statistics.get(new Integer(1));
			%>
			<%= value1 %>
			</td>
			<td class="listClasses"><%= new Float((value1.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
		<tr>
			<td class="listClasses"><font color="#FF0000">0/5</font></td>
			<td class="listClasses">
			<%
				Integer value0 = new Integer(0);
				if (statistics.containsKey(new Integer(0)))
					value0 = (Integer) statistics.get(new Integer(0));
			%>
			<%= value0 %>
			</td>
			<td class="listClasses"><%= new Float((value0.floatValue()/length.floatValue())*100).intValue() %></td>
		</tr>
	</table>
	<br />
	<br />
	<logic:present name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesInformation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ects" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesInformationEnglish.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ectsEnglish" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="/listCoursesInformation.do?method=doSearch" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ects" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
			<div class="button">
				<html:link page="/listCoursesInformationEnglish.do?method=doSearch" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.ectsEnglish" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
			<div class="button">
				<html:link page="/listCoursesAcreditation.do?method=doSearch&amp;basic=false" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:notPresent>
	</logic:present>
	<logic:notPresent name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;basic=false&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:notPresent>
	</logic:notPresent>
</logic:present>