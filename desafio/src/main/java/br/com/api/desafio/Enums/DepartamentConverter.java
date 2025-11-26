package br.com.api.desafio.Enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DepartamentConverter implements AttributeConverter<Departament, String> {
    @Override
    public String convertToDatabaseColumn(Departament attribute) {
        if (attribute == null) return null;
        return attribute.name();
    }

    @Override
    public Departament convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Departament.valueOf(dbData); // converte de volta
    }
}
