package com.nexacare.hospital.mapper.dtotoentity;

import com.nexacare.hospital.dto.request.medicinereq.AddMedicineDto;
import com.nexacare.hospital.dto.response.adminres.MedicineAdminRes;
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
        medicine.setCategory(MedicineCategory.valueOf(dto.category()));
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


    public MedicineAdminRes mapMedicineAdminRes(Medicine medicine) {

        MedicineAdminRes response = new MedicineAdminRes();

        // Medicine details
        response.setId(medicine.getId());
        response.setName(medicine.getName());
        response.setDosage(medicine.getDosage());
        response.setManufacturer(medicine.getManufacturer());

        response.setCategory(
                medicine.getCategory() != null
                        ? medicine.getCategory().name()
                        : null
        );

        response.setMedicineForm(medicine.getMedicineForm());
        response.setUnitPrice(medicine.getUnitPrice());

        // Batch details
        if (medicine.getBatches() != null
                && !medicine.getBatches().isEmpty()) {

            MedicineBatch batch = medicine.getBatches().get(0);

            response.setBatchNo(batch.getBatchNo());
            response.setQuantityReceived(batch.getQuantityReceived());
            response.setQuantityRemaining(batch.getQuantityRemaining());
            response.setExpiryDate(batch.getExpiryDate());
            response.setBatchStatus(batch.getBatchStatus());
        }

        return response;
    }
}