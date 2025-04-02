package br.com.bruno.api.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor

public class Books {
    public String id;
    public String title;
    public String author;
    public String genre;
    public Integer yearPublished;
    public Boolean checkedOut;
    public Boolean isPermanentCollection;
    public String createdAt;
}
