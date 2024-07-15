package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Широта и долгота места проведения события
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "locations")
public class Location {

    /**
     * Идентификатор
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Широта
     * <p>
     * example: 55.754167
     */
    @Column(name = "lat")
    private Float lat;

    /**
     * Долгота
     * <p>
     * example: 37.62
     */
    @Column(name = "lon")
    private Float lon;
}