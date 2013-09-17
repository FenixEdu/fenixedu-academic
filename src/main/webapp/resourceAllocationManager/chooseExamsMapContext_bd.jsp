<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/chooseExamsMapContextDA">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
    <table width="100%">
    	<tr>
        	<td class="infoop">Por favor, proceda &agrave; escolha
            do curso pretendido.</td>
        </tr>
    </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.index" property="index" size="1">
    	<html:options collection="<%=PresentationConstants.DEGREES %>" property="value" labelProperty="label"/>
    </html:select>
	</p>
	<br />
    <table width="100%">
    	<tr>
        	<td class="infoop"><bean:message key="label.select.curricularYears" /></td>
        </tr>
    </table>
	<br />

   	<bean:message key="property.context.curricular.year"/>:
   	<br />
	<br />
	<logic:present name="<%= PresentationConstants.CURRICULAR_YEAR_LIST_KEY %>" scope="request">
		<logic:iterate id="item" name="<%= PresentationConstants.CURRICULAR_YEAR_LIST_KEY %>">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedCurricularYears" property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/> � ano <br/>
		</logic:iterate>
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.selectAllCurricularYears" property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br />
		</html:checkbox>
	</logic:present>
	<br/>
   <p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.next"/>
   </html:submit></p>
</html:form>