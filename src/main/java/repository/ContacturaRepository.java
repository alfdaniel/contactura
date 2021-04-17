package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Contact;

@Repository
public interface ContacturaRepository extends JpaRepository<Contact, Long>{
	
	
}
