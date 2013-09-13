<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h1><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.PrizeAssociation.title.label"/></h1>


<strong><bean:message key="label.prizes.list" bundle="RESEARCHER_RESOURCES"/>:</strong>

<logic:notEmpty name="prizes">
		<ul class="listresearch">
		<logic:iterate id="prize" name="prizes">
			<bean:define id="prizeID" name="prize" property="externalId"/>
			<li class="mtop1">
				<p class="mvert0">
		 			<strong>
						<html:link page="<%= "/prizes/showPrizes.do?method=showPrize&oid=" + prizeID %>"><fr:view name="prize" property="name"/></html:link>	 			
		 			</strong>
		 			 , 
		 			<span>
		 				<fr:view name="prize" property="year"/>
			 		</span>
		 		</p>
		 		<logic:present name="prize" property="researchResult">
				<bean:define id="resultId" name="prize" property="researchResult.externalId"/>
				<bean:define id="action" value="/showResearchResult.do?method=showPublication"/>
				<logic:equal name="prize" property="associatedToPatent" value="true">
					<bean:define id="action" value="/showResearchResult.do?method=showPatent"/>
				</logic:equal>
		 		<p class="mvert0">
		 			<span>
		 				<html:link page="<%= action + "&resultId=" + resultId %>">
			 				<fr:view name="prize" property="researchResult.title"/>
		 				</html:link>
			 		</span>
	 			</p>
	 			</logic:present>
	 			<logic:present name="prize" property="description">
				<p class="mvert0">
					<span class="color888">
						<fr:view name="prize" property="description"/>
					</span>
				</p>
				</logic:present>
 			</li>
 			
 			</logic:iterate>
		</ul>
</logic:notEmpty>

<logic:empty name="prizes">
	<bean:message key="label.no.prizes.in.person" bundle="RESEARCHER_RESOURCES"/>
</logic:empty>