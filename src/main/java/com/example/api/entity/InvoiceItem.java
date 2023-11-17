package com.example.api.entity;

import com.example.api.event.InvoiceItemCalculateTotalEvent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoice_items")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(InvoiceItemCalculateTotalEvent.class)
public class InvoiceItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "quantity")
  private Integer quantity;

  @Column(name = "price")
  private Float price;

  @Column(name = "total")
  private Float total;

  @ManyToOne()
  @JoinColumn(name = "invoice_id")
  @JsonIgnore
  private Invoice invoice;

  public void calculateTotal() {
    if (this.quantity != null && this.price != null) {
      this.total = this.quantity * this.price;
    }
  }
}
