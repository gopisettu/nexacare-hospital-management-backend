package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.doctorres.PrescriptionResDto;
import com.nexacare.hospital.model.PrescriptionItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PrescriptionItemToDtoMapper {
    public PrescriptionResDto mapPrescriptionEntityToDto(PrescriptionItem prescriptionItem){
        return new PrescriptionResDto(
               prescriptionItem.getAppointment().getId(),
                prescriptionItem.getAppointment().getDoctor().getFirstName(),
                prescriptionItem.getMedicine().getName(),
                prescriptionItem.getDosage(),
                prescriptionItem.getFrequency(),
                prescriptionItem.getDurationDays(),
                prescriptionItem.getRoute(),
                prescriptionItem.getInstructions()
        );
    }
}
