<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<tiles:insert page="/fenixLayout_2col.jsp" flush="true">
  <tiles:put name="title" value="Candidatos de Mestrado" />
  <tiles:put name="serviceName" value="Portal de Candidatos a Pós-Graduações" />
  <tiles:put name="navLocal" value="/candidato/navigation.jsp" />
  <tiles:put name="navGeral" value="/candidato/commonNavGeralCandidate.jsp" />
  <tiles:put name="body" value="/candidato/changeSuccess_body.jsp" />
  <tiles:put name="footer" value="/copyright.jsp" />
</tiles:insert>

