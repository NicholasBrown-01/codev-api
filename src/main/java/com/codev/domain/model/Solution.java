package com.codev.domain.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tb_solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @Column(name = "repository_url")
    private String repositoryUrl;

    @Column(name = "deploy_url")
    private String deployUrl;

}
