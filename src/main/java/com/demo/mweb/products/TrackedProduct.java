package com.demo.mweb.products;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class TrackedProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id ;

	private String email;
	private String plid;
	private int price; 
	
    @CreationTimestamp
    private LocalDateTime createDateTime;
 
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPlid() {
		return plid;
	}
	public void setPlid(String plid) {
		this.plid = plid;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}
	public LocalDateTime getUpdateDateTime() {
		return updateDateTime;
	}

}