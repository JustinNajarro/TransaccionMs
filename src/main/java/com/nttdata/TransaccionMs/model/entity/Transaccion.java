package com.nttdata.TransaccionMs.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "transaccion")
public class Transaccion {

    private TipoTransaccionEnum tipo;
    private Double monto;
    private LocalDateTime fecha;
    private String cuentaOrigen;
    private String cuentaDestino;
}
