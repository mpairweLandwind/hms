package com.hms2.converter;



import com.hms2.enums.DoctorStatus; 
import jakarta.persistence.AttributeConverter; 
import jakarta.persistence.Converter;        
import java.util.stream.Stream;

/**
 * Converts DoctorStatus enum to/from String for database persistence.
 * This converter will store the 'text' (display value) of the enum in the database.
 */
@Converter(autoApply = false)
public class DoctorStatusConverter implements AttributeConverter<DoctorStatus, String> {

    @Override
    public String convertToDatabaseColumn(DoctorStatus doctorStatus) {
        if (doctorStatus == null) {
            return null;
        }
        return doctorStatus.getText();
    }

    @Override
    public DoctorStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Stream.of(DoctorStatus.values())
                .filter(status -> status.getText().equals(dbData))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown DoctorStatus value in database: " + dbData));
    }
}