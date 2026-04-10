package com.Sarah.travel_planner.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "destinations")
public class Destination {

    @Id
    private String id;

    private String name;
    private String capital;
    private String region;
    private long population;
    private String currency;
    private String flagUrl;
    private String description;
    private boolean approved;

}
