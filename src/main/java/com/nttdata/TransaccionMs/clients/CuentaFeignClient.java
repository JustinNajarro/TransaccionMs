package com.nttdata.TransaccionMs.clients;

import com.nttdata.TransaccionMs.dto.SaldoResponse;
import com.nttdata.TransaccionMs.dto.SaldoUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "AccountMs")
public interface CuentaFeignClient {
    @GetMapping("/cuentas/numero/{numeroCuenta}/saldo-tipo")
    SaldoResponse getAccountBalanceAndType(@PathVariable("numeroCuenta") String numeroCuenta);

    @PutMapping("/cuentas/numero/{numeroCuenta}/actualizar-saldo")
    void updateAccountBalance(@PathVariable("numeroCuenta") String numeroCuenta,
                              @RequestBody SaldoUpdateRequest saldoUpdateRequest);
}
