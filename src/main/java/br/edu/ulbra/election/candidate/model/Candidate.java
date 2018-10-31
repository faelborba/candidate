package br.edu.ulbra.election.candidate.model;

import javax.persistence.*;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long numberElection;

    @Column(nullable = false)
    private ElectionOutput electionOutput;

    @Column(nullable = false)
    private PartyOutput partyOutput;

    
}
