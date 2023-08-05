package com.icsd.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CustomerDocuments {
    @Id
    @GeneratedValue
    private int documentuploadid;
    private String UploadedDocumentspath;
    @OneToOne
    @JoinColumn
    private Customer custid;
    private Date uploaddate;
    private String filename;

    public CustomerDocuments(String uploadedDocumentspath, Customer custid, Date uploaddate, String filename) {
        super();
        UploadedDocumentspath = uploadedDocumentspath;
        this.custid = custid;
        this.uploaddate = uploaddate;
        this.filename = filename;
    }


}
