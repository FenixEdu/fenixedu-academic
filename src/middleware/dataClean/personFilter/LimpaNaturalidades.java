package middleware.dataClean.personFilter;

import java.util.*;
import ServidorPersistenteFiltroPessoa.*;
import middleware.dataClean.personFilter.ServidorErros.*;

public class LimpaNaturalidades {
	public static int other = -1;
	private ArrayList _stopWords;
	private ArrayList _paises;
	private ArrayList _distritos;
	private ArrayList _concelhos;
	private ArrayList _freguesias;
	private HashMap _abrv;
	private HashMap _paisExpansao;
	private HashMap _convCidPais;
	private HashMap _convLocDistr;
	private HashMap _convLocConc;
	private HashMap _concExpansao;
	private HashMap _fregExpansao;
	private HashMap _mapDistrConcelhos;
	private HashMap _mapDistrFreguesias;
	private HashMap _mapConcFreguesias;

	public LimpaNaturalidades() {
		ServidorAbrv sa = new ServidorAbrv();

		_stopWords = sa.lerTopWords();
		_paises = sa.lerPaises();
		_distritos = sa.lerDistritos();
		_concelhos = sa.lerConcelhos();
		_freguesias = sa.lerFreguesias();
		_abrv = sa.lerAbreviaturas();
		_paisExpansao = sa.lerPaisesExpansao();
		_convCidPais = sa.lerConversaoCidadePais();
		_convLocDistr = sa.lerConversaoLocalDistrito();
		_convLocConc = sa.lerConversaoLocalConcelho();
		_concExpansao = sa.lerConcelhoExpansao();
		_fregExpansao = sa.lerFreguesiaExpansao();
		_mapDistrConcelhos = sa.lerMapConcelhos();
		_mapDistrFreguesias = sa.lerMapDistrFreguesias();
		_mapConcFreguesias = sa.lerMapFreguesias();
		Parameters par = Parameters.getInstance();
		try {
			other = new Integer(par.get("clean.other")).intValue();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Valor de other errado " + ex.toString());
		}
	}

	public LimpaNaturalidades(
		ArrayList sw,
		ArrayList pa,
		ArrayList dis,
		ArrayList conc,
		ArrayList freg,
		HashMap ab,
		HashMap pe,
		HashMap ccp,
		HashMap cld,
		HashMap clc,
		HashMap ce,
		HashMap fe,
		HashMap mdc,
		HashMap mdf,
		HashMap mcf) {
		_stopWords = sw;
		_paises = pa;
		_distritos = dis;
		_concelhos = conc;
		_freguesias = freg;
		_abrv = ab;
		_paisExpansao = pe;
		_convCidPais = ccp;
		_convLocDistr = cld;
		_convLocConc = clc;
		_concExpansao = ce;
		_fregExpansao = fe;
		_mapDistrConcelhos = mdc;
		_mapDistrFreguesias = mdf;
		_mapConcFreguesias = mcf;
	}

	public LimpaOutput limpaNats(int chave, String pais, String distr, String conc, String freg) {
		int nacVal;
		MapMatch mmOp = new MapMatch();
		LimpaOutput res, resaux;

		//Normalização
		// Pais n tem normalização (é um código com 2 dígitos)
		distr = NormalizaDistrito(distr);
		conc = NormalizaConcelho(conc);
		freg = NormalizaFreguesia(freg);

		//tratainvalidos1
		try {
			nacVal = Integer.parseInt(pais);
		} catch (NumberFormatException e) {
			nacVal = 0; // se houver erro, considera-se portugues
		}
		res = new LimpaOutput(nacVal, other, other, other);
		if (nacVal > 3) { //é estrangeiro -> retorna cod do pais e o resto é other
			return res;
		}
		if ((distr == null || distr.trim().length() == 0 || distr.startsWith("xx"))
			&& (conc == null || conc.trim().length() == 0 || conc.startsWith("xx"))
			&& (freg == null || freg.trim().length() == 0 || freg.startsWith("xx"))) {
			LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.SEM_DADOS);
			return res;
		}
		if ((distr == null || distr.trim().length() == 0 || distr.startsWith("xx"))
			&& (conc == null || conc.trim().length() == 0 || conc.startsWith("xx"))) {
			return mmOp.trataBads1ComFreg(chave, nacVal, freg, _distritos, _concelhos, _freguesias);
		}

		//existe alguma coisa nos campos disrt ou conc
		res = mmOp.defineEstrangeiros(distr, conc, _paises);
		if (res != null) //pais estrangeiro
			return res; //retorna classificacao (n existe distrito, concelho, etc.)

		res = mmOp.defineDistritro(nacVal, distr, _distritos); //cod para distrito
		if (res == null) { //bad na classificacao directa do distrito
			//classifica distrito a partir do concelho
			res = mmOp.defineDistritroPeloConcelho(nacVal, conc, _concelhos);
			if (res == null) { //nao consegue classificar o distrito
				LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_DIST);
				return new LimpaOutput(nacVal, other, other, other);
			}
		}
		//distrito já está classificado na variável res
		resaux = mmOp.defineConcelho(nacVal, res.getDistrito(), conc, _mapDistrConcelhos);
		if (resaux == null) { //nao conseguiu classificar o concelho
			resaux = mmOp.defineConcelhoPelaFreguesia(nacVal, res.getDistrito(), freg, _mapDistrFreguesias);
			if (resaux == null) { //possibilidade de distrito estar errado
				resaux = mmOp.corrigeDistritoPorConcelhoFreguesia(nacVal, conc, freg, _concelhos, _mapConcFreguesias);
				if (resaux == null) { //distrito pode estar ainda errado->corrigir apenas pelo concelho
					/*        resaux = mmOp.corrigeDistritoPorConcelho(nacVal, conc, _concelhos);
					           if( resaux == null ) //nao consegue classificar o concelho*/
					LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_CONC);
					return res;
				}
			}
		}

		//ja classificou o concelho-> inicia freguesia
		res = mmOp.defineFreguesia(nacVal, resaux.getDistrito(), resaux.getConcelho(), freg, _mapConcFreguesias);

		if (res != null) //completamente classificado
			return res;
		LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_FREG);
		return resaux; //classificado até ao concelho
	}

	/*
	 * Replicado por Tânia Pousão
	 * 
	 */
	public LimpaOutput limpaNats(String chave, String pais, String distr, String conc, String freg) {
		int nacVal;
		MapMatch mmOp = new MapMatch();
		LimpaOutput res, resaux;

		//Normalização
		// Pais n tem normalização (é um código com 2 dígitos)
		distr = NormalizaDistrito(distr);
		conc = NormalizaConcelho(conc);
		freg = NormalizaFreguesia(freg);

		//tratainvalidos1
		try {
			nacVal = Integer.parseInt(pais);
		} catch (NumberFormatException e) {
			nacVal = 0; // se houver erro, considera-se portugues
		}
		res = new LimpaOutput(nacVal, other, other, other);
		if (nacVal > 3) { //é estrangeiro -> retorna cod do pais e o resto é other
			return res;
		}
		if ((distr == null || distr.trim().length() == 0 || distr.startsWith("xx"))
			&& (conc == null || conc.trim().length() == 0 || conc.startsWith("xx"))
			&& (freg == null || freg.trim().length() == 0 || freg.startsWith("xx"))) {
			LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.SEM_DADOS);
			return res;
		}
		if ((distr == null || distr.trim().length() == 0 || distr.startsWith("xx"))
			&& (conc == null || conc.trim().length() == 0 || conc.startsWith("xx"))) {
			return mmOp.trataBads1ComFreg(chave, nacVal, freg, _distritos, _concelhos, _freguesias);
		}

		//existe alguma coisa nos campos disrt ou conc
		res = mmOp.defineEstrangeiros(distr, conc, _paises);
		if (res != null) { //pais estrangeiro
			return res; //retorna classificacao (n existe distrito, concelho, etc.)
		}

		res = mmOp.defineDistritro(nacVal, distr, _distritos); //cod para distrito
		String nomeDistrito = null;
		if (res == null) { //bad na classificacao directa do distrito
			//classifica distrito a partir do concelho
			res = mmOp.defineDistritroPeloConcelho(nacVal, conc, _concelhos);
			if (res == null) { //nao consegue classificar o distrito
				LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_DIST);
				return new LimpaOutput(nacVal, other, other, other);
			}
		} else {
			nomeDistrito = res.getNomeDistrito();
		}

		//distrito já está classificado na variável res
		resaux = mmOp.defineConcelho(nacVal, res.getDistrito(), conc, _mapDistrConcelhos);
		String nomeConcelho = null;
		if (resaux == null) { //nao conseguiu classificar o concelho
			resaux = mmOp.defineConcelhoPelaFreguesia(nacVal, res.getDistrito(), freg, _mapDistrFreguesias);
			if (resaux == null) { //possibilidade de distrito estar errado
				resaux = mmOp.corrigeDistritoPorConcelhoFreguesia(nacVal, conc, freg, _concelhos, _mapConcFreguesias);
				if (resaux == null) { //distrito pode estar ainda errado->corrigir apenas pelo concelho
					/*        resaux = mmOp.corrigeDistritoPorConcelho(nacVal, conc, _concelhos);
										 if( resaux == null ) //nao consegue classificar o concelho*/
					LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_CONC);
					return res;
				}
			}
		} else{
			nomeConcelho = resaux.getNomeConcelho();
		}

		//ja classificou o concelho-> inicia freguesia
		res = mmOp.defineFreguesia(nacVal, resaux.getDistrito(), resaux.getConcelho(), freg, _mapConcFreguesias);
		if (res != null) { //completamente classificado
			res.setNomeDistrito(nomeDistrito);
			res.setNomeConcelho(nomeConcelho);
			return res;
		}
		LogErros.adicionaErroNat(chave, pais, distr, conc, freg, Erros.ERRO_FREG);
		resaux.setNomeDistrito(nomeDistrito);
		resaux.setNomeConcelho(nomeConcelho);
		return resaux; //classificado até ao concelho
	}

	public String NormalizaFreguesia(String str) {
		String[] aStr;
		Util ut = new Util();
		Object objMatch;
		String saida = new String();
		int i;
		if (str == null || str.trim().length() == 0)
			return null;

		str = str.toLowerCase();
		if ((objMatch = ut.getObjectDescr(_fregExpansao, str)) != null)
			return ((String) objMatch);

		aStr = ut.tokenize(str, "[\\s-.]+");

		for (i = 0; i < aStr.length; i++) {
			if (!ut.findObject(_stopWords, aStr[i])) {
				if ((objMatch = ut.getObjectDescr(_abrv, aStr[i])) != null)
					aStr[i] = (String) objMatch;
				if (saida.length() == 0)
					saida = aStr[i];
				else
					saida = saida + " " + aStr[i];
			}
		}
		return saida.toLowerCase();
	}

	public String NormalizaConcelho(String str) {
		String[] aStr;
		Object objMatch;
		Util ut = new Util();
		String saida = new String();
		int i = 0;
		if (str == null || str.trim().length() == 0)
			return null;

		str = str.toLowerCase();

		if ((objMatch = ut.getObjectDescr(_concExpansao, str)) != null)
			return (String) objMatch;
		if ((objMatch = ut.getObjectDescr(_convLocConc, str)) != null)
			return (String) objMatch;
		if ((objMatch = ut.getObjectDescr(_convCidPais, str)) != null)
			return (String) objMatch;
		if ((objMatch = ut.getObjectDescr(_paisExpansao, str)) != null)
			return (String) objMatch;

		aStr = ut.tokenize(str, "[\\s-.]+");

		for (i = 0; i < aStr.length; i++) {
			if (!ut.findObject(_stopWords, aStr[i])) {
				if ((objMatch = ut.getObjectDescr(_abrv, aStr[i])) != null)
					aStr[i] = (String) objMatch;
				if (saida.length() == 0)
					saida = aStr[i];
				else
					saida = saida + " " + aStr[i];
			}
		}
		return saida.toLowerCase();
	}

	//ArrayList topW, HashMap abrv, HashMap paisConv, HashMap cidPaisConv, ArrayList paises, HashMap convLocDistr
	public String NormalizaDistrito(String str) {
		String[] aStr;
		Util ut = new Util();
		Object objMatch;
		String saida = new String();
		int j, i = 0;

		if (str == null || str.trim().length() == 0)
			return null;

		str = str.toLowerCase();

		if ((objMatch = ut.getObjectDescr(_paisExpansao, str)) != null)
			return (String) objMatch;
		if ((objMatch = ut.getObjectDescr(_convCidPais, str)) != null)
			return (String) objMatch;
		if ((objMatch = ut.getObjectDescr(_convLocDistr, str)) != null)
			return (String) objMatch;

		aStr = ut.tokenize(str, "[\\s-.]+");
		for (i = 0; i < aStr.length; i++) {
			for (j = 0; j < _paises.size(); j++)
				if (((Pais) _paises.get(j)).getPais().equalsIgnoreCase(aStr[i]))
					//pais dentro do nome do distrito
					return ((Pais) _paises.get(j)).getPais();
			if (!ut.findObject(_stopWords, aStr[i])) {
				if ((objMatch = ut.getObjectDescr(_abrv, aStr[i])) != null)
					aStr[i] = (String) objMatch;
				if (saida.length() == 0)
					saida = aStr[i];
				else
					saida = saida + " " + aStr[i];
			}
		}
		return saida.toLowerCase();
	}

}
