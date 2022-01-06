package com.microservices.currencyexchangeservice;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CurrencyExchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curr_id")
    private Long id;
    @Column(name = "curr_from")
    private String from;
    @Column(name = "curr_to")
    private String to;
    @Column(name = "curr_multi")
    private BigDecimal conversionMultiple;
    @Column(name = "curr_env")
    private String environment;
}
