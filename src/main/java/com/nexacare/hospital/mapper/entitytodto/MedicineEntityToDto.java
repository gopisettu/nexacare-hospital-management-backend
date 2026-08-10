package com.nexacare.hospital.mapper.entitytodto;

import com.nexacare.hospital.dto.response.adminres.MedicineAdminRes;
import com.nexacare.hospital.dto.response.adminres.MedicineResDto;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
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

    public MedicineAdminRes mapMedicineAdminRes(MedicineBatch batch) {

        Medicine medicine = batch.getMedicine();

        MedicineAdminRes res = new MedicineAdminRes();

        res.setId(medicine.getId());
        res.setName(medicine.getName());
        res.setDosage(medicine.getDosage());
        res.setManufacturer(medicine.getManufacturer());
        res.setCategory(medicine.getCategory().toString());
        res.setMedicineForm(medicine.getMedicineForm());
        res.setUnitPrice(medicine.getUnitPrice());

        res.setBatchNo(batch.getBatchNo());
        res.setQuantityReceived(batch.getQuantityReceived());
        res.setQuantityRemaining(batch.getQuantityRemaining());
        res.setExpiryDate(batch.getExpiryDate());
        res.setBatchStatus(batch.getBatchStatus());

        return res;
    }
}