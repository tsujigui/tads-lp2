package br.edu.ifsp.biblioteca.repository;

import br.edu.ifsp.biblioteca.domain.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IEmprestimoRepository extends JpaRepository<Emprestimo, Long> {


    /**
     * Em sql seria algo como: SELECT * FROM emprestimo where usuario_id = ? AND data_devolucao IS NULL
     */
    List<Emprestimo> findByUsuarioIdAndDataDevolucaoIsNull(Long usuarioId);

    long countByUsuarioIdAndDataDevolucaoIsNull(Long usuarioId);

    List<Emprestimo> findByDataDevolucaoIsNullAndDataPrevistaDevolucaoBefore(LocalDate data);

    List<Emprestimo> findByExemplarIdAndDataDevolucaoIsNull(Long exemplarId);

    @Query(
            "SELECT e FROM Emprestimo e " +
                    "Where e.usuario.id = :usuarioId AND e.DataDevolucao IS NULL "
    )
    List<Emprestimo> listarEmprestimoAtivos(@Param("usuarioId") Long usuarioId);


    @Query(
            "SELECT e FROM Emprestimo e " +
                    "JOIN FETCH e.usuario " +
                    "JOIN FETCH e.exemplar ex " +
                    "JOIN FETCH ex.livro " +
                    "WHERE e.dataDevolucao IS NULL " +
                    "ORDER BY e.dataPrevistaDevolucao "
    )

    List<Emprestimo> listarAtivosComDetalhes();

    @Query(
            "SELECT e FROM Emprestimo e " +
                    "JOIN FETCH e.usuario " +
                    "JOIN FETCH e.exemplar ex " +
                    "JOIN FETCH ex.livro " +
                    "WHERE e.dataDevolucao IS NULL AND e.usuario.id = :usuarioId " +
                    "ORDER BY e.dataPrevistaDevolucao "
    )

    List<Emprestimo> listarAtivosDoUsuarioComDetalhes(@Param("usuarioId") Long usuarioId);

    @Query(
            "SELECT e FROM Emprestimo e " +
                    "JOIN FETCH e.usuario " +
                    "JOIN FETCH e.exemplar ex " +
                    "JOIN FETCH ex.livro " +
                    "WHERE e.dataDevolucao IS NULL AND e.dataPrevistaDevolucao < :hoje " +
                    "ORDER BY e.dataPrevistaDevolucao "
    )

    List<Emprestimo> listarAtrasadosComDetalhes(@Param("hoje")LocalDate hoje);

}


//O JPQL (Jakarta Persistence Query Language, anteriormente Java Persistence Query Language) é uma linguagem de consulta
// orientada a objetos definida pela especificação do Jakarta Persistence (JPA).

//Principais Características

//Orientação a Objetos: Você trabalha diretamente com o modelo de domínio em Java. Exemplo: SELECT p FROM Pedido p WHERE p.status = :status.

//Portabilidade: É independente do banco de dados.
// O provedor JPA (como o Hibernate) traduz o JPQL para o dialeto SQL específico do banco utilizado (MySQL, PostgreSQL, Oracle, etc.).

//Similaridade com SQL: Possui cláusulas muito
// parecidas com o SQL padrão (SELECT, FROM, WHERE, GROUP BY, HAVING, ORDER BY, além de junções como JOIN e JOIN FETCH).

//Case-Insensitive: As palavras-chave do JPQL e os nomes das entidades/atributos não diferenciam
// maiúsculas de minúsculas, exceto quando referenciam nomes de classes ou constantes Java.