<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.thesis.ThesisState" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<bean:define id="listThesesActionPath" name="listThesesActionPath"/>
<bean:define id="listThesesContext" name="listThesesContext"/>
<bean:define id="listThesesSchema" name="listThesesSchema" type="java.lang.String"/>

<fr:form action="<%= String.format("%s?method=showTheses&amp;%s", listThesesActionPath, listThesesContext) %>">
	<table>
		<tr>
			<td>
				<fr:edit id="filter" name="filter" schema="<%= listThesesSchema %>">
					<fr:layout>
				        <fr:property name="classes" value="tdtop thlight thleft"/>
				        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
					</fr:layout>
					<fr:destination name="listThesis" path="<%= String.format("%s?method=showTheses&amp;%s", listThesesActionPath, listThesesContext) %>"/>
				</fr:edit>
			</td>
			<td style="vertical-align: bottom; padding-bottom: 0.75em;">
				<a href="#" onclick="switchDisplay('state-help');">
					<html:img page="/images/icon_help.gif" module=""/>
				</a>
			</td>
		</tr>
	</table>
</fr:form>

<div id="state-help" class="dnone">
	<div class="bgcolor2" style="padding: 0.5em 1em 0.5em 0;">
   		<ul>
   			<li><strong><bean:message key="ThesisState.DRAFT" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.DRAFT.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
   			<li><strong><bean:message key="ThesisState.SUBMITTED" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.SUBMITTED.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
   			<li><strong><bean:message key="ThesisState.APPROVED" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.APPROVED.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
   			<li><strong><bean:message key="ThesisState.CONFIRMED" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.CONFIRMED.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
   			<li><strong><bean:message key="ThesisState.REVISION" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.REVISION.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
   			<li><strong><bean:message key="ThesisState.EVALUATED" bundle="ENUMERATION_RESOURCES"/></strong>:
	   			<bean:message key="ThesisState.EVALUATED.help" bundle="ENUMERATION_RESOURCES"/>
   			</li>
    	</ul>
   	</div>
</div>

<logic:notEmpty name="years">
	<logic:iterate id="executionYear" name="years" type="net.sourceforge.fenixedu.domain.ExecutionYear">
		<h2 class="greytxt mtop2">
			<fr:view name="executionYear" property="year"/>
		</h2>
		
		<ul>
			<logic:iterate id="thesis" name="theses" property="<%= executionYear.getYear() %>" scope="request">
				<bean:define id="thesisId" name="thesis" property="idInternal"/>
				
				<li>
					<p class="mtop0 mbottom0">
						<html:link page="<%= String.format("%s?method=showThesisDetails&amp;%s&amp;thesisID=%s", listThesesActionPath, listThesesContext, thesisId) %>">
							<fr:view name="thesis" property="finalFullTitle"/>
						</html:link>,
						<fr:view name="thesis" property="student.person" layout="name-with-alias"/>
					</p>
					<p class="mtop05 greytxt">
						<bean:define id="andText">
							<bean:message key="and"/>
						</bean:define>
						
						<bean:message key="label.thesis.coordination"/>: 
						<fr:view name="thesis" property="orientation">
							<fr:layout name="flowLayout">
								<fr:property name="eachLayout" value="values"/>
								<fr:property name="eachSchema" value="public.thesis.coordinator"/>
								<fr:property name="htmlSeparator" value="<%= andText %>"/>
								<fr:property name="emptyMessageKey" value="message.public.thesis.coordination.empty"/>
							</fr:layout>
						</fr:view>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<bean:message key="label.thesis.state"/>: 
						<fr:view name="thesis" property="state"/>
					</p>
				</li>
				
			</logic:iterate>
		</ul>
	</logic:iterate>
</logic:notEmpty>

<logic:empty name="years">
	<p class="mtop2">
		<em>
			<bean:message key="label.theses.empty.message"/>
		</em>
	</p>
</logic:empty>
