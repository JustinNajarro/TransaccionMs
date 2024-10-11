package com.nttdata.TransaccionMs;

import com.nttdata.TransaccionMs.api.TransaccionesApiDelegate;
import com.nttdata.TransaccionMs.business.TransaccionService;
import com.nttdata.TransaccionMs.model.TransactionRequest;
import com.nttdata.TransaccionMs.model.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransaccionDelegateImp implements TransaccionesApiDelegate {

    @Autowired
    private TransaccionService transaccionService;

    @Override
    public ResponseEntity<List<TransactionResponse>> consultarHistorialTransacciones() {
        List<TransactionResponse> historial = transaccionService.consultarHistorialTransacciones();
        return ResponseEntity.ok(historial);
    }

    @Override
    public ResponseEntity<TransactionResponse> registrarDeposito(TransactionRequest transactionRequest) {
        TransactionResponse response = transaccionService.registrarDeposito(transactionRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransactionResponse> registrarRetiro(TransactionRequest transactionRequest) {
        TransactionResponse response = transaccionService.registrarRetiro(transactionRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TransactionResponse> registrarTransferencia(TransactionRequest transactionRequest) {
        TransactionResponse response = transaccionService.registrarTransferencia(transactionRequest);
        return ResponseEntity.ok(response);
    }
}
