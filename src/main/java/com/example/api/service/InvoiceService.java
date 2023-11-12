package com.example.api.service;

import com.example.api.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceDto invoiceDto);
    InvoiceDto getInvoiceById(Integer id);
    List<InvoiceDto> getAllInvoices();
    InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto);
}
