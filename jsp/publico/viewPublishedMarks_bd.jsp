<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>

<logic:present name="siteView">
<bean:define id="marksListComponent" name="siteView" property="component" type="DataBeans.InfoSiteMarks"/>
<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>

<bean:define id="executionCourseId" name="commonComponent" property="executionCourse.idInternal"/>
<bean:define id="examId" name="marksListComponent" property="infoExam.idInternal" />
	
<span class="error"><html:errors/></span>

<table width="90%" align="center">
	<tr>
		<td colspan="3" align="center">
			 <h2><bean:write name="commonComponent" property="executionCourse.nome" /></h2>
			
			<logic:present name="marksListComponent" property="infoExam">  		
				<h2>
				&nbsp;-&nbsp;				
				<bean:write name="marksListComponent" property="infoExam.season"/>&nbsp;
				<bean:write name="marksListComponent" property="infoExam.date"/> - <bean:write name="marksListComponent" property="infoExam.beginningHour"/>
				&nbsp;-&nbsp;				
				</h2>
				<br />
		   </logic:present>
			
			<h2><bean:message key="label.publishedMarks"/></h2>
			<br />
		</td>	   
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
	   	<logic:iterate id="markElem" name="marksListComponent" property="marksList"> 
	   		<bean:define id="studentNumber" name="markElem" property="infoFrequenta.aluno.number" />
			<tr>
				<td class="listClasses">
					<bean:write name="markElem" property="infoFrequenta.aluno.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="markElem" property="infoFrequenta.aluno.infoPerson.nome"/>
				</td>											
				<td class="listClasses">
					<logic:empty name="markElem" property="publishedMark" >
						&nbsp;
					</logic:empty>
					<logic:notEmpty name="markElem" property="publishedMark" >
						<bean:write name="markElem" property="publishedMark"/>
					</logic:notEmpty>

				</td>
			</tr>
	   	</logic:iterate>
	</logic:present>
</table>    
</logic:present>   