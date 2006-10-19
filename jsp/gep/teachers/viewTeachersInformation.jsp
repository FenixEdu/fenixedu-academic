<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="org.apache.commons.collections.CollectionUtils" %>
<%@ page import="org.apache.commons.collections.Predicate" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.teacher.InfoOldPublication" %>
<logic:present name="infoSiteTeachersInformation">	
	<h2>
		<bean:message key="title.gep.teachersInformation"
					  bundle="GEP_RESOURCES"/>
  			(<dt:format pattern="dd/MM/yyyy">
  				<dt:currentTime/>
  			</dt:format>)
  	</h2>
	<logic:present name="infoExecutionDegree">
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
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
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
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionDegreeId=all" + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=false&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:notPresent>
	</logic:notPresent>	
	<table width="90%" border="0" cellspacing="1" style="margin-top:10px">
		<tr> 
			<th class="listClasses-header"><bean:message key="label.gep.teacher" bundle="GEP_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.gep.teacher.number" bundle="GEP_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.gep.teacher.category" bundle="GEP_RESOURCES"/> </th> 
		    <th class="listClasses-header"><bean:message key="label.gep.teachersInformation.associatedLecturingCourses" bundle="GEP_RESOURCES"/></th> 
		    <th class="listClasses-header"><bean:message key="label.gep.teachersInformation.associatedLecturingCourses.degrees" bundle="GEP_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.gep.teachersInformation.associatedLecturingCourses.executionPeriod" bundle="GEP_RESOURCES"></bean:message></th>
		    <th class="listClasses-header"><bean:message key="title.gep.teachersInformationSituation" bundle="GEP_RESOURCES"/></th> 
		    <th class="listClasses-header"><bean:message key="label.gep.teachersInformation.lastModificationDate" bundle="GEP_RESOURCES"/></th> 
	    </tr>
	    <% HashMap statistics = new HashMap(); %>
   		<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation" type="net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation">
   			<% int numberOfFields = 0; 
   			   int requiredFields = 0;%>
			<logic:present name="infoSiteTeacherInformation" property="infoQualifications">
				<%	if (infoSiteTeacherInformation.getInfoQualifications().size() > 0){
						requiredFields++; 
				   	}
				%>
			</logic:present>
			<logic:present name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<% 	if (infoSiteTeacherInformation.getInfoTeachingCareers().size() > 0){
						numberOfFields++; 
					 	requiredFields++; 
					}
				 %>
			</logic:present>
			<logic:present name="infoSiteTeacherInformation" property="infoOldCientificPublications">
				<% 	if (infoSiteTeacherInformation.getInfoOldCientificPublications().size() > 0){
						InfoOldPublication infoOldPublication = (InfoOldPublication) CollectionUtils.find(infoSiteTeacherInformation.getInfoOldCientificPublications(), new Predicate(){ 
								public boolean evaluate(Object arg0){
									InfoOldPublication elem = (InfoOldPublication) arg0;
									if(StringUtils.deleteWhitespace(elem.getPublication()).length() == 0){
										return false;
									}
									return true;
						}});
						if(infoOldPublication != null){
							requiredFields++; 
						}
					}
				 %>
			</logic:present>
			<logic:present name="infoSiteTeacherInformation" property="infoOldDidacticPublications">
				<% 	if (infoSiteTeacherInformation.getInfoOldDidacticPublications().size() > 0){
						InfoOldPublication infoOldPublication = (InfoOldPublication) CollectionUtils.find(infoSiteTeacherInformation.getInfoOldDidacticPublications(), new Predicate(){ 
								public boolean evaluate(Object arg0){
									InfoOldPublication elem = (InfoOldPublication) arg0;
									if(StringUtils.deleteWhitespace(elem.getPublication()).length() == 0){
										return false;
									}
									return true;
						}});
						if(infoOldPublication != null){
							requiredFields++; 
						}
					} 
				 %>
			</logic:present>
			<logic:present name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber">
				<logic:notEmpty name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.national">
					<%	requiredFields++; %>				
				</logic:notEmpty>
			</logic:present>
			
			<% if (!statistics.containsKey(new Integer(requiredFields)))
					statistics.put(new Integer(requiredFields), new Integer(1));
				else
				{
					int value = ((Integer) statistics.get(new Integer(requiredFields))).intValue();
					value++;
					statistics.put(new Integer(requiredFields), new Integer(value));
				}
			%>
			
			<bean:size id="numberCourses" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses"/> 
				<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses" length="1"> 
					<tr>
						<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp;
							<html:link page="<%="/readTeacherInformation.do?executionYear=" + pageContext.findAttribute("executionYear")%>" 
									   paramId="username" 
									   paramName="infoSiteTeacherInformation" 
					    				paramProperty="infoTeacher.infoPerson.username">
					    		<bean:write name="infoSiteTeacherInformation" 
					    					property="infoTeacher.infoPerson.nome"/> 
					    	</html:link> 
					    </td>
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
							<html:link page="<%="/readTeacherInformation.do?executionYear=" + pageContext.findAttribute("executionYear")%>" 
					    			   paramId="username" 
					    			   paramName="infoSiteTeacherInformation" 
					    			   paramProperty="infoTeacher.infoPerson.username"> 
					    		<bean:write name="infoSiteTeacherInformation"   
					    					property="infoTeacher.teacherNumber"/> 
					    	</html:link> 
					    </td>
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
					    	<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeacher.infoCategory">
					    	<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName"/> 
							</logic:notEmpty>
					    </td>
					    <td  class="listClasses" >
					    	<bean:write name="infoExecutionCourse" property="nome" />
					    	<% if (((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses() != null &&
					    		((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
					    	 	){%>
					    	(<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>) <% }  %>					    	
					    </td>
					    <td  class="listClasses" >
							<logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">					    	 
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
					    	</logic:iterate>
					    </td> 
			         	<td class="listClasses" >
			        		<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
						</td>
					    <td  class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">
							<bean:define id="fieldsRequired"><%= requiredFields %></bean:define>
					    	<logic:lessThan name="fieldsRequired" value="5">
					    		<font color="#FF0000"><%= requiredFields %>/5</font>
					    	</logic:lessThan>
					    	<logic:greaterEqual name="fieldsRequired" value="5">
					    		<font color="#008000"><%= requiredFields %>/5</font>
					    	</logic:greaterEqual>
					    </td> 
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
					    	<logic:present  name="infoSiteTeacherInformation" property="lastModificationDate">
					    	 <dt:format pattern="dd/MM/yyyy HH:mm"> 
					    	 	<bean:write name="infoSiteTeacherInformation" property="lastModificationDate.time"/>
					    	  </dt:format>
					    	</logic:present> 
					    	<logic:notPresent name="infoSiteTeacherInformation" property="lastModificationDate">
					    	 <bean:message key="label.gep.teachersInformation.notModified" bundle="GEP_RESOURCES"/> 
					    	</logic:notPresent> 
					    </td>
				    </tr>
    			</logic:iterate>
				<logic:iterate  id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses" offset="1">
					<tr>
				    	<td  class="listClasses">
				    	
				    		<bean:write name="infoExecutionCourse" property="nome" />
				    		<% if (((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses() != null &&
						    	 ((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
						    	 ){%>
					    	 (<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>) <% }  %> 				    	 	
				    	 </td> 
				    	<td  class="listClasses" >
					    	<logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
					    	</logic:iterate>
				     	</td> 
						<td class="listClasses" >
			        		<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
						</td>
				     </tr>
			     </logic:iterate>
		</logic:iterate>		
	</table>
	 
	<br/>
	<h2><bean:message key="label.gep.statistics" bundle="GEP_RESOURCES"/>:</h2>
	<bean:size id="length" name="infoSiteTeachersInformation"/>
	<bean:message key="label.gep.numberOfTeachers" bundle="GEP_RESOURCES"/>:
	<bean:write name="length"/>
	<% int filled = 0; %>
	<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
		<logic:present name="infoSiteTeacherInformation" property="lastModificationDate">
			<% filled++; %>
		</logic:present>
	</logic:iterate>
	<br />
	<br />
	<table width="50%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<th class="listClasses-header"><bean:message key="label.gep.situation" bundle="GEP_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.gep.numberOfFieldsWithInfo" bundle="GEP_RESOURCES"/></th>
			<th class="listClasses-header"><bean:message key="label.gep.stats" bundle="GEP_RESOURCES"/>&nbsp;(%)</th>
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
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
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
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=" + pageContext.findAttribute("basic") + "&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="<%="/listTeachersAcreditation.do?method=doSearch&amp;basic=false&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
					<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:notPresent>
	</logic:notPresent>	
</logic:present>