<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.research"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="label.prizes"/></h2>

<bean:define id="prizeID" name="prize" property="idInternal"/>
<ul>
		<li>
			<html:link page="/prizes/prizeManagement.do?method=listPrizes">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.backTo.link" />
			</html:link>
		</li>
		<logic:equal name="prize" property="deletableByCurrentUser" value="true">
			<li>
			<html:link page="<%="/prizes/prizeManagement.do?method=prepareDelete&oid=" + prizeID%>"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.prize.delete" /></html:link> 
			</li>
		</logic:equal>
</ul>

<logic:equal name="prize" property="deletableByCurrentUser" value="true">
	<logic:present name="deleteRequest">
		<div  class="mtop1 mbottom15 warning0" style="padding: 0.5em;">
			<p class="mtop0 mbottom05"><bean:message key="researcher.prize.delete.confirmation"/></p>
			<fr:form action="<%="/prizes/prizeManagement.do?method=deletePrize&oid=" + prizeID%>">
				<html:submit property="confirm">
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
				</html:submit>
				<html:submit>
					<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
				</html:submit>
			</fr:form>
		</div>
	</logic:present>
</logic:equal>


	<logic:equal name="prize" property="editableByCurrentUser" value="true">
	<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
	<html:link page="<%="/prizes/prizeManagement.do?method=editPrize&oid=" + prizeID  %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.prizeData" />
	</html:link>,
	<html:link page="<%= "/prizes/prizeManagement.do?method=associatePerson&oid=" + prizeID  %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="label.prizeAuthors" />
	</html:link>,
	<html:link page="<%="/prizes/prizeManagement.do?method=associateUnit&oid=" + prizeID  %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.prizeUnits" />
	</html:link>
	</logic:equal>
	
<fr:view name="prize" schema="prize.view.details">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2 thleft thlight thtop"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
		<fr:property name="columnClasses" value="width10em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	</fr:layout>
	<fr:destination name="view.publication" path="/showResearchResult.do?method=showPublication&resultId=${idInternal}"/> 
	<fr:destination name="view.patent" path="/resultPatents/showPatent.do?resultId=${idInternal}"/> 
</fr:view>