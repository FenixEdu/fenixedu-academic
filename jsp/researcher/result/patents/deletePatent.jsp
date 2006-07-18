<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.deletePatentUseCase.title"/></h3>
	
	<span class="error">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.deleteResult.warning"/>
	</span>
	<%-- Participations --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultParticipations"/></h3>
	<fr:view name="patent" property="resultParticipations" schema="result.participations" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="personOrder"/>
		</fr:layout>
	</fr:view>
	<br/>
	
	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<br/>
	
	<html:form action="/patents/patentsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deletePatent"/>
		<bean:define id="patentId" name="patent" property="idInternal" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.resultId" property="resultId" value="<%= patentId.toString() %>"/>
		
		<table>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputButton">
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
					</html:submit>
				</td>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputButton" onclick='<%= "this.form.method.value='listPatents'" %>'>
						<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
					</html:submit>
				</td>
			</tr>
		</table>
	</html:form>
</logic:present>