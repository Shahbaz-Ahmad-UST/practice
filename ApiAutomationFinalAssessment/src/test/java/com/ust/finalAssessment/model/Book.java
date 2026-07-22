package com.ust.finalAssessment.model;

public record Book(
        String isbn,
        String title,
        String subTitle,
        String author,
        String publish_date,
        String publisher,
        long pages,
        String description,
        String website
) {}