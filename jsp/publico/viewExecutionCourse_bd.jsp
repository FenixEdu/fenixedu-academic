<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
	
		
        <table id="anuncios" cellspacing="0">
          <tr>
            <td  class="ultAnuncioAviso"><img "<%= request.getContextPath() %>/images/icon_warning.gif"  width="7" height="9" /> 
            <bean:message key="message.lastWeekAnnouncements"/> 
             </td>
             <td  class="ultAnuncioAviso">
             </td>
             <td  class="ultAnuncioAviso">
             </td>
             <td  class="ultAnuncioAviso">
             </td>
           </tr>
           <logic:present name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" scope="session">
           <tr>
           	<td >
           		<img "<%= request.getContextPath() %>/images/icon_warning.gif"  width="7" height="9" /><bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="title"/>
           	</td>
           	<td>	
           		<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="information"/>
           </td>
           </tr>
           <tr>
           	<td></td>
           	<td></td>
           <td>	
           		<bean:message key="message.createdOn"/>
           		<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="creationDate"/>
           </td>
             </tr>
           <tr>
           	<td></td>
           	<td></td>      
           <td>	
           		<bean:message key="message.modifiedOn"/>
           		<bean:write name="<%= SessionConstants.LAST_ANNOUNCEMENT %>" property="lastModifiedDate"/>
           </td>
          
           </tr>
           </logic:present>
        </table>
    
			
	<bean:message key="property.executionCourse.associatedCurricularCourses"/>
	<logic:present name="publico.infoCurricularCourses" scope="session">
			<table align="center" border='1' cellpadding='10'>
					<tr align="center">
						<td>
							<bean:message key="property.curricularCourse.name"/>
						</td>
						<td>
							<bean:message key="property.degree.initials"/>
						</td>
						<td>
							<bean:message key="property.curricularCourse.curricularYear"/>
						</td>
						<td>
							<bean:message key="property.curricularCourse.semester"/>
						</td>
					</tr>			
				<logic:iterate id="curricularCourse" name="publico.infoCurricularCourses" scope="session">
					<tr align="center">
						<td>
							<bean:write name="curricularCourse" property="name"/>
						</td>
						<td>
							<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
						</td>
						<td>
							<logic:iterate id="infoCurricularSemester" name="curricularCourse" property="associatedInfoCurricularSemesters">
								<bean:write name="infoCurricularSemester" property="infoCurricularYear.year"/>&nbsp;
							</logic:iterate>
						</td>
						<td>
							<logic:iterate id="infoCurricularSemester" name="curricularCourse" property="associatedInfoCurricularSemesters">
								<bean:write name="infoCurricularSemester" property="semester"/>&nbsp;
							</logic:iterate>
						</td>
					</tr>
				</logic:iterate>
			</table>			
	</logic:present>
	
	<logic:notPresent name="publico.infoCurricularCourses" scope="session">
		<bean:message key="message.public.notfound.curricularCourses"/>
	</logic:notPresent>

	</br>
	</br>

	<bean:message key="property.executionCourse.curricularHours"/>
	<blockquote>
		<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoreticalHours" value="0">
			<bean:message key="property.executionCourse.theoreticalHours"/>
			<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoreticalHours"/>
			<bean:message key="property.hours"/>
			</br>
		</logic:notEqual>

		<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="praticalHours" value="0">
			<bean:message key="property.executionCourse.practicalHours"/>
			<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="praticalHours"/>
			<bean:message key="property.hours"/>
			</br>
		</logic:notEqual>

		<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoPratHours" value="0">
			<bean:message key="property.executionCourse.theoreticalPracticalHours"/>
			<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoPratHours"/>
			<bean:message key="property.hours"/>
			</br>
		</logic:notEqual>
		
		<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="labHours" value="0">
			<bean:message key="property.executionCourse.labHours"/>
			<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="labHours"/>
			<bean:message key="property.hours"/>
		</logic:notEqual>
	</blockquote>

	
	
</logic:present>	
		
