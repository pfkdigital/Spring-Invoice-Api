package com.example.api.controller;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Address;
import com.example.api.entity.InvoiceItem;
import com.example.api.exception.InvoiceNotFoundException;
import com.example.api.service.InvoiceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = InvoiceController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class InvoiceControllerTest {
  private InvoiceDto invoiceDto;
  @Autowired private MockMvc mockMvc;
  @MockBean private InvoiceService invoiceService;
  @Autowired private ObjectMapper objectMapper;

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
            .invoiceStatus("paid")
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
  public void InvoiceController_CreateNewInvoice_ReturnCreatedInvoice() throws Exception {
    when(invoiceService.createInvoice(Mockito.any(InvoiceDto.class))).thenReturn(invoiceDto);

    ResultActions response =
        mockMvc.perform(
            post("/api/v1/invoices")
                .content(objectMapper.writeValueAsString(invoiceDto))
                .contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.invoiceReference", CoreMatchers.is(invoiceDto.getInvoiceReference())));
  }

  @Test
  public void InvoiceController_GetAllInvoices_ReturnListOfInvoiceDto() throws Exception {
    List<InvoiceDto> invoiceDtos = new ArrayList<>(List.of(invoiceDto));
    when(invoiceService.getAllInvoices()).thenReturn(invoiceDtos);

    ResultActions response =
        mockMvc.perform(
            get("/api/v1/invoices")
                .content(objectMapper.writeValueAsString(invoiceDtos))
                .contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(invoiceDtos.size())));
  }

  @Test
  public void InvoiceController_GetInvoiceById_ReturnInvoiceDto() throws Exception {
    int invoiceId = 1;
    when(invoiceService.getInvoiceById(invoiceId)).thenReturn(invoiceDto);

    ResultActions response =
        mockMvc.perform(
            get("/api/v1/invoices/{id}", invoiceId)
                .content(objectMapper.writeValueAsString(invoiceDto))
                .contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.invoiceReference", CoreMatchers.is(invoiceDto.getInvoiceReference())));
  }

  @Test
  public void InvoiceController_GetInvoiceById_ReturnInvoiceNotFoundException() throws Exception {
    int invoiceId = 1;
    String expectedResponse = "Invoice of id " + invoiceId + " was not found";
    doThrow(new InvoiceNotFoundException(expectedResponse))
        .when(invoiceService)
        .getInvoiceById(invoiceId);

    ResultActions response =
        mockMvc.perform(
            get("/api/v1/invoices/{id}", invoiceId).contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void InvoiceController_UpdateAnInvoice_ReturnUpdatedInvoice() throws Exception {
    int invoiceId = 1;
    when(invoiceService.updateInvoice(invoiceId, invoiceDto)).thenReturn(invoiceDto);

    ResultActions response =
        mockMvc.perform(
            put("/api/v1/invoices/{id}", invoiceId)
                .content(objectMapper.writeValueAsString(invoiceDto))
                .contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isAccepted());
  }

  @Test
  public void InvoiceController_UpdateAnInvoice_ReturnInvoiceNotFoundException() throws Exception {
    int invoiceId = 1;
    String expectedResponse = "Invoice of id " + invoiceId + " was not found";
    doThrow(new InvoiceNotFoundException(expectedResponse))
        .when(invoiceService)
        .getInvoiceById(invoiceId);

    ResultActions response =
        mockMvc.perform(
            get("/api/v1/invoices/{id}", invoiceId).contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void InvoiceController_UpdateAnInvoiceStatus_ReturnUpdatedInvoice() throws Exception {
    int invoiceId = 1;
    when(invoiceService.updateInvoiceStatusToPaid(invoiceId)).thenReturn(invoiceDto);

    ResultActions response =
        mockMvc.perform(
            put("/api/v1/invoices/{id}/update", invoiceId)
                .contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isAccepted())
        .andExpect(
            MockMvcResultMatchers.jsonPath(
                "$.invoiceReference", CoreMatchers.is(invoiceDto.getInvoiceReference())));
  }

  @Test
  public void InvoiceController_DeleteAnInvoiceById_ReturnString() throws Exception {
    int invoiceId = 1;
    String expectedResponse = "Invoice of id " + invoiceId + " was successfully deleted";

    when(invoiceService.deleteInvoiceById(invoiceId)).thenReturn(expectedResponse);

    ResultActions response =
        mockMvc.perform(
            delete("/api/v1/invoices/{id}", invoiceId).contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
  }

  @Test
  public void InvoiceController_DeleteInvoiceById_ReturnInvoiceNotFoundException()
      throws Exception {
    int invoiceId = 1;
    String expectedResponse = "Invoice of id " + invoiceId + " was not found";
    doThrow(new InvoiceNotFoundException(expectedResponse))
        .when(invoiceService)
        .deleteInvoiceById(Mockito.anyInt());

    ResultActions response =
        mockMvc.perform(
            delete("/api/v1/invoices/{id}", invoiceId).contentType(MediaType.APPLICATION_JSON));

    response
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
  }
}
