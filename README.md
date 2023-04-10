# rest-app-eonix
 si vous n'avez pas consulter les elements httpd.http et schemas.sql dans le dossier resource du projet 
 
 
 ========httpd.http=======


 Get all personnes :
                    GET request    ==>  http://localhost:8080/personnes

Get personne  by id :
                    GET request    ==>  http://localhost:8080/personnes/{id}

Get all personnes :
                    DELETE request   ==>  http://localhost:8080/personnes/{id}

Get all personnes :
                    PUT request    ==>  http://localhost:8080/personnes/{id}

Get all personnes :  les variables fn = firstname = prenom || ln = lastname = nom
                    GET REQUEST    ==>  http://localhost:8080/personnes/search/findByPrenomContainsIgnoreCaseAndPrenomContainsIgnoreCase?{fn}=Ra&ln={ln}

Get all personnes :  les variables fn = firstname = prenom || ln = lastname = nom
                    GET REQUEST    ==>  http://localhost:8080/personnes/search/findByNomContainsIgnoreCaseAndNomContainsIgnoreCase?{fn}=Ra&ln={ln}




la base de données choisie est postgresql ,j'ai tourner la base de données un conteneur docker pour plus d'isolation , portabilité et utilisation de votre portabilité

le dossier docker contient un ficher docker compose , il faut juste executer la commande 'docker-compose up' pour construire l'image postgresql et démarer le service et le fichier schemas.sql contient les requettes sql necessaires pour commencer à faire tourner l'application
=============================

=======schemas.sql===========


-- creer la base de donnée si n'est pas créee
CREATE TABLE IF NOT EXISTS Personne (id SERIAL PRIMARY KEY , prenom varchar(50) NOT NULL, prenom varchar(50) );

-- inserer  des personnes dans la table

insert into personne values (1,'eonix-prenom','eonix-nom');

ou encore on peut ajouter juste dans le fichier application.properties la ligne suivante :
    =>  spring.sql.init.schema-locations=classpath:schemas.sql




as for testing jenkins
