package com.example.api.integration;

import com.example.api.controller.InvoiceController;
import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Address;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import com.example.api.service.InvoiceService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class InvoiceIntegrationTest {
  @Container
  public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13");
  private static RestTemplate restTemplate;
    @Autowired
    private InvoiceService invoiceService;

  private Invoice invoice;
  private InvoiceDto invoiceDto;

  @LocalServerPort private Integer port;

  @BeforeAll
  public static void beforeAll() {
    container.start();
    restTemplate = new RestTemplate();
  }

  public static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }
  @AfterAll
  public static void cleanUp() {
      container.stop();
  }

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

    // Creating an instance of InvoiceDto using the builder
    invoiceDto =
        InvoiceDto.builder()
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
            .invoiceItems(
                invoiceItems.stream()
                    .map(
                        item ->
                            InvoiceItemDto.builder()
                                .id(item.getId())
                                .name(item.getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .total(item.getTotal())
                                .build())
                    .toList())
            .build();
  }

  @Test
  public void testAddNewInvoice(){
    String baseUrl = "http://localhost:8080/api/v1/invoices";
    InvoiceDto newInvoice = restTemplate.postForObject(baseUrl,invoiceDto,InvoiceDto.class);

      assertNotNull(newInvoice);
      assertEquals("INV-1234",newInvoice.getInvoiceReference());
  }
}
