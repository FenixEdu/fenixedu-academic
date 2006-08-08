<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="resultParticipations" name="result" property="orderedParticipations"/>
	<bean:define id="resultParticipationsCount" name="result" property="resultParticipationsCount"/>	
	<bean:define id="result" name="result"/>
	<bean:define id="resultId" name="result" property="idInternal"/>

	<!-- Action paths definitions -->
	<bean:define id="requestParameters">
		resultId=<bean:write name="resultId"/>&resultType=<bean:write name="result" property="class.simpleName"/>
	</bean:define>
	<bean:define id="prepareEditActionPath">
		/resultParticipations/prepareEditParticipation.do?<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="saveOrder">
		/resultParticipations/saveOrder.do?<bean:write name="requestParameters"/>		
	</bean:define>
	<bean:define id="createParticipation">
		/resultParticipations/createParticipation.do?<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="removeLink">
		/resultParticipations/removeParticipation.do?<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="backLink">
		/resultParticipations/backToResult.do?<bean:write name="requestParameters"/>
	</bean:define>
	<bean:define id="cancelPath" value="<%= backLink %>"/>
	<logic:equal name="bean" property="beanExternal" value="true">
		<bean:define id="cancelPath" value="<%= prepareEditActionPath %>"/>
	</logic:equal>
	
	<!-- Schema definitions -->
	<bean:define id="resultParticipationsSchema" name="resultParticipationsSchema" type="java.lang.String"/>
	<bean:define id="createResultParticipationSchema" name="createResultParticipationSchema" type="java.lang.String"/>
	
	<!-- Titles -->		
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.superUseCaseTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.participations.useCaseTitle"/></h2>
	
	<!-- Warning/Error messages -->
	<logic:messagesPresent name="messages" message="true">
		<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
			<p><span class="error"><!-- Error messages go here --><bean:write name="messages"/></span></p>
		</html:messages>
	</logic:messagesPresent>
	
	<!-- Participation List -->
	<h4><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h4>
	<logic:empty name="resultParticipations">
 		<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.emptyList"/></em></p>
 	</logic:empty>	

	<logic:equal name="resultParticipationsCount" value="1">
		<fr:view name="resultParticipations" schema="<%= resultParticipationsSchema %>" layout="tabular">
			<fr:layout><fr:property name="sortBy" value="personOrder"/></fr:layout>
		</fr:view>
	</logic:equal>
	<logic:greaterThan name="resultParticipationsCount" value="1">
	
		<!-- form used to submit the tree structure -->
		<fr:form action="<%= saveOrder %>">
    		<input id="tree-structure" type="hidden" name="tree" value="" />
		</fr:form>
		
		<fr:view name="resultParticipations" layout="tree">
			<fr:layout>
				<fr:property name="treeId" value="tree"/>
		        <fr:property name="fieldId" value="tree-structure"/> <!-- reference to the hidden field above -->
		        
		        <fr:property name="eachLayout" value="values-dash"/>
		        <fr:property name="eachSchema" value="<%= resultParticipationsSchema %>"/>
		        
		        <fr:property name="includeImage" value="false"/>
		        
		        <fr:property name="links">
		        	<html:link page="<%= "/resultParticipations/removeParticipation.do?participationId=${idInternal}&" + requestParameters %>">
		                <bean:message key="link.remove" bundle="RESEARCHER_RESOURCES"/>
		        	</html:link>
		        </fr:property>
		        
		        <fr:property name="hiddenLinks">
		            <html:link page="<%= "/resultParticipations/moveUp.do?participationId=${idInternal}&" + requestParameters %>">
		                <bean:message key="link.moveUp" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>,
		            <html:link page="<%= "/resultParticipations/moveDown.do?participationId=${idInternal}&" + requestParameters %>">
		                <bean:message key="link.moveDown" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>,
		            <html:link page="<%= "/resultParticipations/moveTop.do?participationId=${idInternal}&" + requestParameters %>">
		                <bean:message key="link.moveTop" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>,
		            <html:link page="<%= "/resultParticipations/moveBottom.do?participationId=${idInternal}&" + requestParameters %>">
		                <bean:message key="link.moveBottom" bundle="RESEARCHER_RESOURCES"/>
		            </html:link>
	            </fr:property>
			</fr:layout>
	 	</fr:view>
	 	<br/>
	 	
	 	<div id="tree-controls" style="display: none;">
		 	<fr:form action="<%= prepareEditActionPath %>">
		        <!-- submits the form on top of the page, search for: tree-structure -->
		        <html:button property="saveButton" onclick="tree.saveTree();">
		            <bean:message key="button.save" bundle="RESEARCHER_RESOURCES"/>
		        </html:button>
		    
		        <html:submit>
		            <bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/>
		        </html:submit>
		    </fr:form>
		</div>
    	<script type="text/javascript">
		    document.getElementById("tree-controls").style.display = 'block';
		</script>
 	</logic:greaterThan>
 	<br/>

 	
 	<!-- Create new Result Participation  -->
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewPerson"/></h3>	
 	<logic:equal name="bean" property="beanExternal" value="true">
	 	<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.editResult.participation.addNewExternalPerson"/></p>
 	</logic:equal>
 	
	<fr:edit id="bean" name="bean" schema="<%= createResultParticipationSchema %>" action="<%= createParticipation %>">
		<fr:destination name="invalid" path="<%= prepareEditActionPath %>"/>	
		<fr:destination name="cancel" path="<%= cancelPath %>"/>	
	</fr:edit>
	<br/>
	
	<!-- Go to previous page -->
	<html:link page="<%= backLink %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
<br/>
