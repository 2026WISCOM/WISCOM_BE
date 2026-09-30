package wiscom.backend.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import wiscom.backend.domain.enums.TeamId;

@Converter(autoApply = true)
public class TeamIdAttributeConverter implements AttributeConverter<TeamId, String> {
    @Override
    public String convertToDatabaseColumn(TeamId attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public TeamId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TeamId.fromValue(dbData);
    }
}
