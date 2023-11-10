package com.example.api.service;

import com.example.api.dto.InvoiceDto;

public interface InvoiceService {
    InvoiceDto createInvoice(InvoiceDto invoiceDto);
}
