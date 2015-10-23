package com.tweet.user.model;

import lombok.*;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table
public class User implements Serializable{

    @PrimaryKey
    private String username;
    private String name;

}
