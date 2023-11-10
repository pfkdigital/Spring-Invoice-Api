package com.example.api.mapper;

import com.example.api.dto.InvoiceDto;
import com.example.api.dto.InvoiceItemDto;
import com.example.api.entity.Invoice;
import com.example.api.entity.InvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvoiceMapper {

    @Autowired
    private InvoiceItemMapper invoiceItemMapper;

    public Invoice toEntity(InvoiceDto invoiceDto){
        Invoice invoice = new Invoice();

        invoice.setInvoiceReference(invoiceDto.getInvoiceReference());
        invoice.setCreatedAt(invoiceDto.getCreatedAt());
        invoice.setPaymentDue(invoiceDto.getPaymentDue());
        invoice.setDescription(invoiceDto.getDescription());
        invoice.setPaymentTerms(invoiceDto.getPaymentTerms());
        invoice.setClientName(invoiceDto.getClientName());
        invoice.setClientEmail(invoiceDto.getClientEmail());
        invoice.setInvoiceStatus(invoiceDto.getInvoiceStatus());
        invoice.setSenderAddress(invoiceDto.getSenderAddress());
        invoice.setClientAddress(invoiceDto.getClientAddress());

        if (invoiceDto.getInvoiceItems() != null) {
            invoiceDto.getInvoiceItems().stream().map(invoiceItemMapper::toEntity).forEach(invoice::addInvoiceItem);
        }

        invoice.setTotal(invoiceDto.getTotal());

        return invoice;
    }

    public InvoiceDto toDto(Invoice invoice){
        InvoiceDto invoiceDTO = new InvoiceDto();
        invoiceDTO.setId(invoice.getId());
        invoiceDTO.setInvoiceReference(invoice.getInvoiceReference());
        invoiceDTO.setCreatedAt(invoice.getCreatedAt());
        invoiceDTO.setPaymentDue(invoice.getPaymentDue());
        invoiceDTO.setDescription(invoice.getDescription());
        invoiceDTO.setPaymentTerms(invoice.getPaymentTerms());
        invoiceDTO.setClientName(invoice.getClientName());
        invoiceDTO.setClientEmail(invoice.getClientEmail());
        invoiceDTO.setInvoiceStatus(invoice.getInvoiceStatus());

        // Assuming AddressMapper exists to convert Address to AddressDTO
        invoiceDTO.setSenderAddress(invoice.getSenderAddress());
        invoiceDTO.setClientAddress(invoice.getClientAddress());

        // Convert each InvoiceItem to InvoiceItemDTO
        if (invoice.getInvoiceItems() != null) {
            List<InvoiceItemDto> invoiceItemDtoList = invoice.getInvoiceItems().stream().map(invoiceItemMapper::toDto).toList();
            invoiceDTO.setInvoiceItems(invoiceItemDtoList);
        }

        invoiceDTO.setTotal(invoice.getTotal());

        return invoiceDTO;
    }
}
