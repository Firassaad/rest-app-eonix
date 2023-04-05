package com.example.eonix;

import java.util.List;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.eonix.Model.Personne;
import com.example.eonix.Repository.PersonneRepository;
import jakarta.transaction.Transactional;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EonixApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
    private MockMvc mockMvc;

	@Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PersonneRepository pRepository;

    @Test
	@Transactional 
    void testGetAll() {
        // donners pour tester
        Personne user1 = new Personne("John", "Doe");
        Personne user2 = new Personne("Jane", "Doe");
        pRepository.save(user1);
        pRepository.save(user2);

        // requette GET à l'endpoint qui provoque  getAll methode
        ResponseEntity<List<Personne>> response = restTemplate.exchange("/personnes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Personne>>() {});
		

        // verifier le resultat = valeur attendue
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getPrenom()).isEqualTo("John");
        assertThat(response.getBody().get(0).getNom()).isEqualTo("Doe");
        assertThat(response.getBody().get(1).getPrenom()).isEqualTo("Jane");
        assertThat(response.getBody().get(1).getNom()).isEqualTo("Doe");
    }

	@Test
	@Transactional 
public void testGetPersonneById() {

    Personne personne = new Personne("John", "Doe");

    // Ajout à la base de données

    Personne savedPersonne = pRepository.save(personne);


	// effectuer une requette Get pour l'endpoint /personnes/{id} avec l'id de la personne ajoutée
    ResponseEntity<Personne> response = restTemplate.getForEntity("/personnes/{id}", Personne.class, savedPersonne.getId());

    // Verifier  HTTP status code est 200 OK
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // Verifier response body est non null
    assertThat(response.getBody()).isNotNull();

    
	// verifier que la response body est conform a l'object personne enregistré
    assertThat(response.getBody().getId()).isEqualTo(savedPersonne.getId());
    assertThat(response.getBody().getPrenom()).isEqualTo(savedPersonne.getPrenom());
    assertThat(response.getBody().getNom()).isEqualTo(savedPersonne.getNom());
}

@Test
@Transactional 
public void deletePersonne() throws Exception {
	// creer un nouvel objet Personne
	Personne newPersonne = new Personne(30,"John", "Doe");
	ResponseEntity<Personne> createResponse = restTemplate.postForEntity("/personnes", newPersonne, Personne.class);
	Personne createdPersonne = createResponse.getBody();
	
	// delete the created Personne object by ID
	// supprimer la personne creée par id
	restTemplate.delete("/personnes/{id}", createdPersonne.getId());
	
	// verifier si l'objet créé a été supprimé
	ResponseEntity<Personne> getResponse = restTemplate.getForEntity("/personnes/{id}", Personne.class, createdPersonne.getId());
	assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
}


@Test
@Transactional 
public void testFindByPrenomContainsIgnoreCaseAndPrenomContainsIgnoreCase() {
  // elements pour tester
  Personne personne1 = new Personne("John", "Doe");
  Personne personne2 = new Personne("Jonen", "Doe");
  Personne personne3 = new Personne("Sarah", "Johnson");
  pRepository.saveAll(List.of(personne1, personne2, personne3));
  
  // retourner le resultat de l'execution de la methode
  List<Personne> personnes = pRepository.findByPrenomContainsIgnoreCaseAndPrenomContainsIgnoreCase("JO", "n");
  
  // verification des outputs
  assertThat(personnes).hasSize(2);
  assertThat(personnes).containsExactlyInAnyOrder(personne1,personne2);
}


@Test
@Transactional 
public void TestFindByNomContainsIgnoreCaseAndNomContainsIgnoreCase() {
  // elements pour tester
  Personne personne1 = new Personne("John", "Doe");
  Personne personne2 = new Personne("Jonen", "Doe");
  Personne personne3 = new Personne("Sarah", "Johnson");
  pRepository.saveAll(List.of(personne1, personne2, personne3));
  
  // retourner le resultat de l'execution de la methode
  List<Personne> personnes = pRepository.findByPrenomContainsIgnoreCaseAndPrenomContainsIgnoreCase("D", "e");
  
  // verification des outputs
  assertThat(personnes).hasSize(2);
  assertThat(personnes).containsExactlyInAnyOrder(personne1,personne2);
}

@Test
@Transactional
public void testPost() throws Exception {
	// Test valid post request
	String requestBody = "{\"id\": 1, \"prenom\": \"John\", \"nom\": \"Doe\"}";
	mockMvc.perform(MockMvcRequestBuilders.post("/personnes")
		.contentType(MediaType.APPLICATION_JSON)
		.content(requestBody))
		.andExpect(MockMvcResultMatchers.status().isCreated())
		.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
		.andExpect(MockMvcResultMatchers.jsonPath("$.prenom").value("John"))
		.andExpect(MockMvcResultMatchers.jsonPath("$.nom").value("Doe"));

	// Check if Personne was saved in the database
	Personne savedPersonne = pRepository.findById(1).orElse(null);
	assertNotNull(savedPersonne);
	assertEquals("John", savedPersonne.getPrenom());
	assertEquals("Doe", savedPersonne.getNom());
}

@Test
@Transactional
public void testUpdatePersonne() {
	// creer une nouvelle entité personne
	Personne newPersonne = new Personne();
	newPersonne.setPrenom("John");
	newPersonne.setNom("Doe");
	Personne savedPersonne = pRepository.save(newPersonne);

	// modifier l 'entité Personne 
	savedPersonne.setPrenom("Jane");
	savedPersonne.setNom("Doe");

		// créer la requette PUT avec l entité mise à jour
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Personne> requestEntity = new HttpEntity<>(savedPersonne, headers);
        ResponseEntity<Personne> responseEntity = restTemplate.exchange(
                "http://localhost:8080/personnes/" + savedPersonne.getId(),
                HttpMethod.PUT,
                requestEntity,
                Personne.class);
	//verifier code status
	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

	// retirer l'entité personne mise à jour de la repo
	Personne updatedPersonne = pRepository.findById(savedPersonne.getId()).orElse(null);

	// verifer si les modifications sont correctes
	assertNotNull(updatedPersonne);
	assertEquals(savedPersonne.getPrenom(), updatedPersonne.getPrenom());
	assertEquals(savedPersonne.getNom(), updatedPersonne.getNom());
}
}
