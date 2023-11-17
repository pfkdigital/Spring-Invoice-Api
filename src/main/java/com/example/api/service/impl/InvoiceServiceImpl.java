package com.example.api.service.impl;

import com.example.api.dto.InvoiceDto;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import com.example.api.exception.InvoiceNotFoundException;
import com.example.api.mapper.InvoiceMapper;
import com.example.api.repository.InvoiceRepository;
import com.example.api.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {
  private final InvoiceRepository invoiceRepository;
  private final InvoiceMapper invoiceMapper;

  public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper) {
    this.invoiceRepository = invoiceRepository;
    this.invoiceMapper = invoiceMapper;
  }

  @Override
  @Transactional
  public InvoiceDto createInvoice(InvoiceDto invoiceDto) {
    Invoice invoiceToBeSaved = invoiceMapper.toEntity(invoiceDto);
    Invoice newInvoice = invoiceRepository.save(invoiceToBeSaved);
    return invoiceMapper.toDto(newInvoice);
  }

  @Override
  public InvoiceDto getInvoiceById(Integer id) {
    Invoice selectedInvoice =
        invoiceRepository
            .findInvoiceWithItemsById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    return invoiceMapper.toDto(selectedInvoice);
  }

  @Override
  public List<InvoiceDto> getAllInvoices() {
    List<Invoice> invoices = invoiceRepository.findAllInvoicesWithItems();

    return invoices.stream().map(invoiceMapper::toDto).toList();
  }

  @Override
  @Transactional
  public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {
    // Retrieve the existing invoice from the database
    Invoice selectedInvoice =
        invoiceRepository
            .findInvoiceWithItemsById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    // Create a new Invoice with updated properties using the builder pattern
    Invoice updatedInvoice =
        Invoice.builder()
            .id(selectedInvoice.getId())
            .invoiceReference(invoiceDto.getInvoiceReference())
            .createdAt(invoiceDto.getCreatedAt())
            .paymentDue(invoiceDto.getPaymentDue())
            .paymentTerms(invoiceDto.getPaymentTerms())
            .description(invoiceDto.getDescription())
            .clientName(invoiceDto.getClientName())
            .clientEmail(invoiceDto.getClientEmail())
            .invoiceStatus(invoiceDto.getInvoiceStatus())
            .senderAddress(invoiceDto.getSenderAddress())
            .clientAddress(invoiceDto.getClientAddress())
            .total(invoiceDto.getTotal())
            .invoiceItems(
                invoiceDto.getInvoiceItems().stream()
                    .map(
                        itemDto ->
                            InvoiceItem.builder()
                                .name(itemDto.getName())
                                .quantity(itemDto.getQuantity())
                                .price(itemDto.getPrice())
                                .total(itemDto.getTotal())
                                .build())
                    .collect(Collectors.toList()))
            .build();

    Invoice savedInvoice = invoiceRepository.save(updatedInvoice);

    return invoiceMapper.toDto(savedInvoice);
  }

  @Override
  @Transactional
  public InvoiceDto updateInvoiceStatusToPaid(Integer id) {
    Invoice invoiceToBeUpdated =
        invoiceRepository
            .findInvoiceWithItemsById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    invoiceToBeUpdated.setInvoiceStatus("paid");
    Invoice paidInvoice = invoiceRepository.save(invoiceToBeUpdated);
    return invoiceMapper.toDto(paidInvoice);
  }

  @Override
  @Transactional
  public String deleteInvoiceById(Integer id) {
    Invoice invoiceToBeDeleted =
        invoiceRepository
            .findInvoiceWithItemsById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    invoiceRepository.delete(invoiceToBeDeleted);

    return "Invoice of id " + id + " was successfully deleted";
  }
}
