package middleware.dataClean.personFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;
import middleware.dataClean.personFilter.ServidorErros.*;

public class MapMatch {
	public MapMatch() {
	}

	public LimpaOutput trataBads1ComFreg(
		int chave,
		int nacVal,
		String freg,
		ArrayList distritos,
		ArrayList concelhos,
		ArrayList freguesias) {
		LimpaOutput res = new LimpaOutput();
		Util ut = new Util();
		Class clMet;
		Method mm;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		res.setPais(nacVal);
		try {
			clMet = Distrito.class;
			mm = clMet.getMethod("getDistrito", params);
			Distrito dd = (Distrito) ut.matchExacto(freg, distritos, mm, arglist);
			if (dd != null) {
				res.setDistrito(dd.getChave());
				res.setNomeDistrito(dd.getDistrito());
			} else {
				dd = (Distrito) ut.matchAproximado(freg, distritos, mm, arglist, 1);
				if (dd != null) {
					res.setDistrito(dd.getChave());
					res.setNomeDistrito(dd.getDistrito());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		try {
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);
			Concelho cc = (Concelho) ut.matchExacto(freg, concelhos, mm, arglist);
			if (cc != null) {
				res.setDistrito(cc.getDistrito());
				res.setConcelho(cc.getChave());
				res.setNomeConcelho(cc.getConcelho());
			} else {
				cc = (Concelho) ut.matchAproximado(freg, concelhos, mm, arglist, 1);
				if (cc != null) {
					res.setDistrito(cc.getDistrito());
					res.setConcelho(cc.getChave());
					res.setNomeConcelho(cc.getConcelho());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}

		try {
			clMet = Freguesia.class;
			mm = clMet.getMethod("getFreguesia", params);
			Freguesia ff = (Freguesia) ut.matchExactoUnico(freg, freguesias, mm, arglist);
			if (ff != null) {
				res.setDistrito(ff.getDistrito());
				res.setConcelho(ff.getConcelho());
				res.setFreguesia(ff.getChave());
				res.setNomeFreguesia(ff.getFreguesia());
			} else {
				ff = (Freguesia) ut.matchAproximadoUnico(freg, freguesias, mm, arglist, 1);
				if (ff != null) {
					res.setDistrito(ff.getDistrito());
					res.setConcelho(ff.getConcelho());
					res.setFreguesia(ff.getChave());
					res.setNomeFreguesia(ff.getFreguesia());
				}
			}
			String erroDescr = new String();
			if (res.getFreguesia() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_FREG;
			if (res.getConcelho() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_CONC;
			if (res.getDistrito() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_DIST;
			if (res.getFreguesia() == LimpaNaturalidades.other)
				LogErros.adicionaErroNat(chave, new Integer(nacVal).toString(), "", "", freg, erroDescr);

		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return res;
	}

	/*
	 * Replicado por Tânia Pousão
	 * 
	 */
	public LimpaOutput trataBads1ComFreg(
		String chave,
		int nacVal,
		String freg,
		ArrayList distritos,
		ArrayList concelhos,
		ArrayList freguesias) {
		LimpaOutput res = new LimpaOutput();
		Util ut = new Util();
		Class clMet;
		Method mm;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		res.setPais(nacVal);
		try {
			clMet = Distrito.class;
			mm = clMet.getMethod("getDistrito", params);
			Distrito dd = (Distrito) ut.matchExacto(freg, distritos, mm, arglist);
			if (dd != null) {
				res.setDistrito(dd.getChave());
				res.setNomeDistrito(dd.getDistrito());
			} else {
				dd = (Distrito) ut.matchAproximado(freg, distritos, mm, arglist, 1);
				if (dd != null) {
					res.setDistrito(dd.getChave());
					res.setNomeDistrito(dd.getDistrito());
				}
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
		try {
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);
			Concelho cc = (Concelho) ut.matchExacto(freg, concelhos, mm, arglist);
			if (cc != null) {
				res.setDistrito(cc.getDistrito());
				res.setConcelho(cc.getChave());
				res.setNomeConcelho(cc.getConcelho());
			} else {
				cc = (Concelho) ut.matchAproximado(freg, concelhos, mm, arglist, 1);
				if (cc != null) {
					res.setDistrito(cc.getDistrito());
					res.setConcelho(cc.getChave());
					res.setNomeConcelho(cc.getConcelho());
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}

		try {
			clMet = Freguesia.class;
			mm = clMet.getMethod("getFreguesia", params);
			Freguesia ff = (Freguesia) ut.matchExactoUnico(freg, freguesias, mm, arglist);
			if (ff != null) {
				res.setDistrito(ff.getDistrito());
				res.setConcelho(ff.getConcelho());
				res.setFreguesia(ff.getChave());
				res.setNomeFreguesia(ff.getFreguesia());
			} else {
				ff = (Freguesia) ut.matchAproximadoUnico(freg, freguesias, mm, arglist, 1);
				if (ff != null) {
					res.setDistrito(ff.getDistrito());
					res.setConcelho(ff.getConcelho());
					res.setFreguesia(ff.getChave());
					res.setNomeFreguesia(ff.getFreguesia());
				}
			}

			String erroDescr = new String();
			if (res.getFreguesia() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_FREG;
			if (res.getConcelho() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_CONC;
			if (res.getDistrito() == LimpaNaturalidades.other)
				erroDescr = Erros.ERRO_CF_DIST;
			if (res.getFreguesia() == LimpaNaturalidades.other)
				LogErros.adicionaErroNat(chave, new Integer(nacVal).toString(), "", "", freg, erroDescr);

		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return res;
	} //LimpaOutput

	/* verifica se no campo distrito ou concelho existe esta o pais de origem*/
	public LimpaOutput defineEstrangeiros(String distr, String conc, ArrayList paises) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		Pais p;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		try { //procura paises no campo distrito
			clMet = Pais.class;
			mm = clMet.getMethod("getPais", params);

			if (distr != null && distr.trim().length() != 0) {
				p = (Pais) ut.matchExacto(distr, paises, mm, arglist);
				if (p != null)
					return new LimpaOutput(p.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other, LimpaNaturalidades.other);
				else {
					p = (Pais) ut.matchAproximado(distr, paises, mm, arglist, 1);
					if (p != null)
						return new LimpaOutput(p.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other, LimpaNaturalidades.other);
				}
			}
			//procura paises no campo concelho
			if (conc != null && conc.trim().length() != 0) {
				p = (Pais) ut.matchExacto(conc, paises, mm, arglist);
				if (p != null)
					return new LimpaOutput(p.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other, LimpaNaturalidades.other);
				else {
					p = (Pais) ut.matchAproximado(conc, paises, mm, arglist, 1);
					if (p != null)
						return new LimpaOutput(p.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other, LimpaNaturalidades.other);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return null;
	}

	public LimpaOutput defineDistritro(int pais, String distr, ArrayList distritos) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (distr == null || distr.trim().length() == 0)
			return limpaOutput;

		try { //match exacto de distrito
			clMet = Distrito.class;
			mm = clMet.getMethod("getDistrito", params);
			Distrito newd = (Distrito) ut.matchExacto(distr, distritos, mm, arglist);
			if (newd != null) {
				limpaOutput = new LimpaOutput(pais, newd.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other);
				limpaOutput.setNomeDistrito(newd.getDistrito());
				return limpaOutput;
			} else {
				newd = (Distrito) ut.matchAproximado(distr, distritos, mm, arglist, 1);
				if (newd != null) {
					limpaOutput = new LimpaOutput(pais, newd.getChave(), LimpaNaturalidades.other, LimpaNaturalidades.other);
					limpaOutput.setNomeDistrito(newd.getDistrito());
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

	public LimpaOutput defineDistritroPeloConcelho(int pais, String conc, ArrayList concelhos) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (conc == null || conc.trim().length() == 0)
			return limpaOutput;
		try { //match exacto de distrito
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);
			Concelho newc = (Concelho) ut.matchExacto(conc, concelhos, mm, arglist);
			if (newc != null) {
				limpaOutput = new LimpaOutput(pais, newc.getDistrito(), LimpaNaturalidades.other, LimpaNaturalidades.other);
				return limpaOutput;
			} else {
				newc = (Concelho) ut.matchAproximado(conc, concelhos, mm, arglist, 1);
				if (newc != null) {
					limpaOutput = new LimpaOutput(pais, newc.getDistrito(), LimpaNaturalidades.other, LimpaNaturalidades.other);
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return null;
	}

	public LimpaOutput defineConcelho(int pais, int distr, String conc, HashMap mapDC) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		ArrayList concelhos;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		try { //match exacto de concelho
			//concelhos pertencentes ao distrito distr
			concelhos = (ArrayList) mapDC.get(new Integer(distr));
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);

			Concelho newc = (Concelho) ut.matchExacto(conc, concelhos, mm, arglist);
			if (newc != null) {
				limpaOutput = new LimpaOutput(pais, distr, newc.getChave(), LimpaNaturalidades.other);
				limpaOutput.setNomeConcelho(newc.getConcelho());
				return limpaOutput;
			} else { //match aproximado
				newc = (Concelho) ut.matchAproximado(conc, concelhos, mm, arglist, 2);
				if (newc != null) {
					limpaOutput = new LimpaOutput(pais, distr, newc.getChave(), LimpaNaturalidades.other);
					limpaOutput.setNomeConcelho(newc.getConcelho());
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

	public LimpaOutput defineConcelhoPelaFreguesia(int pais, int distr, String freg, HashMap mapDF) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		ArrayList freguesias;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (freg == null)
			return limpaOutput;
		try { //match exacto de concelho
			//freguesias pertencentes ao distrito distr
			freguesias = (ArrayList) mapDF.get(new Integer(distr));
			clMet = Freguesia.class;
			mm = clMet.getMethod("getFreguesia", params);

			Freguesia newf = (Freguesia) ut.matchExacto(freg, freguesias, mm, arglist);
			if (newf != null) {
				limpaOutput = new LimpaOutput(pais, distr, newf.getConcelho(), LimpaNaturalidades.other);
				return limpaOutput;
			} else { //match aproximado
				newf = (Freguesia) ut.matchAproximado(freg, freguesias, mm, arglist, 1);
				if (newf != null) {
					limpaOutput = new LimpaOutput(pais, distr, newf.getConcelho(), LimpaNaturalidades.other);
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

	public LimpaOutput corrigeDistritoPorConcelhoFreguesia(int pais, String conc, String freg, ArrayList concelhos, HashMap mapCF) {

		int index = -1;
		Util ut = new Util();
		Class clMet;
		Method mm;
		ArrayList freguesias;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (conc == null || freg == null)
			return limpaOutput;
		try {
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);

			Concelho newc = (Concelho) ut.matchExacto(conc, concelhos, mm, arglist);
			if (newc != null)
				index = newc.getChave();
			else { //match aproximado
				newc = (Concelho) ut.matchAproximado(conc, concelhos, mm, arglist, 1);
				if (newc != null)
					index = newc.getChave();
			}
			if (index != -1) {
				clMet = Freguesia.class;
				mm = clMet.getMethod("getFreguesia", params);
				freguesias = (ArrayList) mapCF.get(new Integer(index));
				Freguesia newf = (Freguesia) ut.matchExacto(freg, freguesias, mm, arglist);
				if (newf != null) {
					limpaOutput = new LimpaOutput(pais, newf.getDistrito(), newf.getConcelho(), LimpaNaturalidades.other);
					return limpaOutput;
				} else {
					newf = (Freguesia) ut.matchAproximado(freg, freguesias, mm, arglist, 1);
					if (newf != null) {
						limpaOutput = new LimpaOutput(pais, newf.getDistrito(), newf.getConcelho(), LimpaNaturalidades.other);
						return limpaOutput;
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

	public LimpaOutput corrigeDistritoPorConcelho(int pais, String conc, ArrayList concelhos) {

		Util ut = new Util();
		Class clMet;
		Method mm;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (conc == null)
			return limpaOutput;

		try {
			clMet = Concelho.class;
			mm = clMet.getMethod("getConcelho", params);

			Concelho newc = (Concelho) ut.matchExacto(conc, concelhos, mm, arglist);
			if (newc != null) {
				limpaOutput = new LimpaOutput(pais, newc.getDistrito(), newc.getChave(), LimpaNaturalidades.other);
				return limpaOutput;
			} else { //match aproximado
				newc = (Concelho) ut.matchAproximado(conc, concelhos, mm, arglist, 1);
				if (newc != null) {
					limpaOutput = new LimpaOutput(pais, newc.getDistrito(), newc.getChave(), LimpaNaturalidades.other);
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

	public LimpaOutput defineFreguesia(int pais, int distr, int conc, String freg, HashMap mapCF) {
		Util ut = new Util();
		Class clMet;
		Method mm;
		ArrayList freguesias;
		Class[] params = new Class[0]; //so vou chamar metodos sem parametros
		Object arglist[] = new Object[0]; //so vou chamar metodos sem argumentos

		LimpaOutput limpaOutput = null;

		if (freg == null)
			return limpaOutput;
		
		try { //match exacto da freguesias
			//freguesias pertencentes ao concelho conc
			freguesias = (ArrayList) mapCF.get(new Integer(conc));
			clMet = Freguesia.class;
			mm = clMet.getMethod("getFreguesia", params);

			Freguesia newf = (Freguesia) ut.matchExacto(freg, freguesias, mm, arglist);
			if (newf != null) {
				limpaOutput = new LimpaOutput(pais, distr, conc, newf.getChave());
				limpaOutput.setNomeFreguesia(newf.getFreguesia());
				return limpaOutput;
			}
			else { //match aproximado
				newf = (Freguesia) ut.matchAproximado(freg, freguesias, mm, arglist, 2);
				if (newf != null) {
					limpaOutput = new LimpaOutput(pais, distr, conc, newf.getChave());
					limpaOutput.setNomeFreguesia(newf.getFreguesia());
					return limpaOutput;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.err.println(e);
		}
		return limpaOutput;
	}

}