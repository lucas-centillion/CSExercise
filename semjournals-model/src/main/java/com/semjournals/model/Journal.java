package com.semjournals.model;

import javax.persistence.*;

@Entity
@Table(name ="journal")
public class Journal {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq-gen")
    @SequenceGenerator(name="seq-gen", sequenceName="journal_id_seq", allocationSize=1)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private Account owner;

    @Column(nullable = false)
    private String name;
}
