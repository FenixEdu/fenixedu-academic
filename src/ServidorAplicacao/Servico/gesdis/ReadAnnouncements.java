/*
 * Created on 14/Mar/2003
 */
package ServidorAplicacao.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ServidorAplicacao.IServico;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadAnnouncements implements IServico {
    
	private static ReadAnnouncements _servico = new ReadAnnouncements();
    
	/**
	 * The singleton access method of this class.
	 **/
	public static ObterAnuncios getService() {
		return _servico;
	}
    
	/**
	 * The ctor of this class.
	 **/
	private ObterAnuncios() {
	}
    
	/**
	 * Returns the name of this service.
	 **/
	public final String getNome() {
		return "ObterAnuncios";
	}
    
	/**
	 * Executes the service.
	 *
	 **/
	public List run(SitioView sitio) throws Exception {
        
		ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        
		List listaAnuncios = sp.getISitioPersistente().readAnunciosByNomeSitio(sitio.getNome());
		List listaAnunciosView = new ArrayList();
        
		if (listaAnuncios != null && listaAnuncios.isEmpty() == false) {
			Iterator iterAnuncios = listaAnuncios.iterator();
			while (iterAnuncios.hasNext()) {
				listaAnunciosView.add(new AnuncioView((IAnuncio)iterAnuncios.next()));
			}
		}
        
		return listaAnunciosView;
	}
    
}

