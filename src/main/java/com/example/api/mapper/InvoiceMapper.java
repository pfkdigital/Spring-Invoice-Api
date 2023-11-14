package com.example.api.mapper;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Invoice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMapper {

  private final InvoiceItemMapper invoiceItemMapper;

  public InvoiceMapper(InvoiceItemMapper invoiceItemMapper) {
    this.invoiceItemMapper = invoiceItemMapper;
  }

  public Invoice toEntity(InvoiceDto invoiceDto) {
    Invoice invoice =
        Invoice.builder()
            .invoiceReference(invoiceDto.getInvoiceReference())
            .createdAt(invoiceDto.getCreatedAt())
            .paymentDue(invoiceDto.getPaymentDue())
            .description(invoiceDto.getDescription())
            .paymentTerms(invoiceDto.getPaymentTerms())
            .clientName(invoiceDto.getClientName())
            .clientEmail(invoiceDto.getClientEmail())
            .invoiceStatus(invoiceDto.getInvoiceStatus())
            .senderAddress(invoiceDto.getSenderAddress())
            .clientAddress(invoiceDto.getClientAddress())
            .total(invoiceDto.getTotal())
            .build();

    invoiceDto.getInvoiceItems().stream()
        .map(invoiceItemMapper::toEntity)
        .forEach(invoice::addInvoiceItem);

    return invoice;
  }

  public InvoiceDto toDto(Invoice invoice) {
    return InvoiceDto.builder()
        .id(invoice.getId())
        .invoiceReference(invoice.getInvoiceReference())
        .createdAt(invoice.getCreatedAt())
        .paymentDue(invoice.getPaymentDue())
        .description(invoice.getDescription())
        .paymentTerms(invoice.getPaymentTerms())
        .clientName(invoice.getClientName())
        .clientEmail(invoice.getClientEmail())
        .invoiceStatus(invoice.getInvoiceStatus())
        .senderAddress(invoice.getSenderAddress())
        .clientAddress(invoice.getClientAddress())
        .invoiceItems(invoice.getInvoiceItems().stream().map(invoiceItemMapper::toDto).toList())
        .total(invoice.getTotal())
        .build();
  }
}
