package com.safetynet.alert.controller;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.safetynet.alert.dto.AddressReportDTO;
import com.safetynet.alert.dto.ChildInfoDTO;
import com.safetynet.alert.dto.PersonInfoDTO;
import com.safetynet.alert.model.Person;
import com.safetynet.alert.service.IPersonService;

@RestController
public class PersonController {
	
	private static Logger log = LoggerFactory.getLogger(PersonController.class);
	
	IPersonService personService;
	
	public PersonController(@Autowired IPersonService p_personService) {
		personService = p_personService;
	}
	
	@GetMapping(value="/personInfo")
	public List<PersonInfoDTO> getPersonByFullName(@RequestParam String firstName, @RequestParam String lastName) {
		log.info("GET request /personInfo with param: firstName:{} lastName:{}", firstName, lastName);
		List<PersonInfoDTO> personsByFullName = personService.getPersonInfo(firstName, lastName);
		if(personsByFullName == null || personsByFullName.size() < 1) {
			log.error("Error getting person: {} {}", firstName, lastName);
		}
		log.info("Return person list by fullname: {}", personsByFullName);
		return  personsByFullName;
	}
	
	@GetMapping(value="/communityEmail")
	public List<String> getPersonsEmailByCity(@RequestParam String city){
		log.info("GET request /communityEmail with param: city: {}", city);
		List<String> personsEmailByCity = personService.getPersonEmailByCity(city);
		log.info("Return email list of person by city: {}", personsEmailByCity);
		return personsEmailByCity;
	}
	
	@GetMapping(value = "/childAlert")
	public List<ChildInfoDTO> getChildByAddress(@RequestParam String address){
		log.info("GET request /childAlert with param: address: {}", address);
		List<ChildInfoDTO> childByAddress = personService.getChildByAddress(address);
		log.info("Return child list by address: {}", childByAddress);
		return childByAddress; 
	}
	
	@PostMapping(value = "/person")
	public ResponseEntity<Person> addPerson(@RequestBody Person person) {
		log.info("POST request /person with param: {}", person);
		Person personAdded = personService.add(person);
		if(personAdded == null) {
			log.error("Error while adding person: {}", person);
			return ResponseEntity.noContent().build();
		}
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/personInfo")
				.queryParam("firstName", person.getFirstName())
				.queryParam("lastName", person.getLastName())
				.build().toUri();
		
		log.info("Return response code created at location: {}", location);
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(value="/person")
	public ResponseEntity<Person> updatePerson(@RequestParam(value = "firstName") String firstName,
											@RequestParam(value = "lastName") String lastName,
											@RequestBody Person person) {
		log.info("PUT request /person with param: firstName: {}, lastName: {}, Object: {}", firstName, lastName, person);
		Person editedPerson = personService.update(firstName, lastName, person);
		if(editedPerson == null) {
			log.error("No person found for full name: {} {}", firstName, lastName);
			return ResponseEntity.notFound().build();	
		}
		
		log.info("Return response code OK");
		
		return ResponseEntity.ok().body(editedPerson);
	}
	
	@DeleteMapping(value="/person")
	public ResponseEntity<Person> deletePerson(@RequestParam(value = "firstName") String firstName,
												@RequestParam(value = "lastName") String lastName) { 
		log.info("DELETE request /person with param: firstName: {}, lastName: {}", firstName, lastName);
		Person deletedPerson = personService.delete(firstName, lastName);
		if(deletedPerson == null) {
			log.error("No person found for full name: {} {}", firstName, lastName);
			return ResponseEntity.notFound().build();
		}
		
		log.info("Return response code OK");
		return ResponseEntity.ok().body(deletedPerson);
		
	}
	
	@GetMapping(value="fire")
	public ResponseEntity<AddressReportDTO> getAddressReport(@RequestParam String address) {
		log.info("GET request /fire with param: address: {}", address);
		AddressReportDTO addressReport = personService.getAddressReport(address);
		if(addressReport == null) {
			log.error("No result found for address: {}", address);
			return ResponseEntity.notFound().build();
		}
		
		log.info("Return AddressReport object: {}", addressReport);
		return ResponseEntity.ok(addressReport);
		
	}
	

}
