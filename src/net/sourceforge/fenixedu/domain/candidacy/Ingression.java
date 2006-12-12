/**
 * 
 */
package net.sourceforge.fenixedu.domain.candidacy;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public enum Ingression {

    ACO01("Acordo ELF"), ACO02("Acordo Angola Telecom"), CEA01("Ad-Hoc"), CEA02(
	    "Cursos Médios e Superiores"), CEA03("Sistemas de Ensino Superior Estrangeiro"), CNA01(
	    "Contingente Geral"), CNA02("Contingente Açores"), CNA03("Contingente Madeira"), CNA04(
	    "Contingente Macau"), CNA05("Contingente Emigrantes"), CNA06("Contingente Militar"), CNA07(
	    "Contingente Deficientes"), CON01("Convénio Universidade dos Açores"), ENC01(
	    "Transferências Externas"), ENC02("Mudanças de Curso Externas"), REA01(
	    "Funcionários portugueses de missão diplomática no estrangeiro e seus familiares que os acompanham"), REA02(
	    "Cidadãos portugueses bolseiros no estrangeiro, funcionários públicos em missão oficial no estrangeiro ou funcionários portugueses da CE e seus familiares que os acompanham"), REA03(
	    "Oficiais do quadro permanente das Forças Armadas Portuguesas, no âmbito da satisfação de necessidades específicas de formação das Forças Armadas."), REA04(
	    "Funcionários estrangeiros de missão diplomática acreditada em Portugal e seus familiares aqui residentes em regime de reciprocidade"), REA05(
	    "Estudantes nacionais dos países africanos de expressão portuguesa, bolseiros do Governo Português, da Fundação Calouste Gulbenkian e ao abrigo de convenções com a CE, com frequência de ensino superior"), REA06(
	    "Estudantes nacionais dos países africanos de expressão portuguesa, bolseiros do Governo Português, da Fundação Calouste Gulbenkian e ao abrigo de convenções com a CE, com o 12º ano de escolaridade português"), REA07(
	    "Atletas de Alta Competição"), REA08(
	    "Naturais e filhos de naturais de territórios sob administração portuguesa, mas temporariamente ocupados por Forças Armadas e Estados Estrangeiros"), REA09(
	    "Estudantes nacionais da República de Angola, não bolseiros e que não tenham residido em território português durante a aquisição da habilitação precedente ao 12º ano de escolaridade"), VAG01(
	    "Vagas Adicionais - Vagas que são necessárias criar por erros de serviços do Ministério da Educação (Direcção Geral de Acesso ao Ensino Superior)"), CIA2C(
	    "Concurso Interno de Acesso ao 2º Ciclo"), PMT("Permuta"), MCI("Mudança de Curso Interna");

    String description;

    private Ingression(String description) {
	this.description = description;
    }

    public String getName() {
	return name();
    }

    public String getDescription() {
	return (description.length() > 50 ? description.substring(0, 49) + " ..." : description);
    }

    public String getFullDescription() {
	return description;
    }

    public boolean hasEntryPhase() {
	return this.equals(Ingression.CNA01) || this.equals(Ingression.PMT);
    }

}
