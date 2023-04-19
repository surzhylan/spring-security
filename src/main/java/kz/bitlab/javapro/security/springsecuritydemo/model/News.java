package kz.bitlab.javapro.security.springsecuritydemo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "news")
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "post_date")
    private Date postDate;

    @ManyToOne(fetch = FetchType.EAGER)
    private Users author;

}
