package com.example.api.entity;

import com.example.api.event.InvoiceCalculateTotalEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoices")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@EntityListeners(InvoiceCalculateTotalEvent.class)
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

  @OneToMany(mappedBy = "invoice", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private List<InvoiceItem> invoiceItems;

  public void addInvoiceItem(InvoiceItem invoiceItem) {
    if (invoiceItems == null) {
      invoiceItems = new ArrayList<>();
    }
    invoiceItem.setInvoice(this);
    invoiceItems.add(invoiceItem);
  }

  public void recalculateTotal() {
    if (invoiceItems != null) {
      total =
          invoiceItems.stream()
              .filter(item -> item.getTotal() != null)
              .map(InvoiceItem::getTotal)
              .reduce(0f, Float::sum);
    } else {
      total = 0f;
    }
  }
}
