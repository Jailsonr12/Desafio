package com.jailson.hotel.repository;

import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.HospedeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HospedeRepository extends JpaRepository<Hospede, Long> {
    Optional<Hospede> findByDocumento(String documento);
    List<Hospede> findByNomeContainingIgnoreCase(String nome);
    List<Hospede> findByTelefone(String telefone);


    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           UPDATE Hospede h
           SET h.nome      = :nome,
                h.documento = :documento,
                h.telefone  = :telefone
           WHERE h.id = :id
           """)
    int updateGuest(@Param("nome") String nome,
                    @Param("documento") String documento,
                    @Param("telefone") String telefone,
                    @Param("id") Long id);

    @Query("""
           SELECT h FROM Hospede h
           WHERE h.documento = :documento
           """)
    List<Hospede> selectGuestDocumento(@Param("documento") String documento);

    @Query("""
           SELECT h FROM Hospede h
           WHERE h.id = :id
           """)
    List<Hospede> selectGuestId(@Param("id") Long id);

}