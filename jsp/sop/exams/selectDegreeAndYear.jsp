<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<span class="error"><html:errors/></span>
<html:form action="/ExamSearchByDegreeAndYear">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="choose"/>
    <table width="100%">
    	<tr>
        	<td class="infoop">Por favor, proceda &agrave; escolha
            do curso pretendido.</td>
        </tr>
    </table>
	<br />
    <p><bean:message key="property.context.degree"/>:
	<html:select property="index" size="1">
    	<html:options collection="<%=SessionConstants.DEGREES %>" property="value" labelProperty="label"/>
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
	<logic:present name="<%= SessionConstants.CURRICULAR_YEAR_LIST_KEY %>" scope="request">
		<logic:iterate id="item" name="<%= SessionConstants.CURRICULAR_YEAR_LIST_KEY %>">
			<html:multibox property="selectedCurricularYears">
				<bean:write name="item"/>
			</html:multibox>
			<bean:write name="item"/> º ano <br/>
		</logic:iterate>
		<html:checkbox property="selectAllCurricularYears">
			<bean:message key="checkbox.show.all"/><br />
		</html:checkbox>
	</logic:present>
	<br/>
   <p><html:submit value="Submeter" styleClass="inputbutton">
   		<bean:message key="label.next"/>
   </html:submit></p>
</html:form>