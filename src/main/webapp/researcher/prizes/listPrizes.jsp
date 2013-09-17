<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em>
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.PrizeAssociation.title.label"/></h2>

<ul>
	<li>
		<html:link page="/prizes/prizeManagement.do?method=prepareCreatePrize">
			<bean:message key="label.create.prize" bundle="RESEARCHER_RESOURCES"/>
		</html:link>
	</li>
</ul>

<logic:notEmpty name="prizes">

		<p class="mtop15"><strong><bean:message key="label.prizes.list" bundle="RESEARCHER_RESOURCES"/></strong></p>

		<ul class="listresearch">
		<logic:iterate id="prize" name="prizes">
			<bean:define id="prizeID" name="prize" property="externalId"/>
			<li class="mtop1">
				<p class="mvert0">
		 			<strong>
						<html:link page="<%= "/prizes/prizeManagement.do?method=showPrize&oid=" + prizeID %>"><fr:view name="prize" property="name"/></html:link>	 			
		 			</strong>
		 			 , 
		 			<span>
		 				<fr:view name="prize" property="year"/>
			 		</span>
		 		</p>
		 		<logic:present name="prize" property="researchResult">
				<bean:define id="resultId" name="prize" property="researchResult.externalId"/>
				<bean:define id="action" value="/resultPublications/showPublication.do"/>
				<logic:equal name="prize" property="associatedToPatent" value="true">
					<bean:define id="action" value="/resultPatents/showPatent.do"/>
				</logic:equal>
		 		<p class="mvert0">
		 			<span>
		 				<html:link page="<%= action + "?resultId=" + resultId %>">
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
	<p class="mtop15">
		<em><bean:message key="label.no.prizes.in.person" bundle="RESEARCHER_RESOURCES"/>.</em>
	</p>
</logic:empty>