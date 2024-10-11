package com.nttdata.TransaccionMs.business.Impl;

import com.nttdata.TransaccionMs.business.TransaccionMapper;
import com.nttdata.TransaccionMs.business.TransaccionService;
import com.nttdata.TransaccionMs.clients.CuentaFeignClient;
import com.nttdata.TransaccionMs.dto.SaldoResponse;
import com.nttdata.TransaccionMs.dto.SaldoUpdateRequest;
import com.nttdata.TransaccionMs.exception.CustomExceptions;
import com.nttdata.TransaccionMs.model.TransactionRequest;
import com.nttdata.TransaccionMs.model.TransactionResponse;
import com.nttdata.TransaccionMs.model.entity.Transaccion;
import com.nttdata.TransaccionMs.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransaccionServiceImp implements TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private CuentaFeignClient cuentaFeignClient;

    @Autowired
    private TransaccionMapper transaccionMapper;

    @Override
    public List<TransactionResponse> consultarHistorialTransacciones() {
        return transaccionRepository.findAll()
                .stream()
                .map(transaccionMapper::getTransaccionResponseOfTransaccion)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse registrarDeposito(TransactionRequest transactionRequest) {

        return Optional.ofNullable(cuentaFeignClient.getAccountBalanceAndType(transactionRequest.getCuentaOrigen()))
                .map(saldo -> {

                    SaldoUpdateRequest saldoUpdateRequest = new SaldoUpdateRequest();
                    saldoUpdateRequest.setNuevoSaldo(saldo.getSaldo() + transactionRequest.getMonto());
                    cuentaFeignClient.updateAccountBalance(transactionRequest.getCuentaOrigen(), saldoUpdateRequest);

                    Transaccion transaccion = transaccionMapper.getTransaccionOfTransaccionRequest(transactionRequest);
                    transaccion.setFecha(LocalDateTime.now());
                    transaccionRepository.save(transaccion);

                    return transaccionMapper.getTransaccionResponseOfTransaccion(transaccion);
                })
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Cuenta no encontrada"));
    }

    @Override
    public TransactionResponse registrarRetiro(TransactionRequest transactionRequest) {

        return Optional.ofNullable(cuentaFeignClient.getAccountBalanceAndType(transactionRequest.getCuentaOrigen()))
                .map(saldo -> {

                    if (saldo.getTipoCuenta().equals("AHORROS") && (saldo.getSaldo() - transactionRequest.getMonto()) < 0) {
                        throw new CustomExceptions.BadRequestException("No se puede realizar un retiro que deje el saldo en negativo para cuentas de ahorro");
                    }

                    if (saldo.getTipoCuenta().equals("CORRIENTE") && (saldo.getSaldo() - transactionRequest.getMonto()) < -500) {
                        throw new CustomExceptions.BadRequestException("No se puede realizar un sobregiro mayor a -500 en cuentas corrientes");
                    }

                    SaldoUpdateRequest saldoUpdateRequest = new SaldoUpdateRequest();
                    saldoUpdateRequest.setNuevoSaldo(saldo.getSaldo() - transactionRequest.getMonto());
                    cuentaFeignClient.updateAccountBalance(transactionRequest.getCuentaOrigen(), saldoUpdateRequest);

                    Transaccion transaccion = transaccionMapper.getTransaccionOfTransaccionRequest(transactionRequest);
                    transaccion.setFecha(LocalDateTime.now());
                    transaccionRepository.save(transaccion);

                    return transaccionMapper.getTransaccionResponseOfTransaccion(transaccion);
                })
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Cuenta no encontrada"));
    }

    @Override
    public TransactionResponse registrarTransferencia(TransactionRequest transactionRequest) {

        return Optional.ofNullable(cuentaFeignClient.getAccountBalanceAndType(transactionRequest.getCuentaOrigen()))
                .map(saldoOrigen -> {

                    if (saldoOrigen.getTipoCuenta().equals("AHORROS") && (saldoOrigen.getSaldo() - transactionRequest.getMonto()) < 0) {
                        throw new CustomExceptions.BadRequestException("No se puede realizar una transferencia que deje el saldo en negativo para cuentas de ahorro");
                    }

                    if (saldoOrigen.getTipoCuenta().equals("CORRIENTE") && (saldoOrigen.getSaldo() - transactionRequest.getMonto()) < -500) {
                        throw new CustomExceptions.BadRequestException("No se puede realizar un sobregiro mayor a -500 en cuentas corrientes");
                    }

                    SaldoResponse saldoDestino = cuentaFeignClient.getAccountBalanceAndType(transactionRequest.getCuentaDestino());

                    SaldoUpdateRequest saldoUpdateRequestOrigen = new SaldoUpdateRequest();
                    saldoUpdateRequestOrigen.setNuevoSaldo(saldoOrigen.getSaldo() - transactionRequest.getMonto());
                    cuentaFeignClient.updateAccountBalance(transactionRequest.getCuentaOrigen(), saldoUpdateRequestOrigen);

                    SaldoUpdateRequest saldoUpdateRequestDestino = new SaldoUpdateRequest();
                    saldoUpdateRequestDestino.setNuevoSaldo(saldoDestino.getSaldo() + transactionRequest.getMonto());
                    cuentaFeignClient.updateAccountBalance(transactionRequest.getCuentaDestino(), saldoUpdateRequestDestino);

                    Transaccion transaccion = transaccionMapper.getTransaccionOfTransaccionRequest(transactionRequest);
                    transaccion.setFecha(LocalDateTime.now());
                    transaccionRepository.save(transaccion);

                    return transaccionMapper.getTransaccionResponseOfTransaccion(transaccion);
                })
                .orElseThrow(() -> new CustomExceptions.ResourceNotFoundException("Cuenta no encontrada"));
    }
}


