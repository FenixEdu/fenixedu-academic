<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<em>Cartões de Identificação</em>
<h2>
	<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.show.degree.codes.and.labels"/>
</h2>
<p><html:link page="/manageCardGeneration.do?method=showCategoryCodes">« Voltar</html:link></p>

<bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.type"/>:
<logic:iterate id="degree" name="degrees" length="1">
	<strong>
		<logic:notEqual name="degreeType" value="">
			<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
		</logic:notEqual>
		<logic:equal name="degreeType" value="">
			<bean:message bundle="ENUMERATION_RESOURCES" key="EMPTY.desc"/>
		</logic:equal>
	</strong>
</logic:iterate>


<table class="tstyle4 thlight mtop05">
  <tr>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.name"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.ministery.code"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.card.name"/></th>
    <th></th>
  </tr>
  	<logic:iterate id="degree" name="degrees">
	  	<tr>
    		<td><bean:write name="degree" property="presentationName"/></td>
    		<td class="acenter">
    			<logic:present name="degree" property="ministryCode">
	    			<bean:write name="degree" property="ministryCode"/>
    			</logic:present>
    			<logic:notPresent name="degree" property="ministryCode">
    				<logic:equal name="degree" property="degreeType.name" value="DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>    				
    				<logic:equal name="degree" property="degreeType.name" value="BOLONHA_DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>
    				<logic:equal name="degree" property="degreeType.name" value="BOLONHA_INTEGRATED_MASTER_DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>
    				<logic:notEqual name="degree" property="degreeType.name" value="DEGREE">
    					<logic:notEqual name="degree" property="degreeType.name" value="BOLONHA_DEGREE">
    						<logic:notEqual name="degree" property="degreeType.name" value="BOLONHA_INTEGRATED_MASTER_DEGREE">
    							----
    						</logic:notEqual>
    					</logic:notEqual>
    				</logic:notEqual>    				
    			</logic:notPresent>
    		</td>
    		<td>
    			<logic:present name="degree" property="idCardName">
    				<bean:define id="idCardName" name="degree" property="idCardName" type="java.lang.String"/>
    				<% if (idCardName.length() > 42) { %>
	    				<font color="red">
    				<% } %>
		    				<bean:write name="degree" property="idCardName"/>
		    		<% if (idCardName.length() > 42) { %>
	    				</font>
    				<% } %>
    			</logic:present>
    		</td>
    		<td>
    			<logic:present role="role(MANAGER)">
	    			<html:link page="/manageCardGeneration.do?method=editDegree" paramId="degreeID" paramName="degree" paramProperty="externalId">
		    			<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.edit"/>
    				</html:link>
    			</logic:present>
    		</td>
  		</tr>
	</logic:iterate>
</table>
