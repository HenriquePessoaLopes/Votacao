package com.faculdade.votacao.interfaces;

import com.faculdade.votacao.enums.CpfStatus;

public interface CpfValidatorInterface {
    CpfStatus validarCpf(String cpf);
}