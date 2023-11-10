package com.example.api.service.impl;

import com.example.api.dto.InvoiceDto;
import com.example.api.entity.Invoice;
import com.example.api.mapper.InvoiceMapper;
import com.example.api.repository.InvoiceRepository;
import com.example.api.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


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
}
