package com.example.api.service.impl;

import com.example.api.dto.InvoiceDto;
import com.example.api.entity.Invoice;
import com.example.api.exception.InvoiceNotFoundException;
import com.example.api.mapper.InvoiceMapper;
import com.example.api.repository.InvoiceRepository;
import com.example.api.service.InvoiceService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

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
            .findById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    return invoiceMapper.toDto(selectedInvoice);
  }

  @Override
  public List<InvoiceDto> getAllInvoices() {
    List<Invoice> invoices = invoiceRepository.findAll();
    return invoices.stream().map(invoiceMapper::toDto).toList();
  }

  @Override
  public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {
    Invoice invoiceToBeUpdated = invoiceMapper.toEntity(invoiceDto);
    Invoice selectedInvoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    selectedInvoice.setInvoiceReference(invoiceToBeUpdated.getInvoiceReference());
    selectedInvoice.setCreatedAt(invoiceToBeUpdated.getCreatedAt());
    selectedInvoice.setPaymentDue(invoiceToBeUpdated.getPaymentDue());
    selectedInvoice.setDescription(invoiceToBeUpdated.getDescription());
    selectedInvoice.setPaymentTerms(invoiceToBeUpdated.getPaymentTerms());
    selectedInvoice.setClientName(invoiceToBeUpdated.getClientName());
    selectedInvoice.setClientEmail(invoiceToBeUpdated.getClientEmail());
    selectedInvoice.setInvoiceStatus(invoiceToBeUpdated.getInvoiceStatus());
    selectedInvoice.setSenderAddress(invoiceToBeUpdated.getSenderAddress());
    selectedInvoice.setClientAddress(invoiceToBeUpdated.getClientAddress());
    selectedInvoice.setTotal(invoiceToBeUpdated.getTotal());
    selectedInvoice.setInvoiceItems(invoiceToBeUpdated.getInvoiceItems());

    Invoice updatedInvoice = invoiceRepository.save(selectedInvoice);
    return invoiceMapper.toDto(updatedInvoice);
  }
}
