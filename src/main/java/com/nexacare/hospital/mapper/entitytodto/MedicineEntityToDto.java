package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.AdminRes.MedicineResDto;
import com.nexacare.hospital.model.Medicine;
import org.springframework.stereotype.Component;

@Component
public class MedicineEntityToDto {

    public MedicineResDto toMedicineResDto(Medicine medicine) {

        return new MedicineResDto(
                medicine.getId(),
                medicine.getName(),
                medicine.getDosage(),
                medicine.getManufacturer()
        );
    }
}