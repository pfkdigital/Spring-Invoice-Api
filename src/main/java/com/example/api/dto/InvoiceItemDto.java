package com.example.api.dto;

public class InvoiceItemDto {
  private Integer id;
  private String name;
  private Integer quantity;
  private Float price;

  private Float total;

  public InvoiceItemDto() {}

  public InvoiceItemDto(String name, Integer quantity, Float price,Float total) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
    this.total = total;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
    setTotal(quantity * getPrice());
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public Float getTotal() {
    return total;
  }

  public void setTotal(Float total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "InvoiceItemDto{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", quantity="
        + quantity
        + ", price="
        + price
        + '}';
  }
}
