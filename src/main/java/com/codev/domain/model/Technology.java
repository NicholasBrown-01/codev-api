package com.codev.domain.model;

import com.codev.domain.dto.form.TechnologyDTOForm;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "tb_technology")
public class Technology extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public UUID id;

    @Column(unique = true)
    private String name;

    @Column
    private String description;

    @Column(name = "documentation_link")
    private String documentationLink;

    @Size(min = 6, max = 6, message = "Color must be a valid 6-digit hexadecimal color.")
    @Column
    private String color;

    public Technology(TechnologyDTOForm technologyDTOForm) {
        this.name = technologyDTOForm.getName();
        this.description = technologyDTOForm.getDescription();
        this.documentationLink = technologyDTOForm.getDocumentationLink();
        this.color = technologyDTOForm.getColor();
    }

    public Technology() {}

    public Technology copyProperties(TechnologyDTOForm technologyDTOForm) {
        // TODO : why not use the helper NullAwareBeanUtilsBean.java?
        if (technologyDTOForm.getName() != null)
            this.name = technologyDTOForm.getName();
        if (technologyDTOForm.getDescription() != null)
            this.description = technologyDTOForm.getDescription();
        if (technologyDTOForm.getDocumentationLink() != null)
            this.documentationLink = technologyDTOForm.getDocumentationLink();
        return this;
    }
}
