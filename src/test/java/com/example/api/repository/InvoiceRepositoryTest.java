package com.example.api.repository;

import com.example.api.entity.Address;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InvoiceRepositoryTest {
  @Autowired private InvoiceRepository invoiceRepository;
  private Invoice invoice;

  @BeforeEach
  public void setup() {
    // Create a sender address using the builder
    Address senderAddress =
            Address.builder()
                    .street("123 Sender St")
                    .city("Sender City")
                    .postCode("S123")
                    .country("Sender Country")
                    .build();

    // Create a client address using the builder
    Address clientAddress =
            Address.builder()
                    .street("456 Client Ave")
                    .city("Client City")
                    .postCode("C456")
                    .country("Client Country")
                    .build();

    // Sample InvoiceItem list
    List<InvoiceItem> invoiceItems =
            Arrays.asList(
                    InvoiceItem.builder().name("Item 1").quantity(2).price(100.0f).total(200.0f).build(),
                    InvoiceItem.builder().name("Item 2").quantity(3).price(200.0f).total(600.0f).build());

    // Creating an instance of Invoice using the builder
    invoice =
            Invoice.builder()
                    .invoiceReference("INV-1234")
                    .createdAt(new Date())
                    .paymentDue(new Date()) // Should be set to a future date
                    .description("Invoice for services")
                    .paymentTerms(30)
                    .clientName("Client Inc.")
                    .clientEmail("client@email.com")
                    .invoiceStatus("Pending")
                    .senderAddress(senderAddress)
                    .clientAddress(clientAddress)
                    .total(600.0f)
                    .invoiceItems(invoiceItems)
                    .build();
  }

  @Test
  public void InvoiceRepository_CreateInvoice_ReturnInvoice() {
    Invoice savedInvoice = invoiceRepository.save(invoice);

    assertNotNull(savedInvoice);
    assertEquals("INV-1234", savedInvoice.getInvoiceReference());
  }

  @Test
  public void InvoiceRepository_GetInvoiceById_ReturnInvoice() {
    Invoice savedInvoice = invoiceRepository.save(invoice);

    Invoice selectedInvoice = invoiceRepository.findById(invoice.getId()).get();

    assertNotNull(selectedInvoice);
    assertEquals("INV-1234", invoice.getInvoiceReference());
  }

  @Test
  public void InvoiceRepository_GetAllInvoices_ReturnListOfInvoices() {
    invoiceRepository.save(invoice);

    List<Invoice> selectedInvoices = invoiceRepository.findAll();

    assertNotNull(selectedInvoices);
    assertEquals(1, selectedInvoices.size());
  }

  @Test
  public void InvoiceRepository_UpdateAnInvoice_ReturnUpdatedInvoice() {
    invoiceRepository.save(invoice);

    Invoice selectedInvoice = invoiceRepository.findById(invoice.getId()).get();
    selectedInvoice.setInvoiceReference("INV-TEST-REF");

    Invoice updatedInvoice = invoiceRepository.save(selectedInvoice);

    assertNotNull(updatedInvoice);
    assertEquals("INV-TEST-REF", updatedInvoice.getInvoiceReference());
  }

  @Test
  public void InvoiceRepository_DeleteAnInvoiceById_InvoiceDeleted() {
    Invoice savedInvoice = invoiceRepository.save(invoice);

    invoiceRepository.delete(savedInvoice);

    Optional<Invoice> deletedInvoice = invoiceRepository.findById(savedInvoice.getId());

    assertTrue(deletedInvoice.isEmpty());
  }
}
