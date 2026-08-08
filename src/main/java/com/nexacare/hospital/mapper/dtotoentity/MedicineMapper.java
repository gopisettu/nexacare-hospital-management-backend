package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.MedicineReq.AddMedicineDto;
import com.nexacare.hospital.dto.response.AdminRes.MedicineAdminRes;
import com.nexacare.hospital.enums.MedicineCategory;
import com.nexacare.hospital.model.Medicine;
import com.nexacare.hospital.model.MedicineBatch;
import org.springframework.stereotype.Component;

@Component
public class MedicineMapper {

    public Medicine mapDtoToMedicine(AddMedicineDto dto) {

        Medicine medicine = new Medicine();

        medicine.setName(dto.name());
        medicine.setDosage(dto.dosage());
        medicine.setManufacturer(dto.manufacturer());
        medicine.setCategory(MedicineCategory.valueOf(dto.category().toString()));
        medicine.setMedicineForm(dto.medicineForm());
        medicine.setUnitPrice(dto.unitPrice());

        return medicine;
    }

    public MedicineBatch mapDtoToMedicineBatch(AddMedicineDto dto) {

        MedicineBatch batch = new MedicineBatch();

        batch.setBatchNo(dto.batchNo());
        batch.setQuantityReceived(dto.quantityReceived());
        batch.setQuantityRemaining(dto.quantityReceived());
        batch.setExpiryDate(dto.expiryDate());

        return batch;
    }

    // Entity -> Response DTO
    public MedicineAdminRes mapMedicineAdminRes(MedicineBatch batch) {

        Medicine medicine = batch.getMedicine();

        MedicineAdminRes res = new MedicineAdminRes();

        res.setId(medicine.getId());
        res.setName(medicine.getName());
        res.setDosage(medicine.getDosage());
        res.setManufacturer(medicine.getManufacturer());


        res.setCategory(medicine.getCategory().name());



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