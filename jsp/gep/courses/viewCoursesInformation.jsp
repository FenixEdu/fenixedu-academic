<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.gep.SearchCoursesInformationAction.InfoCourse" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.gep.SearchCoursesInformationAction.InfoSimpleTeacher" %>

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
			<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank"
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
			<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
				<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
			</html:link>
		</div>
	</logic:notPresent>
</logic:notPresent>
<table width="90%" border="0" cellspacing="1" style="margin-top:10px">
	<tr>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseYear" bundle="GEP_RESOURCES"/><br />
			<bean:message key="label.gep.courseSemester" bundle="GEP_RESOURCES"/><br />
			<bean:message key="label.gep.branch" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseName" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.code" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.executionPeriod" bundle="GEP_RESOURCES"/>
		</th>
		<logic:notPresent name="infoExecutionDegree">
			<th class="listClasses-header">
				<bean:message key="label.gep.degree" bundle="GEP_RESOURCES"/>
			</th>
		</logic:notPresent>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseInformation.basic" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseInformation.professorships" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseInformation.department" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="title.gep.teachersInformationSituation" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.courseInformation.lastModificationDate" bundle="GEP_RESOURCES"/>
		</th>
		<th class="listClasses-header">
			<bean:message key="label.gep.teachingReport" bundle="GEP_RESOURCES"/>	
		</th>
	</tr>
	<logic:present name="infoCourses">
		<logic:iterate id="infoCourse" name="infoCourses" type="net.sourceforge.fenixedu.presentationTier.Action.gep.SearchCoursesInformationAction.InfoCourse">
		<logic:present name="infoCourse" property="teachers">
			<bean:size id="numberTeachers" name="infoCourse" property="teachers"/>
		</logic:present>
		<logic:notPresent name="infoCourse" property="teachers">
			<bean:define id="numberTeachers" value="1"/>
		</logic:notPresent>
		<tr>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<logic:present name="infoCourse" property="yearSemesterBranch">				
					<table>
						<logic:iterate id="yearSemesterBranchElement" name="infoCourse" property="yearSemesterBranch">
							<tr>
								<td><bean:write name="yearSemesterBranchElement"/></td>
							</tr>
						</logic:iterate>
					</table>
				</logic:present>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<logic:present name="infoExecutionDegree">
					<bean:define id="idInternal" name="infoExecutionDegree" property="idInternal"/>
					<html:link page='<%= "/readCourseInformation.do?executionDegreeId=" + idInternal %>'
						paramId="executionCourseId" 
						paramName="infoCourse"
						paramProperty="executionCourseID">
						<bean:write name="infoCourse" property="curricularCourseNameAndCode"/>
					</html:link>
				</logic:present>
				<logic:notPresent name="infoExecutionDegree">
					<html:link page="/readCourseInformation.do"
						paramId="executionCourseId" 
						paramName="infoCourse"
						paramProperty="executionCourseID">
						<bean:write name="infoCourse" property="curricularCourseNameAndCode"/>
					</html:link>
				</logic:notPresent>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<bean:write name="infoCourse" property="executionCourseCode"/>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<bean:write name="infoCourse" property="executionPeriod"/>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<logic:equal name="infoCourse" property="basic" value="true">
					<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
				</logic:equal>
				<logic:notEqual name="infoCourse" property="basic" value="true">
					<bean:message key="label.no" bundle="GEP_RESOURCES"/>
				</logic:notEqual>
			</td>
			<logic:notPresent name="infoExecutionDegree">
				<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
					<bean:write name="infoCourse" property="degreeCurricularPlanName"/>
				</td>
			</logic:notPresent>
			<logic:present name="infoCourse" property="teachers">
				<logic:iterate id="teacher" name="infoCourse" property="teachers"  length="1">
					<td class="listClasses">
						<bean:write name="teacher" property="name"/>&nbsp;
							<logic:equal name="teacher" property="responsible" value="true">
								(<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>)
							</logic:equal>
					</td>
					<td class="listClasses">
						<bean:write name="teacher" property="department"/>&nbsp;
					</td>
				</logic:iterate>
			</logic:present>				
			<logic:notPresent name="infoCourse" property="teachers">
				<td class="listClasses">&nbsp;</td>
				<td class="listClasses">&nbsp;</td>
			</logic:notPresent>				
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<logic:lessThan name="infoCourse" property="numberFieldsFilled" value="5">
					<font color="#FF0000"><bean:write name="infoCourse" property="numberFieldsFilled"/>/5</font>
				</logic:lessThan>
				<logic:greaterEqual name="infoCourse" property="numberFieldsFilled" value="5">
					<font color="#008000"><bean:write name="infoCourse" property="numberFieldsFilled"/>/5</font>
				</logic:greaterEqual>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<logic:present name="infoCourse" property="lastModificationDate"> 
					<bean:define id="dateFormated">
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="infoCourse" property="lastModificationDate"/>
						</dt:format>
					</bean:define>
					<logic:notEqual name="dateFormated" value="03/12/2003">
						<logic:notEqual name="dateFormated" value="01/01/2003">
							<logic:notEqual name="dateFormated" value="01/01/1970">
								<bean:write name="dateFormated"/>
							</logic:notEqual>
						</logic:notEqual>
					</logic:notEqual>
					<logic:equal name="dateFormated" value="03/12/2003">
						<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
					</logic:equal>
					<logic:equal name="dateFormated" value="01/01/2003">
						<bean:message key="label.gep.courseInformation.previousYear" bundle="GEP_RESOURCES"/>
					</logic:equal>
					<logic:equal name="dateFormated" value="01/01/1970">
						<bean:message key="label.gep.courseInformation.notModified" bundle="GEP_RESOURCES"/>
					</logic:equal>								
				</logic:present>
				<logic:notPresent name="infoCourse" property="lastModificationDate"> 
					<bean:message key="label.gep.courseInformation.notModified" bundle="GEP_RESOURCES"/>
				</logic:notPresent>
			</td>
			<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberTeachers") %>">&nbsp;
				<html:link page="/viewTeachingReport.do?method=read" paramId="executionCourseId" paramName="infoCourse" paramProperty="executionCourseID">
					<bean:message key ="label.courseInformation.view" bundle="GEP_RESOURCES"/>
				</html:link>
			</td>
		</tr>
		<logic:present name="infoCourse" property="teachers">
			<logic:iterate id="teacher" name="infoCourse" property="teachers"  offset="1">
			<tr>
				<td class="listClasses">
					<bean:write name="teacher" property="name"/>&nbsp;
					<logic:equal name="teacher" property="responsible" value="true">
						(<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>)
					</logic:equal>
				</td>
				<td class="listClasses">
					<bean:write name="teacher" property="department"/>&nbsp;
				</td>
			</tr>
			</logic:iterate>
		</logic:present>
		</logic:iterate>
	</logic:present>
</table>
<br/>
<h2><bean:message key="label.gep.statistics" bundle="GEP_RESOURCES"/>:</h2>
<bean:message key="label.gep.numberOfCourses" bundle="GEP_RESOURCES"/>:
<logic:present name="infoCourses">
	<bean:size id="length" name="infoCourses"/>
	<bean:write name="length"/>
<br/>
<br/>
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
			HashMap statistics = (HashMap) request.getAttribute("statistics");
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
</logic:present>
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
			<html:link page="/listCoursesAcreditation.do?method=doSearch&amp;" target="_blank"
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
			<html:link page="<%="/listCoursesAcreditation.do?method=doSearch&amp;executionDegreeId=all&amp;executionYear=" + pageContext.findAttribute("executionYear")%>" target="_blank">
				<bean:message key="label.list.acred" bundle="GEP_RESOURCES" />
			</html:link>
		</div>
	</logic:notPresent>
</logic:notPresent>
