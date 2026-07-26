package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.AddMedicineDto;
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
        medicine.setCategory(dto.category());
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

        // medicine and batchStatus will be set in the service

        return batch;
    }
}
