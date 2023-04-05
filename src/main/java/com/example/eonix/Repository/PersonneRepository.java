package com.example.eonix.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.eonix.Model.Personne;

@RepositoryRestResource
public interface PersonneRepository extends JpaRepository<Personne,Integer>{

    List<Personne> findByPrenomContainsIgnoreCaseAndPrenomContainsIgnoreCase(@RequestParam(required = false) String fn ,@RequestParam(required = false)  String ln);
    List<Personne> findByNomContainsIgnoreCaseAndNomContainsIgnoreCase(@RequestParam(required = false) String fn ,@RequestParam(required = false)  String ln);

    // @Query("select p from personne where (p.prenom like :fn%) and  (p.prenom like %:ln)")
    // List<Personne> searchPersonne(@Param("fn") String fn , @Param("ln") String ln);

    
}
