package br.com.kuntzeprojects.webscraping.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.kuntzeprojects.webscraping.entities.Partida;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

//	@Query(name = "buscar_quantidade_partidas_periodo",
//			value = "SELECT COUNT(*) FROM tb_partida AS p "
//					+ " WHERE p.data_hora_partida BETWEEN DATEADD(hour, -3, current_timestamp)"
//					+ " AND current_timestamp AND IFNULL(p.tempo_partida, 'Vazio') != 'Encerrado'",
//			nativeQuery = true)
//	Integer checkForMatchesInPeriod();

//	@Query(name = "listar_partidas_periodo",
//			value = "SELECT * FROM tb_partida AS p "
//					+ " WHERE p.data_hora_partida BETWEEN DATEADD(hour, -3, current_timestamp) "
//					+ " AND current_timestamp AND IFNULL(p.tempo_partida, 'Vazio') != 'Encerrado' ",
//			nativeQuery = true)
//	List<Partida> listMatchesInPeriod();

	@Query(name = "buscar_quantidade_partidas_periodo", value = "SELECT COUNT(*) FROM tb_partida AS p "
			+ " WHERE p.data_hora_partida BETWEEN NOW() + interval '-3 hour' "
			+ " AND NOW() AND COALESCE(p.tempo_partida, 'Vazio') != 'Encerrado' ", nativeQuery = true)
	Integer checkForMatchesInPeriod();

	@Query(name = "listar_partidas_periodo", value = "SELECT * FROM tb_partida AS p "
			+ " WHERE p.data_hora_partida BETWEEN NOW() + interval '-3 hour' "
			+ " AND NOW() AND COALESCE(p.tempo_partida, 'Vazio') != 'Encerrado' ", nativeQuery = true)
	List<Partida> listMatchesInPeriod();
}
