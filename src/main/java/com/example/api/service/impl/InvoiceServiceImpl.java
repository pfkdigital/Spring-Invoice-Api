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
  @Transactional
  public InvoiceDto updateInvoice(Integer id, InvoiceDto invoiceDto) {

    Invoice selectedInvoice =
        invoiceRepository
            .findById(id)
            .orElseThrow(
                () -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    selectedInvoice.setInvoiceReference(invoiceDto.getInvoiceReference());
    selectedInvoice.setCreatedAt(invoiceDto.getCreatedAt());
    selectedInvoice.setPaymentDue(invoiceDto.getPaymentDue());
    selectedInvoice.setPaymentTerms(invoiceDto.getPaymentTerms());
    selectedInvoice.setDescription(invoiceDto.getDescription());
    selectedInvoice.setClientName(invoiceDto.getClientName());
    selectedInvoice.setClientEmail(invoiceDto.getClientEmail());
    selectedInvoice.setInvoiceStatus(invoiceDto.getInvoiceStatus());
    selectedInvoice.setSenderAddress(invoiceDto.getSenderAddress());
    selectedInvoice.setSenderAddress(invoiceDto.getSenderAddress());
    selectedInvoice.setTotal(invoiceDto.getTotal());
    selectedInvoice.setInvoiceItems(
        invoiceDto.getInvoiceItems().stream()
            .map(
                item ->
                    new InvoiceItem(
                        item.getName(), item.getQuantity(), item.getPrice(), item.getTotal()))
            .collect(Collectors.toList()));

    Invoice updatedInvoice = invoiceRepository.save(selectedInvoice);

    return invoiceMapper.toDto(updatedInvoice);
  }

  @Override
  public InvoiceDto updateInvoiceStatus(Integer id) {
    Invoice invoiceToBeUpdated = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    invoiceToBeUpdated.setInvoiceReference("Paid");
    Invoice paidInvoice = invoiceRepository.save(invoiceToBeUpdated);

    return invoiceMapper.toDto(paidInvoice);
  }

  @Override
  public String deleteInvoiceById(Integer id) {
    Invoice invoiceToBeDeleted = invoiceRepository.findById(id).orElseThrow(() -> new InvoiceNotFoundException("Invoice of id " + id + " was not found"));

    invoiceRepository.delete(invoiceToBeDeleted);

    return "Invoice of id " + id + " was successfully deleted";
  }
}
