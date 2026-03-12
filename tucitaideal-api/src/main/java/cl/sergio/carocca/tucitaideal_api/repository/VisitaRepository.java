package cl.sergio.carocca.tucitaideal_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergio.carocca.tucitaideal_api.entity.Visita;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, String> {
}