package com.github.marcosws.servicehub.automotive.domain;

import java.math.BigDecimal;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_automotive_services")
public class AutomotiveOffering {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	private String description;
	
	@Column(nullable = false)
	private Integer durationInMinutes;
	
	@Column(nullable = false)
	private BigDecimal basePrice;
	
	public AutomotiveOffering() {

	}
	
	public AutomotiveOffering(String name, String description, Integer durationInMinutes, BigDecimal basePrice) {
		this.name = name;
		this.description = description;
		this.durationInMinutes = durationInMinutes;
		this.basePrice = basePrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getDurationInMinutes() {
		return durationInMinutes;
	}

	public void setDurationInMinutes(Integer durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}

	public BigDecimal getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutomotiveOffering servico = (AutomotiveOffering) o;
        return Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

}
