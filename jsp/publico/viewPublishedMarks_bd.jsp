<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="Util.EvaluationType" %>
<logic:present name="siteView">
<bean:message key="classification.nonOfficial.information" />
<br />
<bean:define id="marksListComponent" name="siteView" property="component" type="DataBeans.InfoSiteMarks"/>
<bean:define id="infoEvaluation" name="marksListComponent" property="infoEvaluation" type="DataBeans.InfoEvaluation"/>
<span class="error"><html:errors/></span>
<table width="90%" align="center">
	<tr>
		<td colspan="3" align="center">
			<logic:equal name="infoEvaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">  		
				<h2>
					<bean:message key="label.exam"/>:				
					<bean:write name="marksListComponent" property="infoEvaluation.season"/>&nbsp;
					<bean:write name="marksListComponent" property="infoEvaluation.date"/> - 
					<bean:write name="marksListComponent" property="infoEvaluation.beginningHour"/>
				</h2>
				<br />
			</logic:equal>
			<logic:equal name="infoEvaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">  		
				<h2><bean:message key="label.publishedMarks"/>&nbsp;<%= EvaluationType.EXAM_STRING %></h2><br />
			</logic:equal>
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