package com.codev.domain.dto.form;

import lombok.Data;

@Data
public class ChallengeDTOForm {

    private String title;

    private String description;

    private Long authorId;

    private String imageURL;

    private Long categoryId;

}
