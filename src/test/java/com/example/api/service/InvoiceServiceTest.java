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
import org.junit.jupiter.api.BeforeAll;
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
  @Mock private InvoiceRepository invoiceRepository;

  @Mock private InvoiceMapper invoiceMapper;

  @InjectMocks private InvoiceServiceImpl invoiceService;

  private static InvoiceDto invoiceDto;
  private static Invoice invoice;

  @BeforeAll
  public static void setup() {
    Address senderAddress = new Address("123 Sender St", "Sender City", "S123", "Sender Country");
    Address clientAddress = new Address("456 Client Ave", "Client City", "C456", "Client Country");

    // Sample InvoiceItem list
    InvoiceItem item1 = new InvoiceItem("Item 1", 2, 100.0f, 200f); // Example item
    InvoiceItem item2 = new InvoiceItem("Item 2", 3, 200.0f, 600f); // Another example item
    List<InvoiceItem> invoiceItems = Arrays.asList(item1, item2);

    // Sample InvoiceItem list
    InvoiceItemDto item1Dto = new InvoiceItemDto(1, "Item 1", 2, 100.0f, 200f); // Example item
    InvoiceItemDto item2Dto =
        new InvoiceItemDto(1, "Item 2", 3, 200.0f, 600f); // Another example item

    List<InvoiceItemDto> invoiceItemsDto = Arrays.asList(item1Dto, item2Dto);

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

    invoiceDto =
        new InvoiceDto(
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
            invoiceItemsDto // invoiceItems
            );
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
