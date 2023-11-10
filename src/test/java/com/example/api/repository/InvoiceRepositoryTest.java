package com.example.api.repository;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class InvoiceRepositoryTest {
    @Autowired
    private InvoiceRepository invoiceRepository;

    private static InvoiceDto invoiceDto;
    private static Invoice invoice;

    @BeforeAll
    public static void setup(){
        Address senderAddress = new Address("123 Sender St", "Sender City", "S123", "Sender Country");
        Address clientAddress = new Address("456 Client Ave", "Client City", "C456", "Client Country");

        // Sample InvoiceItem list
        InvoiceItem item1 = new InvoiceItem("Item 1", 2, 100.0f,200f); // Example item
        InvoiceItem item2 = new InvoiceItem("Item 2", 3, 200.0f,600f); // Another example item
        List<InvoiceItem> invoiceItems = Arrays.asList(item1, item2);

    // Sample InvoiceItem list
    InvoiceItemDto item1Dto = new InvoiceItemDto("Item 1", 2, 100.0f, 200f); // Example item
        InvoiceItemDto item2Dto = new InvoiceItemDto("Item 2", 3, 200.0f,600f); // Another example item
        List<InvoiceItemDto> invoiceItemsDto = Arrays.asList(item1Dto, item2Dto);

        // Creating an instance of Invoice
        invoice = new Invoice(
                "INV-1234",      // invoiceReference
                new Date(),      // createdAt
                new Date(),      // paymentDue (should be a future date)
                "Invoice for services", // description
                30,              // paymentTerms
                "Client Inc.",   // clientName
                "client@email.com", // clientEmail
                "Pending",       // invoiceStatus
                senderAddress,   // senderAddress
                clientAddress,   // clientAddress
                600.0f,          // total (sum of item prices * quantities)
                invoiceItems     // invoiceItems
        );

        invoiceDto = new InvoiceDto(
                "INV-1234",      // invoiceReference
                new Date(),      // createdAt
                new Date(),      // paymentDue (should be a future date)
                "Invoice for services", // description
                30,              // paymentTerms
                "Client Inc.",   // clientName
                "client@email.com", // clientEmail
                "Pending",       // invoiceStatus
                senderAddress,   // senderAddress
                clientAddress,   // clientAddress
                600.0f,          // total (sum of item prices * quantities)
                invoiceItemsDto     // invoiceItems
        );
    }

    @Test
    public void InvoiceRepository_CreateInvoice_ReturnInvoice(){
        Invoice savedInvoice = invoiceRepository.save(invoice);

        assertNotNull(savedInvoice);
        assertEquals("INV-1234",savedInvoice.getInvoiceReference());
    }
}
