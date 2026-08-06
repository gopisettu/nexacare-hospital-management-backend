package com.nexacare.hospital.mapper;

import com.nexacare.hospital.dto.request.DoctorReq.PrescriptionItemDto;
import com.nexacare.hospital.model.PrescriptionItem;
import org.springframework.stereotype.Component;
@Component
public class PrescriptionMapper {

    public PrescriptionItem mapDtoToEntity(PrescriptionItemDto dto) {

        PrescriptionItem item = new PrescriptionItem();

        item.setDosage(dto.dosage());
        item.setFrequency(dto.frequency());
        item.setDurationDays(dto.durationDays());
        item.setRoute(dto.route());
        item.setInstructions(dto.instruction());
item.setQuantity(dto.quantity());

        return item;
    }
}