package dms.model;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Document implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private User author;
    @OneToMany  // a.k.a. owners/people who can access (rwx) the document
    private Collection<User> users;
    @Column
    private String category;
    @Column
    private String comment;
    @Column
    private String documentName;
    @Column
    private String fileEnding;
    @Column // Keywords/Schluesselwoerter, what is this, scan file for it ?
    private String keywords;
    @Column // the document
    private File file;
    @OneToMany
    private Collection<Changes> documentChanges;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileEnding() {
        return fileEnding;
    }

    public void setFileEnding(String fileEnding) {
        this.fileEnding = fileEnding;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Collection<Changes> getDocumentChanges() {
        return documentChanges;
    }

    public void setDocumentChanges(Collection<Changes> documentChanges) {
        this.documentChanges = documentChanges;
    }
}
