package Util;

import java.io.Serializable;

public class ErrorConstants implements Serializable {
    public final static String PERSISTENCE_ERROR = new String("error.persistence");
    public final static String AUTHORIZE_ERROR = new String("error.authorization");
    public final static String TURMA_EXIST_ERROR = new String("error.turma.exist");
    public final static String TURMA_NOT_EXIST_ERROR = new String("error.turma.notExist");
    public final static String TURMAS_NOT_EXIST_ERROR = new String("error.turmas.notExist");
    public final static String LICENCIATURAS_EXECUCAO_NOT_EXIST_ERROR = new String("error.licenciaturasExecucao.notExist");
}

// Created by Nuno Antão