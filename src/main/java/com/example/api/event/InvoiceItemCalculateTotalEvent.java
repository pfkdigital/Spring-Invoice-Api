package com.example.api.event;

import com.example.api.entity.InvoiceItem;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostUpdate;

public class InvoiceItemCalculateTotalEvent {
    @PostLoad
    @PostUpdate
    public void recalculateTotal(InvoiceItem invoiceItem){
        invoiceItem.calculateTotal();
    }
}
