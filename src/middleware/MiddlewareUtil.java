package middleware;

/**
 * @author tfc130
 *
 */
public abstract class MiddlewareUtil {
	public static String gerarSiglaDeLicenciatura(int codigoLicenciatura) {
		switch (codigoLicenciatura) {
			case 1 :
				return "LEC";
			case 2 :
				return "LEMIN";
			case 3 :
				return "LEM";
			case 4 :
				return "LEE"; // Extinta
			case 5 :
				return "LEQ";
			case 6 :
				return "LEMat";
			case 7 :
				return "LEFT";
			case 8 :
				return "LEN";
			case 9 :
				return "LMAC";
			case 10 :
				return "LEIC";
			case 11 :
				return "LEGI";
			case 12 :
				return "LET";
			case 13 :
				return "LEA";
			case 14 :
				return "LEEC";
			case 15 :
				return "LEAM";
			case 16 :
				return "LQ";
			case 17 :
				return "LEBL";
			case 18 :
				return "LA";
			case 19 :
				return "LESIM";
			case 20 :
				return "LCI";
			case 21 :
				return "LEBM";
			case 22 :
				return "LERCI";
			case 23 :
				return "LEGM"; // Extinta
			default :
				return "DESCONHECIDA_"+codigoLicenciatura;

		}
	}
}
