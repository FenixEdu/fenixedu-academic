<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.EvaluationType" %>
<logic:present name="siteView">
<bean:message key="classification.nonOfficial.information" />
<br />
<bean:define id="marksListComponent" name="siteView" property="component" type="net.sourceforge.fenixedu.dataTransferObject.InfoSiteMarks"/>
<bean:define id="infoEvaluation" name="marksListComponent" property="infoEvaluation" type="net.sourceforge.fenixedu.dataTransferObject.InfoEvaluation"/>
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
		<th class="listClasses-header">
			<bean:message key="label.number" /> 
	    </th>
		<th class="listClasses-header">
			<bean:message key="label.name" />
		</th>					
		<th class="listClasses-header">
			<bean:message key="label.mark" />
		</th>
	</tr>    		
		
	<logic:present name="marksListComponent" property="infoAttends">  								
	   	<logic:iterate id="attendElem" name="marksListComponent" property="infoAttends" type="net.sourceforge.fenixedu.dataTransferObject.InfoFrequenta"> 
	   		<bean:define id="studentNumber" name="attendElem" property="aluno.number" />
	   		<bean:define id="studentMark" value=""/>
			<logic:notEmpty name="marksListComponent" property='<%="marks(" + studentNumber + ")"%>'>
		    	<bean:define id="studentMark" name="marksListComponent" property='<%="marks(" + studentNumber + ")"%>' type="java.lang.String"/>
	    	</logic:notEmpty>
	    	
			<tr>
				<td class="listClasses">
					<bean:write name="attendElem" property="aluno.number"/>&nbsp;
				</td>
				<td class="listClasses">
					<bean:write name="attendElem" property="aluno.infoPerson.nome"/>
				</td>											
				<td class="listClasses">
					<logic:notEmpty name="studentMark">
						<bean:write name="studentMark"/>
					</logic:notEmpty>
					<logic:empty name="studentMark">
						&nbsp;
					</logic:empty>
				</td>
			</tr>
	   	</logic:iterate>
	</logic:present>
</table>    
</logic:present>   