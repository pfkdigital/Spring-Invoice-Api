package com.example.api.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "invoice_reference")
  private String invoiceReference;

  @Column(name = "created_at")
  private Date createdAt;

  @Column(name = "payment_due")
  private Date paymentDue;

  @Column(name = "description")
  private String description;

  @Column(name = "payment_terms")
  private Integer paymentTerms;

  @Column(name = "client_name")
  private String clientName;

  @Column(name = "client_email")
  private String clientEmail;

  @Column(name = "invoice_status")
  private String invoiceStatus;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "street", column = @Column(name = "sender_street")),
    @AttributeOverride(name = "city", column = @Column(name = "sender_city")),
    @AttributeOverride(name = "postCode", column = @Column(name = "sender_post_code")),
    @AttributeOverride(name = "country", column = @Column(name = "sender_country"))
  })
  private Address senderAddress;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "street", column = @Column(name = "client_street")),
    @AttributeOverride(name = "city", column = @Column(name = "client_city")),
    @AttributeOverride(name = "postCode", column = @Column(name = "client_post_code")),
    @AttributeOverride(name = "country", column = @Column(name = "client_country"))
  })
  private Address clientAddress;

  @Column(name = "total")
  private Float total;

  @OneToMany(
      mappedBy = "invoice",
      fetch = FetchType.EAGER,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private List<InvoiceItem> invoiceItems;

  public Invoice() {}

  public Invoice(
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
      List<InvoiceItem> invoiceItems) {
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

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public List<InvoiceItem> getInvoiceItems() {
    return invoiceItems;
  }

  public void setInvoiceItems(List<InvoiceItem> invoiceItems) {
    this.invoiceItems = invoiceItems;
    calculateTotal();
  }

  public void calculateTotal() {
    long total;

    for (InvoiceItem item : invoiceItems) {
      total = (long) (item.getQuantity() * item.getPrice());
    }
  }

  public void addInvoiceItem(InvoiceItem invoiceItem) {
    if (invoiceItems == null) {
      invoiceItems = new ArrayList<>();
    }
    invoiceItem.setInvoice(this);
    invoiceItems.add(invoiceItem);
  }

  @Override
  public String toString() {
    return "Invoice{"
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
