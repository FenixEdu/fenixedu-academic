<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="Util.Data" %>
<%@ page import="Util.EvaluationType" %> 

<span class="error"><html:errors/></span>
 
<html:form action="/writeMarks" >  
	<logic:present name="siteView">
	<bean:define id="marksListComponent" name="siteView" property="component" type="DataBeans.InfoSiteMarks"/>
	<bean:define id="commonComponent" name="siteView" property="commonComponent" type="DataBeans.InfoSiteCommon"/>

	<bean:define id="executionCourseId" name="commonComponent" property="executionCourse.idInternal"/>
	<bean:define id="evaluationId" name="marksListComponent" property="infoEvaluation.idInternal" />
	
	<html:hidden property="objectCode" value="<%= executionCourseId.toString() %>" />	
	<html:hidden property="evaluationCode" value="<%= evaluationId.toString() %>" />
		
	<html:hidden property="method" value="writeMarks" />

    <table>        
		<tr>
			<td colspan="3">
			 <h2><bean:write name="commonComponent" property="executionCourse.nome" /></h2>				
			
			<logic:present name="marksListComponent" property="infoEvaluation">  
				<bean:define id="evaluation" name="marksListComponent" property="infoEvaluation" />		
				<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.EXAM_STRING %>">
					<h2>
					&nbsp;-&nbsp;				
					<bean:write name="evaluation" property="season"/>&nbsp;
					<bean:write name="evaluation" property="date"/> <i><bean:message key="label.at" /></i> 
					<bean:write name="marksListComponent" property="infoEvaluation.beginningHour"/><br />
					</h2>
				</logic:equal>
				<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.FINAL_STRING %>">
					<h2>				
					<bean:message key="label.finalEvaluation"/><br />
					</h2>
				</logic:equal>
		   </logic:present>
		   </td>
		</tr>
		<tr>
	    	<td colspan="4" class="infoop"><bean:message key="label.marksOnline.instructions" /></td>
		</tr>
		<tr><td><br></br></td></tr>
		<tr>
			
			<td class="listClasses-header">
				<bean:message key="label.number" /> 
		   </td>
			<td class="listClasses-header">
				<bean:message key="label.name" />
		   </td>
		   <td class="listClasses-header">
				<bean:message key="label.enrolmentEvaluationType" /> 
		   </td>
			<logic:present name="marksListComponent" property="marksList">  						
				<td class="listClasses-header">
					<bean:message key="label.mark" />
				</td>
			</logic:present>
		</tr>    		
		
		<logic:present name="marksListComponent" property="marksList">  
			<bean:size id="size" name="marksListComponent" property="marksList" />	
			<html:hidden property="sizeList" value="<%= size.toString() %>" /> 
				    			    		
	    	<logic:iterate id="markElem" name="marksListComponent" property="marksList" indexId="markId" type="DataBeans.InfoMark" > 
	    	
		    	<bean:define id="studentCode" name="markElem" property="infoFrequenta.aluno.idInternal" />
		    	<bean:define id="studentNumber" name="markElem" property="infoFrequenta.aluno.number" />
		    	<bean:define id="infoFrequenta" name="markElem" property="infoFrequenta"/>
		    	<logic:notEmpty name="infoFrequenta" property="infoEnrolment">
		      		<bean:define id="infoEnrolment" name="infoFrequenta" property="infoEnrolment"/>					
			  		<bean:define id="evaluationType" name="infoEnrolment" property="evaluationType"/>
		  		</logic:notEmpty>

	    		<bean:define id="studentMark" name="markElem" property="mark" />
	    		
	    		<tr>
	    		<logic:present name="markElem" property="infoFrequenta"/>	
					<td class="listClasses">
						<bean:write name="markElem" property="infoFrequenta.aluno.number"/>&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="markElem" property="infoFrequenta.aluno.infoPerson.nome"/>
					</td>
				</logic:present>	
				<td class="listClasses">
					<logic:empty name="markElem" property="infoFrequenta.infoEnrolment" >
						<bean:message key="message.notEnroled"/>
					</logic:empty>	
					<logic:notEmpty name="markElem" property="infoFrequenta.infoEnrolment">
							<bean:write name="evaluationType"/>
					</logic:notEmpty>
				</td>
				
				<td class="listClasses">
					<html:text name="markElem" property="mark" size="4" indexed="true" />
					<html:hidden name="markElem" property="studentCode" value="<%= studentCode.toString() %>" indexed="true" />
					<html:hidden name="markElem" property="studentNumber" value="<%= studentNumber.toString() %>" indexed="true" />
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