package middleware.dataClean.personFilter.ServidorErros;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import middleware.dataClean.personFilter.Parameters;

public class LogErros {
	public LogErros() {
	}

	public static void adicionaErro(String str1, String str2) {
		Parameters par = Parameters.getInstance();
		BufferedWriter bw;
		if (par.get("clean.log").equals("false"))
			return;
		try {
			bw = new BufferedWriter(new FileWriter(par.get("clean.errorFile"), true));
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("(d/M/y) H'h'm'\'s'\''\'");

			bw.write(sdf.format(cal.getTime()) + " -> " + str1);
			bw.newLine();
			bw.write("\t");
			bw.write(str2);
			bw.newLine();
			bw.newLine();
			bw.close();
		} catch (IOException ex) {
			System.out.println("Problemas com o ficheiro de erros " + ex.toString());
		}
	}

	public static void adicionaErroNat(int chave, String pa, String di, String co, String fr, String descr) {
		adicionaErro(descr, "Registo(chave, pais, distr, conc, freg): " + chave + ", " + pa + ", " + di + ", " + co + ", " + fr);
	}

	/*
	 * Replicado por Tânia Pousão
	 * 
	 */
	public static void adicionaErroNat(String chave, String pa, String di, String co, String fr, String descr) {
		adicionaErro(descr, "Registo(chave, pais, distr, conc, freg): " + chave + ", " + pa + ", " + di + ", " + co + ", " + fr);
	}

	public static void limpaErros() {
		Parameters par = Parameters.getInstance();

		if (par.get("clean.log").equals("false"))
			return;

		File f = new File(par.get("clean.errorFile"));
		f.delete();
	}

}