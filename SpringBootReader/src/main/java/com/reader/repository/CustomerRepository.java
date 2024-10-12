package com.reader.repository;

import com.reader.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional //No es necesario aca, se pondria en el service
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

//Cuando trabajo con datos masivos lo mas optimo a utilizar es JpaRepository.
//JpaRepository viene equipado con herramientas que nos van a permitir trabajar
//con lotes.
