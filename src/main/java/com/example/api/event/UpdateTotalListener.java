package com.example.api.event;

import com.example.api.entity.InvoiceItem;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostUpdate;

public class UpdateTotalListener {
    @PostLoad
    @PostUpdate
    public void updateTotal(InvoiceItem invoiceItem) {
        Float quantity = (float) invoiceItem.getQuantity();
        Float price = invoiceItem.getPrice();
        invoiceItem.setTotal(quantity * price);
    }
}