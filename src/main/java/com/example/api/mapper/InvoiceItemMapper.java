package com.example.api.mapper;

import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.InvoiceItem;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {
  public InvoiceItem toEntity(InvoiceItemDto invoiceItemDto) {
    return InvoiceItem.builder()
        .name(invoiceItemDto.getName())
        .price(invoiceItemDto.getPrice())
        .quantity(invoiceItemDto.getQuantity())
        .total(invoiceItemDto.getTotal())
        .build();
  }

  public InvoiceItemDto toDto(InvoiceItem invoiceItem) {
    return InvoiceItemDto.builder()
        .id(invoiceItem.getId())
        .name(invoiceItem.getName())
        .price(invoiceItem.getPrice())
        .quantity(invoiceItem.getQuantity())
        .total(invoiceItem.getTotal())
        .build();
  }
}
