package constants.assiduousness;

public final class Constants {

	public static final String USER_KEY = "user";
	public static final String USERNAME = "username";

	public static final String CARGOS = "cargo";
	public static final String SEXO = "sexo";
	public static final String ESTADOCIVIL = "estadoCivil";

	public static final String DIAS = "dias";
	public static final String MESES = "meses";
	public static final String ANOS = "anos";

	public static final String TIPODOC = "tipoDoc";
	
	/* Comparadores */
	public static final String COMPARADOR_JUSTIFICACAO = "Justificacao";
	public static final String COMPARADOR_MARCACAO = "MarcacaoPonto";
	public static final String COMPARADOR_HORARIO_ROTATIVO = "HorarioRotativo";
	public static final String COMPARADOR_HORARIO = "Horario";

	public static final String INICIO_CONSULTA = "dataInicialConsulta";
	public static final String FIM_CONSULTA = "dataFinalConsulta";

	public static final String APPLICATION_RESOURCES = "ApplicationResources";

	public static final int MAX_DIGITOS_MECANOGRAFICO = 8;
	public static final int MAX_SIGLA = 8;
	public static final int MAX_DURACAO = 6;

	/* Cargos de Pessoa */
	public static final String GESTAO_ASSIDUIDADE = "GestaoAssiduidade";
	public static final String CONSULTA_ASSIDUIDADE = "ConsultaAssiduidade";

	/* Modalidades de Horários */
	public static final String DESFASADO = new String("desfasado");
	public static final String ESPECIFICO = new String("especifico");
	public static final String FLEXIVEL = new String("flexivel");
	public static final String ISENCAOHORARIO = new String("isencaoHorario");
	public static final String JORNADACONTINUA = new String("jornadaContinua");
	public static final String MEIOTEMPO = new String("meioTempo");
	public static final String TURNOS = new String("turnos");

	/* Mapeamento para Listagem do Fecho do Mes */
	public static final int MAP_TURNOS = 1;
	public static final int MAP_JC = 3;
	public static final int MAP_TE = 6;
	public static final int MAP_AF = 7;
	public static final int MAP_AMAMENTACAO = 8;
	public static final int MAP_ISE = 9;
	public static final int MAP_FLEXIVEL = 11;
	public static final int MAP_MEIOTEMPO = 12;
	public static final int MAP_ESP = 13;
	
	/* siglas utilizadas no ficheiro do fecho do mês */
	public static final String SALDO = "SALDO";
	public static final String SALDO_INJUSTIFICADO = "INJUS.";
	public static final String SALDO_PRIM_ESCALAO = "125%";
	public static final String SALDO_SEG_ESCALAO = "150%";
	public static final String SALDO_DEPOIS_SEG_ESCALAO = "150%+2";
	public static final String SALDO_DESCANSO = "200%";
	public static final String SALDO_NOCTURNO = "25%";
	public static final String SALDO_NOCT_PRIM_ESCALAO = "160%";
	public static final String SALDO_NOCT_SEG_ESCALAO = "190%";
	public static final String SALDO_NOCT_DEPOIS_SEG_ESCALAO = "190%+2";

	/* Siglas de Horários de Descanso e Feriado */
	public static final String FERIADO = "FERIADO";
	public static final String DS = "DS"; //domingo
	public static final String DSC = "DSC"; //sábado
	
	/* Sigla de dia Injustificado */
	public static final String INJUSTIFICADO = "INJUS";

	/* Regimes de Horários */
	public static final String NORMAL = new String("normal");
	public static final String TE = new String("trabalhadorEstudante");
	public static final String IPF = new String("isencaoPeriodoFixo");
	public static final String APOIOFAMILIA = new String("apoioFamilia");
	public static final String AMAMENTACAO = new String("amamentacao");
	public static final String ALEITACAO = new String("aleitacao");
	public static final String MENORES12 = new String("assistenciaMenores12");
	public static final String MOTIVOSSAUDE = new String("motivosSaude");
	public static final String SERVICO = new String("convenienciaServico");
	
	/* Semana */
	public static final int NUM_DIAS_SEMANA = 7;

	/* Expediente */
	public static final long EXPEDIENTE_MINIMO = 7 * 3600 * 1000 - 3600 * 1000; //7 horas 
	//	6horas e 30minutos e 59segundos do dia seguinte
	public static final long EXPEDIENTE_MAXIMO = (30 * 3600 + 30 * 60) * 1000 - 3600 * 1000;

	/* Trabalho Nocturno */
	public static final long INICIO_TRABALHO_NOCTURNO = 20 * 3600 * 1000 - 3600 * 1000; //20 horas 
	public static final long FIM_TRABALHO_NOCTURNO = 31 * 3600 * 1000 - 3600 * 1000; //7 horas do dia seguinte

	/* Isencao de Periodos Fixos */
	public static final long INICIO_REFEICAO_IPF = (11 * 3600 + 30 * 60) * 1000 - 3600 * 1000;
	//11 horas e 30 minutos em milisegundos(retira 1 hora para acertos)
	public static final long FIM_REFEICAO_IPF = (15 * 3600 + 30 * 60) * 1000 - 3600 * 1000;
	//15 horas e 30 minutos em milisegundos(retira 1 hora para acertos)

	/* Horario Flexivel */
	public static final int DURACAO_SEMANAL_FLEXIVEL = 35;
	public static final int SEMANA_TRABALHO_FLEXIVEL = 5;
	public static final long PLATAFORMAS_FIXAS_FLEXIVEL = 4 * 3600 * 1000; //4 horas diarias em milisegundos
	public static final long MAX_TRABALHO_FLEXIVEL = 5 * 3600 * 1000; //5 horas(milisegundos) de trabalho consecutivo no máximo

	/* Horario Especifico */
	public static final int SEMANA_TRABALHO_ESPECIFICO = 5;
	public static final int PLATAFORMAS_FIXAS_ESPECIFICO = 4 * 3600 * 1000; //4 horas diarias em milisegundos
	public static final long MAX_TRABALHO_ESPECIFICO = 5 * 3600 * 1000; //5 horas(milisegundos) de trabalho consecutivo no máximo

	/* Horario Isenção Horário*/
	public static final int DURACAO_SEMANAL_ISENCAO = 35;
	public static final int SEMANA_TRABALHO_ISENCAO = 5;
	public static final long MAX_TRABALHO_ISENCAO = 5 * 3600 * 1000; //5 horas(milisegundos) de trabalho consecutivo no máximo


	/* Horario Jornada Continua */
	public static final int DURACAO_SEMANAL_JORNADA = 30;
	public static final int SEMANA_TRABALHO_JORNADA = 5;
	public static final long PLATAFORMAS_FIXAS_JORNADA = 4 * 3600 * 1000; //4 horas diarias em milisegundos

	/* Horario Turnos */
	public static final int DURACAO_SEMANAL_TURNOS = 35;
	public static final int SEMANA_TRABALHO_TURNOS = 5;

	/* Horario Meio Tempo */
	public static final float DURACAO_SEMANAL_MEIOTEMPO = new Float("17.5").floatValue();
	public static final int SEMANA_TRABALHO_MEIOTEMPO = 5;
	public static final long VALIDADE_MINIMA = 31 * 24 * 3600; //1 mes em segundos
	public static final long VALIDADE_MAXIMA = 365 * 2 * 24 * 3600; //2 anos em segundos

	/* Rotacao de Horario por omissao */
	public static final int NUMDIAS_ROTACAO = 5;
	public static final int INICIO_ROTACAO = 2;

	/* Chave Horário Tipo */
	public static final int CHAVE_HTIPO = 1;

	/* Sigla dos dias anterior e seguinte */
	public static final String DIA_ANTERIOR = new String("A");
	public static final String DIA_SEGUINTE = new String("S");

	/* Justificacao de Ocorrência */
	public static final String JUSTIFICACAO_OCORRENCIA = new String("OC");
	/* Justificacao de Horas */
	public static final String JUSTIFICACAO_HORAS = new String("JH");
	/* Justificacao de Saldo */
	public static final String JUSTIFICACAO_SALDO = new String("SALD");

	/* Tipo de Dia da Parametrizacao de Justificacao */
	public static final String UTEIS = new String("U");
	public static final String TODOS = new String("T");

	/*Trabalho Extraordinário */
	public static final long PRIMEIRO_ESCALAO = 3600 * 1000; // uma hora
	public static final long SEGUNDO_ESCALAO = 2 * 3600 * 1000; // duas horas
	
	/* Status Assiduidade */
	public static final String ASSIDUIDADE_ACTIVO = "activo";
	public static final String ASSIDUIDADE_INACTIVO = "inactivo";
	public static final String ASSIDUIDADE_PENDENTE = "pendente";

}