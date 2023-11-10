package com.example.api.mapper;

import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.InvoiceItem;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemMapper {
    public InvoiceItem toEntity(InvoiceItemDto invoiceItemDto) {
        InvoiceItem newInvoiceItem = new InvoiceItem();
        newInvoiceItem.setName(invoiceItemDto.getName());
        newInvoiceItem.setPrice(invoiceItemDto.getPrice());
        newInvoiceItem.setQuantity(invoiceItemDto.getQuantity());
        return newInvoiceItem;
    }

    public InvoiceItemDto toDto(InvoiceItem invoiceItem) {
        InvoiceItemDto newInvoiceItemDto = new InvoiceItemDto();
        newInvoiceItemDto.setName(invoiceItem.getName());
        newInvoiceItemDto.setPrice(invoiceItem.getPrice());
        newInvoiceItemDto.setQuantity(invoiceItem.getQuantity());
        newInvoiceItemDto.setId(invoiceItem.getId());
        return newInvoiceItemDto;
    }
}
