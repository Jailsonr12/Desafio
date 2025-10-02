package com.jailson.hotel.repository;

import com.jailson.hotel.domain.CheckIn;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE CheckIn c
           SET c.adicionalVeiculo = :adicionalVeiculo,
               c.dataEntrada = :dataEntrada,
               c.dataSaida = :dataSaida
           WHERE c.id = :id
           """)
    int updateCheckIn(@Param("id") Long id,
                      @Param("adicionalVeiculo") boolean adicionalVeiculo,
                      @Param("dataEntrada") LocalDateTime dataEntrada,
                      @Param("dataSaida") LocalDateTime dataSaida);
    
}
