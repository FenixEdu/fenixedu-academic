<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<html:form action="/writeMarks" >  
	<logic:present name="siteView">
	<bean:define id="marksListComponent" name="siteView" property="component" type="DataBeans.InfoSiteMarks"/>
	<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>

	<bean:define id="executionCourseId" name="commonComponent" property="executionCourse.idInternal"/>
	<bean:define id="examId" name="marksListComponent" property="infoExam.idInternal" />
	
	<html:hidden property="objectCode" value="<%= executionCourseId.toString() %>" />	
	<html:hidden property="examCode" value="<%= examId.toString() %>" />
    
    <span class="error"><html:errors/></span>

    <table>        
		<tr>
			
			<td colspan="3">
			 <h2><bean:write name="commonComponent" property="executionCourse.nome" /></h2>
			
			<logic:present name="marksListComponent" property="infoExam">  		
				<h2>
				&nbsp;-&nbsp;				
				<bean:write name="marksListComponent" property="infoExam.season"/>&nbsp;
				<bean:write name="marksListComponent" property="infoExam.date"/> <i>às</i> <bean:write name="marksListComponent" property="infoExam.beginningHour"/><br />
				</h2>
		   </logic:present>
		   
		</tr> 
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.name" />
		   </td>
			<logic:present name="marksListComponent" property="marksList">  						
				<td class="listClasses-header">
					<bean:message key="label.mark" />
				</td>
			</logic:present>
		</tr>    		
		
		<logic:present name="marksListComponent" property="marksList">  								
	    	<logic:iterate id="markElem" name="marksListComponent" property="marksList" indexId="markId" > 
	    		<bean:define id="studentNumber" name="markElem" property="infoFrequenta.aluno.number" />
				<tr>
					<td class="listClasses">
						<bean:write name="markElem" property="infoFrequenta.aluno.number"/>&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="markElem" property="infoFrequenta.aluno.infoPerson.nome"/>
					</td>											
					<td class="listClasses">
						<html:text name="markElem" property="mark" size="4" indexed="true" />
						<html:hidden name="markElem" property="studentsNumbers" value="<%= studentNumber.toString() %>" indexed="true" />
					</td>
				</tr>
	    	</logic:iterate>
		</logic:present>
		
    </table>    
  	</logic:present>   
 	<br />
 	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
  	</html:submit>
</html:form> 