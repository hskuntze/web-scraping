package br.com.kuntzeprojects.webscraping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kuntzeprojects.webscraping.entities.Equipe;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long>{

}
