package com.jailson.hotel.repository;

import com.jailson.hotel.domain.Hospede;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {
    Optional<Hospede> findByDocumento(String documento);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update Hospede h
              set h.nome      = :nome,
                  h.documento = :documento,
                  h.telefone  = :telefone
            where h.id = :id
           """)
    int updateGuest(@Param("nome") String nome,
                    @Param("documento") String documento,
                    @Param("telefone") String telefone,
                    @Param("id") Long id);

}


