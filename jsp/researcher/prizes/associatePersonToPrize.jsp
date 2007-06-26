<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="prizeID" name="prize" property="idInternal"/>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizeAuthors"/></h2>

<ul>
	<li>
		<html:link page="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID%>">
			<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
		</html:link>
	</li>
</ul>

<logic:equal name="prize" property="editableByCurrentUser" value="true">
<p class="mtop15 mbottom0"><strong><bean:message key="label.prize.people" bundle="RESEARCHER_RESOURCES"/></strong></p>

<logic:notEmpty name="prize" property="people" >
<fr:view name="prize" property="people" schema="showName">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop05"/>
		<fr:property name="link(remove)" value="<%= "/prizes/prizeManagement.do?method=removePersonFromPrize&oid=" + prizeID %>"/>
		<fr:property name="param(remove)" value="idInternal/pid"/>
		<fr:property name="key(remove)" value="link.remove"/>
		<fr:property name="bundle(remove)" value="RESEARCHER_RESOURCES"/>
	</fr:layout>
</fr:view>
</logic:notEmpty>

<logic:empty name="prize" property="people">  
	<p>
		<em><bean:message key="label.no.units.for.prize" bundle="RESEARCHER_RESOURCES"/></em>
	</p>
</logic:empty>


<logic:messagesPresent name="messages" message="true">
	<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
		<p><span class="error0"><!-- Error messages go here --><bean:write name="messages"/></span></strong></p>
	</html:messages>
</logic:messagesPresent>
	
<bean:define id="schema" value="create.prize.person.association.internal"/>
<logic:equal name="bean" property="external" value="true">
	<bean:define id="schema" value="create.prize.person.association.external"/>
</logic:equal>

<p class="mtop15 mbottom05">
	<strong><bean:message key="researcher.project.editProject.participants.addNewParticipant" bundle="RESEARCHER_RESOURCES"/></strong>
</p>

<logic:present name="prompt-creation">
	<div class="infoop2">
		<bean:message key="label.informationForCreateUser" bundle="RESEARCHER_RESOURCES"/>
	</div>
</logic:present>

<fr:form action="<%= "/prizes/prizeManagement.do?method=associatePersonToPrize&oid=" + prizeID %>">
<fr:edit id="createAssociation" name="bean" schema="<%= schema %>">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thmiddle thright mtop05"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="postBack" path="<%= "/prizes/prizeManagement.do?method=personPostBack&oid=" + prizeID %>"/> 
</fr:edit>

<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
<logic:present name="prompt-creation">
	<html:submit property="create"><bean:message key="button.create" bundle="APPLICATION_RESOURCES"/></html:submit>
</logic:present>
</fr:form>

</logic:equal>