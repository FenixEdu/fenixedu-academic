/*
 * ISeccao.java
 *
 * Created on 23 de Agosto de 2002, 15:29
 */

package Dominio;

/**
 *
 * @author  ars
 */

import java.util.List;

public interface ISeccao {
    String getNome();
    int getOrdem();
    ISitio getSitio();
    ISeccao getSeccaoSuperior();
    List getSeccoesInferiores();
    List getItens();
    
    void setNome(String nome);
    void setOrdem(int ordem);
    void setSitio(ISitio sitio);
    void setSeccaoSuperior(ISeccao seccao);
    void setItens(List itens);
}
