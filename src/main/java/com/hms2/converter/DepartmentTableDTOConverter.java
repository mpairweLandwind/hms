package com.hms2.converter;

import com.hms2.dto.department.DepartmentTableDTO;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.util.List;

@FacesConverter("departmentTableDTOConverter")
public class DepartmentTableDTOConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) return null;
        List<DepartmentTableDTO> departments = (List<DepartmentTableDTO>) component.getAttributes().get("departments");
        if (departments != null) {
            for (DepartmentTableDTO dept : departments) {
                if (dept.getId() != null && dept.getId().toString().equals(value)) {
                    return dept;
                }
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) return "";
        if (value instanceof DepartmentTableDTO) {
            DepartmentTableDTO dept = (DepartmentTableDTO) value;
            if (dept.getId() == null) return "";
            return dept.getId().toString();
        }
        if (value instanceof String) {
            return (String) value;
        }
        return "";
    }
} 