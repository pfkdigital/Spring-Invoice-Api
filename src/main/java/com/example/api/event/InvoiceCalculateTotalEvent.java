package com.example.api.event;

import com.example.api.entity.Invoice;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostUpdate;

public class InvoiceCalculateTotalEvent {
  @PostLoad
  @PostUpdate
  public void updateTotal(Invoice invoice) {
      invoice.recalculateTotal();
  }
}
