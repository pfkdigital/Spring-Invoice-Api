package com.example.api.controller;

import com.example.api.dto.InvoiceDto;
import com.example.api.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @PostMapping
  public ResponseEntity<InvoiceDto> createInvoice(@RequestBody InvoiceDto invoiceDto) {
    return new ResponseEntity<>(invoiceService.createInvoice(invoiceDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<InvoiceDto> updateInvoiceById(
      @PathVariable Integer id, @RequestBody InvoiceDto invoiceDto) {
    return new ResponseEntity<>(invoiceService.updateInvoice(id, invoiceDto), HttpStatus.ACCEPTED);
  }

  @PutMapping("/{id}/update")
  public ResponseEntity<InvoiceDto> updateInvoiceStatusToPaid(@PathVariable Integer id) {
    return new ResponseEntity<>(invoiceService.updateInvoiceStatusToPaid(id), HttpStatus.ACCEPTED);
  }

  @GetMapping
  public ResponseEntity<List<InvoiceDto>> getAllInvoices() {
    return new ResponseEntity<>(invoiceService.getAllInvoices(), HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InvoiceDto> getInvoiceById(@PathVariable Integer id) {
    return new ResponseEntity<>(invoiceService.getInvoiceById(id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteInvoiceById(@PathVariable Integer id) {
    return new ResponseEntity<>(invoiceService.deleteInvoiceById(id), HttpStatus.OK);
  }
}
