package com.example.api.dto;

import com.example.api.entity.Address;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

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

  public InvoiceDto() {}

  public InvoiceDto(
      String invoiceReference,
      Date createdAt,
      Date paymentDue,
      String description,
      Integer paymentTerms,
      String clientName,
      String clientEmail,
      String invoiceStatus,
      Address senderAddress,
      Address clientAddress,
      Float total,
      List<InvoiceItemDto> invoiceItems) {
    this.invoiceReference = invoiceReference;
    this.createdAt = createdAt;
    this.paymentDue = paymentDue;
    this.description = description;
    this.paymentTerms = paymentTerms;
    this.clientName = clientName;
    this.clientEmail = clientEmail;
    this.invoiceStatus = invoiceStatus;
    this.senderAddress = senderAddress;
    this.clientAddress = clientAddress;
    this.total = total;
    this.invoiceItems = invoiceItems;
  }

  public String getInvoiceReference() {
    return invoiceReference;
  }

  public void setInvoiceReference(String invoiceReference) {
    this.invoiceReference = invoiceReference;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getPaymentDue() {
    return paymentDue;
  }

  public void setPaymentDue(Date paymentDue) {
    this.paymentDue = paymentDue;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getPaymentTerms() {
    return paymentTerms;
  }

  public void setPaymentTerms(Integer paymentTerms) {
    this.paymentTerms = paymentTerms;
  }

  public String getClientName() {
    return clientName;
  }

  public void setClientName(String clientName) {
    this.clientName = clientName;
  }

  public String getClientEmail() {
    return clientEmail;
  }

  public void setClientEmail(String clientEmail) {
    this.clientEmail = clientEmail;
  }

  public String getInvoiceStatus() {
    return invoiceStatus;
  }

  public void setInvoiceStatus(String invoiceStatus) {
    this.invoiceStatus = invoiceStatus;
  }

  public Address getSenderAddress() {
    return senderAddress;
  }

  public void setSenderAddress(Address senderAddress) {
    this.senderAddress = senderAddress;
  }

  public Address getClientAddress() {
    return clientAddress;
  }

  public void setClientAddress(Address clientAddress) {
    this.clientAddress = clientAddress;
  }

  public Float getTotal() {
    return total;
  }

  public void setTotal(Float total) {
    this.total = total;
  }

  public List<InvoiceItemDto> getInvoiceItems() {
    return invoiceItems;
  }

  public void setInvoiceItems(List<InvoiceItemDto> invoiceItems) {
    this.invoiceItems = invoiceItems;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void addInvoiceItem(InvoiceItemDto invoiceItem) {
    if (invoiceItems == null) {
      invoiceItems = new ArrayList<>();
    }
    invoiceItems.add(invoiceItem);
  }

  @Override
  public String toString() {
    return "InvoiceDto{"
        + "id="
        + id
        + ", invoiceReference='"
        + invoiceReference
        + '\''
        + ", createdAt="
        + createdAt
        + ", paymentDue="
        + paymentDue
        + ", description='"
        + description
        + '\''
        + ", paymentTerms="
        + paymentTerms
        + ", clientName='"
        + clientName
        + '\''
        + ", clientEmail='"
        + clientEmail
        + '\''
        + ", invoiceStatus='"
        + invoiceStatus
        + '\''
        + ", senderAddress="
        + senderAddress
        + ", clientAddress="
        + clientAddress
        + ", total="
        + total
        + ", invoiceItems="
        + invoiceItems
        + '}';
  }
}
