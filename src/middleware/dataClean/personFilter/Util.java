package middleware.dataClean.personFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Util {
	public Util() {
	}

	public Object getObjectDescr(HashMap hm, String str) {
		if (hm.containsKey(str))
			return hm.get(str);
		else
			return null;
	}

	public boolean findObject(ArrayList al, String str) {
		return al.contains(str);
	}

	public String[] tokenize(String str, String expression) {
		return str.split(expression);
	}

	public Object matchExacto(String str, ArrayList al, Method met, Object args[]) {
		if (str != null) {
			int i, maxi;
			maxi = al.size();
			String resIt;
			Object metObj;

			for (i = 0; i < maxi; i++) {
				metObj = al.get(i);
				try {
					resIt = (String) met.invoke(metObj, args);
					if (resIt.equalsIgnoreCase(str))
						return metObj;
				} catch (Throwable e) {
					e.printStackTrace();
					System.err.println(e);
				}
			}
		}
		return null;
	}

	public Object matchAproximado(String str, ArrayList al, Method met, Object args[], int maxDist) {
		if (str != null) {
			int i, maxi, distValue;
			SimilarWaterman simwa = new SimilarWaterman();
			char str1[] = new char[1024];
			char str2[] = new char[1024];

			maxi = al.size();
			String resIt;
			Object metObj;
			for (i = 0; i < maxi; i++) {
				metObj = al.get(i);
				try {
					resIt = (String) met.invoke(metObj, args);

					str.getChars(0, str.length(), str1, 0);
					resIt.getChars(0, resIt.length(), str2, 0);
					distValue = simwa.sw(str1, str2, str.length(), resIt.length(), maxDist);
					if (distValue != 0 && distValue <= maxDist)
						return metObj;
				} catch (Throwable e) {
					e.printStackTrace();
					System.err.println(e);
				}
			}
		}
		return null;
	}

	/* apena retorna o valor da classe encontrada se fizer match com
	    um e um só objecto do array*/
	public Object matchExactoUnico(String str, ArrayList al, Method met, Object args[]) {
		if (str != null) {
			int i, maxi, count, index;
			maxi = al.size();
			String resIt;
			Object metObj;

			for (i = 0, count = 0, index = 0; i < maxi; i++) {
				metObj = al.get(i);
				try {
					resIt = (String) met.invoke(metObj, args);
					if (resIt.equalsIgnoreCase(str)) {
						count++;
						index = i;
					}
				} catch (Throwable e) {
					e.printStackTrace();
					System.err.println(e);
				}
			}
			if (count == 1)
				return al.get(index);
			else
				return null;
		}
		return null;
	}

	/* apena retorna o valor da classe encontrada se fizer match com
	     um e um só objecto do array*/
	public Object matchAproximadoUnico(String str, ArrayList al, Method met, Object args[], int maxDist) {
		if (str != null) {
			int i, maxi, distValue, count, index;
			SimilarWaterman simwa = new SimilarWaterman();
			char str1[] = new char[1024];
			char str2[] = new char[1024];

			maxi = al.size();
			String resIt;
			Object metObj;
			for (i = 0, count = 0, index = 0; i < maxi; i++) {
				metObj = al.get(i);
				try {
					resIt = (String) met.invoke(metObj, args);

					str.getChars(0, str.length(), str1, 0);
					resIt.getChars(0, resIt.length(), str2, 0);
					distValue = simwa.sw(str1, str2, str.length(), resIt.length(), maxDist);
					if (distValue <= maxDist) {
						count++;
						index = i;
					}
				} catch (Throwable e) {
					e.printStackTrace();
					System.err.println(e);
				}
			}
			if (count == 1)
				return al.get(index);
			else
				return null;
		}
		return null;
	}
}