package com.example.api.integration;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Address;
import com.example.api.entity.InvoiceItem;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class InvoiceIntegrationTest {
  @Container
  public static PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>("postgres:13").withInitScript("data/schema.sql");

  private RestTemplate restTemplate;

  @LocalServerPort private Integer port;

  private InvoiceDto invoiceDto;

  @BeforeAll
  public static void beforeAll() {
    container.start();
  }

  @DynamicPropertySource
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
    restTemplate = new RestTemplate();
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
  public void InvoiceControllerIntegration_SetUpDatabase() {
    assertTrue(container.isCreated());
    assertTrue(container.isRunning());
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_CreateInvoice_ReturnInvoiceDto() {
    String baseUrl = "http://localhost:+" + port + "/api/v1/invoices";
    InvoiceDto newInvoice = restTemplate.postForObject(baseUrl, invoiceDto, InvoiceDto.class);

    assertNotNull(newInvoice);
    assertEquals("INV-1234", newInvoice.getInvoiceReference());
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_GetInvoiceById_ReturnAnInvoiceDto() {
    int invoiceId = 1;
    String baseUrl = "http://localhost:+" + port + "/api/v1/invoices/" + invoiceId;
    String returnedInvoiceReference = "RT3080";

    InvoiceDto selectedInvoice = restTemplate.getForObject(baseUrl, InvoiceDto.class);

    assertNotNull(selectedInvoice);
    assertEquals(returnedInvoiceReference, selectedInvoice.getInvoiceReference());
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_GetInvoiceById_ReturnAnInvoiceNotFoundException() {
    int invoiceId = 10;
    String baseUrl = "http://localhost:+" + port + "/api/v1/invoices/" + invoiceId;

    HttpClientErrorException httpClientErrorException =
        assertThrows(
            HttpClientErrorException.class, () -> restTemplate.getForEntity(baseUrl, String.class));

    assertEquals(HttpStatus.NOT_FOUND, httpClientErrorException.getStatusCode());
    assertEquals(
        "Invoice of id 10 was not found", httpClientErrorException.getResponseBodyAsString());
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_GetAllInvoices_ReturnListInvoiceDto() {
    String baseUrl = "http://localhost:+" + port + "/api/v1/invoices";

    InvoiceDto[] allInvoices = restTemplate.getForObject(baseUrl, InvoiceDto[].class);

    assert allInvoices != null;
    assertEquals(7, allInvoices.length);
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_UpdateAnInvoiceById_ReturnUpdateInvoiceDto() {
    int invoiceId = 1;
    String baseUrl = "http://localhost:" + port + "/api/v1/invoices/" + invoiceId;

    InvoiceDto originalInvoice = restTemplate.getForObject(baseUrl, InvoiceDto.class);

    restTemplate.put(baseUrl, invoiceDto, InvoiceDto.class);

    InvoiceDto updatedInvoice = restTemplate.getForObject(baseUrl, InvoiceDto.class);

    assert originalInvoice != null;
    assert updatedInvoice != null;
    assertNotEquals(originalInvoice.getInvoiceReference(), updatedInvoice.getInvoiceReference());
    assertNotNull(updatedInvoice);
    assertEquals(invoiceDto.getInvoiceReference(), updatedInvoice.getInvoiceReference());
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_DeleteAnInvoiceById_ReturnDeleteConfirmationString() {
    int invoiceId = 1;
    String baseUrlAll = "http://localhost:" + port + "/api/v1/invoices";
    String baseUrlId = "http://localhost:" + port + "/api/v1/invoices/" + invoiceId;

    InvoiceDto[] allInvoices = restTemplate.getForObject(baseUrlAll, InvoiceDto[].class);

    restTemplate.delete(baseUrlId, String.class);

    InvoiceDto[] allInvoicesAfterDeletion =
        restTemplate.getForObject(baseUrlAll, InvoiceDto[].class);

    assert allInvoices != null;
    assert allInvoicesAfterDeletion != null;

    assertTrue(allInvoices.length > allInvoicesAfterDeletion.length);
    assertEquals(7, allInvoices.length);
    assertEquals(6, allInvoicesAfterDeletion.length);
  }

  @Test
  @Sql({"/data/schema.sql", "/data/data.sql"})
  public void InvoiceControllerIntegration_UpdateAnInvoiceStatusById_ReturnUpdateInvoiceDto() {
    int invoiceId = 1;
    String baseUrl = "http://localhost:" + port + "/api/v1/invoices/" + invoiceId + "/update";
    String baseUrlId = "http://localhost:" + port + "/api/v1/invoices/" + invoiceId;

    restTemplate.put(baseUrl, InvoiceDto.class);

    InvoiceDto updatedInvoice = restTemplate.getForObject(baseUrlId, InvoiceDto.class);

    assertNotNull(updatedInvoice);
    assertEquals("paid", updatedInvoice.getInvoiceStatus());
  }
}
