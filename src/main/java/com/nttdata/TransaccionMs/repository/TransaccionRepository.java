package com.nttdata.TransaccionMs.repository;

import com.nttdata.TransaccionMs.model.entity.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransaccionRepository extends MongoRepository<Transaccion, Integer> {

}
