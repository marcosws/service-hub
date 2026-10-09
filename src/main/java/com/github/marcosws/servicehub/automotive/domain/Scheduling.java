package com.github.marcosws.servicehub.automotive.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "tb_schedulings")
public class Scheduling {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id", nullable = false)
	private User client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vehicle_id", nullable = false)
	private Vehicle vehicle;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", nullable = false)
	private AutomotiveOffering service;
	
	@Column(nullable = false)
	private LocalDateTime scheduledDateTime;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StatusScheduling status = StatusScheduling.PENDING;
	
	public enum StatusScheduling {
		PENDING,
		CONFIRMED,
		IN_PROGRESS,
		CANCELED,
		COMPLETED
	}
	
	public Scheduling() {
		
	}
	
	public Scheduling(User client, Vehicle vehicle, AutomotiveOffering service, LocalDateTime scheduledDateTime) {
		this.client = client;
		this.vehicle = vehicle;
		this.service = service;
		this.scheduledDateTime = scheduledDateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getClient() {
		return client;
	}

	public void setClient(User client) {
		this.client = client;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public AutomotiveOffering getService() {
		return service;
	}

	public void setService(AutomotiveOffering service) {
		this.service = service;
	}

	public LocalDateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	public void setScheduledDateTime(LocalDateTime scheduledDateTime) {
		this.scheduledDateTime = scheduledDateTime;
	}

	public StatusScheduling getStatus() {
		return status;
	}

	public void setStatus(StatusScheduling status) {
		this.status = status;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scheduling that = (Scheduling) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
	
	

}
