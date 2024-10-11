package com.nttdata.TransaccionMs.business;

import com.nttdata.TransaccionMs.model.TransactionRequest;
import com.nttdata.TransaccionMs.model.TransactionResponse;

import java.util.List;

public interface TransaccionService {
    List<TransactionResponse> consultarHistorialTransacciones();
    TransactionResponse registrarDeposito(TransactionRequest transactionRequest);
    TransactionResponse registrarRetiro(TransactionRequest transactionRequest);
    TransactionResponse registrarTransferencia(TransactionRequest transactionRequest);

}
