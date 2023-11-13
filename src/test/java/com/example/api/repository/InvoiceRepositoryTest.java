package com.example.api.repository;

import com.example.api.entity.Address;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import org.junit.jupiter.api.BeforeAll;
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
  private static Invoice invoice;

  @BeforeAll
  public static void setup() {
    Address senderAddress = new Address("123 Sender St", "Sender City", "S123", "Sender Country");
    Address clientAddress = new Address("456 Client Ave", "Client City", "C456", "Client Country");

    // Sample InvoiceItem list
    InvoiceItem item1 = new InvoiceItem("Item 1", 2, 100.0f, 200f); // Example item
    InvoiceItem item2 = new InvoiceItem("Item 2", 3, 200.0f, 600f); // Another example item
    List<InvoiceItem> invoiceItems = Arrays.asList(item1, item2);

    // Creating an instance of Invoice
    invoice =
        new Invoice(
            "INV-1234", // invoiceReference
            new Date(), // createdAt
            new Date(), // paymentDue (should be a future date)
            "Invoice for services", // description
            30, // paymentTerms
            "Client Inc.", // clientName
            "client@email.com", // clientEmail
            "Pending", // invoiceStatus
            senderAddress, // senderAddress
            clientAddress, // clientAddress
            600.0f, // total (sum of item prices * quantities)
            invoiceItems // invoiceItems
            );
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
