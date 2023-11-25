package com.example.api.repository;

import com.example.api.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
    @Query("SELECT DISTINCT i FROM Invoice i LEFT JOIN FETCH i.invoiceItems order by i.id")
    List<Invoice> findAllInvoicesWithItems();

    @Query("SELECT i FROM Invoice i LEFT JOIN FETCH i.invoiceItems WHERE i.id = :id")
    Optional<Invoice> findInvoiceWithItemsById(@Param("id") Integer id);
}
