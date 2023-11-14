package com.example.api.service;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Address;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import com.example.api.exception.InvoiceNotFoundException;
import com.example.api.mapper.InvoiceMapper;
import com.example.api.repository.InvoiceRepository;
import com.example.api.service.impl.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
  private static InvoiceDto invoiceDto;
  private static Invoice invoice;
  @Mock private InvoiceRepository invoiceRepository;
  @Mock private InvoiceMapper invoiceMapper;
  @InjectMocks private InvoiceServiceImpl invoiceService;

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
  public void InvoiceService_CreateInvoice_ReturnInvoiceDto() {
    when(invoiceMapper.toEntity(Mockito.any(InvoiceDto.class))).thenReturn(invoice);
    when(invoiceRepository.save(Mockito.any(Invoice.class))).thenReturn(invoice);
    when(invoiceMapper.toDto(Mockito.any(Invoice.class))).thenReturn(invoiceDto);

    InvoiceDto savedInvoice = invoiceService.createInvoice(invoiceDto);

    assertNotNull(savedInvoice);
    assertEquals("INV-1234", savedInvoice.getInvoiceReference());
    verify(invoiceRepository).save(Mockito.any(Invoice.class));
  }

  @Test
  public void InvoiceService_GetInvoiceById_ReturnInvoiceDto() {
    int invoiceId = 1;
    when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    when(invoiceMapper.toDto(Mockito.any(Invoice.class))).thenReturn(invoiceDto);

    InvoiceDto selectedInvoice = invoiceService.getInvoiceById(invoiceId);

    assertNotNull(selectedInvoice);
    assertEquals("INV-1234", selectedInvoice.getInvoiceReference());
    verify(invoiceRepository).findById(invoiceId);
  }

  @Test
  public void InvoiceService_GetInvoiceById_ReturnInvoiceNotFoundException() {
    int invoiceId = 1;
    when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

    assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceById(invoiceId));
    verify(invoiceRepository).findById(invoiceId);
  }

  @Test
  public void InvoiceService_GetAllInvoices_ReturnAListOfInvoices() {
    List<Invoice> invoiceList = new ArrayList<>(List.of(invoice));

    when(invoiceRepository.findAll()).thenReturn(invoiceList);
    when(invoiceMapper.toDto(Mockito.any())).thenReturn(invoiceDto);

    List<InvoiceDto> invoiceDtos = invoiceService.getAllInvoices();

    assertNotNull(invoiceDtos);
    assertEquals(1, invoiceDtos.size());
    verify(invoiceRepository).findAll();
  }

  @Test
  public void InvoiceService_UpdateAnInvoice_ReturnUpdatedInvoice() {
    int invoiceId = 1;

    when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    when(invoiceRepository.save(Mockito.any())).thenReturn(invoice);
    when(invoiceMapper.toDto(Mockito.any())).thenReturn(invoiceDto);

    InvoiceDto updatedInvoice = invoiceService.updateInvoice(1, invoiceDto);

    assertNotNull(updatedInvoice);
    verify(invoiceRepository).findById(invoiceId);
    verify(invoiceRepository).save(Mockito.any(Invoice.class));
  }

  @Test
  public void InvoiceService_DeleteInvoiceById_ReturnString() {
    int invoiceId = 1;
    when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));
    doNothing().when(invoiceRepository).delete(Mockito.any(Invoice.class));

    assertAll(() -> invoiceService.deleteInvoiceById(invoiceId));
    verify(invoiceRepository).findById(Mockito.anyInt());
    verify(invoiceRepository).delete(Mockito.any(Invoice.class));
  }
}
