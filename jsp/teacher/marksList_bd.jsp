<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.EvaluationType" %> 
<logic:messagesPresent>
	<span class="error"><html:errors/></span>
</logic:messagesPresent>
 
<html:form action="/writeMarks" >  
	<logic:present name="siteView">
	<bean:define id="marksListComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks"/>
	<bean:define id="commonComponent" name="siteView" property="commonComponent" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon"/>

	<bean:define id="executionCourseId" name="commonComponent" property="executionCourse.idInternal"/>
	<bean:define id="evaluationId" name="marksListComponent" property="infoEvaluation.idInternal" />
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= executionCourseId.toString() %>" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= evaluationId.toString() %>" />
		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="writeMarks" />

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
				<logic:equal name="evaluation" property="evaluationType" value="<%= EvaluationType.ONLINE_TEST_STRING %>">
					<h2>
						<bean:define id="distributedTest" name="evaluation" property="infoDistributedTest"/>
						<bean:write name="distributedTest" property="title"/>
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
			
			<th class="listClasses-header">
				<bean:message key="label.number" /> 
		   </th>
			<th class="listClasses-header">
				<bean:message key="label.name" />
		   </th>
		   <th class="listClasses-header">
				<bean:message key="label.enrolmentEvaluationType" /> 
		   </th>
			<logic:present name="marksListComponent" property="marksList">  						
				<th class="listClasses-header">
					<bean:message key="label.mark" />
				</th>
			</logic:present>
		</tr>    		
		
		<logic:present name="marksListComponent" property="infoAttends">  
			<bean:size id="size" name="marksListComponent" property="infoAttends" />	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.sizeList" property="sizeList" value="<%= size.toString() %>" /> 
				    			    		
	    	<logic:iterate id="markElem" name="marksListComponent" property="infoAttends"  indexId="markId"  type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta" > 
	    	
		    	<bean:define id="studentCode" name="markElem" property="aluno.idInternal" />
		    	<bean:define id="studentNumber" name="markElem" property="aluno.number" />

		    	<logic:notEmpty name="markElem" property="infoEnrolment">
		      		<bean:define id="infoEnrolment" name="markElem" property="infoEnrolment"/>					
					<bean:define id="evaluationType" name="markElem" property="infoEnrolment.enrolmentEvaluationType" type="net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType"/>
		  		</logic:notEmpty>
				<bean:define id="studentMark" value=""/>
				<logic:notEmpty name="marksListComponent" property='<%="marks(" + studentNumber + ")"%>'>
		    		<bean:define id="studentMark" name="marksListComponent" property='<%="marks(" + studentNumber + ")"%>' type="java.lang.String"/>
	    		</logic:notEmpty>
	    		
	    		<tr>	
					<td class="listClasses">
						<bean:write name="studentNumber" />&nbsp;
					</td>
					<td class="listClasses">
						<bean:write name="markElem" property="aluno.infoPerson.nome"/>
					</td>
					<td class="listClasses">
						<logic:empty name="markElem" property="infoEnrolment" >
							<bean:message key="message.notEnroled"/>
						</logic:empty>	
						<logic:notEmpty name="markElem" property="infoEnrolment">
							<bean:define id="aux"><bean:write name="evaluationType"/></bean:define>
							<bean:message name="aux"/>
						</logic:notEmpty>
					</td>
				
					<td class="listClasses">
						<logic:empty name="studentMark">
							<html:text alt='<%="hashMarks(" + studentNumber + ")" %>' property='<%="hashMarks(" + studentNumber + ")" %>' size="4" value=""/>
						</logic:empty>
						<logic:notEmpty name="studentMark">
							<html:text alt='<%="hashMarks(" + studentNumber + ")" %>' property='<%="hashMarks(" + studentNumber + ")" %>' value="<%=studentMark %>" size="4" />
						</logic:notEmpty>						
					</td>
				</tr>
				
	    	</logic:iterate>	    	  	
		</logic:present>
		
    </table>    
  	</logic:present>   
 	<br />
 	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.save"/>
  	</html:submit>
</html:form> 