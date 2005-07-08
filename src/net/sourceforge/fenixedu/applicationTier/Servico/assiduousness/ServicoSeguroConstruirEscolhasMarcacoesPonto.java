package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.ServicoAutorizacao;
import net.sourceforge.fenixedu.applicationTier.ServicoSeguro;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.domain.Funcionario;
import net.sourceforge.fenixedu.persistenceTierJDBC.ICartaoPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.IFuncionarioPersistente;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 */
public class ServicoSeguroConstruirEscolhasMarcacoesPonto extends ServicoSeguro {
    private List _listaFuncionarios = null;

    private List _listaCartoes = null;

    private Timestamp _dataInicio;

    private Timestamp _dataFim;

    public ServicoSeguroConstruirEscolhasMarcacoesPonto(ServicoAutorizacao servicoAutorizacaoLer,
            List listaFuncionarios, List listaCartoes, Timestamp dataInicio, Timestamp dataFim) {
        super(servicoAutorizacaoLer);
        _listaFuncionarios = listaFuncionarios;
        _listaCartoes = listaCartoes;
        _dataInicio = dataInicio;
        _dataFim = dataFim;
    }

    public void execute() throws NotExecuteException {

        IFuncionarioPersistente iFuncionarioPersistente = SuportePersistente.getInstance()
                .iFuncionarioPersistente();
        ICartaoPersistente iCartaoPersistente = SuportePersistente.getInstance().iCartaoPersistente();

        Funcionario funcionario = null;
        Integer numMecanografico = null;
        List listaCartoesFuncionarios = null;

        if (_listaFuncionarios != null && _listaCartoes == null) {
            _listaCartoes = new ArrayList();
            _listaCartoes = (List) ((ArrayList) _listaFuncionarios).clone();

            ListIterator iterListaFuncionarios = _listaFuncionarios.listIterator();
            while (iterListaFuncionarios.hasNext()) {
                numMecanografico = (Integer) iterListaFuncionarios.next();
                if ((funcionario = iFuncionarioPersistente
                        .lerFuncionarioSemHistoricoPorNumMecanografico(numMecanografico.intValue())) == null) {
                    throw new NotExecuteException("error.funcionario.naoExiste");
                }

                listaCartoesFuncionarios = iCartaoPersistente.lerCartoesFuncionarioComValidade(
                        funcionario.getCodigoInterno(), _dataInicio, _dataFim);
                if (listaCartoesFuncionarios != null) {
                    ListIterator iterListaCartoesFuncionarios = listaCartoesFuncionarios.listIterator();
                    Integer numCartao = null;
                    while (iterListaCartoesFuncionarios.hasNext()) {
                        numCartao = (Integer) iterListaCartoesFuncionarios.next();
                        if (!_listaCartoes.contains(numCartao)) {
                            _listaCartoes.add(numCartao);
                        }
                    }
                }
            }
        } else if (_listaFuncionarios != null && _listaCartoes != null) {
            List listaCartoesConsultar = new ArrayList();

            ListIterator iterListaFuncionarios = _listaFuncionarios.listIterator();
            while (iterListaFuncionarios.hasNext()) {
                numMecanografico = (Integer) iterListaFuncionarios.next();
                if ((funcionario = iFuncionarioPersistente
                        .lerFuncionarioSemHistoricoPorNumMecanografico(numMecanografico.intValue())) == null) {
                    throw new NotExecuteException("error.funcionario.naoExiste");
                }
                listaCartoesFuncionarios = iCartaoPersistente.lerCartoesFuncionarioComValidade(
                        funcionario.getCodigoInterno(), _dataInicio, _dataFim);
                if (listaCartoesFuncionarios != null) {
                    ListIterator iterListaCartoesFuncionarios = listaCartoesFuncionarios.listIterator();
                    Integer numCartao = null;

                    while (iterListaCartoesFuncionarios.hasNext()) {
                        numCartao = (Integer) iterListaCartoesFuncionarios.next();
                        /* interseccao das lista */
                        if (_listaCartoes.contains(numCartao)
                                && listaCartoesFuncionarios.contains(numCartao)
                                && (!listaCartoesConsultar.contains(numCartao))) {
                            listaCartoesConsultar.add(numCartao);
                        }
                    }
                }
            }
            _listaCartoes = listaCartoesConsultar;
        }
    }

    public List getListaFuncionarios() {
        return _listaFuncionarios;
    }

    public List getListaCartoes() {
        return _listaCartoes;
    }
}