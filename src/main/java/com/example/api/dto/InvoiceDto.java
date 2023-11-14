package com.example.api.dto;

import com.example.api.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDto {
  private Integer id;
  private String invoiceReference;
  private Date createdAt;
  private Date paymentDue;
  private String description;
  private Integer paymentTerms;
  private String clientName;
  private String clientEmail;
  private String invoiceStatus;
  private Address senderAddress;
  private Address clientAddress;
  private Float total;
  private List<InvoiceItemDto> invoiceItems;

  public void setInvoiceItems(List<InvoiceItemDto> invoiceItems) {
    this.invoiceItems = invoiceItems;
    calculateTotal();
  }

  private void calculateTotal() {
    if (invoiceItems != null && !invoiceItems.isEmpty()) {
      total =
          invoiceItems.stream()
              .map(InvoiceItemDto::getTotal)
              .filter(java.util.Objects::nonNull)
              .reduce(0f, Float::sum);
    } else {
      total = 0f;
    }
  }
}
