<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value=".IST - Candidatos de Mestrado" />
  <tiles:put name="serviceName" value="Portal de Coordenador" />
  <tiles:put name="navLocal" value="" />
  <tiles:put name="navGeral" value="/coordinator/commonNavGeralCandidate.jsp" />
  <tiles:put name="body-context" value=""/>  
  <tiles:put name="body" value="/coordinator/chooseDegreePage_bd.jsp" />
  <tiles:put name="footer" value="/coordinator/copyrightDefault.jsp" />
</tiles:insert>